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

        TextView name = (TextView)findViewById(R.id.name);
        TextView flower_flower = (TextView)findViewById(R.id.flower_flower);
        TextView imgUri = (TextView)findViewById(R.id.flower_img);
        TextView flower_content = (TextView)findViewById(R.id.flower_content);
        Intent intent = getIntent();

        String plant_name = intent.getExtras().getString("name_txt");
        String plant_flower = intent.getExtras().getString("flower_txt");
        String plant_img = intent.getExtras().getString("imgUri_txt");
        String plant_content = intent.getExtras().getString("content_txt");
        name.setText(plant_name);
        flower_flower.setText(plant_flower);
        imgUri.setText(plant_img);
        flower_content.setText(plant_content);
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
