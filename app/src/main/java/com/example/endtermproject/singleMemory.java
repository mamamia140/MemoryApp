package com.example.endtermproject;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class singleMemory implements Parcelable {
    public Uri image;
    public String title;
    public String message;
    public String date;

    public singleMemory(){

    }

    @Override
    public String toString() {
        return "title= " + title + '\n' +
                "date= " + date + '\n' +
                "message= " + message;
    }

    public singleMemory(Uri imagePath, String title, String message, String date){
        this.image = imagePath;
        this.title = title;
        this.message = message;
        this.date = date;

    }
    protected singleMemory(Parcel in) {
        image = (Uri) in.readValue(null);
        title = (String) in.readValue(null);
        message = (String) in.readValue(null);
        date = (String) in.readValue(null);
    }
    public static final Creator<singleMemory> CREATOR = new Creator<singleMemory>() {
        @Override
        public singleMemory createFromParcel(Parcel in) {
            return new singleMemory(in);
        }

        @Override
        public singleMemory[] newArray(int size) {
            return new singleMemory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(image);
        dest.writeValue(title);
        dest.writeValue(message);
        dest.writeValue(date);
    }
}
