package com.cliffex.Fixezi;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewList extends AppCompatActivity {

    RecyclerView RVTEST;
    ImageView RightIM,LeftIM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);

        RVTEST = (RecyclerView) findViewById(R.id.RVTEST);
        LeftIM = (ImageView) findViewById(R.id.LeftIM);
        RightIM= (ImageView) findViewById(R.id.RightIM);

        final LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RVTEST.setLayoutManager(horizontalLayoutManagaer);

        HorizontalAdapter adapter = new HorizontalAdapter(this, null);
        RVTEST.setAdapter(adapter);


        LeftIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });


        RightIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                RVTEST.getLayoutManager().scrollToPosition(horizontalLayoutManagaer.findFirstVisibleItemPosition() + 1);

            }
        });



    }


    private class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

        private Context mContext;
        List<String> incomingRequestListBeanList;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            RecyclerView RVTEST2;

            public MyViewHolder(View view) {
                super(view);

                RVTEST2 = (RecyclerView) view.findViewById(R.id.RVTEST2);
                LinearLayoutManager linearLayoutManagaer = new LinearLayoutManager(mContext);
                RVTEST2.setLayoutManager(linearLayoutManagaer);

            }
        }

        public HorizontalAdapter(Context mContext, List<String> incomingRequestListBeanList1) {
            this.mContext = mContext;
            incomingRequestListBeanList = incomingRequestListBeanList1;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.test_rowitem, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {


            LinearAdapter linearAdapter = new LinearAdapter(mContext, null);
            holder.RVTEST2.setAdapter(linearAdapter);
        }

        @Override
        public int getItemCount() {

            //return incomingRequestListBean == null ? 0 : incomingRequestListBean.size();
            return 5;

        }
    }


    private class LinearAdapter extends RecyclerView.Adapter<LinearAdapter.MyViewHolder> {

        private Context mContext;
        List<String> incomingRequestListBeanList;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView RVTEST2;

            public MyViewHolder(View view) {
                super(view);

                //RVTEST2 = (RecyclerView) view.findViewById(R.id.RVTEST2);
            }
        }


        public LinearAdapter(Context mContext, List<String> incomingRequestListBeanList1) {
            this.mContext = mContext;
            incomingRequestListBeanList = incomingRequestListBeanList1;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.job_request_cardview, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {


        }

        @Override
        public int getItemCount() {

            //return incomingRequestListBean == null ? 0 : incomingRequestListBean.size();
            return 15;
        }
    }
}

