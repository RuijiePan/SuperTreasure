package com.supertreasure.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.util.AbViewUtil;
import com.supertreasure.R;

import java.util.List;

/**
 * Created by Administrator on 2016/2/25.
 */
public class CampusAdapter extends BaseAdapter {

    private Context context;
    private List<String> campusList;

    public CampusAdapter(Context context){
        this.context = context;
    }

    public CampusAdapter(Context context, List<String> campusList){
        this.context = context;
        this.campusList = campusList;
    }

    public void setList(List<String> campusList){
        this.campusList = campusList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return campusList==null?0:campusList.size();
    }

    @Override
    public Object getItem(int position) {
        return campusList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position+1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_address_list,null);
            AbViewUtil.scaleContentView((LinearLayout) convertView.findViewById(R.id.root));
            holder = new ViewHolder();
            holder.tv_campus = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_campus.setText(campusList.get(position));
        return convertView;
    }

    public static class ViewHolder{
        TextView tv_campus;
    }
}
