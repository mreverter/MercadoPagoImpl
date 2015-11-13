package adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mreverter.mercadopagoprueba.R;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.util.MercadoPagoUtil;

import java.util.List;

/**
 * Created by mreverter on 10/11/15.
 */
public class PaymentMethodAdapter extends ArrayAdapter<PaymentMethod> {

    private Context mContext;
    public PaymentMethodAdapter(Context context, int resource) {
        super(context, resource);
        this.mContext = context;
    }

    public PaymentMethodAdapter(Context context, int resource, List<PaymentMethod> items) {
        super(context, resource, items);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.payment_methods_row_layout, null);
        }

        PaymentMethod paymentMethod = getItem(position);

        if (paymentMethod != null) {
            TextView textViewId = (TextView) view.findViewById(R.id.name);
            ImageView image = (ImageView) view.findViewById(R.id.image);
            if (textViewId != null) {
                textViewId.setText(paymentMethod.getId());
            }
            if(image != null){
                image.setImageResource(MercadoPagoUtil.getPaymentMethodIcon(this.mContext, paymentMethod.getId()));
            }

        }

        return view;
    }
}
