package com.example.plnatsub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlantDetail extends AppCompatActivity {

    private final String BASE_URL = "http://fc841842624b.ngrok.io";  //url주소

    private MyAPI mMyAPI;
    private final  String TAG = getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_plant);

        TextView name = (TextView)findViewById(R.id.name);
        TextView flower_flower = (TextView)findViewById(R.id.flower_flower);
        ImageView flower_img = (ImageView)findViewById(R.id.flower_img);
        TextView flower_content = (TextView)findViewById(R.id.flower_content);
        Intent intent = getIntent();

        String plant_name = intent.getExtras().getString("name_txt");
        String plant_flower = intent.getExtras().getString("flower_txt");
        String img_txt = intent.getExtras().getString("img_txt");
        String plant_content = intent.getExtras().getString("content_txt");

        name.setText(plant_name);
        Picasso.get().load(img_txt).into(flower_img);
        flower_flower.setText(plant_flower);
        flower_content.setText(plant_content);

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
