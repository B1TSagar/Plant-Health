package com.example.planthealth;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class ProfileFragment extends Fragment {

    Button prof_logout;
    TextView name,email,user;

//    String uid = getActivity().FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_profile, container, false);

         prof_logout= v.findViewById(R.id.logout);
        prof_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ProfileFragment.this.getActivity() , Login.class);
                startActivity(intent);
            }
        });

        name= v.findViewById(R.id.profName);
        email= v.findViewById(R.id.profEmail);
        user= v.findViewById(R.id.profUsername);
        showUserData();
        return v;
    }
    public  void showUserData(){
        Intent intent = getActivity().getIntent();

        String nameUser= intent.getStringExtra("name");
        String emailUser= intent.getStringExtra("email");
        String usernameUser= intent.getStringExtra("username");

        name.setText("Name: "+nameUser);
        email.setText("Email: "+emailUser);
        user.setText("Username: "+usernameUser);
    }
}