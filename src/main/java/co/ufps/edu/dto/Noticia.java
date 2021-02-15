package co.ufps.edu.dto;

import java.sql.Date;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Clase DTO con la información de la noticia.
 *
 * @author UFPS
 */
public class Noticia {

    private long id;
    private String nombre;
    private String descripcion;
    private Date fecha;
    private int orden;
    private MultipartFile imagen1;
    private String im1Base64image;
    private Contenido contenido;

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
     * Se valida si la noticia se puede registrar.
     *
     * @return Retorna true si es válido para registrar, si no retorna false.
     */
    public boolean isValidoParaRegistrar() {
        return (!StringUtils.isEmpty(this.nombre) && !StringUtils.isEmpty(this.descripcion) && !StringUtils.isEmpty(fecha.toString()));
    }

    /**
     * Se valida si la noticia se puede actualizar.
     *
     * @return Retorna true si es válido para registrar, si no retorna false.
     */
    public boolean isValidoParaActualizar() {
        return (!StringUtils.isEmpty(this.nombre) && !StringUtils.isEmpty(this.descripcion) && !StringUtils.isEmpty(fecha.toString()) && !StringUtils.isEmpty(im1Base64image));
    }

    /**
     * ToString de la noticia.
     *
     * @return Información de la noticia.
     */
    @Override
    public String toString() {
        return "Noticia [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", fecha="
                + fecha + ", orden=" + orden + "]";
    }

    /**
     * Get de fecha.
     *
     * @return Valor de fecha.
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Set de fecha.
     *
     * @param fecha Nuevo valor de fecha.
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Get de imagen1.
     *
     * @return Valor de imagen1.
     */
    public MultipartFile getImagen1() {
        return imagen1;
    }

    /**
     * Set de imagen1.
     *
     * @param imagen1 Nuevo valor de imagen1.
     */
    public void setImagen1(MultipartFile imagen1) {
        this.imagen1 = imagen1;
    }

    /**
     * Get de im1Base64image.
     *
     * @return Valor de im1Base64image.
     */
    public String getIm1Base64image() {
        return im1Base64image;
    }

    /**
     * Set de im1Base64image.
     *
     * @param im1Base64image Nuevo valor de im1Base64image.
     */
    public void setIm1Base64image(String im1Base64image) {
        this.im1Base64image = im1Base64image;
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
