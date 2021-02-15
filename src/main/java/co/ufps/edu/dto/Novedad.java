package co.ufps.edu.dto;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Clase DTO con la información de la novedad.
 *
 * @author UFPS
 */
public class Novedad {

    private long id;
    private String nombre;
    private Date fecha;
    private MultipartFile imagen;
    private String imBase64image;
    private String fechaEnFormato;
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
        LocalDate today = LocalDate.of(fecha.getYear(), fecha.getMonth() + 1, fecha.getDate());
        String mes = today.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        this.fechaEnFormato = fecha.getDate() + " de " + mes + " de " + (fecha.getYear() + 1900);
    }

    /**
     * Se valida si la novedad se puede registrar.
     *
     * @return Retorna true si es válido para registrar, si no retorna false.
     */
    public boolean isValidoParaRegistrar() {
        return (!StringUtils.isEmpty(this.nombre) && !StringUtils.isEmpty(this.fecha));
    }

    /**
     * Se valida si la novedad se puede actualizar.
     *
     * @return Retorna true si es válido para registrar, si no retorna false.
     */
    public boolean isValidoParaActualizar() {
        return (!StringUtils.isEmpty(this.nombre) && !StringUtils.isEmpty(this.fecha) && !StringUtils.isEmpty(imBase64image));
    }

    /**
     * ToString de la novedad.
     *
     * @return Información de la novedad.
     */
    @Override
    public String toString() {
        return "Novedad [id=" + id + ", nombre=" + nombre + ", fecha=" + fecha + "]";
    }

    /**
     * Get de imagen.
     *
     * @return Valor de imagen.
     */
    public MultipartFile getImagen() {
        return imagen;
    }

    /**
     * Set de imagen.
     *
     * @param imagen Nuevo valor de imagen.
     */
    public void setImagen(MultipartFile imagen) {
        this.imagen = imagen;
    }

    /**
     * Get de imBase64image.
     *
     * @return Valor de imBase64image.
     */
    public String getImBase64image() {
        return imBase64image;
    }

    /**
     * Set de imBase64image.
     *
     * @param imBase64image Nuevo valor de imBase64image.
     */
    public void setImBase64image(String imBase64image) {
        this.imBase64image = imBase64image;
    }

    /**
     * Get de fechaEnFormato.
     *
     * @return Valor de fechaEnFormato.
     */
    public String getFechaEnFormato() {
        return fechaEnFormato;
    }

    /**
     * Set de fechaEnFormato.
     *
     * @param fechaEnFormato Nuevo valor de fechaEnFormato.
     */
    public void setFechaEnFormato(String fechaEnFormato) {
        this.fechaEnFormato = fechaEnFormato;
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
