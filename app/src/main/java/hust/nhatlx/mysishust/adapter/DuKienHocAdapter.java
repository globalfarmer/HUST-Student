package hust.nhatlx.mysishust.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import hust.nhatlx.mysishust.model.HocPhanModel;
import hust.nhatlx.mysishust.R;

public class DuKienHocAdapter extends ArrayAdapter<HocPhanModel> {
    private Activity context;
    private int resource;
    private List<HocPhanModel> objects;
    public DuKienHocAdapter(Activity context, int resource, List<HocPhanModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource,null);
        TextView txtMaHocPhan = (TextView) row.findViewById(R.id.txtMaHocPhan_DuKien_item);
        TextView txtTenHocPhan = (TextView) row.findViewById(R.id.txtTenHocPhan_DuKien_item);
        TextView txtSoTinChi = (TextView) row.findViewById(R.id.txtSoTinChi_DuKien_item);
        TextView txtDiemCaiThien = (TextView) row.findViewById(R.id.txtDiemChu_DuKien_item);

        HocPhanModel diemHocPhanModel = this.objects.get(position);
        txtMaHocPhan.setText(diemHocPhanModel.getMaHocPhan());
        txtTenHocPhan.setText(diemHocPhanModel.getTenHocPhan());
        txtSoTinChi.setText(diemHocPhanModel.getSoTinChi());
        txtDiemCaiThien.setText(diemHocPhanModel.getDiemChu());
        return row;
    }

}
