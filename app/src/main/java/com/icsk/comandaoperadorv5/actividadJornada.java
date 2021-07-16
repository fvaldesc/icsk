package com.icsk.comandaoperadorv5;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;





public class actividadJornada extends AppCompatActivity {
    String codigoUsuario;
    TextView nombreUsuario;
    String codigoMaquinaria;
    String codigoTurno;
    String codigoActividadNombre;
    String s ;
    private DatePickerDialog datePickerDialog;
    EditText txtJornada;
    EditText  fechaActual;
    EditText txtSistemaSeleccionado;
    EditText horaActual;
    ImageView btnSalidaDeTurno;
    ImageView btnIngresarActividad;
    ImageView btnEliminarActividad;
    ImageView btnIniciarJornada;
    ImageView  btnTerminarJornada;
    ImageView  btnSistemaSeleccion;
    ImageView  btnFecha;
    View viewActividades;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
//

    List items = new ArrayList();
    //
    String webServer="54.232.154.141";
    //   String webServer="192.168.28.7";
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_jornada);
        recuperarPreferencia();


        horaActual= findViewById(R.id.horaActual);
        fechaActual= findViewById(R.id.fechaActual);
        txtSistemaSeleccionado=findViewById(R.id.txtSistemaSeleccionado);
        btnSistemaSeleccion =findViewById(R.id.btnSistemaSeleccion);
        btnSistemaSeleccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(actividadJornada.this, actividadSistemas.class);
                i.putExtra("codOperador", codigoUsuario);
               //   startActivity(i);
               startActivityForResult(i, 2);

            }
        });
        btnFecha =findViewById(R.id.btnFecha);
        viewActividades =findViewById(R.id.viewActividades);

        txtJornada = findViewById(R.id.txtSalidaTurno);
        Date d = new Date();
        String timeZone = "GMT-4:00";
        Date local = new Date(d.getTime() + TimeZone.getTimeZone(timeZone).getOffset(d.getTime()));

        s  = (String) DateFormat.format("dd-MM-yyyy", local.getTime());
        CharSequence h  = DateFormat.format("HH:MM", local.getTime());
        fechaActual.setText(s.toString());
        horaActual.setText(h.toString());


        initDatePicker();

        btnFecha.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                datePickerDialog.show();
                s=getTodaysDate();
            }
        });

        btnIngresarActividad = findViewById(R.id.btnIngresarActividad);
        btnIngresarActividad.setOnClickListener(new View.OnClickListener()        {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext() ,ingresarActividad.class);
       //         i.putExtra("codOperador", codigoOperador);
                //    startActivity(i);
                startActivityForResult(i, 4);

            }
        });
        btnTerminarJornada = findViewById(R.id.btnTerminarJornada);
        btnTerminarJornada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                guardarPreferencia("TerminarJornada");
     //           activarBotones(true);
                if (btnTerminarJornada.getRotationY()==-180 ) {
                    btnTerminarJornada.setRotationY( 0);
                    txtJornada.setText("INICIO JORNADA");

                }
                else {
                    btnTerminarJornada.setRotationY(-180);
                    txtJornada.setText("TERMINO JORNADA");
                }


            }
        });




        btnSalidaDeTurno = findViewById(R.id.btnSalidaDeTurno);
        btnSalidaDeTurno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getApplicationContext(), actividadTurno.class);
                guardarPreferencia("TerminarTurno");
                startActivity(intent);

            }
        });

        // Inicializar Animes



// Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.viewActividades);
        recycler.setHasFixedSize(true);

// Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

// Crear un nuevo adaptador
        adapter = new IngresoActividadesAdapter(items);
        recycler.setAdapter(adapter);




    }

    public static class IngresoActividadesAdapter extends RecyclerView.Adapter<IngresoActividadesAdapter.IngresoDetalleViewHolder> {
        private List<CIngresoActividades> items;

        public static class IngresoDetalleViewHolder extends RecyclerView.ViewHolder {
            // Campos respectivos de un item
            public ImageView eliminarActividad;
            public TextView nombreActividad;
            public ImageView modificarActividad;

            public IngresoDetalleViewHolder(View v) {
                super(v);
                eliminarActividad = (ImageView) v.findViewById(R.id.btnEliminarActividad);
                nombreActividad = (TextView) v.findViewById(R.id.txtNombreActividad);
                modificarActividad = (ImageView) v.findViewById(R.id.btnModificarActividad);
            }
        }

        public IngresoActividadesAdapter(List<CIngresoActividades> items) {
            this.items = items;
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public IngresoDetalleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.ingresa_actividad, viewGroup, false);
            return new IngresoDetalleViewHolder(v);
        }

        @Override
        public void onBindViewHolder(IngresoDetalleViewHolder viewHolder, int i) {
            viewHolder.eliminarActividad.setImageResource(items.get(i).getEliminarActividad());
            viewHolder.modificarActividad.setImageResource(items.get(i).getModificarActividad());
            viewHolder.nombreActividad.setText(items.get(i).getNombreActividad());
        }
    }


    private void recuperarPreferencia(){
        SharedPreferences sharedPreferences= getSharedPreferences("login", Context.MODE_PRIVATE);

        codigoUsuario= sharedPreferences.getString("usuario","");
        codigoMaquinaria= sharedPreferences.getString("sistemaActual","");
        codigoTurno= sharedPreferences.getString("turnoActual","");
        codigoActividadNombre= sharedPreferences.getString("codigoActividadNombre","");




    }
    private void activarBotones (boolean estado) {
        btnIngresarActividad.setEnabled(estado);
        btnEliminarActividad.setEnabled(estado);
        btnIniciarJornada.setEnabled(!estado);
        btnSalidaDeTurno.setEnabled(estado);
        btnFecha.setEnabled(estado);
        viewActividades.setEnabled(estado);
    }

    private void guardarPreferencia(String actividadLaboral){
        SharedPreferences sharedPreferences= getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("sistemaActual",codigoMaquinaria);
        editor.putString("turnoActual",codigoTurno );
        editor.putString("codigoActividadLaboral",actividadLaboral );
        editor.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getTodaysDate()    {
        Calendar cal = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            cal = Calendar.getInstance();
        }
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initDatePicker()    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);

                fechaActual.setText(date);
                s=date;
    //            if (primeraVez)
      //              buscarComandaDiaria (codigoOperador,codigoSistema,codigoTurno,s.toString());
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year)    {
        String dayStr   =   String.format("%02d" , day);
        String monthStr =   String.format( "%02d",month );

        return  dayStr +"-" + monthStr +"-" + year;
    }

    private String getMonthFormat(int month)    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view)
    {
      datePickerDialog.show();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //ASA
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {

                String cod = data.getStringExtra("codigoTurno");
   /*             turnoActual.setText(cod);

                buscarComandaDiaria(codigoOperador,codigoSistema,cod,s.toString());
                buscarNombreTurno(cod);
                */

            }
        }

        if (requestCode == 4) {

                recuperarPreferencia();
                items.add(new CIngresoActividades(codigoActividadNombre,R.drawable.activity_type, R.drawable.delete_x));
                 recycler.setAdapter(adapter);


       //         buscarComandaDiaria(codigoOperador,cod,codigoTurno,s.toString());



        }


    }


    private void buscarNombreSistemas(String ultimoSistema) {
        String url = "http://"+webServer+"/android/buscarNombreSistemas.php";

        StringRequest stringRequest= new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = null;
                            obj = new JSONObject(response);
                            txtSistemaSeleccionado.setText(obj.get("describien").toString());
                        } catch (JSONException e) {
                            Toast.makeText(actividadJornada.this, "7" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(actividadJornada.this, "8" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("numerobien", ultimoSistema);
                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}