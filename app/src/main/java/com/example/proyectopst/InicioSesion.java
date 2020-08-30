package com.example.proyectopst;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InicioSesion extends AppCompatActivity {
    EditText usuario, contraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);
        usuario = (EditText)findViewById(R.id.editTextInicioUsuario);
        contraseña = (EditText)findViewById(R.id.editTextInicioContraseña);
    }

    public void registro(View view){
        Intent registrar = new Intent(this,Registro.class);
        startActivity(registrar);
    }

    public void consultaUsuario(View view){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("https://undried-modes.000webhostapp.com/busqueda_usuario.php?cedula="+usuario.getText(),
                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Log.i("USUARIO",jsonObject.get("cedula").toString());
                        Log.i("CONTRASEÑA_BD",jsonObject.get("contraseña").toString());
                        Log.i("CONTRASEÑA_ING",contraseña.getText().toString());
                        if(contraseña.getText().toString().equals(jsonObject.get("contraseña"))){
                            Intent cambioVentana = new Intent(getApplicationContext(), MenuPrincipal1.class);
                            cambioVentana.putExtra("id_usuario",jsonObject.get("id_usuario").toString());
                            cambioVentana.putExtra("cedula",jsonObject.get("cedula").toString());
                            cambioVentana.putExtra("nombre",jsonObject.get("nombre").toString());
                            cambioVentana.putExtra("apellido",jsonObject.get("apellido").toString());
                            cambioVentana.putExtra("correo",jsonObject.get("correo").toString());
                            cambioVentana.putExtra("contraseña",jsonObject.get("contraseña").toString());
                            cambioVentana.putExtra("conteo",jsonObject.get("conteo_personas").toString());
                            startActivity(cambioVentana);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "Contraseña incorrecta.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException je) {
                        Log.e("ERROR_JSON",je.getMessage());
                        Toast.makeText(getApplicationContext(), "Usuario incorrecto.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR_CONEXION",error.getMessage());
                Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrecta.", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}