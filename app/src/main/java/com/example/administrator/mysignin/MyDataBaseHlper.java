package com.example.administrator.mysignin;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/10/31.
 */
public class MyDataBaseHlper extends SQLiteOpenHelper {

    public MyDataBaseHlper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists student_photos(student_id text not null,student_image null)");
        sqLiteDatabase.execSQL("create table if not exists student_info(student_id text not null,student_name text not null," +
                "student_class text not null)");
        sqLiteDatabase.execSQL("create table if not exists student_usual_score(student_id text ,student_qimhjia integer  null,student_chidao integer  null," +
                "student_kuangke integer  null,student_zaotui integer  null,student_chuqin integer  null)");
        sqLiteDatabase.execSQL("create table if not exists student_lab_score(student_id integer ,student_score integer  null)");
        sqLiteDatabase.execSQL("create table if not exists student_exam_score(student_id text ,student_score integer  null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
