package co.ufps.edu.dto;

import org.springframework.web.multipart.MultipartFile;

/**
 * Clase DTO con la información del logo.
 *
 * @author UFPS
 */
public class Logo {

    private long id;
    private String tipo;
    private MultipartFile contenido;
    private String imBase64image;

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
     * Get de tipo.
     *
     * @return valor de tipo.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Set de tipo.
     *
     * @param tipo nuevo valor de tipo.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Get de contenido.
     *
     * @return valor de contenido.
     */
    public MultipartFile getContenido() {
        return contenido;
    }

    /**
     * Set de contenido.
     *
     * @param contenido nuevo valor de contenido.
     */
    public void setContenido(MultipartFile contenido) {
        this.contenido = contenido;
    }

    /**
     * Get de imBase64image.
     *
     * @return valor de imBase64image.
     */
    public String getImBase64image() {
        return imBase64image;
    }

    /**
     * Set de imBase64image.
     *
     * @param imBase64image nuevo valor de imBase64image.
     */
    public void setImBase64image(String imBase64image) {
        this.imBase64image = imBase64image;
    }

    /**
     * ToString de logo.
     *
     * @return Información del logo.
     */
    @Override
    public String toString() {
        return "Logo [id=" + id + ", tipo=" + tipo + "]";
    }

}
