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
    // 전역변수를 선언한다
    TabHost mTabHost = null;
    String myId, myPWord, myTitle, mySubject, myResult;
    int cas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);



        mTabHost = getTabHost();          // Tab 만들기
        mTabHost.addTab(mTabHost.newTabSpec("tab_1").setIndicator("서버로 전송").setContent(R.id.page01));
        mTabHost.addTab(mTabHost.newTabSpec("tab_2").setIndicator("서버에서 받음").setContent(R.id.page02));



        findViewById(R.id.button_submit).setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                String url = "http://sitaiyo.iptime.org:8888/listen"; 	//URL
//                String url="http://10.0.2.2/testhttp/servertest.php";
                HashMap<String, String> param = new HashMap<String, String>();


                myId = ((EditText)(findViewById(R.id.edit_Id))).getText().toString();
                myPWord = ((EditText)(findViewById(R.id.edit_pword))).getText().toString();
                myTitle = ((EditText)(findViewById(R.id.edit_title))).getText().toString();
                mySubject = ((EditText)(findViewById(R.id.edit_subject))).getText().toString();


                File imgFile;


                if(myId.equals("chicken")){
                    cas=1;
                    imgFile = new  File("/storage/emulated/0/Download/hello.jpg");
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    param.put("image",BitmapToString(myBitmap));
                }
                else if(myId.equals("noimage")){
                    cas=2;
                }
                else{
                    cas=3;
                    imgFile = new  File("/storage/emulated/0/DCIM/Camera/IMG_20200521_142359.jpg");
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    param.put("image",BitmapToString(myBitmap));
                }

                param.put("id",myId);
                param.put("pword", myPWord);
                param.put("title", myTitle);
                param.put("subject", mySubject);



                NetworkTask networkTask = new NetworkTask(url, param);
                networkTask.execute();
            }
        });


    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        String url;
        HashMap values;

        NetworkTask(String url, HashMap values){
            this.url = url;
            this.values = values;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progress bar를 보여주는 등등의 행위
        }

        @Override
        protected String doInBackground(Void... params) {
            String result;
            HttpConnectionUtil requestHttpURLConnection = new HttpConnectionUtil();
            result = requestHttpURLConnection.postRequest(url, values);
            return result; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
        }

        @Override
        protected void onPostExecute(String result) {
            // 통신이 완료되면 호출됩니다.
            // 결과에 따른 UI 수정 등은 여기서 합니다.
            ((TextView)(findViewById(R.id.text_result))).setText(result);
//            ((TextView)(findViewById(R.id.text_result))).setText(values.toString());
            Toast.makeText(getApplicationContext(), myId+"!! cas is "+cas+" and "+result, Toast.LENGTH_LONG).show();
        }
    }



    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
        byte[] bytes = baos.toByteArray();
        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
        return temp;
    }



} // Activity
