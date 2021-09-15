package com.cliffex.Fixezi.helpus;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.legacy.content.WakefulBroadcastReceiver;
import android.util.Log;


/**
 * This receiver is called if an alarm was triggerd from android.
 * It also contains methods to set and cancel alarms.
 * @author Matthias
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    /**
     * the AlarmManager, which provides access to the system alarm services.
     */
    private AlarmManager alarmMgr;
    /**
     * a pending intent which is triggered when the alarm is fired
     */
    private PendingIntent alarmIntent;

    /**
     * Starts the ConditionService.
     * It is called when an alarm is triggerd.
     * @param context the context
     * @param intent the intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", "onReceive fired!");
        // if an alarm is triggered
        // create a new intent
        Intent service = new Intent(context, ConditionService.class);
        // pass the action and the extras
        service.setAction(intent.getAction());
        service.putExtras(intent);
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, service);
    }

    /**
     * Sets an alarm.
     * @param context the context
     * @param conditionTime the time condition for which the alarm shall be set
     */
    public void setAlarm(Context context, DBConditionTime conditionTime){
        // if there has already been an alarm, cancel it
        cancelAlarm();
        // get the android Alarmmanager
        if(alarmMgr == null)
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        // create a new intent for this
        Intent intent = new Intent(context, AlarmReceiver.class);
        // set the intent action to the conditionID to know which rule's timecondition was triggered
        intent.setAction(ConditionService.CHECKCONDITIONTIME);
        intent.putExtra("conditionId", conditionTime.getId());
        // set a broadcast id to allow multiple alarms :-)
        alarmIntent = PendingIntent.getBroadcast(context, (int) conditionTime.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        // set the android alarm (RTC = real time clock)
        alarmMgr.set(AlarmManager.RTC_WAKEUP, conditionTime.getStart().getTimeInMillis(), alarmIntent);
        Log.d("AlarmReceiver", "setAlarm with action: " + intent.getAction() + ": " + conditionTime.getStart().toString());
    }

    /**
     * Cancels the alarm set by this receiver.
     */
    public void cancelAlarm(){
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }
    }
}
