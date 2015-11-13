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
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.util.LayoutUtil;

import java.util.List;

import adapters.PaymentMethodAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyPaymentMethodsListActivity extends ListActivity {

    private final String PUBLIC_KEY = "TEST-4021c912-cdfa-4595-b37b-59b92765b663";
    private List<PaymentMethod> mPaymentMethods;
    private String descripcion;
    private float precio;

    private Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_payment_methods_list);
        mActivity = this;
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle(R.string.title_activity_my_payment_methods_list);
        LayoutUtil.showProgressLayout(this);
        this.getPaymentMethodsAsync(PUBLIC_KEY);
    }

    private void getPaymentMethodsAsync(String merchantPublicKey) {

        MercadoPago mercadoPago = new MercadoPago.Builder()
                .setContext(this)
                .setPublicKey(merchantPublicKey)
                .build();


        mercadoPago.getPaymentMethods(new Callback<List<PaymentMethod>>() {
            @Override
            public void success(List<PaymentMethod> paymentMethods, Response response) {
                populatePaymentMethodsList(paymentMethods);
                LayoutUtil.showRegularLayout(mActivity);
            }

            @Override
            public void failure(RetrofitError error) {
                // TODO: Manage api failure
            }
        });
    }

    private void populatePaymentMethodsList(List<PaymentMethod> paymentMethods) {

        mPaymentMethods = paymentMethods;
        ArrayAdapter<PaymentMethod> myAdapter = new PaymentMethodAdapter(this, R.layout.payment_methods_row_layout, paymentMethods);
        setListAdapter(myAdapter);

    }

    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);

        PaymentMethod selectedMethod = (PaymentMethod) getListView().getItemAtPosition(position);

        Class nextActivity;
        if(selectedMethod.isIssuerRequired())
            nextActivity = IssuersActivity.class;
        else
            nextActivity = CardDataActivity.class;

        Intent intent = new Intent(MyPaymentMethodsListActivity.this, nextActivity);

        intent.putExtras(getIntent());
        intent.putExtra("paymentMethod", JsonUtil.getInstance().toJson(selectedMethod));
        intent.putExtra("merchantPublicKey", PUBLIC_KEY);
        startActivity(intent);
    }

}
