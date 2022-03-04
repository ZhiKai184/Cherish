package com.sp.p2002640_cherish;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class StorageHelper extends SQLiteOpenHelper {
    public StorageHelper(@Nullable Context context) {
        super(context, StorageConstants.DB_NAME, null, StorageConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(StorageConstants.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+StorageConstants.TABLE_NAME);
        onCreate(db);
    }

    // insert info function
    public long insertInfo(String name, String date, String price, String weight, byte[] image,String addTimeStamp, String updateTimeStamp) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(StorageConstants.C_NAME, name);
        cv.put(StorageConstants.C_DATE, date);
        cv.put(StorageConstants.C_PRICE, price);
        cv.put(StorageConstants.C_WEIGHT, weight);
        cv.put(StorageConstants.C_ADD_TIMESTAMP, addTimeStamp);
        cv.put(StorageConstants.C_UPDATE_TIMESTAMP, updateTimeStamp);
        cv.put(StorageConstants.C_IMAGE, image);



        long id = db.insert(StorageConstants.TABLE_NAME, StorageConstants.C_NAME, cv);
        db.close();
        return id;
    }


    // update function
    public void updateInfo(String id, String name, String date, String price, String weight, byte[] image,String addTimeStamp, String updateTimeStamp) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(StorageConstants.C_NAME, name);
        cv.put(StorageConstants.C_DATE, date);
        cv.put(StorageConstants.C_PRICE, price);
        cv.put(StorageConstants.C_WEIGHT, weight);
        cv.put(StorageConstants.C_IMAGE, image);
        cv.put(StorageConstants.C_ADD_TIMESTAMP, addTimeStamp);
        cv.put(StorageConstants.C_UPDATE_TIMESTAMP, updateTimeStamp);
        db.update(StorageConstants.TABLE_NAME, cv, StorageConstants.C_ID + " =?", new String[]{id});
        db.close();
    }

    public void deleteInfo(String id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(StorageConstants.TABLE_NAME, StorageConstants.C_ID + " = ? ", new String[]{id});
        db.close();
    }


    public ArrayList<StorageModel> getAllData (String orderBy) {
        ArrayList<StorageModel> arrayList = new ArrayList<>();

        // query for select all info in database
        String selectQuery = "SELECT * FROM " + StorageConstants.TABLE_NAME + " ORDER BY " + orderBy;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        // curate the data from all the column in each row, moveToNext() -> Move the cursor to the next row, start from index -1
        if(cursor.moveToNext()) {
            do {
                StorageModel model = new StorageModel(
                        cursor.getString(cursor.getColumnIndexOrThrow(StorageConstants.C_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(StorageConstants.C_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(StorageConstants.C_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(StorageConstants.C_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(StorageConstants.C_WEIGHT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(StorageConstants.C_ADD_TIMESTAMP)),
                        cursor.getString(cursor.getColumnIndexOrThrow(StorageConstants.C_UPDATE_TIMESTAMP)),
                        cursor.getBlob(cursor.getColumnIndexOrThrow(StorageConstants.C_IMAGE))
                );

                arrayList.add(model);
            } while (cursor.moveToNext());
        }

        db.close();
        return arrayList;
    }
}
