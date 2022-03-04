package com.sp.p2002640_cherish;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ShoppingHelper extends SQLiteOpenHelper {
    public ShoppingHelper(@Nullable Context context) {
        super(context, ShoppingConstants.DB_NAME, null, ShoppingConstants.DB_VERSION);
}
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ShoppingConstants.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ShoppingConstants.TABLE_NAME);
        onCreate(db);
    }

    // insert info function
    public long insertInfo(String name_shopping, String quantity, String type, byte[] image,String addTimeStamp_shopping, String updateTimeStamp_shopping) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ShoppingConstants.C_NAME_SHOPPING, name_shopping);
        cv.put(ShoppingConstants.C_QUANTITY, quantity);
        cv.put(ShoppingConstants.C_TYPE, type);
        cv.put(ShoppingConstants.C_ADD_TIMESTAMP_SHOPPING, addTimeStamp_shopping);
        cv.put(ShoppingConstants.C_UPDATE_TIMESTAMP_SHOPPING, updateTimeStamp_shopping);
        cv.put(ShoppingConstants.C_IMAGE_SHOPPING, image);

        long id = db.insert(ShoppingConstants.TABLE_NAME, ShoppingConstants.C_NAME_SHOPPING, cv);
        db.close();
        return id;
    }


    // update function
    public void updateInfo(String id_shopping, String name_shopping, String quantity, String type, byte[] image,String addTimeStamp_shopping, String updateTimeStamp_shopping) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ShoppingConstants.C_NAME_SHOPPING, name_shopping);
        cv.put(ShoppingConstants.C_QUANTITY, quantity);
        cv.put(ShoppingConstants.C_TYPE, type);
        cv.put(ShoppingConstants.C_IMAGE_SHOPPING, image);
        cv.put(ShoppingConstants.C_ADD_TIMESTAMP_SHOPPING, addTimeStamp_shopping);
        cv.put(ShoppingConstants.C_UPDATE_TIMESTAMP_SHOPPING, updateTimeStamp_shopping);
        db.update(ShoppingConstants.TABLE_NAME, cv, ShoppingConstants.C_ID_SHOPPING + " =?", new String[]{id_shopping});
        db.close();
    }

    public void deleteInfo(String id_shopping) {
        SQLiteDatabase db_shopping = getWritableDatabase();
        db_shopping.delete(ShoppingConstants.TABLE_NAME, ShoppingConstants.C_ID_SHOPPING + " = ? ", new String[]{id_shopping});
        db_shopping.close();
    }


    public ArrayList<ShoppingModel> getAllData (String orderBy) {
        ArrayList<ShoppingModel> arrayList_shopping = new ArrayList<>();

        // query for select all info in database
        String selectQuery = "SELECT * FROM " + ShoppingConstants.TABLE_NAME + " ORDER BY " + orderBy;

        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        // curate the data from all the column in each row, moveToNext() -> Move the cursor to the next row, start from index -1
        if(cursor.moveToNext()) {
            do {
                ShoppingModel model = new ShoppingModel(
                        cursor.getString(cursor.getColumnIndexOrThrow(ShoppingConstants.C_ID_SHOPPING)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ShoppingConstants.C_NAME_SHOPPING)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ShoppingConstants.C_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ShoppingConstants.C_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ShoppingConstants.C_ADD_TIMESTAMP_SHOPPING)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ShoppingConstants.C_UPDATE_TIMESTAMP_SHOPPING)),
                        cursor.getBlob(cursor.getColumnIndexOrThrow(ShoppingConstants.C_IMAGE_SHOPPING))
                );

                arrayList_shopping.add(model);
            } while (cursor.moveToNext());
        }

        db.close();
        return arrayList_shopping;
    }
}
