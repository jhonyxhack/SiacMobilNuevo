package Clases;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PublicKey;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Clase_Cursos {

	public boolean ElUsuarioSiEstaSuscrito=false;

	public String nombre_area = "nombre_area";
	public String fecha_inicio = "";
	public String nombre_curso="nombre_curso";
	public String imagen_curso="extra3";
	public String url_video_curso="";
	public String DescripcionCurso = "descripcion";

	public String ID_CURSO = "id_curso";
	public String ID_AREA = "id_area";

	public int ID_CURSONumerico = 0;

	public static String URLImages = "http://www.tech-inservice.com/files/videos_cursos/";

   public static boolean HAYCURSOS = false;


	public String NOmbreImagenSolacurso="";
	public String costo_CAMPO="costo";
	public double Costo_Curso =0;

}

