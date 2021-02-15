package co.ufps.edu.dto;

/**
 * Clase DTO con la información del auditoria.
 *
 * @author UFPS
 */
public class Auditoria {

    private long idAuditoria;
    private String tabla;
    private String idRegistro;
    private String nombre;
    private String accion;
    private String fecha;
    private String hora;
    
    /**
     * Get de idAuditoria.
     *
     * @return valor de idAuditoria.
     */
    public long getIdAuditoria() {
        return idAuditoria;
    }

    /**
     * Get de tabla.
     *
     * @return valor de tabla.
     */
    public String getTabla() {
        return tabla;
    }

    /**
     * Get de idRegistro.
     *
     * @return valor de idRegistro.
     */
    public String getIdRegistro() {
        return idRegistro;
    }

    /**
     * Get de nombre.
     *
     * @return valor de nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Get de accion.
     *
     * @return valor de accion.
     */
    public String getAccion() {
        return accion;
    }

    /**
     * Set de idAuditoria.
     *
     * @param idAuditoria nuevo valor de idAuditoria.
     */
    public void setIdAuditoria(long idAuditoria) {
        this.idAuditoria = idAuditoria;
    }

    /**
     * Set de tabla.
     *
     * @param tabla nuevo valor de tabla.
     */
    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    /**
     * Set de idRegistro.
     *
     * @param idRegistro nuevo valor de idRegistro.
     */
    public void setIdRegistro(String idRegistro) {
        this.idRegistro = idRegistro;
    }

    /**
     * Set de nombre.
     *
     * @param nombre nuevo valor de nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Set de accion.
     *
     * @param accion nuevo valor de accion.
     */
    public void setAccion(String accion) {
        this.accion = accion;
    }

    /**
     * Get de fecha.
     *
     * @return valor de fecha.
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * Set de fecha.
     *
     * @param fecha nuevo valor de fecha.
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * Get de hora.
     *
     * @return valor de hora.
     */
    public String getHora() {
        return hora;
    }

    /**
     * Set de hora.
     *
     * @param hora nuevo valor de hora.
     */
    public void setHora(String hora) {
        this.hora = hora;
    }

    
}
