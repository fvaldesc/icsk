package com.icsk.comandaoperadorv5;


import android.content.ContentValues;
import android.content.Intent;
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

public class actividadSistemas extends AppCompatActivity {
    ContentValues val=new ContentValues();
    ListView lista;
    private ArrayList<CSistemaOperador> arrayList;
    //   String webServer="192.168.28.7";
    String webServer="54.232.154.141";
    String codOperador="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_sistemas);
        lista=(ListView)findViewById(R.id.sistemaListaOperador);
        Intent i = getIntent();
        codOperador = i.getStringExtra("codOperador");
        buscarLosSistemaDelOperador(codOperador);
    }

    public class Adpatador extends ArrayAdapter<CSistemaOperador> {
        AppCompatActivity appCompatActivity;
        Adpatador(AppCompatActivity context) {
            super(context, R.layout.sistemas_nombres, arrayList);
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
        arrayList=new ArrayList<CSistemaOperador>();
        try {
            for(int i = 0; i < objArray.length(); i++)
            {
                obj = objArray.getJSONObject(i);
                String codigo=     obj.get("codigoSistema").toString();
                String nombre=     obj.get("codigoSistemaNombre").toString();
                arrayList.add( new CSistemaOperador(codigo,nombre));
            }
            actividadSistemas.Adpatador adaptador = new actividadSistemas.Adpatador(this);
            lista.setAdapter(adaptador);
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent();

                    intent.putExtra("codigoSistema", arrayList.get(i).getCodigo());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void buscarLosSistemaDelOperador (String  pOperador){
        String url = "http://"+webServer+"/android/buscarSistemasDelOperador.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray obj = null;
                            obj = new JSONArray(response);
                            llevarListaOperador(obj);
                        } catch (JSONException e) {
                            Toast.makeText(com.icsk.comandaoperadorv5.actividadSistemas.this,"1"+e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(com.icsk.comandaoperadorv5.actividadSistemas.this, "2"+error.toString(), Toast.LENGTH_LONG).show();
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