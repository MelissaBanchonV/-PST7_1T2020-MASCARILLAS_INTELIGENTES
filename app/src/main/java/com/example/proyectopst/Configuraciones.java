package com.example.proyectopst;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Map;

public class Configuraciones extends AppCompatActivity {
    CheckBox cbNombre, cbApellido, cbCorreo, cbContraseña;
    EditText etNombre, etApellido, etCorreo, etContraseña, etContraseña2;
    String cedula,nombre,apellido,correo,contraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuraciones);
        cbNombre = (CheckBox)findViewById(R.id.checkBoxNombre);
        cbApellido = (CheckBox)findViewById(R.id.checkBoxApellido);
        cbCorreo = (CheckBox)findViewById(R.id.checkBoxCorreo);
        cbContraseña = (CheckBox)findViewById(R.id.checkBoxContraseña);

        etNombre = (EditText)findViewById(R.id.editTextConfNombre);
        etApellido = (EditText)findViewById(R.id.editTextConfApellido);
        etCorreo = (EditText)findViewById(R.id.editTextConfCorreo);
        etContraseña = (EditText)findViewById(R.id.editTextConfContraseña);
        etContraseña2 = (EditText)findViewById(R.id.editTextConfContraseña2);

        etNombre.setEnabled(false);
        etApellido.setEnabled(false);
        etCorreo.setEnabled(false);
        etContraseña.setEnabled(false);
        etContraseña2.setEnabled(false);

        cbNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    etNombre.setEnabled(true);
                }
                else{
                    etNombre.setEnabled(false);
                }
            }
        });

        incializarCB(cbApellido,etApellido);


        cbCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    etCorreo.setEnabled(true);
                }
                else{
                    etCorreo.setEnabled(false);
                }
            }
        });

        cbContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    etContraseña.setEnabled(true);
                    etContraseña2.setEnabled(true);
                }
                else{
                    etContraseña.setEnabled(false);
                    etContraseña2.setEnabled(false);
                }
            }
        });

        Bundle obtenerDatos = getIntent().getExtras();
        cedula = obtenerDatos.getString("cedula");
        nombre = obtenerDatos.getString("nombre");
        apellido = obtenerDatos.getString("apellido");
        correo = obtenerDatos.getString("correo");
        contraseña = obtenerDatos.getString("contraseña");
    }

    public void incializarCB(CheckBox cb, final EditText et){
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    et.setEnabled(true);
                }
                else{
                    et.setEnabled(false);
                }
            }
        });
    }

    public void modificarDatos(View view){
        if (cbNombre.isChecked()) {
            nombre = etNombre.getText().toString();
        }

        if (cbApellido.isChecked()) {
            apellido = etApellido.getText().toString();
        }

        if (cbCorreo.isChecked()) {
            correo = etCorreo.getText().toString();
        }

        if (cbContraseña.isChecked()) {
            if(etContraseña.getText().toString().equals(etContraseña2.getText().toString())){
                contraseña = etContraseña.getText().toString();
            }else{
                Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            }
        }

        ejecutarServicio(cedula,nombre,apellido,correo,contraseña);
        etNombre.setText("");
        etApellido.setText("");
        etCorreo.setText("");
        etContraseña.setText("");
        etContraseña2.setText("");
    }

    private void ejecutarServicio(final String cedula,final String nombre, final String apellido, final String correo, final String contraseña){
        StringRequest strRq = new StringRequest(Request.Method.POST,
                "https://undried-modes.000webhostapp.com/editar_usuario.php",
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Registro exitoso.", Toast.LENGTH_SHORT).show();
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
                parametros.put("cedula",cedula);
                parametros.put("nombre",nombre);
                parametros.put("apellido",apellido);
                parametros.put("correo",correo);
                parametros.put("contraseña",contraseña);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strRq);
        //Toast.makeText(this, "Cierre sesión para actualizar.", Toast.LENGTH_SHORT).show();
    }

    public void regresar(View view){
        finish();
    }
}