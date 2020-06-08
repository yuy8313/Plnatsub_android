package com.example.plnatsub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchResult extends AppCompatActivity {

    private TextView result1, result2, result1_percent, result2_percent;
    private MyAPI mMyAPI;

    private final  String TAG = getClass().getSimpleName();

    private final String BASE_URL = "http://e437c9f55054.ngrok.io";
    Button first_detail_btn,second_detail_btn;
    private String android_id;
    private String formatDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        result1 = (TextView)findViewById(R.id.result1);
        result1_percent = (TextView)findViewById(R.id.result1_percent);

        result2 = (TextView)findViewById(R.id.result2);
        result2_percent = (TextView)findViewById(R.id.result2_percent);

        first_detail_btn = (Button) findViewById(R.id.first_detail_btn);
        second_detail_btn = (Button) findViewById(R.id.second_detail_btn);

        Intent intent = getIntent(); //인텐트를 가져옴

        String plant_name_list1 = intent.getExtras().getString("frist_txt");
        result1.setText(plant_name_list1);
        String plant_percent_list1 = intent.getExtras().getString("first_percent_txt");
        result1_percent.setText(plant_percent_list1);

        String plant_name_list2 = intent.getExtras().getString("second_txt");
        result2.setText(plant_name_list2);
        String plant_percent_list2 = intent.getExtras().getString("second_percent_txt");
        result2_percent.setText(plant_percent_list2);

        final String one = plant_name_list1; // 첫번째 버튼에 해당
        final String two = plant_name_list2; // 두번째 버튼에 해당

        first_detail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getApplicationContext(), PlantDetail.class);

                Call<List<AccountItem>> plantconCall = mMyAPI.get_plant_con(one);
                plantconCall.enqueue(new Callback<List<AccountItem>>() {
                    @Override
                    public void onResponse(Call<List<AccountItem>> call, Response<List<AccountItem>> response) {
                        if(response.isSuccessful()){
                            List<AccountItem> versionList =response.body();
                            Log.d(TAG,response.body().toString());
                            String name_txt = "";
                            String flower_txt = "";
                            String imgUri_txt = "";
                            String content_txt = "";
                            for(AccountItem accountItem:versionList){
                                Log.d(TAG,"ㅅ"+one);
                                Log.d(TAG,"ㅎ"+accountItem.getName());

                                name_txt +=""+ accountItem.getName();
                                flower_txt +=" 꽃말: "+accountItem.getFlower();
                                imgUri_txt +=" 꽃 사진 ";
                                content_txt +=" 꽃 내용: "+accountItem.getContent();

                            }
                            // first_test.setText(name_txt);
                            //Log.d(TAG,"실화냐"+name_txt);
                            intent.putExtra("name_txt",name_txt);
                            intent.putExtra("flower_txt",flower_txt);
                            intent.putExtra("imgUri_txt",imgUri_txt);
                            intent.putExtra("content_txt",content_txt);
                            startActivity(intent);

                        }else{
                            int StatusCode =response.code();
                            Log.d(TAG,"dd아"+StatusCode);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AccountItem>> call, Throwable t) {
                        Log.d(TAG,"실패"+t.getMessage());
                    }
                });
//                            startActivity(intent);
            }
        });

        second_detail_btn.setOnClickListener(new View.OnClickListener() {  //버튼 클릭시 2등인 정확도 정보나옴
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getApplicationContext(), PlantDetail.class);

                Call<List<AccountItem>> plantconCall = mMyAPI.get_plant_con(two);
                plantconCall.enqueue(new Callback<List<AccountItem>>() {
                    @Override
                    public void onResponse(Call<List<AccountItem>> call, Response<List<AccountItem>> response) {
                        if(response.isSuccessful()){
                            List<AccountItem> versionList =response.body();
                            Log.d(TAG,response.body().toString());
                            String name_txt = "";
                            String flower_txt = "";
                            String imgUri_txt = "";
                            String content_txt = "";
                            for(AccountItem accountItem:versionList){
                                Log.d(TAG,"ㅅ"+two);
                                Log.d(TAG,"ㅎ"+accountItem.getName());
//
                                name_txt +=""+ accountItem.getName();
                                flower_txt +=" 꽃말: "+accountItem.getFlower();
                                imgUri_txt +=" 꽃 사진 ";
                                content_txt +=" 꽃 내용: "+accountItem.getContent();
//
                            }
                            intent.putExtra("name_txt",name_txt);
                            intent.putExtra("flower_txt",flower_txt);
                            intent.putExtra("imgUri_txt",imgUri_txt);
                            intent.putExtra("content_txt",content_txt);
                            startActivity(intent);
//                                intent.putExtra("detail_txt",detail2_txt);

                        }else{
                            int StatusCode =response.code();
                            Log.d(TAG,"dd아"+StatusCode);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AccountItem>> call, Throwable t) {

                    }
                });

//                            startActivity(intent);
            }
        });



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
