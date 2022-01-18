package com.cliffex.Fixezi.util;

import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import com.cliffex.Fixezi.BuildConfig;
import com.cliffex.Fixezi.helper.DataManager;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ProjectUtil {

    private static ProgressDialog mProgressDialog;

    public static Dialog showProgressDialog(Context context, boolean isCancelable, String message) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
        mProgressDialog.setCancelable(isCancelable);
        return mProgressDialog;
    }

    public static String getRealPathFromURI(Context mContext, Uri contentUri) {

        String stringPath = null;

        try {
            if (contentUri.getScheme().toString().compareTo("content") == 0) {
                String[] proj = {MediaStore.Images.Media.DATA};
                CursorLoader loader = new CursorLoader(((Activity) mContext), contentUri, proj, null, null, null);
                Cursor cursor = loader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                stringPath = cursor.getString(column_index);
                cursor.close();
            } else if (contentUri.getScheme().compareTo("file") == 0) {
                stringPath = contentUri.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringPath;

    }

//    public static String getPolyLineUrl(Context context, LatLng origin, LatLng dest) {
//        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
//        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
//        String sensor = "sensor=false";
//        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&key=" + context.getResources().getString(R.string.places_api_key);
//        String output = "json";
//        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
//        Log.e("PathURL","====>"+url);
//        return url;
//    }

    public static void requestPermissions(Context mContext) {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                ActivityCompat.requestPermissions(
                        ((Activity) mContext), new String[]{
                                Manifest.permission.CAMERA
                        }, 101);
            } else {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", mContext.getApplicationContext().getPackageName())));
                    ((Activity) mContext).startActivityForResult(intent, 2296);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    ((Activity) mContext).startActivityForResult(intent, 2296);
                }
            }
        } else {
            // below android 11
            ActivityCompat.requestPermissions(
                    ((Activity) mContext), new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    }, 101);
        }

    }

    public static void openGallery(Context mContext, int GALLERY) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        ((Activity) mContext).startActivityForResult(Intent.createChooser(intent, "Select Image"), GALLERY);
    }

    public static String openCamera(Context mContext, int CAMERA) {

        File dirtostoreFile = new File(Environment.getExternalStorageDirectory() + "/fixezi/Images/");

        if (!dirtostoreFile.exists()) {
            dirtostoreFile.mkdirs();
        }

        String timestr = DataManager.getInstance().convertDateToString(Calendar.getInstance().getTimeInMillis());

        File tostoreFile = new File(Environment.getExternalStorageDirectory() + "/fixezi/Images/" + "IMG_" + timestr + ".jpg");

        String str_image_path = tostoreFile.getPath();

        Uri uriSavedImage = FileProvider.getUriForFile(Objects.requireNonNull(((Activity) mContext)),
                BuildConfig.APPLICATION_ID + ".provider", tostoreFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        ((Activity) mContext).startActivityForResult(intent, CAMERA);

        return str_image_path;

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//
//            File dirtostoreFile = new File(Environment.getExternalStorageDirectory() + "/amanahdelivery/Images/");
//
//            if (!dirtostoreFile.exists()) {
//                dirtostoreFile.mkdirs();
//            }
//
//            String timestr = DataManager.getInstance().convertDateToString(Calendar.getInstance().getTimeInMillis());
//
//            File tostoreFile = new File(Environment.getExternalStorageDirectory() + "/amanahdelivery/Images/" + "IMG_" + timestr + ".jpg");
//
//            Log.e("fsasdasda","tostoreFile = " + tostoreFile.getPath());
//
//            Toast.makeText(mContext, tostoreFile.getPath() , Toast.LENGTH_SHORT).show();
//
//            ContentValues values = new ContentValues(1);
//            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
//            // Uri outputFileUri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//            Uri uriSavedImage = FileProvider.getUriForFile(Objects.requireNonNull(((Activity) mContext)),
//                    BuildConfig.APPLICATION_ID + ".provider", tostoreFile);
//
//            // Uri outputFileUri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            captureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
//            ((Activity) mContext).startActivityForResult(captureIntent, CAMERA);
//
//            return tostoreFile.getPath();
//        } else {
//
//        }

    }

    public static void pauseProgressDialog() {
        try {
            if (mProgressDialog != null) {
                mProgressDialog.cancel();
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }

//    public static void changeStatusBarColor(Activity activity) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.orange, activity.getTheme()));
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.orange));
//        }
//    }

    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = dateFormat.format(new Date());
        return formattedDate;
    }

    public static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        String formattedDate = dateFormat.format(new Date());
        return formattedDate;
    }

    public static long getTimeInMillSec(String givenDateString){
        long timeInMilliseconds = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date mDate = sdf.parse(givenDateString);
            timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }

//    public static void TimePicker(Context context, onDateSetListener listene){
//        Calendar mcurrentTime = Calendar.getInstance();
//        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//        int minute = mcurrentTime.get(Calendar.MINUTE);
//        TimePickerDialog mTimePicker;
//        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                listene.SelectedDate(String.valueOf(getTimeInMillSec(selectedHour+":"+selectedMinute)));
//            }
//        }, hour, minute, true);
//        mTimePicker.setTitle("Set arrival time");
//        mTimePicker.show();
//    }

    public static String reduceDateByDays(String dateString, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            cal.setTime(s.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.add(Calendar.DAY_OF_MONTH, -days);
        return s.format(cal.getTime());
    }

//    public static void DatePicker(long minDateMilliseconds,Context context, onDateSetListener listener) {
//        final Calendar myCalendar = Calendar.getInstance();
//        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                // TODO Auto-generated method stub
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                String myFormat = "dd-MMM-yyyy"; // your format
//                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
//                listener.SelectedDate(sdf.format(myCalendar.getTime()));
//            }
//
//        };
//        DatePickerDialog datePickerDialog = new DatePickerDialog(context, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                myCalendar.get(Calendar.DAY_OF_MONTH));
//        if(minDateMilliseconds != 0)
//            datePickerDialog.getDatePicker().setMinDate(minDateMilliseconds);
//        else
//            datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
//        datePickerDialog.show();
//    }

    public static String getCompleteAddressString(Context context, double LATITUDE, double LONGITUDE) {
        String strAdd = "getting address...";
        if (context != null) {
            Geocoder geocoder = new Geocoder(context.getApplicationContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
                if (addresses != null) {
                    Address returnedAddress = addresses.get(0);
                    StringBuilder strReturnedAddress = new StringBuilder("");

                    for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                    }
                    strAdd = strReturnedAddress.toString();
                    Log.w("My Current address", strReturnedAddress.toString());
                } else {
                    strAdd = "No Address Found";
                    Log.w("My Current address", "No Address returned!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                strAdd = "Cant get Address";
                Log.w("My Current address", "Canont get Address!");
            }
        }
        return strAdd;
    }

}
