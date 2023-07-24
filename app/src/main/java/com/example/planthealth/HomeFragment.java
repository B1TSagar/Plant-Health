package com.example.planthealth;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.planthealth.ml.Plant;

import org.tensorflow.lite.support.image.TensorImage;

import java.io.IOException;


public class HomeFragment extends Fragment {


    Button camera, gallery;
    ImageView imageView;
    TextView result;

    public interface OnFragmentClickListener {
        void onFragmentClick();
        void onFragmentClick2();
    }
    private OnFragmentClickListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnFragmentClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnFragmentClickListener");
        }
    }

    private void notifyClickListener() {
        if (listener != null) {
            listener.onFragmentClick();
        }
    }

    private void notifyClickListener2(){
        if (listener!= null){
            listener.onFragmentClick2();
        }
    }

    public class MyFragment extends Fragment {

        // Other code in your fragment

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_home, container, false);



            return view;
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_home, container, false);

//        Intent intent= new Intent(getActivity(), MainActivity.class);
//        startActivity(intent);

        camera = v.findViewById(R.id.button);
        gallery = v.findViewById(R.id.button2);
        result = v.findViewById(R.id.result);
        imageView = v.findViewById(R.id.imageView);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Notify the MainActivity about the click event
                camera.setVisibility(View.GONE);
                gallery.setVisibility(View.GONE);
                notifyClickListener();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Notify the MainActivity about the click event
                camera.setVisibility(View.GONE);
                gallery.setVisibility(View.GONE);
                notifyClickListener2();
            }
        });

//        camera.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
//                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    getActivity().startActivityForResult(cameraIntent, 3);
//                }
//                else{
//                    requestPermissions(new String[]{Manifest.permission.CAMERA},108);
//                }
//            }
//
//        });
//        gallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                getActivity().startActivityForResult(cameraIntent, 1);
//            }
//        });



        return  v;
    }

//    public void classifyImage(Bitmap image){
//        try {
//            Plant model = Plant.newInstance(getActivity().getApplicationContext());
//            // Creates inputs for reference.
//            TensorImage image1 = TensorImage.fromBitmap(image);
//
//            // Runs model inference and gets result.
//            Plant.Outputs outputs = model.process(image1);
//            Plant.DetectionResult detectionResult = outputs.getDetectionResultList().get(0);
//
//            // Gets result from DetectionResult.
//            float location = detectionResult.getScoreAsFloat();
//            RectF category = detectionResult.getLocationAsRectF();
//            String score = detectionResult.getCategoryAsString();
//            result.setText(location+"  "+score);
//            Toast.makeText(getActivity(), score, Toast.LENGTH_SHORT).show();
//
//            // Releases model resources if no longer used.
//            model.close();
//        } catch (IOException e) {
//            // TODO Handle the exception
//        }
//    }
////    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if(resultCode == RESULT_OK){
//            if(requestCode == 3){
//                Bitmap image = (Bitmap) data.getExtras().get("data");
//                int dimensions = Math.min(image.getWidth(), image.getHeight());
//                image = ThumbnailUtils.extractThumbnail(image, dimensions, dimensions);
//                imageView.setImageBitmap(image);
//                classifyImage(image);
//
//            }
//            else{
//                Uri dat = data.getData();
//                Bitmap image = null;
//                try {
//                    image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), dat);
//                } catch (IOException e){
//                    e.printStackTrace();
//                }
//                imageView.setImageBitmap(image);
//
//                classifyImage(image);
//
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
}