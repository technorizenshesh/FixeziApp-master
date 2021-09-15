package com.cliffex.Fixezi.helpus;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * This action creates a push notification with a specific text.
 * @author: Matthias
 */
public class DBActionNotification extends DBAction {
    /**
     * the text the notification displays.
     */
    private String message;

    /**
     * Selects all notification actions from the database which are assigned to a given rule.
     * @param ruleId the id of the rule for which the message actions shall be selected
     * @return an arraylist of the notification actions fetched from the database
     */
    public static ArrayList<DBAction> selectAllFromDB(long ruleId){
        ArrayList<DBAction> actions = new ArrayList<DBAction>();
        // read from database
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        String[] columns = {
                DBHelper.COLUMN_ACTION_NOTIFICATION_ID,
                DBHelper.COLUMN_MESSAGE,
                DBHelper.COLUMN_ACTIVE
        };
        String where = DBHelper.COLUMN_RULE_ID + " = ?";
        String[] whereArgs = {String.valueOf(ruleId)};
        Cursor cursor = db.query(DBHelper.TABLE_ACTION_NOTIFICATION, columns, where, whereArgs, null, null, null);
        // read result
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            DBActionNotification action = new DBActionNotification(cursor.getLong(0), cursor.getString(1), cursor.getInt(2) != 0);
            actions.add(action);
            cursor.moveToNext();
        }
        return actions;
    }

    /**
     * Selects a specific notification action from the database.
     * @param id the id of the notification action
     * @return the fetched notification action
     */
    public static DBActionNotification selectFromDB(long id) {
        // read from database
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        String[] columns = {
                DBHelper.COLUMN_ACTION_NOTIFICATION_ID,
                DBHelper.COLUMN_MESSAGE,
                DBHelper.COLUMN_ACTIVE
        };
        String where = DBHelper.COLUMN_ACTION_NOTIFICATION_ID + " = ?";
        String[] whereArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DBHelper.TABLE_ACTION_NOTIFICATION, columns, where, whereArgs, null, null, null);
        // read result
        cursor.moveToFirst();
        if(cursor.isAfterLast()) return null;
        DBActionNotification action = new DBActionNotification(cursor.getLong(0), cursor.getString(1), cursor.getInt(2) != 0);
        return action;
    }

    public DBActionNotification(){

    }

    /**
     * creates the notification.
     */
    @Override
    protected void doActionStart() {
        NotificationFactory.createNotification(ContextManager.getContext(), getRule().getName(), message, false);
    }

    /**
     * Creates a new notification action.
     * Use this to create notification actions fetched from the database.
     * @param id the id of the notification action
     * @param message the text of the notification
     * @param active the flag whether the action is active
     */
    public DBActionNotification(long id, String message, boolean active){
        super(id, active);
        this.message = message;
    }

    /**
     * Inserts the notification action into the database.
     * @param db the reference to the sqlite database
     * @return the id of the inserted notification action
     */
    @Override
    protected long insertIntoDB(SQLiteDatabase db) throws Exception {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_MESSAGE, message);
        values.put(DBHelper.COLUMN_ACTIVE, isActive());
        values.put(DBHelper.COLUMN_RULE_ID, getRule().getId());
        return db.insert(DBHelper.TABLE_ACTION_NOTIFICATION, null, values);
    }

    /**
     * Updates the notification action on the database.
     * @param db the reference to the sqlite database
     */
    @Override
    protected void updateOnDB(SQLiteDatabase db) throws Exception {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_MESSAGE, message);
        values.put(DBHelper.COLUMN_ACTIVE, isActive());
        if(getRule()!= null){
            values.put(DBHelper.COLUMN_RULE_ID, getRule().getId());
        }
        String where = DBHelper.COLUMN_ACTION_NOTIFICATION_ID + " = ?";
        String[] whereArgs = {String.valueOf(getId())};
        db.update(DBHelper.TABLE_ACTION_NOTIFICATION, values, where, whereArgs);
    }

    /**
     * Deletes the notification action from the database.
     */
    @Override
    public void deleteFromDB() {
        try {
            SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
            String where = DBHelper.COLUMN_ACTION_NOTIFICATION_ID + " = ?";
            String[] whereArgs = {String.valueOf(getId())};
            db.delete(DBHelper.TABLE_ACTION_NOTIFICATION, where, whereArgs);
        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(ContextManager.getContext(), "Couldn't delete notification action from Server!", Toast.LENGTH_SHORT);
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
