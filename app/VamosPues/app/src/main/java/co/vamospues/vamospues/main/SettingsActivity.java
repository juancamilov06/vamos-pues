package co.vamospues.vamospues.main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.database.DatabaseHandler;
import co.vamospues.vamospues.models.Prefs;

public class SettingsActivity extends AppCompatActivity {

    private LinearLayout generalLayout, languagesLayout;
    private Context context;
    private DatabaseHandler database;
    private AppCompatCheckBox notificationsBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);

        context = SettingsActivity.this;
        database = new DatabaseHandler(context);

        LinearLayout termsHeader = (LinearLayout) findViewById(R.id.terms_header);
        termsHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, TermsActivity.class));
            }
        });

        LinearLayout aboutHeader = (LinearLayout) findViewById(R.id.about_header);
        aboutHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, AboutActivity.class));
            }
        });

        generalLayout = (LinearLayout) findViewById(R.id.general_layout);
        LinearLayout generalHeader = (LinearLayout) findViewById(R.id.general_header);
        generalHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (generalLayout.getVisibility() == View.VISIBLE){
                    generalLayout.setVisibility(View.GONE);
                } else {
                    generalLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        languagesLayout = (LinearLayout) findViewById(R.id.languages_layout);
        LinearLayout languagesHeader = (LinearLayout) findViewById(R.id.languages_header);
        languagesHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (languagesLayout.getVisibility() == View.VISIBLE){
                    languagesLayout.setVisibility(View.GONE);
                } else {
                    languagesLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        notificationsBox = (AppCompatCheckBox) findViewById(R.id.notifications_box);
        notificationsBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationsBox.setChecked(!notificationsBox.isChecked());
                if (notificationsBox.isChecked()){
                    final Dialog dialog = new Dialog(context, R.style.StyledDialog);
                    View view  = View.inflate(context, R.layout.dialog_confirm_notify, null);
                    dialog.setContentView(view);

                    Button confirmButton = (Button) view.findViewById(R.id.finish_button);
                    confirmButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Prefs prefs = database.getPrefs();
                            prefs.setNotify(false);
                            database.insertPrefs(prefs);
                            notificationsBox.setChecked(false);
                            dialog.dismiss();
                        }
                    });

                    Button cancelButton = (Button) view.findViewById(R.id.cancel_button);
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            notificationsBox.setChecked(true);
                            dialog.dismiss();
                        }
                    });

                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            notificationsBox.setChecked(true);
                        }
                    });

                    dialog.show();
                } else {
                    Prefs prefs = database.getPrefs();
                    prefs.setNotify(true);
                    database.insertPrefs(prefs);
                    notificationsBox.setChecked(true);
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setNotificationsCheckBox();
    }

    private void setNotificationsCheckBox(){
        Prefs prefs = database.getPrefs();
        if (prefs.isNotify()){
            notificationsBox.setChecked(true);
            return;
        }
        notificationsBox.setChecked(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
