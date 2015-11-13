package com.example.mreverter.flavor2;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class FlavorChoiseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flavor_choise);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnFlavor1 = (Button) findViewById(R.id.btnF1);
        Button btnFlavor2 = (Button) findViewById(R.id.btnF2);
        Button btnFlavor3 = (Button) findViewById(R.id.btnF3);

        btnFlavor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.example.mreverter.mercadopagoprueba", "com.example.mreverter.mercadopagoprueba.MyItemsActivity"));
                startActivity(intent);
            }
        });
        btnFlavor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startItemsActivity(false);

            }
        });

        btnFlavor3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startItemsActivity(true);
            }
        });
    }

    private void startItemsActivity(boolean useVault) {
        Intent intent = new Intent(FlavorChoiseActivity.this, MyItemsActivity.class);
        intent.putExtra("useVault", useVault);
        startActivity(intent);
    }


}
