package hust.nhatlx.mysishust;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import hust.nhatlx.mysishust.adapter.BangDiemAdapter;
import hust.nhatlx.mysishust.model.HocPhanModel;

public class BangDiem_Activity extends AppCompatActivity {
    String data;
    ListView lvDiemHocPhan;
    ArrayList<HocPhanModel> diemHocPhanModelArrayList;
    BangDiemAdapter diemHocPhanAdapter;
    TextView txtCPA, txtSoTinChiTichLuy;
    Spinner spnPhanLoai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bang_diem);

        lvDiemHocPhan = (ListView) findViewById(R.id.lvBangDiem_BangDiem);
        txtCPA = (TextView) findViewById(R.id.txtCPA_BangDiem);
        txtSoTinChiTichLuy = (TextView) findViewById(R.id.txtSoTinChi_BangDiem);
        spnPhanLoai = (Spinner) findViewById(R.id.spnPhanLoai_BangDiem);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarBangDiem);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BangDiem_Activity.this, TongQuan_Activity.class);
                startActivity(intent);
            }
        });

        readData();
        hienThiCPA();
        hienThiSpinner();
    }
    private void hienThiSpinner() {
        List<String> phanLoaiList = new ArrayList<>();
        phanLoaiList.add("All");
        phanLoaiList.add("A/A+");
        phanLoaiList.add("B+");
        phanLoaiList.add("B");
        phanLoaiList.add("C+");
        phanLoaiList.add("C");
        phanLoaiList.add("D+");
        phanLoaiList.add("D");
        phanLoaiList.add("F");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                BangDiem_Activity.this,
                android.R.layout.simple_spinner_item,
                phanLoaiList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPhanLoai.setAdapter(adapter);

        spnPhanLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        hienThiTatCa();
                        break;
                    case 1:
                        hienThiMonDiemAAplus();
                        break;
                    case 2:
                        hienThiMonDiemBplus();
                        break;
                    case 3:
                        hienThiMonDiemB();
                        break;
                    case 4:
                        hienThiMonDiemCplus();
                        break;
                    case 5:
                        hienThiMonDiemC();
                        break;
                    case 6:
                        hienThiMonDiemDplus();
                        break;
                    case 7:
                        hienThiMonDiemD();
                        break;
                    case 8:
                        hienThiMonDiemF();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        lvOnClick();
    }

    private void hienThiCPA() {
        try {
            JSONObject dataJsonO = new JSONObject(data);
            JSONObject bangDiemCaNhan = dataJsonO.getJSONObject("bangDiemCaNhan");
            JSONArray bangDiemHocPhan = bangDiemCaNhan.getJSONArray("diemHocPhan");
            double diemTong = 0;
            double tongSoTinCHi = 0;
            for (int i = 0; i<bangDiemHocPhan.length(); i++){
                JSONObject diemHocPhan = bangDiemHocPhan.getJSONObject(i);
                String soTinChiString = diemHocPhan.getString("soTinChi");
                double soTinChi = Double.parseDouble(soTinChiString);
                double diemSo = diemHocPhan.getDouble("diemSo");
                diemTong += soTinChi*diemSo;
                tongSoTinCHi += soTinChi;
            }
            double cpa = diemTong/tongSoTinCHi;
            String cpaString = String.valueOf((double) Math.round(cpa * 100) / 100);
            txtCPA.setText(String.valueOf(cpaString));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void hienThiMonDiemF() {
        diemHocPhanModelArrayList = new ArrayList<>();
        try {
            JSONObject dataJsonO = new JSONObject(data);
            JSONObject bangDiemCaNhan = dataJsonO.getJSONObject("bangDiemCaNhan");
            JSONArray bangDiemHocPhan = bangDiemCaNhan.getJSONArray("diemHocPhan");
            Integer tongSoTinChi = 0;
            for (int i = bangDiemHocPhan.length() - 1; i >= 0; i--) {
                JSONObject diemHocPhan = bangDiemHocPhan.getJSONObject(i);
                if (diemHocPhan.getString("diemChu").equals("F")) {
                    HocPhanModel diemHocPhanModel = new HocPhanModel();
                    diemHocPhanModel.setMaHocPhan(diemHocPhan.getString("maHocPhan"));
                    diemHocPhanModel.setTenHocPhan(diemHocPhan.getString("tenHocPhan"));
                    diemHocPhanModel.setSoTinChi(diemHocPhan.getString("soTinChi"));
                    diemHocPhanModel.setDiemChu(diemHocPhan.getString("diemChu"));
                    diemHocPhanModel.setDiemSo(diemHocPhan.getDouble("diemSo"));
                    diemHocPhanModel.setDiemQuaTrinh(diemHocPhan.getString("diemQuaTrinh"));
                    diemHocPhanModel.setDiemCuoiKi(diemHocPhan.getString("diemCuoiKi"));
                    diemHocPhanModelArrayList.add(diemHocPhanModel);
                    tongSoTinChi = tongSoTinChi + Integer.parseInt(diemHocPhan.getString("soTinChi"));
                }
            }
            String tongSoTinChiString = String.valueOf(tongSoTinChi);
            txtSoTinChiTichLuy.setText(tongSoTinChiString + " tín");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        diemHocPhanAdapter = new BangDiemAdapter(
                BangDiem_Activity.this,
                R.layout.item_bangdiem,
                diemHocPhanModelArrayList);
        lvDiemHocPhan.setAdapter(diemHocPhanAdapter);
    }

    private void hienThiMonDiemD() {
        diemHocPhanModelArrayList = new ArrayList<>();
        try {
            JSONObject dataJsonO = new JSONObject(data);
            JSONObject bangDiemCaNhan = dataJsonO.getJSONObject("bangDiemCaNhan");
            JSONArray bangDiemHocPhan = bangDiemCaNhan.getJSONArray("diemHocPhan");
            Integer tongSoTinChi = 0;
            for (int i = bangDiemHocPhan.length() - 1; i >= 0; i--) {
                JSONObject diemHocPhan = bangDiemHocPhan.getJSONObject(i);
                if (diemHocPhan.getString("diemChu").equals("D")) {
                    HocPhanModel diemHocPhanModel = new HocPhanModel();
                    diemHocPhanModel.setMaHocPhan(diemHocPhan.getString("maHocPhan"));
                    diemHocPhanModel.setTenHocPhan(diemHocPhan.getString("tenHocPhan"));
                    diemHocPhanModel.setSoTinChi(diemHocPhan.getString("soTinChi"));
                    diemHocPhanModel.setDiemChu(diemHocPhan.getString("diemChu"));
                    diemHocPhanModel.setDiemSo(diemHocPhan.getDouble("diemSo"));
                    diemHocPhanModel.setDiemQuaTrinh(diemHocPhan.getString("diemQuaTrinh"));
                    diemHocPhanModel.setDiemCuoiKi(diemHocPhan.getString("diemCuoiKi"));
                    diemHocPhanModelArrayList.add(diemHocPhanModel);
                    tongSoTinChi = tongSoTinChi + Integer.parseInt(diemHocPhan.getString("soTinChi"));
                }
            }
            String tongSoTinChiString = String.valueOf(tongSoTinChi);
            txtSoTinChiTichLuy.setText(tongSoTinChiString + " tín");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        diemHocPhanAdapter = new BangDiemAdapter(
                BangDiem_Activity.this,
                R.layout.item_bangdiem,
                diemHocPhanModelArrayList);
        lvDiemHocPhan.setAdapter(diemHocPhanAdapter);
    }

    private void hienThiMonDiemDplus() {
        diemHocPhanModelArrayList = new ArrayList<>();
        try {
            JSONObject dataJsonO = new JSONObject(data);
            JSONObject bangDiemCaNhan = dataJsonO.getJSONObject("bangDiemCaNhan");
            JSONArray bangDiemHocPhan = bangDiemCaNhan.getJSONArray("diemHocPhan");
            Integer tongSoTinChi = 0;
            for (int i = bangDiemHocPhan.length() - 1; i >= 0; i--) {
                JSONObject diemHocPhan = bangDiemHocPhan.getJSONObject(i);
                if (diemHocPhan.getString("diemChu").equals("D+")) {
                    HocPhanModel diemHocPhanModel = new HocPhanModel();
                    diemHocPhanModel.setMaHocPhan(diemHocPhan.getString("maHocPhan"));
                    diemHocPhanModel.setTenHocPhan(diemHocPhan.getString("tenHocPhan"));
                    diemHocPhanModel.setSoTinChi(diemHocPhan.getString("soTinChi"));
                    diemHocPhanModel.setDiemChu(diemHocPhan.getString("diemChu"));
                    diemHocPhanModel.setDiemSo(diemHocPhan.getDouble("diemSo"));
                    diemHocPhanModel.setDiemQuaTrinh(diemHocPhan.getString("diemQuaTrinh"));
                    diemHocPhanModel.setDiemCuoiKi(diemHocPhan.getString("diemCuoiKi"));
                    diemHocPhanModelArrayList.add(diemHocPhanModel);
                    tongSoTinChi = tongSoTinChi + Integer.parseInt(diemHocPhan.getString("soTinChi"));
                }
            }
            String tongSoTinChiString = String.valueOf(tongSoTinChi);
            txtSoTinChiTichLuy.setText(tongSoTinChiString + " tín");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        diemHocPhanAdapter = new BangDiemAdapter(
                BangDiem_Activity.this,
                R.layout.item_bangdiem,
                diemHocPhanModelArrayList);
        lvDiemHocPhan.setAdapter(diemHocPhanAdapter);
    }

    private void hienThiMonDiemC() {
        diemHocPhanModelArrayList = new ArrayList<>();
        try {
            JSONObject dataJsonO = new JSONObject(data);
            JSONObject bangDiemCaNhan = dataJsonO.getJSONObject("bangDiemCaNhan");
            JSONArray bangDiemHocPhan = bangDiemCaNhan.getJSONArray("diemHocPhan");
            Integer tongSoTinChi = 0;
            for (int i = bangDiemHocPhan.length() - 1; i >= 0; i--) {
                JSONObject diemHocPhan = bangDiemHocPhan.getJSONObject(i);
                if (diemHocPhan.getString("diemChu").equals("C")) {
                    HocPhanModel diemHocPhanModel = new HocPhanModel();
                    diemHocPhanModel.setMaHocPhan(diemHocPhan.getString("maHocPhan"));
                    diemHocPhanModel.setTenHocPhan(diemHocPhan.getString("tenHocPhan"));
                    diemHocPhanModel.setSoTinChi(diemHocPhan.getString("soTinChi"));
                    diemHocPhanModel.setDiemChu(diemHocPhan.getString("diemChu"));
                    diemHocPhanModel.setDiemSo(diemHocPhan.getDouble("diemSo"));
                    diemHocPhanModel.setDiemQuaTrinh(diemHocPhan.getString("diemQuaTrinh"));
                    diemHocPhanModel.setDiemCuoiKi(diemHocPhan.getString("diemCuoiKi"));
                    diemHocPhanModelArrayList.add(diemHocPhanModel);
                    tongSoTinChi = tongSoTinChi + Integer.parseInt(diemHocPhan.getString("soTinChi"));
                }
            }
            String tongSoTinChiString = String.valueOf(tongSoTinChi);
            txtSoTinChiTichLuy.setText(tongSoTinChiString + " tín");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        diemHocPhanAdapter = new BangDiemAdapter(
                BangDiem_Activity.this,
                R.layout.item_bangdiem,
                diemHocPhanModelArrayList);
        lvDiemHocPhan.setAdapter(diemHocPhanAdapter);
    }

    private void hienThiMonDiemCplus() {
        diemHocPhanModelArrayList = new ArrayList<>();
        try {
            JSONObject dataJsonO = new JSONObject(data);
            JSONObject bangDiemCaNhan = dataJsonO.getJSONObject("bangDiemCaNhan");
            JSONArray bangDiemHocPhan = bangDiemCaNhan.getJSONArray("diemHocPhan");
            Integer tongSoTinChi = 0;
            for (int i = bangDiemHocPhan.length() - 1; i >= 0; i--) {
                JSONObject diemHocPhan = bangDiemHocPhan.getJSONObject(i);
                if (diemHocPhan.getString("diemChu").equals("C+")) {
                    HocPhanModel diemHocPhanModel = new HocPhanModel();
                    diemHocPhanModel.setMaHocPhan(diemHocPhan.getString("maHocPhan"));
                    diemHocPhanModel.setTenHocPhan(diemHocPhan.getString("tenHocPhan"));
                    diemHocPhanModel.setSoTinChi(diemHocPhan.getString("soTinChi"));
                    diemHocPhanModel.setDiemChu(diemHocPhan.getString("diemChu"));
                    diemHocPhanModel.setDiemSo(diemHocPhan.getDouble("diemSo"));
                    diemHocPhanModel.setDiemQuaTrinh(diemHocPhan.getString("diemQuaTrinh"));
                    diemHocPhanModel.setDiemCuoiKi(diemHocPhan.getString("diemCuoiKi"));
                    diemHocPhanModelArrayList.add(diemHocPhanModel);
                    tongSoTinChi = tongSoTinChi + Integer.parseInt(diemHocPhan.getString("soTinChi"));
                }
            }
            String tongSoTinChiString = String.valueOf(tongSoTinChi);
            txtSoTinChiTichLuy.setText(tongSoTinChiString + " tín");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        diemHocPhanAdapter = new BangDiemAdapter(
                BangDiem_Activity.this,
                R.layout.item_bangdiem,
                diemHocPhanModelArrayList);
        lvDiemHocPhan.setAdapter(diemHocPhanAdapter);
    }

    private void hienThiMonDiemB() {
        diemHocPhanModelArrayList = new ArrayList<>();
        try {
            JSONObject dataJsonO = new JSONObject(data);
            JSONObject bangDiemCaNhan = dataJsonO.getJSONObject("bangDiemCaNhan");
            JSONArray bangDiemHocPhan = bangDiemCaNhan.getJSONArray("diemHocPhan");
            Integer tongSoTinChi = 0;
            for (int i = bangDiemHocPhan.length() - 1; i >= 0; i--) {
                JSONObject diemHocPhan = bangDiemHocPhan.getJSONObject(i);
                if (diemHocPhan.getString("diemChu").equals("B")) {
                    HocPhanModel diemHocPhanModel = new HocPhanModel();
                    diemHocPhanModel.setMaHocPhan(diemHocPhan.getString("maHocPhan"));
                    diemHocPhanModel.setTenHocPhan(diemHocPhan.getString("tenHocPhan"));
                    diemHocPhanModel.setSoTinChi(diemHocPhan.getString("soTinChi"));
                    diemHocPhanModel.setDiemChu(diemHocPhan.getString("diemChu"));
                    diemHocPhanModel.setDiemSo(diemHocPhan.getDouble("diemSo"));
                    diemHocPhanModel.setDiemQuaTrinh(diemHocPhan.getString("diemQuaTrinh"));
                    diemHocPhanModel.setDiemCuoiKi(diemHocPhan.getString("diemCuoiKi"));
                    diemHocPhanModelArrayList.add(diemHocPhanModel);
                    tongSoTinChi = tongSoTinChi + Integer.parseInt(diemHocPhan.getString("soTinChi"));
                }
            }
            String tongSoTinChiString = String.valueOf(tongSoTinChi);
            txtSoTinChiTichLuy.setText(tongSoTinChiString + " tín");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        diemHocPhanAdapter = new BangDiemAdapter(
                BangDiem_Activity.this,
                R.layout.item_bangdiem,
                diemHocPhanModelArrayList);
        lvDiemHocPhan.setAdapter(diemHocPhanAdapter);
    }

    private void hienThiMonDiemBplus() {
        diemHocPhanModelArrayList = new ArrayList<>();
        try {
            JSONObject dataJsonO = new JSONObject(data);
            JSONObject bangDiemCaNhan = dataJsonO.getJSONObject("bangDiemCaNhan");
            JSONArray bangDiemHocPhan = bangDiemCaNhan.getJSONArray("diemHocPhan");
            Integer tongSoTinChi = 0;
            for (int i = bangDiemHocPhan.length() - 1; i >= 0; i--) {
                JSONObject diemHocPhan = bangDiemHocPhan.getJSONObject(i);
                if (diemHocPhan.getString("diemChu").equals("B+")) {
                    HocPhanModel diemHocPhanModel = new HocPhanModel();
                    diemHocPhanModel.setMaHocPhan(diemHocPhan.getString("maHocPhan"));
                    diemHocPhanModel.setTenHocPhan(diemHocPhan.getString("tenHocPhan"));
                    diemHocPhanModel.setSoTinChi(diemHocPhan.getString("soTinChi"));
                    diemHocPhanModel.setDiemChu(diemHocPhan.getString("diemChu"));
                    diemHocPhanModel.setDiemSo(diemHocPhan.getDouble("diemSo"));
                    diemHocPhanModel.setDiemQuaTrinh(diemHocPhan.getString("diemQuaTrinh"));
                    diemHocPhanModel.setDiemCuoiKi(diemHocPhan.getString("diemCuoiKi"));
                    diemHocPhanModelArrayList.add(diemHocPhanModel);
                    tongSoTinChi = tongSoTinChi + Integer.parseInt(diemHocPhan.getString("soTinChi"));
                }
            }
            String tongSoTinChiString = String.valueOf(tongSoTinChi);
            txtSoTinChiTichLuy.setText(tongSoTinChiString + " tín");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        diemHocPhanAdapter = new BangDiemAdapter(
                BangDiem_Activity.this,
                R.layout.item_bangdiem,
                diemHocPhanModelArrayList);
        lvDiemHocPhan.setAdapter(diemHocPhanAdapter);
    }

    private void hienThiMonDiemAAplus() {
        diemHocPhanModelArrayList = new ArrayList<>();
        try {
            JSONObject dataJsonO = new JSONObject(data);
            JSONObject bangDiemCaNhan = dataJsonO.getJSONObject("bangDiemCaNhan");
            JSONArray bangDiemHocPhan = bangDiemCaNhan.getJSONArray("diemHocPhan");
            Integer tongSoTinChi = 0;
            for (int i = bangDiemHocPhan.length() - 1; i >= 0; i--) {
                JSONObject diemHocPhan = bangDiemHocPhan.getJSONObject(i);
                if (diemHocPhan.getString("diemChu").equals("A") || diemHocPhan.getString("diemChu").equals("A+")) {
                    HocPhanModel diemHocPhanModel = new HocPhanModel();
                    diemHocPhanModel.setMaHocPhan(diemHocPhan.getString("maHocPhan"));
                    diemHocPhanModel.setTenHocPhan(diemHocPhan.getString("tenHocPhan"));
                    diemHocPhanModel.setSoTinChi(diemHocPhan.getString("soTinChi"));
                    diemHocPhanModel.setDiemChu(diemHocPhan.getString("diemChu"));
                    diemHocPhanModel.setDiemSo(diemHocPhan.getDouble("diemSo"));
                    diemHocPhanModel.setDiemQuaTrinh(diemHocPhan.getString("diemQuaTrinh"));
                    diemHocPhanModel.setDiemCuoiKi(diemHocPhan.getString("diemCuoiKi"));
                    diemHocPhanModelArrayList.add(diemHocPhanModel);
                    tongSoTinChi = tongSoTinChi + Integer.parseInt(diemHocPhan.getString("soTinChi"));
                }
            }
            String tongSoTinChiString = String.valueOf(tongSoTinChi);
            txtSoTinChiTichLuy.setText(tongSoTinChiString + " tín");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        diemHocPhanAdapter = new BangDiemAdapter(
                BangDiem_Activity.this,
                R.layout.item_bangdiem,
                diemHocPhanModelArrayList);
        lvDiemHocPhan.setAdapter(diemHocPhanAdapter);
    }

    private void hienThiTatCa() {
        diemHocPhanModelArrayList = new ArrayList<>();
        try {
            JSONObject dataJsonO = new JSONObject(data);
            JSONObject bangDiemCaNhan = dataJsonO.getJSONObject("bangDiemCaNhan");
            JSONArray bangDiemHocPhan = bangDiemCaNhan.getJSONArray("diemHocPhan");
            Integer tongSoTinChi = 0;
            for (int i = bangDiemHocPhan.length() - 1; i >= 0; i--) {
                JSONObject diemHocPhan = bangDiemHocPhan.getJSONObject(i);
                HocPhanModel diemHocPhanModel = new HocPhanModel();
                diemHocPhanModel.setMaHocPhan(diemHocPhan.getString("maHocPhan"));
                diemHocPhanModel.setTenHocPhan(diemHocPhan.getString("tenHocPhan"));
                diemHocPhanModel.setSoTinChi(diemHocPhan.getString("soTinChi"));
                diemHocPhanModel.setDiemChu(diemHocPhan.getString("diemChu"));
                diemHocPhanModel.setDiemSo(diemHocPhan.getDouble("diemSo"));
                diemHocPhanModel.setDiemQuaTrinh(diemHocPhan.getString("diemQuaTrinh"));
                diemHocPhanModel.setDiemCuoiKi(diemHocPhan.getString("diemCuoiKi"));

                diemHocPhanModelArrayList.add(diemHocPhanModel);
                tongSoTinChi = tongSoTinChi + Integer.parseInt(diemHocPhan.getString("soTinChi"));
            }
            String tongSoTinChiString = String.valueOf(tongSoTinChi);
            txtSoTinChiTichLuy.setText(tongSoTinChiString + " tín");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        diemHocPhanAdapter = new BangDiemAdapter(
                BangDiem_Activity.this,
                R.layout.item_bangdiem,
                diemHocPhanModelArrayList);
        lvDiemHocPhan.setAdapter(diemHocPhanAdapter);
    }

    private void lvOnClick() {
        //Item set on click=========================================================================
        lvDiemHocPhan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, final long id) {
                final HocPhanModel diemHocPhanModel = diemHocPhanModelArrayList.get(position);
                final AlertDialog dialog = new AlertDialog.Builder(BangDiem_Activity.this)
                        .setTitle("Cải thiện: " + diemHocPhanModel.getMaHocPhan())
                        .setView(R.layout.dialog_bangdiem)
                        .create();
                dialog.show();
                final RadioGroup radioGroup;
                final TextView txtNhanXetCaiThien;
                final ImageView imgNhanXet;
                imgNhanXet = (ImageView) dialog.findViewById(R.id.imgNhanXet);
                radioGroup = (RadioGroup) dialog.findViewById(R.id.radGroup);
                txtNhanXetCaiThien = (TextView) dialog.findViewById(R.id.txtNhanXetCaiThien);
                assert radioGroup != null;
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        double DIEMSO = diemHocPhanModel.getDiemSo();
                        if (checkedId == R.id.radBtnA) {
                            if (DIEMSO < 4) {
                                assert txtNhanXetCaiThien != null;
                                txtNhanXetCaiThien.setText("Chúc thím may mắn :)");
                                assert imgNhanXet != null;
                                imgNhanXet.setImageResource(R.drawable.goodluck);
                            } else if (DIEMSO == 4) {
                                assert txtNhanXetCaiThien != null;
                                txtNhanXetCaiThien.setText("Điểm max rồi thím :)");
                                assert imgNhanXet != null;
                                imgNhanXet.setImageResource(R.drawable.diemmax);
                            }
                        } else if (checkedId == R.id.radBtnBplus) {
                            if (DIEMSO < 3.5) {
                                assert txtNhanXetCaiThien != null;
                                txtNhanXetCaiThien.setText("Chúc thím may mắn :)");
                                assert imgNhanXet != null;
                                imgNhanXet.setImageResource(R.drawable.goodluck);
                            } else if (DIEMSO == 3.5) {
                                assert txtNhanXetCaiThien != null;
                                txtNhanXetCaiThien.setText("Có gì thay đổi không thím? :)");
                                assert imgNhanXet != null;
                                imgNhanXet.setImageResource(R.drawable.khongthaydoi);
                            } else {
                                assert txtNhanXetCaiThien != null;
                                txtNhanXetCaiThien.setText("Cải thiện lùi à thím :v");
                                assert imgNhanXet != null;
                                imgNhanXet.setImageResource(R.drawable.caithienlui);
                            }
                        } else if (checkedId == R.id.radBtnB) {
                            if (DIEMSO < 3) {
                                assert txtNhanXetCaiThien != null;
                                txtNhanXetCaiThien.setText("Chúc thím may mắn :)");
                                assert imgNhanXet != null;
                                imgNhanXet.setImageResource(R.drawable.goodluck);
                            } else if (DIEMSO == 3) {
                                assert txtNhanXetCaiThien != null;
                                txtNhanXetCaiThien.setText("Có gì thay đổi không thím? :)");
                                assert imgNhanXet != null;
                                imgNhanXet.setImageResource(R.drawable.khongthaydoi);
                            } else {
                                assert txtNhanXetCaiThien != null;
                                txtNhanXetCaiThien.setText("Cải thiện lùi à thím :v");
                                assert imgNhanXet != null;
                                imgNhanXet.setImageResource(R.drawable.caithienlui);
                            }
                        } else if (checkedId == R.id.radBtnCplus) {
                            if (DIEMSO < 2.5) {
                                assert txtNhanXetCaiThien != null;
                                txtNhanXetCaiThien.setText("Chúc thím may mắn :)");
                                assert imgNhanXet != null;
                                imgNhanXet.setImageResource(R.drawable.goodluck);
                            } else if (DIEMSO == 2.5) {
                                assert txtNhanXetCaiThien != null;
                                txtNhanXetCaiThien.setText("Có gì thay đổi không thím? :)");
                                assert imgNhanXet != null;
                                imgNhanXet.setImageResource(R.drawable.khongthaydoi);
                            } else {
                                assert txtNhanXetCaiThien != null;
                                txtNhanXetCaiThien.setText("Cải thiện lùi à thím :v");
                                assert imgNhanXet != null;
                                imgNhanXet.setImageResource(R.drawable.caithienlui);
                            }
                        } else if (checkedId == R.id.radBtnC) {
                            if (DIEMSO < 2) {
                                assert txtNhanXetCaiThien != null;
                                txtNhanXetCaiThien.setText("Chúc thím may mắn :)");
                                assert imgNhanXet != null;
                                imgNhanXet.setImageResource(R.drawable.goodluck);
                            } else if (DIEMSO == 2) {
                                assert txtNhanXetCaiThien != null;
                                txtNhanXetCaiThien.setText("Có gì thay đổi không thím? :)");
                                assert imgNhanXet != null;
                                imgNhanXet.setImageResource(R.drawable.khongthaydoi);
                            } else {
                                assert txtNhanXetCaiThien != null;
                                txtNhanXetCaiThien.setText("Cải thiện lùi à thím :v");
                                assert imgNhanXet != null;
                                imgNhanXet.setImageResource(R.drawable.caithienlui);
                            }
                        } else if (checkedId == R.id.radBtnDplus) {
                            if (DIEMSO < 1.5) {
                                assert txtNhanXetCaiThien != null;
                                txtNhanXetCaiThien.setText("Chúc thím may mắn :)");
                                assert imgNhanXet != null;
                                imgNhanXet.setImageResource(R.drawable.goodluck);
                            } else if (DIEMSO == 1.5) {
                                assert txtNhanXetCaiThien != null;
                                txtNhanXetCaiThien.setText("Có gì thay đổi không thím? :)");
                                assert imgNhanXet != null;
                                imgNhanXet.setImageResource(R.drawable.khongthaydoi);
                            } else {
                                assert txtNhanXetCaiThien != null;
                                txtNhanXetCaiThien.setText("Cải thiện lùi à thím :v");
                                assert imgNhanXet != null;
                                imgNhanXet.setImageResource(R.drawable.caithienlui);
                            }
                        } else if (checkedId == R.id.radBtnD) {
                            if (DIEMSO < 1) {
                                assert txtNhanXetCaiThien != null;
                                txtNhanXetCaiThien.setText("Chúc thím may mắn :)");
                                assert imgNhanXet != null;
                                imgNhanXet.setImageResource(R.drawable.goodluck);
                            } else if (DIEMSO == 1) {
                                assert txtNhanXetCaiThien != null;
                                txtNhanXetCaiThien.setText("Có gì thay đổi không thím? :)");
                                assert imgNhanXet != null;
                                imgNhanXet.setImageResource(R.drawable.khongthaydoi);
                            } else {
                                assert txtNhanXetCaiThien != null;
                                txtNhanXetCaiThien.setText("Cải thiện lùi à thím :v");
                                assert imgNhanXet != null;
                                imgNhanXet.setImageResource(R.drawable.caithienlui);
                            }

                        }
                    }
                });
                //Button Cancel is clicked-----------------------------------------------------------
                Button btnHuy = (Button) dialog.findViewById(R.id.btnHuy);
                assert btnHuy != null;
                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                //Button Save is clicked------------------------------------------------------------
                Button btnLuu = (Button) dialog.findViewById(R.id.btnLuu);
                assert btnLuu != null;
                btnLuu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (radioGroup.getCheckedRadioButtonId() != -1) {
                            try {
                                //Doc data de ghi lai vao file moi--------------------------------------
                                JSONObject dataJsonO = new JSONObject(data);
                                JSONObject bangDiemCaNhanJsonO = dataJsonO.getJSONObject("bangDiemCaNhan");
                                JSONArray lichHocJsonA = dataJsonO.getJSONArray("lichHoc");
                                JSONObject taiKhoanLoginJsonO = dataJsonO.getJSONObject("taiKhoanLogin");
                                JSONArray thongTinJsonA = dataJsonO.getJSONArray("thongTin");
                                JSONArray caiThienJsonA = dataJsonO.getJSONArray("caiThien");
                                JSONObject hocPhiJsonO = dataJsonO.getJSONObject("hocPhi");
                                //Ghi lai mon cai thien cux---------------------------------------------
                                ArrayList<String> caiThienList = new ArrayList<>();
                                if (caiThienJsonA.length() != 0) {
                                    for (int i = 0; i < caiThienJsonA.length(); i++) {
                                        caiThienList.add(caiThienJsonA.getJSONObject(i).toString());
                                    }
                                    for (int j = 0; j < caiThienJsonA.length(); j++) {
                                        JSONObject monCaiThienCuJsonO = caiThienJsonA.getJSONObject(j);
                                        if (monCaiThienCuJsonO.getString("maHocPhan").equals(diemHocPhanModel.getMaHocPhan())) {
                                            caiThienList.remove(monCaiThienCuJsonO.toString());
                                        }
                                    }
                                    //Them mon cai thien moi------------------------------------------------
                                    JSONObject monCaiThienNewJsonO = new JSONObject();
                                    monCaiThienNewJsonO.put("maHocPhan", diemHocPhanModel.getMaHocPhan());
                                    monCaiThienNewJsonO.put("soTinChi", diemHocPhanModel.getSoTinChi());
                                    monCaiThienNewJsonO.put("tenHocPhan", diemHocPhanModel.getTenHocPhan());
                                    monCaiThienNewJsonO.put("diemSoHienTai", diemHocPhanModel.getDiemSo());
                                    monCaiThienNewJsonO.put("loaiDuKien","cai thien");
                                    String diemChuCaiThien;
                                    int idx = radioGroup.getCheckedRadioButtonId();
                                    View radButton = radioGroup.findViewById(idx);
                                    int radioID = radioGroup.indexOfChild(radButton);
                                    RadioButton btn = (RadioButton) radioGroup.getChildAt(radioID);
                                    diemChuCaiThien = (String) btn.getText();
                                    monCaiThienNewJsonO.put("diemChuCaiThien", diemChuCaiThien);

                                    caiThienList.add(monCaiThienNewJsonO.toString());

                                } else {
                                    //Them mon cai thien moi------------------------------------------------
                                    JSONObject monCaiThienNewJsonO = new JSONObject();
                                    monCaiThienNewJsonO.put("maHocPhan", diemHocPhanModel.getMaHocPhan());
                                    monCaiThienNewJsonO.put("soTinChi", diemHocPhanModel.getSoTinChi());
                                    monCaiThienNewJsonO.put("tenHocPhan", diemHocPhanModel.getTenHocPhan());
                                    monCaiThienNewJsonO.put("diemSoHienTai", diemHocPhanModel.getDiemSo());
                                    monCaiThienNewJsonO.put("loaiDuKien","cai thien");
                                    String diemChuCaiThien;
                                    int idx = radioGroup.getCheckedRadioButtonId();
                                    View radButton = radioGroup.findViewById(idx);
                                    int radioID = radioGroup.indexOfChild(radButton);
                                    RadioButton btn = (RadioButton) radioGroup.getChildAt(radioID);
                                    diemChuCaiThien = (String) btn.getText();
                                    monCaiThienNewJsonO.put("diemChuCaiThien", diemChuCaiThien);

                                    caiThienList.add(monCaiThienNewJsonO.toString());
                                }

                                JSONArray caiThienJsonANew = new JSONArray(caiThienList.toString());
                                //Tao file moi va ghi lai vao bo nho================================
                                JSONObject sisJsonO = new JSONObject();
                                sisJsonO.put("bangDiemCaNhan", bangDiemCaNhanJsonO);
                                sisJsonO.put("lichHoc", lichHocJsonA);
                                sisJsonO.put("taiKhoanLogin", taiKhoanLoginJsonO);
                                sisJsonO.put("thongTin", thongTinJsonA);
                                sisJsonO.put("caiThien", caiThienJsonANew);
                                sisJsonO.put("hocPhi", hocPhiJsonO);

                                //Save all to 1 file have name is sis.txt===========================================
                                //==================================================================================
                                String savedata = sisJsonO.toString();
                                FileOutputStream fOS = openFileOutput("sis.txt", MODE_PRIVATE);
                                OutputStreamWriter oSW = new OutputStreamWriter(fOS);
                                oSW.write(savedata);
                                oSW.flush();
                                oSW.close();

                                dialog.dismiss();
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(BangDiem_Activity.this, "Đã lưu", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(BangDiem_Activity.this, DuKienHoc_Activity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(BangDiem_Activity.this, "Chọn điểm muốn cải thiện đi thím", Toast.LENGTH_LONG).show();
                        }
                    }
                });
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
