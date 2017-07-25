package hust.nhatlx.mysishust;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class Login_Activity extends AppCompatActivity {
    EditText edtMaSinhVien, edtMatKhau;
    Button btnDangNhap;
    String data;
    String maSinhVien, matKhau;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtMaSinhVien = (EditText) findViewById(R.id.edtMaSinhVien_Login);
        edtMatKhau = (EditText) findViewById(R.id.edtMatKhau_Login);
        btnDangNhap = (Button) findViewById(R.id.btnDangNhap_Login);

        readData();
        if (data != null) {
            Intent intent = new Intent(Login_Activity.this, TongQuan_Activity.class);
            startActivity(intent);
        }

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternet();
                checkEditTextnotNull();
                if (checkInternet() && checkEditTextnotNull()) {
                    maSinhVien = edtMaSinhVien.getText().toString();
                    matKhau = edtMatKhau.getText().toString();
                    HashMap<String, String> acount = new HashMap<>();
                    acount.put("maSinhVien", maSinhVien);
                    acount.put("passWord", matKhau);
                    //AsyncTask
                    ParseKetQuaHTMLTask parseHTMLTask = new ParseKetQuaHTMLTask();
                    parseHTMLTask.execute(acount);
                }
            }
        });
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

    private boolean checkInternet() {
        boolean checkInternet = true;
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo == null) {
            checkInternet = false;
            Toast.makeText(Login_Activity.this, "Hmmm, Internet đâu thím...", Toast.LENGTH_LONG).show();
        }
        return checkInternet;
    }

    private boolean checkEditTextnotNull() {
        boolean checkEditTextNotNull = true;
        String maSVCheck = edtMaSinhVien.getText().toString();
        String passWordCheck = edtMatKhau.getText().toString();
        if (maSVCheck.equals("") || passWordCheck.equals("")) {
            checkEditTextNotNull = false;
            Toast.makeText(Login_Activity.this, "Điền thông tin đi thím, hazz...", Toast.LENGTH_LONG).show();
        }
        return checkEditTextNotNull;
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

    private class ParseKetQuaHTMLTask extends AsyncTask<HashMap, Void, Boolean> {
        ProgressDialog dialog = new ProgressDialog(Login_Activity.this);
        JSONObject sisJsonO = new JSONObject(); //tat ca thong tin duoc ghi vao day
        boolean checkLogin = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Chờ xíu nha thím . . .");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(HashMap... params) {
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
                Document homPageDoc = homePage.parse();
                Element bangThongBaoElement = homPageDoc.getElementById("MainContent_ctl08_ICell");
                //bang thong bao element not null -> log in thanh cong
                if (bangThongBaoElement != null) {
                    checkLogin = true;
                    //Parse thong tin thong bao=========================================================
                    //==================================================================================
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
                    //Go to tra cuu hoc phi---------------------------------------------------------
                    Connection.Response traCuuHocPhi = Jsoup.connect("http://sis.hust.edu.vn/ModuleSearch/CheckTuition.aspx")
                            .cookies(cookies)
                            .method(Connection.Method.GET)
                            .userAgent(Config.USER_AGENT)
                            .execute();
                    if (traCuuHocPhi != null) {
                        //parse tra cuu hoc phi-----------------------------------------------------
                        Document traCuuHocPhiDoc = traCuuHocPhi.parse();
                        Element hocPhi = traCuuHocPhiDoc.getElementById("MainContent_rpEditTables_ASPxCallbackPanel1_TuitionInfo");
                        String hocKi = hocPhi.getElementsByTag("h3").text();
                        String muc1 = hocPhi.getElementsByTag("span").get(1).text();
                        String muc2 = hocPhi.getElementsByTag("span").get(2).text();
                        String tinChiTrongChuongTrinh = hocPhi.getElementsByTag("span").get(3).text();
                        String tinChiNgoaiChuongTrinh = hocPhi.getElementsByTag("span").get(4).text();
                        String tinChiDacBiet = hocPhi.getElementsByTag("span").get(5).text();
                        String phuPhi = hocPhi.getElementsByTag("span").get(6).text();
                        String tốngTienPhaiDong = hocPhi.getElementsByTag("span").get(7).text();
                        String ghiChu = hocPhi.getElementsByTag("span").get(8).text();

                        JSONObject hocPhiJsonO = new JSONObject();
                        hocPhiJsonO.put("hocKi", hocKi);
                        hocPhiJsonO.put("muc1", muc1);
                        hocPhiJsonO.put("muc2", muc2);
                        hocPhiJsonO.put("tinChiTrongChuongTrinh", tinChiTrongChuongTrinh);
                        hocPhiJsonO.put("tinChiNgoaiChuongTrinh", tinChiNgoaiChuongTrinh);
                        hocPhiJsonO.put("tinChiDacBiet", tinChiDacBiet);
                        hocPhiJsonO.put("phuPhi", phuPhi);
                        hocPhiJsonO.put("tongTienPhaiDong", tốngTienPhaiDong);
                        hocPhiJsonO.put("ghiChu", ghiChu);

                        sisJsonO.put("hocPhi", hocPhiJsonO);
                    }
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
                            jsonObject.put("diemQuaTrinh", diemQuaTrinh);
                            jsonObject.put("diemCuoiKi", diemCuoiKi);
                            jsonObject.put("diemChu", diemChu);
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
                                String diemChu = jsonObject1.getString("diemChu");
                                String diemQuaTrinh = jsonObject1.getString("diemQuaTrinh");
                                String diemCuoiKi = jsonObject1.getString("diemCuoiKi");
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
                                        diemHocPhanJsonO.put("diemQuaTrinh", diemQuaTrinh);
                                        diemHocPhanJsonO.put("diemCuoiKi", diemCuoiKi);
                                        diemHocPhanJsonO.put("diemChu", diemChu);
                                        diemHocPhanJsonO.put("diemSo", diemSo);
                                        diemHocPhanJsonASauLoc.put(diemHocPhanJsonO);
                                    }
                                } else {
                                    JSONObject diemHocPhanJsonO = new JSONObject();
                                    diemHocPhanJsonO.put("maHocPhan", maHocPhan);
                                    diemHocPhanJsonO.put("tenHocPhan", tenHocPhan);
                                    diemHocPhanJsonO.put("soTinChi", soTinChi);
                                    diemHocPhanJsonO.put("diemQuaTrinh", diemQuaTrinh);
                                    diemHocPhanJsonO.put("diemCuoiKi", diemCuoiKi);
                                    diemHocPhanJsonO.put("diemChu", diemChu);
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
                }
                JSONArray caiThien = new JSONArray();
                sisJsonO.put("caiThien", caiThien);

                if (sisJsonO != null) {
                    //Save all to 1 file have name is sis.txt===========================================
                    //==================================================================================
                    String savedata = sisJsonO.toString();
                    FileOutputStream fOS = openFileOutput("sis.txt", MODE_PRIVATE);
                    OutputStreamWriter oSW = new OutputStreamWriter(fOS);
                    oSW.write(savedata);
                    oSW.flush();
                    oSW.close();
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return checkLogin;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            if (aVoid) {
                Intent intent = new Intent(Login_Activity.this, TongQuan_Activity.class);
                startActivity(intent);
            } else {
                Toast.makeText(Login_Activity.this, "Tài khoản sai hoặc lỗi mạng rồi thím :'(", Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        }
    }
}
