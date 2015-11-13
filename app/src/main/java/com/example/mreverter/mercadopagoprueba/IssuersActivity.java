package com.example.mreverter.mercadopagoprueba;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mercadopago.core.MercadoPago;
import com.mercadopago.model.Issuer;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.util.LayoutUtil;

import java.util.List;

import adapters.IssuersAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class IssuersActivity extends ListActivity {

    private Activity mActivity;
    private MercadoPago mMercadoPago;
    private PaymentMethod mSelectedPaymentMethod;
    private String mMerchantPublicKey;
    private List<Issuer> mIssuers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issuers);
        mActivity = this;
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle(R.string.title_activity_issuers);

        mMerchantPublicKey = getIntent().getStringExtra("merchantPublicKey");
        mSelectedPaymentMethod = JsonUtil.getInstance().fromJson(getIntent().getStringExtra("paymentMethod"), PaymentMethod.class);

        mMercadoPago = new MercadoPago.Builder()
                .setContext(this)
                .setPublicKey(mMerchantPublicKey)
                .build();

        LayoutUtil.showProgressLayout(this);
        getIssuersAsync(mSelectedPaymentMethod, this.mMerchantPublicKey);

    }

    private void getIssuersAsync(PaymentMethod mSelectedPaymentMethod, String merchantPublicKey) {

        mMercadoPago.getIssuers(mSelectedPaymentMethod.getId(), new Callback<List<Issuer>>() {
            @Override
            public void success(List<Issuer> issuers, Response response) {
                populateIssuersList(issuers);
                LayoutUtil.showRegularLayout(mActivity);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    private void populateIssuersList(List<Issuer> issuers) {
        mIssuers = issuers;
        ArrayAdapter<Issuer> myAdapter = new IssuersAdapter(this, R.layout.issuers_row_layout, issuers);
        setListAdapter(myAdapter);
    }

    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);Intent intent = new Intent(IssuersActivity.this, CardDataActivity.class);

        intent.putExtras(getIntent());
        intent.putExtra("issuer", JsonUtil.getInstance().toJson(this.mIssuers.get(position)));
        startActivity(intent);
    }

}
