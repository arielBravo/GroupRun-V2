package com.ariel_bravo_outlook.grouprun;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by ariel on 23/05/2017.
 */

public class DatosListView {
    public ImageView fotoUsuario;
    public String nomUsuario ;
    public String emailUsu;
    public long id;


    public DatosListView(Bitmap imag, String nom, String email, long id){
        this.fotoUsuario.setImageBitmap(imag);
        this.nomUsuario=nom;
        this.emailUsu=email;
        this.id=id;

    }





}
