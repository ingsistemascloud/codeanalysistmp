package co.ufps.edu.dto;

import org.springframework.util.StringUtils;

/**
 * Clase DTO con la información del ítem.
 *
 * @author UFPS
 */
public class ItemSubCategoria {

    private long id;
    private String nombre;
    private String descripcion;
    private int orden;
    private long subcategoria_id;
    private SubCategoria subcategoria;
    private Contenido contenido;

    /**
     * Get de subcategoria_id.
     *
     * @return valor de subcategoria_id.
     */
    public long getSubcategoria_id() {
        return subcategoria_id;
    }

    /**
     * Set de subcategoria_id.
     *
     * @param subcategoria_id nuevo valor de subcategoria_id.
     */
    public void setSubcategoria_id(long subcategoria_id) {
        this.subcategoria_id = subcategoria_id;
    }
    
    /**
     * Get de subcategoria.
     *
     * @return valor de subcategoria.
     */
    public SubCategoria getSubcategoria() {
        return subcategoria;
    }

    /**
     * Set de subcategoria.
     *
     * @param subcategoria nuevo valor de subcategoria.
     */
    public void setSubcategoria(SubCategoria subcategoria) {
        this.subcategoria = subcategoria;
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
     * Get de orden.
     *
     * @return Valor de orden.
     */
    public int getOrden() {
        return orden;
    }

    /**
     * Set de orden.
     *
     * @param orden Nuevo valor de orden.
     */
    public void setOrden(int orden) {
        this.orden = orden;
    }
    
    /**
     * Se valida si el ítem se puede registrar.
     *
     * @return Retorna true si es válido para registrar, si no retorna false.
     */
    public boolean isValidoParaRegistrar() {
        return (!StringUtils.isEmpty(this.nombre) && !StringUtils.isEmpty(this.descripcion) && (this.subcategoria.getId() > 0));
    }
    
    /**
     * ToString del ítem.
     *
     * @return Información del ítem.
     */
    @Override
    public String toString() {
        return "ItemSubCategoria [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion
                + ", orden=" + orden + ", subcategoria=" + subcategoria + "]";
    }

    /**
     * Get de contenido.
     *
     * @return Valor de contenido.
     */
    public Contenido getContenido() {
        return contenido;
    }

    /**
     * Set de contenido.
     *
     * @param contenido Nuevo valor de contenido.
     */
    public void setContenido(Contenido contenido) {
        this.contenido = contenido;
    }

}
