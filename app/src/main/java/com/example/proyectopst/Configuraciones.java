package com.example.proyectopst;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Configuraciones extends AppCompatActivity {
    CheckBox cbNombre, cbApellido, cbCorreo, cbPass;
    EditText etNombre, etApellido, etCorreo, etPass, etPass2;
    String cedula,nombre,apellido,correo, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuraciones);

        cbNombre = (CheckBox)findViewById(R.id.checkBoxNombre);
        cbApellido = (CheckBox)findViewById(R.id.checkBoxApellido);
        cbCorreo = (CheckBox)findViewById(R.id.checkBoxCorreo);
        cbPass = (CheckBox)findViewById(R.id.checkBoxPass);

        etNombre = (EditText)findViewById(R.id.editTextConfNombre);
        etApellido = (EditText)findViewById(R.id.editTextConfApellido);
        etCorreo = (EditText)findViewById(R.id.editTextConfCorreo);
        etPass = (EditText)findViewById(R.id.editTextConfPass);
        etPass2 = (EditText)findViewById(R.id.editTextConfPass2);

        etNombre.setEnabled(false);
        etApellido.setEnabled(false);
        etCorreo.setEnabled(false);
        etPass.setEnabled(false);
        etPass2.setEnabled(false);

        incializarCB(cbNombre,etNombre);
        incializarCB(cbApellido,etApellido);
        incializarCB(cbCorreo,etCorreo);
        incializarCB(cbApellido,etApellido);

        cbPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    etPass.setEnabled(true);
                    etPass2.setEnabled(true);
                }
                else{
                    etPass.setEnabled(false);
                    etPass2.setEnabled(false);
                }
            }
        });

        Bundle obtenerDatos = getIntent().getExtras();
        cedula = obtenerDatos.getString("cedula");
        nombre = obtenerDatos.getString("nombre");
        apellido = obtenerDatos.getString("apellido");
        correo = obtenerDatos.getString("correo");
        pass = obtenerDatos.getString("contraseña");
    }

    /**
     * Permite al usuario accionar los EditText para llenar el formulario.
     * @param cb Checkbox a marcar.
     * @param et EditText que debe activarse.
     */
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

    /**
     * Acción del botón que inicializa el método para enviar los datos.
     * Verifica qué campos han sido modificados y cambia los datos.
     * @param view
     */
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

        if (cbPass.isChecked()) {
            if(etPass.getText().toString().equals(etPass2.getText().toString())){
                pass = etPass.getText().toString();
            }else{
                Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            }
        }

        modificarDatos(cedula,nombre,apellido,correo, pass);
        etNombre.setText("");
        etApellido.setText("");
        etCorreo.setText("");
        etPass.setText("");
        etPass2.setText("");
    }

    /**
     * Envía la información a la base de datos para actualizarlos.
     * @param cedula
     * @param nombre
     * @param apellido
     * @param correo
     * @param pass
     */
    private void modificarDatos(final String cedula, final String nombre, final String apellido, final String correo, final String pass){
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
                parametros.put("pass",pass);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strRq);
    }

    public void regresar(View view){
        finish();
    }
}