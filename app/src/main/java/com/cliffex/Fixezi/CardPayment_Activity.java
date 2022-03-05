package com.cliffex.Fixezi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.cliffex.Fixezi.Constant.Constant;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.util.ProjectUtil;
import com.cooltechworks.creditcarddesign.CardEditActivity;
import com.cooltechworks.creditcarddesign.CreditCardUtils;
import com.cooltechworks.creditcarddesign.CreditCardView;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CardPayment_Activity extends AppCompatActivity {

    final int GET_NEW_CARD = 2;
    private CreditCardView creditCardView;
    private String cardHolderName, cardNumber, expiry, cvv;
    Toolbar Toolbarcard;
    TextView toolbar_title;
    private String token_id;
    Context mContext = CardPayment_Activity.this;
    Button btn_paynow;
    String cusId = "";
    public static Activity instance = null;
    RelativeLayout NavigationUpIM;
    SessionTradesman sessionUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment_);

        instance = this;

        creditCardView = (CreditCardView) findViewById(R.id.cardview);
        Toolbarcard = (Toolbar) findViewById(R.id.Toolbarcard);
        NavigationUpIM = (RelativeLayout) Toolbarcard.findViewById(R.id.NavigationUpIM);
        toolbar_title = (TextView) Toolbarcard.findViewById(R.id.toolbar_title);
        btn_paynow = (Button) findViewById(R.id.btn_paynow);
        sessionUser = new SessionTradesman(this);

        cusId = getIntent().getStringExtra("cusid");

        btn_paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Card.Builder card = new Card.Builder(cardNumber,
                        Integer.valueOf(expiry.split("/")[0]),
                        Integer.valueOf(expiry.split("/")[1]), cvv);

                Log.e("sdfdsfdsfdsf", "cardHolderName____________________" + cardHolderName);
                Log.e("sdfdsfdsfdsf", "cardNumber________________________" + cardNumber);
                Log.e("sdfdsfdsfdsf", "expirymonth____________________________" + expiry.split("/")[0]);
                Log.e("sdfdsfdsfdsf", "expirymonthyear____________________________" + expiry.split("/")[1]);
                Log.e("sdfdsfdsfdsf", "cvv_______________________________" + cvv);

                System.out.println("cardHolderName____________________" + cardHolderName);
                System.out.println("cardNumber________________________" + cardNumber);
                System.out.println("expiry____________________________" + expiry);
                System.out.println("cvv_______________________________" + cvv);

                // Stripe stripe = new Stripe(mContext, Constant.STRIPE_LIVE_KEY);
                Stripe stripe = new Stripe(mContext, Constant.STRIPE_TEST_KEY);

                ProjectUtil.showProgressDialog(mContext, false, "Please wait...");
                stripe.createCardToken(
                        card.build(), new ApiResultCallback<Token>() {
                            @Override
                            public void onSuccess(Token token) {
                                ProjectUtil.pauseProgressDialog();

                                // Toast.makeText(mContext, getString(R.string.successful), Toast.LENGTH_SHORT).show();
                                // charge(token);

                                HashMap<String, String> userdetails = sessionUser.getUserDetails();
                                String userEmail = userdetails.get("Email");

                                if (cusId == null || cusId.equals("")) {
                                    saveCardApi(token.getId(), userEmail, "PayPerJob",
                                            cardHolderName, cardNumber, expiry.split("/")[0],
                                            expiry.split("/")[1], cvv);
                                } else {
                                    addCardApi(token.getId());
                                }

                            }

                            @Override
                            public void onError(@NotNull Exception e) {
                                ProjectUtil.pauseProgressDialog();
                            }

                        });

            }
        });

        setSupportActionBar(Toolbarcard);
        toolbar_title.setText("Enter your details");

        Intent intent = new Intent(CardPayment_Activity.this, CardEditActivity.class);
        startActivityForResult(intent, GET_NEW_CARD);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            cardHolderName = data.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME);
            cardNumber = data.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER);
            expiry = data.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY);
            cvv = data.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV);
            creditCardView.setCVV(cvv);
            creditCardView.setCardHolderName(cardHolderName);
            creditCardView.setCardExpiry(expiry);
            creditCardView.setCardNumber(cardNumber);

            //            Card.Builder card = new Card.Builder(cardNumber,
//                    Integer.valueOf(expiry.split("/")[0]),
//                    Integer.valueOf(expiry.split("/")[1]), cvv);
//
//            Log.e("sdfdsfdsfdsf","cardHolderName____________________" + cardHolderName);
//            Log.e("sdfdsfdsfdsf","cardNumber________________________" + cardNumber);
//            Log.e("sdfdsfdsfdsf","expirymonth____________________________" + expiry.split("/")[0]);
//            Log.e("sdfdsfdsfdsf","expirymonthyear____________________________" + expiry.split("/")[1]);
//            Log.e("sdfdsfdsfdsf","cvv_______________________________" + cvv);
//
//            System.out.println("cardHolderName____________________" + cardHolderName);
//            System.out.println("cardNumber________________________" + cardNumber);
//            System.out.println("expiry____________________________" + expiry);
//            System.out.println("cvv_______________________________" + cvv);
//
//            Stripe stripe = new Stripe(mContext, Constant.STRIPE_TEST_KEY);
//
//            ProjectUtil.showProgressDialog(mContext, false, "Please wait...");
//            stripe.createCardToken(
//                    card.build(), new ApiResultCallback<Token>() {
//                        @Override
//                        public void onSuccess(Token token) {
//                            ProjectUtil.pauseProgressDialog();
//
//                            // Toast.makeText(mContext, getString(R.string.successful), Toast.LENGTH_SHORT).show();
//                            // charge(token);
//
//                            HashMap<String,String> userdetails = sessionUser.getUserDetails();
//                            String userEmail = userdetails.get("Email");
//
//                            if(cusId == null || cusId.equals("")) {
//                                saveCardApi(token.getId(),userEmail,"PayPerJob",
//                                        cardHolderName,cardNumber,expiry.split("/")[0],
//                                        expiry.split("/")[1],cvv);
//                            } else {
//                                addCardApi(token.getId());
//                            }
//
//                        }
//
//                        @Override
//                        public void onError(@NotNull Exception e) {
//                            ProjectUtil.pauseProgressDialog();
//                        }
//
//                    });

        }

    }

    private void addCardApi(String token) {
        ProjectUtil.showProgressDialog(mContext, false, "Please wait...");
        HashMap<String, String> param = new HashMap<>();
        param.put("cus_id", cusId);
        param.put("source", token);
        Log.e("addCardApiSource", "param = " + param);
        AndroidNetworking.post(HttpPAth.Urlpath + "add_card")
                .addBodyParameter(param)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("addCardApiSource", "response = " + response);
                        ProjectUtil.pauseProgressDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("1")) {
                                cardSaveDialog();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("adfasdasdas", "ANError = " + anError.getErrorDetail());
                        Log.e("adfasdasdas", "ANError = " + anError.getErrorBody());
                    }
                });

    }

    private void cardSaveDialog() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(CardPayment_Activity.this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#042587"));
        pDialog.setTitleText("Your Card Added Successfully Done");
        pDialog.setConfirmText("Ok");
        pDialog.setCancelable(false);

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                pDialog.dismissWithAnimation();
                startActivity(new Intent(mContext, ChooseCardAct.class));
                finish();
            }
        });

        pDialog.show();
    }

    private void saveCardApi(String token, String email, String plantype, String cardHolder,
                             String cardNo, String expMonth, String expYear, String cvv) {
        ProjectUtil.showProgressDialog(mContext, false, "Please wait...");
        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", sessionUser.getId());
        param.put("email", email);
        param.put("description", email);
        param.put("source", token);
        param.put("plan_type", plantype);
        param.put("cardHolderName", cardHolder);
        param.put("cardnumber", cardNo);
        param.put("expmonth", expMonth);
        param.put("expyear", expYear);
        param.put("cvc", cvv);
        Log.e("adfasdasdas", "param = " + param.toString());
        AndroidNetworking.post(HttpPAth.Urlpath + "save_card")
                .addBodyParameter(param)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("adfasdasdas", "response = " + response);
                        ProjectUtil.pauseProgressDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("1")) {
                                cardSaveDialog();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("adfasdasdas", "ANError = " + anError.getErrorDetail());
                        Log.e("adfasdasdas", "ANError = " + anError.getErrorBody());
                        ProjectUtil.pauseProgressDialog();
                    }
                });

    }

/*  public void onClickSomething(String cardNumber, int cardExpMonth, int cardExpYear, String cardCVC) {
        Card card = new Card(cardNumber, cardExpMonth, cardExpYear, cardCVC);
        card.validateNumber();
        card.validateCVC();
    }*/

}
