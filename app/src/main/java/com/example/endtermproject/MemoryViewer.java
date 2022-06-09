package com.example.endtermproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MemoryViewer extends AppCompatActivity {
    ArrayList<singleMemory> memories;
    TextView memoryTitle;
    TextView memoryText;
    TextView memoryDate;
    ImageView memoryImage;
    FloatingActionButton shareButton;
    FloatingActionButton pdfButton;
    Bitmap bitmap,scaledBitmap;
    int index;
    int width = 1200;
    int height = 2010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_memory_viewer);

        memoryText = findViewById(R.id.memory_text);
        memoryDate = findViewById(R.id.memory_date);
        memoryImage = findViewById(R.id.memory_image);
        memoryTitle = findViewById(R.id.memory_title);
        shareButton = findViewById(R.id.share_button);
        pdfButton = findViewById(R.id.pdf_button);
        memories = (ArrayList<singleMemory>) getIntent().getSerializableExtra("LIST");
        index = (int) getIntent().getSerializableExtra("INDEX");
        memoryText.setText(memories.get(index).message);
        memoryDate.setText(memories.get(index).date);
        memoryTitle.setText(memories.get(index).title);
        memoryImage.setImageURI(memories.get(index).image);



        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent smsIntent = new Intent(Intent.ACTION_SEND);
                smsIntent.putExtra(Intent.EXTRA_STREAM, memories.get(index).image);
                smsIntent.putExtra("sms_body", memories.get(index).toString());
                smsIntent.setType("image/gif");
                startActivity(smsIntent);
            }
        });
        pdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(MemoryViewer.this,new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
                try{
                    generatePdf();
                    Toast.makeText(MemoryViewer.this, "Pdf file has been generated succesfully", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(MemoryViewer.this, "Pdf file couldn't have been generated", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void generatePdf() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), memories.get(index).image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PdfDocument pdf = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint();

        PdfDocument.PageInfo pdfInfo = new PdfDocument.PageInfo.Builder(1200,2010,1).create();
        PdfDocument.Page page1 = pdf.startPage(pdfInfo);
        Canvas canvas = page1.getCanvas();

        int temp = bitmap.getHeight()+50;
        int temp2 = (width - (int) bitmap.getWidth())/2;
        canvas.drawBitmap(bitmap,temp2,10,paint);
        canvas.drawText(memories.get(index).message,50,temp+100,paint);

        titlePaint.setTextAlign(Paint.Align.CENTER);
        paint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(40);

        canvas.drawText(memories.get(index).title,width/2,temp,titlePaint);
        canvas.drawText(memories.get(index).date,width/2,temp+30,paint);



        pdf.finishPage(page1);
        File file = new File(Environment.getExternalStorageDirectory(),"/Download/" + memories.get(index).title + ".pdf");
        try{
            pdf.writeTo(new FileOutputStream(file));
        }
        catch(IOException e){
            e.printStackTrace();
        }
        pdf.close();
    }
}