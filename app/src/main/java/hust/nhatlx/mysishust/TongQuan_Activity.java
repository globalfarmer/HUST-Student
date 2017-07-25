package hust.nhatlx.mysishust;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class TongQuan_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String data;
    BarChart chart;
    LineChart lineChart;
    TextView txtNhanXet, txtHoTen, txtLop, txtTinChiTichLuy, txtTinChiNo, txtCPA;
    int soMonCu = 0, soMonMoi = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tong_quan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        readData();
        addControls();
        hienThiBarChart();
        hienThiLineChartCPA();
        hienThiThongTinSinhVien();
        hienThiThongTinTongQuan();
    }

    private void addControls() {
        chart = (BarChart) findViewById(R.id.chart);
        lineChart = (LineChart) findViewById(R.id.lineChart);
        txtNhanXet = (TextView) findViewById(R.id.txtNhanXet_TongQuan);
        txtHoTen = (TextView) findViewById(R.id.txtHoTen_TongQuan);
        txtLop = (TextView) findViewById(R.id.txtLop_TongQuan);
        txtTinChiTichLuy = (TextView) findViewById(R.id.txtTInChiTichLuy_TongQuan);
        txtTinChiNo = (TextView) findViewById(R.id.txtTinChiNo_TongQuan);
        txtCPA = (TextView) findViewById(R.id.txtCPA_TongQuan);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tong_quan_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_CapNhat) {
            capNhatDuLieu();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void capNhatDuLieu() {
        if (checkInternet()) {
            File dir = getFilesDir();
            File file = new File(dir, "sis.txt");
            boolean delete = file.delete();
            if (delete) {
                try {
                    JSONObject dataJsonO = new JSONObject(data);
                    JSONObject taiKhoanLogin = dataJsonO.getJSONObject("taiKhoanLogin");
                    HashMap<String, String> acount = new HashMap<>();
                    //Gia ma tai khoan
                    byte[] maSV_data = Base64.decode(taiKhoanLogin.getString("maSinhVien"), Base64.DEFAULT);
                    byte[] pass_data = Base64.decode(taiKhoanLogin.getString("passWord"), Base64.DEFAULT);
                    String maSV_decodeString = new String(maSV_data, "UTF-8");
                    String pass_decodeString = new String(pass_data, "UTF-8");
                    acount.put("maSinhVien", maSV_decodeString);
                    acount.put("passWord", pass_decodeString);

                    ParseKetQuaHTMLTask parseKetQuaHTMLTask = new ParseKetQuaHTMLTask();
                    parseKetQuaHTMLTask.execute(acount);
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_TinTuc) {
            Intent intent = new Intent(TongQuan_Activity.this, TinTuc_Activity.class);
            startActivity(intent);

        } else if (id == R.id.nav_LichHoc) {
            Intent intent = new Intent(TongQuan_Activity.this, LichHoc_Activity.class);
            startActivity(intent);

        } else if (id == R.id.nav_MonNo) {
            Intent intent = new Intent(TongQuan_Activity.this, MonNo_Activity.class);
            startActivity(intent);

        } else if (id == R.id.nav_BangDiem) {
            Intent intent = new Intent(TongQuan_Activity.this, BangDiem_Activity.class);
            startActivity(intent);

        } else if (id == R.id.nav_DuKienHoc) {
            Intent intent = new Intent(TongQuan_Activity.this, DuKienHoc_Activity.class);
            startActivity(intent);

        } else if (id == R.id.nav_HocPhi) {
            Intent intent = new Intent(TongQuan_Activity.this, HocPhi_Activity.class);
            startActivity(intent);

        } else if (id == R.id.nav_About_me) {
            Intent intent = new Intent(TongQuan_Activity.this, AboutMe_Activity.class);
            startActivity(intent);

        } else if (id == R.id.nav_DangXuat) {
            File dir = getFilesDir();
            File file = new File(dir, "sis.txt");
            boolean delete = file.delete();
            if (delete) {
                Intent intent = new Intent(TongQuan_Activity.this, Login_Activity.class);
                startActivity(intent);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void hienThiLineChartCPA() {
        float cpaEnd;
        try {
            JSONObject dataJsonO = new JSONObject(data);
            JSONObject bangDiemCaNhanJsonO = dataJsonO.getJSONObject("bangDiemCaNhan");
            JSONArray bangDiemTongKetJsonA = bangDiemCaNhanJsonO.getJSONArray("diemTongKet");
            //Hien thi nhan xet---------------------------------------------------------------------
            JSONObject end = bangDiemTongKetJsonA.getJSONObject(bangDiemTongKetJsonA.length() - 1);
            cpaEnd = Float.valueOf(end.getString("cpa"));
            if (cpaEnd < 2.5) {
                txtNhanXet.setText("Hazz, cố gắng lên thím, CPA tù quá :'(");
                txtNhanXet.setTextColor(Color.RED);
            } else if (cpaEnd >= 3.2) {
                txtNhanXet.setText("Good, cố gắng duy trì nha thím :D");
                txtNhanXet.setTextColor(Color.GREEN);
            } else {
                txtNhanXet.setText("OK, tiếp tục cố gắng nha thím :)");
            }

            //Hien thi bieu do----------------------------------------------------------------------
            ArrayList<Entry> entriesCPA = new ArrayList<>();
            ArrayList<String> lables = new ArrayList<>();
            for (int i = 0; i < bangDiemTongKetJsonA.length(); i++) {
                JSONObject diemTongKetJsonO = bangDiemTongKetJsonA.getJSONObject(i);
                String cpa = diemTongKetJsonO.getString("cpa");
                float cpaFloat = Float.valueOf(cpa);
                entriesCPA.add(new Entry(cpaFloat, i));

                String kiHoc = diemTongKetJsonO.getString("kiHoc");
                lables.add(kiHoc);
            }
            LineDataSet dataSet = new LineDataSet(entriesCPA, "CPA");
            dataSet.setValueTextSize(12);
            LineData dataLine = new LineData(lables, dataSet);
            dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
            lineChart.setData(dataLine);
            lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            lineChart.setDescription("");
            lineChart.getXAxis().setTextSize(12);
            lineChart.animateY(1000);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void hienThiThongTinTongQuan() {
        try {
            JSONObject dataJsonO = new JSONObject(data);
            JSONObject bangDiemCaNhanJsonO = dataJsonO.getJSONObject("bangDiemCaNhan");
            JSONArray bangDiemTongKetJsonA = bangDiemCaNhanJsonO.getJSONArray("diemTongKet");
            JSONObject diemTongKetLast = bangDiemTongKetJsonA.getJSONObject(bangDiemTongKetJsonA.length() - 1);
            String cpa = diemTongKetLast.getString("cpa");
            String soTinChiTichLuy = diemTongKetLast.getString("soTinChiTichLuy");
            String soTinChiNoDangKy = diemTongKetLast.getString("soTinChiNoDangKy");
            txtCPA.setText(cpa);
            txtTinChiTichLuy.setText(soTinChiTichLuy);
            txtTinChiNo.setText(soTinChiNoDangKy);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void hienThiBarChart() {
        int soTinCHi_A = 0;
        int soTinChi_Bplus = 0;
        int soTinChi_B = 0;
        int soTinChi_Cplus = 0;
        int soTinChi_C = 0;
        int soTinCHi_Dplus = 0;
        int soTinCHi_D = 0;
        int soTinChi_F = 0;
        try {
            JSONObject dataJsonO = new JSONObject(data);
            JSONObject bangDiemCaNhanJsonO = dataJsonO.getJSONObject("bangDiemCaNhan");
            JSONArray bangDiemHocPhanJsonA = bangDiemCaNhanJsonO.getJSONArray("diemHocPhan");
            for (int i = 0; i < bangDiemHocPhanJsonA.length(); i++) {
                JSONObject diemHocPhanJsonO = bangDiemHocPhanJsonA.getJSONObject(i);
                String diemChu = diemHocPhanJsonO.getString("diemChu");
                String soTinChi = diemHocPhanJsonO.getString("soTinChi");
                int soTinCHi_int = Integer.parseInt(soTinChi);
                switch (diemChu) {
                    case "A+":
                        soTinCHi_A += soTinCHi_int;
                        break;
                    case "A":
                        soTinCHi_A += soTinCHi_int;
                        break;
                    case "B+":
                        soTinChi_Bplus += soTinCHi_int;
                        break;
                    case "B":
                        soTinChi_B += soTinCHi_int;
                        break;
                    case "C+":
                        soTinChi_Cplus += soTinCHi_int;
                        break;
                    case "C":
                        soTinChi_C += soTinCHi_int;
                        break;
                    case "D+":
                        soTinCHi_Dplus += soTinCHi_int;
                        break;
                    case "D":
                        soTinCHi_D += soTinCHi_int;
                        break;
                    case "F":
                        soTinChi_F += soTinCHi_int;
                        break;
                }
            }
            ArrayList<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(soTinCHi_A, 0));
            entries.add(new BarEntry(soTinChi_Bplus, 1));
            entries.add(new BarEntry(soTinChi_B, 2));
            entries.add(new BarEntry(soTinChi_Cplus, 3));
            entries.add(new BarEntry(soTinChi_C, 4));
            entries.add(new BarEntry(soTinCHi_Dplus, 5));
            entries.add(new BarEntry(soTinCHi_D, 6));
            entries.add(new BarEntry(soTinChi_F, 7));

            BarDataSet dataSet = new BarDataSet(entries, "Số tín chỉ");
            dataSet.setValueTextSize(12);
            ArrayList<String> labels = new ArrayList<>();
            labels.add("A/A+");
            labels.add("B+");
            labels.add("B");
            labels.add("C+");
            labels.add("C");
            labels.add("D+");
            labels.add("D");
            labels.add("F");
            BarData barData = new BarData(labels, dataSet);
            barData.setValueFormatter(new MyValueFormatter()); //float to integer
            chart.setData(barData);
            chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
            chart.getXAxis().setTextSize(12);
            chart.animateY(1000);
            chart.setDescription("");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void hienThiThongTinSinhVien() {
        try {
            JSONObject dataJsonO = new JSONObject(data);
            JSONObject bangDiemCaNhanJsonO = dataJsonO.getJSONObject("bangDiemCaNhan");
            JSONObject user = bangDiemCaNhanJsonO.getJSONObject("user");
            txtHoTen.setText(user.getString("hoTen"));
            txtLop.setText(user.getString("lop"));
        } catch (JSONException e) {
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
            Toast.makeText(TongQuan_Activity.this, "Hmmm, Internet đâu thím...", Toast.LENGTH_LONG).show();
        }
        return checkInternet;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Nếu nhan back thi quay ve home
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    //fomat float to integer
    private class MyValueFormatter implements ValueFormatter {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return Math.round(value) + "";
        }
    }

    private class ParseKetQuaHTMLTask extends AsyncTask<HashMap, Void, Boolean> {
        ProgressDialog dialog = new ProgressDialog(TongQuan_Activity.this);
        JSONObject sisJsonO = new JSONObject(); //tat ca thong tin duoc ghi vao day

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Chờ xíu nha thím . . .");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(HashMap... params) {
            try {
                JSONObject dataJsonO = new JSONObject(data);
                JSONObject bangDiemCaNhan = dataJsonO.getJSONObject("bangDiemCaNhan");
                JSONArray bangDiemHocPhan = bangDiemCaNhan.getJSONArray("diemHocPhan");
                soMonCu = bangDiemHocPhan.length();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject acount = new JSONObject(params[0]);
            String maSinhVien = null, passWord = null;
            try {
                maSinhVien = acount.getString("maSinhVien");
                passWord = acount.getString("passWord");
                //Ma hoa du lieu truoc khi luu
                byte[] maSV_data = maSinhVien.getBytes("UTF-8");
                byte[] pass_data = passWord.getBytes("UTF-8");
                String maSV_encodeString = Base64.encodeToString(maSV_data, Base64.DEFAULT);
                String pass_encodeString = Base64.encodeToString(pass_data, Base64.DEFAULT);
                JSONObject taiKhoanLoginJsonO = new JSONObject();
                taiKhoanLoginJsonO.put("maSinhVien", maSV_encodeString);
                taiKhoanLoginJsonO.put("passWord", pass_encodeString);
                sisJsonO.put("taiKhoanLogin", taiKhoanLoginJsonO);
            } catch (JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                //LogIn to Sis======================================================================
                //==================================================================================
                HashMap<String, String> cookies = new HashMap<>();
                HashMap<String, String> formData = new HashMap<>();
                Connection.Response loginForm = Jsoup.connect(Config.LOGIN_URL)
                        .method(Connection.Method.GET)
                        .userAgent(Config.USER_AGENT)
                        .execute();
                Document loginDocument = loginForm.parse();
                cookies.putAll(loginForm.cookies()); //get and save cookies
                //get thong tin truy cap, mỗi lần sẽ có 1 mã khác nhau
                String __EVENTVALIDATION = loginDocument.select("input[id=__EVENTVALIDATION]").first().attr("value");
                String __VIEWSTATE = loginDocument.select("input[id=__VIEWSTATE]").first().attr("value");
                //Chuẩn bị data post theo mẫu của web
                formData.put("__EVENTTARGET", "ctl00$cLogIn1$bt_cLogIn");
                formData.put("__EVENTARGUMENT", "Click");
                formData.put("__VIEWSTATE", __VIEWSTATE);
                formData.put("__VIEWSTATEGENERATOR", "CA0B0334");
                formData.put("__EVENTVALIDATION", __EVENTVALIDATION);
                formData.put("ctl00$cLogIn1$tb_cLogIn_User", maSinhVien);
                formData.put("ctl00$cLogIn1$tb_cLogIn_Pass", passWord);
                formData.put("ctl00$MainContent$Hướng dẫn", "0;3;1;0");
                formData.put("ctl00$MainContent$Lịch thi", "0;3;5;0");
                formData.put("ctl00$MainContent$Thông báo của ban Quản trị", "0;3;18;0");
                formData.put("ctl00$MainContent$Thông báo đăng ký học tập", "0;3;22;0");
                formData.put("ctl00$MainContent$Thông báo xét nhận đồ án tốt nghiệp", "0;3;4;0");
                formData.put("ctl00$MainContent$Thông báo xét tốt nghiệp", "0;3;14;0");
                formData.put("ctl00$MainContent$Thông báo xử lý học tập", "0;3;6;0");
                formData.put("ctl00$MainContent$ctl08", "0;3;1;0");
                formData.put("DXScript", "1_145,1_81,1_137,1_122,1_99,1_106,1_78,1_92,1_130,1_135,1_121,1_126,1_84,1_124");
                //Tien hanh login
                Connection.Response homePage = Jsoup.connect(Config.LOGIN_URL_ACTION)
                        .cookies(cookies)
                        .data(formData)
                        .method(Connection.Method.POST)
                        .userAgent(Config.USER_AGENT)
                        .execute();
                //Check login not null
                if (homePage != null) {
                    //Parse thong tin thong bao=========================================================
                    //==================================================================================
                    Document homPageDoc = homePage.parse();
                    Element bangThongBaoElement = homPageDoc.getElementById("MainContent_ctl08_ICell");
                    Elements theThongTinElements = bangThongBaoElement.select("div[class=dxncItemHeader_SisTheme]");
                    JSONArray thongTinJsonA = new JSONArray();
                    for (Element element : theThongTinElements) {
                        String thongTin = element.getElementsByTag("a").text();
                        String urlThongTin = element.getElementsByTag("a").attr("href");
                        JSONObject thongTinJsonO = new JSONObject();
                        thongTinJsonO.put("thongTin", thongTin);
                        thongTinJsonO.put("urlThongTin", urlThongTin);
                        thongTinJsonA.put(thongTinJsonO);
                    }
                    sisJsonO.put("thongTin", thongTinJsonA);
                }
                JSONObject dataJsonO = new JSONObject(data);
                JSONObject hocPhiJsonO = dataJsonO.getJSONObject("hocPhi");
                sisJsonO.put("hocPhi", hocPhiJsonO);

                //Go to bang diem ca nhan-----------------------------------------------------------
                Connection.Response bangDiemCaNhan = Jsoup.connect("http://sis.hust.edu.vn/ModuleGradeBook/StudentCourseMarks.aspx")
                        .cookies(cookies)
                        .method(Connection.Method.GET)
                        .userAgent(Config.USER_AGENT)
                        .execute();
                if (bangDiemCaNhan != null) {
                    //Parse bang diem ca nhan ==========================================================
                    //==================================================================================
                    Document bangDiemCaNhanDoc = bangDiemCaNhan.parse();
                    //parse user------------------------------------------------------------------------
                    Element user = bangDiemCaNhanDoc.getElementById("MainContent_rpInfo_RPC");
                    String msv = user.getElementsByTag("b").first().text();
                    String hoTen = user.getElementsByTag("b").get(1).text();
                    String ngaySinh = user.getElementsByTag("b").get(2).text();
                    String lop = user.getElementsByTag("b").get(3).text();
                    String chuongTrinh = user.getElementsByTag("b").get(4).text();
                    String heHoc = user.getElementsByTag("b").get(5).text();
                    String trangThai = user.getElementsByTag("b").get(6).text();
                    //Convert to Json
                    JSONObject userJsonO = new JSONObject();
                    userJsonO.put("msv", msv);
                    userJsonO.put("hoTen", hoTen);
                    userJsonO.put("ngaySinh", ngaySinh);
                    userJsonO.put("lop", lop);
                    userJsonO.put("chuongTrinh", chuongTrinh);
                    userJsonO.put("heHoc", heHoc);
                    userJsonO.put("trangThai", trangThai);

                    //Parse bang hoc phan---------------------------------------------------------------
                    Element bangDiemHocPhan = bangDiemCaNhanDoc.getElementById("MainContent_gvCourseMarks_DXMainTable");
                    Elements elements = bangDiemHocPhan.select("tr.dxgvDataRow_SisTheme");
                    JSONArray diemHocPhanJsonA = new JSONArray();
                    for (Element element : elements) {
                        String maHocPhan = element.select("td.dxgv").get(1).text();
                        String tenHocPhan = element.select("td.dxgv").get(2).text();
                        String soTinChi = element.select("td.dxgv").get(3).text();
                        String diemQuaTrinh = element.select("td.dxgv").get(5).text();
                        String diemCuoiKi = element.select("td.dxgv").get(6).text();
                        String diemChu = element.select("td.dxgv").get(7).text();

                        double diemSo = 0;
                        switch (diemChu) {
                            case "A+":
                                diemSo = 4;
                                break;
                            case "A":
                                diemSo = 4;
                                break;
                            case "B+":
                                diemSo = 3.5;
                                break;
                            case "B":
                                diemSo = 3;
                                break;
                            case "C+":
                                diemSo = 2.5;
                                break;
                            case "C":
                                diemSo = 2;
                                break;
                            case "D+":
                                diemSo = 1.5;
                                break;
                            case "D":
                                diemSo = 1;
                                break;
                            case "F":
                                diemSo = 0;
                                break;
                        }

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("maHocPhan", maHocPhan);
                        jsonObject.put("tenHocPhan", tenHocPhan);
                        jsonObject.put("soTinChi", soTinChi);
                        jsonObject.put("diemChu", diemChu);
                        jsonObject.put("diemQuaTrinh", diemQuaTrinh);
                        jsonObject.put("diemCuoiKi", diemCuoiKi);
                        jsonObject.put("diemSo", diemSo);

                        //Put to json array
                        diemHocPhanJsonA.put(jsonObject);
                    }

                    //Parse diem tong ket---------------------------------------------------------------
                    Element bangDiemTongKet = bangDiemCaNhanDoc.getElementById("MainContent_gvResults_DXMainTable");
                    Elements elements1 = bangDiemTongKet.select("tr.dxgvDataRow_SisTheme");
                    JSONArray diemTongKetJsonA = new JSONArray();
                    for (Element element : elements1) {
                        String kiHoc = element.select("td.dxgv").get(0).text();
                        String gpa = element.select("td.dxgv").get(1).text();
                        String cpa = element.select("td.dxgv").get(2).text();
                        String soTinChiTichLuy = element.select("td.dxgv").get(4).text();
                        String soTinChiNoDangKy = element.select("td.dxgv").get(5).text();
                        String thieuDiem = element.select("td.dxgv").get(9).text();

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("kiHoc", kiHoc);
                        jsonObject.put("gpa", gpa);
                        jsonObject.put("cpa", cpa);
                        jsonObject.put("soTinChiTichLuy", soTinChiTichLuy);
                        jsonObject.put("soTinChiNoDangKy", soTinChiNoDangKy);
                        jsonObject.put("thieuDiem", thieuDiem);

                        diemTongKetJsonA.put(jsonObject);
                        //Lọc học phần bị trùng hoac thap hon-----------------------------------------------
                        JSONArray diemHocPhanJsonASauLoc = new JSONArray();
                        for (int i = 0; i < diemHocPhanJsonA.length(); i++) {
                            JSONObject jsonObject1 = diemHocPhanJsonA.getJSONObject(i);
                            String maHocPhan = jsonObject1.getString("maHocPhan");
                            String tenHocPhan = jsonObject1.getString("tenHocPhan");
                            String soTinChi = jsonObject1.getString("soTinChi");
                            String diemQuaTrinh = jsonObject1.getString("diemQuaTrinh");
                            String diemCuoiKi = jsonObject1.getString("diemCuoiKi");
                            String diemChu = jsonObject1.getString("diemChu");
                            Double diemSo = jsonObject1.getDouble("diemSo");
                            for (int j = i + 1; j < diemHocPhanJsonA.length(); j++) {
                                JSONObject jsonObject2 = diemHocPhanJsonA.getJSONObject(j);
                                String maHocPhan2 = jsonObject2.getString("maHocPhan");
                                Double diemSo2 = jsonObject2.getDouble("diemSo");
                                if (maHocPhan.equals(maHocPhan2) && diemSo < diemSo2) {
                                    maHocPhan = jsonObject2.getString("maHocPhan");
                                    tenHocPhan = jsonObject2.getString("tenHocPhan");
                                    soTinChi = jsonObject2.getString("soTinChi");
                                    diemQuaTrinh = jsonObject2.getString("diemQuaTrinh");
                                    diemCuoiKi = jsonObject2.getString("diemCuoiKi");
                                    diemChu = jsonObject2.getString("diemChu");
                                    diemSo = jsonObject2.getDouble("diemSo");
                                }
                            }
                            if (diemHocPhanJsonASauLoc.length() != 0) {
                                boolean check = false;
                                for (int k = 0; k < diemHocPhanJsonASauLoc.length(); k++) {
                                    JSONObject diemHocPhanCheck = diemHocPhanJsonASauLoc.getJSONObject(k);
                                    String maHocPhanCheck = diemHocPhanCheck.getString("maHocPhan");
                                    if (maHocPhan.equals(maHocPhanCheck)) {
                                        check = true;
                                    }
                                }
                                if (!check) {
                                    JSONObject diemHocPhanJsonO = new JSONObject();
                                    diemHocPhanJsonO.put("maHocPhan", maHocPhan);
                                    diemHocPhanJsonO.put("tenHocPhan", tenHocPhan);
                                    diemHocPhanJsonO.put("soTinChi", soTinChi);
                                    diemHocPhanJsonO.put("diemChu", diemChu);
                                    diemHocPhanJsonO.put("diemQuaTrinh", diemQuaTrinh);
                                    diemHocPhanJsonO.put("diemCuoiKi", diemCuoiKi);
                                    diemHocPhanJsonO.put("diemSo", diemSo);
                                    diemHocPhanJsonASauLoc.put(diemHocPhanJsonO);
                                }
                            } else {
                                JSONObject diemHocPhanJsonO = new JSONObject();
                                diemHocPhanJsonO.put("maHocPhan", maHocPhan);
                                diemHocPhanJsonO.put("tenHocPhan", tenHocPhan);
                                diemHocPhanJsonO.put("soTinChi", soTinChi);
                                diemHocPhanJsonO.put("diemChu", diemChu);
                                diemHocPhanJsonO.put("diemQuaTrinh", diemQuaTrinh);
                                diemHocPhanJsonO.put("diemCuoiKi", diemCuoiKi);
                                diemHocPhanJsonO.put("diemSo", diemSo);
                                diemHocPhanJsonASauLoc.put(diemHocPhanJsonO);
                            }
                        }
                        //Bang diem ca nhan to JsonO
                        JSONObject bangDiemCaNhanJsonO = new JSONObject();
                        bangDiemCaNhanJsonO.put("user", userJsonO);
                        bangDiemCaNhanJsonO.put("diemHocPhan", diemHocPhanJsonASauLoc);
                        bangDiemCaNhanJsonO.put("diemTongKet", diemTongKetJsonA);

                        sisJsonO.put("bangDiemCaNhan", bangDiemCaNhanJsonO);
                    }
                }

                //Parse Lich Hoc ===================================================================
                //==================================================================================
                //Connect lấy dữ liệu---------------------------------------------------------------
                HashMap<String, String> cookies2 = new HashMap<>();
                HashMap<String, String> formData2 = new HashMap<>();
                Connection.Response tkbForm2 = Jsoup.connect(Config.TKB_URL)
                        .method(Connection.Method.GET)
                        .userAgent(Config.USER_AGENT)
                        .execute();
                if (tkbForm2 != null) {
                    Document tkbDoc = tkbForm2.parse();
                    cookies.putAll(tkbForm2.cookies());

                    String __EVENTVALIDATION2 = tkbDoc.select("input[id=__EVENTVALIDATION]").first().attr("value");
                    String __VIEWSTATE2 = tkbDoc.select("input[id=__VIEWSTATE]").first().attr("value");
                    String CallBackState = tkbDoc.select("input[id=MainContent_gvStudentRegister_CallbackState]").first().attr("value");
                    String CBS = tkbDoc.select("input[id=ctl00_MainContent_ASPxScheduler1_stateBlock_CBS]").first().attr("value");
                    String stateBlockDay = tkbDoc.select("input[id=ctl00_MainContent_ASPxScheduler1_stateBlock_VDAYS]").first().attr("value");

                    formData2.put("__EVENTTARGET", "");
                    formData2.put("__EVENTARGUMENT", "");
                    formData2.put("__VIEWSTATE", __VIEWSTATE2);
                    formData2.put("__VIEWSTATEGENERATOR", "7042C162");
                    formData2.put("__EVENTVALIDATION", __EVENTVALIDATION2);
                    formData2.put("ctl00$cLogIn1$tb_cLogIn_User", "");
                    formData2.put("ctl00$cLogIn1$tb_cLogIn_Pass", "");
                    formData2.put("ctl00$MainContent$Studentid", maSinhVien);
                    formData2.put("ctl00$MainContent$btFind", "");
                    formData2.put("ctl00$MainContent$gvStudentRegister$DXSelInput", "");
                    formData2.put("ctl00$MainContent$gvStudentRegister$DXKVInput", "[]");
                    formData2.put("ctl00$MainContent$gvStudentRegister$CallbackState", CallBackState);
                    formData2.put("ctl00_MainContent_ASPxScheduler1_viewNavigatorBlock_ctl00_gotodateCalendar_FNPWS", "0:0:-1:-10000:-10000:0:0px:-10000:1:0:0:0");
                    formData2.put("ctl00$MainContent$ASPxScheduler1$viewNavigatorBlock$ctl00$gotodateCalendar", "03/03/2017");
                    formData2.put("ctl00$MainContent$ASPxScheduler1$stateBlock$CBS", CBS);
                    formData2.put("ctl00$MainContent$ASPxScheduler1$stateBlock$SEL", "1488531600000,1800000,1488531600000,1800000,null");
                    formData2.put("ctl00$MainContent$ASPxScheduler1$stateBlock$FVR", "0");
                    formData2.put("ctl00$MainContent$ASPxScheduler1$stateBlock$APTSEL", "");
                    formData2.put("ctl00$MainContent$ASPxScheduler1$stateBlock$FRMTYPE", "None");
                    formData2.put("ctl00$MainContent$ASPxScheduler1$stateBlock$EDTAPT", "");
                    formData2.put("ctl00$MainContent$ASPxScheduler1$stateBlock$SCROLL", "");
                    formData2.put("ctl00$MainContent$ASPxScheduler1$stateBlock$CHECKSUMS", "");
                    formData2.put("ctl00$MainContent$ASPxScheduler1$stateBlock$REMINDERS", "");
                    formData2.put("ctl00$MainContent$ASPxScheduler1$stateBlock$VDAYS", stateBlockDay);
                    formData2.put("ctl00$MainContent$ASPxScheduler1$stateBlock$TRTD", "0,-1");
                    formData2.put("ctl00$MainContent$ASPxScheduler1$stateBlock$TRTW", "0,-1");
                    formData2.put("ctl00$MainContent$ASPxScheduler1$stateBlock$WRKD", "WD:62");
                    formData2.put("ctl00_MainContent_ASPxScheduler1_aptMenuBlock_SMAPTCI", "");
                    formData2.put("ctl00_MainContent_ASPxScheduler1_viewMenuBlock_SMVIEWCI", "");
                    formData2.put("DXScript", "1_145,1_81,1_137,1_99,1_106,1_92,1_130,1_135,1_121,1_105,1_91,1_114,1_133,1_85,1_83,8_10,8_17,8_24,8_26,8_9,8_12,8_13,8_18,8_21,1_93,1_78,1_128,8_23,8_22,8_16,8_19,1_129,8_20,1_112,8_14");
                    Connection.Response tkbPage = Jsoup.connect(Config.TKB_URL)
                            .cookies(cookies2)
                            .data(formData2)
                            .method(Connection.Method.POST)
                            .userAgent(Config.USER_AGENT)
                            .execute();
                    //Parse Lich học--------------------------------------------------------------------
                    Document tkbDocument = tkbPage.parse();
                    Element tkbTable = tkbDocument.getElementById("MainContent_gvStudentRegister_DXMainTable");
                    Elements monHoc = tkbTable.select("tr[class=dxgvDataRow_SisTheme]");
                    JSONArray tkbJsonA = new JSONArray();
                    for (Element element : monHoc) {
                        String thoiGian = element.select("td[class=dxgv]").get(0).text();
                        String tuanHoc = element.select("td[class=dxgv]").get(1).text();
                        String phongHoc = element.select("td[class=dxgv").get(2).text();
                        String maLop = element.select("td[class=dxgv").get(3).text();
                        String loaiLop = element.select("td[class=dxgv").get(4).text();
                        String nhom = element.select("td[class=dxgv").get(5).text();
                        String maHocPhan = element.select("td[class=dxgv").get(6).text();
                        String tenLop = element.select("td[class=dxgv]").get(7).text();
                        String ghiChu = element.select("td[class=dxgv]").get(8).text();

                        JSONObject tkbMonHocJsonO = new JSONObject();
                        tkbMonHocJsonO.put("thoiGian", thoiGian);
                        tkbMonHocJsonO.put("tuanHoc", tuanHoc);
                        tkbMonHocJsonO.put("phongHoc", phongHoc);
                        tkbMonHocJsonO.put("maLop", maLop);
                        tkbMonHocJsonO.put("loaiLop", loaiLop);
                        tkbMonHocJsonO.put("nhom", nhom);
                        tkbMonHocJsonO.put("maHocPhan", maHocPhan);
                        tkbMonHocJsonO.put("tenLop", tenLop);
                        tkbMonHocJsonO.put("ghiChu", ghiChu);

                        tkbJsonA.put(tkbMonHocJsonO);
                    }
                    //put to sis data
                    sisJsonO.put("lichHoc", tkbJsonA);
                }
                //Ghi lai mon du kien cai thien cũ==================================================
                JSONArray caiThienJsonA = dataJsonO.getJSONArray("caiThien");
                sisJsonO.put("caiThien", caiThienJsonA);

                //Save all to 1 file have name is sis.txt===========================================
                //==================================================================================
                String savedata = sisJsonO.toString();
                FileOutputStream fOS = openFileOutput("sis.txt", MODE_PRIVATE);
                OutputStreamWriter oSW = new OutputStreamWriter(fOS);
                oSW.write(savedata);
                oSW.flush();
                oSW.close();

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            readData();
            hienThiThongTinSinhVien();
            hienThiBarChart();
            hienThiThongTinTongQuan();
            hienThiLineChartCPA();
            Toast.makeText(TongQuan_Activity.this, "Xong rồi đó thím :)", Toast.LENGTH_SHORT).show();
            try {
                JSONObject dataJsonO = new JSONObject(data);
                JSONObject bangDiemCaNhan = dataJsonO.getJSONObject("bangDiemCaNhan");
                JSONArray bangDiemHocPhan = bangDiemCaNhan.getJSONArray("diemHocPhan");
                soMonMoi = bangDiemHocPhan.length();
                if (soMonCu < soMonMoi) {
                    final AlertDialog dialog = new AlertDialog.Builder(TongQuan_Activity.this)
                            .setTitle("Đã có điểm môn mới")
                            .setView(R.layout.dialog_thongbaocodiemmonmoi)
                            .create();
                    dialog.show();
                    Button btnBoQua = (Button) dialog.findViewById(R.id.btnBoQua_dilaogThongBao);
                    Button btnXem = (Button) dialog.findViewById(R.id.btnXem_dialogThongBao);
                    btnBoQua.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    btnXem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(TongQuan_Activity.this, BangDiem_Activity.class);
                            startActivity(intent);
                        }
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        }
    }
}
