package co.ufps.edu.dto;

import java.sql.Date;
import org.springframework.util.StringUtils;

/**
 * Clase DTO con la información de la actividad.
 *
 * @author UFPS
 */
public class Actividad {

    private long id;
    private String nombre;
    private String lugar;
    private Date fechaInicial;
    private Date fechaFinal;
    private Contenido contenido;
    private String fechaEnFormato;

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
     * Método encargado de validar si los campos para registrar son válidos.
     *
     * @return Estado de la validación.
     */
    public boolean isValidoParaRegistrar() {
        return (!StringUtils.isEmpty(this.nombre) && !StringUtils.isEmpty(this.lugar) && !StringUtils.isEmpty(this.fechaInicial.toString()) && !StringUtils.isEmpty(this.fechaFinal.toString()));
    }

    /**
     * Get de lugar.
     *
     * @return Valor de lugar.
     */
    public String getLugar() {
        return lugar;
    }

    /**
     * Set de valor.
     *
     * @param lugar Nuevo valor de lugar.
     */
    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    /**
     * Get de fecha inicial.
     *
     * @return Valor de fecha inicial.
     */
    public Date getFechaInicial() {
        return fechaInicial;
    }

    /**
     * Set de fecha inicial.
     *
     * @param fechaInicial Nuevo valor de fecha inicial.
     */
    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    /**
     * Get de fecha final.
     *
     * @return Valor de fecha final.
     */
    public Date getFechaFinal() {
        return fechaFinal;
    }

    /**
     * Set de fecha inicial.
     *
     * @param fechaFinal Nuevo valor de fecha final.
     */
    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    /**
     * ToString de actividad.
     *
     * @return Información de la actividad.
     */
    @Override
    public String toString() {
        return "Actividad [id=" + id + ", nombre=" + nombre + ", lugar=" + lugar + ", fechaInicial="
                + fechaInicial + ", fechaFinal=" + fechaFinal + "]";
    }

    /**
     * Método encargado de validar si los campos para actualizar son válidos.
     *
     * @return Estado de la validación.
     */
    public boolean isValidoParaActualizar() {
        return (!StringUtils.isEmpty(this.nombre) && !StringUtils.isEmpty(this.lugar) && !StringUtils.isEmpty(fechaInicial.toString()) && !StringUtils.isEmpty(fechaFinal.toString()));
    }

    /**
     * Get de contenido.
     *
     * @return Valor de contenido
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
     * Get de fecha en formato.
     *
     * @return Valor de fecha en formato.
     */
    public String getFechaEnFormato() {
        return fechaEnFormato;
    }

    /**
     * Set de fecha en formato.
     *
     * @param fechaEnFormato Nuevo valor de fecha en formato.
     */
    public void setFechaEnFormato(String fechaEnFormato) {
        this.fechaEnFormato = fechaEnFormato;
    }

    /**
     * Método encargado de crear la fecha en formato.
     */
    public void crearFechaFormato() {
        if (fechaInicial.getTime() == fechaFinal.getTime()) {
            fechaEnFormato = fechaInicial.toString();
        } else {
            fechaEnFormato = "Desde " + fechaInicial.toString() + " Hasta " + fechaFinal.toString();
        }

    }

    /**
     * Método encargado de comprobar que las fechas no se solapen.
     *
     * @return Estado de la validación.
     */
    public boolean HaySolapamiento() {
        return (fechaInicial.getTime() > fechaFinal.getTime());
    }

}
