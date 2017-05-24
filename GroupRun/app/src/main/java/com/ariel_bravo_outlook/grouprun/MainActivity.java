package com.ariel_bravo_outlook.grouprun;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;

import android.util.Base64;
import android.view.View;

import android.support.v7.app.AppCompatActivity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity implements FragmentDatosUsuario.OnFragmentInteractionListener,View.OnClickListener
         {
             FloatingActionButton agregar,buscarEvento;
             ImageView fotoPerfil,imag;
             TextView nombreUsuario,correoUsario,resultado,stringImagen;

             String nombreEvento;
             String hora;
             String fecha;
             String descripcion;
             String origen;
             String destino;
             String idUsuario;

             TextView tnombreEvento;
             TextView thora;
             EditText tfecha;
             TextView tdescripcion;
             TextView torigen;
             TextView tdestino;
             TextView tidUsuario;

             //IP de mi Url
             String IP="http://www.arielbravo.skn1.com";

             //Rutas de los webServices
             //cadenas de coneccion tabla Usuarios
             String GET=IP+"/obtener_usuarios.php";
             String GET_BY_ID=IP+"/obtener_usuario_por_id.php";
             //String UPDATE=IP+"/actualizar_usuario.php";
            // String DELETE=IP+"/borrar_usuario.php";
             String INSERT=IP+"/insertar_usuario.php";
            // ObtenerWebService hiloconexion;



             //Rutas de los webServices
             //cadenas de coneccion tabla Eventos
             String GET2=IP+"/obtener_eventos.php";
             String GET_BY_ID2=IP+"/obtener_evento_por_id.php";
             String UPDATE2=IP+"/actualizar_evento.php";
             String DELETE2=IP+"/borrar_evento.php";
             String INSERT2=IP+"/insertar_evento.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        fotoPerfil=(ImageView)findViewById(R.id.fotoPerfil);
       // imag=(ImageView)findViewById(R.id.imagen);
        agregar=(FloatingActionButton) findViewById(R.id.agregar);
       buscarEvento=(FloatingActionButton) findViewById(R.id.buscar);
        nombreUsuario=(TextView)findViewById(R.id.nombreusuario);
        correoUsario=(TextView)findViewById(R.id.correousuario);
        //stringImagen=(TextView)findViewById(R.id.StringImagen);

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAgregarScreen();
            }
        });

           buscarEvento.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   goBuscar_Evento();
               }
           });



        if(AccessToken.getCurrentAccessToken()==null){//si es nulo significa que no tengo la cesion de facebook iniciada
         goLoginScreen();       //si pasa eso queremos que vaya a la pantalla de login
        }else{

        sendGraphRequest(AccessToken.getCurrentAccessToken());

            fecha=getIntent().getStringExtra("fecha");
            hora=getIntent().getStringExtra("hora");
            nombreEvento=getIntent().getStringExtra("nombreEvento");


            descripcion=getIntent().getStringExtra("descripcion");
            origen=getIntent().getStringExtra("origen");
            destino=getIntent().getStringExtra("destino");
            idUsuario=AccessToken.getCurrentAccessToken().getUserId();


            //tfecha.setText(fecha);
            //thora.setText(hora);
            //    boolean a=fecha.equals("");
        if(fecha==null||hora==null||fecha==null||descripcion==null||
                   origen==null||destino==null||descripcion==null||idUsuario==null){//si todos los campos estan vacios


            }else{//si todos los campos estan llenos
                //agrego a mi tabla
                idUsuario=(","+idUsuario+",");
                String cadena=GET_BY_ID2+"?nombre="+nombreEvento;

                if(buscar_en_tablaEventos_por_Nombre(cadena)==false) {

                    insertar__tabla_Eventos(INSERT2,nombreEvento,idUsuario,hora,fecha,origen,destino,descripcion,1);
                    Toast.makeText(this, "Evento Guardado", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "El nombre del evento ya ha sido utilizado", Toast.LENGTH_SHORT).show();
                }

            }

        }



    }
//metodos
    private void goLoginScreen() {
        Intent intent=new Intent(this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        startActivity(intent);
    }
             public void goBuscar_Evento(){
                 Intent intent=new Intent(this,BuscarEvento.class);
                    intent.putExtra("idUsuario",AccessToken.getCurrentAccessToken().getUserId());
                 startActivity(intent);
             }


    //metodo para ir a la actividad agregar evento
    public void goAgregarScreen() {
        Intent intent=new Intent(this,AgregarEvento.class);
        intent.putExtra("idUsuario",AccessToken.getCurrentAccessToken().getUserId());
        startActivity(intent);
    }


             protected void sendGraphRequest(AccessToken accessToken ) {

                 GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                     @Override
                     public void onCompleted(JSONObject object, GraphResponse response) {


                         try {
                             if (android.os.Build.VERSION.SDK_INT > 9) {
                                 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                 StrictMode.setThreadPolicy(policy);
                                 String profilePicUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");

                                 URL fb_url = new URL(profilePicUrl);//small | noraml | large
                                 HttpsURLConnection conn1 = (HttpsURLConnection) fb_url.openConnection();
                                 HttpsURLConnection.setFollowRedirects(true);
                                 conn1.setInstanceFollowRedirects(true);
                                 Bitmap fb_img = BitmapFactory.decodeStream(conn1.getInputStream());
                                 fotoPerfil.setImageBitmap(fb_img);
                                // stringImagen.setText(BitMapToString(fb_img));
                                 //imag.setImageBitmap(StringToBitMap(stringImagen.getText().toString()));
                                 nombreUsuario.setText(object.getString("name"));
                                 correoUsario.setText(object.getString("email"));

                                 String cadenallamada = GET_BY_ID + "?idUsuario=" + object.getString("id");

                                if(buscar_en_base_datos_MySQL_por_id(cadenallamada)==false) {//si es que no se ha registrado antes, agrego a la tabla Uusarios

                                    insertar__base_datos_MySQL(INSERT,object.getString("id"),object.getString("name"),object.getString("email"),stringImagen.getText().toString());

                                }else{

                                }
                             }
                         }catch (Exception ex) {
                             ex.printStackTrace();
                         }
                     }
                 });
                 Bundle parameters = new Bundle();
                 parameters.putString("fields", "id,name,email,birthday,picture");
                 request.setParameters(parameters);
                 request.executeAsync();
             }
             @Override
             public void onFragmentInteraction(Uri uri) {

             }
             public void logout(View view) {
                 LoginManager.getInstance().logOut();
                 goLoginScreen();//metodo para ir a la pantalla de login

             }

             @Override
             public void onClick(View v) {


             }
             //hilos de ejecucion
           /*  public class ObtenerWebService extends AsyncTask<String,Void,String> {

                 @Override
                 protected String doInBackground(String... params) {

                     String cadena = params[0];
                     URL url = null; // Url de donde queremos obtener información
                     String devuelve ="";
                      if(params[1].equals("4")){    // update

                         try {
                             HttpURLConnection urlConn;

                             DataOutputStream printout;
                             DataInputStream input;
                             url = new URL(cadena);
                             urlConn = (HttpURLConnection) url.openConnection();
                             urlConn.setDoInput(true);
                             urlConn.setDoOutput(true);
                             urlConn.setUseCaches(false);
                             urlConn.setRequestProperty("Content-Type", "application/json");
                             urlConn.setRequestProperty("Accept", "application/json");
                             urlConn.connect();
                             //Creo el Objeto JSON
                             JSONObject jsonParam = new JSONObject();
                             jsonParam.put("idUsuario",params[2]);
                             jsonParam.put("nombre", params[3]);
                             jsonParam.put("correo", params[4]);
                             // Envio los parámetros post.
                             OutputStream os = urlConn.getOutputStream();
                             BufferedWriter writer = new BufferedWriter(
                                     new OutputStreamWriter(os, "UTF-8"));
                             writer.write(jsonParam.toString());
                             writer.flush();
                             writer.close();

                             int respuesta = urlConn.getResponseCode();


                             StringBuilder result = new StringBuilder();

                             if (respuesta == HttpURLConnection.HTTP_OK) {

                                 String line;
                                 BufferedReader br=new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                                 while ((line=br.readLine()) != null) {
                                     result.append(line);
                                     //response+=line;
                                 }

                                 //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                                 JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                                 //Accedemos al vector de resultados

                                 String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                                 if (resultJSON.equals("1")) {      // hay un alumno que mostrar
                                     devuelve = "Uusario actualizado correctamente";

                                 } else if (resultJSON.equals("2")) {
                                     devuelve = "El usuario no pudo actualizarse";
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
                     else if(params[1].equals("5")) {    // delete

                         try {
                             HttpURLConnection urlConn;

                             DataOutputStream printout;
                             DataInputStream input;
                             url = new URL(cadena);
                             urlConn = (HttpURLConnection) url.openConnection();
                             urlConn.setDoInput(true);
                             urlConn.setDoOutput(true);
                             urlConn.setUseCaches(false);
                             urlConn.setRequestProperty("Content-Type", "application/json");
                             urlConn.setRequestProperty("Accept", "application/json");
                             urlConn.connect();
                             //Creo el Objeto JSON
                             JSONObject jsonParam = new JSONObject();
                             jsonParam.put("id", params[2]);
                             // Envio los parámetros post.
                             OutputStream os = urlConn.getOutputStream();
                             BufferedWriter writer = new BufferedWriter(
                                     new OutputStreamWriter(os, "UTF-8"));
                             writer.write(jsonParam.toString());
                             writer.flush();
                             writer.close();

                             int respuesta = urlConn.getResponseCode();


                             StringBuilder result = new StringBuilder();

                             if (respuesta == HttpURLConnection.HTTP_OK) {

                                 String line;
                                 BufferedReader br=new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                                 while ((line=br.readLine()) != null) {
                                     result.append(line);
                                     //response+=line;
                                 }

                                 //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                                 JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                                 //Accedemos al vector de resultados

                                 String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                                 if (resultJSON.equals("1")) {      // hay un alumno que mostrar
                                     devuelve = "Uusario borrado correctamente";

                                 } else if (resultJSON.equals("2")) {
                                     devuelve = "No hay usuarios";
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



                     return null;
                 }

                 @Override
                 protected void onCancelled(String s) {
                     super.onCancelled(s);
                 }

                 @Override
                 protected void onPostExecute(String s) {
                     resultado.setText(s);
                     //super.onPostExecute(s);
                 }

                 @Override
                 protected void onPreExecute() {
                     super.onPreExecute();
                 }

                 @Override
                 protected void onProgressUpdate(Void... values) {
                     super.onProgressUpdate(values);
                 }
             }*/


             public void insertar__base_datos_MySQL(String cadena,String idUsua,String nom,String corr,String foto){
                 URL url = null;
                 try {
                     HttpURLConnection urlConn;

                     DataOutputStream printout;
                     DataInputStream input;
                     url = new URL(cadena);
                     urlConn = (HttpURLConnection) url.openConnection();
                     urlConn.setDoInput(true);
                     urlConn.setDoOutput(true);
                     urlConn.setUseCaches(false);
                     urlConn.setRequestProperty("Content-Type", "application/json");
                     urlConn.setRequestProperty("Accept", "application/json");
                     urlConn.connect();
                     //Creo el Objeto JSON
                     JSONObject jsonParam = new JSONObject();
                     jsonParam.put("idUsuario", idUsua);
                     jsonParam.put("nombre", nom);
                     jsonParam.put("correo", corr);
                     jsonParam.put("fotoUsuario", foto);
                     // Envio los parámetros post.
                     OutputStream os = urlConn.getOutputStream();
                     BufferedWriter writer = new BufferedWriter(
                             new OutputStreamWriter(os, "UTF-8"));
                     writer.write(jsonParam.toString());
                     writer.flush();
                     writer.close();

                     int respuesta = urlConn.getResponseCode();


                     StringBuilder result = new StringBuilder();

                     if (respuesta == HttpURLConnection.HTTP_OK) {

                         String line;
                         BufferedReader br=new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                         while ((line=br.readLine()) != null) {
                             result.append(line);
                             //response+=line;
                         }

                         //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                         JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                         //Accedemos al vector de resultados

                         String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                         if (resultJSON.equals("1")) {      // hay un usuario que mostrar
                             //devuelve = "Usuario insertado correctamente";

                         } else if (resultJSON.equals("2")) {
                            // devuelve = "El usuario no pudo insertarse";
                         }

                     }

                 } catch (MalformedURLException e) {
                     e.printStackTrace();
                 } catch (IOException e) {
                     e.printStackTrace();
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }

               //  return devuelve;


         }
            public boolean buscar_en_base_datos_MySQL_por_id(String cadena){
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

                        if (resultJSON.equals("1")){      // hay un usuario que mostrar
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
            public void consultar_toda_Usuarios(String cadena){
                String devuelve="";
                try {

                    URL url = null;
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



                        if (resultJSON.equals("1")){      // hay alumnos a mostrar
                            JSONArray alumnosJSON = respuestaJSON.getJSONArray("usuarios");   // estado es el nombre del campo en el JSON
                            for(int i=0;i<alumnosJSON.length();i++){
                                devuelve = devuelve + alumnosJSON.getJSONObject(i).getString("idUsuario") + " " +
                                        alumnosJSON.getJSONObject(i).getString("nombre") + " " +
                                        alumnosJSON.getJSONObject(i).getString("correo") + "\n";

                            }

                        }
                        else if (resultJSON.equals("2")){
                            devuelve = "No hay usuarios";
                        }


                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }







            }
             public String BitMapToString(Bitmap bitmap){//metodo para convertir una imagen a string para guardarle en la base de datos
                 ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                 bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
                 byte [] b=baos.toByteArray();
                 String temp= Base64.encodeToString(b, Base64.DEFAULT);
                 return temp;
             }
             public Bitmap StringToBitMap(String encodedString){//metodo para convertir un String a imagen
                 try {
                     byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
                     Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                     return bitmap;
                 } catch(Exception e) {
                     e.getMessage();
                     return null;
                 }
             }



             //conexion tabla Eventos
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

                         if (resultJSON.equals("1")){      // hay un usuario que mostrar
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
             public void insertar__tabla_Eventos(String cadena,String nombre,String idPar,String hor,String fec,String ori,String dest,String desc,int numPar){
                 URL url = null;
                 try {
                     HttpURLConnection urlConn;

                     DataOutputStream printout;
                     DataInputStream input;
                     url = new URL(cadena);
                     urlConn = (HttpURLConnection) url.openConnection();
                     urlConn.setDoInput(true);
                     urlConn.setDoOutput(true);
                     urlConn.setUseCaches(false);
                     urlConn.setRequestProperty("Content-Type", "application/json");
                     urlConn.setRequestProperty("Accept", "application/json");
                     urlConn.connect();
                     //Creo el Objeto JSON
                     JSONObject jsonParam = new JSONObject();
                     jsonParam.put("nombre", nombre);
                     jsonParam.put("idParticipantes", idPar);
                     jsonParam.put("hora", hor);
                     jsonParam.put("fecha", fec);
                     jsonParam.put("origen", ori);
                     jsonParam.put("destino", dest);
                     jsonParam.put("descripcion", desc);
                     jsonParam.put("numeroParticipantes", numPar);
                     // Envio los parámetros post.
                     OutputStream os = urlConn.getOutputStream();
                     BufferedWriter writer = new BufferedWriter(
                             new OutputStreamWriter(os, "UTF-8"));
                     writer.write(jsonParam.toString());
                     writer.flush();
                     writer.close();

                     int respuesta = urlConn.getResponseCode();


                     StringBuilder result = new StringBuilder();

                     if (respuesta == HttpURLConnection.HTTP_OK) {

                         String line;
                         BufferedReader br=new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                         while ((line=br.readLine()) != null) {
                             result.append(line);
                             //response+=line;
                         }

                         //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                         JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                         //Accedemos al vector de resultados

                         String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                         if (resultJSON.equals("1")) {      // hay un usuario que mostrar
                             //devuelve = "Usuario insertado correctamente";

                         } else if (resultJSON.equals("2")) {
                             // devuelve = "El usuario no pudo insertarse";
                         }

                     }

                 } catch (MalformedURLException e) {
                     e.printStackTrace();
                 } catch (IOException e) {
                     e.printStackTrace();
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }

                 //  return devuelve;


             }

             public void consultar_toda_Eventos(String cadena){
                 String devuelve="";
                 try {

                     URL url = null;
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



                         if (resultJSON.equals("1")){      // hay alumnos a mostrar
                             JSONArray alumnosJSON = respuestaJSON.getJSONArray("eventos");   // estado es el nombre del campo en el JSON
                             for(int i=(alumnosJSON.length()-1);i>=0;i--){
                                 devuelve = devuelve + alumnosJSON.getJSONObject(i).getString("Nombre") + " " +
                                         alumnosJSON.getJSONObject(i).getString("idParticipantes") + " " +
                                         alumnosJSON.getJSONObject(i).getString("hora") + " " +
                                         alumnosJSON.getJSONObject(i).getString("fecha") + " " +
                                         alumnosJSON.getJSONObject(i).getString("origen") + " " +
                                         alumnosJSON.getJSONObject(i).getString("destino") + " " +
                                         alumnosJSON.getJSONObject(i).getString("descripcion") + " " +
                                         alumnosJSON.getJSONObject(i).getString("numeroParticipantes") + "\n";

                             }

                         }

                     }


                 } catch (MalformedURLException e) {
                     e.printStackTrace();
                 } catch (IOException e) {
                     e.printStackTrace();
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }







             }
             public String obtenerIDS(String cadena){
                 String devuelve="";
                 try {

                     URL url = null;
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



                         if (resultJSON.equals("1")){      // hay alumnos a mostrar
                             JSONArray alumnosJSON = respuestaJSON.getJSONArray("eventos");   // estado es el nombre del campo en el JSON
                             for(int i=0;i<alumnosJSON.length();i++){
                                 devuelve = devuelve+alumnosJSON.getJSONObject(i).getString("idParticipantes");

                             }

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
         }
