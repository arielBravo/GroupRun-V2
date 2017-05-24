package com.ariel_bravo_outlook.grouprun;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AgregarEvento extends AppCompatActivity implements View.OnClickListener {

    EditText nombreEvento,descripcion;
    FloatingActionButton regresar;
    FloatingActionButton guardar;
    Button fecha;
    Button hora;
    Button ruta;
    TextView tfecha,thora;
    String origen;
    String destino;
    String idUsuario;

     public int dia,mes,anio,hor,minutos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_evento);


        regresar = (FloatingActionButton) findViewById(R.id.regresarActividadPrincipal);
        guardar = (FloatingActionButton) findViewById(R.id.confirmarAgregacion);
        fecha = (Button) findViewById(R.id.fecha);
        ruta = (Button) findViewById(R.id.ruta);
        hora = (Button) findViewById(R.id.hora);
        tfecha = (TextView) findViewById(R.id.texviewFecha);
        thora = (TextView) findViewById(R.id.textviewHora);
        nombreEvento=(EditText)findViewById(R.id.nombreEvento);
        descripcion=(EditText)findViewById(R.id.descripcionEvento);

        fecha.setOnClickListener(this);
        hora.setOnClickListener(this);
        regresar.setOnClickListener(this);
        ruta.setOnClickListener(this);
        guardar.setOnClickListener(this);



        //puedo extraer informacion
         origen=getIntent().getStringExtra("infoOrigen");
        destino=getIntent().getStringExtra("infoDestino");
        idUsuario=getIntent().getStringExtra("idUsuario");


    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();


    }



    //listeners de los botones
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v){
        if(v==fecha){
            final Calendar calendario = Calendar.getInstance();
            dia = calendario.get(Calendar.DAY_OF_MONTH);
            mes = calendario.get(Calendar.MONTH);
            anio = calendario.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    tfecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                }
            }
            ,anio,mes,dia);
            datePickerDialog.show();
        }

        if(v==hora){
            final Calendar calendario = Calendar.getInstance();
            hor = calendario.get(Calendar.HOUR_OF_DAY);
            minutos = calendario.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    thora.setText(hourOfDay+" : "+minute);
                }
            },hor,minutos,false);
            timePickerDialog.show();
        }
        if(v==regresar){
            goActividadPrincial();
        }
        if(v==ruta){
            goAgregarRuta();
        }
        if(v==guardar) {

            if (tfecha.getText().toString().length() == 0  || thora.getText().toString().length() == 0  || nombreEvento.getText().toString().length() == 0  || descripcion.getText().toString().length() == 0
                    ||origen==null||destino==null) {
                Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show();

            }else{

                goActividadPrincial_con_datos();
            }
        }
    }
    public void goActividadPrincial() {
        Intent intent=new Intent(this,MainActivity.class);


        startActivity(intent);
    }
    public void goAgregarRuta() {
        Intent intent=new Intent(this,AgregarRuta.class);

        startActivity(intent);
    }
    public void goActividadPrincial_con_datos() {
        Intent intent=new Intent(AgregarEvento.this,MainActivity.class);

        intent.putExtra("fecha",tfecha.getText().toString());
        intent.putExtra("hora",thora.getText().toString());
        intent.putExtra("nombreEvento",nombreEvento.getText().toString());
        intent.putExtra("descripcion",descripcion.getText().toString());
        intent.putExtra("origen",origen);
        intent.putExtra("destino",destino);

        startActivity(intent);
    }
}