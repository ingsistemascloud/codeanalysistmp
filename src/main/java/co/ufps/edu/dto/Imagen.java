package co.ufps.edu.dto;

import java.sql.Date;

/**
 * Clase DTO con la información de la imagen.
 *
 * @author UFPS
 */
public class Imagen {

    private int id;
    private String imagen;
    private String descripcion;

    /**
     * Get de id.
     *
     * @return valor de id.
     */
    public int getId() {
        return id;
    }

    /**
     * Set de id.
     *
     * @param id nuevo valor de id.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get de imagen.
     *
     * @return valor de imagen.
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * Set de imagen.
     *
     * @param imagen nuevo valor de imagen.
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    /**
     * Get de descripcion.
     *
     * @return valor de descripcion.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Set de descripcion.
     *
     * @param descripcion nuevo valor de descripcion.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
