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
import com.example.weatherapp.model.GoogleWeatherResponseDaily;

import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {
    private List<GoogleWeatherResponseDaily.ForecastDay> dayList;

    public DayAdapter(List<GoogleWeatherResponseDaily.ForecastDay> dayList) {
        this.dayList = dayList;
    }
    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_day, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        GoogleWeatherResponseDaily.ForecastDay forecastDay = dayList.get(position);
        holder.day.setText(forecastDay.getDayOfWeek());
        holder.maxTemp.setText(Math.round(forecastDay.maxTemperature.degrees) + "°");
        holder.minTemp.setText(Math.round(forecastDay.minTemperature.degrees) + "°");


        String iconUrl = forecastDay.daytimeForecast.weatherCondition.iconBaseUri + ".png";
        Glide.with(holder.imageCondition.getContext()).load(iconUrl).into(holder.imageCondition);
    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView maxTemp, minTemp, day;
        ImageView imageCondition;

        DayViewHolder(View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day);
            maxTemp = itemView.findViewById(R.id.maxTemp);
            minTemp = itemView.findViewById(R.id.minTemp);
            imageCondition = itemView.findViewById(R.id.imageCondition);
        }
}
}
