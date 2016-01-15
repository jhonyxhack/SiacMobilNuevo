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

public class activity_login extends Activity implements View.OnClickListener {
    String Emailusuario = "", PasswordUsuario = "";


     boolean CerrarSession = false;
     boolean RecordarPassword = false;
     boolean ElusuarioExiste=false;

     URLServices ur = new URLServices();

     Clase_usuario usuario = new Clase_usuario();
    // ArrayList<HashMap<String, String>> jsonlist = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View boton_menu = findViewById(R.id.boton_iniciar);
        boton_menu.setOnClickListener(this);
        View boton_Reg = findViewById(R.id.boton_registrarse);
        boton_Reg.setOnClickListener(this);
        View boton_recordar = findViewById(R.id.boton_OlvideMipassword);
        boton_recordar.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        try {
            if (bundle != null) {
                if (bundle.containsKey("Cerrar") && bundle.containsKey(ur.VIENEDECERRARSESSION)) {
                    CerrarSession = Boolean.parseBoolean(bundle.getString("Cerrar"));
                    if (CerrarSession) {
                        new ProgressTask(activity_login.this).execute();
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
            if (v.getId() == findViewById(R.id.boton_iniciar).getId()) {
                Emailusuario = ((EditText) findViewById(R.id.txt_email)).getText().toString();
                PasswordUsuario = ((EditText) findViewById(R.id.txt_password)).getText().toString();
                new ProgressTask(activity_login.this).execute();
            }else if (v.getId() == findViewById(R.id.boton_OlvideMipassword).getId()) {
                Emailusuario= ((EditText) findViewById(R.id.txt_email)).getText().toString();

                if (!Emailusuario.equals("")) {
                    Toast.makeText(getApplicationContext(), "Si olvido su Password, le enviaremos un email para recordarselo.", Toast.LENGTH_SHORT).show();
                    RecordarPassword = true;

                    new ProgressTask(activity_login.this).execute();
                }else
                {
                    Toast.makeText(getApplicationContext(), "Escriba su correo electronico.", Toast.LENGTH_SHORT).show();
                }
            }else if (v.getId() == findViewById(R.id.boton_registrarse).getId()) {
                AbrirRegistrarse();
            }
        } catch (Exception e) {
            Log.e("Error", " Error al abrir los botones de Login " + e.toString());
        }

    }

    private void MandarAInicioLogeado() {
        try {
            Intent myIntent;
            Bundle bundle = new Bundle();
            bundle.putString(ur.VIENEABRIRSESION, ur.VIENEABRIRSESION);
            myIntent = new Intent(activity_login.this, MainActivity.class);
            myIntent.putExtras(bundle);
            startActivity(myIntent);
        }catch (Exception ex)
        {

        }

    }
    private void AbrirRegistrarse() {
        try {
            Intent myIntent;
            Bundle bundle = new Bundle();
            bundle.putString(ur.VIENEACREARUSUARIO, ur.VIENEACREARUSUARIO);
            myIntent = new Intent(activity_login.this, activity_registro.class);
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
                    this.dialog.setMessage("Iniciando Sessión");
                    this.dialog.show();
                }else
                {
                    this.dialog.setMessage("Procesando...");
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
            if ( usuario.Activo == true && CerrarSession==false && RecordarPassword==false   ) {
                Toast.makeText(getApplicationContext(), "Hola " + usuario.NombreCompleto + ", Bienvenido.", Toast.LENGTH_SHORT).show();
                MandarAInicioLogeado();
            }else if (CerrarSession==true) {
                CerrarSession=true;
                Toast.makeText(getApplicationContext(), "La Sessión fue cerrada correctamente ", Toast.LENGTH_SHORT).show();
                MandarAInicioLogeado();
            }else if (RecordarPassword==true && ElusuarioExiste==true ) {
                RecordarPassword=false;
                Toast.makeText(getApplicationContext(), "Le hemos enviado un correo electronico con sus Credenciales", Toast.LENGTH_SHORT).show();

            }else if (RecordarPassword==true && ElusuarioExiste==false ) {
                RecordarPassword=false;
                Toast.makeText(getApplicationContext(), "El usuario al que intenta recordar no existe o existe algun error del sistema", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "El usuario o Contraseña no existen. Intente nuevamente ", Toast.LENGTH_SHORT).show();
            }
        }

        protected Boolean doInBackground(final String... args) {

            JSONParser jParser = new JSONParser(); // get JSON data from URL

            String _url = ur.UrlSetSesion + "Usuario=" + Emailusuario + "&Password=" + PasswordUsuario +
                    "&NumTelefonico=" + usuario.NumeroMac;

            if (CerrarSession) {
                _url = _url + "&CerrarSession=True";

            }else if (RecordarPassword){
                _url = _url + "&"+ ur.RECORDARUSUARIO +"=" + ur.RECORDARUSUARIO+"&NumberPhone="+usuario.NumeroTelefono;

            }

            _url = ur.Clear_URL(_url);

            JSONArray json = jParser.getJSONFromUrl(_url);

            for (int i = 0; i < json.length(); i++) {
                try {
                    JSONObject c = json.getJSONObject(i);

                    HashMap<String, String> map = new HashMap<String, String>(); // Add child node to HashMap key & value

                    if (c.has(usuario.correo))
                    {
                        map.put(usuario.nombre_CAMPO, c.getString(usuario.nombre_CAMPO));
                        map.put(usuario.apellidos_CAMPO, c.getString(usuario.apellidos_CAMPO));
                        map.put(usuario.correo, c.getString(usuario.correo));

                        usuario.id_usuario = ur.ConvertToInteger(c.getString(usuario.CampoIdUusuario));
                        usuario.Activo = true;
                        usuario.NombreCompleto = c.getString(usuario.nombre_CAMPO) + " " + c.getString(usuario.apellidos_CAMPO);
                        usuario.NombredelaEmpresa = c.getString(usuario.NombreEmpresa_CAMPO);
                        usuario.EmailUsuario = c.getString(usuario.correo);

                        ElusuarioExiste = true;
                        // jsonlist.add(map);

                    }else if(c.has("NOEXISTE")) {
                        ElusuarioExiste = false;
                        usuario.Activo = false;
                        usuario.NombreCompleto = "Bienvenido";
                    }
                    else {
                        usuario.Activo = false;
                        usuario.NombreCompleto = "Bienvenido";
                        //usuario.YAPASO = false;
                        ElusuarioExiste = false;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
