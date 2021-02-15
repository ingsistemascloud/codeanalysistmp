package co.ufps.edu.dto;

/**
 * Clase DTO con la información del login.
 *
 * @author UFPS
 */
public class Login {

    private String correoInstitucional;
    private String contrasena;

    /**
     * Constructor de la clase en donde se inicializan las variables.
     */
    public Login() {
    }

    /**
     * Get de correoInstitucional.
     *
     * @return valor de correoInstitucional.
     */
    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    /**
     * Set de correoInstitucional.
     *
     * @param correoInstitucional nuevo valor de correoInstitucional.
     */
    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }

    /**
     * Get de contrasena.
     *
     * @return valor de contrasena.
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Set de contrasena.
     *
     * @param contrasena nuevo valor de contrasena.
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

}
