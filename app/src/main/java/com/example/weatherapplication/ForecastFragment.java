package com.example.weatherapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weatherapplication.Adapter.WeatherForecastAdapter;
import com.example.weatherapplication.OpenWeather.Model.WeatherForecastResult;
import com.example.weatherapplication.OpenWeather.OpenWeather;
import com.example.weatherapplication.OpenWeather.Retrofit.IOpenWeather;
import com.example.weatherapplication.OpenWeather.Retrofit.RetrofitClient;

/**
 * A simple {@link Fragment} subclass.
*/
public class ForecastFragment extends Fragment {

    CompositeDisposable compositeDisposable;
    IOpenWeather mService;

    TextView txt_cityname, txt_geo_coord;
    RecyclerView recycler_forecast;

    static ForecastFragment instance;

    public static ForecastFragment getInstance() {
        if(instance == null)
            instance = new ForecastFragment();
        return instance;
    }

    public ForecastFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeather.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_forecast, container, false);

        txt_cityname = (TextView)itemView.findViewById(R.id.txt_city_name);
        txt_geo_coord = (TextView)itemView.findViewById(R.id.txt_geo_coord);

        recycler_forecast = (RecyclerView)itemView.findViewById(R.id.recycler_forecast);
        recycler_forecast.setHasFixedSize(true);
        recycler_forecast.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));

        getForecastWeatherInfo();

        return itemView;
    }

    private void getForecastWeatherInfo() {
        compositeDisposable.add(mService.getForecastWeatherByLatLng(
                String.valueOf(OpenWeather.current_location.getLatitude()),
                String.valueOf(OpenWeather.current_location.getLongitude()),
                OpenWeather.APP_ID,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherForecastResult>() {
                    @Override
                    public void accept(WeatherForecastResult weatherForecastResult) throws Exception {
                        displayForecastWeather(weatherForecastResult);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("ERROR", ""+throwable.getMessage());
                    }
                })

        );
    }

    private void displayForecastWeather(WeatherForecastResult weatherForecastResult) {
        txt_cityname.setText(new StringBuilder(weatherForecastResult.city.name));
        txt_geo_coord.setText(new StringBuilder(weatherForecastResult.city.coord.toString()));

        WeatherForecastAdapter adapter = new WeatherForecastAdapter(getContext(), weatherForecastResult);
        recycler_forecast.setAdapter(adapter);
    }
}