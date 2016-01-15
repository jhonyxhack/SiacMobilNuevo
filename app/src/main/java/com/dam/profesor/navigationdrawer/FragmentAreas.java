package com.dam.profesor.navigationdrawer;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cuasmex.saec.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Clases.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentAreas.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FragmentAreas extends Fragment {

    Clase_usuario usuario = new Clase_usuario();
   // private int contad=0;
    ArrayList<Clase_area> listaareas = new ArrayList<Clase_area>();

    URLServices ur = new URLServices();

    private OnFragmentInteractionListener mListener;

    public FragmentAreas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_camera, container, false);


        // new ProgressTask(MainActivity.this).execute();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;

            new ProgressTask(FragmentAreas.this).execute();

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    ListView lv;

    private class ProgressTask extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog dialog;
        private Fragment activity; // private List<Message> messages;

        public ProgressTask(Fragment activity) {
            try {
                this.activity = activity;
                context = activity.getContext();
                dialog = new ProgressDialog(context);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        private Context context;

        protected void onPreExecute() {
            this.dialog.setMessage("Obteniendo Programas de Entrenamiento");
            this.dialog.show();
        }


        @Override
        protected void onPostExecute(final Boolean success) {

            try {

                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                lv = (ListView) getActivity().findViewById(R.id.ListaAreas);
                lv.setAdapter(new AreaAdapter(context, R.layout.list_xml, listaareas));
                // lv = getListView();
                //set listener onclick
                lv.setOnItemClickListener(new OnCursosClick());

                if ( usuario.Activo ) {
                    CambiarNombreBotonCerrarSession();
                }
            } catch (Exception ex) {
                Log.e("onPostExecute","Error al finalizar le Proceso de Ejecutar areas");
            }

        }

        private void CambiarNombreBotonCerrarSession() {
            try {

                if (usuario.Activo) {

                    TextView _nombreUsuario = (TextView) getActivity(). findViewById(R.id.txt_nombreUsuario);
                    TextView _nombreempresa = (TextView) getActivity().findViewById(R.id.txt_nombreempresa);

                    _nombreUsuario.setText(usuario.NombreCompleto);
                    _nombreempresa.setText(usuario.EmailUsuario); // Temporalmente tendra el Email

                    NavigationView navView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                    Menu m = navView.getMenu();
                    MenuItem mi = m.findItem(R.id.BotonIniciarSesion);
                    mi.setTitle(R.string._MP_Cerrarsesion);
                    mi.setIcon(R.drawable.logout);

                }
            } catch (Exception ex) {
                Log.e("Error Cambiar ",ex.toString());
            }

        }


        protected Boolean doInBackground(final String... args) {
            //contad=0;
            try {

                JSONParser jParser = new JSONParser(); // get JSON data from URL
                JSONArray json = jParser.getJSONFromUrl(ur.UrlGetAreas);

                for (int i = 0; i < json.length(); i++) {
                    try {
                        JSONObject c = json.getJSONObject(i);

                        Clase_area Areas = new Clase_area();

                        HashMap<String, String> map = new HashMap<String, String>(); // Add child node to HashMap key & value
                        map.put(Areas.nombre, c.getString(Areas.nombre));
                        map.put(Areas.descripcion, c.getString(Areas.descripcion));
                        map.put(Areas.imagen, c.getString(Areas.imagen));

                        Areas.NombreDelaImagenSola = ur.Acentos(c.getString(Areas.imagen));
                        Areas.imagen = Areas.URLImages + ur.Acentos(c.getString(Areas.imagen));
                        Areas.nombre = c.getString(Areas.nombre);
                        Areas.descripcion = ur.RecortarString(c.getString(Areas.descripcion), 150);


                        // jsonlist.add(map);
                        listaareas.add(Areas);


                    } catch (JSONException e) {
                        Log.e("Obtener Areas", e.toString());
                    }
                }

                if(usuario.Activo == false) {
                    ValidarSession();
                }

                if (usuario.Activo == true) {
                   GetSuscripciones();
                }

            } catch (Exception ex) {
                Log.e("Obtener Areas",ex.toString());
            }

            return null;
        }

        private void ValidarSession() {
            try {
                JSONParser jParser = new JSONParser(); // get JSON data from URL
                String _url = ur.UrlGetSesion + "NumeroMac=" + usuario.NumeroMac;
                _url = ur.Clear_URL(_url);
                JSONArray json = jParser.getJSONFromUrl(_url);
                for (int i = 0; i < json.length(); i++) {
                    try {
                        JSONObject c = json.getJSONObject(i);
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
                        // e.printStackTrace();
                        Log.e("ValidarSession", e.toString());
                    }
                }

            } catch (Exception e) {
            }
        }

        private void GetSuscripciones() {
            try {
                JSONParser jParser = new JSONParser(); // get JSON data from URL
                String _url = ur.UrlGetSusCriptionUser + "Usuario=" + usuario.id_usuario;
                _url = ur.Clear_URL(_url);
                JSONArray json = jParser.getJSONFromUrl(_url);
                for (int i = 0; i < json.length(); i++) {
                    try {
                        JSONObject c = json.getJSONObject(i);
                        Clase_Cursos curso = new Clase_Cursos();

                        if (c.has(curso.nombre_curso)) {
                            curso.nombre_area = c.getString(curso.ID_AREA);
                            curso.nombre_curso = c.getString(curso.nombre_curso);
                            curso.ID_CURSONumerico = ur.ConvertToInteger(c.getString(curso.ID_CURSO));
                            curso.ID_CURSO = c.getString(curso.ID_CURSO);
                            curso.ID_AREA = c.getString(curso.ID_AREA);
                            curso.ElUsuarioSiEstaSuscrito = true;
                            usuario.ListaSuscripciones.add(curso);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
            }
        }
    }

    private class AreaAdapter extends ArrayAdapter<Clase_area> {
        LayoutInflater vi;
        private ArrayList<Clase_area> items;

        public AreaAdapter(Context context, int textViewResourceId, ArrayList<Clase_area> items) {
            super(context, textViewResourceId, items);
            this.items = items;
            vi   = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                v = vi.inflate(R.layout.list_xml, null);
            }

            Clase_area area = items.get(position);

            if (area != null  ) {

                try {

                    LinearLayout rl = (LinearLayout)v.findViewById(R.id.Renglon);

                    ur.ChangueColorRow(getContext(),rl,position);

                    TextView txt_tituloArea = (TextView) v.findViewById(R.id.NombrCurso);
                    TextView txt_descripcionArea = (TextView) v.findViewById(R.id.DescripcionCurso);
                    TextView txt_nombrearea= (TextView) v.findViewById(R.id.txtNombredelArea);

                    if (txt_tituloArea != null) {
                        txt_tituloArea.setText(area.nombre);
                    }
                    if (txt_descripcionArea != null) {
                        txt_descripcionArea.setText(area.descripcion);
                    }
                    if (txt_nombrearea != null) {
                        txt_nombrearea.setText("");
                        txt_nombrearea.setVisibility(View.INVISIBLE);
                    }
                    area.imagen = ur.Clear_URL(area.imagen);

                    ImageView slidingimage = (ImageView) v.findViewById(R.id.ImagenCurso);

                    if (area.imagen != "" && area.ImagenDescargada == false) {

                        area.ImagenDescargada = false;
                        if(area.NombreDelaImagenSola.equals(""))
                        {
                            slidingimage.setImageResource(R.drawable.icon_areas);
                        }else
                        {
                            new DownloadImageTask(slidingimage,getContext(),area.NombreDelaImagenSola).execute(area.imagen);
                        }
                        // Log.i("NameImage", "Veces Entrado " + contad +" Imagen" + area.imagen);
                        //contad++;
                    }


                } catch (Exception e) {
                    Log.e("Error ", " Error al asiganar la imagen : " + area.imagen + " e: " + e.toString());
                }

            }
            return v;
        }
    }

    private class OnCursosClick implements AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            //LinearLayout linear = (LinearLayout) arg1;
            //String area = (String) ((TextView) linear.findViewById(R.id.NombrCurso)).getText();
            //Toast.makeText(getApplicationContext(), "Clicked: " Intent myIntent;+ country +" position "+arg2 , Toast.LENGTH_SHORT).show();
            Intent myIntent;
            Clase_area Area = listaareas.get(arg2);
            Clase_Cursos curso = new Clase_Cursos();
            Bundle bundle = new Bundle();
            bundle.putString(curso.nombre_area, Area.nombre);
            bundle.putString(ur.VIENEDEAREA,ur.VIENEDEAREA );
            myIntent = new Intent(getActivity(), MainActivity.class);

            myIntent.putExtras(bundle);
            //startActivityForResult(myIntent, 0);
            startActivity(myIntent);

            }
    }
}
