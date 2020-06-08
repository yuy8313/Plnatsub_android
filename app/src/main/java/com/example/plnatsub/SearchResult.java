package com.example.plnatsub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchResult extends AppCompatActivity {

    private TextView result1, result2, result1_percent, result2_percent;
    private MyAPI mMyAPI;

    private final  String TAG = getClass().getSimpleName();

    private final String BASE_URL = "http://e437c9f55054.ngrok.io";
    Button first_detail_btn,second_detail_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        result1 = (TextView)findViewById(R.id.result1);
        result1_percent = (TextView)findViewById(R.id.result1_percent);

        result2 = (TextView)findViewById(R.id.result2);
        result2_percent = (TextView)findViewById(R.id.result2_percent);

        Intent intent = getIntent(); //인텐트를 가져옴

        String plant_name_list1 = intent.getExtras().getString("frist_txt");
        result1.setText(plant_name_list1);
        String plant_percent_list1 = intent.getExtras().getString("first_percent_txt");
        result1_percent.setText(plant_percent_list1);

        String plant_name_list2 = intent.getExtras().getString("second_txt");
        result2.setText(plant_name_list2);
        String plant_percent_list2 = intent.getExtras().getString("second_percent_txt");
        result2_percent.setText(plant_percent_list2);

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
