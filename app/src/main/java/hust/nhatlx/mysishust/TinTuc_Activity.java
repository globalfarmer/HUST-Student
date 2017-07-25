package hust.nhatlx.mysishust;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import hust.nhatlx.mysishust.adapter.ThongTinAdapter;
import hust.nhatlx.mysishust.model.ThongTinModel;

public class TinTuc_Activity extends AppCompatActivity {
    String data;
    ListView lvThongTin;
    ArrayList<ThongTinModel> thongTinArrayList;
    ThongTinAdapter thongTinAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tin_tuc);
        lvThongTin = (ListView) findViewById(R.id.lvTinTuc_TinTuc);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTinTuc);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TinTuc_Activity.this, TongQuan_Activity.class);
                startActivity(intent);
            }
        });

        readData();
        try {
            JSONObject dataJson = new JSONObject(data);
            JSONArray thongTinJsonA = dataJson.getJSONArray("thongTin");
            thongTinArrayList = new ArrayList<>();
            for (int i = 0; i<thongTinJsonA.length(); i++){
                JSONObject thongTinJsonO = thongTinJsonA.getJSONObject(i);
                ThongTinModel thongTinModel = new ThongTinModel();
                String thongTin = thongTinJsonO.getString("thongTin");
                String urlThongTin = thongTinJsonO.getString("urlThongTin");
                thongTinModel.setThongTin(thongTin);
                thongTinModel.setUrlThongTin(urlThongTin);

                thongTinArrayList.add(thongTinModel);
            }
            thongTinAdapter = new ThongTinAdapter(
                    TinTuc_Activity.this,
                    R.layout.item_thongtin,
                    thongTinArrayList);
            lvThongTin.setAdapter(thongTinAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        lvThongTin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkInternet();
                if(checkInternet()){
                    ThongTinModel thongTinModel = thongTinArrayList.get(position);
                    String urlThongTin = thongTinModel.getUrlThongTin();
                    Intent intent = new Intent(TinTuc_Activity.this, Webview_Activity.class);
                    intent.putExtra("urlThongTin", urlThongTin);
                    startActivity(intent);
                }
            }
        });
    }
    private void readData() {
        try {
            FileInputStream fIS = openFileInput("sis.txt");
            InputStreamReader iSR = new InputStreamReader(fIS);
            BufferedReader bR = new BufferedReader(iSR);
            String dataLine;
            StringBuilder sB = new StringBuilder();
            while ((dataLine = bR.readLine()) != null) {
                sB.append(dataLine);
                sB.append("\n");
            }
            fIS.close();
            data = sB.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean checkInternet() {
        boolean checkInternet = true;
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo == null) {
            checkInternet = false;
            Toast.makeText(TinTuc_Activity.this, "Hmmm, Internet đâu thím...", Toast.LENGTH_LONG).show();
        }
        return checkInternet;
    }
}
