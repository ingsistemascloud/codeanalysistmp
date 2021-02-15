package co.ufps.edu.dto;

import org.springframework.util.StringUtils;

/**
 * Clase DTO con la información de la red.
 *
 * @author UFPS
 */
public class RedSocial {

    private long id;
    private String nombre;
    private String url;
    private String logo;

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
     * ToString de la red.
     *
     * @return Información de la red.
     */
    @Override
    public String toString() {
        return "RedSocial [id=" + id + ", nombre=" + nombre + ", url=" + url + "]";
    }

    /**
     * Se valida si la red se puede registrar.
     *
     * @return Retorna true si es válido para registrar, si no retorna false.
     */
    public boolean isValidoParaRegistrar() {
        return (!StringUtils.isEmpty(this.nombre) && !StringUtils.isEmpty(this.url));
    }

    /**
     * Get de logo.
     *
     * @return Valor de logo.
     */
    public String getLogo() {
        return logo;
    }

    /**
     * Set de logo.
     *
     * @param logo Nuevo valor de logo.
     */
    public void setLogo(String logo) {
        this.logo = logo;
    }

}
