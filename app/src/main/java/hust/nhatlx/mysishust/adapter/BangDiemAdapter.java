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


public class BangDiemAdapter extends ArrayAdapter<HocPhanModel>{
    private Activity context;
    private int resource;
    private List<HocPhanModel> objects;
    public BangDiemAdapter(Activity context, int resource, List<HocPhanModel> objects) {
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
        TextView txtTenHocPhan = (TextView) row.findViewById(R.id.txtTenHocPhan_DuKien_item);
        TextView txtSoTinChi = (TextView) row.findViewById(R.id.txtSoTinChi_item);
        TextView txtDiemChu = (TextView) row.findViewById(R.id.txtDiemChu_DuKien_item);
        TextView txtDiemQuaTrinh = (TextView) row.findViewById(R.id.txtDiemQuaTrinh);
        TextView txtDiemCuoiKi = (TextView) row.findViewById(R.id.txtDiemCuoiKi);

        HocPhanModel diemHocPhanModel = this.objects.get(position);
        txtTenHocPhan.setText(diemHocPhanModel.getTenHocPhan());
        txtSoTinChi.setText(diemHocPhanModel.getSoTinChi());
        txtDiemChu.setText(diemHocPhanModel.getDiemChu());
        txtDiemQuaTrinh.setText(diemHocPhanModel.getDiemQuaTrinh());
        txtDiemCuoiKi.setText(diemHocPhanModel.getDiemCuoiKi());
        return row;
    }

}
