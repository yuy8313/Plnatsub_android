package com.example.plnatsub;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.telephony.mbms.FileInfo;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PlnatCarmer extends AppCompatActivity {
    private MyAPI mMyAPI;
    private final  String TAG = getClass().getSimpleName();
    // server의 url을 적어준다
    private final String BASE_URL = "http://e437c9f55054.ngrok.io";
    //    private final String BASE_URL = "http://127.0.0.1:5000/";
    Boolean album = false;
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    private Uri photoUri;
    Button btn_cature, btn_complete;
    ImageView image_result;
    String android_id, formatDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carmer);

        btn_cature = findViewById(R.id.btn_cature); //재촬영 선언
        btn_complete = findViewById(R.id.btn_complete); //완료버튼 선언
        image_result = findViewById(R.id.image_result); // 이미지뷰 선언

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy.MM.ddHH.mm.ss");
            formatDate = sdfNow.format(date);

            android_id = android.provider.Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

            try {
                photoFile = createImageFile();

            } catch (IOException e) {

            }
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }

        initMyAPI(BASE_URL);

        btn_cature.setOnClickListener(new View.OnClickListener() {   //재촬영
            @Override
            public void onClick(View v) {
//                getplant();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy.MM.ddHH.mm.ss");
                    formatDate = sdfNow.format(date);

                    android_id = android.provider.Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                    try {
                        photoFile = createImageFile();

                    } catch (IOException e) {

                    }

                    if (photoFile != null) {
                        photoUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);


                    }
                }
            }
        });

    }


    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES); // 보안에걸려서 못끄냄
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        imageFilePath = image.getAbsolutePath();

        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;
            galleryAddPic();
//            PostData();
//            getversion();
//            getaccount();
            ImageUpdate();


            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegress(exifOrientation);


            } else {
                exifDegree = 0;

            }
            Uri myUri = Uri.parse(imageFilePath);
            album =true;
            image_result.setImageBitmap(rotate(bitmap,exifDegree));//사진나옴

        }
    }

    private int exifOrientationToDegress(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    private void galleryAddPic(){
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imageFilePath);
        Uri uri = Uri.fromFile(f);
        intent.setData(uri);
        this.sendBroadcast(intent);
//        Log.i( "사진이 앨범에 저장되었습니다.",imageFilePath+"ㅇㅇ"+intent);
    }


    public void getplant(){
        Log.d(TAG,"안드"+android_id);
        Log.d(TAG,"시간"+formatDate);

        Call<List<AccountItem>> plantCall = mMyAPI.get_plant(""+android_id,""+formatDate);
        plantCall.enqueue(new Callback<List<AccountItem>>() {
            @Override
            public void onResponse(Call<List<AccountItem>> call, Response<List<AccountItem>> response) {
                if(response.isSuccessful()){
                    List<AccountItem> versionList =response.body();
                    Log.d(TAG,"뭐지"+response.body().toString());
                    String first_txt = "";
                    String first_percent_txt = "";
                    String second_txt = "";
                    String second_percent_txt = "";
                    for(AccountItem accountItem:versionList){
                        first_txt += accountItem.getFirst_name();
                        first_percent_txt += "확률: "+accountItem.getFirst_percent();
                        second_txt += accountItem.getSecond_name();
                        second_percent_txt += "확률:"+accountItem.getSecond_percent();
                    }
                    final Intent intent = new Intent(getApplicationContext(), SearchResult.class);

                    final String one = first_txt;
                    final String two = second_txt;
                    Log.d(TAG,"라라"+one);
//                    mListTv.setText(first_txt);
//                    result1_percent.setText(first_percent_txt);

                    intent.putExtra("frist_txt",first_txt);
                    intent.putExtra("first_percent_txt",first_percent_txt);

                    intent.putExtra("second_txt",second_txt);
                    intent.putExtra("second_percent_txt",second_percent_txt);

                    intent.putExtra("android_id", android_id);
                    intent.putExtra("formatDate", formatDate);

                    btn_complete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(intent);
                        }
                    });

                }else{
                    int StatusCode =response.code();
                    Log.d(TAG,"dd아"+StatusCode);
                }
            }

            @Override
            public void onFailure(Call<List<AccountItem>> call, Throwable t) {}

        });
    }



    public void ImageUpdate(){
        File file = new File(imageFilePath);

//        android_id = android.provider.Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Log.d("아나", android_id);
        Log.d("날짜",formatDate);
        Log.d(TAG, "Filename " + file.getName());
        Log.i( "사진이 앨범에 저장되었습니다.3121",file.getName());

        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part images = MultipartBody.Part.createFormData("images", file.getName(), mFile);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        mMyAPI = retrofit.create(MyAPI.class);

//        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/data"),file);
//        MultipartBody.Part multiPartBody = MultipartBody.Part
//                .createFormData("images", file.getName(),requestBody);

        Intent intent = new Intent(getApplicationContext(), Loading.class);
        startActivity(intent); //로딩화면 출력

        Call<AccountItem> call = mMyAPI.upload(images,""+android_id,""+formatDate);
        call.enqueue(new Callback<AccountItem>() {
            @Override
            public void onResponse(Call<AccountItem> call, Response<AccountItem> response) {
                Log.i("good", "good");

                getplant();

            }

            @Override
            public void onFailure(Call<AccountItem> call, Throwable t) {
                Log.i(TAG,"Fail msg : " + t.getMessage());

                getplant();

            }

        });


//        call.enqueue(new Callback<AccountItem>() {
//            @Override
//            public void onResponse(Call<AccountItem> call, Response<ResponseBody> response) {
//                Log.i("good", "good");
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.i(TAG,"Fail msg : " + t.getMessage());
//
//            }
//        });
    }

    private void initMyAPI(String baseUrl){

        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMyAPI = retrofit.create(MyAPI.class);
    }

}