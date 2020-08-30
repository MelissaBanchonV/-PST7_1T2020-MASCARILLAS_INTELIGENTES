package com.example.proyectopst.ui.MisMascarillas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.proyectopst.R;
import com.example.proyectopst.TablaDinamica;

import java.util.ArrayList;

public class InformacionMascarilla extends AppCompatActivity {
    TextView titulo;
    private TableLayout consultaTabla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_mascarilla);
        titulo = (TextView)findViewById(R.id.textViewCodigoMascDetalles);
        consultaTabla = (TableLayout) findViewById(R.id.tablaDatos);
        Intent datos = getIntent();
        Bundle b = datos.getExtras();

        DisplayMetrics medidaVentana = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(medidaVentana);
        int ancho = medidaVentana.widthPixels;
        int alto = medidaVentana.heightPixels;
        getWindow().setLayout((int)(ancho*0.8),(int)(alto*0.5));

        titulo.setText("Mascarilla: "+b.getString("Codigo"));

        TablaDinamica tablaD = new TablaDinamica(consultaTabla,getApplicationContext());
        String[] header = new String[]{"Fecha", "Distancia", "Seguro"};
        tablaD.addHeader(header);
        ArrayList<String> datosCompletos = b.getStringArrayList("Info");
        ArrayList<String[]> contenido = new ArrayList<String[]>();
        for(int i=0; i<datosCompletos.size()-3;i++){
            if(datosCompletos.get(i).equals(b.getString("Codigo"))){
                if(!datosCompletos.get(i+1).equals("0000-00-00")){
                    String[] cont = new String[3];
                    cont[0] = datosCompletos.get(i+1);
                    cont[1] = datosCompletos.get(i+2);
                    cont[2] = datosCompletos.get(i+3);
                    contenido.add(cont);
                }
            }
        }
        if(contenido.size()==0){
            contenido.add(new String[]{"No hay datos para mostrar."});
        }
        tablaD.addData(contenido);
    }
}