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
import com.example.weatherapp.model.WeatherResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {
    private List<WeatherResponse.ForecastDay> dayList;

    public DayAdapter(List<WeatherResponse.ForecastDay> dayList) {
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
        WeatherResponse.ForecastDay forecastDay = dayList.get(position);

        // Display time and temp_c dynamically (last 5 chars of "YYYY-MM-DD HH:mm")
//        holder.textTime.setText(forecastDay.time.substring(forecastDay.time.length() - 5));
//        holder.textTemp.setText(Math.round(forecastDay.temp_c) + "°");


        // Convert "yyyy-MM-dd" into "EEE" (Mon, Tue, etc.)
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("EEE", Locale.getDefault()); // "Mon"
            Date date = inputFormat.parse(forecastDay.date);
            String dayOfWeek = outputFormat.format(date);

            holder.day.setText(dayOfWeek);
        } catch (Exception e) {
            e.printStackTrace();
            holder.day.setText(forecastDay.date); // fallback
        }

        holder.maxTemp.setText(Math.round(forecastDay.day.maxtemp_c) + "°");
        holder.minTemp.setText(Math.round(forecastDay.day.mintemp_c) + "°");


        String iconUrl = "https:" + forecastDay.day.condition.icon;
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
