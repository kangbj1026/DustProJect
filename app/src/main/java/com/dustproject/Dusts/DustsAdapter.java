package com.dustproject.Dusts;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.dustproject.DustData;
import com.dustproject.R;

import java.util.ArrayList;

public class DustsAdapter extends RecyclerView.Adapter<DustsAdapter.CustomViewHolder> {
    private ArrayList<DustData> dList = null;
    private Activity context = null;
    public DustsAdapter(Activity context, ArrayList<DustData> list) {
        this.context = context;
        this.dList = list;
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView humi;
        protected TextView temp;
        protected TextView dust;
        protected TextView dustmin;
        protected TextView tvoc;
        protected TextView co2;
        protected TextView co;
        protected TextView ch2o;
        protected TextView pm1;

        public CustomViewHolder(View view) {
            super(view);
            this.humi = (TextView) view.findViewById(R.id.humi);
            this.temp = (TextView) view.findViewById(R.id.temp);
            this.dust = (TextView) view.findViewById(R.id.dust);
            this.dustmin = (TextView) view.findViewById(R.id.dustmin);
            this.tvoc = (TextView) view.findViewById(R.id.tvoc);
            this.co2 = (TextView) view.findViewById(R.id.co2);
            this.co = (TextView) view.findViewById(R.id.co);
            this.ch2o = (TextView) view.findViewById(R.id.ch2o);
            this.pm1 = (TextView) view.findViewById(R.id.pm1);
        }
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder ;
    }
    @Override
    public void onBindViewHolder(CustomViewHolder viewHolder, int position) {
        viewHolder.humi.setText(String.valueOf(dList.get(position).getHumi()));
        viewHolder.temp.setText(String.valueOf(dList.get(position).getTemp()));
        viewHolder.dust.setText(String.valueOf(dList.get(position).getDust()));
        viewHolder.dustmin.setText(String.valueOf(dList.get(position).getDustmin()));
        viewHolder.tvoc.setText(String.valueOf(dList.get(position).getTvoc()));
        viewHolder.co2.setText(String.valueOf(dList.get(position).getCo2()));
        viewHolder.co.setText(String.valueOf(dList.get(position).getCo()));
        viewHolder.ch2o.setText(String.valueOf(dList.get(position).getCh2o()));
        viewHolder.pm1.setText(String.valueOf(dList.get(position).getPm1()));
    }
    @Override
    public int getItemCount() {
        return (null != dList ? dList.size() : 0);
    }
}