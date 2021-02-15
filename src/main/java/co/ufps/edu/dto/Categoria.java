package co.ufps.edu.dto;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;

/**
 * Clase DTO con la información del categoria.
 *
 * @author UFPS
 */
public class Categoria {

    private long id;
    private String nombre;
    private String descripcion;
    private int orden;
    private List<SubCategoria> subcategorias;
    private Contenido contenido;

    /**
     * Constructor de la clase en donde se inicializan las variables.
     */
    public Categoria() {
        subcategorias = new ArrayList<>();
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
     * Se valida si la categoria se puede registrar.
     *
     * @return Retorna true si es válido para registrar, si no retorna false.
     */
    public boolean isValidoParaRegistrar() {
        return (!StringUtils.isEmpty(this.nombre) && !StringUtils.isEmpty(this.descripcion));
    }

    /**
     * ToString de categoria.
     *
     * @return Información de la categoria.
     */
    @Override
    public String toString() {
        return "Categoria [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion
                + ", orden=" + orden + "]";
    }

    /**
     * Get de subcategorias.
     *
     * @return Lista de subcategorias.
     */
    public List<SubCategoria> getSubcategorias() {
        return subcategorias;
    }

    /**
     * Set de subcategorias.
     *
     * @param subcategorias Nuevo valor de subcategorias.
     */
    public void setSubcategorias(List<SubCategoria> subcategorias) {
        this.subcategorias = subcategorias;
    }

    /**
     * Método que permite agregar una subcategoria a la lista.
     *
     * @param subCategoria Subcategoria a agregar.
     */
    public void agregarSubcategoria(SubCategoria subCategoria) {
        this.subcategorias.add(subCategoria);

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

    /**
     * Método que permite validar si hay subtegorias en la lista.
     *
     * @return True si hay subcategoria, si no hay retorna false.
     */
    public boolean haySubCategorias() {
        return this.subcategorias.size() > 0;
    }

}
