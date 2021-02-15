package co.ufps.edu.dto;

import java.io.InputStream;

/**
 * Clase DTO con la información del archivo.
 *
 * @author UFPS
 */
public class Archivo {

    private String nombre;
    private InputStream contenido;
    private String tipo;

    /**
     * Get de nombre.
     *
     * @return valor de nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Set de nombre.
     *
     * @param nombre nuevo valor de nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Get de contenido.
     *
     * @return valor de contenido.
     */
    public InputStream getContenido() {
        return contenido;
    }

    /**
     * Set de contenido.
     *
     * @param contenido nuevo valor de contenido.
     */
    public void setContenido(InputStream contenido) {
        this.contenido = contenido;
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

}
