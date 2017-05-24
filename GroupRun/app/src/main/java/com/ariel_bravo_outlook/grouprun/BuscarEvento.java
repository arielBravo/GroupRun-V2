package com.ariel_bravo_outlook.grouprun;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BuscarEvento extends AppCompatActivity implements FragmentEvento.OnFragmentInteractionListener {
    Button buscarEvento,regresar;
    EditText ebus;
    //IP de mi Url
    String IP="http://www.arielbravo.skn1.com";
    //Rutas de los webServices
    //cadenas de coneccion tabla Eventos
    String GET2=IP+"/obtener_eventos.php";
    String GET_BY_ID2=IP+"/obtener_evento_por_id.php";
    String UPDATE2=IP+"/actualizar_evento.php";
    String DELETE2=IP+"/borrar_evento.php";
    String INSERT2=IP+"/insertar_evento.php";
     String numparti;
    String idPartici;
    String fecha;
    String hora;
    String destino;
    String origen;
    String nomEV;
    String descrip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_evento);

        buscarEvento=(Button)findViewById(R.id.botonBus);
        regresar=(Button)findViewById(R.id.bsalir);
        ebus=(EditText)findViewById(R.id.eBuscar);

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gomainScreen();
            }
        });


        buscarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ebus.getText().length()!=0){
                    String cadena=GET_BY_ID2+"?nombre="+ebus.getText().toString();
                    if(buscar_en_tablaEventos_por_Nombre(cadena)) {
                        buscar_en_tablaEventos_Datos_Usuarios(cadena);
                           //sacar el fragment

                       String idUser=getIntent().getStringExtra("idUsuario");
                       Bundle bundle=new Bundle();
                        bundle.putString("nom",nomEV);
                        bundle.putString("idPar",idPartici);
                        bundle.putString("fecha",fecha);
                        bundle.putString("hora",hora);
                        bundle.putString("origen",origen);
                        bundle.putString("destino",destino);
                        bundle.putString("descripcion",descrip);
                        bundle.putString("numeroParticipantes",numparti);
                        bundle.putString("idUser",idUser);

                        FragmentEvento fragment=new FragmentEvento();

                        FragmentManager fragmentManager=getFragmentManager();
                        FragmentTransaction transaction=fragmentManager.beginTransaction();

                        fragment.setArguments(bundle);
                        transaction.add(R.id.layout,fragment);
                        fragment.setArguments(bundle);
                        //Paso 4: Confirmar el cambio
                        transaction.commit();

                    }else{
                        Toast.makeText(BuscarEvento.this, "No se encuentra el evento", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(BuscarEvento.this, "No hay nada que buscar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
public void gomainScreen(){
    Intent intent=new Intent(this,MainActivity.class);
    startActivity(intent);
}
    public boolean buscar_en_tablaEventos_por_Nombre(String cadena){
        boolean devuelve=false;
        try {


            URL url = null; // Url de donde queremos obtener información
            url = new URL(cadena);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
            connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                    " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
            //connection.setHeader("content-type", "application/json");

            int respuesta = connection.getResponseCode();
            StringBuilder result = new StringBuilder();

            if (respuesta == HttpURLConnection.HTTP_OK){


                InputStream in = new BufferedInputStream(connection.getInputStream());  // preparo la cadena de entrada

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));  // la introduzco en un BufferedReader

                // El siguiente proceso lo hago porque el JSONOBject necesita un String y tengo
                // que tranformar el BufferedReader a String. Esto lo hago a traves de un
                // StringBuilder.

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);        // Paso toda la entrada al StringBuilder
                }

                //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                //Accedemos al vector de resultados

                String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                if (resultJSON.equals("1")){

                    devuelve = true;

                }
                else if (resultJSON.equals("2")){
                    //le dejo en false
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return devuelve;



    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    public void buscar_en_tablaEventos_Datos_Usuarios(String cadena){
        boolean devuelve=false;
        try {


            URL url = null; // Url de donde queremos obtener información
            url = new URL(cadena);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
            connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                    " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
            //connection.setHeader("content-type", "application/json");

            int respuesta = connection.getResponseCode();
            StringBuilder result = new StringBuilder();

            if (respuesta == HttpURLConnection.HTTP_OK){


                InputStream in = new BufferedInputStream(connection.getInputStream());  // preparo la cadena de entrada

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));  // la introduzco en un BufferedReader

                // El siguiente proceso lo hago porque el JSONOBject necesita un String y tengo
                // que tranformar el BufferedReader a String. Esto lo hago a traves de un
                // StringBuilder.

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);        // Paso toda la entrada al StringBuilder
                }

                //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                //Accedemos al vector de resultados

                   idPartici=respuestaJSON.getJSONObject("evento").getString("idParticipantes");
                    numparti=respuestaJSON.getJSONObject("evento").getString("numeroParticipantes");
                    fecha=respuestaJSON.getJSONObject("evento").getString("fecha");
                    hora=respuestaJSON.getJSONObject("evento").getString("hora");
                    nomEV=respuestaJSON.getJSONObject("evento").getString("nombre");
                    descrip=respuestaJSON.getJSONObject("evento").getString("descripcion");
                    destino=respuestaJSON.getJSONObject("evento").getString("destino");
                    origen=respuestaJSON.getJSONObject("evento").getString("origen");// hay un usuario que mostrar




            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }





    }
}
