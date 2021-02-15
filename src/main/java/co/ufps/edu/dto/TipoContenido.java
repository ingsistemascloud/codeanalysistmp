package co.ufps.edu.dto;

/**
 * Clase DTO con la información del tipo de contenido.
 *
 * @author UFPS
 */
public class TipoContenido {

    private long id;
    private String nombre;
    private String descripcion;

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
     * Get de descripcion.
     *
     * @return Valor de descripcion.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Set de descripcion.
     *
     * @param descripcion Nuevo valor de descripcion.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * ToString de tipo de contenido.
     *
     * @return Información de tipo de contenido.
     */
    @Override
    public String toString() {
        return "TipoContenido [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + "]";
    }

}
