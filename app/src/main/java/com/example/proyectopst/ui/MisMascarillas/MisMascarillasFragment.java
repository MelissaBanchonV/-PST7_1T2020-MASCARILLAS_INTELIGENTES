package com.example.proyectopst.ui.MisMascarillas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectopst.MenuPrincipal1;
import com.example.proyectopst.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class MisMascarillasFragment extends Fragment {
    private ListView lista;
    private MisMascarillasViewModel misMascarillasViewModel;
    public int cantidad_contactos = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        misMascarillasViewModel =
                ViewModelProviders.of(this).get(MisMascarillasViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mis_mascarillas, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        final ListView lista = root.findViewById(R.id.listViewContenedorMascarillas);
        consultaMascarillaUsuario(lista);
        misMascarillasViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    public void consultaMascarillaUsuario(final ListView mascarillas){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("https://undried-modes.000webhostapp.com/prueba_busqueda_mascarilla.php?id_usuario="+MenuPrincipal1.id_usuario,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject jsonObject = null;
                        if(response.length()!=0){
                            String[][] datos_adaptador = new String[response.length()][4];
                            ArrayList<String> datosCompletos = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    jsonObject = response.getJSONObject(i);
                                    datos_adaptador[i][0]=jsonObject.get("codigo_mascarilla").toString();
                                    datos_adaptador[i][1]=jsonObject.get("seguro").toString();
                                    datosCompletos.add(jsonObject.get("codigo_mascarilla").toString());
                                    datosCompletos.add(jsonObject.get("fecha").toString());
                                    datosCompletos.add(jsonObject.get("distancia").toString());
                                    if(jsonObject.get("seguro").toString().equals("0")){
                                        datosCompletos.add("No");
                                    }else{
                                        datosCompletos.add("Sí");
                                    }
                                } catch (JSONException je) {
                                    Log.e("ERROR_JSON",je.getMessage());
                                }
                            }
                            Hashtable<String, Integer> personasyEdades = new Hashtable<String, Integer>();
                            for (int x=0; x < datos_adaptador.length; x++){
                                int pruebaValor = 0;
                                Log.e("SEGURO? - ",datos_adaptador[x][1]);
                                for (Map.Entry<String, Integer> entry : personasyEdades.entrySet()) {
                                    if(entry.getKey().equals(datos_adaptador[x][0])){
                                        pruebaValor = 1;
                                    }
                                }
                                for (Map.Entry<String, Integer> entry : personasyEdades.entrySet()) {
                                    Log.i("CLAVE ",entry.getKey());
                                    Log.i("VALOR ", String.valueOf(entry.getValue()));
                                }
                                if(pruebaValor==1){
                                    if(datos_adaptador[x][1].equals("0")){
                                        int numero = personasyEdades.get(datos_adaptador[x][0]) + 1;
                                        Log.i("CONTACTO_ENCONTRADO ","sumo uno");
                                        personasyEdades.put(datos_adaptador[x][0], numero);
                                    }
                                }
                                else{
                                    if(datos_adaptador[x][1].equals("0")){
                                        personasyEdades.put(datos_adaptador[x][0], 1);
                                    }else{
                                        personasyEdades.put(datos_adaptador[x][0], 0);
                                    }
                                }
                            }
                            String [][] listaEnviar= new String[personasyEdades.size()][4];
                            int contador = 0;
                            for (Map.Entry<String, Integer> entry : personasyEdades.entrySet()) {
                                listaEnviar[contador][0] = Integer.toString(contador+1);
                                listaEnviar[contador][1] = entry.getKey();
                                listaEnviar[contador][2] = Integer.toString(entry.getValue());
                                int numero = entry.getValue();
                                numero /= 1.5;
                                if(numero>=100){
                                    numero = 100;
                                }
                                listaEnviar[contador][3] = Integer.toString(numero);
                                contador++;
                            }
                            mostrarMascarillas(listaEnviar,mascarillas,datosCompletos);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR_CONEXION","consultaMU  -- "+error.getMessage());
                String[][] datos_adaptador = new String[1][4];
                datos_adaptador[0] = new String[]{"0", "0", "0", "0"};
                ArrayList<String> datos = new ArrayList<>();
                datos.add("0");
                datos.add("0000-00-00");
                datos.add("0");
                datos.add("0");
                mostrarMascarillas(datos_adaptador,mascarillas,datos);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    public void mostrarMascarillas(final String[][] datos, ListView lista,final ArrayList<String> informacionMascarilla){
        lista.setAdapter(new Adaptador(getContext(), datos,datos.length));
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long id) {
                Intent detalles = new Intent(view.getContext(),InformacionMascarilla.class);
                detalles.putExtra("Codigo",datos[posicion][1]);
                detalles.putExtra("Info",informacionMascarilla);
                startActivity(detalles);
            }
        });
    }
}