package com.nirwal.epoint.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nirwal.epoint.models.ParentChildListItem;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG= "DatabaseHelper";
    SQLiteDatabase _db;

    private static final int Database_Version = 8;//6for up
    private static final String Database_Name = "epoint_db";
    public static final String Table_Name1 = "main_card_table";
    public static final String Table_Name2 = "quiz_list_table";
    public static final String Table_Name3 = "favourites_table";

    public DatabaseHelper(Context context) {
        super(context, Database_Name, null, Database_Version);
        this._db = this.getWritableDatabase();

    }

   /* public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }*/


    @Override
    public synchronized void close() {
        super.close();
        this._db.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Table.getCreateTableQuery(Table_Name1));
        db.execSQL(Table.getCreateTableQuery(Table_Name2));
        db.execSQL(Table.getCreateTableQuery(Table_Name3));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name1);
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name2);
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name3);
        onCreate(db);
    }

    public boolean insertMainCard(ParentChildListItem card) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.insert(Table_Name1, null, Table.generateContentValves(card));

        if (result == -1) {
            Log.d(TAG, "error :MainCard insert fail");
            return false;
        }
        Log.d(TAG, "MainCard insert Success");
        return true;
    }

    public ArrayList<ParentChildListItem> readAllMainCard() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Table_Name1, null);

        if (cursor == null && cursor.getCount() == 0) {
            Log.d(TAG,"MainCard Table empty");
            return null;
        }
        return Table.readCursor(cursor);

    }

    public ArrayList<ParentChildListItem> readAllDataFromTable(TableType t){
        Cursor cursor;
        String table;
        switch (t){
            case MainCard:{
                table =Table_Name1;
                break;
            }
            case Sublist:{
                table =Table_Name2;
                break;
            }
            case Favourites:{
                table =Table_Name3;
                break;
            }
            default:table=null;
        }
        cursor = _db.rawQuery("SELECT * FROM " + table, null);
        return Table.readCursor(cursor);
    }


    public boolean deleteAllMainCard() {
        this._db.execSQL("DELETE FROM " + Table_Name1);
        return true;
    }

    public boolean insertQuizList(ParentChildListItem list) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Id", list.Id);
        values.put("ParentId", list.ParentId);
        values.put("ChildId", list.ChildId);
        values.put("ChildType", list.ChildType);
        values.put("Title", list.Title);
        values.put("Description", list.Description);
        values.put("ImageUrl", list.ImageUrl);


        long result = db.insert(Table_Name2, null, values);

        if (result == -1) {
            Log.v("epoint DB", "error :Quiz list insert fail");
            return false;
        }
        Log.v("epoint DB", "Quiz list insert sucess");
        return true;
    }

    public ArrayList<ParentChildListItem> readAllQuizListfromParentID(String parentId) {
        ArrayList<ParentChildListItem> cards = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Table_Name2 + " Where ParentId=?", new String[]{parentId});

        if (cursor == null && cursor.getCount() == 0) {
            Log.v("epoint DB", "error: Quiz list no data in cursor");
            return null;
        }

        return readParentChildListItemFrmCursor(cursor);
    }



    public boolean deleteAllQuizlist() {
        this._db.execSQL("DELETE FROM " + Table_Name2);
        return true;
    }

    public Boolean insertParaentListItemIntoDb(TableType t, ParentChildListItem card){
        long result=0;
        switch(t){
            case MainCard:{
                result = _db.insert(Table_Name1, null, Table.generateContentValves(card));
                break;
            }
            case Sublist:{
                result = _db.insert(Table_Name2, null, Table.generateContentValves(card));
                break;
            }
            case Favourites:{
                result = _db.insert(Table_Name3, null, Table.generateContentValves(card));
                break;
            }
        }

        if (result == -1) {
            Log.d(TAG, "SQL insert fail");
            return false;
        }
        Log.d(TAG, "SQL Insert success");
        return true;


    }


    public boolean deleteParentClildListItemfrmDb(TableType t, String Id){
        long result=0;
        switch(t){
            case MainCard:{
                result = _db.delete(Table_Name1,"Id=?",new String[]{Id});

                break;
            }
            case Sublist:{
                result = _db.delete(Table_Name2,"Id=?",new String[]{Id});
                break;
            }
            case Favourites:{
                result = _db.delete(Table_Name3,"Id=?",new String[]{Id});
                break;
            }
        }

        if (result == -1) {
            Log.d(TAG, "SQL insert fail");
            return false;
        }
        Log.d(TAG, "SQL Insert success");
        return true;
    }


    public void deleteAllParentListItemFromDb(TableType t){
        switch (t){
            case MainCard:{
                this._db.execSQL("DELETE FROM " + Table_Name1);
                break;
            }
            case Favourites:{
                this._db.execSQL("DELETE FROM " + Table_Name2);
                break;
            }
            case Sublist:{
                this._db.execSQL("DELETE FROM " + Table_Name3);
                break;
            }
        }

    }

    public enum TableType{
        MainCard,Sublist,Favourites
    }

    public static final class Table {
        public static final String col_1 = "Id";
        public static final String col_2 = "ChildId";
        public static final String col_3 = "ParentId";
        public static final String col_4 = "ChildType";
        public static final String col_5 = "Title";
        public static final String col_6 = "Description";
        public static final String col_7 = "ImageUrl";
        public static final String col_8 = "Language";
        public static final String col_9 = "Favourites";

        public static String getCreateTableQuery(String table_name){
            return "CREATE TABLE IF NOT EXISTS "+ table_name
            + " ("+col_1+ " TEXT PRIMARY KEY, "+col_2+" TEXT, "+col_3+" TEXT, "
                    +col_4+" TEXT, "+col_5+" TEXT, "+col_6+" TEXT, "
                    +col_7+" TEXT, "+col_8+" TEXT, "+col_9+" INTEGER)";
        }

        public static ContentValues generateContentValves(ParentChildListItem card){
            ContentValues values= new ContentValues();
            values.put(col_1, card.Id);
            values.put(col_2, card.ChildId);
            values.put(col_3, card.ParentId);
            values.put(col_4, card.ChildType);
            values.put(col_5, card.Title);
            values.put(col_6, card.Description);
            values.put(col_7, card.ImageUrl);
            values.put(col_8, card.Language);
            return values;
        }

        public static ArrayList<ParentChildListItem> readCursor(Cursor cursor){
            ArrayList<ParentChildListItem> list = new ArrayList<>();
            ParentChildListItem item ;

            while (cursor.moveToNext()) {
                item = new ParentChildListItem();
                item.Id = cursor.getString(0);
                item.ChildId = cursor.getString(1);
                item.ParentId = cursor.getString(2);
                item.ChildType = cursor.getString(3);
                item.Title = cursor.getString(4);
                item.Description=cursor.getString(5);
                item.ImageUrl =cursor.getString(6);
                item.Language = cursor.getString(7);

                list.add(item);
            }
            return list;
        }
    }


    public static ArrayList<ParentChildListItem> readParentChildListItemFrmCursor(Cursor cursor){
        ArrayList<ParentChildListItem> list = new ArrayList<>();
        ParentChildListItem item ;

        while (cursor.moveToNext()) {
            item = new ParentChildListItem();
            item.Id = cursor.getString(0);
            item.ChildId = cursor.getString(1);
            item.ParentId = cursor.getString(2);
            item.ChildType = cursor.getString(3);
            item.Title = cursor.getString(4);
            item.Description=cursor.getString(5);
            item.ImageUrl =cursor.getString(6);
            item.Language = cursor.getString(7);
            item.Favourites = cursor.getInt(8);

            list.add(item);
        }
        return list;
    }

    public static ContentValues convertCVfrmParentChildlistItem(ParentChildListItem card){
        ContentValues values= new ContentValues();
        values.put(Table.col_1, card.Id);
        values.put(Table.col_2, card.ChildId);
        values.put(Table.col_3, card.ParentId);
        values.put(Table.col_4, card.ChildType);
        values.put(Table.col_5, card.Title);
        values.put(Table.col_6, card.Description);
        values.put(Table.col_7, card.ImageUrl);
        values.put(Table.col_8, card.Language);
        values.put(Table.col_9, card.Favourites);
        return values;
    }

    public SQLiteDatabase getDb() {
        return this._db;
    }
}

