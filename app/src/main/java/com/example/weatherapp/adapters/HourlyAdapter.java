package com.example.weatherapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weatherapp.MainActivity;
import com.example.weatherapp.R;
import com.example.weatherapp.model.WeatherResponse;

import java.util.List;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.HourViewHolder> {
    private List<WeatherResponse.Hour> hourList;

    public HourlyAdapter(List<WeatherResponse.Hour> hourList) {
        this.hourList = hourList;
    }

    @NonNull
    @Override
    public HourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hour, parent, false);
        return new HourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourViewHolder holder, int position) {
        WeatherResponse.Hour hour = hourList.get(position);

        // Display time and temp_c dynamically (last 5 chars of "YYYY-MM-DD HH:mm")
        holder.textTime.setText(hour.time.substring(hour.time.length() - 5));
        holder.textTemp.setText(Math.round(hour.temp_c) + "°");

        String iconUrl = "https:" + hour.condition.icon;
        Glide.with(holder.imageCondition.getContext()).load(iconUrl).into(holder.imageCondition);
    }

    @Override
    public int getItemCount() {
        return hourList.size();
    }

    public static class HourViewHolder extends RecyclerView.ViewHolder {
        TextView textTime, textTemp;
        ImageView imageCondition;

        HourViewHolder(View itemView) {
            super(itemView);
            textTime = itemView.findViewById(R.id.hourTime);
            textTemp = itemView.findViewById(R.id.hourTemp);
            imageCondition = itemView.findViewById(R.id.imageCondition);
        }
    }
}

