package com.icsk.comandaoperadorv5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

public class presentacionActividad extends AppCompatActivity {

    ProgressBar progressBar;
    private final Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentacion_actividad);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        startTheTransitionAfterTheSplashScreen();
    }
    private void startTheTransitionAfterTheSplashScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                boolean session = sharedPreferences.getBoolean("session", false);

                if (session) {
                    if (sharedPreferences.getString("codigoActividadLaboral", "").toString().equals("IniciarTurno")   ||
                    sharedPreferences.getString("codigoActividadLaboral", "").equals("TerminarTurno")) {
                        Intent intent = new Intent(getApplicationContext(), actividadTurno.class);
                        startActivity(intent);
                        finish();
                    }
                    if (sharedPreferences.getString("codigoActividadLaboral", "").equals("IniciarJornada")  ||
                        sharedPreferences.getString("codigoActividadLaboral", "").equals("TerminarJornada")) {
                        Intent intent = new Intent(getApplicationContext(), actividadJornada.class);
                        startActivity(intent);
                        finish();
                    }

                }
                else {
                    Intent intent = new Intent(getApplicationContext(), ingresoUsuario.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 1000);
    }
}