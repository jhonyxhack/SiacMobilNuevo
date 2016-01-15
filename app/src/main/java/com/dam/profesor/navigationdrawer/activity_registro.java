package com.dam.profesor.navigationdrawer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cuasmex.saec.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import Clases.Clase_usuario;
import Clases.JSONParser;
import Clases.URLServices;

public class activity_registro extends Activity implements View.OnClickListener {
    String NombreUsuario, EmpresaUsuario, Emailusuario, PasswordUsuario1, PasswordUsuario2 ;

    boolean Cerrar = false;

    URLServices ur = new URLServices();

    Clase_usuario usuario = new Clase_usuario();

    // ArrayList<HashMap<String, String>> jsonlist = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_registro);
        View boton_registrar = findViewById(R.id.boton_Reg_registrarse);
        boton_registrar.setOnClickListener(this);
        View boton_olvide = findViewById(R.id.boton_Reg_registrarse);
        boton_olvide.setOnClickListener(this);


        Bundle bundle = getIntent().getExtras();
        try {
            if (bundle != null) {
                if (bundle.containsKey("Cerrar") && bundle.containsKey(ur.VIENEDECERRARSESSION)) {
                    Cerrar = Boolean.parseBoolean(bundle.getString("Cerrar"));
                    if (Cerrar) {
                        new ProgressTask(activity_registro.this).execute();
                    }
                }else if(bundle.containsKey(ur.VIENEABRIRSESION))
                {

                }
            }
        } catch (Exception ex) {

        }

    }


    public void onClick(View v) {

        try {
            if (v.getId() == findViewById(R.id.boton_Reg_registrarse).getId()) {

                NombreUsuario = ((EditText) findViewById(R.id.txt_Reg_NombreCompleto)).getText().toString();
                Emailusuario = ((EditText) findViewById(R.id.txt_Reg_Email)).getText().toString();
                PasswordUsuario1 = ((EditText) findViewById(R.id.txt_Reg_Password1)).getText().toString();
                PasswordUsuario2 = ((EditText) findViewById(R.id.txt_Reg_Password2)).getText().toString();
                EmpresaUsuario = ((EditText) findViewById(R.id.txt_Reg_Empresa)).getText().toString();

                if( !Emailusuario.trim().equals("") && !NombreUsuario.trim().equals("") ) {
                    if (PasswordUsuario1.equals(PasswordUsuario2)) {
                        new ProgressTask(activity_registro.this).execute();
                    } else {
                        Toast.makeText(this, "Los Password no Coinciden", Toast.LENGTH_SHORT);
                    }
                }else
                {
                    Toast.makeText(this, "Debe establecer nombre completo y password", Toast.LENGTH_SHORT);
                }

            }
        } catch (Exception e) {
            Log.e("Error", " Error al abrir los botones de los cursos Gratis " + e.toString());
        }

    }

    private void AbrirTemaSielusuarioesValido() {
        try {
            Intent myIntent;
            Bundle bundle = new Bundle();
            bundle.putString(ur.VIENEABRIRSESION, ur.VIENEABRIRSESION);
                        myIntent = new Intent(activity_registro.this, MainActivity.class);
            myIntent.putExtras(bundle);

            startActivity(myIntent);
        }catch (Exception ex)
        {

        }

    }

    private class ProgressTask extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog dialog;
        private Activity activity; // private List<Message> messages;


        public ProgressTask(Activity activity) {
            this.activity = activity;
            context = activity;
            dialog = new ProgressDialog(context);
        }

       private Context context;

        protected void onPreExecute() {
            try{
                if(usuario.Activo==false)
                {
                    this.dialog.setMessage("Registrando usuario");
                    this.dialog.show();
                }else
                {
                    this.dialog.setMessage("Registrando Usuario");
                    this.dialog.show();
                }
            }catch (Exception ex)
            {

            }

        }


        @Override
        protected void onPostExecute(final Boolean success) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (usuario.Activo == true) {
                Toast.makeText(getApplicationContext(), "Hola " + usuario.NombreCompleto + ", Bienvenido.", Toast.LENGTH_SHORT).show();
                AbrirTemaSielusuarioesValido();
            }else if (Cerrar==true) {
                Toast.makeText(getApplicationContext(), "La Sessión fue cerrada correctamente ", Toast.LENGTH_SHORT).show();
                AbrirTemaSielusuarioesValido();
            }
            else {
                Toast.makeText(getApplicationContext(), "El usuario o Contraseña no existen. Intente nuevamente ", Toast.LENGTH_SHORT).show();
            }
        }

        protected Boolean doInBackground(final String... args) {

            JSONParser jParser = new JSONParser(); // get JSON data from URL

            String _url = ur.UrlCreateUser + ur.CREARUSUARIO + "=" + ur.CREARUSUARIO + "&NombreCompleto=" + NombreUsuario + "&Usuario=" + Emailusuario + "&Password1=" + PasswordUsuario1 +
                    "&Password2=" + PasswordUsuario2 + "&Empresa=" + EmpresaUsuario + "&NumTelefonico=" + usuario.NumeroMac;

            if (Cerrar) {
                _url = _url + "&RecordarPassword=True";
            }

            _url = ur.Clear_URL(_url);

            JSONArray json = jParser.getJSONFromUrl(_url);

            for (int i = 0; i < json.length(); i++) {
                try {
                    JSONObject c = json.getJSONObject(i);

                    HashMap<String, String> map = new HashMap<String, String>(); // Add child node to HashMap key & value

                    if (c.has(usuario.id_session)) {
                        usuario.NumeroSessionCompartida = ur.ConvertToInteger(c.getString(usuario.id_session));
                        usuario.Activo = true;
                        usuario.id_usuario = ur.ConvertToInteger(c.getString(usuario.CampoIdUusuario));
                        usuario.NombreCompleto = c.getString(usuario.nombre_CAMPO);
                        usuario.EmailUsuario = c.getString("hora"); // Del camcpo hora sacamos el email Temporalmente
                    } else {
                        usuario.NumeroSessionCompartida = 0;
                        usuario.Activo = false;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
