package com.example.mreverter.flavor2;

import android.app.Activity;
import android.content.Intent;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.mercadopago.core.MercadoPago;
import com.mercadopago.model.CardToken;
import com.mercadopago.model.Installment;
import com.mercadopago.model.Issuer;
import com.mercadopago.model.PayerCost;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.Token;
import com.mercadopago.util.ApiUtil;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.util.LayoutUtil;
import com.mercadopago.util.MercadoPagoUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import model.ItemView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FakeVoultActivity extends AppCompatActivity {

    private MercadoPago.StartActivityBuilder startActivityBuilder;
    private MercadoPago mMercadoPago;
    protected List<String> mSupportedPaymentTypes = new ArrayList<String>(){{
        add("credit_card");
        add("debit_card");
        add("prepaid_card");
    }};

    private TextView textMessage;
    private String mPublicKey;
    private ItemView mItem;
    private CardToken mCardToken;
    private Issuer mIssuer;
    private PaymentMethod mPaymentMethod;
    private PayerCost mPayerCost;

    private Activity mActivity;
    private Token mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_voult);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restarTestApp();
            }
        });

        textMessage = (TextView) findViewById(R.id.message);
        mActivity = this;
        mPublicKey = getResources().getString(R.string.mp_public_key);
        mItem = JsonUtil.getInstance().fromJson(getIntent().getStringExtra("item"), ItemView.class);
        mMercadoPago = new MercadoPago.Builder()
                .setContext(this)
                .setPublicKey(mPublicKey)
                .build();

        startActivityBuilder =
                new MercadoPago.StartActivityBuilder()
                        .setActivity(this)
                        .setPublicKey(mPublicKey)
                        .setSupportedPaymentTypes(mSupportedPaymentTypes);

        startActivityBuilder.setAmount(mItem.getPrice());
        startActivityBuilder.startPaymentMethodsActivity();
    }

    private void restarTestApp() {
        Intent intent = new Intent(FakeVoultActivity.this, FlavorChoiseActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MercadoPago.PAYMENT_METHODS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                mPaymentMethod = JsonUtil.getInstance().fromJson(data.getStringExtra("paymentMethod"), PaymentMethod.class);
                startActivityBuilder.setPaymentMethod(mPaymentMethod);
                if(mPaymentMethod.isIssuerRequired()){
                    startActivityBuilder.startIssuersActivity();
                }
                else {
                    startActivityBuilder.startNewCardActivity();
                }
            }
        }

        else if (requestCode == MercadoPago.ISSUERS_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                mIssuer = JsonUtil.getInstance().fromJson(data.getStringExtra("issuer"), Issuer.class);
                startActivityBuilder.startNewCardActivity();
            }
        }

        else if (requestCode == MercadoPago.NEW_CARD_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mCardToken = JsonUtil.getInstance().fromJson(data.getStringExtra("cardToken"), CardToken.class);
                createTokenAsync();

                getInstallmentsAsync();
            }
        }

        else if (requestCode == MercadoPago.INSTALLMENTS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mPayerCost = JsonUtil.getInstance().fromJson(data.getStringExtra("payerCost"), PayerCost.class);
                this.showCollectedData();
            }
        }


    }

    private void showCollectedData() {

        String message = String.format("Item: %s por %s en %d cuotas  con issuer %s mediante %s.  Su token es: %s",
                mItem.getDescription(), mItem.getPrice(), mPayerCost.getInstallments(), mIssuer.getName(), mPaymentMethod.getName(), mToken.getId());

        this.textMessage.setText(message);
    }

    private void getInstallmentsAsync() {

        LayoutUtil.showProgressLayout(this);
        String bin = mCardToken.getCardNumber().substring(0, 6);

        mMercadoPago.getInstallments(bin, mItem.getPrice(), mIssuer.getId(), mPaymentMethod.getPaymentTypeId(), new Callback<List<Installment>>() {
            @Override
            public void success(List<Installment> installments, Response response) {
                startActivityBuilder.setPayerCosts(installments.get(0).getPayerCosts());
                LayoutUtil.showRegularLayout(mActivity);
                startActivityBuilder.startInstallmentsActivity();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void createTokenAsync() {

        LayoutUtil.showProgressLayout(mActivity);

        mMercadoPago.createToken(mCardToken, new Callback<Token>() {
            @Override
            public void success(Token token, Response response) {
                mToken = token;
            }

            @Override
            public void failure(RetrofitError error) {
                ApiUtil.finishWithApiException(mActivity, error);
            }
        });
    }
}

