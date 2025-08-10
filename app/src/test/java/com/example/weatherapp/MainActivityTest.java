package com.example.weatherapp;

import android.widget.TextView;
import com.example.weatherapp.api.WeatherApi;
import com.example.weatherapp.model.WeatherResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    private MainActivity activity;
    private WeatherApi mockApi;
    private Call<WeatherResponse> mockCall;

    @Before
    public void setUp() {
        ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class).create().start();
        activity = controller.get();

        mockApi = mock(WeatherApi.class);
        mockCall = mock(Call.class);

        activity.setApi(mockApi); // Inject mock API
    }

    @Test
    @Config(sdk = 33)
    public void testFetchWeatherSuccessUpdatesUI() {
        // Create WeatherResponse and nested objects
        WeatherResponse response = new WeatherResponse();

        WeatherResponse.Location location = response.new Location();
        location.name = "Tynemouth";
        location.region = "Tyne & Wear";
        location.country = "UK";

        WeatherResponse.Current current = response.new Current();
        current.temp_c = 17.2;
        current.wind_mph = 10.5;
        current.gust_mph = 18.4;
        current.wind_dir = "NE";

        WeatherResponse.Current.Condition condition = current.new Condition();
        condition.text = "Sunny";
        condition.icon = "//cdn.weatherapi.com/weather/64x64/day/113.png";

        current.condition = condition;
        response.location = location;
        response.current = current;

        // Mock API behavior
        when(mockApi.getCurrentWeather(any(), any())).thenReturn(mockCall);
        doAnswer(invocation -> {
            Callback<WeatherResponse> callback = invocation.getArgument(0);
            callback.onResponse(mockCall, Response.success(response));
            return null;
        }).when(mockCall).enqueue(any());

        // Trigger fetch
        activity.fetchWeather("Tynemouth");

        // Validate UI updates
        assertEquals("Tynemouth, Tyne & Wear", ((TextView) activity.findViewById(R.id.locationName)).getText().toString());
        assertEquals("17.2°C", ((TextView) activity.findViewById(R.id.temperature)).getText().toString());
        assertEquals("Wind Speed: 11 mph", ((TextView) activity.findViewById(R.id.windSpeed)).getText().toString());
        assertEquals("Wind Gust: 18 mph", ((TextView) activity.findViewById(R.id.windGust)).getText().toString());
        assertEquals("Wind Direction: NE", ((TextView) activity.findViewById(R.id.windDirection)).getText().toString());
    }
}