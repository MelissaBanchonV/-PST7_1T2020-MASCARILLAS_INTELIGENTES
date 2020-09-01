package com.example.proyectopst.ui.MisMascarillas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.proyectopst.R;

public class Adaptador extends BaseAdapter {
    private static LayoutInflater inflater = null;

    Context contexto;
    String[][] datos;
    int cantidad;

    /**
     * Constructor del adaptador que ingresa los datos de la mascarilla en el layout elemento_lista.
     * @param contexto Contexto del activity.
     * @param datos Información que contendrán los elementos de la lista.
     * @param cantidad Cantidad de datos que se mostrarán.
     */
    public Adaptador(Context contexto, String[][] datos, int cantidad) {
        this.contexto = contexto;
        this.datos = datos;
        this.cantidad = cantidad;
        inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Ingresa los datos obtenidos en su respectivo contenedor.
     * @param i
     * @param view
     * @param viewGroup
     * @return
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View vista = inflater.inflate(R.layout.elemento_lista, null);
        TextView Num = (TextView) vista.findViewById(R.id.textViewNumMascarilla);
        TextView Cod = (TextView) vista.findViewById(R.id.textViewCodigoMascarilla);
        TextView Con = (TextView) vista.findViewById(R.id.textViewCantidadContactos);
        TextView Uso = (TextView) vista.findViewById(R.id.textViewUsoMascarilla);
        if(datos[i][1].equals("0")){
            Cod.setText("Agregue una mascarilla.");
        }else{
            Cod.setText("Código: "+datos[i][1]);
        }
        Num.setText("Mascarilla N°"+datos[i][0]);
        Con.setText("Cantidad de contactos: "+datos[i][2]);
        Uso.setText("Uso: "+datos[i][3]+"%");
        return vista;
    }

    /**
     *
     * @return
     */
    @Override
    public int getCount() {
        return cantidad;
    }

    /**
     *
     * @param i
     * @return
     */
    @Override
    public Object getItem(int i) {
        return null;
    }

    /**
     *
     * @param i
     * @return
     */
    @Override
    public long getItemId(int i) {
        return 0;
    }

}
