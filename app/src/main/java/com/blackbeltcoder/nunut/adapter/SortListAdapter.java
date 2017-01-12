package com.blackbeltcoder.nunut.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackbeltcoder.nunut.R;
import com.blackbeltcoder.nunut.pojo.SortObject;

import java.util.List;

/**
 * Created by ainozenbook on 9/20/2016.
 */
public class SortListAdapter extends RecyclerView.Adapter<SortListAdapter.ViewHolder> {

    private List<SortObject> dataSet;
    private Context context;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(SortObject item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvLabel;
        public ImageView ivStatus;

        public ViewHolder(View v) {
            super(v);
            tvLabel = (TextView) v.findViewById(R.id.tvLabel);
            ivStatus = (ImageView) v.findViewById(R.id.ivStatus);
        }

        public void bind(final SortObject item, final OnItemClickListener listener) {
            tvLabel.setText(item.getLabel());

            if(item.getStatus().equals("Y"))
                ivStatus.setVisibility(View.VISIBLE);
            else
                ivStatus.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    public SortListAdapter(List<SortObject> dataSet, Context context, OnItemClickListener listener) {
        this.dataSet = dataSet;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sort_criteria, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(dataSet.get(position), listener);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return dataSet.get(arg0).getId();
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
