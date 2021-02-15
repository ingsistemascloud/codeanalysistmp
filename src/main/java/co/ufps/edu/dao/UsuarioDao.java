package co.ufps.edu.dao;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import co.ufps.edu.bd.SpringDbMgr;
import co.ufps.edu.dto.Usuario;
import co.ufps.edu.util.MailUtil;
import co.ufps.edu.util.PassUtil;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase que permite acceder a la capa de datos en el entorno de usuarios.
 *
 * @author UFPS
 */
@Component
public class UsuarioDao {

    private SpringDbMgr springDbMgr;
    private MailUtil mailUtil;
    private JavaMailSenderImpl javaMailSender;
    private PassUtil passUtil;

    /**
     * Constructor de la clase en donde se inicializan las variables.
     */
    public UsuarioDao() {
        passUtil = new PassUtil();
        springDbMgr = new SpringDbMgr();
        mailUtil = new MailUtil();
    }

    /**
     * M�todo que consulta en base de datos todos los usuarios admin existentes.
     *
     * @return Una lista con todos los usuarios admin.
     */
    public List<Usuario> getUsuariosAdmin() throws Exception {
        // Lista para retornar con los datos.
        List<Usuario> usuarios = new LinkedList<>();
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM usuario WHERE rol='Admin' ORDER BY id DESC");
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            // Objeto en el que se guarda la informaci�n del registro.
            Usuario usuario = new Usuario();

            usuario.setCodigo(sqlRowSet.getString("id"));
            usuario.setCorreo(sqlRowSet.getString("correoInstitucional"));
            usuario.setContrasena(passUtil.Desencriptar(sqlRowSet.getString("contrase�a")));
            usuario.setRol(sqlRowSet.getString("rol"));
            usuario.setActivo(sqlRowSet.getInt("activo"));
            // Se guarda el registro para ser retornado.
            usuarios.add(usuario);
        }
        return usuarios;
    }

    /**
     * M�todo que consulta en base de datos todos los usuarios existentes.
     *
     * @return Una lista con todos los usuarios.
     */
    public List<Usuario> getUsuarios() throws Exception {
        // Lista para retornar con los datos.
        List<Usuario> usuarios = new LinkedList<>();
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM usuario ORDER BY id DESC");
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            // Objeto en el que se guarda la informaci�n del registro.
            Usuario usuario = new Usuario();

            usuario.setCodigo(sqlRowSet.getString("id"));
            usuario.setCorreo(sqlRowSet.getString("correoInstitucional"));
            usuario.setCorreoCalendario(sqlRowSet.getString("correoCalendario"));
            usuario.setContrasena(passUtil.Desencriptar(sqlRowSet.getString("contrase�a")));
            usuario.setRol(sqlRowSet.getString("rol"));
            // Se guarda el registro para ser retornado.
            usuarios.add(usuario);
        }
        return usuarios;
    }

    /**
     * M�todo que consulta en base de datos el usuario superAdmin activo.
     *
     * @return Una lista con todos los usuarios.
     * @throws java.lang.Exception
     */
    public Usuario getUsuarioSuperAdmin() throws Exception {
        // Lista para retornar con los datos.
        Usuario usuario = new Usuario();
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM usuario WHERE rol='SuperAdmin'");
        // Se consulta si la actividad existe.
        if (sqlRowSet.next()) {
            // Se almacenan los datos del usuario.
            usuario.setCodigo(sqlRowSet.getString("id"));
            usuario.setCorreo(sqlRowSet.getString("correoInstitucional"));
            usuario.setContrasena(passUtil.Desencriptar(sqlRowSet.getString("contrase�a")));
            usuario.setActivo(sqlRowSet.getInt("activo"));
        }
        return usuario;
    }

    /**
     * M�todo que permite activar un usuario de acuerdo a un c�digo.
     *
     * @param usuario Objeto en donde con la informaci�n a actualizar.
     *
     * @return Resultado de la accipon.
     */
    public String activarUsuario(Usuario usuario) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("correo", usuario.getCorreo());
        map.addValue("codigoAct", usuario.getCodActivo());
        // Se arma la sentencia de base de datos.
        String query = "UPDATE usuario SET activo = 1, codigoActivar='' WHERE correoInstitucional =:correo AND codigoActivar =:codigoAct";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al activar el usuario.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Activacion exitosa"
                : "El usuario no se puede activar, revise que se este ingresando informaci�n v�lida.";
    }

    /**
     * M�todo que edita un usuario.
     *
     * @param usuario Objeto en donde con la informaci�n a actualizar.
     *
     * @return Resultado de la acci�n.
     */
    public String editarUsuario(Usuario usuario) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("codigo", usuario.getCodigo());
        map.addValue("codigoNuevo", usuario.getCodigoNuevo());
        map.addValue("correo", usuario.getCorreo());
        map.addValue("correoCalendario", usuario.getCorreoCalendario());
        map.addValue("contraNueva", passUtil.Encriptar(usuario.getContrasenaNueva()));
        map.addValue("contraNueva2", passUtil.Encriptar(usuario.getContrasenaNueva2()));
        // Se arma la sentencia de base de datos.
        String query = "UPDATE usuario SET ";
        if (!usuario.getCodigoNuevo().isEmpty()) {
            query += "id = :codigoNuevo, ";
        }
        if (!usuario.getCorreo().isEmpty()) {
            if (usuario.validarCorreo(usuario.getCorreo())) {
                query += "correoInstitucional = :correo, ";
            } else {
                return "El correo intitucional debe ser tipo ejemplo@ufps.edu.co";
            }
        }
        if (!usuario.getCorreoCalendario().isEmpty()) {
            if (usuario.validarCorreoCalendario(usuario.getCorreoCalendario())) {
                query += "correoCalendario = :correoCalendario, ";
            } else {
                return "El correo calendario debe ser tipo ejemplo@gmail.com";
            }
        }
        if (!usuario.getContrasenaNueva().isEmpty()) {
            if (usuario.contraValida(usuario.getContrasenaNueva())) {
                if (usuario.seRepiten()) {
                    query += "contrase�a = :contraNueva, ";
                } else {
                    return "Las contrase�as no coinciden.";
                }
            } else {
                return "La contrase�a debe tener m�nimo 4 caracteres n�mericos, 4 caracteres en minuscula y 2 caracteres en may�scula.";
            }
        }
        query += "WHERE id = :codigo";
        query = query.replace(", WHERE", " WHERE");
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al actualizar el usuario.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Actualizacion exitosa"
                : "El usuario no se puede editar, revise que se este ingresando informaci�n v�lida.";
    }

    /**
     * M�todo que elimina un usuario.
     *
     * @param usuario Objeto en donde con la informaci�n a eliminar.
     *
     * @return Resultado de la acci�n.
     */
    public String eliminarUsuario(Usuario usuario) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", usuario.getCodigo());
        // Se arma la sentencia de base de datos.
        String query = "DELETE FROM usuario WHERE id = :id";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al eliminar el usuario.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Eliminacion exitosa"
                : "El usuario no se puede eliminar.";
    }

    /**
     * M�todo que envia un correo con la contrase�a del usuario.
     *
     * @param correo Correo del usuario que desea recuperar la contrase�a
     *
     * @return Resulatdo de la acci�n.
     */
    public String enviarCorreo(String correo) throws Exception {
        int cant = 0;
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("correo", correo);
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT Contrase�a pass FROM usuario where correoInstitucional = :correo", map);
        String pass = "";
        if (sqlRowSet.next()) {
            pass = sqlRowSet.getString("pass");
        }
        // Mensaje de error, si lo hay.
        String mensaje = "El correo no esta registrado en el sistema. Contacte al administrador.";
        if (!pass.equals("")) {
            // Se arma el correo para enviarse.
            String subject = "Recuperaci�n de contrase�a ADMIN-UFPS";
            String body = "Hola, la contrase�a para iniciar sesi�n a trav�s de su cuenta es: " + passUtil.Desencriptar(pass);
            String to[] = {correo};
            try {
                mailUtil.sendFromGMail(to, subject, body);
                mensaje = "Actualizacion";
            } catch (Exception e) {
                e.printStackTrace();
                mensaje = "Correo no autorizado. Debes permitir el acceso de aplicaciones no seguras en la configuraci�n de google.";
            }
        }
        return mensaje;
    }

    /**
     * M�todo que envia un correo con el c�digo del usuario.
     *
     * @param correo Correo del usuario que desea obtener el c�digo.
     * @param codigo C�digo a enviar.
     *
     * @return Resulatdo de la acci�n.
     */
    public String enviarCodigo(String correo, String codigo) throws Exception {
        int cant = 0;
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("correo", correo);
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT Contrase�a pass FROM usuario where correoInstitucional = :correo", map);
        String pass = "";
        if (sqlRowSet.next()) {
            pass = sqlRowSet.getString("pass");
        }
        // Mensaje de error, si lo hay.
        String mensaje = "El correo no esta registrado en el sistema. Contacte al administrador.";
        if (!pass.equals("")) {
            // Se arma el correo para enviarse.
            String subject = "Codigo de activacion de cuenta SUPERADMIN-UFPS";
            String body = "Hola, el c�digo de activacion es: " + codigo;
            String to[] = {correo};
            SimpleMailMessage smm = new SimpleMailMessage();

            smm.setFrom("recuperacion.webapp@gmail.com");
            smm.setTo(correo);
            smm.setSubject(subject);
            smm.setText(body);
            try {
                mailUtil.sendFromGMail(to, subject, body);
                mensaje = "Actualizacion";
            } catch (Exception e) {
                e.printStackTrace();
                mensaje = "Correo no autorizado. Debes permitir el acceso de aplicaciones no seguras en la configuraci�n de google.";
            }
        }
        return mensaje;
    }

    /**
     * M�todo que registra el c�digo del usuario para activar.
     *
     * @param correo Correo del usuario al que se le registra el c�digo de
     * activaci�n.
     * @param codigo C�digo a enviar.
     *
     * @return Resulatdo de la acci�n.
     */
    public String registrarCodigoActivacion(String correo, String codigo) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("correo", correo);
        map.addValue("codigo", codigo);
        // Se arma la sentencia de base de datos.
        String query = "UPDATE usuario SET codigoActivar = :codigo WHERE correoInstitucional = :correo";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al actualizar el codigo.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Actualizacion exitosa"
                : "Error al actualizar el c�digo. Contacte al administrador del sistema.";
    }

    /**
     * M�todo que registra un usuario en base de datos
     *
     * @param usuario Objeto con todos los datos del usuario a registrar.
     *
     * @return El resultado de la acci�n.
     */
    public String registrarUsuario(Usuario usuario) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("codigo", usuario.getCodigo());
        map.addValue("correo", usuario.getCorreo());
        map.addValue("rol", usuario.getRol());
        map.addValue("activo", usuario.getActivo());
        map.addValue("contrasenaNueva", passUtil.Encriptar(usuario.getContrasenaNueva()));
        map.addValue("contrasenaNueva2", passUtil.Encriptar(usuario.getContrasenaNueva2()));
        map.addValue("correoCalendario", usuario.getCorreoCalendario());
        // Se arma la sentencia de base de datos.
        String query = "INSERT INTO usuario(id,correoInstitucional,correoCalendario,rol,contrase�a,activo) VALUES(:codigo,:correo,:correoCalendario,:rol,:contrasenaNueva,:activo)";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al registrar el usuario.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Registro exitoso"
                : "El usuario no se puede registrar, revise que se este ingresando informaci�n v�lida.";

    }

    /**
     * M�todo que consulta en base de datos la informaci�n de un usuario dado.
     *
     * @param codigo Identificador del usuario.
     *
     * @return La informaci�n del usuario en un objeto.
     */
    public Usuario obtenerUsuarioPorId(long codigo) throws Exception {
        // Lista para retornar con los datos.
        Usuario usuario = new Usuario();
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", codigo);
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM usuario WHERE id = :id", map);
        // Se consulta si la actividad existe.
        if (sqlRowSet.next()) {
            // Se almacenan los datos del usuario.
            usuario.setCodigo(sqlRowSet.getString("id"));
            usuario.setCorreo(sqlRowSet.getString("correoInstitucional"));
            usuario.setContrasena(passUtil.Desencriptar(sqlRowSet.getString("contrase�a")));
            usuario.setActivo(sqlRowSet.getInt("activo"));
        }
        return usuario;
    }

    /**
     * Metodo que consulta en base de datos la informaci�n de un usuario dado.
     *
     * @param correo Correo del usuario.
     *
     * @return La informaci�n de una actividad en un objeto.
     */
    public Usuario obtenerUsuarioPorCorreo(String correo) throws Exception {
        // Lista para retornar con los datos.
        Usuario usuario = new Usuario();
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("correo", correo);
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM usuario WHERE correoInstitucional = :correo", map);
        // Se consulta si el usuario existe.
        if (sqlRowSet.next()) {
            // Se almacenan los datos del usuario.
            usuario.setCodigo(sqlRowSet.getString("id"));
            usuario.setCorreo(sqlRowSet.getString("correoInstitucional"));
            usuario.setCorreoCalendario(sqlRowSet.getString("correoCalendario"));
            usuario.setContrasena(passUtil.Desencriptar(sqlRowSet.getString("contrase�a")));
            usuario.setRol(sqlRowSet.getString("rol"));
            usuario.setActivo(sqlRowSet.getInt("activo"));
        }
        return usuario;
    }

    /**
     * M�todo que obtiene la cantidad de usuarios admin registrados.
     *
     * @return Cantidad de usuarios registrados.
     */
    public int getCantidadUsuarios() {
        int cant = 0;
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT COUNT(id) cantidad FROM usuario WHERE rol='Admin'");
        if (sqlRowSet.next()) {
            cant = sqlRowSet.getInt("cantidad");
        }
        return cant;
    }

    /**
     * M�todo que activa o inactiva un usuario de acuerdo al c�digo.
     *
     * @param codigo C�digo del usuario.
     *
     * @return Resultado de la acci�n.
     */
    public String activarDesactivarUsuario(Long codigo) throws Exception {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("codigo", codigo);
        Usuario u = obtenerUsuarioPorId(codigo);
        String query = "";
        int result = 0;
        // Si el usuario esta activo se inactiva, si esta inactivo, se activa el usuario.
        if (u.getActivo() == 1) {
            query = "UPDATE usuario SET activo=0 WHERE id = :codigo";
            try {
                result = springDbMgr.executeDml(query, map);
            } catch (Exception e) {
                e.printStackTrace();
                return "Error al actualizar el usuario.";
            }
        } else {
            query = "UPDATE usuario SET activo=1 WHERE id = :codigo";
            try {
                result = springDbMgr.executeDml(query, map);
            } catch (Exception e) {
                e.printStackTrace();
                return "Error al actualizar el usuario.";
            }
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Actualizacion exitosa"
                : "Error al actualizar el usuario. Contacte al administrador del sistema.";
    }

    /**
     * M�todo que genera le c�digo de activaci�n.
     *
     * @param codigo C�digo del usuario.
     * @param correo Correo del usuario.
     *
     * @return C�digo para activar.
     */
    public String codigoActivar(String codigo, String correo) throws Exception {
        String numeros = "";
        for (int i = 0; i < 5; i++) {
            int v = (int) (Math.random() * 9) + 1;
            numeros += v;
        }
        String c = passUtil.Encriptar(codigo) + numeros + passUtil.Encriptar(correo);
        return c;
    }
}
