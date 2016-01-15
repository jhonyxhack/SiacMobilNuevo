package com.dam.profesor.navigationdrawer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Clases.Clase_Cursos;
import Clases.Clase_tema;
import Clases.Clase_usuario;
import Clases.URLServices;

import com.cuasmex.saec.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentAreas.OnFragmentInteractionListener,
        FragmentCursos.OnFragmentInteractionListener, FragmentTemas.OnFragmentInteractionListener {

    Clase_usuario usuario = new Clase_usuario();
    URLServices ur = new URLServices();
    Clase_Cursos Curso = new Clase_Cursos();
    Clase_tema Tema = new Clase_tema();

    boolean FragmentTransaction = false;
    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Context con = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Buscando un curso o programa de entrenamiento", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                OpcionBuscar(con);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        try {

            usuario.NumeroMac = ur.getMacAddress(this);
            usuario.NumeroTelefono = ur.getNumberPhone(this);

            Bundle bundle = getIntent().getExtras();

            if (bundle != null) {
                if (bundle.containsKey(Curso.nombre_area) && bundle.containsKey(ur.VIENEDEAREA)) {

                    CargarCursosDelArea(bundle.getString(Curso.nombre_area));

                } else if (bundle.containsKey(Curso.nombre_area) && bundle.containsKey(Curso.nombre_curso) && bundle.containsKey(ur.VIENEDECURSOS)) {
                    setTitle(Curso.nombre_curso);
                    CargarTemasdelCurso(bundle.getString(Curso.nombre_area),
                            bundle.getString(Curso.nombre_curso),"", bundle.getString(ur.IMAGENDELCURSO), bundle.getDouble(ur.COSTODELCURSO),0);

                } else if (bundle.containsKey(Tema.nombre_area) && bundle.containsKey(Tema.nombre_curso) &&
                        bundle.containsKey(Tema.nombre_tema) && bundle.containsKey(ur.VIENEDETEMAS)) {

                    setTitle(Tema.nombre_curso);

                    Toast.makeText(this, bundle.getString(Tema.nombre_tema), Toast.LENGTH_SHORT).show();

                    CargarTemasdelCurso(bundle.getString(Tema.nombre_area),
                            bundle.getString(Tema.nombre_curso), bundle.getString(ur.VIDEODELTEMA),
                            bundle.getString(ur.IMAGENDELCURSO), bundle.getDouble(ur.COSTODELCURSO),bundle.getInt(ur.SIGUIENTETEMA));



                } else {
                    CargarAreasInicio();
                }
            } else {
                CargarAreasInicio();
            }

            CambiarNombreBotonCerrarSession();

        } catch (Exception ex) {
            CargarAreasInicio();
            Log.e("Error al iniciar", ex.toString());
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Carga la Opcion de Obtener Areas
            fragment = new FragmentAreas();
            FragmentTransaction = true;
        } else if (id == R.id.nav_gallery) {

            if (usuario.Activo) {
                fragment = new FragmentCursos(ur.MISCURSOS,"");
                FragmentTransaction = true;
            } else {
                startActivity(new Intent(this, activity_login.class));
                Log.i("NavigationDrawer", "Entro a la Opcion Login");
            }

        } else if (id == R.id.nav_slideshow) {

            OpcionBuscar(this);


        } else if (id == R.id.nav_manage) {
            fragment = new FragmentCursos(ur.NUEVOSCURSOS,"");
            FragmentTransaction = true;
            Log.i("NavigationDrawer", "Entro en opción " + ur.NUEVOSCURSOS);

        } else if (id == R.id.nav_CursosGratuitos) {
            fragment = new FragmentCursos(ur.CURSOSGRATIS,"");
            FragmentTransaction = true;
            Log.i("NavigationDrawer", "Entro en opción " + ur.CURSOSGRATIS);

        } else if (id == R.id.nav_share) {

            //          SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            CompatirApp();

            /*Log.i("NavigationDrawer", "Opción 1: " + pref.getBoolean("opcion1", false));
            Log.i("NavigationDrawer", "Opción 2: " + pref.getString("opcion2", ""));
            Log.i("NavigationDrawer", "Opción 3: " + pref.getString("opcion3", ""));
*/

            Log.i("NavigationDrawer", "Entro en opción Compartir");

        } else if (id == R.id.preferencias) {

            startActivity(new Intent(this, Preferencias.class));
            Log.i("NavigationDrawer", "Entro en opción preferencias");

        } else if (id == R.id.BotonIniciarSesion) {

            if (usuario.Activo == false) {
                startActivity(new Intent(this, activity_login.class));
                Log.i("NavigationDrawer", "Entro a la Opcion Login");
            } else {
                Intent myIntent;
                Bundle bundle = new Bundle();
                bundle.putString("Cerrar", "true");
                bundle.putString(ur.VIENEDECERRARSESSION, ur.VIENEDECERRARSESSION);
                myIntent = new Intent(this, activity_login.class);
                myIntent.putExtras(bundle);
                startActivity(myIntent);
                Log.i("NavigationDrawer", "Entro a la Opcion Cerrar Session");
            }

        }
        if (FragmentTransaction) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, fragment)
                    .commit();

            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean CargarAreasInicio() {
        try {

            fragment = new FragmentAreas();
            FragmentTransaction = true;
            if (FragmentTransaction) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_main, fragment)
                        .commit();

                // item.setChecked(true);
                getSupportActionBar().setTitle(R.string._MP_areas);
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

            return true;
        } catch (Exception ex) {

        }
        return FragmentTransaction;
    }

    private boolean CargarCursosDelArea(String area) {
        try {

            fragment = new FragmentCursos(area,"");
            FragmentTransaction = true;
            if (FragmentTransaction) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_main, fragment)
                        .commit();

                getSupportActionBar().setTitle(area);
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

            return true;
        } catch (Exception ex) {

        }
        return FragmentTransaction;
    }

    private boolean CargarCursosBuscados(String busqueda) {
        try {

            fragment = new FragmentCursos(true, busqueda);
            FragmentTransaction = true;
            if (FragmentTransaction) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_main, fragment)
                        .commit();

                getSupportActionBar().setTitle(R.string._MP_buscarCurso);
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);


            return true;
        } catch (Exception ex) {

        }
        return FragmentTransaction;
    }

    private boolean CargarTemasdelCurso(String area, String curso, String video, String imagen, double costo, int siguientevideo) {
        try {

            fragment = new FragmentTemas(area, curso, video, imagen, costo,siguientevideo);
            FragmentTransaction = true;
            if (FragmentTransaction) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_main, fragment)
                        .commit();

                getSupportActionBar().setTitle(area);
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);


            return true;
        } catch (Exception ex) {

        }
        return FragmentTransaction;
    }


    private void CompatirApp() {
        try {
            URLServices ur = new URLServices();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, ur._TXTInvitacion);
            intent.putExtra(Intent.EXTRA_TEXT, ur._URLSAECGOOGLE);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent, "Compartir con.."));
        } catch (Exception ex) {

        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    /**
     * Create and return an example alert dialog with an edit text box.
     */
    private void OpcionBuscar(Context context) {
        try {
            getSupportActionBar().setTitle(R.string._MP_buscarCurso);
            Log.i("NavigationDrawer", "Entro en opción BUSCAR");

            final String TAG = "DialogActivity";
            int DLG_EXAMPLE1 = 0;
            int TEXT_ID = 0;
            final boolean[] BUSCAR = {false};
            final String[] _busqueda = {""};

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Hola " + usuario.NombreCompleto);
            builder.setMessage("¿Que buscas Hoy?");

            // Use an EditText view to get user input.
            final EditText input = new EditText(this);
            input.setId(TEXT_ID);
            builder.setView(input);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = input.getText().toString();
                    Log.d("Entra a buscar", "User name: " + value);
                    BUSCAR[0] = true;
                    _busqueda[0] = value;
                    // new ProgressTask(activity_cursos.this).execute();
                    CargarCursosBuscados(_busqueda[0]);
                    dialog.cancel();
                    return;
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    return;
                }
            });

            AlertDialog alert1 = builder.create();
            alert1.show();
            // return builder.create();
        } catch (Exception ex) {

        }

    }

    public void CambiarNombreBotonCerrarSession() {
        try {

            if (usuario.Activo) {

            TextView _nombreUsuario = (TextView) findViewById(R.id.txt_nombreUsuario);
            TextView _nombreempresa = (TextView) findViewById(R.id.txt_nombreempresa);

            _nombreUsuario.setText(usuario.NombreCompleto);
            _nombreempresa.setText(usuario.EmailUsuario); // Temporalmente tendra el Email

                NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
                Menu m = navView.getMenu();
                MenuItem mi = m.findItem(R.id.BotonIniciarSesion);
                mi.setTitle(R.string._MP_Cerrarsesion);
                mi.setIcon(R.drawable.logout);

            }
        } catch (Exception ex) {
             Log.e("Error Cambiar ",ex.toString());
        }

    }
}
