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

public class DustMinItemAdapter extends RecyclerView.Adapter<DustMinItemAdapter.CustomViewHolder> {
    private ArrayList<DustData> dustminList = null;
    private Activity context = null;
    public DustMinItemAdapter(Activity context, ArrayList<DustData> list) {
        this.context = context;
        this.dustminList = list;
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView dustmin;

        public CustomViewHolder(View view) {
            super(view);
            this.dustmin = (TextView) view.findViewById(R.id.dustmin);
        }
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_dustmin, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(CustomViewHolder viewHolder, int position) {
        viewHolder.dustmin.setText(String.valueOf(dustminList.get(position).getDustmin()));
    }
    @Override
    public int getItemCount() {
        return (null != dustminList ? dustminList.size() : 0);
    }
}