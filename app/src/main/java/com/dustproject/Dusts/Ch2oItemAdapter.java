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

public class Ch2oItemAdapter extends RecyclerView.Adapter<Ch2oItemAdapter.CustomViewHolder> {
    private ArrayList<DustData> ch2oList = null;
    private Activity context = null;
    public Ch2oItemAdapter(Activity context, ArrayList<DustData> list) {
        this.context = context;
        this.ch2oList = list;
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView ch2o;

        public CustomViewHolder(View view) {
            super(view);
            this.ch2o = (TextView) view.findViewById(R.id.ch2o);
        }
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ch2o, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(CustomViewHolder viewHolder, int position) {
        viewHolder.ch2o.setText(String.valueOf(ch2oList.get(position).getCh2o()));
    }
    @Override
    public int getItemCount() {
        return (null != ch2oList ? ch2oList.size() : 0);
    }
}