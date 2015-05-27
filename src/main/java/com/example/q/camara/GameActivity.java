package com.example.q.camara;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.LogRecord;

public class GameActivity extends ActionBarActivity implements SensorEventListener{
    //private List<Cell> lnumbers = new ArrayList<>();
    private int mida;
    private GridView gv;
    private int nMoves;
    private Player player1;
    private SensorManager sensorManager;
    private Sensor sensorLight;
    private View view;
    private String ip;
    //private String role ;
    Handler handler;
    private Tauler tauler;
    private SocketConection con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState==null) {
            Bundle b = getIntent().getExtras();
            mida = b.getInt("size");
            nMoves = 0;
            //initlnumbers();
            tauler = new Tauler(initlnumbers(), 0);
            player1 = new Player("client"/*savedInstanceState.getString("role")*/);
            if(player1.getRole().equals("server")){
                player1.setIsMyTurn(true);
            }else{
                player1.setIsMyTurn(false);
            }
        }else{
            mida = savedInstanceState.getInt("size");
            nMoves = savedInstanceState.getInt("nMoves");
            player1 = (Player)savedInstanceState.getSerializable("player1");
            tauler = (Tauler) savedInstanceState.getSerializable("tauler");
        }
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        setContentView(R.layout.activity_game);
        view = this.getWindow().getDecorView();
        gv = (GridView) findViewById(R.id.gridview1);
        gv.setNumColumns(4);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                gv.setAdapter(new NumberAdapter(GameActivity.this));
                Log.e("999999999","ieeeeep actualitzo");
                Toast.makeText(getApplicationContext(), "message arrived", Toast.LENGTH_SHORT).show();
            }
        };
        con = new SocketConection();
        con.start();

    }
    private List<Cell> initlnumbers() {  // TODO Guardar persistentment no fer de forma aleatoria
        List<Cell> lnumbers = new ArrayList<>();
        Random r =new Random();
        for (int i = 0; i < mida; i++){
            lnumbers.add(new Cell(Integer.toString(r.nextInt(5))));
        }
        return lnumbers;
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(sensorLight != null){
            sensorManager.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_NORMAL);
        }
        gv.setAdapter(new NumberAdapter(this));

    }

    @Override
    public void onBackPressed() {
        con.closeSocket();
        super.onBackPressed();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        modifyBackground(sensorEvent);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    private void modifyBackground(SensorEvent sensorEvent) {

        if(sensorEvent.values[0]<20){
            view.setBackgroundColor(Color.BLACK);
        }else {
            view.setBackgroundColor(Color.GRAY);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tauler", tauler);
        outState.putSerializable("player1", player1);
        outState.putInt("size", mida);
        outState.putInt("nMoves", nMoves);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
    private class SocketConection extends Thread {
        private ServerSocket serverSocket;
        private Socket socket;
        ObjectInputStream input;
        ObjectOutputStream output;

        int port = 8080;

        @Override
        public void run() {
            Message msg;
            if (player1.getRole().equals("server")) {
                try {
                    serverSocket = new ServerSocket(port);
                    //serverSocket.setSoTimeout(1000000000);
                    socket = serverSocket.accept();
                    doMove();
                    while (nMoves < tauler.getTauler().size()) {
                        nMoves+=2;
                        waitMove();
                        msg = new Message();
                        msg.what = 1;
                        msg.obj = "socket connection";
                        handler.sendMessage(msg);
                        doMove();
                    }

                } catch (Exception ignored) {
                    System.out.println("****************++++"+ ignored.toString()+"+++++++++++++++********************");
                }
            }

            if (GameActivity.this.player1.getRole().equals("client")) {
                // TODO enviar part del client al servidor ACABAR
                //SocketConection s = new SocketConection(ip, port);
                try {
                    socket = new Socket("172.16.107.17", port);
                    while (nMoves < tauler.getTauler().size() ){
                        nMoves += 2;
                        waitMove();
                        msg = new Message();
                        msg.what = 1;
                        String s = "socket connection";
                        msg.obj = s;
                        handler.sendMessage(msg);
                        doMove();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            Log.e("Finish!!!!", "-----------------------");
            this.closeSocket();
            GameActivity.this.startActivity(new Intent(getApplicationContext(), ResumeActivity.class ));
        }
        private void waitMove() throws IOException, InterruptedException, ClassNotFoundException {
            int i=0;
            while(socket.getInputStream().available()<=0 && i<120){
                sleep(1000); i++;
            }
            input = new ObjectInputStream(socket.getInputStream());
            player1.setIsMyTurn(true);
            tauler = (Tauler)input.readObject();
        }
        private void doMove() throws IOException, InterruptedException {
            if(nMoves < tauler.getTauler().size()) {
                while (tauler.getMove2() == ' ') {
                    sleep(1000);
                }

                tauler.reset();
                output = new ObjectOutputStream(socket.getOutputStream());
                output.writeObject(tauler);
                player1.setIsMyTurn(false);
            }
            Log.e("-------------------nMoves------------------", "nomves = "+nMoves + " size lnumbers " + tauler.getTauler().size());


        }
        public void closeSocket(){
            try {
                if (!serverSocket.isClosed()) {
                    serverSocket.close();
                }
                if (!socket.isClosed()) {
                    socket.close();
                }
            }catch(Exception e){}
        }

    }
    public class NumberAdapter extends BaseAdapter {
        private Context mContext;

        public NumberAdapter(Context c) {
            this.mContext = c;
        }

        @Override
        public int getCount() {
            return tauler.getTauler().size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Button btn;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                btn = new Button(mContext);
                btn.setLayoutParams(new GridView.LayoutParams( GridView.LayoutParams.MATCH_PARENT,100));
                //btn.setScaleType(ImageView.ScaleType.CENTER_CROP);
                btn.setPadding(8, 8, 8, 8);
            } else {
                btn = (Button) convertView;
            }

            btn.setText(tauler.getTauler().get(position).getNumber());
            btn.setId(position);
            btn.setOnClickListener(new OnMyClickListener());
            return btn;
        }
    }
    private class OnMyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Integer i = v.getId();
            Button b = (Button) findViewById(v.getId());
            if(!tauler.getTauler().get(i).isClicked() && player1.getIsMyTurn())
            {
                tauler.setMove(i);
                tauler.getTauler().get(i).setClicked(true);
                b.setText(tauler.getTauler().get(i).getNumber());
                if(tauler.getTurn() == 2){
                    player1.setIsMyTurn(false);
                    if(tauler.compareMoves()){
                        player1.addPoint();
                    }
                    Toast.makeText(GameActivity.this, "points player1: "+player1.getPoints()+"\n", Toast.LENGTH_SHORT).show();
                }
                nMoves++;
            }
            else{
                Toast.makeText(GameActivity.this, "Is Clicked or isn't yout¡r turn", Toast.LENGTH_SHORT).show();
            }
        }
    }


}

