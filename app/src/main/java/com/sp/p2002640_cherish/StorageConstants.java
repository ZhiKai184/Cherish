package com.sp.p2002640_cherish;

public class StorageConstants {
    // db name
    public static final String DB_NAME = "FOOD_DB";
    // db version
    public static final int DB_VERSION = 1;
    // db table
    public static final String TABLE_NAME = "FOOD_TABLE";
    // table columns
    public static final String C_ID = "ID";
    public static final String C_NAME = "NAME";
    public static final String C_DATE = "DATE";
    public static final String C_PRICE = "PRICE";
    public static final String C_WEIGHT = "WEIGHT";

    public static final String C_IMAGE = "IMAGE";
    public static final String C_ADD_TIMESTAMP = "ADD_TIMESTAMP";
    public static final String C_UPDATE_TIMESTAMP = "UPDATE_TIMESTAMP";

    // create query for table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            +C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            +C_NAME + " TEXT,"
            +C_DATE + " TEXT,"
            +C_PRICE + " TEXT,"
            +C_WEIGHT + " TEXT,"
            +C_IMAGE + " BLOB,"
            +C_ADD_TIMESTAMP + " TEXT,"
            +C_UPDATE_TIMESTAMP + " TEXT"
            + ");";

}
