package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.mreverter.mercadopagoprueba.R;
import com.mercadopago.util.LayoutUtil;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.List;

import model.ItemView;

/**
 * Created by mreverter on 12/11/15.
 */
public class ItemsAdapter extends ArrayAdapter<ItemView> {
    private Context mContext;
    public ItemsAdapter(Context context, int resource) {
        super(context, resource);
        mContext = context;
    }

    public ItemsAdapter(Context context, int resource, List<ItemView> items) {
        super(context, resource, items);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.item_row_layout, null);
        }

        ItemView item = getItem(position);

        if (item != null) {
            TextView textDescription = (TextView) view.findViewById(R.id.name);
            ImageView itemImage = (ImageView) view.findViewById(R.id.image);
            TextView txtPrice = (TextView) view.findViewById(R.id.price);
            if (textDescription != null) {
                textDescription.setText(item.getDescription());
            }
            if(itemImage != null){
                Picasso.with(this.mContext).load(item.getImgUrl()).into((itemImage));

            }
            if(txtPrice != null){
                txtPrice.setText("$" + String.valueOf(item.getPrice()));
            }

        }

        return view;
    }

}
