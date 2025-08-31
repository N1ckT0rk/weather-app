package com.example.weatherapp.adapters;

import android.util.Log;
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

        // Day
        holder.day.setText(forecastDay.getDayOfWeek());

        // Temps: max / min
        String tempText = Math.round(forecastDay.maxTemperature.degrees) + "° / "
                + Math.round(forecastDay.minTemperature.degrees) + "°";
        holder.temp.setText(tempText);

        // Rain: percent / amount
        String rainText = Math.round(forecastDay.daytimeForecast.precipitation.probability.percent) + "% / "
                + Math.round(forecastDay.daytimeForecast.precipitation.qpf.quantity) + "mm";
        holder.rain.setText(rainText);

        // Wind: speed / gust in mph
        double speedMph = forecastDay.daytimeForecast.wind.speed.value * 0.621371;
        double gustMph = forecastDay.daytimeForecast.wind.gust.value * 0.621371;
        String windText = Math.round(speedMph) + " / " + Math.round(gustMph);
        holder.wind.setText(windText);

        // Rotate arrow
        float rotation = (forecastDay.daytimeForecast.wind.direction.degrees + 180) % 360;
        holder.windDirection.setRotation(rotation);

        // Icon
        String iconUrl = forecastDay.daytimeForecast.weatherCondition.iconBaseUri + ".png";
        Glide.with(holder.imageCondition.getContext()).load(iconUrl).into(holder.imageCondition);
    }
    @Override
    public int getItemCount() {
        return dayList.size();
    }


    public static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView day, temp, rain, wind;
        ImageView imageCondition, windDirection;

        DayViewHolder(View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day);
            temp = itemView.findViewById(R.id.temp);
            rain = itemView.findViewById(R.id.rain);
            wind = itemView.findViewById(R.id.wind);
            imageCondition = itemView.findViewById(R.id.imageCondition);
            windDirection = itemView.findViewById(R.id.windDirection);
        }
    }
}
