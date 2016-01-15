package Clases;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.LinearLayout;

import com.cuasmex.saec.BuildConfig;
import com.cuasmex.saec.R;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Romina on 17/12/2015.
 */
public class URLServices {


    public static  final String VIENEDEAREA = "VienedeAreas";
    public static  final String VIENEDECURSOS = "VienedeCursos";
    public static  final String VIENEDETEMAS = "VienedeTemas";
    public static  final String VIENEDECERRARSESSION = "VienedeCERRARSESSION";
    public static  final String VIENEABRIRSESION = "VieneABRIRSESSION";
    public static  final String VIENEPAGOS = "VIENEPAGOS";
    public static final String IMAGENDELCURSO="IMAGENDELCURSO";
    public static final String COSTODELCURSO="COSTODELCURSO";
    public static final String VIDEODELTEMA="VIDEODELTEMA";
    public static final String SIGUIENTETEMA="SIGUIENTETEMA";
    public static final String CREARUSUARIO="CREARUSUARIO";

    public static final String VIENEACREARUSUARIO="VIENEACREARUSUARIO";
    public static final String RECORDARUSUARIO="RECORDARUSUARIO";

    public static final String _URLSAECGOOGLE = "https://play.google.com/store/apps/details?id=com.cuasmex.saec";
    public static final String _TXTInvitacion = "Hola estoy usando la aplicación SAEC, donde encuentro Videos de Entrenamiento" +
            ", de todo relacionado con la Industría y me gustaria que la pudieras instalar. Descargala en el siguiente link ";

    public static  final String PATHImages="ImagesSAEC";

    // public static final String IP = "http://192.168.1.132:81/WebServiceSaec";
    public static final String IP = "http://www.tech-inservice.com/WebServiceSaec";
    public String UrlGetAreas = IP + "/GetAreas.php";
    public String UrlGetCursos = IP + "/GetCursos.php?id_area=";
    public String UrlGetTemas = IP + "/GetTemas.php?";
    public String UrlGetSesion = IP + "/GetSession.php?";
    public String UrlSetSesion = IP + "/SetSession.php?";
    public String UrlCreateUser= IP + "/CreateUser.php?";
    public String UrlGetSusCriptionUser = IP + "/GetSusCriptionUser.php?";

    public String Version_Pagos_PHP = "http://www.tech-inservice.com/PagosOnline/";

    public static String URLVideoTemas = "http://www.tech-inservice.com/files/videos_cursos/videos/";

    public  static  String MISCURSOS= "MISCURSOS";
    public  static  String NUEVOSCURSOS= "NUEVOSCURSOS";
    public  static  String CURSOSGRATIS= "CURSOSGRATIS";

        /**
         * @param html String con el texto con etiquetas
         * @param openString caracter(String) que inicia la etiqueta '<'
         * @param closeString caracter(String) que cierra la etiqueta '>'
         * @return String sin etiquetas
         */
        public String ClearHTML(String html, String openString, String closeString) {
            try{
                html = html.replace("<br>","\n" );
                html = html.replace("<BR>","\n" );

                int indexOfOpenString = html.indexOf(openString);
                int indexOfCloseString = html.indexOf(closeString);
                if (indexOfOpenString != -1 && indexOfCloseString != -1) {
                    String substring = html.substring(indexOfOpenString, indexOfCloseString + 1);
                    html = ClearHTML(html.replace(substring, ""), openString, closeString);
                }
                return html;
            }catch (Exception x)
            {

            }
            return  html;
        }


    public String Clear_URL(String urlclear) {
        try {

            urlclear = urlclear.replace(" ", "%20");
            Log.d("Debug", " Url del video -> " + urlclear.toString());


        } catch (Exception e) {
            urlclear = "";
        }
        return urlclear;

    }

    public String Acentos(String parametro)
    {
        try{
            // parametro = URLEncoder.encode(parametro);

        }catch (Exception x){
            parametro = "";
        }

        return parametro;
    }

    public String RecortarString(String cadena, int tamanio) {
        try {

            cadena = ClearHTML(cadena,"<",">");

            if (cadena.length() > tamanio) {
                cadena = cadena.substring(0, tamanio) + " (..mas)";
                return cadena;
            } else {
                return cadena;
            }
        } catch (Exception x) {

        }
        return cadena;
    }

    public String ObtenerVideoUrl(String url_video)
    {

        try {

            String videos[] = new String[3];
            videos = url_video.split(";");

            url_video = videos[0]; //url_video.replace(";", "");

            return url_video;

        } catch (Exception e) {
            // TODO: handle exception
            url_video= "";
        }
        Log.d("Debug", " Url del video -> " + url_video.toString());
        return url_video;
    }

    public int ConvertToInteger(String valor){
        int num =0;
        try{

            num = Integer.parseInt(valor);

        }catch (Exception ex)
        {
            num = 0;
        }
        return  num;
    }

    public double ConvertToDouble(String valor){
        double num =0;
        try{

            num = Double.parseDouble(valor);

        }catch (Exception ex)
        {
            num = 0;
        }
        return  num;
    }



    public String getMacAddress(Context context) {
        String macAddress = "";
        try {
            WifiManager wimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            macAddress  = wimanager.getConnectionInfo().getMacAddress();
            if (macAddress == null) {
               // macAddress = "Device don't have mac address or wi-fi is disabled";
                macAddress = getNumberPhone(context);
            }

        }catch (Exception ex)
        {
            Log.e("getMacAddress",ex.toString());
        }

        return macAddress;
    }

    public String getNumberPhone(Context context)
    {
        String getSimSerialNumber = "Local";
        try{
            TelephonyManager telemamanger = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            getSimSerialNumber = telemamanger.getSimSerialNumber();

            GetVersionNameAndVersionCode();

        }catch (Exception ex){
            getSimSerialNumber = "Not-Found-Number";
        }
        return  getSimSerialNumber;
    }

    public void GetVersionNameAndVersionCode()
    {
        try
        {
            Clase_usuario u = new Clase_usuario();
            u.versionCode = BuildConfig.VERSION_CODE;
            u.versionName = BuildConfig.VERSION_NAME;

        }catch (Exception ex){

        }
    }

    /*
    private String getMyPhoneNumber(Context context){
        TelephonyManager mTelephonyMgr;
        mTelephonyMgr = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getLine1Number();
    }

    private String getMy10DigitPhoneNumber(){
        String s = getMyPhoneNumber();
        return s.substring(2);
    }
    */


    public static Bitmap loadBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;

        try {
            in = new java.net.URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(in);

        } catch (IOException e) {
            Log.e("Error", "loadBitmap Could not load Bitmap from: " + url);
        } finally {
            //closeStream(in);
            //closeStream(out);
        }

        return bitmap;
    }


    public void ChangueColorRow(Context context, LinearLayout rl, int posision)
    {
        try{
            if(posision % 2 == 0) {
                rl.setBackgroundColor(context.getResources().getColor(R.color.RenglonPar));
            }else
            {
                rl.setBackgroundColor(context.getResources().getColor(R.color.RenglonBlanco));
            }
        }catch (Exception ex)
        {

        }
    }
}
