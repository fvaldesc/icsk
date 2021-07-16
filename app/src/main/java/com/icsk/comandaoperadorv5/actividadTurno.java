package com.icsk.comandaoperadorv5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class actividadTurno extends AppCompatActivity {
    String codigoUsuario;
    TextView nombreUsuario;
    ImageView btnTurno;
    String webServer="54.232.154.141";
    //   String webServer="192.168.28.7";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_turno);
        nombreUsuario = findViewById(R.id.txtNombreUsuario);
        String url = "http://"+webServer+"/android/buscarNombreUsuario.php";

        btnTurno = findViewById(R.id.btnInicioTurno);
        btnTurno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                guardarPreferencia();


                Intent intent = new Intent(getApplicationContext(), actividadJornada.class);
                guardarPreferencia();

                startActivity(intent);

            }
        });

        recuperarPreferencia();
        obtieneNombreUsuario(url);
    }
    private void guardarPreferencia(){
        SharedPreferences  sharedPreferences= getSharedPreferences("login",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();

        editor.putString("codigoActividadLaboral","IniciarJornada" );
        editor.putBoolean("session",true);
        editor.commit();
    }

    private void obtieneNombreUsuario(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.isEmpty()){
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(response);
                                nombreUsuario.setText(obj.get("nombre").toString());

                            } catch (JSONException e) {
                                Toast.makeText(actividadTurno.this, e.toString(),Toast.LENGTH_LONG).show();
                            }

                        }
                        else
                            Toast.makeText(actividadTurno.this, "usuario no valido",Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override

                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(actividadTurno.this, error.toString(), Toast.LENGTH_LONG).show();
                    }

                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map <String,String> parametros = new HashMap<String,String>();
                parametros.put("usuario",codigoUsuario);

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void recuperarPreferencia(){
        SharedPreferences sharedPreferences= getSharedPreferences("login", Context.MODE_PRIVATE);
        codigoUsuario= sharedPreferences.getString("usuario","");

    }
}