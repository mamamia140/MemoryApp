package com.example.endtermproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class database extends SQLiteOpenHelper {
    ArrayList<singleMemory> memories = new ArrayList<>();
    public static final String DBNAME = "memory.db";
    public database(Context context) {
        super(context, "memory.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table memories(title TEXT, date TEXT, message TEXT, image BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists memories");
    }

    public Boolean insertData(String title, String date, String message, Bitmap image){
        byte[] imageBlob = getBitmapAsByteArray(image);
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("title", title);
        contentValues.put("date", date);
        contentValues.put("message", message);
        contentValues.put("image",imageBlob);
        long result = MyDB.insert("memories", null, contentValues);

        if(result==-1) return false;
        else
            return true;
    }
    public void deleteData(String title){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        MyDB.delete("memories","title" + "=?", new String[]{title});
    }

    private byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public ArrayList<singleMemory> getMemories(Context context){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from memories", null);
        while(cursor.moveToNext()){
            byte[] imgByte = cursor.getBlob(3);
            Uri imgUri = getImageUri(context,BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
            singleMemory item = new singleMemory(imgUri,cursor.getString(0),cursor.getString(2),cursor.getString(1));
            memories.add(item);
        }
        cursor.close();
        return memories;
    }
    public Uri getImageUri(Context context,Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 0, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "IMG_" + System.currentTimeMillis(), null);
        return Uri.parse(path);
    }
}
