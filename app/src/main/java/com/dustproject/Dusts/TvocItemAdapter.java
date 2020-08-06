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

public class TvocItemAdapter extends RecyclerView.Adapter<TvocItemAdapter.CustomViewHolder> {
    private ArrayList<DustData> tvocList = null;
    private Activity context = null;
    public TvocItemAdapter(Activity context, ArrayList<DustData> list) {
        this.context = context;
        this.tvocList = list;
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvoc;

        public CustomViewHolder(View view) {
            super(view);
            this.tvoc = (TextView) view.findViewById(R.id.tvoc);
        }
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tvoc, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(CustomViewHolder viewHolder, int position) {
        viewHolder.tvoc.setText(String.valueOf(tvocList.get(position).getTvoc()));
    }
    @Override
    public int getItemCount() {
        return (null != tvocList ? tvocList.size() : 0);
    }
}