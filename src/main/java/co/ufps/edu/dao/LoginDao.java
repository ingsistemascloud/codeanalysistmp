package co.ufps.edu.dao;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import co.ufps.edu.bd.SpringDbMgr;
import co.ufps.edu.util.PassUtil;

/**
 * Clase que permite acceder a la capa de datos en el entorno de acceso.
 * 
 * @author UFPS 
 */
public class LoginDao {

    SpringDbMgr springDbMgr = new SpringDbMgr();

    /**
     * Método que permite autenticar o logear a un usuario de acuerdo al correo y contraseña del mismo.
     * @param correo Correo del usuario a autenticar.
     * @param contraseña COntraseña del usuario a autenticar.
     * 
     * @return El rol del usuario, verificando si el usuario fue autenticado.
     */
    public String authenticate(String correo, String contraseña) {
        UsuarioDao u = new UsuarioDao();
        try {
            // Se agregan los datos del registro (nombreColumna/Valor).
            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
            mapSqlParameterSource.addValue("correo", correo);
            mapSqlParameterSource.addValue("pass", PassUtil.Encriptar(contraseña));
            // Se arma la sentencia de base de datos.
            SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT id FROM usuario"
                    + "	WHERE correoInstitucional = :correo AND Contraseña = :pass", mapSqlParameterSource);
            // Se verifica si hubo algún resultado.
            if ((sqlRowSet.next())) {
                // Si éxiste el usuario en base de datos se retorna el rol en señal de éxito.
                return u.obtenerUsuarioPorCorreo(correo).getRol();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }
}
