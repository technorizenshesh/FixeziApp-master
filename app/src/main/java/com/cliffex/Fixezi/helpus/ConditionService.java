package com.cliffex.Fixezi.helpus;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

/**
 * Handles conditions and sets conditions on startup.
 * @author Matthias
 */
public class ConditionService extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {
    public static final String TAG = "CheckConditionService";
    public static final String AUTO_START = "Autostart";
    public static final String ADDGEO = "AddGeofence";
    public static final String STARTAPP = "StartApp";
    public static final String REMOVEGEO = "RemoveGeofence";
    public static final String CHECKCONDITIONTIME = "CheckConditionTime";
    public static final String UPDATEGEO = "UpdateGeofence";
    private GeofencingClient geofencingClient;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<DBFence> mDBGeofenceList;
    private ArrayList<DBCondition> dbConditionFences;
    private ArrayList mGeofenceList = new ArrayList();
    private LocationRequest mLocationRequest;
    private static PendingIntent mPendingIntent;
    public static Location gLastLocation;
    private Boolean updateFence = false;
    private Boolean removeFence = false;
    private Boolean registerFence = false;
    private Boolean connected = false;
    private DBFence fenceToRemove = null;

    //mappingtable
    private HashMap<String, Integer> typeMapping;

    public ConditionService() {
        super(TAG);
    }

    /**
     * onCreate
     * set up Type Mapping
     */
    public void onCreate() {
        super.onCreate();
        ContextManager.setContext(this);
        if (mPendingIntent == null) {
            createPendingIntent();
        }
        setUpTypeMapping();
    }

    /**
     * setUp MappingTable - enter,stay, exit commands
     */
    private void setUpTypeMapping() {
        typeMapping = new HashMap<String, Integer>();
        typeMapping.put(DBConditionFence.TYPE_ENTER, Geofence.GEOFENCE_TRANSITION_ENTER);
        typeMapping.put(DBConditionFence.TYPE_LEAVE, Geofence.GEOFENCE_TRANSITION_EXIT);
        typeMapping.put(DBConditionFence.TYPE_STAY, Geofence.GEOFENCE_TRANSITION_DWELL);
    }

    /**
     * Open Connection to GoogleApi
     */
    private void connectToGoogleAPI() {
        // get GoogleApiClient
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void createPendingIntent() {
        Intent intent = new Intent(ContextManager.getContext(), ConditionService.class);
        mPendingIntent = PendingIntent.getService(ContextManager.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Handles the intent either fired by an alarm triggered or an autostart.
     * @param intent informations that are given to the service, can has the attributes Action
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent fired!");
        // register context
        ContextManager.setContext(this);
        try {
            switch (intent.getAction()) {
                case AUTO_START:
                    handleAutoStart(intent);
                    break;
                case STARTAPP:
                    handleStartApp(intent);
                    break;
                case CHECKCONDITIONTIME:
                    handleCheckConditionTime(intent);
                    break;
                case ADDGEO:
                    handleAddGeofence(intent);
                    break;
                case REMOVEGEO:
                    handleRemoveGeofence(intent);
                    break;
                case UPDATEGEO:
                    handleUpdateGeofence(intent);
                    break;
                default:
                    Log.e("ERROR", "No matching string found");
            }
        } catch (Exception e) {
            // check if geofenceevent was triggerd
            handleGeofence(intent);
        }
    }

    /**
     * update geofences
     * @param intent informations that are given to the service
     */
    private void handleUpdateGeofence(Intent intent) {
        updateFence = true;
        Bundle bundle = intent.getExtras();
        long fenceId = bundle.getLong("DBFenceID", -1);
        if (fenceId != -1) {
            DBFence fence = DBFence.selectFromDB(fenceId);
            fenceToRemove = fence;
            removeFence = true;
            handleAddGeofence(intent);
        }
    }

    /**
     * handle event geofence triggerd
     * check conditions and perform action
     * @param intent informations that are given to the service
     */
    private void handleGeofence(Intent intent) {
        Log.d("ConditionService", "Handle Geofence!");
        try {
            GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
            if (geofencingEvent.hasError()) {
                Log.d("Maps/Geofencing", String.valueOf(geofencingEvent.getErrorCode()));
                return;
            }


            // Get the transition type.
            int geofenceTransition = geofencingEvent.getGeofenceTransition();
            if (geofenceTransition != -1) {
                // Get the geofences that were triggered. A single event can trigger
                // multiple geofences.
                List triggeringGeofences = geofencingEvent.getTriggeringGeofences();
                for (Object object : triggeringGeofences) {
                    Geofence geofence = (Geofence) object;
                    long id = -1;
                    try {
                        id = Long.parseLong(geofence.getRequestId());
                    } catch (Exception e) {
                        Log.e("ERROR", "Long parse error");
                        e.printStackTrace();
                    }
                    if (id != -1) {
                        DBFence fence = DBFence.selectFromDB(id);
                        DBConditionFence conditionFence = DBConditionFence.selectFromDB(fence);
                        performAction(conditionFence);
                    }
                }

                // Send notification and log the transition details.
                Log.d("ConditionService/Geo", "Handle geofence finish");
            }

        } catch (Exception e) {
            Log.e("ERROR", "Error handling Geofences");
            e.printStackTrace();
        }
    }

    /**
     * remove geofence
     * @param intent informations that are given to the service
     */
    private void handleRemoveGeofence(Intent intent) {
        Bundle bundle = intent.getExtras();
        removeFence = true;
        //remove fence from db
        long fenceId = bundle.getLong("DBFenceID", -1);
        if (fenceId != -1) {
            DBFence fence = DBFence.selectFromDB(fenceId);
            long conditionFenceId = bundle.getLong("DBConditionFenceID", -1);
            if (conditionFenceId != -1) {
                DBConditionFence conditionFence = DBConditionFence.selectFromDB(conditionFenceId);
                conditionFence.removeFence(fence);
                conditionFence.writeToDB();
                fenceToRemove = fence;
            }
        }
        //connect to google api
        connectToGoogleAPI();
    }

    /**
     * remove geofence from googleAPI
     */
    private void removeGeofence() {
        // remove Geofence from GoogleApiScoupe
        List<String> fences = new ArrayList<String>();
        fences.add(String.valueOf(fenceToRemove.getId()));
        try {
            LocationServices.GeofencingApi.removeGeofences(mGoogleApiClient, fences);
            removeFence = false;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR", "Romoving Geofence: Google API is not connected");
        }
        if (!updateFence) {
            // delete Geofence from DB
            fenceToRemove.deleteFromDB();
        }
        fenceToRemove = null;
    }

    /**
     * add geofence to googleAPI
     * @param intent informations that are given to the service
     */
    private void handleAddGeofence(Intent intent) {
        Bundle bundle = intent.getExtras();
        long fenceId = bundle.getLong("DBFenceID", -1);
        if (fenceId != -1) {
            long conditionFenceId = bundle.getLong("DBConditionFenceID", -1);
            if (conditionFenceId != -1) {
                DBFence fence = DBFence.selectFromDB(fenceId);
                DBConditionFence conditionFence = DBConditionFence.selectFromDB(conditionFenceId);
                mGeofenceList.add(new Geofence.Builder()
                        .setRequestId(String.valueOf(fence.getId()))
                        .setTransitionTypes(typeMapping.get(conditionFence.getType()))
                        .setCircularRegion(fence.getLatitude(), fence.getLongitude(), fence.getRadius())
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setLoiteringDelay(500)
                        .build());
            }
        }
        if (mPendingIntent == null) {
            Log.e("ERROR", "PendingIntent must be specified!");
        }
        registerFence = true;
        if (mGoogleApiClient == null) {
            connectToGoogleAPI();
        } else {
            registerGeofences();
            registerFence = false;
        }

        Log.d("ConditionService/add", "AddGeofence succ");
    }

    /**
     * handle start app to get current position
     * @param intent informations that are given to the service
     */
    private void handleStartApp(Intent intent) {
        //connect to Google
        connectToGoogleAPI();
    }

    /**
     * set up geofenceList with all active geofences
     */
    private void setUpGeofenceList() {
        registerFence = true;
        // get all active ContionFences from DB
        try {
            dbConditionFences = DBConditionFence.selectAllFromDB();
        } catch (Exception e) {
            Log.e("Error", "Can't get ConditionFences from DB");
            e.printStackTrace();
        }
        for (int i = 0; i < dbConditionFences.size(); i++) {
            DBConditionFence cf = (DBConditionFence) dbConditionFences.get(i);
            cf.loadAllFences();
            ArrayList<DBFence> fences = cf.getFences();
            for (int j = 0; j < fences.size(); j++) {
                mGeofenceList.add(new Geofence.Builder()
                        .setRequestId(String.valueOf(fences.get(j).getId()))
                        .setTransitionTypes(typeMapping.get(cf.getType()))
                        .setCircularRegion(fences.get(j).getLatitude(), fences.get(j).getLongitude(), fences.get(j).getRadius())
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setLoiteringDelay(500)
                        .build());
            }
        }
        Log.d("ConditionService", "Setup List successful");
    }

    /**
     * Checks the conditions and fires the actions if all conditions are met after an alarm was triggered.
     */
    private void handleCheckConditionTime(Intent intent) {
        // get Id out of intent
        long id = intent.getLongExtra("conditionId", 0);
        // get the corresponding condition
        DBConditionTime condition = DBConditionTime.selectFromDB(id);
        if (condition == null) return;
        Log.d(TAG, "Condition: " + condition.getName());
        // reset the alarm
        condition.updateAlarm();
        performAction(condition);
    }

    /**
     * performe action if event trigger
     * @param condition a DBCondition Object that has to be checked
     */
    private void performAction(DBCondition condition) {

        try {
            // get connection
            connectToGoogleAPI();
            for (int i = 0; i < 100; i++) {
                if (connected) {
                    break;
                } else {
                    Thread.sleep(100);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DBHelper.getInstance().logDB();
        // get the corresponding rules
        ArrayList<DBRule> rules = DBRule.selectFromDB(condition);
        Log.d(TAG, "Number of rules to be checked: " + rules.size());
        // loop through the rules and check whether conditions are met
        for (int i = 0; i < rules.size(); i++) {
            if (rules.get(i).allConditionsMet()) { // if all conditions are met
                rules.get(i).startAllActions();
            }
        }
    }

    /**
     * registers all alarms after a reboot of the phone.
     * @param intent informations that are given to the service
     */
    private void handleAutoStart(Intent intent) {
        NotificationFactory.createNotification(this, "Autostart", "Willkommen!", false);
        // execute rules for which the conditions are met
        ArrayList<DBRule> rules = DBRule.selectAllFromDB();
        for (int i = 0; i < rules.size(); i++) {
            DBRule rule = rules.get(i);
            if (rule.allConditionsMet()) {
                rule.startAllActions();
            }
        }
        // register alarms
        for (int i = 0; i < rules.size(); i++) {
            rules.get(i).registerAllAlarms();
        }

        // register all geofences
        setUpGeofenceList();
        connectToGoogleAPI();
    }

    /**
     * register geofence at google
     */
    @SuppressLint("MissingPermission")
    private void registerGeofences() {
        geofencingClient = LocationServices.getGeofencingClient(this);
        // start geofencing
        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    getGeofencingRequest(),
                    mPendingIntent
            ).setResultCallback(this);
        } catch (Exception e) {
            Log.e("ERROR", "Register Geofences failed");
            e.printStackTrace();
        }
        registerFence = false;
        Log.d("ConditionService/Reg", "Geofence Registration succ");

    }

    /**
     * create geofencingrequest
     * @return a geofencingRequest with all information about the geofences
     */
    private GeofencingRequest getGeofencingRequest() {
        // create GeofenceRequest
        try {
            GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
            builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
            builder.addGeofences(mGeofenceList);
            return builder.build();
        } catch (Exception e) {
            Log.e("ERROR", "No geofences has been added to this request");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * performe action if connection to googleAPI is successful
     * @param bundle added information about the connection
     */
    @Override
    public void onConnected(Bundle bundle) {
        connected = true;
        getLastLocation();
        Log.d("ConditionService", "Connection to GoogleAPI successful");
        if (mPendingIntent != null) {
            if (removeFence) {
                removeGeofence();
            }
            if (registerFence) {
                registerGeofences();
            }
        }
    }

    /**
     * set last known location in var gLastLocation
     */
    private void getLastLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            gLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * connection suspended
     * @param i
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.d("ConditionService","Connection to GoogleAPI suspended");
    }

    /**
     * connection failed
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("ConditionService","Connection to GoogleAPI failed");
    }

    /**
     * result status when geofence was added to googleAPI
     * @param status
     */
    @Override
    public void onResult(Status status) {
        Log.d("ConditionService","Add Geofence: " + status.toString());
    }
}
