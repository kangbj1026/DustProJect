package com.dustproject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DustItemAdapter extends RecyclerView.Adapter<DustItemAdapter.CustomViewHolder> {
    private ArrayList<DustData> dustList = null;
    private Activity context = null;
    public DustItemAdapter(Activity context, ArrayList<DustData> list) {
        this.context = context;
        this.dustList = list;
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView dust;

        public CustomViewHolder(View view) {
            super(view);
            this.dust = (TextView) view.findViewById(R.id.dust);
        }
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_dust, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(CustomViewHolder viewHolder, int position) {
        viewHolder.dust.setText(String.valueOf(dustList.get(position).getDustitem()));
    }
    @Override
    public int getItemCount() {
        return (null != dustList ? dustList.size() : 0);
    }
}