package com.example.planthealth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SettingFragment extends Fragment {

    EditText editName, editEmail, editPassword;
    Button saveButton;
    String nameUser, emailUser, usernameUser,passwordUser;
    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_setting, container, false);

        reference = FirebaseDatabase.getInstance().getReference("users");

        editName = v.findViewById(R.id.edit_name);
        editEmail = v.findViewById(R.id.edit_email);
        editPassword = v.findViewById(R.id.edit_password);
        saveButton = v.findViewById(R.id.edit_details);

        showData();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNameChanged() || isEmailChanged() || isPasswordChanged() ){
                    inif();
                }
                else{
                    inelse();
                }
            }
        });

        return v;
    }

    public boolean isNameChanged(){
        if(!nameUser.equals(editName.getText().toString())){
            reference.child(usernameUser).child("name").setValue(editName.getText().toString());
            nameUser = editName.getText().toString();
            return true;
        }
        else{
            return false;
        }
    }
    public boolean isEmailChanged(){
        if(!emailUser.equals(editEmail.getText().toString())){
            reference.child(usernameUser).child("email").setValue(editEmail.getText().toString());
            emailUser = editEmail.getText().toString();
            return true;
        }
        else{
            return false;
        }
    }
    public boolean isPasswordChanged(){

        if(!passwordUser.equals(editPassword.getText().toString())){
            reference.child(usernameUser).child("password").setValue(editPassword.getText().toString());
            passwordUser = editPassword.getText().toString();
            return true;
        }
        else{
            return false;
        }
    }


    public void showData(){
        Intent intent = requireActivity().getIntent();

        nameUser= intent.getStringExtra("name");
        emailUser= intent.getStringExtra("email");
        usernameUser= intent.getStringExtra("username");
        passwordUser= intent.getStringExtra("password");

        editName.setText(nameUser);
        editEmail.setText(emailUser);
        editPassword.setText(passwordUser);
    }
    private void inif(){
        Context context = requireActivity().getApplicationContext();
        Toast.makeText(context, "Changes Saved", Toast.LENGTH_SHORT).show();
    }
    private void inelse(){
        Context context = requireActivity().getApplicationContext();
        Toast.makeText(context, "No Changes Found", Toast.LENGTH_SHORT).show();
    }
}