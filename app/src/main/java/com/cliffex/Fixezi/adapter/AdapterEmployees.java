package com.cliffex.Fixezi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cliffex.Fixezi.Model.ModelEmployeesTrade;
import com.cliffex.Fixezi.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdapterEmployees extends RecyclerView.Adapter<AdapterEmployees.MyEmployees> {

    Context mContext;
    ArrayList<ModelEmployeesTrade> empList;

    public AdapterEmployees(Context mContext, ArrayList<ModelEmployeesTrade> empList) {
        this.mContext = mContext;
        this.empList = empList;
    }

    @NonNull
    @NotNull
    @Override
    public MyEmployees onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_empl_trade,parent,false);
        return new MyEmployees(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyEmployees holder, int position) {
        ModelEmployeesTrade data = empList.get(position);
        holder.tvProblemCount.setText(String.valueOf(data.getProblem_count()));
        holder.tvEmail.setText(data.getUsername());
    }

    @Override
    public int getItemCount() {
        return empList == null?0:empList.size();
    }

    public class MyEmployees extends RecyclerView.ViewHolder {

        TextView tvProblemCount,tvEmail;

        public MyEmployees(@NonNull @NotNull View itemView) {
            super(itemView);
            tvProblemCount = itemView.findViewById(R.id.tvProblemCount);
            tvEmail = itemView.findViewById(R.id.tvEmail);
        }

    }

}
