package com.example.weatherapp;

import com.example.weatherapp.api.WeatherApi;
import com.example.weatherapp.model.WeatherResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;

public class WeatherApiTest {

    private MockWebServer mockWebServer;
    private WeatherApi api;

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(WeatherApi.class);
    }

    @Test
    public void testGetForecast() throws Exception {
        String mockJson = "{\n" +
                "  \"location\": {\n" +
                "    \"name\": \"Tynemouth\",\n" +
                "    \"region\": \"Tyne and Wear\",\n" +
                "    \"country\": \"UK\"\n" +
                "  },\n" +
                "  \"current\": {\n" +
                "    \"temp_c\": 18.5,\n" +
                "    \"wind_mph\": 12.3,\n" +
                "    \"gust_mph\": 20.1,\n" +
                "    \"wind_dir\": \"NE\",\n" +
                "    \"condition\": {\n" +
                "      \"text\": \"Partly cloudy\",\n" +
                "      \"icon\": \"//cdn.weatherapi.com/weather/64x64/day/116.png\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockJson));

        WeatherResponse response = api.getForecast("fakeKey", "Tynemouth").execute().body();

        assertNotNull(response);
        assertEquals("Tynemouth", response.location.name);
        assertEquals("NE", response.current.wind_dir);
        assertEquals(18.5, response.current.temp_c, 0.01);
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }
}
