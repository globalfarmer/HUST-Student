package hust.nhatlx.mysishust.model;



public class HocPhanModel {
    private String maHocPhan, tenHocPhan, soTinChi, diemChu, diemQuaTrinh, diemCuoiKi;
    private Double diemSo;

    public HocPhanModel() {

    }

    public String getDiemQuaTrinh() {
        return diemQuaTrinh;
    }

    public void setDiemQuaTrinh(String diemQuaTrinh) {
        this.diemQuaTrinh = diemQuaTrinh;
    }

    public String getDiemCuoiKi() {
        return diemCuoiKi;
    }

    public void setDiemCuoiKi(String diemCuoiKi) {
        this.diemCuoiKi = diemCuoiKi;
    }

    public String getMaHocPhan() {
        return maHocPhan;
    }

    public void setMaHocPhan(String maHocPhan) {
        this.maHocPhan = maHocPhan;
    }

    public String getTenHocPhan() {
        return tenHocPhan;
    }

    public void setTenHocPhan(String tenHocPhan) {
        this.tenHocPhan = tenHocPhan;
    }

    public String getSoTinChi() {
        return soTinChi;
    }

    public void setSoTinChi(String soTinChi) {
        this.soTinChi = soTinChi;
    }

    public String getDiemChu() {
        return diemChu;
    }

    public void setDiemChu(String diemChu) {
        this.diemChu = diemChu;
    }

    public Double getDiemSo() {
        return diemSo;
    }

    public void setDiemSo(Double diemSo) {
        this.diemSo = diemSo;
    }

    @Override
    public String toString() {
        return maHocPhan + tenHocPhan + soTinChi + diemChu + diemSo + diemQuaTrinh + diemCuoiKi;
    }
}
