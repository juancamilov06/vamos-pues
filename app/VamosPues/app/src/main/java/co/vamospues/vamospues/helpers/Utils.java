package co.vamospues.vamospues.helpers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.database.DatabaseHandler;
import co.vamospues.vamospues.main.LoginActivity;
import co.vamospues.vamospues.models.Place;
import co.vamospues.vamospues.models.User;

/**
 * Created by Manuela Duque M on 16/03/2017.
 */

public class Utils {

    public static Dialog getAlertDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.AppTheme_LoadDialog);
        View view = View.inflate(context, R.layout.dialog_loading, null);
        dialog.setContentView(view);
        AVLoadingIndicatorView loadingIndicatorView = (AVLoadingIndicatorView) dialog.findViewById(R.id.indicator);
        loadingIndicatorView.smoothToShow();

        return dialog;
    }

    public static Dialog getExpiredDialog(final AppCompatActivity context){

        final Dialog dialog = new Dialog(context, R.style.StyledDialog);
        View view = View.inflate(context, R.layout.dialog_token_expired, null);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);

        Button acceptButton = (Button) view.findViewById(R.id.finish_button);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseHandler database = new DatabaseHandler(context.getApplicationContext());
                database.deleteToken();
                LoginManager.getInstance().logOut();

                dialog.dismiss();
                context.finish();
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        });

        return dialog;
    }

    public static void showSnackbar(String message, Activity context, int id) {
        Snackbar.make(context.findViewById(id), message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public static String getFormat(double price){
        DecimalFormat format = new DecimalFormat("###,###.##");
        return format.format(price);
    }

    public static String generateOrderId(Place place, int userId){
        Calendar calendar = new GregorianCalendar();
        return String.valueOf(calendar.get(Calendar.YEAR)) + String.valueOf(calendar.get(Calendar.MONTH)) + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))
                + String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + String.valueOf(calendar.get(Calendar.MINUTE)) + String.valueOf(calendar.get(Calendar.SECOND))
                + String.valueOf(place.getId()) + String.valueOf(userId);
    }

    public static boolean isSessionActive(Context context){
        DatabaseHandler database = new DatabaseHandler(context);
        return AccessToken.getCurrentAccessToken() != null && Long.valueOf(database.getExpirationDate()) > System.currentTimeMillis();
    }

}
