package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mreverter.mercadopagoprueba.R;
import com.mercadopago.model.Issuer;

import java.util.List;

/**
 * Created by mreverter on 11/11/15.
 */
public class IssuersAdapter extends ArrayAdapter<Issuer>{

    public IssuersAdapter(Context context, int resource, List<Issuer> issuerList) {
        super(context, resource, issuerList);
    }

    public IssuersAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.issuers_row_layout, null);
        }

        Issuer p = getItem(position);

        if (p != null) {
            TextView textViewName = (TextView) view.findViewById(R.id.name);

            if (textViewName != null) {
                textViewName.setText(p.getName());
            }

        }

        return view;
    }
}
