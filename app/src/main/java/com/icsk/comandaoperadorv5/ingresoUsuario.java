package com.icsk.comandaoperadorv5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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


public class ingresoUsuario extends AppCompatActivity {

    EditText txtUsuario, txtPassword;
    Button btnLogin;
    String p ,u ;
    String codigoOperador;
    String codigoMaquinaria;
    String codigoTurno;
    String codigoActividadLaboral;
    String codigoActividadNombre;
    String codigoActividad;
    String nombre;
    Boolean usuarioChequeado;
    String webServer="54.232.154.141";
    //   String webServer="192.168.28.7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_usuario);
        txtUsuario = findViewById(R.id.edtUsuario);
        txtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        recuperarPreferencia();
        u  = txtUsuario.getText().toString();
        p  = txtPassword.getText().toString();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                u  = txtUsuario.getText().toString();
                p =txtPassword.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor= sharedPreferences.edit();
                boolean session = sharedPreferences.getBoolean("session", false);
                editor.putString("password",txtPassword.getText().toString() );

                editor.commit();
                if (!u .equals("") && !p.equals("") ) {
                    String URL = "http://"+webServer+"/android/validarUsuario.php";

                    validarUsuario(URL);

                    //        finish();
                } else {
                    Toast.makeText(ingresoUsuario.this, "DEBE LLENAR LOS CAMPOS OBLIGATORIOS", Toast.LENGTH_LONG).show();
                }


            }
        });

    }
    private void validarUsuario(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.isEmpty()){
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(response);
                                SharedPreferences sharedPreferences= getSharedPreferences("login", Context.MODE_PRIVATE);

                                String usuarioPassword=sharedPreferences.getString("password","");
                                if (usuarioPassword.toString().equals(obj.get("usuarioPassword").toString()) ) {
                                    codigoOperador = obj.get("usuario").toString();
                                    codigoMaquinaria = obj.get("sistemaActual").toString();
                                    codigoTurno = obj.get("turnoActual").toString();
                                    nombre = obj.get("nombre").toString();

                                    Toast.makeText(ingresoUsuario.this, "usuario  valido", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), actividadTurno.class);
                                    guardarPreferencia();

                                    startActivity(intent);

                                    finish();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(ingresoUsuario.this, e.toString(),Toast.LENGTH_LONG).show();
                            }

                        }
                        else
                            Toast.makeText(ingresoUsuario.this, "usuario no valido",Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override

                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(ingresoUsuario.this, error.toString(), Toast.LENGTH_LONG).show();
                    }

                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map <String,String> parametros = new HashMap<String,String>();
                parametros.put("usuario",u );
                parametros.put("password",p);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void recuperarPreferencia(){
        SharedPreferences sharedPreferences= getSharedPreferences("login", Context.MODE_PRIVATE);
        txtPassword.setText(sharedPreferences.getString("password",""));
        txtUsuario.setText(sharedPreferences.getString("usuario",""));
        codigoMaquinaria=(sharedPreferences.getString("codigoMaquinaria","347087"));
        codigoTurno=(sharedPreferences.getString("codigoTurno","turno1"));
        codigoActividadLaboral=(sharedPreferences.getString("codigoActividadLaboral","turno"));
        codigoActividad=(sharedPreferences.getString("codigoActividad","turno"));
        codigoActividadNombre=(sharedPreferences.getString("codigoActividadNombre","turno"));
        nombre=(sharedPreferences.getString("nombre","*********"));
    }
    private void guardarPreferencia(){
        SharedPreferences  sharedPreferences= getSharedPreferences("login",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("usuario",u );
        editor.putString("password",p );
        editor.putString("codigoMaquinaria",codigoMaquinaria );
        editor.putString("codidoTurno",codigoTurno );
        editor.putString("codigoActividadLaboral","IniciarTurno" );
        editor.putString("codigoActividad",codigoActividad );
        editor.putString("codigoActividadNombre",codigoActividadNombre );
        editor.putString("nombre",nombre );


        editor.putBoolean("session",true);
        editor.commit();
    }
}

