package hust.nhatlx.mysishust;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;

import hust.nhatlx.mysishust.adapter.MonNoAdapter;
import hust.nhatlx.mysishust.model.HocPhanModel;

public class MonNo_Activity extends AppCompatActivity {
    String data;

    ListView lvMonTach;
    ArrayList<HocPhanModel> monTachModelArrayList;
    MonNoAdapter monTachAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_no);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMonNo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MonNo_Activity.this, TongQuan_Activity.class);
                startActivity(intent);
            }
        });

        lvMonTach = (ListView) findViewById(R.id.lvMonNo_MonNo);

        readData();
        hienThiMonNoListView();
    }

    private void hienThiMonNoListView() {
        try {
            monTachModelArrayList = new ArrayList<>();
            JSONObject dataJsonO = new JSONObject(data);
            JSONObject bangDiemCaNhanJsonO = dataJsonO.getJSONObject("bangDiemCaNhan");
            JSONArray diemHocPhanJsonA = bangDiemCaNhanJsonO.getJSONArray("diemHocPhan");
            for (int i = 0; i<diemHocPhanJsonA.length(); i++){
                JSONObject diemHocPhanJsonO = diemHocPhanJsonA.getJSONObject(i);
                String diemChu = diemHocPhanJsonO.getString("diemChu");
                if(diemChu.equals("F")){
                    HocPhanModel monTachModel = new HocPhanModel();
                    monTachModel.setMaHocPhan(diemHocPhanJsonO.getString("maHocPhan"));
                    monTachModel.setTenHocPhan(diemHocPhanJsonO.getString("tenHocPhan"));
                    monTachModel.setSoTinChi(diemHocPhanJsonO.getString("soTinChi"));
                    monTachModelArrayList.add(monTachModel);
                }
            }
            if(monTachModelArrayList!=null){
                monTachAdapter = new MonNoAdapter(
                        MonNo_Activity.this,
                        R.layout.item_monno,
                        monTachModelArrayList);
                lvMonTach.setAdapter(monTachAdapter);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void readData() {
        try{
            FileInputStream fIS = openFileInput("sis.txt");
            InputStreamReader iSR = new InputStreamReader(fIS);
            BufferedReader bR = new BufferedReader(iSR);
            String dataLine;
            StringBuilder sB = new StringBuilder();
            while ((dataLine=bR.readLine())!=null){
                sB.append(dataLine);
                sB.append("\n");
            }
            fIS.close();
            data = sB.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
