package co.ufps.edu.dao;

import java.util.LinkedList;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import co.ufps.edu.bd.SpringDbMgr;
import co.ufps.edu.dto.EnlaceDeInteres;

/**
 * Clase que permite acceder a la capa de datos en el entorno de enlace de
 * interés.
 *
 * @author UFPS
 */
public class EnlaceDeInteresDao {

    private SpringDbMgr springDbMgr;

    /**
     * Constructor de la clase en donde se inicializan las variables.
     */
    public EnlaceDeInteresDao() {
        springDbMgr = new SpringDbMgr();
    }

    /**
     * Método que consulta en base de datos todas los enlaces existentes.
     *
     * @return Una lista con todos los enlaces.
     */
    public List<EnlaceDeInteres> getEnlacesDeInteres() {
        // Lista para retornar con los datos.
        List<EnlaceDeInteres> enlacesDeInteres = new LinkedList<>();
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM enlaceinteres ORDER BY id ASC");
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            // Objeto en el que sera guardada la informacion del registro.
            EnlaceDeInteres enlaceDeInteres = new EnlaceDeInteres();
            enlaceDeInteres.setId(sqlRowSet.getLong("id"));
            enlaceDeInteres.setNombre(sqlRowSet.getString("nombre"));
            enlaceDeInteres.setUrl(sqlRowSet.getString("url"));
            // Se guarda el registro para ser retornado.
            enlacesDeInteres.add(enlaceDeInteres);
        }
        // Se retornan los contenidos obtenidos desde base de datos.
        return enlacesDeInteres;
    }

    /**
     * Método que registra un enlace de interés en base de datos
     *
     * @param enlaceDeInteres Objeto con todos los datos del enlace a registrar.
     *
     * @return El resultado de la acción.
     */
    public String registrarEnlaceDeInteres(EnlaceDeInteres enlaceDeInteres) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("nombre", enlaceDeInteres.getNombre());
        map.addValue("url", enlaceDeInteres.getUrl());
        // Se arma la sentencia de registro de base de datos.
        String query = "INSERT INTO enlaceinteres(nombre, url) VALUES(:nombre, :url)";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al registrar el enlace de interés.";
        }
        // Si hay filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Registro exitoso"
                : "La información del enlace de interés no se puede registrar.";
    }

    /**
     * Método que consulta en base de datos la información de un enlace de
     * interés.
     *
     * @param idEnlaceDeInteres Identificador del enlace.
     *
     * @return La información de un enlace de interés en un objeto.
     */
    public EnlaceDeInteres obtenerEnlaceDeInteresPorId(long idEnlaceDeInteres) {
        EnlaceDeInteres enlaceDeInteres = new EnlaceDeInteres();
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", idEnlaceDeInteres);
        // Se arma la sentencia de registro de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM enlaceinteres WHERE id = :id", map);
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            // Objeto en el que sera guardada la informacion del registro obtenido.
            enlaceDeInteres.setId(sqlRowSet.getLong("id"));
            enlaceDeInteres.setNombre(sqlRowSet.getString("nombre"));
            enlaceDeInteres.setUrl(sqlRowSet.getString("url"));
        }
        return enlaceDeInteres;
    }

    /**
     * Método que consulta en base de datos la información de un enlace de
     * interés.
     *
     * @param enlaceDeInteres Objeto que contiene la información del enlace a
     * editar.
     *
     * @return La información de un enlace de interés en un objeto.
     */
    public String editarEnlaceDeInteres(EnlaceDeInteres enlaceDeInteres) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", enlaceDeInteres.getId());
        map.addValue("nombre", enlaceDeInteres.getNombre());
        map.addValue("url", enlaceDeInteres.getUrl());
        // Se arma la sentencia de registro de base de datos.
        String query = "UPDATE enlaceinteres SET nombre = :nombre, url = :url  WHERE id = :id";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al actualizar el enlace de interés.";
        }
        // Si hay filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Actualizacion exitosa"
                : "El enlace de interés no se puede actualizar, revise que se este ingresando información válida.";
    }

    /**
     * Método que elimina de base de datos la información de un enlace de
     * interés.
     *
     * @param enlaceDeInteres Objeto que contiene la información del enlace a
     * eliminar.
     *
     * @return Si el resultado de la acción es exitoso.
     */
    public String eliminarEnlaceDeInteres(EnlaceDeInteres enlaceDeInteres) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", enlaceDeInteres.getId());
        // Se arma la sentencia de registro de base de datos.
        String query = "DELETE FROM enlaceinteres WHERE id = :id";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al eliminar el enlace de interés.";
        }
        // Si hay filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Eliminacion exitosa" : "El enlace de interés no se puede eliminar";
    }

    /*
     * Método que obtiene la cantidad de enlaces registrados.
     *
     * @return Cantidad de registros.
     */
    public int getCantidadEnlacesDeInteres() {
        int cant = 0;
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT COUNT(id) cantidad FROM enlaceinteres ");
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            cant = sqlRowSet.getInt("cantidad");
        }
        return cant;
    }
}
