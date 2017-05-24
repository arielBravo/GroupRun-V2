package com.ariel_bravo_outlook.grouprun;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by ariel on 23/05/2017.
 */

public class AdapterLIstView extends BaseAdapter {

    public Activity activity;
    public ArrayList<DatosListView> items;
    public AdapterLIstView(Activity activity,ArrayList<DatosListView>items){
this.activity=activity;
        this.items=items;



    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=convertView;
        if(convertView==null){
            LayoutInflater inf=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inf.inflate(R.layout.itemlista,null);
        }
        return null;
    }
}
