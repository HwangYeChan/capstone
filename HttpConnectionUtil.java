package com.example.commu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;




public class HttpConnectionUtil {
    public static String postRequest(String pURL, HashMap < String, String > pList) {

        String myResult = "";

        try {

            //   URL 설정하고 접속하기
            URL url = new URL(pURL); // URL 설정

            HttpURLConnection http = (HttpURLConnection) url.openConnection(); // 접속
            //--------------------------
            //   전송 모드 설정 - 기본적인 설정
            //--------------------------
            http.setDefaultUseCaches(false);
            http.setDoInput(true); // 서버에서 읽기 모드 지정
            http.setDoOutput(true); // 서버로 쓰기 모드 지정
            http.setRequestMethod("POST"); // 전송 방식은 POST




            //--------------------------
            // 헤더 세팅
            //--------------------------
            // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
//            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            http.setRequestProperty("Content-Type", "application/json; utf-8");
            http.setRequestProperty("Accept", "application/json");

            //--------------------------
            //   서버로 값 전송
            //--------------------------
            StringBuffer buffer = new StringBuffer();
            String jsonString="";
            //HashMap으로 전달받은 파라미터가 null이 아닌경우 버퍼에 넣어준다
            if (pList != null) {
                jsonString="{";
                Set key = pList.keySet();

                for (Iterator iterator = key.iterator(); iterator.hasNext();) {

                    String keyName = (String) iterator.next();
                    String valueName = pList.get(keyName);
                    buffer.append(keyName).append("=").append(valueName);
                    jsonString+="\""+keyName;
                    jsonString+="\": \"";
                    jsonString+=valueName+"\"";
                    if(iterator.hasNext()){
                        buffer.append("&");
                        jsonString+=", ";
                    }

                }
                jsonString+="}";
            }



            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(jsonString);
            writer.flush();



            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;

            while ((str = reader.readLine()) != null) {
                builder.append(str + "\n");
            }

            myResult = builder.toString();
            return myResult;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myResult;
    }


}
