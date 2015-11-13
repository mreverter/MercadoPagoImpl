package com.example.mreverter.mercadopagoprueba;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.mercadopago.model.Issuer;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.Token;
import com.mercadopago.util.JsonUtil;

import model.ItemView;

public class PaymentResultActivity extends AppCompatActivity {

    private ItemView mItem;
    private Issuer mIssuer;
    private PaymentMethod mPaymentMethod;
    private int installments;
    private Token mToken;

    private FloatingActionButton fab;

    private TextView txtData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_result);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restarTestApp();
            }
        });
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle(R.string.title_activity_payment_result);
        this.txtData = (TextView) findViewById(R.id.txtData);

        this.mItem = JsonUtil.getInstance().fromJson(getIntent().getStringExtra("item"), ItemView.class);
        this.mIssuer = JsonUtil.getInstance().fromJson(getIntent().getStringExtra("issuer"), Issuer.class);
        this.mPaymentMethod = JsonUtil.getInstance().fromJson(getIntent().getStringExtra("paymentMethod"), PaymentMethod.class);
        this.installments = getIntent().getExtras().getInt("installments");
        this.mToken = JsonUtil.getInstance().fromJson(getIntent().getStringExtra("token"), Token.class);

        this.txtData.setText(String.format("Item: %s por %s en %d cuotas  con issuer %s mediante %s.  Su token es: %s",
                mItem.getDescription(), mItem.getPrice(), installments, mIssuer.getName(), mPaymentMethod.getName(), mToken.getId()));

    }

    private void restarTestApp() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.example.mreverter.flavor2", "com.example.mreverter.flavor2.FlavorChoiseActivity"));
        startActivity(intent);
    }

}
