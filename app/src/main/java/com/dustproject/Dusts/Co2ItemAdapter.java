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

public class Co2ItemAdapter extends RecyclerView.Adapter<Co2ItemAdapter.CustomViewHolder> {
    private ArrayList<DustData> co2List = null;
    private Activity context = null;
    public Co2ItemAdapter(Activity context, ArrayList<DustData> list) {
        this.context = context;
        this.co2List = list;
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView co2;

        public CustomViewHolder(View view) {
            super(view);
            this.co2 = (TextView) view.findViewById(R.id.co2);
        }
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_co2, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(CustomViewHolder viewHolder, int position) {
        viewHolder.co2.setText(String.valueOf(co2List.get(position).getCo2()));
    }
    @Override
    public int getItemCount() {
        return (null != co2List ? co2List.size() : 0);
    }
}