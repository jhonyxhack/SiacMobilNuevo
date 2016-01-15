package Clases;

import java.net.PortUnreachableException;
import java.util.ArrayList;

/**
 * Created by Romina on 19/12/2015.
 */
public class Clase_usuario {


    public static boolean Activo = false;
     // public static boolean YAPASO= false;

    int id = 0;
    //String id_usuario = "id_usuario";

   public String CampoIdUusuario="id_usuario";
   public String apellidos_CAMPO = "apellidos";
   public String correo = "correo";
   // String password = "password";
   public String id_session= "id_sesion";
    public  String NombreEmpresa_CAMPO = "empresa";
    public String nombre_CAMPO = "nombre";

    public static int id_usuario = 0;
    public static String NumeroMac = "";
    public static int NumeroSessionCompartida = 0;
    public static  String NombreCompleto = "Hola , ";
    public static String NombredelaEmpresa="";

   public static  String EmailUsuario = "";
   public static ArrayList<Clase_Cursos> ListaSuscripciones = new ArrayList<Clase_Cursos>();
   public static String NumeroTelefono = "";

    public static int versionCode  = 0;
    public static String versionName= "";
}
