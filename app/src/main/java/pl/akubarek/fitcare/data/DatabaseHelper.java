package pl.akubarek.fitcare.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import pl.akubarek.fitcare.util.Constants;

/**
 * Created by BloodyFire on 25.03.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private final static int DB_VERSION = 2;
    private final static String DB_NAME = "fitcare.db";

    private final static String TAG = DatabaseHelper.class.getSimpleName();

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PRODUCT_TABLE);

        Log.d(TAG, "Product list table created");
        Log.d(TAG, CREATE_PRODUCT_TABLE);

        db.execSQL(CREATE_SHOPPING_TABLE);

        Log.d(TAG, "Shopping cart created");
        Log.d(TAG, CREATE_SHOPPING_TABLE);

        db.execSQL(CREATE_MEALS_TABLE);
        Log.d(TAG, "Meals cart created");
        Log.d(TAG, CREATE_MEALS_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(CREATE_TEMP_SHOPPING_CART);
    }

    private static final String CREATE_PRODUCT_TABLE =
            "CREATE TABLE " + Constants.PRODUCT_TABLE + "("
                    + Constants.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Constants.COLUMN_NAME + " TEXT, "
                    + Constants.COLUMN_CATEGORY + " TEXT, "
                    + Constants.COLUMN_WEIGHT + " INTEGER, "
                    + Constants.COLUMN_CALORIES + " INTEGER, "
                    + Constants.COLUMN_PROTEIN + " NUMERIC, "
                    + Constants.COLUMN_FAT + " NUMERIC, "
                    + Constants.COLUMN_CARBS + " NUMERIC);";

    private static final String CREATE_SHOPPING_TABLE =
            "CREATE TABLE " + Constants.SHOPPING_TABLE + "("
                    + Constants.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Constants.COLUMN_NAME + " TEXT, "
                    + Constants.COLUMN_CATEGORY + " TEXT, "
                    + Constants.COLUMN_WEIGHT + " INTEGER, "
                    + Constants.COLUMN_CALORIES + " INTEGER, "
                    + Constants.COLUMN_PROTEIN + " NUMERIC, "
                    + Constants.COLUMN_FAT + " NUMERIC, "
                    + Constants.COLUMN_CARBS + " NUMERIC, "
                    + Constants.COLUMN_MEAL_ID + " INTEGER);";

    private static final String CREATE_MEALS_TABLE =
            "CREATE TABLE " + Constants.MEALS_TABLE + "("
                    + Constants.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Constants.COLUMN_NAME + " TEXT, "
                    + Constants.COLUMN_DATE_CREATED + " BIGINT);";

    private static final String CREATE_TEMP_SHOPPING_CART =
            "CREATE TABLE " + Constants.TEMP_CART_TABLE + "("
                    + Constants.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Constants.COLUMN_NAME + " TEXT, "
                    + Constants.COLUMN_CATEGORY + " TEXT, "
                    + Constants.COLUMN_WEIGHT + " INTEGER, "
                    + Constants.COLUMN_CALORIES + " INTEGER, "
                    + Constants.COLUMN_PROTEIN + " NUMERIC, "
                    + Constants.COLUMN_FAT + " NUMERIC, "
                    + Constants.COLUMN_CARBS + " NUMERIC);";


}
