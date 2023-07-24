package com.example.planthealth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.planthealth.ml.Plant;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.tensorflow.lite.support.image.TensorImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity  implements  HomeFragment.OnFragmentClickListener {

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    SettingFragment settingFragment = new SettingFragment();

    Button share;
    ImageView imageView;
    TextView result;
    int imageSize = 1080;
    BitmapDrawable bitmapDrawable;
    Bitmap bitmap;

//    BitmapDrawable bitmapDrawable= (BitmapDrawable) imageView.getDrawable();

    @Override
    public void onFragmentClick() {
//        Checks if the Take Picture Button is clicked on HomeFragment
        if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 3);
        }
        else{
            requestPermissions(new String[]{Manifest.permission.CAMERA},108);
        }
        Toast.makeText(this, "Camera Opened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFragmentClick2(){
        //        Checks if the Launch Gallery Button is clicked on HomeFragment
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(cameraIntent, 1);
        Toast.makeText(this, "Gallery Opened", Toast.LENGTH_SHORT).show();
    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        share = findViewById(R.id.share);
        share.setVisibility(View.GONE);
//        camera = findViewById(R.id.button);
//        gallery = findViewById(R.id.button2);
        result = findViewById(R.id.result);
         imageView = findViewById(R.id.imageView);

         getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

         bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
             @SuppressLint("NonConstantResourceId")
             @Override
             public boolean onNavigationItemSelected( MenuItem item) {

                 if (item.getItemId() == R.id.navhome){

                     getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                     share.setVisibility(View.GONE);
                     return  true;

                 }
                 else if (item.getItemId() == R.id.navsettings) {
                     if(imageView!= null && imageView.getVisibility()==View.VISIBLE) {
                         imageView.setImageDrawable(null);
                         result.setText("");
                     }
                     getSupportFragmentManager().beginTransaction().replace(R.id.container, settingFragment).commit();
                     share.setVisibility(View.GONE);
                     return  true;
                 }
                 else if (item.getItemId() == R.id.navprofile) {
                     if(imageView!= null && imageView.getVisibility()==View.VISIBLE) {
                         imageView.setImageDrawable(null);
                         result.setText("");
                     }
                     getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                     share.setVisibility(View.GONE);
                     return  true;
                 }
                 else {
                     return false;
                 }
             }
         });

         share.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
//                 BitmapDrawable bitmapDrawable= (BitmapDrawable) imageView.getDrawable();

                 shareImageAndText(bitmap);

             }
         });

    }
    public void classifyImage(Bitmap image){
        try {
            Plant model = Plant.newInstance(getApplicationContext());
            // Creates inputs for reference.
            TensorImage image1 = TensorImage.fromBitmap(image);

            // Runs model inference and gets result.
            Plant.Outputs outputs = model.process(image1);
            Plant.DetectionResult detectionResult = outputs.getDetectionResultList().get(0);

            // Gets result from DetectionResult.
            float location = detectionResult.getScoreAsFloat();
            RectF category = detectionResult.getLocationAsRectF();
            String score = detectionResult.getCategoryAsString();
            if(score.equals("healthy"))
            {
                result.setTextColor(Color.GREEN);
            }
            else {
                result.setTextColor(Color.RED);
                score="unhealthy";
            }
            result.setGravity(Gravity.CENTER);
            result.setText("The Plant is "+score);


            Toast.makeText(this, score, Toast.LENGTH_SHORT).show();

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            assert data != null;
            if(requestCode == 3){
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimensions = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimensions, dimensions);
                imageView.setImageBitmap(image);
                bitmapDrawable= (BitmapDrawable) imageView.getDrawable();
                bitmap =bitmapDrawable.getBitmap();
                classifyImage(image);
            }
            else{
                Uri dat = data.getData();
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                } catch (IOException e){
                    e.printStackTrace();
                }
                imageView.setImageBitmap(image);
                bitmapDrawable= (BitmapDrawable) imageView.getDrawable();
                bitmap =bitmapDrawable.getBitmap();
                classifyImage(image);

            }
            share.setVisibility(View.VISIBLE);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void shareImageAndText(Bitmap image){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        Uri uri;
        uri=saveImage(image,getApplicationContext());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        intent.putExtra(Intent.EXTRA_TEXT,"Can anyone share some details about this plant?");
        intent.putExtra(Intent.EXTRA_SUBJECT,"Curious about this plant");

        startActivity(Intent.createChooser(intent,"Share via"));

    }
//    private Uri getImageToShare(Bitmap image){
//        File folder = new File(getCacheDir(),"images");
//        Uri uri = null;
//        try {
//            folder.mkdirs();
//            File file = new File(folder, "image.jpg");
//            FileOutputStream fileOutputStream = new FileOutputStream(file);
//
//            image.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
//            fileOutputStream.flush();
//            fileOutputStream.close();
//
//            uri = FileProvider.getUriForFile(this,"com.example.planthealth",file);
//
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return uri;
//    }
    private static Uri saveImage(Bitmap image, Context context){
        File folder = new File(context.getCacheDir(),"images");
        Uri uri = null;
        try {
            folder.mkdirs();
            File file = new File(folder, "image.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            image.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            uri = FileProvider.getUriForFile(Objects.requireNonNull(context.getApplicationContext()),"com.example.planthealth"+".provider",file);

        } catch (Exception e){
            e.printStackTrace();
        }
        return uri;
    }
}