package com.dam.profesor.navigationdrawer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Clases.Clase_Cursos;
import Clases.Clase_usuario;
import Clases.DownloadImageTask;
import Clases.JSONParser;
import Clases.URLServices;

import com.cuasmex.saec.R;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentCursos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FragmentCursos extends Fragment {

    String nombre_area = "";

    ArrayList<Clase_Cursos> listacursos = new ArrayList<Clase_Cursos>();

    URLServices ur = new URLServices();

    //ArrayList<HashMap<String, String>> jsonlist = new ArrayList<HashMap<String, String>>();
    Clase_usuario usuario = new Clase_usuario();

    private boolean ObtenerMisCursos = false;
    private boolean ObtenerNuevosCursos = false;
    private  boolean ObtenerCursosGratis=false;


    public FragmentCursos()
    {
 // Empty
    }
    private OnFragmentInteractionListener mListener;


    private String y;
    public FragmentCursos(String _area, String x) {
        super();

        if (_area.equals(ur.MISCURSOS)) {
            nombre_area = _area;
            ObtenerMisCursos = true;

        }else if (_area.equals(ur.NUEVOSCURSOS)) {
            nombre_area = ur.NUEVOSCURSOS;
            ObtenerNuevosCursos = true;

        }else if (_area.equals(ur.CURSOSGRATIS)) {
            nombre_area = ur.CURSOSGRATIS;
            ObtenerCursosGratis = true;

        } else {
            ObtenerMisCursos = false;
            nombre_area = _area;
        }
   y = x;

    }

    private String _busqueda = "";
    private boolean BUSCAR = false;

    public FragmentCursos(boolean Buscar, String cadenabusqueda) {
        // Required empty public constructor
        BUSCAR = Buscar;
        _busqueda = cadenabusqueda;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_galeria, container, false);
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
            new ProgressTask(FragmentCursos.this).execute();
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
            this.dialog.setMessage("Obteniendo Cursos");
            this.dialog.show();
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            try {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                lv = (ListView) getActivity().findViewById(R.id.ListaCursos);
                lv.setAdapter(new CursoAdapter(context, R.layout.list_xml, listacursos));
                lv.setOnItemClickListener(new OnCursosClick());
            } catch (Exception ex) {
                Log.e("onPostExecute", "Error al finalizar le Proceso de Ejecutar Obtener Cursos");
            }
        }


        protected Boolean doInBackground(final String... args) {

            JSONParser jParser = new JSONParser(); // get JSON data from URL

            String _url = ur.UrlGetCursos + nombre_area;

            if (BUSCAR == true) {
                _url = _url + "&Buscar=" + _busqueda;
                BUSCAR = false;
            } else if (ObtenerMisCursos) {
                _url = _url + "&MisCursos=" + ur.MISCURSOS + "&Usuario=" + usuario.id_usuario;
            }else if (ObtenerNuevosCursos) {
                _url = _url + "&"+ ur.NUEVOSCURSOS +"=" + ur.NUEVOSCURSOS ;
            }else if(ObtenerCursosGratis)
            {
                _url = _url + "&"+ ur.CURSOSGRATIS +"=" + ur.CURSOSGRATIS ;
            }


            _url = ur.Clear_URL(_url);

            JSONArray json = jParser.getJSONFromUrl(_url);



            for (int i = 0; i < json.length(); i++) {
                try {
                    JSONObject c = json.getJSONObject(i);

                    Clase_Cursos curso = new Clase_Cursos();

                    HashMap<String, String> map = new HashMap<String, String>(); // Add child node to HashMap key & value

                    if(c.has(curso.nombre_curso)) {
                        map.put(curso.nombre_curso, c.getString(curso.nombre_curso));
                        map.put(curso.nombre_area, c.getString(curso.nombre_area));
                        map.put(curso.imagen_curso, c.getString(curso.imagen_curso));

                        curso.NOmbreImagenSolacurso =   c.getString(curso.imagen_curso);
                        curso.imagen_curso = curso.URLImages + c.getString(curso.imagen_curso);

                        curso.nombre_curso = c.getString(curso.nombre_curso);
                        curso.ID_CURSONumerico = ur.ConvertToInteger(c.getString(curso.ID_CURSO));
                        curso.ID_CURSO = c.getString(curso.ID_CURSO);
                        curso.nombre_area = c.getString(curso.nombre_area);
                        curso.DescripcionCurso = ur.RecortarString(c.getString(curso.DescripcionCurso), 150);
                        //jsonlist.add(map);

                        curso.Costo_Curso =  ur.ConvertToDouble(c.getString(curso.costo_CAMPO));

                        listacursos.add(curso);
                        curso.HAYCURSOS = true;
                    }else
                    {
                        curso.HAYCURSOS = false;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


    }

    private class CursoAdapter extends ArrayAdapter<Clase_Cursos> {

        private ArrayList<Clase_Cursos> items;

        public CursoAdapter(Context context, int textViewResourceId, ArrayList<Clase_Cursos> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_xml, null);
            }
            Clase_Cursos Cursos = items.get(position);

            if (Cursos != null) {
                try {


                    LinearLayout rl = (LinearLayout)v.findViewById(R.id.Renglon);

                    ur.ChangueColorRow(getContext(),rl,position);

                    TextView titulo_curso = (TextView) v.findViewById(R.id.NombrCurso);
                    TextView txt_DescripcionCurso = (TextView) v.findViewById(R.id.DescripcionCurso);
                    TextView txt_nombrearea = (TextView) v.findViewById(R.id.txtNombredelArea);
                    if (titulo_curso != null) {
                        titulo_curso.setText(Cursos.nombre_curso);
                    }
                    if (txt_DescripcionCurso != null) {
                        txt_DescripcionCurso.setText(Cursos.DescripcionCurso);
                    }
                    if (txt_nombrearea != null) {
                        txt_nombrearea.setText(Cursos.nombre_area);
                    }

                    Cursos.imagen_curso = ur.Clear_URL(Cursos.imagen_curso);

                    ImageView slidingimage = (ImageView) v.findViewById(R.id.ImagenCurso);

                    if (Cursos.imagen_curso != "") {

                        if(Cursos.NOmbreImagenSolacurso.equals(""))
                        {
                            slidingimage.setImageResource(R.drawable.icon_cursos);
                        }else{
                            new DownloadImageTask(slidingimage,getContext(),Cursos.NOmbreImagenSolacurso).execute(Cursos.imagen_curso);
                        }


                    }


                } catch (Exception e) {
                    Log.e("Error ", " Error al Recorret el View Cursos : " + Cursos.imagen_curso +
                            " e: " + e.toString());
                }

            }
            return v;
        }
    }

    private class OnCursosClick implements AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            try{

            }catch (Exception ex)
            {

            }
            Clase_Cursos aux = new Clase_Cursos();
            Clase_Cursos Curso = listacursos.get(arg2);

            Intent myIntent;
            Bundle bundle = new Bundle();
            bundle.putString(aux.nombre_area, Curso.nombre_area);
            bundle.putString(aux.nombre_curso, Curso.nombre_curso);
            bundle.putString(ur.VIENEDECURSOS, ur.VIENEDECURSOS);
            bundle.putString(ur.IMAGENDELCURSO, Curso.NOmbreImagenSolacurso);
            bundle.putDouble(ur.COSTODELCURSO, Curso.Costo_Curso);
            myIntent = new Intent(getActivity(), MainActivity.class);
            myIntent.putExtras(bundle);
            //startActivityForResult(myIntent, 0);
            startActivity(myIntent);


        }
    }

}
