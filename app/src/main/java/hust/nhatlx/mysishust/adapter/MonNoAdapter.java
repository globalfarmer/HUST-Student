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

public class MonNoAdapter extends ArrayAdapter<HocPhanModel> {
    private Activity context;
    private List<HocPhanModel> objects;
    public MonNoAdapter(Activity context, int resource, List<HocPhanModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(R.layout.item_monno, null);
        TextView txtMaHocPhan_item = (TextView) row.findViewById(R.id.txtMaHocPhan_DuKien_item);
        TextView txtTenHocPhan_item = (TextView) row.findViewById(R.id.txtTenHocPhan_DuKien_item);
        TextView txtSoTinChi_item = (TextView) row.findViewById(R.id.txtSoTinCHi_item);
        HocPhanModel monTachModel = this.objects.get(position);
        txtMaHocPhan_item.setText(monTachModel.getMaHocPhan());
        txtTenHocPhan_item.setText(monTachModel.getTenHocPhan());
        txtSoTinChi_item.setText(monTachModel.getSoTinChi());
        return row;
    }
}
