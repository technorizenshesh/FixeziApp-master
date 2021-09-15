package com.cliffex.Fixezi.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cliffex.Fixezi.IncomingJobRequestDetail;
import com.cliffex.Fixezi.Other.AppConfig;
import com.cliffex.Fixezi.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cliffex.Fixezi.Other.MySharedPref.saveData;


public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // Get a Calendar instance
        final Calendar calendar = Calendar.getInstance();
        // Get the current hour and minute
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        /*
            Creates a new time picker dialog with the specified theme.

                TimePickerDialog(Context context, int themeResId,
                    TimePickerDialog.OnTimeSetListener listener,
                    int hourOfDay, int minute, boolean is24HourView)
         */

        // TimePickerDialog Theme : THEME_HOLO_LIGHT
        TimePickerDialog tpd = new TimePickerDialog(getActivity(),
                AlertDialog.THEME_HOLO_LIGHT,this,hour,minute,false);

        // Return the TimePickerDialog
        return tpd;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        // Set a variable to hold the current time AM PM Status
        // Initially we set the variable value to AM
        String status = "AM";

        if(hourOfDay > 11)
        {
            // If the hour is greater than or equal to 12
            // Then the current AM PM status is PM
            status = "PM";
        }

        // Initialize a new variable to hold 12 hour format hour value
        int hour_of_12_hour_format;

        if(hourOfDay > 11){

            // If the hour is greater than or equal to 12
            // Then we subtract 12 from the hour to make it 12 hour format time
            hour_of_12_hour_format = hourOfDay - 12;
        }
        else {
            hour_of_12_hour_format = hourOfDay;
        }

        // Get the calling activity TextView reference
        TextView tv = (TextView) getActivity().findViewById(R.id.TimeDetail);
        // Display the 12 hour format time in app interface
        tv.setText(hour_of_12_hour_format + ":" + minute + " " + status);
        IncomingJobRequestDetail.strdate=tv.getText().toString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            updatenum(IncomingJobRequestDetail.strday,tv.getText().toString(),IncomingJobRequestDetail.ProblemId);
            saveData(getContext(),"offertme",tv.getText().toString());
        }

    }

    private void updatenum(String date,String time,String problem_id) {

        Call<ResponseBody> call = AppConfig.loadInterface().updatedate(date,time,problem_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        String message=object.getString("message");
                        System.out.println("Updatenum" + object);
                        if (object.getString("status").equals("1")) {
                           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                            }*/

                        } else {
                           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();
                            }*/

                        }

                    } else ;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
