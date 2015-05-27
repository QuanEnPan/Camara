package com.example.q.camara.Statistics;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by q on 27/05/15.
 */
public class HttpThread extends Thread {


    private Handler h;
    private String method;
    private JSONObject jsonObject;
    private String url;
    private OutputStream outputStream;
    private Message message;

    public HttpThread(String method, String url, JSONObject jsonObject, Handler h) {
        this.method = method;
        this.url = url;
        this.jsonObject = jsonObject;
        this.h = h;
    }

    @Override
    public void run() {
        try {
            String ip = "172.16.0.165";
            String port = "3000";
            URL ur = new URL("http://" + ip + ":" + port + "/" + url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) ur.openConnection();
            if (method.equals("GET")) {
                httpURLConnection.setDoOutput(false);
                httpURLConnection.setRequestMethod("GET");

                if (httpURLConnection.getResponseCode() == 200) {
                    message = new Message();
                    message.obj = "ha arribat correctament";
                    h.sendMessage(message);
                } else {
                    Log.e("****",ur.toString());
                    message = new Message();
                    message.obj = "received" + httpURLConnection.getResponseCode();
                    h.sendMessage(message);
                }
            }
            else if(method.equals("POST")){
                httpURLConnection.setRequestMethod(method);
                outputStream = httpURLConnection.getOutputStream();
                outputStream.write(jsonObject.toString().getBytes());
                if (httpURLConnection.getResponseCode() == 200) {
                    message = new Message();
                    message.obj = "ha arribat correctament";
                    h.sendMessage(message);

                } else {
                    message = new Message();
                    message.obj = "received" + httpURLConnection.getResponseCode();
                    h.sendMessage(message);
                }
            }
            else if(method.equals("PUT")){
                httpURLConnection.setRequestMethod(method);
                outputStream = httpURLConnection.getOutputStream();
                outputStream.write(jsonObject.toString().getBytes());
                if (httpURLConnection.getResponseCode() == 200) {
                    message = new Message();
                    message.obj = "ha arribat correctament";
                    h.sendMessage(message);

                } else {
                    message = new Message();
                    message.obj = "received" + httpURLConnection.getResponseCode();
                    h.sendMessage(message);
                }
            }
            else if(method.equals("DELETE")){
                httpURLConnection.setRequestMethod(method);
                outputStream = httpURLConnection.getOutputStream();
                outputStream.write(jsonObject.toString().getBytes());
                if (httpURLConnection.getResponseCode() == 200) {
                    message = new Message();
                    message.obj = "ha arribat correctament";
                    h.sendMessage(message);

                } else {
                    message = new Message();
                    message.obj = "received" + httpURLConnection.getResponseCode();
                    h.sendMessage(message);
                }
            }

        /*} catch (IOException e) {
            message = new Message();
            message.obj = e.toString();
            h.sendMessage(message);
*/
        } catch (Exception e) {
            Log.e("uuuuuu", e.toString());
            e.printStackTrace();
        }
    }
}