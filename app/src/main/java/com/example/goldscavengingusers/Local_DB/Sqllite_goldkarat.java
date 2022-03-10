package com.example.goldscavengingusers.Local_DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DecimalFormat;

import androidx.annotation.Nullable;

public class Sqllite_goldkarat extends SQLiteOpenHelper {
    private static final String MYDB = "gkarat.db";
    private static final String MYTAB = "gkarat_table";
    private static final int VERSION = 1;

    //ROW
    private static final String COL_ID = "id";
    private static final String COL_GOLDBAR_ID = "goldbar_id";
    private static final String COL_GOLD_BAR_OWNER = "gold_bar_owner";
    private static final String COL_GOLD_INGOT_WEIGHT = "gold_ingot_weight";
    private static final String COL_SAMPLE_WEIGHT = "sample_weight";
    private static final String COL_GOLD_KARAT_WEIGHT = "gold_karat_weight";
    private static final String COL_NET = "gold_net";
    private static final String COL_PRICE_GRAM = "price_gram";
    private static final String COL_PRICE = "price";
    private static final String COL_datetime = "datetime";


    public Sqllite_goldkarat(@Nullable Context context) {
        super(context, MYDB, null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String create = "CREATE TABLE " + Sqllite_goldkarat.MYTAB + " ("
                + Sqllite_goldkarat.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Sqllite_goldkarat.COL_GOLDBAR_ID + " Text NOT NULL, "
                + Sqllite_goldkarat.COL_GOLD_BAR_OWNER + " TEXT NOT NULL, "
                + Sqllite_goldkarat.COL_GOLD_INGOT_WEIGHT + " TEXT NOT NULL, "
                + Sqllite_goldkarat.COL_SAMPLE_WEIGHT + " TEXT NOT NULL, "
                + Sqllite_goldkarat.COL_GOLD_KARAT_WEIGHT + " TEXT NOT NULL, "
                + Sqllite_goldkarat.COL_NET + " TEXT NOT NULL, "
                + Sqllite_goldkarat.COL_PRICE_GRAM + " TEXT NOT NULL, "
                + Sqllite_goldkarat.COL_PRICE+ " TEXT NOT NULL, "
                + Sqllite_goldkarat.COL_datetime + " TIMESTAMP default CURRENT_TIMESTAMP "
                + " );";
        sqLiteDatabase.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Sqllite_goldkarat.MYTAB);
    }

    //iNSERT
    public void insert(String idd,String gold_bar_id ,String gold_bar_ownerr, String gold_ingot_weightt, String sample_weightt, String gold_karat_weightt, String nett,String price_gramm,String price) {
        SQLiteDatabase databaseinset = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Sqllite_goldkarat.COL_ID, idd);
        contentValues.put(Sqllite_goldkarat.COL_GOLDBAR_ID, gold_bar_id);
        contentValues.put(Sqllite_goldkarat.COL_GOLD_BAR_OWNER, gold_bar_ownerr);
        contentValues.put(Sqllite_goldkarat.COL_GOLD_INGOT_WEIGHT, gold_ingot_weightt);
        contentValues.put(Sqllite_goldkarat.COL_SAMPLE_WEIGHT, sample_weightt);
        contentValues.put(Sqllite_goldkarat.COL_GOLD_KARAT_WEIGHT, gold_karat_weightt);
        contentValues.put(Sqllite_goldkarat.COL_NET, nett);
        contentValues.put(Sqllite_goldkarat.COL_PRICE_GRAM, price_gramm);
        contentValues.put(Sqllite_goldkarat.COL_PRICE, price);
        databaseinset.insert(Sqllite_goldkarat.MYTAB, null, contentValues);
    }

    //UPDATE
    public Boolean update(String idd, String gold_bar_ownerr, String gold_ingot_weightt, String sample_weightt, String gold_karat_weightt, String nett,String price_gramm) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Sqllite_goldkarat.COL_ID, idd);
        contentValues.put(Sqllite_goldkarat.COL_GOLD_BAR_OWNER, gold_bar_ownerr);
        contentValues.put(Sqllite_goldkarat.COL_GOLD_INGOT_WEIGHT, gold_ingot_weightt);
        contentValues.put(Sqllite_goldkarat.COL_SAMPLE_WEIGHT, sample_weightt);
        contentValues.put(Sqllite_goldkarat.COL_GOLD_KARAT_WEIGHT, gold_karat_weightt);
        contentValues.put(Sqllite_goldkarat.COL_NET, nett);
        contentValues.put(Sqllite_goldkarat.COL_PRICE_GRAM, price_gramm);
        database.update(Sqllite_goldkarat.MYTAB, contentValues, "id=?", new String[]{String.valueOf(idd)});
        return true;
    }

    public Cursor show() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM gkarat_table Order BY datetime Asc", null);
        return cursor;
    }


    public Cursor getTotal_Gold() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursort = database.rawQuery("SELECT SUM(gold_ingot_weight + sample_weight) FROM gkarat_table", null);
        return cursort;
    }

    public Cursor getTotal_Gold_NET() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursornet = database.rawQuery("SELECT SUM(gold_net) FROM gkarat_table", null);
        return cursornet;
    }

    public Cursor getTotal_PRICE_NET() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursornet = database.rawQuery("SELECT SUM(replace(price, ',','')) FROM gkarat_table", null);
        return cursornet;
    }

    public int delete(String id) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(Sqllite_goldkarat.MYTAB, "id=?", new String[]{String.valueOf(id)});
    }

    public int deleteall() {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(Sqllite_goldkarat.MYTAB, null,null);
    }

    //Select Num of Row
    public int getRowCount() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM gkarat_table", null);
        return cursor.getCount();
    }

    //Select Get GOLDBAR ID
    public Cursor getID() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursornet = database.rawQuery("SELECT id FROM gkarat_table", null);
        return cursornet;
    }

}