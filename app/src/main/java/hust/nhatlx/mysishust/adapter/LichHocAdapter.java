package hust.nhatlx.mysishust.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import hust.nhatlx.mysishust.model.LichHocModel;
import hust.nhatlx.mysishust.R;

public class LichHocAdapter extends ArrayAdapter <LichHocModel> {
    private Activity context;
    private List<LichHocModel> objects;
    public LichHocAdapter(Activity context, int resource, List<LichHocModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(R.layout.item_lichhoc, null);
        TextView txtThoiGian_item = (TextView) row.findViewById(R.id.txtThoiGian_item);
        TextView txtTenLop_item = (TextView) row.findViewById(R.id.txtTenLop_item);
        TextView txtLoaiLop_item = (TextView) row.findViewById(R.id.txtLoaiLop_item);
        TextView txtPhongHoc_item = (TextView) row.findViewById(R.id.txtPhongHoc_item);

        LichHocModel lichHocModel = this.objects.get(position);
        txtThoiGian_item.setText(lichHocModel.getThoiGian());
        txtTenLop_item.setText(lichHocModel.getTenLop());
        txtLoaiLop_item.setText(lichHocModel.getLoaiLop());
        txtPhongHoc_item.setText(lichHocModel.getPhongHoc());
        
        return row;
    }
}
