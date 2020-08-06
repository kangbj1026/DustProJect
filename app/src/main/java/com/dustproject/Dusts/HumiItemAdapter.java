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

public class HumiItemAdapter extends RecyclerView.Adapter<HumiItemAdapter.CustomViewHolder> {
    private ArrayList<DustData> humiList = null;
    private Activity context = null;
    public HumiItemAdapter(Activity context, ArrayList<DustData> list) {
        this.context = context;
        this.humiList = list;
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView humi;

        public CustomViewHolder(View view) {
            super(view);
            this.humi = (TextView) view.findViewById(R.id.humi);
        }
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_humi, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(CustomViewHolder viewHolder, int position) {
        viewHolder.humi.setText(String.valueOf(humiList.get(position).getHumi()));
    }
    @Override
    public int getItemCount() {
        return (null != humiList ? humiList.size() : 0);
    }
}