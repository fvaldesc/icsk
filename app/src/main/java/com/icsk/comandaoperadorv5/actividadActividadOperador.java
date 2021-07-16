package com.icsk.comandaoperadorv5;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class actividadActividadOperador extends AppCompatActivity {
    ContentValues val=new ContentValues();
    ListView lista;
    private ArrayList<CActividadesOperador> arrayList;
    //   String webServer="192.168.28.7";
    String webServer="54.232.154.141";
    String codOperador=" ";
    String nombreActividad   ;
    String codigoActividad  ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_actividad_operador);
        lista=(ListView)findViewById(R.id.actividadListaOperador);
        Intent i = getIntent();
        SharedPreferences sharedPreferences= getSharedPreferences("login", Context.MODE_PRIVATE);




        codOperador= sharedPreferences.getString("usuario","");

        buscarLasActividadesDelOperador(codOperador);
    }

    public class Adpatador extends ArrayAdapter<CActividadesOperador> {
        AppCompatActivity appCompatActivity;
        Adpatador(AppCompatActivity context) {
            super(context, R.layout.actividades_nombres, arrayList);
            appCompatActivity = context;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = appCompatActivity.getLayoutInflater();
            View item = inflater.inflate(R.layout.sistemas_nombres, null);
            TextView textView1 = item.findViewById(R.id.sistemaNombre);
            textView1.setText(arrayList.get(position).getCodigoNombre());
            return(item);
        }
    }
    private void  llevarListaOperador (JSONArray objArray ) throws JSONException {
        JSONObject obj = null;
        arrayList=new ArrayList<CActividadesOperador>();
        try {
            for(int i = 0; i < objArray.length(); i++)
            {
                obj = objArray.getJSONObject(i);
                String codigo=     obj.get("codigoActividad").toString();
                String nombre=     obj.get("codigoNombreActividad").toString();
                arrayList.add( new CActividadesOperador(codigo,nombre));
            }

            actividadActividadOperador.Adpatador adaptador = new actividadActividadOperador.Adpatador(this);

            lista.setAdapter(adaptador);
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                    SharedPreferences  sharedPreferences= getSharedPreferences("login",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor= sharedPreferences.edit();

                    editor.putString("codigoActividad", arrayList.get(i).getCodigo() );
                    editor.putString("codigoActividadNombre",arrayList.get(i).getCodigoNombre() );

                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), ingresarActividad.class);

                    startActivity(intent);
                    finish();



                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void buscarLasActividadesDelOperador (String  pOperador){
        String url = "http://"+webServer+"/android/buscarActividadesDelOperador.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray obj = null;
                            obj = new JSONArray(response);
                            llevarListaOperador(obj);
                        } catch (JSONException e) {
                            Toast.makeText(actividadActividadOperador.this,"1"+e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(actividadActividadOperador.this, "2"+error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map <String,String> parametros = new HashMap<String,String>();
                parametros.put("codigoOperador",pOperador );
                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestQueue requestQueues = Volley.newRequestQueue(this);
        requestQueues.add(stringRequest);
    }
}