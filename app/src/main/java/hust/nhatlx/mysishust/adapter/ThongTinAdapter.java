package hust.nhatlx.mysishust.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import hust.nhatlx.mysishust.model.ThongTinModel;
import hust.nhatlx.mysishust.R;

public class ThongTinAdapter extends ArrayAdapter<ThongTinModel> {
    private Activity context;
    private List<ThongTinModel> objects;
    public ThongTinAdapter(Activity context, int resource, List<ThongTinModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(R.layout.item_thongtin, null);
        TextView txtThongTin = (TextView) row.findViewById(R.id.txtThongTin_item);

        ThongTinModel thongTinModel = this.objects.get(position);
        txtThongTin.setText(thongTinModel.getThongTin());
        return row;
    }
}
