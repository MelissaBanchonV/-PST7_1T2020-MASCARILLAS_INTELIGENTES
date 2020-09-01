package com.example.proyectopst;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {
    EditText cedula, nombre, apellido, correo, pass, passConf;
    String usuarioExiste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        cedula = (EditText)findViewById(R.id.editTextRegCedula);
        nombre = (EditText)findViewById(R.id.editTextRegNombre);
        apellido = (EditText)findViewById(R.id.editTextRegApellido);
        correo = (EditText)findViewById(R.id.editTextRegCorreo);
        pass = (EditText)findViewById(R.id.editTextRegPass);
        passConf = (EditText)findViewById(R.id.editTextRegPass);
    }

    /**
     * Acción del botón que registra al usuario, primero comprobando que este no exista en la base de datos.
     * Llama a la función ejecutarServicio.
     * @param view
     */
    public void registrar(View view){
        consultaUsuario();
        if ("falso".equals(usuarioExiste)){
            if(cedula.getText().toString().length() == 10 ){
                if (pass.getText().toString().length()>5){
                    if(pass.getText().toString().equals(passConf.getText().toString())){
                        ejecutarServicio("https://undried-modes.000webhostapp.com/ingreso_usuario.php");
                    }else{
                        Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "La contraseña debe tener más de 5 dígitos.", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "La cédula debe tener 10 dígitos.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Acción del botón que finaliza el activity registro y lleva nuevamente al inicio de sesión.
     * @param view
     */
    public void regresar(View view){
        Intent inicioSesion = new Intent(this, InicioSesion.class);
        startActivity(inicioSesion);
        finish();
    }

    /**
     * Función que envía los datos del usuario a la base de datos.
     * @param URL Correspondiente a la página junto con su archivo .php que contiene el query de insert en la tabla Usuarios.
     */
    private void ejecutarServicio(String URL){
        StringRequest strRq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Registro.this, "Registro exitoso.", Toast.LENGTH_SHORT).show();
                cedula.setText("");
                nombre.setText("");
                apellido.setText("");
                correo.setText("");
                pass.setText("");
                passConf.setText("");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("cedula",cedula.getText().toString());
                parametros.put("nombre",nombre.getText().toString());
                parametros.put("apellido",apellido.getText().toString());
                parametros.put("correo",correo.getText().toString());
                parametros.put("contraseña", pass.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strRq);
    }

    public void consultaUsuario(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("https://undried-modes.000webhostapp.com/busqueda_usuario.php?cedula="+cedula.getText(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        usuarioExiste = "verdadero";
                        Toast.makeText(getApplicationContext(), "Usuario ya registrado", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                usuarioExiste = "falso";
                Log.e("ERROR_CONEXION",error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}