package co.ufps.edu.dto;

import java.io.File;
import java.util.ArrayList;
import org.springframework.util.StringUtils;

/**
 * Clase DTO con la información del contenido.
 *
 * @author UFPS
 */
public class Contenido {

    private long id;
    private String nombre;
    private String tipoAsociacion;
    private long asociacion;
    private String contenido;
    private TipoContenido tipoContenido;
    private long tipocontenido_id;
    private String url;
    private ArrayList<String> conn;

    /**
     * Constructor de la clase en donde se inicializan las variables.
     */
    public Contenido() {

    }

    /**
     * Constructor de la clase en donde se inicializan las variables.
     */
    public Contenido(long id, String nombre, String tipoAsociacion, long asociacion, String contenido,
            TipoContenido tipoContenido, long tipocontenido_id, String url) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.tipoAsociacion = tipoAsociacion;
        this.asociacion = asociacion;
        this.contenido = contenido;
        this.tipoContenido = tipoContenido;
        this.tipocontenido_id = tipocontenido_id;
        this.url = url;
    }

    /**
     * Get de tipocontenido_id.
     *
     * @return valor de tipocontenido_id.
     */
    public long getTipocontenido_id() {
        return tipocontenido_id;
    }

    /**
     * Set de tipocontenido_id.
     *
     * @param tipocontenido_id nuevo valor de tipocontenido_id.
     */
    public void setTipocontenido_id(long tipocontenido_id) {
        this.tipocontenido_id = tipocontenido_id;
    }

    /**
     * Get de id.
     *
     * @return valor de id.
     */
    public long getId() {
        return id;
    }

    /**
     * Set de id.
     *
     * @param id nuevo valor de id.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get de nombre.
     *
     * @return Valor de nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Set de nombre.
     *
     * @param nombre Nuevo valor de nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Get de tipoAsociacion.
     *
     * @return Valor de tipoAsociacion.
     */
    public String getTipoAsociacion() {
        return tipoAsociacion;
    }

    /**
     * Set de tipoAsociacion.
     *
     * @param tipoAsociacion Nuevo valor de tipoAsociacion.
     */
    public void setTipoAsociacion(String tipoAsociacion) {
        this.tipoAsociacion = tipoAsociacion;
    }

    /**
     * Get de asociacion.
     *
     * @return Valor de asociacion.
     */
    public long getAsociacion() {
        return asociacion;
    }

    /**
     * Set de asociacion.
     *
     * @param asociacion Nuevo valor de asociacion.
     */
    public void setAsociacion(long asociacion) {
        this.asociacion = asociacion;
    }

    /**
     * Get de contenido.
     *
     * @return Valor de contenido.
     */
    public String getContenido() {
        return contenido;
    }

    /**
     * Set de contenido.
     *
     * @param contenido Nuevo valor de contenido.
     */
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    /**
     * Se válida si el contenido se puede registrar.
     *
     * @return Retorna true si es válido para registrar, si no retorna false.
     */
    public boolean isValidoParaRegistrar() {
        if (this.tipoContenido.getId() == 1) {
            return (!StringUtils.isEmpty(this.nombre)
                    && (conn.size() > 0)
                    && !StringUtils.isEmpty(this.tipoContenido.getNombre())
                    && !StringUtils.isEmpty(this.tipoAsociacion) && this.asociacion != 0);

        } else {
            return (!StringUtils.isEmpty(this.nombre)
                    && (this.contenido.length() > 0)
                    && !StringUtils.isEmpty(this.tipoContenido.getNombre())
                    && !StringUtils.isEmpty(this.tipoAsociacion) && this.asociacion != 0);

        }

    }

    /**
     * Get de tipoContenido.
     *
     * @return Valor de tipoContenido.
     */
    public TipoContenido getTipoContenido() {
        return tipoContenido;
    }

    /**
     * Set de tipoContenido.
     *
     * @param tipoContenido Nuevo valor de tipoContenido.
     */
    public void setTipoContenido(TipoContenido tipoContenido) {
        this.tipoContenido = tipoContenido;
    }

    /**
     * Get de url.
     *
     * @return Valor de url.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set de url.
     *
     * @param url Nuevo valor de url.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * ToString del contenido.
     *
     * @return Información del contenido.
     */
    @Override
    public String toString() {
        return "Contenido [id=" + id + ", nombre=" + nombre + ", tipoAsociacion=" + tipoAsociacion
                + ", asociacion=" + asociacion + ",  \n contenido=" + contenido + ", \n tipoContenido="
                + tipoContenido + ", url=" + url + "]";
    }

    /**
     * Get de conn.
     *
     * @return Valor de conn.
     */
    public ArrayList<String> getConn() {
        return conn;
    }

    /**
     * Set de conn.
     *
     * @param conn Nuevo valor de conn.
     */
    public void setConn(ArrayList<String> conn) {
        this.conn = conn;
    }

}
