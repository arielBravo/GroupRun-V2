package com.ariel_bravo_outlook.grouprun;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentEvento.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FragmentEvento extends Fragment {
Button botonUnirse,verParticipantes;
    TextView nombre;
    TextView fecha;
    TextView hora;
    TextView descipcion;
    TextView destino;
    TextView origen;
    TextView numeroParticipantes;
    String idUSER;
    String idGeneralParticipantes;
    String numeroPar;
    //IP de mi Url
    String IP="http://www.arielbravo.skn1.com";
    private OnFragmentInteractionListener mListener;

    //Rutas de los webServices
    //cadenas de coneccion tabla Eventos
    String GET2=IP+"/obtener_eventos.php";
    String GET_BY_ID2=IP+"/obtener_evento_por_id.php";
    String UPDATE2=IP+"/actualizar_evento.php";
    String DELETE2=IP+"/borrar_evento.php";
    String INSERT2=IP+"/insertar_evento.php";
    public FragmentEvento() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_fragment_evento,container,false);

        botonUnirse=(Button)view.findViewById(R.id.unirse);
        verParticipantes=(Button)view.findViewById(R.id.verParticipantes);
        nombre=(TextView)view.findViewById(R.id.nomEvento);
        fecha=(TextView)view.findViewById(R.id.fechaEv);
        hora=(TextView)view.findViewById(R.id.horaEv);
        descipcion=(TextView)view.findViewById(R.id.descripcionEven);
        destino=(TextView)view.findViewById(R.id.destinoEv);
        origen=(TextView)view.findViewById(R.id.origenEv);
        numeroParticipantes=(TextView)view.findViewById(R.id.numPart);

        //Bundle bundle=getIntent().getExtras();
      if(getArguments()!=null) {//si tenemos parametros
          nombre.invalidate();
          fecha.setText("");
          hora.setText("");
          descipcion.setText("");
          destino.setText("");
          origen.setText("");
          numeroParticipantes.setText("");

          nombre.setText(getArguments().getString("nom").toString());
          fecha.setText(getArguments().getString("fecha").toString());
          hora.setText(getArguments().getString("hora").toString());
          descipcion.setText(getArguments().getString("descripcion").toString());
          destino.setText(getArguments().getString("destino").toString());
          origen.setText(getArguments().getString("origen").toString());
          numeroParticipantes.setText(getArguments().getString("numeroParticipantes".toString()));
           idUSER=(getArguments().getString("idUser")).toString();

      }else{

          }

      //
verParticipantes.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
goVerParticipantes();

    }
});
        botonUnirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cadena=GET_BY_ID2+"?nombre="+nombre.getText().toString();
            buscar_en_tablaEventos_por_Nombre_editar(cadena);

                //comprobamos que no se haya registrado antes
                String comparar[]=idGeneralParticipantes.split(",");
                int contador=0;
                for(int i=0;i<comparar.length;i++){
                    if(!comparar[i].equals(idUSER)){
                        contador++;
                    }
                }
                if(contador==comparar.length) {//aun no se ha registrado
                    idGeneralParticipantes = idGeneralParticipantes + idUSER + ",";

                    int numero=0;
                            numero=Integer.parseInt(numeroPar);
                    numero = numero + 1;
                    actualizar_tabla_Eventos(UPDATE2,nombre.getText().toString(),idGeneralParticipantes, numero);
                    Toast.makeText(getContext(), "Ha sido agregado al evento", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Ya se encuentra registrado en el evento", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public boolean buscar_en_tablaEventos_por_Nombre_editar(String cadena){
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

                idGeneralParticipantes=respuestaJSON.getJSONObject("evento").getString("idParticipantes");
                numeroPar=respuestaJSON.getJSONObject("evento").getString("numeroParticipantes");

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
    public void actualizar_tabla_Eventos(String cadena,String nombre,String idUsua,int numPar){
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
            jsonParam.put("nombre",nombre);
            jsonParam.put("idParticipantes",idUsua);
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



            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }




    }
    public void goVerParticipantes(){
      /*  Intent intent=new Intent(getActivity(),VerParticipantes.class);
        intent.putExtra("usuarios",idUSER);
        startActivity(intent);*/
    }
}