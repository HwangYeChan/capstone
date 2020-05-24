package com.example.creal_main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;

@SuppressWarnings("deprecation")

public class goodResultActivity  extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.good_result);
        setActionBar();


        Intent intent = getIntent();

        ImageView up_image=(ImageView)findViewById(R.id.up_image);


        String imagePath=intent.getExtras().getString("imagePath");
        String responseData=intent.getExtras().getString("responseData");
        TextView up_text=(TextView)findViewById(R.id.server_response);
        up_text.setText("server message : "+responseData);

        File imgFile = new  File(imagePath);
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            up_image.setImageBitmap(myBitmap);
        }

    }



    public void setActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.real_creal_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        return;
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
