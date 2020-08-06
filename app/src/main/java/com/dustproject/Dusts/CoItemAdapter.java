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

public class CoItemAdapter extends RecyclerView.Adapter<CoItemAdapter.CustomViewHolder> {
    private ArrayList<DustData> coList = null;
    private Activity context = null;
    public CoItemAdapter(Activity context, ArrayList<DustData> list) {
        this.context = context;
        this.coList = list;
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView co;

        public CustomViewHolder(View view) {
            super(view);
            this.co = (TextView) view.findViewById(R.id.co);
        }
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_co, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(CustomViewHolder viewHolder, int position) {
        viewHolder.co.setText(String.valueOf(coList.get(position).getCo()));
    }
    @Override
    public int getItemCount() {
        return (null != coList ? coList.size() : 0);
    }
}