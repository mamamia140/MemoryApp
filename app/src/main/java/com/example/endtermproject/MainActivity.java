package com.example.endtermproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addButton;
    static ArrayList<singleMemory> memories = new ArrayList<>();
    static ArrayList<singleMemory> temp = new ArrayList<>();
    static MemoryListAdapter adapter;
    int code = 1;
    public static database DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);


        DB = new database(this);
        temp = DB.getMemories(this);
        if(temp != null){
            memories = temp;
        }
        recyclerView = findViewById(R.id.recycler_view);
        addButton = findViewById(R.id.add_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MemoryListAdapter(memories,getApplicationContext());
        recyclerView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddMemory.class);
                intent.putExtra("LIST",memories);
                startActivityForResult(intent,1);
            }
        });
    }
    public static void notify(int index){
        DB.deleteData(memories.get(index).title);
        memories.remove(index);
        adapter.notifyItemRemoved(index);
        adapter.notifyItemRangeChanged(index,memories.size());
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(recyclerView!=null){
            recyclerView.setAdapter(new MemoryListAdapter(memories,getApplicationContext()));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("main activityye girdi request:" + requestCode + ",result:" + resultCode);
        if (requestCode == 1) {
            if (resultCode == 1) {
                singleMemory temp = data.getParcelableExtra("MEMORY");
                memories.add(0, temp);
                adapter.notifyItemInserted(0);
                Bitmap selectedImageBitmap = null;
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), temp.image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(selectedImageBitmap.getWidth());
                int width = (selectedImageBitmap.getWidth())/5;
                int height = (selectedImageBitmap.getHeight())/5;
                DB.insertData(temp.title,temp.date,temp.message, Bitmap.createScaledBitmap(selectedImageBitmap,width,height,false));
            }

        }
    }

}