package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mreverter.mercadopagoprueba.InstallmentsActivity;
import com.example.mreverter.mercadopagoprueba.R;
import com.mercadopago.model.PayerCost;


import java.util.List;

/**
 * Created by mreverter on 12/11/15.
 */
public class PayerCostsAdapter extends ArrayAdapter<PayerCost> {

    public PayerCostsAdapter(Context context, int resource) {
        super(context, resource);
    }

    public PayerCostsAdapter(Context context, int resource, List<PayerCost> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.payer_costs_row_layout, null);
        }

        PayerCost p = getItem(position);

        if (p != null) {
            TextView textViewMessage = (TextView) view.findViewById(R.id.name);

            if (textViewMessage != null) {
                textViewMessage.setText(p.getRecommendedMessage());
            }

        }

        return view;
    }
}
