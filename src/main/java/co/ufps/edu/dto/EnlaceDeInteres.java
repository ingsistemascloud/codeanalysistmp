package co.ufps.edu.dto;

import org.springframework.util.StringUtils;

/**
 * Clase DTO con la información de enlaces.
 *
 * @author UFPS
 */
public class EnlaceDeInteres {

    private long id;
    private String nombre;
    private String url;

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
     * Se valida si el enlace se puede registrar.
     *
     * @return Retorna true si es válido para registrar, si no retorna false.
     */
    public boolean isValidoParaRegistrar() {
        return (!StringUtils.isEmpty(this.nombre) && !StringUtils.isEmpty(this.url));
    }

    /**
     * ToString del enlace.
     *
     * @return Información del enlace.
     */
    @Override
    public String toString() {
        return "EnlaceDeInteres [id=" + id + ", nombre=" + nombre + ", url=" + url + "]";
    }

}
