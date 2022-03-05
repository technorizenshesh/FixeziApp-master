package com.cliffex.Fixezi;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cliffex.Fixezi.Model.NotesBean;
import com.cliffex.Fixezi.MyUtils.HttpPAth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.cliffex.Fixezi.MyUtils.InternetDetect;

public class ViewNotes extends AppCompatActivity {

    Toolbar toolbar;
    SessionTradesman sessionTradesman;
    TextView toolbar_textview;
    RelativeLayout NavigationUpIM;
    RecyclerView RecyclerViewVN;
    String ProblemId = "";
    TextView NoJobsFoundTV8;
    TextView Signtt;

    RelativeLayout.LayoutParams layoutParams43;
    RelativeLayout.LayoutParams layoutParams169;
    RelativeLayout.LayoutParams layoutParams44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);
        Signtt= (TextView) findViewById(R.id.Signtt);

        String text = "<font color='#49A2F0'>To delete a note, </font> just press and hold the note you wish to delete.";
        Signtt.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        Bundle bundle = getIntent().getExtras();
        ProblemId = bundle.getString("ProblemId");

        Log.e("ProblemId",">>>"+ProblemId);

        sessionTradesman = new SessionTradesman(this);
        toolbar = (Toolbar) findViewById(R.id.ToolbarVN);
        toolbar_textview = (TextView) findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        toolbar_textview.setText("View Notes");
        
        setSupportActionBar(toolbar);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RecyclerViewVN = (RecyclerView) findViewById(R.id.RecyclerViewVN);
        NoJobsFoundTV8 = (TextView) findViewById(R.id.NoJobsFoundTV8);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        RecyclerViewVN.setLayoutManager(mLayoutManager);
        RecyclerViewVN.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        RecyclerViewVN.setItemAnimator(new DefaultItemAnimator());

        if (InternetDetect.isConnected(this)) {
            new JsonGetNotes().execute(ProblemId);
        } else {
            Toast.makeText(ViewNotes.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;

        int MyHeight43 = ((width * 3) / 4);
        int MyHeight169 = ((width * 9) / 16);
        int MyHeight44 = width;

        layoutParams43 = new RelativeLayout.LayoutParams(width, MyHeight43);
        layoutParams169 = new RelativeLayout.LayoutParams(width, MyHeight169);
        layoutParams44 = new RelativeLayout.LayoutParams(width, MyHeight44);

    }


    private class JsonGetNotes extends AsyncTask<String, String, List<NotesBean>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<NotesBean> doInBackground(String... strings) {

            try {
                URL url = new URL(HttpPAth.Urlpath + "get_all_trademan_notes&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("problem_id", strings[0]);

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);

                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

                writer.write(urlParameters);
                writer.flush();

                String response = "";
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line = reader.readLine()) != null) {
                    response += line;
                }
                writer.close();
                reader.close();
                Log.e("JsonGetNote", response);

                List<NotesBean> notesBeanList = new ArrayList<>();


                JSONArray jsonArray = new JSONArray(response);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject object = jsonArray.getJSONObject(i);
                    NotesBean notesBean = new NotesBean();
                    notesBean.setId(object.getString("id"));
                    notesBean.setNotes_description(object.getString("notes_description"));
                    notesBean.setNotes_image(object.getString("notes_image"));
                    notesBean.setNotes_image_ratio(object.getString("notes_image_ratio"));
                    notesBean.setNotes_type(object.getString("notes_type"));
                    notesBean.setTimeonjob(object.getString("timeonjob"));

                    notesBeanList.add(notesBean);


                }

                return notesBeanList;

            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
                Log.e("EncodingException",e1.getMessage());
            } catch (IOException e1) {
                e1.printStackTrace();
                Log.e("IOException",e1.getMessage());
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSONException",e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<NotesBean> result) {
            super.onPostExecute(result);

            if (result == null) {

                NoJobsFoundTV8.setVisibility(View.VISIBLE);
            } else if (result.isEmpty()) {

                NoJobsFoundTV8.setVisibility(View.VISIBLE);
            } else {

                Log.e("REsult size",">>>"+result.size());
                NotesAdapter adapter = new NotesAdapter(ViewNotes.this, result);
                RecyclerViewVN.setAdapter(adapter);
            }
        }
    }


    private class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

        private Context mContext;
        List<NotesBean> notesBeanList;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView NotesDesc,NoteNumberTV,Notesdate;
            ImageView NotesImage;

            public MyViewHolder(View view) {
                super(view);

                NotesDesc = (TextView) view.findViewById(R.id.NotesDesc);
                NoteNumberTV= (TextView) view.findViewById(R.id.NoteNumberTV);
                Notesdate= (TextView) view.findViewById(R.id.Notesdate);
                NotesImage = (ImageView) view.findViewById(R.id.NotesImage);

            }
        }


        public NotesAdapter(Context mContext, List<NotesBean> notesBeanList) {
            this.mContext = mContext;
            this.notesBeanList = notesBeanList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.notes_rowitem, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            int myposition=position+1;
            holder.NoteNumberTV.setText("Note "+myposition);

            holder.NotesDesc.setText(notesBeanList.get(position).getNotes_description());

            Glide.with(mContext).load(notesBeanList.get(position).getNotes_image())
                    .thumbnail(0.5f)
                    .into(holder.NotesImage);

            holder.Notesdate.setText(notesBeanList.get(position).getTimeonjob());


          /* if (notesBeanList.get(position).getNotes_description().equalsIgnoreCase("")){

               holder.NotesDesc.setVisibility(View.GONE);
               holder.NotesImage.setVisibility(View.VISIBLE);


               if (notesBeanList.get(position).getNotes_image_ratio().equalsIgnoreCase("43")) {

                   holder.NotesImage.setLayoutParams(layoutParams43);
               } else if (notesBeanList.get(position).getNotes_image_ratio().equalsIgnoreCase("169")) {

                   holder.NotesImage.setLayoutParams(layoutParams169);
               } else if (notesBeanList.get(position).getNotes_image_ratio().equalsIgnoreCase("44")) {

                   holder.NotesImage.setLayoutParams(layoutParams44);
               }


               Glide.with(mContext).load(notesBeanList.get(position).getNotes_image())
                       .thumbnail(0.5f)
                       .crossFade()
                       .diskCacheStrategy(DiskCacheStrategy.ALL)
                       .into(holder.NotesImage);

           }else{
               holder.NotesDesc.setVisibility(View.VISIBLE);
               holder.NotesImage.setVisibility(View.GONE);

               holder.NotesDesc.setText(notesBeanList.get(position).getNotes_description());


           }
*/
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });

        }

        @Override
        public int getItemCount() {

            return notesBeanList == null ? 0 : notesBeanList.size();

        }
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = 10; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


}
