package co.vamospues.vamospues.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.helpers.Utils;

public class BudgetActivity extends AppCompatActivity {

    private EditText budgetInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);

        budgetInput = (EditText) findViewById(R.id.budget_input);

        Button searchButton = (Button) findViewById(R.id.search_budget_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String budgetParam = budgetInput.getText().toString();
                if (!TextUtils.isEmpty(budgetParam)) {
                    double budget = Double.valueOf(budgetParam);
                    if(budget >= 5000) {
                        startActivity(new Intent(BudgetActivity.this, BudgetResultsActivity.class).putExtra("budget", budget));
                    } else {
                        Utils.showSnackbar("Debes ingresar un presupuesto minimo de 5.000", BudgetActivity.this, R.id.activity_budget);
                    }
                } else {
                    Utils.showSnackbar("Debes ingresar el presupuesto", BudgetActivity.this, R.id.activity_budget);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
