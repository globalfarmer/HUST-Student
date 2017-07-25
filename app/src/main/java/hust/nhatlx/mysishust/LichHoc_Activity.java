package hust.nhatlx.mysishust;

import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import hust.nhatlx.mysishust.adapter.LichHocAdapter;
import hust.nhatlx.mysishust.model.LichHocModel;

public class LichHoc_Activity extends AppCompatActivity {
    String data;
    ListView lvLichHoc;
    ArrayList<LichHocModel> lichHocModelArrayList;
    LichHocAdapter lichHocAdapter;
    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_hoc_);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarLichHoc);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LichHoc_Activity.this, TongQuan_Activity.class);
                startActivity(intent);
            }
        });

        //Show calendar view
        calendarView = (CalendarView) findViewById(R.id.calendarView);

        //Show lich hoc
        lvLichHoc = (ListView) findViewById(R.id.lvLichHoc_LichHoc);
        readData();
        lichHocModelArrayList = new ArrayList<>();
        try {
            JSONObject dataJsonO = new JSONObject(data);
            JSONArray lichHocJsonA = dataJsonO.getJSONArray("lichHoc");
            for (int i = 0; i < lichHocJsonA.length(); i++) {
                JSONObject lichHocJsonO = lichHocJsonA.getJSONObject(i);
                LichHocModel lichHocModel = new LichHocModel();
                lichHocModel.setThoiGian(lichHocJsonO.getString("thoiGian"));
                lichHocModel.setTenLop(lichHocJsonO.getString("tenLop"));
                lichHocModel.setLoaiLop(lichHocJsonO.getString("loaiLop"));
                lichHocModel.setPhongHoc(lichHocJsonO.getString("phongHoc"));
                lichHocModel.setMaHocPhan(lichHocJsonO.getString("maHocPhan"));
                lichHocModel.setMaLop(lichHocJsonO.getString("maLop"));
                lichHocModel.setNhom(lichHocJsonO.getString("nhom"));
                lichHocModel.setTuanHoc(lichHocJsonO.getString("tuanHoc"));
                lichHocModel.setGhiChu(lichHocJsonO.getString("ghiChu"));
                lichHocModelArrayList.add(lichHocModel);
            }
            lichHocAdapter = new LichHocAdapter(LichHoc_Activity.this, R.layout.item_lichhoc, lichHocModelArrayList);
            lvLichHoc.setAdapter(lichHocAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Item click Event==========================================================================
        lvLichHoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LichHocModel lichHocModel = lichHocModelArrayList.get(position);
                AlertDialog dialog = new AlertDialog.Builder(LichHoc_Activity.this)
                        .setTitle("Chi tiết:")
                        .setView(R.layout.dialog_lichhoc)
                        .create();
                dialog.show();
                TextView txtThoiGian = (TextView) dialog.findViewById(R.id.txtThoiGian_Detail);
                TextView txtTenLop = (TextView) dialog.findViewById(R.id.txtTenLop_Detail);
                TextView txtLoaiLop = (TextView) dialog.findViewById(R.id.txtLoaiLop_Detail);
                TextView txtPhongHoc = (TextView) dialog.findViewById(R.id.txtPhongHoc_Detail);
                TextView txtMaHocPhan = (TextView) dialog.findViewById(R.id.txtMaHocPhan_Detail);
                TextView txtMaLop = (TextView) dialog.findViewById(R.id.txtMaLop_Detail);
                TextView txtNhom = (TextView) dialog.findViewById(R.id.txtNhom_Detail);
                TextView txtTuanHoc = (TextView) dialog.findViewById(R.id.txtTuanHoc_Detail);
                TextView txtGhiCHu = (TextView) dialog.findViewById(R.id.txtGhiChu_Detail);
                assert txtThoiGian != null;
                txtThoiGian.setText("<+>  " + lichHocModel.getThoiGian());
                assert txtTenLop != null;
                txtTenLop.setText("<+>  " + lichHocModel.getTenLop());
                assert txtLoaiLop != null;
                txtLoaiLop.setText("<+>  " + lichHocModel.getLoaiLop());
                assert txtPhongHoc != null;
                txtPhongHoc.setText("<+>  " + lichHocModel.getPhongHoc());
                assert txtMaHocPhan != null;
                txtMaHocPhan.setText("<+>  " + lichHocModel.getMaHocPhan());
                assert txtMaLop != null;
                txtMaLop.setText("<+>  " + lichHocModel.getMaLop());
                assert txtNhom != null;
                txtNhom.setText("<+>  " + lichHocModel.getNhom());
                assert txtTuanHoc != null;
                txtTuanHoc.setText("<+>  " + lichHocModel.getTuanHoc());
                assert txtGhiCHu != null;
                txtGhiCHu.setText("<+>  " + lichHocModel.getGhiChu());
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
}
