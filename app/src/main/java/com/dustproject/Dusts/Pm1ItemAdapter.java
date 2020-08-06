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

public class Pm1ItemAdapter extends RecyclerView.Adapter<Pm1ItemAdapter.CustomViewHolder> {
    private ArrayList<DustData> pm1List = null;
    private Activity context = null;
    public Pm1ItemAdapter(Activity context, ArrayList<DustData> list) {
        this.context = context;
        this.pm1List = list;
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView pm1;

        public CustomViewHolder(View view) {
            super(view);
            this.pm1 = (TextView) view.findViewById(R.id.pm1);
        }
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pm1, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(CustomViewHolder viewHolder, int position) {
        viewHolder.pm1.setText(String.valueOf(pm1List.get(position).getPm1()));
    }
    @Override
    public int getItemCount() {
        return (null != pm1List ? pm1List.size() : 0);
    }
}