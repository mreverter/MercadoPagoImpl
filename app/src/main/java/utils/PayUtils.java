package utils;

import android.app.Activity;
import android.widget.Toast;

import com.mercadopago.core.MercadoPago;
import com.mercadopago.core.MerchantServer;
import com.mercadopago.model.Discount;
import com.mercadopago.model.Item;
import com.mercadopago.model.MerchantPayment;
import com.mercadopago.model.Payment;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.util.LayoutUtil;

import java.math.BigDecimal;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by mreverter on 10/11/15.
 */
public class PayUtils {

    public static final String DUMMY_ITEM_ID = "id1";
    public static final Integer DUMMY_ITEM_QUANTITY = 1;
    public static final BigDecimal DUMMY_ITEM_UNIT_PRICE = new BigDecimal("100");

    public static final String DUMMY_MERCHANT_PUBLIC_KEY = "444a9ef5-8a6b-429f-abdf-587639155d88";
    // * Merchant server vars
    public static final String DUMMY_MERCHANT_GET_CUSTOMER_URI = "/checkout/examples/getCustomer";
    public static final String DUMMY_MERCHANT_BASE_URL = "https://www.mercadopago.com";
    public static final String DUMMY_MERCHANT_CREATE_PAYMENT_URI = "/checkout/examples/doPayment";

    // * Merchant access token
    public static final String DUMMY_MERCHANT_ACCESS_TOKEN = "TEST-7176766875549918-111008-fa5660d2d0aa37532716eb2bf2f9089b__LB_LC__-192992930";
    public static void createPayment(final Activity activity, String token, Integer installments, Long cardIssuerId, final PaymentMethod paymentMethod, Discount discount) {

        if (paymentMethod != null) {

            LayoutUtil.showProgressLayout(activity);

            // Set item
            Item item = new Item(DUMMY_ITEM_ID, DUMMY_ITEM_QUANTITY,
                    DUMMY_ITEM_UNIT_PRICE);

            // Set payment method id
            String paymentMethodId = paymentMethod.getId();

            // Set campaign id
            Long campaignId = (discount != null) ? discount.getId() : null;

            // Set merchant payment
            MerchantPayment payment = new MerchantPayment(item, installments, cardIssuerId,
                    token, paymentMethodId, campaignId, DUMMY_MERCHANT_ACCESS_TOKEN);

            // Create payment
            MerchantServer.createPayment(activity, DUMMY_MERCHANT_BASE_URL, DUMMY_MERCHANT_CREATE_PAYMENT_URI, payment, new Callback<Payment>() {
                @Override
                public void success(Payment payment, Response response) {

                    new MercadoPago.StartActivityBuilder()
                            .setActivity(activity)
                            .setPayment(payment)
                            .setPaymentMethod(paymentMethod)
                            .startCongratsActivity();
                }

                @Override
                public void failure(RetrofitError error) {

                    LayoutUtil.showRegularLayout(activity);
                    Toast.makeText(activity, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {

            Toast.makeText(activity, "Invalid payment method", Toast.LENGTH_LONG).show();
        }
    }
}
