package com.example.proyectopst.ui.Inicio;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

public class InicioFragment extends Fragment {
    private InicioViewModel inicioViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        inicioViewModel = ViewModelProviders.of(this).get(InicioViewModel.class);
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        final TextView usuario = root.findViewById(R.id.textViewUsuario);
        final TextView cedula = root.findViewById(R.id.textViewCedula);
        final TextView correo = root.findViewById(R.id.textViewCorreo);
        final TextView contactos = root.findViewById(R.id.textViewCantidadContactos);
        final TextView cantidadMascarillas = root.findViewById(R.id.textViewCantidadMascRegist);
        final TextView ultimoRegistro = root.findViewById(R.id.textViewMostrarUltimoRegistro);
        final ImageView touchscreen = root.findViewById(R.id.touchscreen);
        consultaUltimoRegistro(ultimoRegistro);
        consultaUsuario(usuario,cedula,correo,contactos);
        consultaMascarilla(cantidadMascarillas);
        touchscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consultaUltimoRegistro(ultimoRegistro);
                consultaUsuario(usuario,cedula,correo,contactos);
                consultaMascarilla(cantidadMascarillas);
            }
        });
        inicioViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    /**
     * Obtiene de la base de datos la información del usuario que ha iniciado sesión.
     * @param usuario TextView para mostrar el nombre del usuario.
     * @param cedula TextView para mostrar la cédula del usuario.
     * @param correo TextView para mostrar el correo del usuario.
     * @param contactos TextView para mostrar la cantidad de acercamientos o contactos con otra persona que ha tenido el usuario.
     */
    public void consultaUsuario(final TextView usuario, final TextView cedula, final TextView correo, final TextView contactos){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("https://undried-modes.000webhostapp.com/consulta_datos_usuario.php?id_usuario="+MenuPrincipal1.id_usuario,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject jsonObject = null;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                jsonObject = response.getJSONObject(i);
                                usuario.setText(jsonObject.get("nombre").toString()+" "+jsonObject.get("apellido").toString());
                                cedula.setText(jsonObject.get("cedula").toString());
                                correo.setText(jsonObject.get("correo").toString());
                                contactos.setText(jsonObject.get("conteo_personas").toString());
                            } catch (JSONException je) {
                                Log.e("ERROR_JSON",je.getMessage());
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR_CONEXION",error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    /**
     * Obtiene de la base de datos la cantidad de mascarillas que tiene el usuario.
     * @param cantidadMascarillas TextView para mostrar la cantidad de mascarillas del usuario.
     */
    public void consultaMascarilla(final TextView cantidadMascarillas){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("https://undried-modes.000webhostapp.com/consulta_datos_mascarilla.php?id_usuario="+MenuPrincipal1.id_usuario,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(Integer.toString(response.length())!=null){
                            cantidadMascarillas.setText(Integer.toString(response.length()));
                        }else{
                        cantidadMascarillas.setText("0");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR_CONEXION",error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    /**
     * Obtiene de la base de datos la fecha del último registro de información de alguna de las mascarillas del usuario.
     * @param ultimoRegistro TextView para mostrar la fecha del último contacto que ha tenido el usuario.
     */
    public void consultaUltimoRegistro(final TextView ultimoRegistro){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("https://undried-modes.000webhostapp.com/fecha_ultimo_registro.php?id_usuario="+MenuPrincipal1.id_usuario,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject jsonObject = null;
                        if(response.length()!=0){
                            for (int i = 0; i < 1; i++) {
                                try{
                                    jsonObject = response.getJSONObject(i);
                                    if(!jsonObject.get("fecha").toString().equals("0000-00-00")){
                                        ultimoRegistro.setText(jsonObject.get("fecha").toString());
                                    }
                                }catch (JSONException je) {
                                    Log.e("ERROR_JSON_FECHA",je.getMessage());
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR_CONEXION_FECHA",error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }
}