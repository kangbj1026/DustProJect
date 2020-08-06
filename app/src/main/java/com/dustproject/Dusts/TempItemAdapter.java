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

public class TempItemAdapter extends RecyclerView.Adapter<TempItemAdapter.CustomViewHolder> {
    private ArrayList<DustData> tempList = null;
    private Activity context = null;
    public TempItemAdapter(Activity context, ArrayList<DustData> list) {
        this.context = context;
        this.tempList = list;
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView temp;

        public CustomViewHolder(View view) {
            super(view);
            this.temp = (TextView) view.findViewById(R.id.temp);
        }
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_temp, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(CustomViewHolder viewHolder, int position) {
        viewHolder.temp.setText(String.valueOf(tempList.get(position).getTemp()));
    }
    @Override
    public int getItemCount() {
        return (null != tempList ? tempList.size() : 0);
    }
}