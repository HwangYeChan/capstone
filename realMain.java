package com.example.commu;

import java.io.*;
import java.net.*;
import java.util.HashMap;

import android.app.*;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

@SuppressWarnings("deprecation")

public class MainActivity extends TabActivity {
    TabHost mTabHost = null;
    String myId, myPWord, myTitle, mySubject, myResult;
    int cas;
    String reponse_data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);



        mTabHost = getTabHost();
        mTabHost.addTab(mTabHost.newTabSpec("tab_1").setIndicator("서버로 전송").setContent(R.id.page01));
        mTabHost.addTab(mTabHost.newTabSpec("tab_2").setIndicator("서버에서 받음").setContent(R.id.page02));



        findViewById(R.id.button_submit).setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

//                String url = "http://sitaiyo.iptime.org:8888/listen"; 	//URL
////                String url="http://10.0.2.2/testhttp/servertest.php";
//                HashMap<String, String> param = new HashMap<String, String>();
//
//
//                myId = ((EditText)(findViewById(R.id.edit_Id))).getText().toString();
//                myPWord = ((EditText)(findViewById(R.id.edit_pword))).getText().toString();
//                myTitle = ((EditText)(findViewById(R.id.edit_title))).getText().toString();
//                mySubject = ((EditText)(findViewById(R.id.edit_subject))).getText().toString();
//
//
//                File imgFile;
//
//
//                if(myId.equals("chicken")){
//                    cas=1;
//                    imgFile = new  File("/storage/emulated/0/Download/hello.jpg");
//                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                    param.put("image",BitmapToString(myBitmap));
//                }
//                else if(myId.equals("noimage")){
//                    cas=2;
//                }
//                else{
//                    cas=3;
//                    imgFile = new  File("/storage/emulated/0/DCIM/Camera/IMG_20200521_142359.jpg");
//                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                    param.put("image",BitmapToString(myBitmap));
//                }
//
//                param.put("id",myId);
//                param.put("pword", myPWord);
//                param.put("title", myTitle);
//                param.put("subject", mySubject);
//
//
//
//                NetworkTask networkTask = new NetworkTask(url, param);
//                networkTask.execute();
                new Task_finder().execute();
            }
        });


    }

    public class Task_finder extends AsyncTask<Void, Void, Void> {
//        private final ProgressDialog dialog = new ProgressDialog(Facebook_Post_View.this);

        // can use UI thread here
        protected void onPreExecute() {
//            this.dialog.setMessage("Loading...");
//            this.dialog.setCancelable(false);
//            this.dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            DataInputStream inStream = null;
            String existingFileName = "/storage/emulated/0/Download/photo-1477959858617-67f85cf4f1df.jpeg";
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

            ((TextView) (findViewById(R.id.text_result))).setText(reponse_data);
//            ((TextView)(findViewById(R.id.text_result))).setText(values.toString());
            Toast.makeText(getApplicationContext(), "done!", Toast.LENGTH_LONG).show();
        }


    }
} // Activity
