package co.ufps.edu.dto;

import co.ufps.edu.util.PassUtil;
import org.springframework.util.StringUtils;

/**
 * Clase DTO con la información de usuario.
 *
 * @author UFPS
 */
public class Usuario {

    private long activo;
    private String codActivo;
    private String rol;
    private String codigo;
    private String codigoNuevo;
    private String correo;
    private String contrasena;
    private String contrasenaAntigua;
    private String contrasenaNueva;
    private String contrasenaNueva2;
    private String correoCalendario;
    
    /**
     * Get de codActivo.
     *
     * @return valor de codActivo.
     */
    public String getCodActivo() {
        return codActivo;
    }

    /**
     * Set de codActivo.
     *
     * @param codActivo nuevo valor de codActivo.
     */
    public void setCodActivo(String codActivo) {
        this.codActivo = codActivo;
    }

    /**
     * Get de activo.
     *
     * @return valor de activo.
     */
    public long getActivo() {
        return activo;
    }

    /**
     * Set de activo.
     *
     * @param activo nuevo valor de activo.
     */
    public void setActivo(long activo) {
        this.activo = activo;
    }

    /**
     * Get de codigoNuevo.
     *
     * @return valor de codigoNuevo.
     */
    public String getCodigoNuevo() {
        return codigoNuevo;
    }

    /**
     * Set de codigoNuevo.
     *
     * @param codigoNuevo nuevo valor de codigoNuevo.
     */
    public void setCodigoNuevo(String codigoNuevo) {
        this.codigoNuevo = codigoNuevo;
    }

    /**
     * Get de rol.
     *
     * @return valor de rol.
     */
    public String getRol() {
        return rol;
    }

    /**
     * Set de rol.
     *
     * @param rol nuevo valor de rol.
     */
    public void setRol(String rol) {
        this.rol = rol;
    }

    /**
     * Get de codigo.
     *
     * @return valor de codigo.
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Set de codigo.
     *
     * @param codigo nuevo valor de codigo.
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Get de correo.
     *
     * @return valor de correo.
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Set de correo.
     *
     * @param correo nuevo valor de correo.
     */
    public void setCorreo(String correo) {
        this.correo = correo;
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

    /**
     * Get de contrasenaAntigua.
     *
     * @return valor de contrasenaAntigua.
     */
    public String getContrasenaAntigua() {
        return contrasenaAntigua;
    }

    /**
     * Set de contrasenaAntigua.
     *
     * @param contrasenaAntigua nuevo valor de contrasenaAntigua.
     */
    public void setContrasenaAntigua(String contrasenaAntigua) {
        this.contrasenaAntigua = contrasenaAntigua;
    }

    /**
     * Get de contrasenaNueva.
     *
     * @return valor de contrasenaNueva.
     */
    public String getContrasenaNueva() {
        return contrasenaNueva;
    }

    /**
     * Set de contrasenaNueva.
     *
     * @param contrasenaNueva nuevo valor de contrasenaNueva.
     */
    public void setContrasenaNueva(String contrasenaNueva) {
        this.contrasenaNueva = contrasenaNueva;
    }

    /**
     * Get de contrasenaNueva2.
     *
     * @return valor de contrasenaNueva2.
     */
    public String getContrasenaNueva2() {
        return contrasenaNueva2;
    }

    /**
     * Set de contrasenaNueva2.
     *
     * @param contrasenaNueva2 nuevo valor de contrasenaNueva2.
     */
    public void setContrasenaNueva2(String contrasenaNueva2) {
        this.contrasenaNueva2 = contrasenaNueva2;
    }

    /**
     * Get de correoCalendario.
     *
     * @return valor de contrasenaNueva2.
     */
    public String getCorreoCalendario() {
        return correoCalendario;
    }

    /**
     * Set de correoCalendario.
     *
     * @param correoCalendario nuevo valor de correoCalendario.
     */
    public void setCorreoCalendario(String correoCalendario) {
        this.correoCalendario = correoCalendario;
    }
    
    /**
     * Se valida si el usuario se puede registrar.
     *
     * @return Retorna true si es válido para registrar, si no retorna false.
     */
    public boolean isValidoParaRegistrar() {
        return (!StringUtils.isEmpty(this.contrasenaAntigua) && !StringUtils.isEmpty(this.contrasenaNueva) && !StringUtils.isEmpty(this.contrasenaNueva2));
    }

    /**
     * Se valida si el usuario se puede actualizar.
     *
     * @return Retorna true si es válido para registrar, si no retorna false.
     */
    public boolean isValidoParaActualizar() {
        return (!StringUtils.isEmpty(this.codigoNuevo) || !StringUtils.isEmpty(this.correo) || !StringUtils.isEmpty(this.contrasenaNueva) || !StringUtils.isEmpty(this.contrasenaNueva2) || !StringUtils.isEmpty(this.correoCalendario));
    }

    /**
     * Se valida si el usuario se puede registrar.
     *
     * @return Retorna true si es válido para registrar, si no retorna false.
     */
    public boolean isValidoParaRegistrarUsua() {
        return (!StringUtils.isEmpty(this.codigo) && !StringUtils.isEmpty(this.correo) && !StringUtils.isEmpty(this.rol) && !StringUtils.isEmpty(this.contrasenaNueva) && !StringUtils.isEmpty(this.contrasenaNueva2) );
    }

    /**
     * Se valida si se repiten las contraseñas.
     *
     * @return Retorna true si se repiten, si no retorna false.
     */
    public boolean seRepiten() {
        return this.contrasenaNueva.equals(this.contrasenaNueva2);
    }

    /**
     * Se valida si la contraseña cumple con las politicas definidas.
     *
     * @return Retorna true si es valida, si no retorna false.
     */
    public boolean contraValida(String pass) {
        int contadorNum = 0;
        int contadorMayus = 0;
        int contadorMinus = 0;
        for (int i = 0; i < pass.length(); i++) {
            String letra = pass.substring(i, i + 1);
            try {
                int num = Integer.parseInt(letra);
                contadorNum++;
            } catch (Exception e) {
                String m = letra.toUpperCase();
                if (m.equals(letra)) {
                    contadorMayus++;
                } else {
                    contadorMinus++;
                }
            }
        }
        if (contadorNum >= 4 && contadorMayus >= 2 && contadorMinus >= 4) {
            return true;
        }
        return false;
    }
    
    public boolean validarCorreo(String correo) {
        if (correo.length() < 16) {
            return false;
        }
        String institucional = correo.substring(correo.length() - 12, correo.length());
        if (institucional.equals("@ufps.edu.co")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validarCorreoCalendario(String correo) {
        if (correo.length() < 11) {
            return false;
        }
        String calendario = correo.substring(correo.length() - 10, correo.length());
        if (calendario.equals("@gmail.com")) {
            return true;
        } else {
            return false;
        }
    }
}
