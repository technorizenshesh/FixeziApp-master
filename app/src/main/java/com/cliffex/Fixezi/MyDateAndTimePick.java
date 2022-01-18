package com.cliffex.Fixezi;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.mustafaferhan.MFCalendarView;
import com.mustafaferhan.onMFCalendarViewListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialogSet;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by technorizen8 on 3/5/16.
 */
public class MyDateAndTimePick extends AppCompatActivity implements
        TimePickerDialogSet.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    TextView toolbar_title, picktimetxtview;
    Button dialogacceptbtn;
    String strSelectedDate = "";
    Toolbar datetimetoolbar;
    Date GCurrentDate, GSelectedDate;
    boolean istrue, isDateYesNo, isTimeYesNo;
    Context mContext = MyDateAndTimePick.this;
    Dialog callFeeDialog;
    int currentyear;
    RelativeLayout picktimetxtviewlayyy;
    RelativeLayout NavigationUpIM;
    Button yeslayyout, nolayouttt, yeslayyout1, nolayouttt1;
    MFCalendarView mf;
    ImageView CallOutFeeInfoDT, TimeFlexInfoIM, DateFlexInfoIM;
    private String type_string;
    private RelativeLayout rl_calender_layout_new_daily_entry_screen, ss;
    private LinearLayout ss1, time_picker_linear, CalloutfeeTextview;
    private Button acceptBtDT;
    private Button cancelBtDT;
    Calendar date = Calendar.getInstance();
    private TimePicker timePicker1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datetimedialog);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            if (extras == null) {
                type_string = "null";
            } else {
                type_string = extras.getString("type");
            }
        } else {
            type_string = (String) savedInstanceState.getSerializable("type");
        }

        DateAndTimePickerActivity();

        currentyear = Calendar.getInstance().get(Calendar.YEAR);
        datetimetoolbar = (Toolbar) findViewById(R.id.datetimetoolbar);
        mf = (MFCalendarView) findViewById(R.id.mFCalendarView);
        picktimetxtview = (TextView) findViewById(R.id.picktimetxtview);
        dialogacceptbtn = (Button) findViewById(R.id.dialogacceptbtn);
        yeslayyout = (Button) findViewById(R.id.yeslayyout);
        yeslayyout1 = (Button) findViewById(R.id.yeslayyout1);
        nolayouttt = (Button) findViewById(R.id.nolayouttt);
        nolayouttt1 = (Button) findViewById(R.id.nolayouttt1);
        picktimetxtviewlayyy = (RelativeLayout) findViewById(R.id.picktimetxtviewlayyy);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        CallOutFeeInfoDT = (ImageView) findViewById(R.id.CallOutFeeInfoDT);
        TimeFlexInfoIM = (ImageView) findViewById(R.id.TimeFlexInfoIM);
        DateFlexInfoIM = (ImageView) findViewById(R.id.DateFlexInfoIM);

        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        rl_calender_layout_new_daily_entry_screen = (RelativeLayout) findViewById(R.id.rl_calender_layout_new_daily_entry_screen);
        CalloutfeeTextview = (LinearLayout) findViewById(R.id.CalloutfeeTextview);
        ss = (RelativeLayout) findViewById(R.id.ss);
        ss1 = (LinearLayout) findViewById(R.id.ss1);
        time_picker_linear = (LinearLayout) findViewById(R.id.time_picker_linear);

        if (type_string.equals("date")) {
            time_picker_linear.setVisibility(View.GONE);
        }

        if (type_string.equals("time")) {
            rl_calender_layout_new_daily_entry_screen.setVisibility(View.GONE);
            CalloutfeeTextview.setVisibility(View.GONE);
            ss.setVisibility(View.GONE);
            ss1.setVisibility(View.GONE);
        }

        setSupportActionBar(datetimetoolbar);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbar_title = (TextView) datetimetoolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Set date & time");

        Appconstants.DATE_SELECTED = "";
        Appconstants.TIME_SELECTED = "";
        Appconstants.dateFlexi = "";
        Appconstants.timeFlexi = "";

        yeslayyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yeslayyout.setBackgroundResource(R.drawable.border_black_solid_skyblue);
                yeslayyout.setTextColor(Color.parseColor("#ffffff"));
                nolayouttt.setBackgroundResource(R.drawable.border_black_solid_white);
                nolayouttt.setTextColor(Color.parseColor("#000000"));
                Appconstants.dateFlexi = "yes";
            }

        });

        yeslayyout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ValidationSuccess()) {

                    final String[] strings = {"Flexible up to 2 hrs", "Flexible up to 2-4 hrs", "I'm free all day", "I'm free all week"};
                    new AlertDialog.Builder(MyDateAndTimePick.this)
                            .setTitle("Please Select")
                            .setSingleChoiceItems(strings, 0, null)
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                            int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                            yeslayyout1.setBackgroundResource(R.drawable.border_black_solid_skyblue);
                            yeslayyout1.setTextColor(Color.parseColor("#ffffff"));
                            nolayouttt1.setBackgroundResource(R.drawable.border_black_solid_white);
                            nolayouttt1.setTextColor(Color.parseColor("#000000"));
                            Appconstants.timeFlexi = "Yes";
                            Appconstants.timeFlexibleValue = strings[selectedPosition];
                        }
                    }).show();
                }
            }

            public boolean ValidationSuccess() {
                return true;
            }

        });

        CallOutFeeInfoDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog YourAddDialog = new Dialog(MyDateAndTimePick.this);
                YourAddDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                YourAddDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                YourAddDialog.setContentView(R.layout.alert_dialog_address);

                final Button OkayBT = (Button) YourAddDialog.findViewById(R.id.OkayBT);
                final TextView MyContent = (TextView) YourAddDialog.findViewById(R.id.MyContent);
                MyContent.setText(getResources().getString(R.string.TwentyFourHour));
                OkayBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        YourAddDialog.dismiss();
                    }
                });

                YourAddDialog.show();

            }

        });

        TimeFlexInfoIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog YourAddDialog = new Dialog(MyDateAndTimePick.this);
                YourAddDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                YourAddDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                YourAddDialog.setContentView(R.layout.alert_dialog_address);

                final Button OkayBT = (Button) YourAddDialog.findViewById(R.id.OkayBT);
                final TextView MyContent = (TextView) YourAddDialog.findViewById(R.id.MyContent);
                MyContent.setText("If Tradesman are unavailable to do your job at selected date/time, Can date/time be flexible. i.e. +/- 4 hrs (before or after) Or the next day.");

                OkayBT.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        YourAddDialog.dismiss();
                    }
                });

                YourAddDialog.show();
            }
        });

        DateFlexInfoIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog YourAddDialog = new Dialog(MyDateAndTimePick.this);
                YourAddDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                YourAddDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                YourAddDialog.setContentView(R.layout.alert_dialog_address);

                final Button OkayBT = (Button) YourAddDialog.findViewById(R.id.OkayBT);
                final TextView MyContent = (TextView) YourAddDialog.findViewById(R.id.MyContent);
                MyContent.setText("If Tradesman cannot do you job at selected date/time. Can date/time be flexible i.e.-The next day, 4 hrs before or after.");

                OkayBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        YourAddDialog.dismiss();
                    }
                });

                YourAddDialog.show();

            }
        });

        nolayouttt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nolayouttt.setBackgroundResource(R.drawable.border_black_solid_skyblue);
                nolayouttt.setTextColor(Color.parseColor("#ffffff"));
                yeslayyout.setBackgroundResource(R.drawable.border_black_solid_white);
                yeslayyout.setTextColor(Color.parseColor("#000000"));
                Appconstants.dateFlexi = "no";
            }
        });

        nolayouttt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ValidationSuccess()) {
                    nolayouttt1.setBackgroundResource(R.drawable.border_black_solid_skyblue);
                    nolayouttt1.setTextColor(Color.parseColor("#ffffff"));
                    yeslayyout1.setBackgroundResource(R.drawable.border_black_solid_white);
                    yeslayyout1.setTextColor(Color.parseColor("#000000"));
                    Appconstants.timeFlexi = "No";
                    Appconstants.timeFlexibleValue = "Not available";
                }

            }

            public boolean ValidationSuccess() {

                if (Appconstants.TIME_SELECTED.equalsIgnoreCase("")) {
                    Toast.makeText(MyDateAndTimePick.this, "Please select time first", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }

        });

        dialogacceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidationSuccess()) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("isstart", "start");
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }

            public boolean ValidationSuccess() {

                if (type_string.equals("date")) {
                    if (Appconstants.DATE_SELECTED.equalsIgnoreCase("")) {
                        Toast.makeText(MyDateAndTimePick.this, "Select date", Toast.LENGTH_SHORT).show();
                        return false;
                    } else if (Appconstants.dateFlexi.equals("")) {
                        alertDialogYesNo("Please select date flexible yes or no ?");
                        // Toast.makeText(mContext, "IS SELECTED DATE FLEXIBLE ?", Toast.LENGTH_LONG).show();
                        return false;
                    }
                }

                if (type_string.equals("time")) {
                    if (Appconstants.TIME_SELECTED.equalsIgnoreCase("")) {
                        Toast.makeText(MyDateAndTimePick.this, "Select Time", Toast.LENGTH_SHORT).show();
                        return false;
                    } else if (Appconstants.timeFlexi.equals("")) {
                        alertDialogYesNo("Please select time flexible yes or no ?");
                        // Toast.makeText(mContext, "IS SELECTED TIME FLEXIBLE ?", Toast.LENGTH_LONG).show();
                        return false;
                    }
                }

                return true;

            }

        });

        mf.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        mf.setOnCalendarViewListener(new onMFCalendarViewListener() {
            @Override
            public void onDisplayedMonthChanged(int month, int year, String monthStr) {

                StringBuffer bf = new StringBuffer().append(" month:")
                        .append(month).append(" year:").append(year)
                        .append(" monthStr: ").append(monthStr);

                if (month > 9) {

                    strSelectedDate = year + "-" + (month) + "-" + "01";

                } else {
                    strSelectedDate = year + "-0" + (month) + "-" + "01";
                }

            }

            @Override
            public void onDateChanged(String date) {

                strSelectedDate = date;
                Log.e("Check this one", "slected strSelectedDate= " + strSelectedDate);
                mf.setDate(strSelectedDate);
                mf.setBackgroundColor(getTitleColor());
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                String WhichDay = "";
                try {
                    Date date2 = df.parse(strSelectedDate);
                    WhichDay = sdf.format(date2);
                    Log.e("DAy", "....." + WhichDay);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String splitstring[] = strSelectedDate.split("-");

                String year = splitstring[0];
                System.out.println("yyyy" + year);
                String month = splitstring[1];
                System.out.println("mmmm" + month);
                String daaate = splitstring[2];
                System.out.println("dddddd" + daaate);

                istrue = checkDate(year, month, daaate);

                if (istrue) {

                    Appconstants.DATE_SELECTED = daaate + "-" + month + "-" + year;
                    UserActivity.selectDate = daaate + "-" + month + "-" + year;

                    Log.e("Valid DAte", "YES");
                    Log.e("DATE_SELECTed", Appconstants.DATE_SELECTED);

                    if (WhichDay.equalsIgnoreCase("Saturday") || WhichDay.equalsIgnoreCase("Sunday")) {

                        final Dialog SatSunDialog = new Dialog(MyDateAndTimePick.this);
                        SatSunDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        SatSunDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        SatSunDialog.setContentView(R.layout.alert_dialog_call_out_fee);
                        TextView YourText = (TextView) SatSunDialog.findViewById(R.id.YourText);

                        YourText.setText("An Emergency Call Out applies Saturday and Sunday.");
                        final Button okBt = (Button) SatSunDialog.findViewById(R.id.okBt);

                        okBt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SatSunDialog.dismiss();
                            }
                        });

                        SatSunDialog.show();

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Unable to select previous date", Toast.LENGTH_SHORT).show();
                }

            }
        });

        if (type_string.equals("time")) {
            timePicker1.setIs24HourView(true);
            final Calendar currentDate = Calendar.getInstance();
            TimePickerDialog timePickerDialog = new TimePickerDialog(mContext,
                    android.R.style.Theme_Holo_Light_Dialog,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            date.set(Calendar.MINUTE, minute);
                            RadialPickerLayout view1 = null;
                            final int hourOfDay1 = date.get(Calendar.HOUR_OF_DAY);
                            final int minute1 = date.get(Calendar.MINUTE);
                            int second1 = currentDate.get(Calendar.SECOND);
                            MyDateAndTimePick.this.onTimeSet(view1, hourOfDay1, minute1, second1);
                        }
                    }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false);
            timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_date_time_picker_dialog, null);
            timePickerDialog.setCustomTitle(dialogView);
            timePickerDialog.show();

//            TimePickerDialogSet tpd = TimePickerDialogSet.newInstance(MyDateAndTimePick.this, now.get(Calendar.HOUR_OF_DAY),
//                    now.get(Calendar.MINUTE),false);
//
//            tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialogInterface) {
//                    Log.e("TimePicker", "Dialog was cancelled");
//                }
//            });
//
//            tpd.show(getFragmentManager(), "Timepickerdialog");

        }

        //        picktimetxtviewlayyy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Calendar now = Calendar.getInstance();
//                TimePickerDialog tpd = TimePickerDialog.newInstance(MyDateAndTimePick.this,android.R.style.Theme_Holo_Light_Dialog,
//                        now.get(Calendar.HOUR_OF_DAY),
//                        now.get(Calendar.MINUTE), false);
//
//                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialogInterface) {
//                        Log.e("TimePicker", "Dialog was cancelled");
//                    }
//                });
//
//                tpd.show(getFragmentManager(), "Timepickerdialog");
//
//            }
//
//        });

        picktimetxtviewlayyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar currentDate = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext,
                        android.R.style.Theme_Holo_Light_Dialog,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                date.set(Calendar.MINUTE, minute);
                                RadialPickerLayout view1 = null;
                                final int hourOfDay1 = date.get(Calendar.HOUR_OF_DAY);
                                final int minute1 = date.get(Calendar.MINUTE);
                                int second1 = currentDate.get(Calendar.SECOND);
                                MyDateAndTimePick.this.onTimeSet(view1, hourOfDay1, minute1, second1);
                            }
                        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_date_time_picker_dialog, null);
                timePickerDialog.setCustomTitle(dialogView);
                timePickerDialog.show();
//                Calendar now = Calendar.getInstance();
//                TimePickerDialogSet tpd = TimePickerDialogSet.newInstance(MyDateAndTimePick.this, now.get(Calendar.HOUR_OF_DAY),
//                        now.get(Calendar.MINUTE), false);
//
//                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialogInterface) {
//                        Log.e("TimePicker", "Dialog was cancelled");
//                    }
//                });
//                tpd.setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Light_Dialog);
//                tpd.show(getFragmentManager(), "Timepickerdialog");
            }

        });

    }

    private void alertDialogYesNo(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Fixezi")
                .setMessage(msg)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    private int mYear, mMonth, mDay, mHour, mMinute;

    public void DateAndTimePickerActivity() {
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        System.out.println("cumyear" + mYear);
        mMonth = c.get(Calendar.MONTH);
        System.out.println("cumyear" + mMonth);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        System.out.println("cumyear" + mDay);
    }

    public boolean checkDate(String year_date, String month_date, String day_date) {

        System.out.println("checkYY" + year_date);
        System.out.println("checkMM" + month_date);
        System.out.println("checkDD" + day_date);

        boolean checkDateFlag1 = false;

        String mm = "", md = "";//mm=(mMonth+1)+""
        String my = mYear + "";
        if ((mMonth + 1) > 9) {
            mm = (mMonth + 1) + "";
        } else {
            mm = "0" + (mMonth + 1);
        }

        if (mDay > 9) {
            md = mDay + "";
        } else {
            md = "0" + mDay;
        }


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date2 = null, date1 = null;

        try {


            date1 = sdf.parse(my + "-" + mm + "-" + md);//current date
            date2 = sdf.parse(year_date + "-" + month_date + "-" + day_date);//checking date

            GCurrentDate = date1;
            GSelectedDate = date2;
            Log.e("", "for check year_date= " + year_date + " month_date= " + month_date + " day_date= " + day_date);
            Log.e("", "current date= " + (sdf.format(date1)));
            Log.e("", "checking date= " + (sdf.format(date2)));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.e("dateeee1", "" + date1);
        Log.e("dateee2", "" + date2);


        if (date1.equals(date2)) {
            checkDateFlag1 = true;
        }

        if (date1.compareTo(date2) < 0)// if(date1.after(date2))
        {
            Log.e("", "after current date= " + date1 + " ,checking date= " + date2);

            checkDateFlag1 = true;
        }

        return checkDateFlag1;
    }

    private TimePickerDialogSet.OnTimeSetListener timePickerListener = new TimePickerDialogSet.OnTimeSetListener() {
        @Override
        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

        }

        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {

        }
    };

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {


    }


    @Override
    public void onTimeSet(RadialPickerLayout view, final int hourOfDay, final int minute, int second) {

        Calendar now = Calendar.getInstance();
        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        int currentMinute = now.get(Calendar.MINUTE);

        Log.e("SelectedHour", "?" + hourOfDay);
        Log.e("SelectedMinutes", "?" + minute);

        Log.e("currentHour", "?" + currentHour);
        Log.e("currentMinute", "?" + currentMinute);

        int SelectedTimeInMinutes = hourOfDay * 60 + minute;
        int CurrentTimeInMinutes = currentHour * 60 + currentMinute;

        Log.e("SelectedTimeInMinutes", "?" + SelectedTimeInMinutes);
        Log.e("CurrentTimeInMinutes", "?" + CurrentTimeInMinutes);

        int Difference = SelectedTimeInMinutes - CurrentTimeInMinutes;
        int Add = SelectedTimeInMinutes + CurrentTimeInMinutes;

        Log.e("Difference", ">" + Difference);
        Log.e("Add ", ">" + Add);


        //today updateed code .//

        callFeeDialog = new Dialog(MyDateAndTimePick.this);
        callFeeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        callFeeDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        callFeeDialog.setContentView(R.layout.alert_dialog_callout_fee_timeanddate);
        acceptBtDT = (Button) callFeeDialog.findViewById(R.id.acceptBtDT);
        cancelBtDT = (Button) callFeeDialog.findViewById(R.id.cancelBtDT);

        acceptBtDT.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Appconstants.afterhours = "yes";
                updateTime(hourOfDay, minute);
                callFeeDialog.dismiss();
            }
        });

        cancelBtDT.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callFeeDialog.dismiss();
            }
        });

        if (SelectedTimeInMinutes >= 390 && SelectedTimeInMinutes <= 990) {
            callFeeDialog.dismiss();
            Appconstants.afterhours = "yes";
            updateTime(hourOfDay, minute);
        } else {
            callFeeDialog.show();
        }

        //today updated code .//

        try {

            if (GSelectedDate.compareTo(GCurrentDate) == 0) {

                if (SelectedTimeInMinutes < CurrentTimeInMinutes) {

                    Toast.makeText(MyDateAndTimePick.this, "Unable to select previous time", Toast.LENGTH_SHORT).show();

                } else if (Difference >= 0) {


                    if (Difference >= 120) {

                        if (SelectedTimeInMinutes >= 990 && SelectedTimeInMinutes <= 1439) {

                            callFeeDialog = new Dialog(MyDateAndTimePick.this);
                            callFeeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            callFeeDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                            callFeeDialog.setContentView(R.layout.alert_dialog_callout_fee_timeanddate);

                            final Button acceptBtDT = (Button) callFeeDialog.findViewById(R.id.acceptBtDT);
                            final Button cancelBtDT = (Button) callFeeDialog.findViewById(R.id.cancelBtDT);

                            acceptBtDT.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    Appconstants.afterhours = "yes";
                                    updateTime(hourOfDay, minute);
                                    callFeeDialog.dismiss();
                                }
                            });

                            cancelBtDT.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    callFeeDialog.dismiss();
                                }
                            });

                            callFeeDialog.show();


                        } else if (SelectedTimeInMinutes <= 420) {

                            callFeeDialog = new Dialog(MyDateAndTimePick.this);
                            callFeeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            callFeeDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                            callFeeDialog.setContentView(R.layout.alert_dialog_callout_fee_timeanddate);

                            final Button acceptBtDT = (Button) callFeeDialog.findViewById(R.id.acceptBtDT);
                            final Button cancelBtDT = (Button) callFeeDialog.findViewById(R.id.cancelBtDT);

                            acceptBtDT.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    Appconstants.afterhours = "yes";
                                    updateTime(hourOfDay, minute);
                                    callFeeDialog.dismiss();
                                }
                            });

                            cancelBtDT.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    callFeeDialog.dismiss();
                                }
                            });

                            callFeeDialog.show();

                        } else {
                            Appconstants.afterhours = "Both";
                            updateTime(hourOfDay, minute);
                        }

                    } else {

                        TimeDurationDialog();

                    }


                } else {

                    if (Add >= 1560) {

                        if (SelectedTimeInMinutes >= 990 && SelectedTimeInMinutes <= 1439) {

                            callFeeDialog = new Dialog(MyDateAndTimePick.this);
                            callFeeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            callFeeDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                            callFeeDialog.setContentView(R.layout.alert_dialog_callout_fee_timeanddate);

                            final Button acceptBtDT = (Button) callFeeDialog.findViewById(R.id.acceptBtDT);
                            final Button cancelBtDT = (Button) callFeeDialog.findViewById(R.id.cancelBtDT);

                            acceptBtDT.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Appconstants.afterhours = "yes";
                                    updateTime(hourOfDay, minute);
                                    callFeeDialog.dismiss();
                                }
                            });

                            cancelBtDT.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    callFeeDialog.dismiss();
                                }
                            });

                            callFeeDialog.show();


                        } else if (SelectedTimeInMinutes <= 420) {

                            callFeeDialog = new Dialog(MyDateAndTimePick.this);
                            callFeeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            callFeeDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                            callFeeDialog.setContentView(R.layout.alert_dialog_callout_fee_timeanddate);

                            final Button acceptBtDT = (Button) callFeeDialog.findViewById(R.id.acceptBtDT);
                            final Button cancelBtDT = (Button) callFeeDialog.findViewById(R.id.cancelBtDT);

                            acceptBtDT.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    Appconstants.afterhours = "yes";
                                    updateTime(hourOfDay, minute);
                                    callFeeDialog.dismiss();
                                }
                            });

                            cancelBtDT.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    callFeeDialog.dismiss();
                                }
                            });

                            callFeeDialog.show();

                        } else {

                            Appconstants.afterhours = "Both";
                            updateTime(hourOfDay, minute);
                        }

                    } else {

                        TimeDurationDialog();

                    }

                }

            } else {

                if (SelectedTimeInMinutes >= 990 && SelectedTimeInMinutes <= 1439) {

                    Toast.makeText(getApplicationContext(), "15!!!", Toast.LENGTH_SHORT).show();
                    callFeeDialog = new Dialog(MyDateAndTimePick.this);
                    callFeeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    callFeeDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    callFeeDialog.setContentView(R.layout.alert_dialog_callout_fee_timeanddate);

                    final Button acceptBtDT = (Button) callFeeDialog.findViewById(R.id.acceptBtDT);
                    final Button cancelBtDT = (Button) callFeeDialog.findViewById(R.id.cancelBtDT);

                    acceptBtDT.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(), "s4!!!", Toast.LENGTH_SHORT).show();
                            Appconstants.afterhours = "yes";
                            updateTime(hourOfDay, minute);
                            callFeeDialog.dismiss();
                        }
                    });

                    cancelBtDT.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callFeeDialog.dismiss();
                        }
                    });

                    callFeeDialog.show();


                } else if (SelectedTimeInMinutes <= 420) {

                    Toast.makeText(getApplicationContext(), "16!!!", Toast.LENGTH_SHORT).show();


                    callFeeDialog = new Dialog(MyDateAndTimePick.this);
                    callFeeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    callFeeDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    callFeeDialog.setContentView(R.layout.alert_dialog_callout_fee_timeanddate);

                    final Button acceptBtDT = (Button) callFeeDialog.findViewById(R.id.acceptBtDT);
                    final Button cancelBtDT = (Button) callFeeDialog.findViewById(R.id.cancelBtDT);

                    acceptBtDT.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(), "s5!!!", Toast.LENGTH_SHORT).show();
                            Appconstants.afterhours = "yes";
                            updateTime(hourOfDay, minute);
                            callFeeDialog.dismiss();
                        }
                    });

                    cancelBtDT.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callFeeDialog.dismiss();
                        }
                    });

                    callFeeDialog.show();

                } else {
                    Appconstants.afterhours = "Both";
                    updateTime(hourOfDay, minute);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':').append(minutes).append(" ").append(timeSet).toString();
        Appconstants.TIME_SELECTED = aTime;
        picktimetxtview.setText(aTime);

    }

    private void TimeDurationDialog() {

        callFeeDialog = new Dialog(MyDateAndTimePick.this);
        callFeeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        callFeeDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        callFeeDialog.setContentView(R.layout.alert_dialog_call_out_fee);
        TextView YourText = (TextView) callFeeDialog.findViewById(R.id.YourText);

        YourText.setText("Please select time after 2 hours from current Time");

        final Button okBt = (Button) callFeeDialog.findViewById(R.id.okBt);

        okBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFeeDialog.dismiss();
            }
        });

        callFeeDialog.show();

    }

}
