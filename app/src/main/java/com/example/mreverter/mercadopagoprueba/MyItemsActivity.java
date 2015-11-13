package com.example.mreverter.mercadopagoprueba;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mercadopago.util.JsonUtil;
import com.mercadopago.util.LayoutUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import adapters.ItemsAdapter;
import model.ItemView;

public class MyItemsActivity extends ListActivity {

    private List<ItemView> mItems;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_items);
        mActivity = this;
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle(R.string.title_activity_my_items);
        this.populateItemsList();
    }

    private void beginBuy(ItemView item) {
        Intent intent = new Intent(MyItemsActivity.this, MyPaymentMethodsListActivity.class);
        intent.putExtra("item", JsonUtil.getInstance().toJson(item));
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_flavour3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
