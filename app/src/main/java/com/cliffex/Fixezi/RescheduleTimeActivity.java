package com.cliffex.Fixezi;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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
import com.cliffex.Fixezi.MyUtils.InternetDetect;

public class RescheduleTimeActivity extends AppCompatActivity implements
        TimePickerDialogSet.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    TextView toolbar_title, picktimetxtview;
    Button dialogacceptbtn;
    String strSelectedDate = "";
    Toolbar datetimetoolbar;
    Date GCurrentDate, GSelectedDate;
    boolean istrue;
    Dialog callFeeDialog;
    int currentyear;
    RelativeLayout picktimetxtviewlayyy;
    RelativeLayout NavigationUpIM;
    Button yeslayyout, nolayouttt, yeslayyout1, nolayouttt1;
    MFCalendarView mf;
    ImageView CallOutFeeInfoDT, TimeFlexInfoIM, DateFlexInfoIM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datetimedialog);
        Appconstants.DATE_SELECTED = "";
        Appconstants.TIME_SELECTED = "";
        Appconstants.afterhours = "";
        Appconstants.dateFlexi = "";
        Appconstants.timeFlexi = "";

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

        setSupportActionBar(datetimetoolbar);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        toolbar_title = (TextView) datetimetoolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Set date & time");

        yeslayyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                yeslayyout.setBackgroundResource(R.drawable.border_black_solid_green_two);
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


                    String[] strings = {"Flexible upto 2 hrs", "Flexible upto 2-4 hrs", "I'm free all day", "I'm free all week"};
                    new AlertDialog.Builder(RescheduleTimeActivity.this)
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

                                    yeslayyout1.setBackgroundResource(R.drawable.border_black_solid_green_two);
                                    yeslayyout1.setTextColor(Color.parseColor("#ffffff"));
                                    nolayouttt1.setBackgroundResource(R.drawable.border_black_solid_white);
                                    nolayouttt1.setTextColor(Color.parseColor("#000000"));
                                    Appconstants.timeFlexi = "yes";


                                }
                            }).show();

                }
            }


            public boolean ValidationSuccess() {

                if (Appconstants.TIME_SELECTED.equalsIgnoreCase("")) {
                    Toast.makeText(RescheduleTimeActivity.this, "Please select time first", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }

        });

        CallOutFeeInfoDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog YourAddDialog = new Dialog(RescheduleTimeActivity.this);
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

                final Dialog YourAddDialog = new Dialog(RescheduleTimeActivity.this);
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


                final Dialog YourAddDialog = new Dialog(RescheduleTimeActivity.this);
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

                nolayouttt.setBackgroundResource(R.drawable.border_black_solid_green_two);
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

                    nolayouttt1.setBackgroundResource(R.drawable.border_black_solid_green_two);
                    nolayouttt1.setTextColor(Color.parseColor("#ffffff"));
                    yeslayyout1.setBackgroundResource(R.drawable.border_black_solid_white);
                    yeslayyout1.setTextColor(Color.parseColor("#000000"));
                    Appconstants.timeFlexi = "no";

                }

            }

            public boolean ValidationSuccess() {

                if (Appconstants.TIME_SELECTED.equalsIgnoreCase("")) {
                    Toast.makeText(RescheduleTimeActivity.this, "Please select time first", Toast.LENGTH_SHORT).show();
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
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }

            public boolean ValidationSuccess() {

                if (Appconstants.DATE_SELECTED.equalsIgnoreCase("")) {
                    Toast.makeText(RescheduleTimeActivity.this, "Select date", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (Appconstants.TIME_SELECTED.equalsIgnoreCase("")) {
                    Toast.makeText(RescheduleTimeActivity.this, "Select Time", Toast.LENGTH_SHORT).show();
                    return false;
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
            public void onDisplayedMonthChanged(int month, int year,
                                                String monthStr) {
                StringBuffer bf = new StringBuffer().append(" month:")
                        .append(month).append(" year:").append(year)
                        .append(" monthStr: ").append(monthStr);

                if (month > 9) {


//                    setDotMethods(year + "-" + (month));
                    strSelectedDate = year + "-" + (month) + "-" + "01";
                } else {
//                    setDotMethods(year + "-0" + (month));// for dot
                    strSelectedDate = year + "-0" + (month) + "-" + "01";
                }

            }

            @Override
            public void onDateChanged(String date) {
                strSelectedDate = date;
                Log.e("Check this one", "slected strSelectedDate= " + strSelectedDate);
                mf.setDate(strSelectedDate);
//                strSelectedDate= 2016-05-18


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
                    Log.e("Valid DAte", "YES");

                    if (WhichDay.equalsIgnoreCase("Saturday") || WhichDay.equalsIgnoreCase("Sunday")) {

                        final Dialog SatSunDialog = new Dialog(RescheduleTimeActivity.this);
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


        picktimetxtviewlayyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ValidationSuccess()) {


                    Calendar now = Calendar.getInstance();
                    TimePickerDialogSet tpd = TimePickerDialogSet.newInstance(RescheduleTimeActivity.this, now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE), false);

                    tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            Log.e("TimePicker", "Dialog was cancelled");
                        }
                    });
                    tpd.show(getFragmentManager(), "Timepickerdialog");


                }
            }


            public boolean ValidationSuccess() {

                if (Appconstants.DATE_SELECTED.equalsIgnoreCase("")) {
                    Toast.makeText(RescheduleTimeActivity.this, "Please select date first", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }


        });


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


//		String sy="",sm="",sd="";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date2 = null, date1 = null;

        try {

//			for testing
//			if((month_date.equals("2")||month_date.equals("02"))&&day_date.equals("21"))
//			{
//				month_date="05";
//			}

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


        if (GSelectedDate.compareTo(GCurrentDate) == 0) {


            if (SelectedTimeInMinutes < CurrentTimeInMinutes) {
                Toast.makeText(RescheduleTimeActivity.this, "Unable to select previous time", Toast.LENGTH_SHORT).show();
            } else if (Difference >= 0) {

                if (Difference >= 120) {


                    if (SelectedTimeInMinutes >= 990 && SelectedTimeInMinutes <= 1439) {

                        callFeeDialog = new Dialog(RescheduleTimeActivity.this);
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


                        callFeeDialog = new Dialog(RescheduleTimeActivity.this);
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

                        callFeeDialog = new Dialog(RescheduleTimeActivity.this);
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


                        callFeeDialog = new Dialog(RescheduleTimeActivity.this);
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

                callFeeDialog = new Dialog(RescheduleTimeActivity.this);
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


                callFeeDialog = new Dialog(RescheduleTimeActivity.this);
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
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();


        Appconstants.TIME_SELECTED = aTime;
        picktimetxtview.setText(aTime);

    }


    private void TimeDurationDialog() {

        callFeeDialog = new Dialog(RescheduleTimeActivity.this);
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

