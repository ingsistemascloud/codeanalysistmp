package co.ufps.edu.dto;

import org.springframework.util.StringUtils;

/**
 * Clase DTO con la información del contacto.
 *
 * @author UFPS
 */
public class Contacto {

    private long id;
    private String nombre;

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
     * Se valida si el contacto se puede registrar.
     *
     * @return Retorna true si es válido para registrar, si no retorna false.
     */
    public boolean isValidoParaRegistrar() {
        return (!StringUtils.isEmpty(this.nombre));
    }

    /**
     * ToString del contacto.
     *
     * @return Información del contacto.
     */
    @Override
    public String toString() {
        return "Contacto [id=" + id + ", nombre=" + nombre + "]";
    }

}
