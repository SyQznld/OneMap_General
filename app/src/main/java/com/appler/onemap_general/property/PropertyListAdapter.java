package com.appler.onemap_general.property;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.appler.onemap_general.R;

import java.util.List;

/**
 *   点查 填充数据的适配器adapter
 */

public class PropertyListAdapter extends BaseAdapter {
    private Context context;
    private List<PropertyData> datas;

    public PropertyListAdapter(Context context, List<PropertyData> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            holder.tvname = (TextView) view.findViewById(R.id.tv_name);
            holder.tvParams = (TextView) view.findViewById(R.id.tv_params);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvname.setText(datas.get(i).getName());
        holder.tvParams.setText(datas.get(i).getParam());
        return view;
    }

    class ViewHolder {
        TextView tvname;
        TextView tvParams;
    }


}

