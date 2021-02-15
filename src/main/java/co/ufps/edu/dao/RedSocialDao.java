package co.ufps.edu.dao;

import java.util.LinkedList;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import co.ufps.edu.bd.SpringDbMgr;
import co.ufps.edu.dto.RedSocial;

/**
 * Clase que permite acceder a la capa de datos en el entorno de redes sociales.
 *
 * @author UFPS
 */
public class RedSocialDao {

    private SpringDbMgr springDbMgr;

    /**
     * Constructor de la clase en donde se inicializan las variables.
     */
    public RedSocialDao() {
        springDbMgr = new SpringDbMgr();
    }

    /**
     * Método que consulta en base de datos todos las redes sociales existentes.
     *
     * @return Una lista con todas las redes sociales.
     */
    public List<RedSocial> getRedesSociales() {
        List<RedSocial> redesSociales = new LinkedList<>();
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM redsocial ORDER BY id DESC");
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            // Objeto en el que se guarda la información del registro.
            RedSocial redSocial = new RedSocial();

            redSocial.setId(sqlRowSet.getLong("id"));
            redSocial.setNombre(sqlRowSet.getString("nombre"));
            redSocial.setUrl(sqlRowSet.getString("url"));
            redSocial.setLogo(sqlRowSet.getString("logo"));

            // Se guarda el registro para ser retornado.
            redesSociales.add(redSocial);
        }
        return redesSociales;
    }

    /**
     * Método que registra una red social en base de datos.
     *
     * @param redSocial Objeto con todos los datos de la red social a registrar.
     * 
     * @return El resultado de la acción.
     */
    public String registrarRedSocial(RedSocial redSocial) {
        // Se arma la sentencia de base de datos.
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("nombre", redSocial.getNombre());
        map.addValue("url", redSocial.getUrl());
        map.addValue("logo", redSocial.getLogo());
        // Se arma la sentencia de base de datos.
        String query = "INSERT INTO redsocial(nombre, url, logo) VALUES(:nombre, :url, :logo)";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al registrar la red social.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Registro exitoso"
                : "La red social no se puede registrar, revise que se este ingresando información válida.";
    }

    /**
     * Método que consulta en base de datos la información de una red social.
     *
     * @param idRedSocial Identificador de la red social.
     * 
     * @return La información de una red social en un objeto.
     */
    public RedSocial obtenerRedSocialPorId(long idRedSocial) {
        RedSocial redSocial = new RedSocial();
        // Se arma la sentencia de base de datos.
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", idRedSocial);
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM redsocial WHERE id = :id", map);
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            // Se almacenan los datos de la red social.
            redSocial.setId(sqlRowSet.getLong("id"));
            redSocial.setNombre(sqlRowSet.getString("nombre"));
            redSocial.setUrl(sqlRowSet.getString("url"));
            redSocial.setLogo(sqlRowSet.getString("logo"));
        }
        return redSocial;
    }

    /**
     * Método que edita en base de datos la información de una red social.
     *
     * @param redSocial Objeto con la información de la red social.
     * 
     * @return Mesaje de confirmación del proceso.
     */
    public String editarRedSocial(RedSocial redSocial) {
        // Se arma la sentencia de base de datos.
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", redSocial.getId());
        map.addValue("nombre", redSocial.getNombre());
        map.addValue("url", redSocial.getUrl());
        map.addValue("logo", redSocial.getLogo());
        // Se arma la sentencia de base de datos.
        String query = "UPDATE redsocial SET nombre = :nombre, url = :url, logo = :logo WHERE id = :id";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al actualizar la red social.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Actualizacion exitosa"
                : "La red social no se puede actualizar, revise que se este ingresando información válida.";
    }

    /**
     * Método que elimina en base de datos la información de una red social.
     *
     * @param redSocial Objeto con la información de la red social.
     * 
     * @return Mesaje de confirmación del proceso.
     */
    public String eliminarRedSocial(RedSocial redSocial) {
        // Se arma la sentencia de base de datos.
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", redSocial.getId());
        // Se arma la sentencia de base de datos.
        String query = "DELETE FROM redsocial WHERE id = :id";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al eliminar la red social.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Eliminacion exitosa" : "La red social no se puede eliminar.";
    }

    /**
     * Método que obtiene la cantidad de redes sociales registradas.
     *
     * @return Cantidad de redes sociales registradas.
     */
    public int getCantidadRedesSociales() {
        int cant = 0;
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT COUNT(id) cantidad FROM redsocial");
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            // Se retorna la cantidad de noticias desde base de datos.
            cant = sqlRowSet.getInt("cantidad");
        }
        return cant;
    }
}
