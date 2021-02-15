package co.ufps.edu.dto;

import java.sql.Date;

/**
 * Clase DTO con la información de visita.
 *
 * @author UFPS
 */
public class Visita {

    private String ip;
    private Date fecha;

    /**
     * Get de ip.
     *
     * @return valor de ip.
     */
    public String getIp() {
        return ip;
    }

    /**
     * Set de ip.
     *
     * @param ip nuevo valor de ip.
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Get de fecha.
     *
     * @return valor de fecha.
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Set de fecha.
     *
     * @param fecha nuevo valor de fecha.
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

}
