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
import com.supertreasure.bean.Address;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/25.
 */
public class ProvinceAdapter extends BaseAdapter {

    private Context context;
    private List<Address.ListEntity> list = new ArrayList<>();

    public ProvinceAdapter(Context context){
        this.context = context;
    }

    public ProvinceAdapter(Context context, List<Address.ListEntity> list){
        this.list = list;
    }

    public void setList(List<Address.ListEntity> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(list.get(position).getProvinceID());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHoloder viewHoloder = null;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_address_list,null);
            AbViewUtil.scaleContentView((LinearLayout) convertView.findViewById(R.id.root));
            viewHoloder = new ViewHoloder();
            viewHoloder.textView = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(viewHoloder);
        }else {
            viewHoloder = (ViewHoloder) convertView.getTag();
        }

        viewHoloder.textView.setText(list.get(position).getProvince());
        return convertView;
    }

    public static class ViewHoloder{
        TextView textView;
    }
}
