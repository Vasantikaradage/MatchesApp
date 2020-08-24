package com.example.matchesapp.Constants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.matchesapp.Model.MatchesModel;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {
    public static String DATABASENAME = "MatchesApp";
    public static int ver = 33;
    Context c;

    public static final String TblMatches = "tbl_matches";

    ArrayList<MatchesModel> frndlist = new ArrayList<>();


    public DataBase(Context context) {
        super(context, DATABASENAME, null, ver);
        c = context;
    }

    @Override
    public void onCreate(SQLiteDatabase skBCDB) {

        skBCDB.execSQL("CREATE TABLE IF NOT EXISTS " + TblMatches + " ( id text  , name text,status int);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        db.execSQL("DROP TABLE IF EXISTS " + TblMatches);

    }


    public void addFriend(String iddata, String name, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Toast.makeText(c, "Successfully Saved Into Database", Toast.LENGTH_SHORT).show();
        contentValues.put("id", iddata);
        contentValues.put("name", name);
        contentValues.put("status", status);

        db.insert(TblMatches, null, contentValues);
        db.close();
    }

    //---deletes a particular title---
    public void deleteRecord(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Toast.makeText(c, "Record deleted Succefully", Toast.LENGTH_SHORT).show();
        db.delete(TblMatches, "id" + "=?", new String[]{String.valueOf(id)});
    }


    public boolean ifExists(String id) {


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tbl_matches where id='" + id + "'", null);
        boolean exists = (cursor.getCount() > 0);
        Toast.makeText(c, "Alreay Exist this record in database", Toast.LENGTH_SHORT).show();
        cursor.close();
        db.close();
        return exists;
    }


    public void empty(String table_name) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("delete from " + table_name);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList<MatchesModel> getFriends() {

        frndlist.clear();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TblMatches, null);
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    MatchesModel item = new MatchesModel();
                    item.name = cursor.getString(cursor.getColumnIndex("name"));
                    item.status = cursor.getInt(cursor.getColumnIndex("status"));
                    item.id = cursor.getString(cursor.getColumnIndex("id"));
                    frndlist.add(item);

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return frndlist;
    }

}
