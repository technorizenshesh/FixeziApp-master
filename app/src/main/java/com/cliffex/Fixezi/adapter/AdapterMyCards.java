package com.cliffex.Fixezi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.print.PageRange;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.cliffex.Fixezi.Model.ModelMyCards;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.R;
import com.cliffex.Fixezi.util.ProjectUtil;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterMyCards extends RecyclerView.Adapter<AdapterMyCards.MyViewCardHolder> {

    Context mContext;
    ArrayList<ModelMyCards.Data> cardsList;
    AddPaymentInterface addPaymentInterface;

    public AdapterMyCards(Context mContext, ArrayList<ModelMyCards.Data> cardsList,AddPaymentInterface addPaymentInterface) {
        this.mContext = mContext;
        this.cardsList = cardsList;
        this.addPaymentInterface = addPaymentInterface;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewCardHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_cards,parent,false);
        return new MyViewCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewCardHolder holder, int position) {
        ModelMyCards.Data data = cardsList.get(position);
        holder.cardHolder.setText(data.getName());
        holder.cardNumber.setText(data.getBrand()+" **** **** **** "+data.getLast4());
        holder.tvDateYear.setText(data.getExp_month()+"/"+ data.getExp_year());

        holder.itemView.setOnClickListener(v -> {
            addPaymentDialog(data);
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteCardDialog(data,position);
                return false;
            }
        });

    }

    public interface AddPaymentInterface {
        void onSuccess(ModelMyCards.Data data);
    }

    private void addPaymentDialog(ModelMyCards.Data data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Add Payment");
        builder.setMessage("Are you sure you want to use this card?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addPaymentInterface.onSuccess(data);
            }
        }).create().show();
    }

    private void deleteCardDialog(ModelMyCards.Data data,int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Delete Card");
        builder.setMessage("Are you sure you want to delete?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCardApi(data,position);
            }
        }).create().show();
    }

    private void deleteCardApi(ModelMyCards.Data data,int position) {

        ProjectUtil.showProgressDialog(mContext,false,mContext.getString(R.string.please_wait));
        HashMap<String,String> params = new HashMap<>();
        params.put("cus_id",data.getCustomer());
        params.put("card_id",data.getId());
        Log.e("deleteCardApi","deleteCardApi = " + params.toString());
        AndroidNetworking.post(HttpPAth.Urlpath + "delete_card")
                .addBodyParameter(params)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("deleteCardApi","response = " + response);
                        ProjectUtil.pauseProgressDialog();
                        cardsList.remove(position);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtil.pauseProgressDialog();
                        Log.e("deleteCardApi","anError = " + anError.getErrorBody());
                        Log.e("deleteCardApi","anError = " + anError.getErrorDetail());
                    }
                });


    }

    @Override
    public int getItemCount() {
        return cardsList == null?0:cardsList.size();
    }

    public class MyViewCardHolder extends RecyclerView.ViewHolder{

        CheckBox cbCheck;
        TextView cardHolder,cardNumber,tvDateYear;

        public MyViewCardHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cbCheck = itemView.findViewById(R.id.cbCheck);
            cardHolder = itemView.findViewById(R.id.cardHolder);
            cardNumber = itemView.findViewById(R.id.cardNumber);
            tvDateYear = itemView.findViewById(R.id.tvDateYear);
        }
    }

}
