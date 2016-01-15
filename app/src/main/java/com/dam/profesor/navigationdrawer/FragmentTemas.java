package com.dam.profesor.navigationdrawer;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Clases.Clase_Cursos;
import Clases.Clase_tema;
import Clases.Clase_usuario;
import Clases.DownloadImageTask;
import Clases.JSONParser;
import Clases.URLServices;

import com.cuasmex.saec.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentTemas.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FragmentTemas extends Fragment {

    String nombre_area, nombre_curso, imagen_tema;
    String VideldelTemaMP4;
    double costo_curso = 0;
    int SiguienteTema = 0;

    Clase_usuario usuario = new Clase_usuario();

    ArrayList<Clase_tema> listatemas = new ArrayList<Clase_tema>();

    URLServices ur = new URLServices();

    //ArrayList<HashMap<String, String>> jsonlist = new ArrayList<HashMap<String, String>>();

    public FragmentTemas() {
        // Required empty public constructor

    }

    private OnFragmentInteractionListener mListener;

    public FragmentTemas(String area, String curso, String video, String imagen, double costo, int Siguie) {
        // Required empty public constructor
        nombre_area = area;
        nombre_curso = curso;
        VideldelTemaMP4 = video;
        imagen_tema = imagen;
        costo_curso = costo;
        SiguienteTema = Siguie;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_temas, container, false);

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

            new ProgressTask(FragmentTemas.this).execute();


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

    @SuppressWarnings("deprecation")
    public void detectScreenOrientation() {
        try {
            WindowManager wm = getActivity().getWindowManager();
            Display d = wm.getDefaultDisplay();
            if (d.getWidth() > d.getHeight()) {
                Log.d("Orientation", "Orientación Horizontal");
                // Toast.makeText(getActivity(), "landscape", Toast.LENGTH_SHORT).show();
                LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.LayoutVideo);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.weight = 0;
                layout.setLayoutParams(params);

            } else {
                // Toast.makeText(getActivity(), "Vertical", Toast.LENGTH_SHORT).show();
                Log.d("Orientation", "Orientacion Vertical");
            }
        } catch (Exception x) {
            Log.e("Orientation", "Portrait mode " + x.toString());
        }


    }


    VideoView videoView;
    ProgressDialog progressDialog;

    private void cargar_video(String video_url) {

        detectScreenOrientation();

        MediaController mediaController;
        progressDialog = ProgressDialog.show(getActivity(), "", "Preparando el video ...", true);
        getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);

        if (video_url == null || VideldelTemaMP4.equals("")) {
            video_url = "http://www.tech-inservice.com/files/videos_cursos/videos/intro_Robot_.mp4";
        } else {
            video_url = ur.URLVideoTemas + VideldelTemaMP4;
        }

        try {
            videoView = (VideoView) getActivity().findViewById(R.id.videotema);
            mediaController = new MediaController(getContext());
            mediaController.setAnchorView(videoView);
            // Set video link (mp4 format )
            Uri video = Uri.parse(video_url);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(video);

            videoView.seekTo(100);

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    try {
                        progressDialog.dismiss();
                        videoView.start();
                    } catch (Exception ex) {
                        videoView.stopPlayback();
                        progressDialog.dismiss();
                    }

                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer vmp) {
                    /*Intent i = new Intent(getBaseContext(),
                            com.PlayerOrange.ViewPlaylist.class);
                    i.putExtra("id", idPlaylist);
                    i.putExtra("timer", timer);
                    startActivity(i);
                    currentActivityName.finish();
                    */
                    //Toast.makeText(getApplicationContext(), "Clicked: " Intent myIntent;+ country +" position "+arg2 , Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getActivity(),"El video ha terminado",Toast.LENGTH_LONG);
                    if (SiguienteTema <= listatemas.size()) {
                        LlamarelVideo(SiguienteTema);
                    }
                }
            });



        } catch (Exception e) {
            videoView.stopPlayback();
            progressDialog.dismiss();
            cargar_video("");
            Log.e("ErrorReproducirVideo", " Error al Reproducir el video del curso :" + e.toString());
            // abrir_regresar("", "", "", 0);
        }

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
            this.dialog.setMessage("Obteniendo Temas");
            this.dialog.show();
        }


        @Override
        protected void onPostExecute(final Boolean success) {

            try {

                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (listatemas.size() > 0) {
                    // SiguienteTema = 0;
                    // Adelantar = true;
                }

                lv = (ListView) getActivity().findViewById(R.id.ListaTemas);
                lv.setAdapter(new TemaAdapter(context, R.layout.list_xml, listatemas));
                // lv = getListView();
                //set listener onclick
                lv.setOnItemClickListener(new OnCursosClick());

                cargar_video(VideldelTemaMP4);

            } catch (Exception ex) {
                Log.e("onPostExecute", "Error al finalizar le Proceso de Ejecutar areas");
            }

        }

        protected Boolean doInBackground(final String... args) {

            JSONParser jParser = new JSONParser(); // get JSON data from URL

            String _url = ur.UrlGetTemas + "id_area=" + nombre_area + "&id_curso=" + nombre_curso;

            _url = ur.Clear_URL(_url);

            JSONArray json = jParser.getJSONFromUrl(_url);

            for (int i = 0; i < json.length(); i++) {
                try {
                    JSONObject c = json.getJSONObject(i);
                    Clase_tema Tema = new Clase_tema();

                    HashMap<String, String> map = new HashMap<String, String>(); // Add child node to HashMap key & value
                    map.put(Tema.nombre_area, c.getString(Tema.nombre_area));
                    map.put(Tema.nombre_curso, c.getString(Tema.nombre_curso));
                    map.put(Tema.nombre_tema, c.getString(Tema.nombre_tema));

                    Tema.NombreVideo = ur.ObtenerVideoUrl(c.getString("archivo")); // el campo archivo contiene los nombres de los videos
                    // Tema.video_url = ur.URLVideoTemas + ur.ObtenerVideoUrl(c.getString(Tema.video_url));

                    Tema.nombre_curso = c.getString(Tema.nombre_curso);
                    Tema.nombre_area = c.getString(Tema.nombre_area);
                    Tema.nombre_tema = c.getString(Tema.nombre_tema);
                    Tema.IDCURSONumerico = ur.ConvertToInteger(c.getString(Tema._CampoIDCURSO));
                    Tema.descripcion_tema = ur.RecortarString(c.getString(Tema.descripcion_tema), 100);

                    // VideldelTemaMP4 = Tema.NombreVideo ;
                    // jsonlist.add(map);
                    listatemas.add(Tema);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

    }


    private class TemaAdapter extends ArrayAdapter<Clase_tema> {
        LayoutInflater vi;
        private ArrayList<Clase_tema> items;

        public TemaAdapter(Context context, int textViewResourceId, ArrayList<Clase_tema> items) {
            super(context, textViewResourceId, items);
            this.items = items;
            vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                v = vi.inflate(R.layout.list_xml, null);
            }

            Clase_tema Tema = items.get(position);

            if (Tema != null) {
                try {


                    LinearLayout rl = (LinearLayout)v.findViewById(R.id.Renglon);

                    ur.ChangueColorRow(getContext(),rl,position);

                    TextView TituloTema = (TextView) v.findViewById(R.id.NombrCurso);
                    TextView txt_DescripcionCurso = (TextView) v.findViewById(R.id.DescripcionCurso);
                    TextView txt_area = (TextView) v.findViewById(R.id.txtNombredelArea);

                    if (TituloTema != null) {
                        TituloTema.setText(Tema.nombre_tema);
                    }
                    if (txt_DescripcionCurso != null) {
                        txt_DescripcionCurso.setText(Tema.descripcion_tema);
                    }
                    if (txt_area != null) {
                        txt_area.setText(Tema.nombre_curso);
                    }

                    ImageView slidingimage = (ImageView) v.findViewById(R.id.ImagenCurso);

                    if (imagen_tema != null) {
                        if (imagen_tema.equals("")) {
                            slidingimage.setImageResource(R.drawable.icon_temas);
                        } else {
                            new DownloadImageTask(slidingimage, getContext(), imagen_tema).execute(imagen_tema);
                        }

                    }


                } catch (Exception e) {
                    Log.e("Error ", " Error al asiganar la imagen : " + Tema.nombre_tema + " e: " + e.toString());
                }

            }
            return v;
        }
    }


    boolean Adelantar = false;

    private class OnCursosClick implements AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {


            if (usuario.Activo) {
                Clase_tema TemaAux = new Clase_tema();
                Clase_tema Tema = listatemas.get(arg2);

                boolean SiTieneSuscripcion = false;

                Log.i("CostoCurso", "Costo = " + costo_curso);

                if (costo_curso == 0) {
                    SiTieneSuscripcion = true;
                } else  // Si el costo del Curso es Diferente de 0 indica que requiere de una suscrpcion
                {
                    for (int x = 0; x < usuario.ListaSuscripciones.size(); x++) {

                        Clase_Cursos copia = usuario.ListaSuscripciones.get(x);

                        if (copia.ID_CURSONumerico == Tema.IDCURSONumerico) {
                            SiTieneSuscripcion = true;
                        }
                    }
                }

                if (SiTieneSuscripcion) {


                    LlamarelVideo(arg2);

                } else {
                    createSimpleDialog(getContext());
                }
            } else // Si el usuario no esta Activo lo mandamos a Logearse
            {
                Intent myIntent;
                Bundle bundle = new Bundle();
                bundle.putString(ur.VIENEABRIRSESION, ur.VIENEABRIRSESION);
                myIntent = new Intent(getActivity(), activity_login.class);
                myIntent.putExtras(bundle);
                //startActivityForResult(myIntent, 0);
                startActivity(myIntent);
            }

        }
    }

    private void LlamarelVideo(int NumeroTema) {
        try {
            Clase_tema Tema = new Clase_tema();
            boolean entra = false;

            if (NumeroTema < listatemas.size()) {
                Tema = listatemas.get(NumeroTema);
                entra = true;
            }
            if ((NumeroTema + 1) < listatemas.size()) {
                SiguienteTema = NumeroTema + 1;
                Adelantar = true;
            } else {
                Adelantar = false;
            }

            if (entra) {
                Clase_tema TemaAux = new Clase_tema();
                Intent myIntent;
                Bundle bundle = new Bundle();
                bundle.putString(TemaAux.nombre_tema, Tema.nombre_tema);
                bundle.putString(TemaAux.nombre_curso, Tema.nombre_curso);
                bundle.putString(TemaAux.nombre_area, Tema.nombre_area);
                bundle.putString(ur.VIDEODELTEMA, Tema.NombreVideo);
                bundle.putString(TemaAux.descripcion_tema, Tema.descripcion_tema);
                bundle.putString(ur.VIENEDETEMAS, ur.VIENEDETEMAS);
                bundle.putString(ur.IMAGENDELCURSO, imagen_tema);
                bundle.putDouble(ur.COSTODELCURSO, costo_curso);
                bundle.putInt(ur.SIGUIENTETEMA, SiguienteTema);

                myIntent = new Intent(getActivity(), MainActivity.class);
                myIntent.putExtras(bundle);
                startActivity(myIntent);
            }
        } catch (Exception ex) {

        }

    }

    /**
     * Crea un diálogo de alerta sencillo
     *
     * @return Nuevo diálogo
     */
    public void createSimpleDialog(Context context) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(R.string.DeseaSuscribirse);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AbrirPago();
                        //dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void AbrirPago() {
        try {
            String URL_PagoCurso = ur.Version_Pagos_PHP + "aplicarpago.php?id_curso=" +
                    nombre_curso + "&type_product=course&nameCurso=" + nombre_curso + "&user=" +
                    usuario.EmailUsuario;

            /*
            Intent myIntent;
            Bundle bundle = new Bundle();
            bundle.putString(ur.VIENEPAGOS, ur.VIENEPAGOS);
            bundle.putString("_url",URL_PagoCurso);
            myIntent = new Intent(getActivity(), Explorador.class);
            myIntent.putExtras(bundle);
            startActivity(myIntent);*/

            SENDEmailToSus(URL_PagoCurso);

        } catch (Exception ex) {

        }
    }

    private void SENDEmailToSus(String _url) {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");

            String Cuerpo = "Hola , requiero suscribirme a un Curso el cual es de mi interes " +
                    "\n Programa " + nombre_area +
                    "\n Curso " + nombre_curso +
                    "\n El Link Directo es " +
                    "\n\n" + ur.Clear_URL(_url);

            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"capacitacion@tech-inservice.com", usuario.EmailUsuario});
            i.putExtra(Intent.EXTRA_SUBJECT, "Requiero Suscripción a " + nombre_curso);
            i.putExtra(Intent.EXTRA_TEXT, Cuerpo);
            try {
                startActivity(Intent.createChooser(i, "Enviando Email..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getActivity(), "No tiene ningun Servicio de Email instalado.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {

        }
    }

}
