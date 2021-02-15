package co.ufps.edu.dto;

import java.sql.Date;
import java.util.ArrayList;
import org.springframework.util.StringUtils;

/**
 * Clase DTO con la información del galeria.
 *
 * @author UFPS
 */
public class Galeria {

    private long id;
    private String nombre;
    private String descripcion;
    private Date fecha;
    private ArrayList<Imagen> imagenes;
    private String primeraImagen;

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
     * Se valida si la galeria se puede registrar.
     *
     * @return Retorna true si es válido para registrar, si no retorna false.
     */
    public boolean isValidoParaRegistrar() {
        return (!StringUtils.isEmpty(this.nombre) && !StringUtils.isEmpty(this.fecha) && !StringUtils.isEmpty(this.descripcion) && this.imagenes.size() > 0);
    }

    /**
     * Get de imagenes.
     *
     * @return Valor de imagenes.
     */
    public ArrayList<Imagen> getImagenes() {
        return imagenes;
    }

    /**
     * Set de imagenes.
     *
     * @param imagenes Nuevo valor de imagenes.
     */
    public void setImagenes(ArrayList<Imagen> imagenes) {
        this.imagenes = imagenes;
    }

    /**
     * Get de primeraImagen.
     *
     * @return Valor de primeraImagen.
     */
    public String getPrimeraImagen() {
        return primeraImagen;
    }

    /**
     * Set de primeraImagen.
     *
     * @param primeraImagen Nuevo valor de primeraImagen.
     */
    public void setPrimeraImagen(String primeraImagen) {
        this.primeraImagen = primeraImagen;
    }

}
