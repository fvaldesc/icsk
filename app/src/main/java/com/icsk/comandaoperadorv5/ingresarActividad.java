package com.icsk.comandaoperadorv5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ingresarActividad extends AppCompatActivity {
    String codigoOperador;
    String codigoSistema;
    String codigoActividad;
    String nombreActividad;
    ImageView btnImgresarActividadSeleccionadaVolver;
    ImageView btnSeleccionarActividad;

    TextView txtSeleccionarActividad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_actividad);
        recuperarPreferencia();
        txtSeleccionarActividad = findViewById(R.id.txtSeleccionarActividad);
        txtSeleccionarActividad.setText(nombreActividad);
        btnSeleccionarActividad=findViewById(R.id.btnSeleccionarActividad);
        btnSeleccionarActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ingresarActividad.this, actividadActividadOperador.class);
                i.putExtra("codigoActividad", codigoActividad);
                //    startActivity(i);
                startActivityForResult(i, 4);
                finish();

            }
        });

        btnImgresarActividadSeleccionadaVolver=findViewById(R.id.btnIngresarActividadSeleccionVolver);
        btnImgresarActividadSeleccionadaVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent();


                setResult(-1, intent);
                finish();
            }
        });

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 4) {
            if(resultCode == RESULT_OK) {
                String nombreActividad = data.getStringExtra("nombreActividad");
                String codigoActividad = data.getStringExtra("codigoActividad");
                txtSeleccionarActividad.setText(nombreActividad);
            }
        }


    }
    private void guardarPreferencia(){
        SharedPreferences sharedPreferences= getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();

        editor.putString("codigoActividadLaboral","IniciarJornada" );
        editor.putBoolean("session",true);
        editor.commit();
    }
    private void recuperarPreferencia(){
        SharedPreferences sharedPreferences= getSharedPreferences("login", Context.MODE_PRIVATE);
        codigoOperador= sharedPreferences.getString("usuario","");
        nombreActividad= sharedPreferences.getString("codigoActividadNombre","");
    }

}