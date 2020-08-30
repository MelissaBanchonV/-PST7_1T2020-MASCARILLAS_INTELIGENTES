package com.example.proyectopst.ui.AdministrarMascarillas;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectopst.MenuPrincipal1;
import com.example.proyectopst.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdministrarMascarillasFragment extends Fragment {

    private AdministracionViewModel administracionViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        administracionViewModel =
                ViewModelProviders.of(this).get(AdministracionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_administrar_mascarillas, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        final EditText codigo = root.findViewById(R.id.editTextCodigo);
        final TextView numMascarillas = root.findViewById(R.id.textViewNumMascarillas);
        consultaMascarilla(numMascarillas);
        final Button agregar = root.findViewById(R.id.buttonAgregarMasc);
        agregar.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarMascarilla(codigo,numMascarillas,"No");
            }
        });
        administracionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        final Button eliminar = root.findViewById(R.id.buttonEliminarMasc);
        eliminar.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarMascarilla(codigo,numMascarillas,"No");
            }
        });
        administracionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        final Button restaurar = root.findViewById(R.id.buttonRestablecerMasc);
        restaurar.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                restaurarMascarilla(codigo,numMascarillas);
            }
        });
        administracionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    public void agregarMascarilla(final EditText codigo, final TextView cantidad, final String actualizar){
        if(codigo.getText().toString().length()<4){
            Toast.makeText(getContext(), "El código no es válido.", Toast.LENGTH_SHORT).show();
        }else{
            JsonArrayRequest jsonArrayRequestVerif = new JsonArrayRequest("https://undried-modes.000webhostapp.com/consulta_mascarillas_totales.php?codigo_mascarilla="+codigo.getText(),
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            JSONObject jsonObject = null;
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    jsonObject = response.getJSONObject(i);
                                    if(jsonObject.get("codigo_mascarilla").toString().equals(codigo.getText().toString())){
                                        Toast.makeText(getContext(), "Mascarilla ya registrada.", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException je) {
                                    Log.e("ERROR_JSON",je.getMessage());
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ERROR_CONEXIONDB",error.getMessage());
                    if(actualizar.equals("No")){
                        StringRequest strRq = new StringRequest(Request.Method.POST,
                                "https://undried-modes.000webhostapp.com/ingreso_mascarilla.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if(actualizar.equals("No")){
                                            Toast.makeText(getContext(), "Registro exitoso.", Toast.LENGTH_SHORT).show();
                                            cantidad.setText(Integer.toString(Integer.parseInt(cantidad.getText().toString())+1));
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("ERROR_CONEXION_AGREGAR",error.getMessage());
                                Toast.makeText(getContext(),"Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> parametros = new HashMap<String, String>();
                                parametros.put("id_usuario",MenuPrincipal1.id_usuario);
                                parametros.put("codigo_mascarilla",codigo.getText().toString());
                                return parametros;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        requestQueue.add(strRq);
                    }
                }
            });
            RequestQueue requestQueueVerif = Volley.newRequestQueue(getContext());
            requestQueueVerif.add(jsonArrayRequestVerif);
        }
    }

    public void eliminarMascarilla(final EditText codigo, final TextView cantidad, final String actualizar){
        if(codigo.getText().toString().length()<4){
            Toast.makeText(getContext(), "El código no es válido.", Toast.LENGTH_SHORT).show();
        }else{
            JsonArrayRequest jsonArrayRequestVerif = new JsonArrayRequest("https://undried-modes.000webhostapp.com/consulta_datos_mascarilla.php?id_usuario="+ MenuPrincipal1.id_usuario,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            JSONObject jsonObject = null;
                            int contador =0;
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    jsonObject = response.getJSONObject(i);
                                    if(jsonObject.get("codigo_mascarilla").toString().equals(codigo.getText().toString())){
                                        contador++;
                                        Log.i("MASCARILLA_IGUAL",jsonObject.get("codigo_mascarilla").toString());

                                        StringRequest strRq2 = new StringRequest(Request.Method.POST,
                                                "https://undried-modes.000webhostapp.com/setear_datos.php",
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        if(actualizar.equals("No")){
                                                            Toast.makeText(getContext(), "Datos actualizados.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(getContext(),"Mascarilla no encontrada", Toast.LENGTH_SHORT).show();
                                            }
                                        }){
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> parametros = new HashMap<String, String>();
                                                parametros.put("codigo_mascarilla",codigo.getText().toString());
                                                parametros.put("id_usuario",MenuPrincipal1.id_usuario);
                                                return parametros;
                                            }
                                        };
                                        RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                                        requestQueue2.add(strRq2);

                                        StringRequest strRq = new StringRequest(Request.Method.POST,
                                                "https://undried-modes.000webhostapp.com/eliminar_mascarilla.php",
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        Toast.makeText(getContext(), "Mascarilla eliminada exitosamente.", Toast.LENGTH_SHORT).show();
                                                        cantidad.setText(Integer.toString(Integer.parseInt(cantidad.getText().toString())-1));
                                                    }
                                                }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(getContext(),"Mascarilla no encontrada", Toast.LENGTH_SHORT).show();
                                            }
                                        }){
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> parametros = new HashMap<String, String>();
                                                parametros.put("codigo_mascarilla",codigo.getText().toString());
                                                return parametros;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                                        requestQueue.add(strRq);
                                    }
                                    if(contador==0){
                                        Toast.makeText(getContext(), "Mascarilla no encontrada.", Toast.LENGTH_SHORT).show();
                                    }
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
            RequestQueue requestQueueVerif = Volley.newRequestQueue(getContext());
            requestQueueVerif.add(jsonArrayRequestVerif);
        }
    }

    public void restaurarMascarilla(final EditText codigo,final TextView cantidad){
        eliminarMascarilla(codigo,cantidad,"Si");
        agregarMascarilla(codigo,cantidad,"Si");
    }

    public void consultaMascarilla(final TextView cantidadMascarillas){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("https://undried-modes.000webhostapp.com/consulta_datos_mascarilla.php?id_usuario="+ MenuPrincipal1.id_usuario,
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


}