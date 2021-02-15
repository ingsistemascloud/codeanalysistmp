package co.ufps.edu.constantes;

import java.net.InetAddress;
import java.net.UnknownHostException;
import co.ufps.edu.util.FileUtil;

/**
 * Clase con la definición de las costantes usadas en el sistema.
 *
 * @author UFPS
 */
public class Constantes {

    private static FileUtil fileUtil;
    public static final String NOVEDAD = "Novedad";
    public static final String ACTIVIDAD = "Actividad";
    public static final String NOTICIA = "Noticia";
    public static final String CATEGORIA = "Categoria";
    public static final String SUBCATEGORIA = "Subcategoria";
    public static final String ITEMSUBCATEGORIA = "ItemSubcategoria";
    public static String RUTA = "";
    public static final String SERVER = "";

    /**
     * Contructor de la clase.
     */
    public Constantes() {
        fileUtil = new FileUtil();
        this.RUTA = fileUtil.getProperties().getProperty("name");
    }

    /**
     * Método para obtener la dirección del servidor.
     *
     * @return String con la dirección del servidor.
     */
    private static String getServer() {
        try {
            return InetAddress.getLocalHost().getCanonicalHostName().split("/")[0];
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "";
    }
}
