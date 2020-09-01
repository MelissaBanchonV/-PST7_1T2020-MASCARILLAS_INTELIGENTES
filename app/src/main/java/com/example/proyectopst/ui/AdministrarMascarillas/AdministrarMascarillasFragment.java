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
                eliminarRestaurarMascarilla(codigo,numMascarillas,"No");
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

    /**
     * Agrega una mascarilla a la base de datos.
     * Verifica si el código ingresado ya se encuentra registrado.
     * @param codigo EditText donde el usuario ingresa el código de la mascarilla.
     * @param cantidad TextView donde se setea la cantidad de mascarillas que tiene actualmente el usuario después de agregar una mascarilla.
     * @param actualizar String que indica si la función es utilizada para solo agregar o resetear la mascarilla.
     */
    public void agregarMascarilla(final EditText codigo, final TextView cantidad, final String actualizar){
        if(codigo.getText().toString().length()<3){
            Toast.makeText(getContext(), "El código no es válido.", Toast.LENGTH_SHORT).show();
        }else{
            Log.i("VERIFICACION_1",codigo.getText().toString());
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
                                        codigo.setText("");
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

                        StringRequest strRq = new StringRequest(Request.Method.POST,
                                "https://undried-modes.000webhostapp.com/ingreso_mascarilla.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if(actualizar.equals("No")){
                                            Toast.makeText(getContext(), "Registro exitoso.", Toast.LENGTH_SHORT).show();
                                            cantidad.setText(Integer.toString(Integer.parseInt(cantidad.getText().toString())+1));
                                            codigo.setText("");
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("ERROR_CONEXION_AGREGAR",error.getMessage());
                                Toast.makeText(getContext(),"Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                                codigo.setText("");
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> parametros = new HashMap<String, String>();
                                parametros.put("id_usuario",MenuPrincipal1.id_usuario);
                                Log.i("VERIFICACION_ANTES",codigo.getText().toString());
                                parametros.put("codigo_mascarilla",codigo.getText().toString());
                                return parametros;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        requestQueue.add(strRq);
                    }
            });
            RequestQueue requestQueueVerif = Volley.newRequestQueue(getContext());
            requestQueueVerif.add(jsonArrayRequestVerif);
        }
    }

    /**
     Elimina una mascarilla a la base de datos.
     * Verifica si el código ingresado ya se encuentra registrado.
     * Utiliza el método agregarMascarilla para volverla a ingresar en la base de datos.
     * @param codigo EditText donde el usuario ingresa el código de la mascarilla.
     * @param cantidad TextView donde se setea la cantidad de mascarillas que tiene actualmente el usuario después de borrar una mascarilla.
     * @param actualizar String que indica si la función es utilizada solo para eliminar o restaurar las estadísticas de la mascarilla.
     */
    public void eliminarRestaurarMascarilla(final EditText codigo, final TextView cantidad, final String actualizar){
        if(codigo.getText().toString().length()<3){
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
                                    Log.i("ID_MASCBD",jsonObject.get("codigo_mascarilla").toString());
                                    Log.i("ID_MASCBD2","Hay "+codigo.getText().toString());
                                    if(jsonObject.get("codigo_mascarilla").toString().equals(codigo.getText().toString())){
                                        contador++;
                                        Log.i("MASCARILLA_IGUAL",jsonObject.get("codigo_mascarilla").toString());

                                        /*----------------------------------------ELIMINAR DATOS-----------------------------------------*/
                                        StringRequest strRq = new StringRequest(Request.Method.POST,
                                                "https://undried-modes.000webhostapp.com/eliminar_mascarilla.php",
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        if(actualizar.equals("No")){
                                                            Toast.makeText(getContext(), "Mascarilla eliminada exitosamente.", Toast.LENGTH_SHORT).show();
                                                            cantidad.setText(Integer.toString(Integer.parseInt(cantidad.getText().toString())-1));
                                                        }/*else{
                                                            agregarMascarilla(codigo,cantidad,"Si");
                                                            Toast.makeText(getContext(), "Mascarilla restaurada.", Toast.LENGTH_SHORT).show();
                                                        }*/
                                                        //codigo.setText("");
                                                    }
                                                }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(getContext(),"No se pudo eliminar la mascarilla.", Toast.LENGTH_SHORT).show();
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
                                } catch (JSONException je) {
                                    Log.e("ERROR_JSON",je.getMessage());
                                }
                            }
                            if(contador==0){
                                Log.e("CONT","CONTADOR VACIO");
                                Toast.makeText(getContext(), "Mascarilla no encontrada.", Toast.LENGTH_SHORT).show();
                                codigo.setText("");
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
            if(actualizar.equals("Si")){
                agregarMascarilla(codigo,cantidad,"Si");
                Toast.makeText(getContext(), "Mascarilla restaurada.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Elimina y vuelve a agregar la mascarilla para eliminar cualquier registro de esta.
     * Utiliza el método eliminarRestaurarMascarilla.
     * @param codigo EditText donde el usuario ingresa el código de la mascarilla.
     * @param cantidad TextView donde se setea la cantidad de mascarillas que tiene actualmente el usuario.
     */
    public void restaurarMascarilla(final EditText codigo,final TextView cantidad){
        eliminarRestaurarMascarilla(codigo,cantidad,"Si");
    }

    /**
     * Obtiene de la base de datos la cantidad de mascarillas que posee el usuario.
     * @param cantidadMascarillas TextView donde se setea la cantidad de mascarillas que tiene actualmente el usuario.
     */
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