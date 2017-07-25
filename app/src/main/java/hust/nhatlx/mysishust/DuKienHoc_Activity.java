package hust.nhatlx.mysishust;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import hust.nhatlx.mysishust.adapter.DuKienHocAdapter;
import hust.nhatlx.mysishust.model.HocPhanModel;

public class DuKienHoc_Activity extends AppCompatActivity {
    String data;
    ListView lvCaiThien;
    ArrayList<HocPhanModel> caiThienArrayList;
    DuKienHocAdapter caiThienAdapter;
    TextView txtCPA, txtSoTinChi;
    Button btnAdd;

    EditText edtSoTinChiCanHoc;
    RadioGroup radGMucTieu;
    TextView txtNhanXetMucTieu;
    ImageView imgNhanXetMucTieu;
    double cpaMucTieu, soTinChiCanHoc, cpaDuKienHoc, soTinChiDuKienHoc, soTinChiTichLuy,
            soTinChiConLai;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_du_kien_hoc);
        lvCaiThien = (ListView) findViewById(R.id.lvDuKien_DuKien);
        txtCPA = (TextView) findViewById(R.id.txtCPA_DuKien);
        txtSoTinChi = (TextView) findViewById(R.id.txtSoTinCHi_DuKien);
        btnAdd = (Button) findViewById(R.id.btnAdd_DuKien);
        edtSoTinChiCanHoc = (EditText) findViewById(R.id.edtSoTinChiCanHoc);
        radGMucTieu = (RadioGroup) findViewById(R.id.radG_MucTieu);
        txtNhanXetMucTieu = (TextView) findViewById(R.id.txtNhanXetMucTieu);
        imgNhanXetMucTieu = (ImageView) findViewById(R.id.imgNhanXetMucTieu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDuKien);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DuKienHoc_Activity.this, TongQuan_Activity.class);
                startActivity(intent);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = new AlertDialog.Builder(DuKienHoc_Activity.this)
                        .setView(R.layout.dialog_adddukien)
                        .setTitle("Chọn loại dự kiến")
                        .create();
                dialog.show();
                Button btnAddCaiThien = (Button) dialog.findViewById(R.id.btnAddCaiThien_dialog);
                Button btnAddMonMoi = (Button) dialog.findViewById(R.id.btnAddHocPhanMoi_dialog);
                assert btnAddCaiThien != null;
                btnAddCaiThien.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DuKienHoc_Activity.this, BangDiem_Activity.class);
                        startActivity(intent);
                    }
                });
                assert btnAddMonMoi != null;
                btnAddMonMoi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog dialog1 = new AlertDialog.Builder(DuKienHoc_Activity.this)
                                .setView(R.layout.dialog_themmonmoi)
                                .create();
                        dialog1.show();
                        Button btnLuu = (Button) dialog1.findViewById(R.id.btnLuu_New_dialog);
                        Button btnHuy = (Button) dialog1.findViewById(R.id.btnHuy_New_dialog);
                        assert btnHuy != null;
                        btnHuy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.dismiss();
                                dialog.dismiss();
                            }
                        });
                        assert btnLuu != null;
                        btnLuu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EditText edtMaHocPhan = (EditText) dialog1.findViewById(R.id.edtMaHocPhan_dialog);
                                EditText edtTenHocPhan = (EditText) dialog1.findViewById(R.id.edtTenHocPhan_dialog);
                                EditText edtSoTinChi = (EditText) dialog1.findViewById(R.id.edtSoTinChi_dialog);
                                RadioGroup radioGroup = (RadioGroup) dialog1.findViewById(R.id.radGDiemChu_dialog);
                                boolean checkNotNull = false;
                                assert edtMaHocPhan != null;
                                assert edtTenHocPhan != null;
                                assert edtSoTinChi != null;
                                if (!edtMaHocPhan.getText().toString().equals("")
                                        && !edtTenHocPhan.getText().toString().equals("")
                                        && !edtSoTinChi.getText().toString().equals("")) {
                                    checkNotNull = true;
                                }
                                assert radioGroup != null;
                                if (checkNotNull && radioGroup.getCheckedRadioButtonId() != -1) {
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
                                        HocPhanModel diemHocPhanModel = new HocPhanModel();
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
                                            //Them mon moi------------------------------------------------
                                            JSONObject monCaiThienNewJsonO = new JSONObject();
                                            monCaiThienNewJsonO.put("maHocPhan", edtMaHocPhan.getText());
                                            monCaiThienNewJsonO.put("soTinChi", edtSoTinChi.getText());
                                            monCaiThienNewJsonO.put("tenHocPhan", edtTenHocPhan.getText());
                                            monCaiThienNewJsonO.put("loaiDuKien", "mon moi");
                                            String diemChuCaiThien;
                                            int idx = radioGroup.getCheckedRadioButtonId();
                                            View radButton = radioGroup.findViewById(idx);
                                            int radioID = radioGroup.indexOfChild(radButton);
                                            RadioButton btn = (RadioButton) radioGroup.getChildAt(radioID);
                                            diemChuCaiThien = (String) btn.getText();
                                            monCaiThienNewJsonO.put("diemChuCaiThien", diemChuCaiThien);

                                            caiThienList.add(monCaiThienNewJsonO.toString());

                                        } else {
                                            //Them mon moi------------------------------------------------
                                            JSONObject monCaiThienNewJsonO = new JSONObject();
                                            monCaiThienNewJsonO.put("maHocPhan", edtMaHocPhan.getText());
                                            monCaiThienNewJsonO.put("soTinChi", edtSoTinChi.getText());
                                            monCaiThienNewJsonO.put("tenHocPhan", edtTenHocPhan.getText());
                                            monCaiThienNewJsonO.put("loaiDuKien", "mon moi");
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
                                    } catch (JSONException | IOException e) {
                                        e.printStackTrace();
                                    }
                                    dialog1.dismiss();
                                    dialog.dismiss();
                                    Toast.makeText(DuKienHoc_Activity.this, "Đã lưu", Toast.LENGTH_LONG).show();
                                    readData();
                                    hienThiListView();
                                    hienThiCPAvaSoTinChiCaiThien();

                                } else {
                                    Toast.makeText(DuKienHoc_Activity.this, "Điền thông tin đi thím :)", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
            }
        });
        readData();
        hienThiListView();
        hienThiCPAvaSoTinChiCaiThien();
        addEvents();
        radGMucTieu_isChecked();
    }

    private void radGMucTieu_isChecked() {
        radGMucTieu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                String edtSoTinCHiCanHoc1 = edtSoTinChiCanHoc.getText().toString();
                switch (checkedId) {
                    case R.id.radXuatSac:
                        cpaMucTieu = 3.6;
                        if(!edtSoTinCHiCanHoc1.equals("")){
                            tinhToanMucTieu();
                        }else {
                            Toast.makeText(DuKienHoc_Activity.this, "Điền tổng số tín chỉ cần học đi thím :)", Toast.LENGTH_LONG).show();
                            imgNhanXetMucTieu.setBackgroundResource(R.drawable.muctieucuathimlagi);
                            txtNhanXetMucTieu.setText("Mục tiêu của thím là gì?");
                        }
                        break;
                    case R.id.radGioi:
                        cpaMucTieu = 3.2;
                        if(!edtSoTinCHiCanHoc1.equals("")){
                            tinhToanMucTieu();
                        }else {
                            Toast.makeText(DuKienHoc_Activity.this, "Điền tổng số tín chỉ cần học đi thím :)", Toast.LENGTH_LONG).show();
                            imgNhanXetMucTieu.setBackgroundResource(R.drawable.muctieucuathimlagi);
                            txtNhanXetMucTieu.setText("Mục tiêu của thím là gì?");
                        }
                        break;
                    case R.id.radKha:
                        cpaMucTieu = 2.5;
                        if(!edtSoTinCHiCanHoc1.equals("")){
                            tinhToanMucTieu();
                        }else {
                            Toast.makeText(DuKienHoc_Activity.this, "Điền tổng số tín chỉ cần học đi thím :)", Toast.LENGTH_LONG).show();
                            imgNhanXetMucTieu.setBackgroundResource(R.drawable.muctieucuathimlagi);
                            txtNhanXetMucTieu.setText("Mục tiêu của thím là gì?");
                        }
                        break;
                    case R.id.radTrungBinh:
                        cpaMucTieu = 2.0;
                        if(!edtSoTinCHiCanHoc1.equals("")){
                            tinhToanMucTieu();
                            imgNhanXetMucTieu.setBackgroundResource(R.drawable.vuahocvuachoi);
                        }else {
                            Toast.makeText(DuKienHoc_Activity.this, "Điền tổng số tín chỉ cần học đi thím :)", Toast.LENGTH_LONG).show();
                            imgNhanXetMucTieu.setBackgroundResource(R.drawable.muctieucuathimlagi);
                            txtNhanXetMucTieu.setText("Mục tiêu của thím là gì?");
                        }
                        break;
                    case R.id.radYeuTruong:
                        txtNhanXetMucTieu.setText("Quẩy đi thím :v");
                        imgNhanXetMucTieu.setBackgroundResource(R.drawable.quaydithim);
                        break;
                }
            }
        });
    }

    private void tinhToanMucTieu() {
        if (!edtSoTinChiCanHoc.getText().equals("")) {
            soTinChiCanHoc = Double.parseDouble(edtSoTinChiCanHoc.getText().toString());
        }
        cpaDuKienHoc = Double.parseDouble(txtCPA.getText().toString());
        soTinChiDuKienHoc = 0;
        soTinChiTichLuy = 0;
        try {
            JSONObject dataJsonO = new JSONObject(data);
            JSONArray duKienJsonA = dataJsonO.getJSONArray("caiThien");
            if (duKienJsonA.length() != 0) {
                for (int i = 0; i < duKienJsonA.length(); i++) {
                    JSONObject duKienJsonO = duKienJsonA.getJSONObject(i);
                    String loaiDuKien = duKienJsonO.getString("loaiDuKien");
                    if (loaiDuKien.equals("mon moi")) {
                        double soTinChi = Double.parseDouble(duKienJsonO.getString("soTinChi"));
                        soTinChiDuKienHoc = soTinChiDuKienHoc + soTinChi;
                    }
                }
            }
            JSONObject bangDiemCaNhanJsonO = dataJsonO.getJSONObject("bangDiemCaNhan");
            JSONArray bangDiemTongKetJsonA = bangDiemCaNhanJsonO.getJSONArray("diemTongKet");
            JSONObject diemTongKetLast = bangDiemTongKetJsonA.getJSONObject(bangDiemTongKetJsonA.length() - 1);
            soTinChiTichLuy = Double.parseDouble(diemTongKetLast.getString("soTinChiTichLuy"));
            soTinChiConLai = soTinChiCanHoc - soTinChiTichLuy - soTinChiDuKienHoc;
            double tongDiemConLai = cpaMucTieu * soTinChiCanHoc - cpaDuKienHoc * (soTinChiTichLuy + soTinChiDuKienHoc);
            double cpaTrungBinhConLai = tongDiemConLai/soTinChiConLai;
            if(cpaTrungBinhConLai>4.0){
                txtNhanXetMucTieu.setText("Chúng ta không thuộc về nhau :'(");
                imgNhanXetMucTieu.setBackgroundResource(R.drawable.chungtakhongthuocvenhau2);
            }else if(cpaTrungBinhConLai<0){
                txtNhanXetMucTieu.setText("Vừa học vừa chơi cũng được :)");
                imgNhanXetMucTieu.setBackgroundResource(R.drawable.vuahocvuachoi);

            } else {
                String cpaCPATrungBinhConLai_String = String.valueOf((double) Math.round(cpaTrungBinhConLai * 100) / 100);
                txtNhanXetMucTieu.setText("Điểm trung bình còn lại cần: " + cpaCPATrungBinhConLai_String);
                imgNhanXetMucTieu.setBackgroundResource(R.drawable.goodluck);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addEvents() {
        //List view item click===================================================================
        lvCaiThien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog dialog = new AlertDialog.Builder(DuKienHoc_Activity.this)
                        .setView(R.layout.dialog_dukien)
                        .create();
                dialog.show();

                Button btnHuy = (Button) dialog.findViewById(R.id.btnHuy);
                assert btnHuy != null;
                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button btnXoa = (Button) dialog.findViewById(R.id.btnXoa);
                assert btnXoa != null;
                btnXoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DeleteCaiThienTask deleteCaiThienTask = new DeleteCaiThienTask();
                        deleteCaiThienTask.execute(position);
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    private void hienThiCPAvaSoTinChiCaiThien() {
        try {
            JSONObject dataJsonO = new JSONObject(data);
            //Hien thi so tin chi cai thien---------------------------------------------------------
            JSONArray caiThienJsonA = dataJsonO.getJSONArray("caiThien");
            int soTinChi = 0;
            for (int i = 0; i < caiThienJsonA.length(); i++) {
                JSONObject monCaiThienJsonO = caiThienJsonA.getJSONObject(i);
                soTinChi += Integer.parseInt(monCaiThienJsonO.getString("soTinChi"));
            }
            String soTinChiCaiThien = String.valueOf(soTinChi);
            txtSoTinChi.setText(soTinChiCaiThien + " tín");

            //Hien thi cpa cai thien----------------------------------------------------------------
            JSONObject bangDiemCaNhanJsonO = dataJsonO.getJSONObject("bangDiemCaNhan");
            JSONArray bangDiemTongKetJsonA = bangDiemCaNhanJsonO.getJSONArray("diemTongKet");
            JSONObject diemTongKetLast = bangDiemTongKetJsonA.getJSONObject(bangDiemTongKetJsonA.length() - 1);
            String cpa = diemTongKetLast.getString("cpa");
            String soTinChiTichLuyString = diemTongKetLast.getString("soTinChiTichLuy");

            Double soTinChiTichLuyHienTai = Double.parseDouble(soTinChiTichLuyString);
            Double cpaDuKien = Double.parseDouble(cpa);
            Double soTinChiSauDuKien = soTinChiTichLuyHienTai;


            for (int j = 0; j < caiThienJsonA.length(); j++) {
                JSONObject monDuKienJsonO = caiThienJsonA.getJSONObject(j);
                String loaiDuKien = monDuKienJsonO.getString("loaiDuKien");
                if (loaiDuKien.equals("cai thien")) {
                    String diemChuCaiThien = monDuKienJsonO.getString("diemChuCaiThien");
                    double diemSoCaiThien = 0;
                    switch (diemChuCaiThien) {
                        case "A/A+":
                            diemSoCaiThien = 4;
                            break;
                        case "B+":
                            diemSoCaiThien = 3.5;
                            break;
                        case "B":
                            diemSoCaiThien = 3;
                            break;
                        case "C+":
                            diemSoCaiThien = 2.5;
                            break;
                        case "C":
                            diemSoCaiThien = 2;
                            break;
                        case "D+":
                            diemSoCaiThien = 1.5;
                            break;
                        case "D":
                            diemSoCaiThien = 1;
                            break;
                    }
                    String soTinChiHocPhanString = monDuKienJsonO.getString("soTinChi");
                    Double diemSoHienTai = monDuKienJsonO.getDouble("diemSoHienTai");
                    Double soTinChiHocPhan = Double.parseDouble(soTinChiHocPhanString);
                    cpaDuKien = (cpaDuKien * soTinChiSauDuKien - (diemSoHienTai - diemSoCaiThien) * soTinChiHocPhan) / soTinChiSauDuKien;

                } else if (loaiDuKien.equals("mon moi")) {
                    String soTinChiMonMoiString = monDuKienJsonO.getString("soTinChi");
                    String diemChuMonMoiString = monDuKienJsonO.getString("diemChuCaiThien");
                    double diemSoMonMoi = 0;
                    switch (diemChuMonMoiString) {
                        case "A/A+":
                            diemSoMonMoi = 4;
                            break;
                        case "B+":
                            diemSoMonMoi = 3.5;
                            break;
                        case "B":
                            diemSoMonMoi = 3;
                            break;
                        case "C+":
                            diemSoMonMoi = 2.5;
                            break;
                        case "C":
                            diemSoMonMoi = 2;
                            break;
                        case "D+":
                            diemSoMonMoi = 1.5;
                            break;
                        case "D":
                            diemSoMonMoi = 1;
                            break;
                    }
                    Double soTinChiMonMoi = Double.parseDouble(soTinChiMonMoiString);
                    soTinChiSauDuKien += soTinChiMonMoi;
                    cpaDuKien = (cpaDuKien * (soTinChiSauDuKien - soTinChiMonMoi) + diemSoMonMoi * soTinChiMonMoi) / soTinChiSauDuKien;
                }
            }
            String cpaDuKienString = String.valueOf((double) Math.round(cpaDuKien * 100) / 100);
            txtCPA.setText(cpaDuKienString);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void hienThiListView() {
        caiThienArrayList = new ArrayList<>();
        try {
            JSONObject dataJsonO = new JSONObject(data);
            JSONArray caiThienJsonA = dataJsonO.getJSONArray("caiThien");
            for (int i = 0; i < caiThienJsonA.length(); i++) {
                JSONObject monCaiThien = caiThienJsonA.getJSONObject(i);
                HocPhanModel diemHocPhanModel = new HocPhanModel();
                diemHocPhanModel.setMaHocPhan(monCaiThien.getString("maHocPhan"));
                diemHocPhanModel.setTenHocPhan(monCaiThien.getString("tenHocPhan"));
                diemHocPhanModel.setSoTinChi(monCaiThien.getString("soTinChi"));
                diemHocPhanModel.setDiemChu(monCaiThien.getString("diemChuCaiThien"));
                caiThienArrayList.add(diemHocPhanModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        caiThienAdapter = new DuKienHocAdapter(
                DuKienHoc_Activity.this,
                R.layout.item_dukien,
                caiThienArrayList);
        lvCaiThien.setAdapter(caiThienAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Nếu nhan back thi quay ve main activity
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(DuKienHoc_Activity.this, TongQuan_Activity.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
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

    private class DeleteCaiThienTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(final Integer... params) {
            int position = params[0];
            try {
                //Doc data de ghi lai vao file moi--------------------------------------
                JSONObject dataJsonO = new JSONObject(data);
                JSONObject bangDiemCaNhanJsonO = dataJsonO.getJSONObject("bangDiemCaNhan");
                JSONArray lichHocJsonA = dataJsonO.getJSONArray("lichHoc");
                JSONObject taiKhoanLoginJsonO = dataJsonO.getJSONObject("taiKhoanLogin");
                JSONArray thongTinJsonA = dataJsonO.getJSONArray("thongTin");
                JSONArray caiThienJsonA = dataJsonO.getJSONArray("caiThien");
                JSONObject hocPhiJsonO = dataJsonO.getJSONObject("hocPhi");
                ArrayList<String> caiThienList = new ArrayList<>();
                for (int i = 0; i < caiThienJsonA.length(); i++) {
                    caiThienList.add(caiThienJsonA.getJSONObject(i).toString());
                }
                caiThienList.remove(position);
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
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            readData();
            hienThiListView();
            hienThiCPAvaSoTinChiCaiThien();
        }
    }
}
