package co.ufps.edu.dto;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;

/**
 * Clase DTO con la información de la subcategoria.
 *
 * @author UFPS
 */
public class SubCategoria {

    private long id;
    private String nombre;
    private String descripcion;
    private int orden;
    private Categoria categoria;
    private Contenido contenido;
    private List<ItemSubCategoria> itemsubcategorias;

    /**
     * Constructor de la clase en donde se inicializan las variables.
     */
    public SubCategoria() {
        itemsubcategorias = new ArrayList<>();
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
     * Get de ítem de subcategorias.
     *
     * @return Lista de ítem de subcategorias.
     */
    public List<ItemSubCategoria> getItemsubcategorias() {
        return itemsubcategorias;
    }

    /**
     * Set de ítem de subcategorias.
     *
     * @param itemsubcategorias nuevo valor de ítem de subcategorias.
     */
    public void setItemsubcategorias(List<ItemSubCategoria> itemsubcategorias) {
        this.itemsubcategorias = itemsubcategorias;
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
     * Se valida si la subcategoria se puede registrar.
     *
     * @return Retorna true si es válido para registrar, si no retorna false.
     */
    public boolean isValidoParaRegistrar() {
        return (!StringUtils.isEmpty(this.nombre) && !StringUtils.isEmpty(this.descripcion) && (this.categoria.getId() > 0));
    }

    /**
     * Get de categoria.
     *
     * @return Valor de categoria.
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * Set de categoria.
     *
     * @param categoria Nuevo valor de categoria.
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * ToString de subcategoria.
     *
     * @return Información de la subcategoria.
     */
    @Override
    public String toString() {
        return "SubCategoria [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion
                + ", orden=" + orden + ", categoria=" + categoria + "]";
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
     * Get de itemsubcategorias.
     *
     * @param itemsubcategorias Valor de itemsubcategorias.
     */
    public void setItemSubcategorias(List<ItemSubCategoria> itemsubcategorias) {
        this.itemsubcategorias = itemsubcategorias;
    }

    /**
     * Set de itemsubcategoria.
     *
     * @param itemsubcategoria Nuevo valor de itemsubcategoria.
     */
    public void agregarItemSubcategoria(ItemSubCategoria itemsubcategoria) {
        this.itemsubcategorias.add(itemsubcategoria);

    }
}
