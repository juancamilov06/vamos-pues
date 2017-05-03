package co.vamospues.vamospues.main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.database.DatabaseHandler;
import co.vamospues.vamospues.enums.Services;
import co.vamospues.vamospues.helpers.GillSansLightTextView;
import co.vamospues.vamospues.helpers.Queue;
import co.vamospues.vamospues.helpers.Utils;
import co.vamospues.vamospues.models.User;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager manager;
    private LoginManager loginManager;
    private Context context;
    private DatabaseHandler database;
    boolean isReallyDismissed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = LoginActivity.this;
        database = new DatabaseHandler(context);

        manager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        loginManager.registerCallback(manager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                final Dialog dialog = getLoadingDialog();
                dialog.show();

                AccessToken accessToken = loginResult.getAccessToken();
                GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(final JSONObject object, GraphResponse response) {

                        final String mail = object.optString("email");
                        final String firstName = object.optString("first_name");
                        final String lastName = object.optString("last_name");

                        if (mail != null) {
                            StringRequest request = new StringRequest(Request.Method.POST, Services.BASE_URL + Services.AUTH_LOGIN_SERVICE, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try{
                                        JSONObject serverResponse = new JSONObject(response);
                                        boolean success = serverResponse.getBoolean("success");
                                        if (success){
                                            JSONObject responseData = serverResponse.getJSONObject("data");
                                            boolean exists = responseData.getBoolean("exists");
                                            if (exists){
                                                dialog.dismiss();

                                                String token = responseData.getString("token");
                                                String expiration = responseData.getString("expiration");
                                                int id = responseData.getInt("code");

                                                database.setAuthToken(token, expiration, mail, id);

                                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                finish();
                                            } else {

                                                dialog.dismiss();

                                                final Dialog finishDialog = new Dialog(context, R.style.StyledDialog);
                                                finishDialog.setCancelable(false);
                                                View finishDialogView = View.inflate(context, R.layout.dialog_finish_sign_in, null);
                                                finishDialog.setContentView(finishDialogView);

                                                final TextInputEditText phoneInput = (TextInputEditText) finishDialogView.findViewById(R.id.phone_input);

                                                GillSansLightTextView messageLabel = (GillSansLightTextView) finishDialogView.findViewById(R.id.message_label);
                                                messageLabel.setText("Hola " + firstName + ", llena estos datos para finalizar tu registro");

                                                Button finishButton = (Button) finishDialogView.findViewById(R.id.finish_button);
                                                finishButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        finishDialog.dismiss();
                                                        String phone = phoneInput.getText().toString();

                                                        final User user = new User();
                                                        user.setFirstName(firstName);
                                                        user.setLastName(lastName);
                                                        user.setMail(mail);
                                                        user.setPhone(phone);

                                                        dialog.show();
                                                        StringRequest createRequest = new StringRequest(Request.Method.POST, Services.BASE_URL + Services.AUTH_CREATE_SERVICE, new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                dialog.dismiss();
                                                                try{
                                                                    JSONObject responseObject = new JSONObject(response);
                                                                    boolean success = responseObject.getBoolean("success");
                                                                    if (success){
                                                                        String token = responseObject.getJSONObject("data").getString("token");
                                                                        String expiration = responseObject.getJSONObject("data").getString("expiration");
                                                                        int id = responseObject.getJSONObject("data").getInt("code");
                                                                        database.setAuthToken(token, expiration, mail, id);
                                                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                                        finish();
                                                                    } else {
                                                                        Utils.showSnackbar("Ha ocurrido un error interno, intenta luego", LoginActivity.this, R.id.activity_login);
                                                                        LoginManager.getInstance().logOut();
                                                                    }
                                                                } catch (Exception e){
                                                                    e.printStackTrace();
                                                                    Utils.showSnackbar("Ha ocurrido un error interno, intenta luego", LoginActivity.this, R.id.activity_login);
                                                                    LoginManager.getInstance().logOut();
                                                                }
                                                            }
                                                        }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                dialog.dismiss();
                                                                Utils.showSnackbar("Ha ocurrido un error interno, intenta luego", LoginActivity.this, R.id.activity_login);
                                                                LoginManager.getInstance().logOut();
                                                            }
                                                        }){
                                                            @Override
                                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                                Map<String, String> params = new HashMap<>();
                                                                JSONObject data = getJSONUser(user);
                                                                if (data != null) {
                                                                    params.put("user", data.toString());
                                                                }
                                                                return params;
                                                            }
                                                        };
                                                        Queue.getInstance(context).addToRequestQueue(createRequest);
                                                    }
                                                });
                                                Button cancelButton = (Button) finishDialogView.findViewById(R.id.cancel_button);
                                                cancelButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        isReallyDismissed = true;
                                                        finishDialog.dismiss();
                                                    }
                                                });

                                                finishDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                    @Override
                                                    public void onDismiss(DialogInterface dialog) {
                                                        if (isReallyDismissed) {
                                                            LoginManager.getInstance().logOut();
                                                        }
                                                    }
                                                });

                                                finishDialog.show();
                                            }
                                        } else {
                                            dialog.dismiss();
                                            Utils.showSnackbar("Ha ocurrido un error interno, intenta luego", LoginActivity.this, R.id.activity_login);
                                            LoginManager.getInstance().logOut();
                                        }
                                    } catch (Exception e){
                                        dialog.dismiss();
                                        LoginManager.getInstance().logOut();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    dialog.dismiss();
                                    LoginManager.getInstance().logOut();
                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("mail", mail);
                                    return params;
                                }
                            };
                            Queue.getInstance(context).addToRequestQueue(request);
                        } else {
                            Utils.showSnackbar("Error ingresando, intenta luego", LoginActivity.this, R.id.activity_login);
                            LoginManager.getInstance().logOut();
                            dialog.dismiss();
                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "email, first_name, last_name");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                Utils.showSnackbar("Has cancelado el ingreso", LoginActivity.this, R.id.activity_login);
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this,
                        "Error: "
                                + error.getMessage()
                        , Toast.LENGTH_SHORT).show();
            }
        });

        Button facebookLoginButton = (Button) findViewById(R.id.login_button);
        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    loginManager.logOut();
                } else {
                    loginManager.logInWithReadPermissions(LoginActivity.this, Collections.singletonList("public_profile"));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        manager.onActivityResult(requestCode, resultCode, data);
    }

    private JSONObject getJSONUser(User user){
        try {
            JSONObject object = new JSONObject();
            object.put("first_name", user.getFirstName());
            if (!TextUtils.isEmpty(user.getLastName())) {
                object.put("last_name", user.getLastName());
            } else {
                object.put("last_name", "");
            }
            object.put("phone", user.getPhone());
            object.put("mail", user.getMail());
            return object;
        } catch (Exception e){
            return null;
        }
    }

    private Dialog getLoadingDialog(){
        Dialog dialog = Utils.getAlertDialog(context);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}

