package com.sp.p2002640_cherish;

public class ShoppingConstants {
    // db name
    public static final String DB_NAME = "SHOPPING_DB";
    // db version
    public static final int DB_VERSION = 1;
    // db table
    public static final String TABLE_NAME = "SHOPPING_TABLE";
    // table columns
    public static final String C_ID_SHOPPING = "ID_SHOPPING";
    public static final String C_NAME_SHOPPING = "NAME_SHOPPING";
    public static final String C_QUANTITY = "QUANTITY";
    public static final String C_TYPE = "TYPE";

    public static final String C_IMAGE_SHOPPING = "IMAGE_SHOPPING";
    public static final String C_ADD_TIMESTAMP_SHOPPING = "ADD_TIMESTAMP_SHOPPING";
    public static final String C_UPDATE_TIMESTAMP_SHOPPING = "UPDATE_TIMESTAMP_SHOPPING";

    // create query for table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            +C_ID_SHOPPING + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            +C_NAME_SHOPPING + " TEXT,"
            +C_QUANTITY + " TEXT,"
            +C_TYPE + " TEXT,"
            +C_IMAGE_SHOPPING + " BLOB,"
            +C_ADD_TIMESTAMP_SHOPPING + " TEXT,"
            +C_UPDATE_TIMESTAMP_SHOPPING + " TEXT"
            + ");";
}
