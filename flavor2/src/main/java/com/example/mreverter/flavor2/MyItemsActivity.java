package com.example.mreverter.flavor2;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mercadopago.core.MercadoPago;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.util.LayoutUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import adapters.ItemsAdapter;
import model.ItemView;
import utils.PayUtils;

public class MyItemsActivity extends ListActivity {

    private List<ItemView> mItems;
    private Activity mActivity;
    private boolean useVault;

    protected List<String> mSupportedPaymentTypes = new ArrayList<String>(){{
        add("credit_card");
        add("debit_card");
        add("prepaid_card");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_items);
        mActivity = this;
        useVault = getIntent().getBooleanExtra("useVault", false);
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle(R.string.title_activity_my_items);
        this.populateItemsList();
    }

    private void beginBuy(ItemView item) {

        if(this.useVault) {
            new MercadoPago.StartActivityBuilder()
                    .setActivity(this)
                    .setPublicKey(getResources().getString(R.string.mp_public_key))
                    .setMerchantBaseUrl(PayUtils.DUMMY_MERCHANT_BASE_URL)
                    .setMerchantGetCustomerUri(PayUtils.DUMMY_MERCHANT_GET_CUSTOMER_URI)
                    .setMerchantAccessToken(PayUtils.DUMMY_MERCHANT_ACCESS_TOKEN)
                    .setAmount(item.getPrice())
                    .setSupportedPaymentTypes(mSupportedPaymentTypes)
                    .setShowBankDeals(true)
                    .startVaultActivity();
        }
        else {
            Intent intent = new Intent(MyItemsActivity.this, FakeVoultActivity.class);
            intent.putExtra("item", JsonUtil.getInstance().toJson(item));
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MercadoPago.VAULT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void populateItemsList() {
        List<ItemView> items = new ArrayList<>();
        items.add(new ItemView("Mate barato", new BigDecimal(20), "http://www.facepopular.net/file/pic/user/42816_50_square.jpg"));
        items.add(new ItemView("Mate caro", new BigDecimal(150.50), "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xfa1/v/t1.0-1/p50x50/10455767_885577911457402_2785984785430573779_n.png?oh=c51b3ad04870be021f86af0b2da7b6f6&oe=56C2B708&__gda__=1455185240_4e43a1c2c3e9180e95266dd1feee6d1e"));
        items.add(new ItemView("Termo Lumilagro", new BigDecimal(85), "http://tumenajedecocina.es/media/catalog/product/cache/1/thumbnail/50x/9df78eab33525d08d6e5fb8d27136e95/t/e/termo_de_cocina.jpg"));
        items.add(new ItemView("Termo Thermo", new BigDecimal(120), "http://raicescriollas.com.ar/media/catalog/product/cache/1/thumbnail/50x/9df78eab33525d08d6e5fb8d27136e95/s/e/set102o.jpg"));

        this.mItems = items;
        LayoutUtil.showProgressLayout(mActivity);
        ArrayAdapter<ItemView> adapter = new ItemsAdapter(this, R.layout.item_row_layout, items);
        setListAdapter(adapter);
        LayoutUtil.showRegularLayout(this);
    }
    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);

        ItemView selectedItem = (ItemView) getListView().getItemAtPosition(position);

        this.beginBuy(selectedItem);

    }

}
