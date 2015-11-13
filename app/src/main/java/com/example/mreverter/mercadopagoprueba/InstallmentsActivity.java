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
import com.mercadopago.model.Installment;
import com.mercadopago.model.Issuer;
import com.mercadopago.model.PayerCost;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.util.LayoutUtil;

import java.math.BigDecimal;
import java.util.List;

import adapters.PayerCostsAdapter;
import model.ItemView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class InstallmentsActivity extends ListActivity {

    private MercadoPago mMercadoPago;
    private PaymentMethod mPaymentMethod;
    private Issuer mIssuer;
    private BigDecimal mAmount;
    private String mMerchantPublicKey;
    private List<Installment> mInstallments;
    private String mBin;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installments);
        this.mActivity = this;
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle(R.string.title_activity_installments);
        mMerchantPublicKey = getIntent().getStringExtra("merchantPublicKey");
        mPaymentMethod = JsonUtil.getInstance().fromJson(getIntent().getStringExtra("paymentMethod"), PaymentMethod.class);
        mIssuer = JsonUtil.getInstance().fromJson(getIntent().getStringExtra("issuer"), Issuer.class);
        mBin = getIntent().getStringExtra("bin");
        mAmount = JsonUtil.getInstance().fromJson(getIntent().getStringExtra("item"), ItemView.class).getPrice();
        
        mMercadoPago = new MercadoPago.Builder()
                .setContext(this)
                .setPublicKey(mMerchantPublicKey)
                .build();

        LayoutUtil.showProgressLayout(this);
        this.getInstallmentsAsync();

    }

    private void getInstallmentsAsync() {
        mMercadoPago.getInstallments(mBin, mAmount, mIssuer.getId(), mPaymentMethod.getPaymentTypeId(), new Callback<List<Installment>>() {
            @Override
            public void success(List<Installment> installments, Response response) {
                populateInstallmentsList(installments);
                LayoutUtil.showRegularLayout(mActivity);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void populateInstallmentsList(List<Installment> installments) {
        this.mInstallments = installments;
        List<PayerCost> payerCosts = installments.get(0).getPayerCosts();
        ArrayAdapter<PayerCost> myAdapter = new PayerCostsAdapter(this, R.layout.payer_costs_row_layout, payerCosts);
        setListAdapter(myAdapter);
    }

    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);

        Intent intent = new Intent(InstallmentsActivity.this, PaymentResultActivity.class);

        intent.putExtras(getIntent());
        intent.putExtra("installments", this.mInstallments.get(0).getPayerCosts().get(position).getInstallments());
        startActivity(intent);
    }
}
