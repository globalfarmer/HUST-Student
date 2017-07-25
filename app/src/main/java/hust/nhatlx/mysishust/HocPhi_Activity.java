package hust.nhatlx.mysishust;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class HocPhi_Activity extends AppCompatActivity {
    String data;
    EditText edtHocPhiMuc_1, edtSoTinChiMuc_1,
            edtHocPhiMuc_2, edtSoTinChiMuc_2,
            edtHocPhiDacBiet, edtSoTinChiDacBiet,
            edtPhuPhi;
    TextView txtLuuY, txtTongHocPhi;
    Button btnTinh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoc_phi);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarHocPhi);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HocPhi_Activity.this, TongQuan_Activity.class);
                startActivity(intent);
            }
        });


        edtHocPhiMuc_1 = (EditText) findViewById(R.id.edtMucHocPhi_1);
        edtSoTinChiMuc_1 = (EditText) findViewById(R.id.edtSoTinChiMuc_1);
        edtHocPhiMuc_2 = (EditText) findViewById(R.id.edtMucHocPhi_2);
        edtSoTinChiMuc_2 = (EditText) findViewById(R.id.edtSoTinChiMuc_2);
        edtHocPhiDacBiet = (EditText) findViewById(R.id.edtMucHocPhi_3);
        edtSoTinChiDacBiet = (EditText) findViewById(R.id.edtSoTinChiMuc_3);
        edtPhuPhi = (EditText) findViewById(R.id.edtPhuPhi_HocPhi);
        txtLuuY = (TextView) findViewById(R.id.txtLuuY_HocPhi);
        txtTongHocPhi = (TextView) findViewById(R.id.txtTong_HocPhi);
        btnTinh = (Button) findViewById(R.id.btnTinh_HocPhi);
        try {
            readData();
            JSONObject dataJsonO = new JSONObject(data);
            JSONObject hocPhiJsonO = dataJsonO.getJSONObject("hocPhi");
            String hocPhiMuc_1 = hocPhiJsonO.getString("muc1");
            String hocPhiMuc_2 = hocPhiJsonO.getString("muc2");
            String tinChiTrongChuongTrinh = hocPhiJsonO.getString("tinChiTrongChuongTrinh");
            String tinChiNgoaiChuongTrinh = hocPhiJsonO.getString("tinChiNgoaiChuongTrinh");
            String tinChiDacBiet = hocPhiJsonO.getString("tinChiDacBiet");
            String phuPhi = hocPhiJsonO.getString("phuPhi");
            String tongTienPhaiDong = hocPhiJsonO.getString("tongTienPhaiDong");
            String ghiChu = hocPhiJsonO.getString("ghiChu");

            edtHocPhiMuc_1.setText(hocPhiMuc_1.substring(12).replace(",","").replace(" đ.",""));
            edtHocPhiMuc_2.setText(hocPhiMuc_2.substring(12).replace(",","").replace(" đ.",""));
            edtSoTinChiMuc_1.setText(tinChiTrongChuongTrinh.substring(36).replace(".",""));
            edtSoTinChiMuc_2.setText(tinChiNgoaiChuongTrinh.substring(36).replace(".",""));
            edtSoTinChiDacBiet.setText(tinChiDacBiet.substring(61).replace(".",""));
            edtPhuPhi.setText(phuPhi.substring(9).replace(" đ.",""));
            edtHocPhiDacBiet.setText("0");
            txtLuuY.setText(ghiChu);
            txtTongHocPhi.setText(tongTienPhaiDong.substring(24).replace(" đ.","").replaceAll(",",""));

            btnTinh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer hocPhiMuc1 = Integer.parseInt(edtHocPhiMuc_1.getText().toString())
                            * Integer.parseInt(edtSoTinChiMuc_1.getText().toString());
                    Integer hocPhiMuc2 = Integer.parseInt(edtHocPhiMuc_2.getText().toString())
                            * Integer.parseInt(edtSoTinChiMuc_2.getText().toString());
                    Integer hocPhiDacBiet = Integer.parseInt(edtHocPhiDacBiet.getText().toString())
                            * Integer.parseInt(edtSoTinChiDacBiet.getText().toString());
                    Integer tong = hocPhiMuc1 + hocPhiMuc2 + hocPhiDacBiet;
                    txtTongHocPhi.setText(String.valueOf(tong));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
