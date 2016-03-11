package com.example.gio.sqllexicon;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private List<String> engList;
    private List<String> geoList;

    public CustomAdapter(List<String> engArray1, List<String> geoArray1) {
        engList = engArray1;
        geoList = geoArray1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.english.setText(engList.get(position));
        holder.georgian.setText(geoList.get(position));
    }

    @Override
    public int getItemCount() {
        return engList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView english;
        private TextView georgian;

        public ViewHolder(View view) {
            super(view);
            english = (TextView) view.findViewById(R.id.english);
            georgian = (TextView) view.findViewById(R.id.georgian);
        }

    }
}
