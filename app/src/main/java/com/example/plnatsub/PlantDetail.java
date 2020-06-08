package com.example.plnatsub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlantDetail extends AppCompatActivity {

    private final String BASE_URL = "http://e437c9f55054.ngrok.io";

    private MyAPI mMyAPI;
    private final  String TAG = getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_plant);

        TextView detail = (TextView)findViewById(R.id.detail);
        Intent intent = getIntent();

        String plant_list = intent.getExtras().getString("detail_txt");
        detail.setText(plant_list);
        initMyAPI(BASE_URL);
    }

    public void initMyAPI(String baseUrl){

        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMyAPI = retrofit.create(MyAPI.class);
    }
}
