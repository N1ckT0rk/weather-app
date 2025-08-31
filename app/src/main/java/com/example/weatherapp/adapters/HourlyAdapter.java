package com.example.weatherapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weatherapp.R;
import com.example.weatherapp.model.GoogleWeatherResponseHourly;

import java.util.List;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.HourViewHolder> {
    private List<GoogleWeatherResponseHourly.ForecastHour> googleHourList;

    public HourlyAdapter(List<GoogleWeatherResponseHourly.ForecastHour> googleHourList) {
        this.googleHourList = googleHourList;
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
        GoogleWeatherResponseHourly.ForecastHour hour = googleHourList.get(position);

        holder.textTime.setText(hour.displayDateTime.hours + ":00");

        holder.textTemp.setText(Math.round(hour.temperature.degrees) + "°");

        String iconUrl = hour.weatherCondition.iconBaseUri + ".png";
        Glide.with(holder.imageCondition.getContext())
                .load(iconUrl)
                .into(holder.imageCondition);
    }

    @Override
    public int getItemCount() {
        return googleHourList.size();
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

