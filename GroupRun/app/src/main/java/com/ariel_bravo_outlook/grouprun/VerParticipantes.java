package com.ariel_bravo_outlook.grouprun;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
public class VerParticipantes extends AppCompatActivity {
String usuarios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_participantes);
        usuarios=getIntent().getStringExtra("usuarios");




    }
}
