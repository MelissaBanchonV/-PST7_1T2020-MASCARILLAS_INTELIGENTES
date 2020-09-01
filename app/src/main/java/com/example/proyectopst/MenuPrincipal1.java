package com.example.proyectopst;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MenuPrincipal1 extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    public static String id_usuario;
    String cedula,nombre,apellido,correo, pass;

    /**
     * Se obtienen los datos de inicio de sesión  mediante un bundle y se setean en el nav_view
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle obtenerDatos = getIntent().getExtras();
        id_usuario = obtenerDatos.getString("id_usuario");
        cedula = obtenerDatos.getString("cedula");
        nombre = obtenerDatos.getString("nombre");
        apellido = obtenerDatos.getString("apellido");
        correo = obtenerDatos.getString("correo");
        pass = obtenerDatos.getString("contraseña");

        NavigationView navigationView = findViewById(R.id.nav_view);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.textViewUsuarioNav);
        TextView navMail = (TextView) headerView.findViewById(R.id.textViewCorreoNav);

        navUsername.setText(obtenerDatos.getString("nombre")+" "+obtenerDatos.getString("apellido"));
        navMail.setText(obtenerDatos.getString("correo"));
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    /**
     * Añade los items al action bar si este está presente.
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal1, menu);
        return true;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * Acciones disponibles en el menú:
     * CONFIGURACIÓN
     * Acción del botón que inicializa el activity de configuraciones.
     * Envía los datos actuales del usuario por medio de un bundle.
     * CERRAR SESIÓN
     * Acción del botón que inicializa el activity de inicio de sesión.
     * Finaliza el activity actual.
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent conf = new Intent(getApplicationContext(),Configuraciones.class);
                conf.putExtra("cedula",cedula);
                conf.putExtra("nombre",nombre);
                conf.putExtra("apellido",apellido);
                conf.putExtra("correo",correo);
                conf.putExtra("contraseña", pass);
                startActivity(conf);
                return true;
            case R.id.action_logout:
                Intent ini = new Intent(getApplicationContext(),InicioSesion.class);
                startActivity(ini);
                finish();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}