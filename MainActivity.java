package com.example.creal_main;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

@SuppressWarnings("deprecation")

public class MainActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    ProgressDialog dialog;

    private final int GET_GALLERY_IMAGE = 200;
    private View mLayout;
    String reponse_data;

    private static final int REQUEST_IMAGE_1 = 1;
//    private String imagePath1 = "/storage/emulated/0/DCIM/Camera/IMG_20200521_142359.jpg";
    private String imagePath1 = "";

    public void setActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.real_creal_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        return;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setActionBar();

        dialog = new ProgressDialog(MainActivity.this);
        mLayout = findViewById(R.id.layout_main);

        Button sendCamera = (Button)findViewById(R.id.by_camera);
        sendCamera.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getBaseContext(), goodResultActivity.class);
                startActivityForResult(intent, 1000);
            }
        });



        Button sendImageButton = (Button)findViewById(R.id.by_gallery);
        sendImageButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);

            }
        });



    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    public class Task_finder extends AsyncTask<Void, Void, Void> {


        protected void onPreExecute() {

            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            dialog.setMessage("이미지 분석중입니다..");
            dialog.show();

            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method


            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            DataInputStream inStream = null;
            String existingFileName = imagePath1;

            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;

            try {
                String urlString = "http://sitaiyo.iptime.org:8888/listen";
                //------------------ CLIENT REQUEST
                FileInputStream fileInputStream = new FileInputStream(new File(existingFileName));
                // open a URL connection to the Servlet
                URL url = new URL(urlString);
                // Open a HTTP connection to the URL
                conn = (HttpURLConnection) url.openConnection();
                // Allow Inputs
                conn.setDoInput(true);
                // Allow Outputs
                conn.setDoOutput(true);
                // Don't use a cached copy.
                conn.setUseCaches(false);

                // Use a post method.
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + "testImage" + "\"" + lineEnd); // uploaded_file_name is the Name of the File to be uploaded
                dos.writeBytes(lineEnd);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                fileInputStream.close();
                dos.flush();
                dos.close();
            } catch (MalformedURLException ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            } catch (IOException ioe) {
                Log.e("Debug", "HERE! error: " + ioe.getMessage(), ioe);
            }
            //------------------ read the SERVER RESPONSE
            try {
                inStream = new DataInputStream(conn.getInputStream());
                String str;
                while ((str = inStream.readLine()) != null) {
                    Log.e("Debug", "Server Response " + str);
                    reponse_data=str;
                }
                inStream.close();
            } catch (IOException ioex) {
                Log.e("Debug", "error: " + ioex.getMessage(), ioex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
//            ((TextView) (findViewById(R.id.help_text))).setText(reponse_data);
//            ((TextView) (findViewById(R.id.help_text))).setText(imagePath1);
//            ((TextView)(findViewById(R.id.text_result))).setText(values.toString());
//            Toast.makeText(getApplicationContext(), "sended "+imagePath1, Toast.LENGTH_LONG).show();
            dialog.dismiss();
            Intent passIntent = new Intent(getBaseContext(), goodResultActivity.class);
            passIntent.putExtra("imagePath",imagePath1);
            passIntent.putExtra("responseData",reponse_data);
            startActivityForResult(passIntent, 1000);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                // 결과가 OK 였다면
                // 이미지를 요청했는대
                case REQUEST_IMAGE_1:
                    imagePath1 = getPath(data.getData());
//                    ((TextView) (findViewById(R.id.help_text))).setText(imagePath1);
                    new Task_finder().execute();

                    break;
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }

}
