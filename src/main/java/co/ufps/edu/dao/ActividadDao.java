package co.ufps.edu.dao;

import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.Charsets;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import co.ufps.edu.bd.SpringDbMgr;
import co.ufps.edu.constantes.Constantes;
import co.ufps.edu.dto.Actividad;
import co.ufps.edu.dto.Contenido;
import co.ufps.edu.dto.TipoContenido;

/**
 * Clase que permite acceder a la capa de datos en el entorno de actividades.
 *
 * @author UFPS
 */
@Component
public class ActividadDao {

    private SpringDbMgr springDbMgr;

    /**
     * Constructor de la clase en donde se inicializan las variables.
     */
    public ActividadDao() {
        springDbMgr = new SpringDbMgr();
    }

    /**
     * Método que obtiene el numero de actividades guardadas en base de datos.
     *
     * @return La cantidad de actividades registradas.
     */
    public int getCantidadActividades() {
        int cant = 0;
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT COUNT(id) cantidad FROM proximaactividad");
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            cant = sqlRowSet.getInt("cantidad");
        }
        return cant;
    }

    /**
     * Método que consulta en base de datos todas las actividades existentes y
     * las devuelve ordenadamente.
     *
     * @return Una lista con todas las actividades.
     */
    public List<Actividad> getActividades() {
        // Lista para retornar con los datos.
        List<Actividad> actividades = new LinkedList<>();
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM proximaactividad ORDER BY fechaInicial DESC");
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            // Objeto en el que sera guardada la informacion del registro.
            Actividad actividad = new Actividad();
            actividad.setId(sqlRowSet.getLong("id"));
            actividad.setNombre(sqlRowSet.getString("nombre"));
            actividad.setLugar(sqlRowSet.getString("lugar"));
            actividad.setFechaInicial(sqlRowSet.getDate("fechaInicial"));
            actividad.setFechaFinal(sqlRowSet.getDate("fechaFinal"));
            actividad.crearFechaFormato();
            // Se guarda el registro para ser retornado.
            actividades.add(actividad);
        }
        // Se retornan todos las actividades desde base de datos.
        return actividades;
    }

    /**
     * Método que registra en base de datos una actividad.
     *
     * @param actividad Objeto que contiene la información de la actividad a
     * registrar.
     *
     * @return Una lista con todas las actividades.
     */
    public String registrarActividad(Actividad actividad) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("nombre", actividad.getNombre());
        map.addValue("lugar", actividad.getLugar());
        map.addValue("fechaInicial", actividad.getFechaInicial());
        map.addValue("fechaFinal", actividad.getFechaFinal());
        // Se arma la sentencia de registro de base de datos.
        String query = "INSERT INTO proximaactividad(nombre,lugar,fechaInicial,fechaFinal) VALUES(:nombre,:lugar,:fechaInicial,:fechaFinal)";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al registrar la actividad.";
        }
        // Si hay filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Registro exitoso"
                : "La actividad no se puede registrar, revise que se este ingresando información válida.";
    }

    /**
     * Método que consulta en base de datos la informacion de una actividad dado
     * un id.
     *
     * @param idActividad Identificador de la actividad.
     *
     * @return La información de una actividad en un objeto.
     */
    public Actividad obtenerActividadPorId(long idActividad) {
        // Objeto para retornar con los datos.
        Actividad actividad = new Actividad();
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", idActividad);
        // Se arma la sentencia de registro de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM proximaactividad WHERE id = :id", map);
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            // Se almacenan los datos de la actividad.
            actividad.setId(sqlRowSet.getLong("id"));
            actividad.setNombre(sqlRowSet.getString("nombre"));
            actividad.setLugar(sqlRowSet.getString("lugar"));
            actividad.setFechaInicial(sqlRowSet.getDate("fechaInicial"));
            actividad.setFechaFinal(sqlRowSet.getDate("fechaFinal"));
            actividad.crearFechaFormato();
        }
        // Se retorna la actividad desde base de datos.
        return actividad;
    }

    /**
     * Método que edita en base de datos una actividad.
     *
     * @param actividad Objeto que contiene la información de la actividad a
     * editar.
     *
     * @return Una lista con todas las actividades.
     */
    public String editarActividad(Actividad actividad) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", actividad.getId());
        map.addValue("nombre", actividad.getNombre());
        map.addValue("lugar", actividad.getLugar());
        map.addValue("fechaInicial", actividad.getFechaInicial());
        map.addValue("fechaFinal", actividad.getFechaFinal());
        // Se arma la sentencia de registro de base de datos.
        String query = "UPDATE proximaactividad SET nombre = :nombre, lugar = :lugar, fechaInicial = :fechaInicial, fechaFinal = :fechaFinal WHERE id = :id";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al actualizar la actividad.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Actualizacion exitosa"
                : "La actividad no se puede actualizar, revise que se este ingresando información válida.";
    }

    /**
     * Método que elimana en base de datos una actividad
     *
     * @param actividad Objeto que contiene la información de la actividad a
     * eliminar
     *
     * @return Una lista con todas las actividades
     */
    public String eliminarActividad(Actividad actividad) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", actividad.getId());
        // Se arma la sentencia de registro de base de datos.
        String query = "DELETE FROM proximaactividad WHERE id = :id";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al eliminar la actividad.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Eliminacion exitosa"
                : "La actividad no se puede eliminar.";
    }

    /**
     * Método que consulta en base de datos las primeras 4 actividades
     * acomodadas descendentemente por fechaInicial.
     *
     * @return Una lista con todas las actividades.
     */
    public List<Actividad> getUltimasActividades() {
        // Lista para retornar con los datos.
        List<Actividad> actividades = new LinkedList<>();
        // Se arma la sentencia de registro de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM proximaactividad ORDER BY fechaInicial DESC LIMIT 0, 4");
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            // Objeto en el que se guarda la información del registro.
            Actividad actividad = new Actividad();
            actividad.setId(sqlRowSet.getLong("id"));
            actividad.setNombre(sqlRowSet.getString("nombre"));
            actividad.setLugar(sqlRowSet.getString("lugar"));
            actividad.setFechaInicial(sqlRowSet.getDate("fechaInicial"));
            actividad.setFechaFinal(sqlRowSet.getDate("fechaFinal"));
            actividad.crearFechaFormato();
            cargarContenido(actividad);
            // Se guarda el registro para ser retornado.
            actividades.add(actividad);
        }
        // Se retornan todas las actividades desde base de datos.
        return actividades;

    }

    /**
     * Método que carga el contenido de una actividad.
     *
     * @param actividad Objeto que contiene la informacion de la actividad a
     * cargar.
     *
     */
    private void cargarContenido(Actividad actividad) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", actividad.getId());
        map.addValue("tipo", Constantes.ACTIVIDAD);
        // Se arma la sentencia de registro de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT contenido.id idContenido,"
                + " contenido.contenido contenido,"
                + " contenido.TipoContenido_id tipoContenido"
                + " FROM proximaactividad"
                + " INNER JOIN contenido ON contenido.asociacion = proximaactividad.id"
                + " WHERE proximaactividad.id = :id"
                + " AND contenido.tipoasociacion = :tipo", map);
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            // Objeto en el que sera guardada la informacion del registro.
            Contenido contenido = new Contenido();
            contenido.setId(sqlRowSet.getLong("idContenido"));
            byte[] a = (byte[]) sqlRowSet.getObject("contenido");
            String res = new String(a, Charsets.UTF_8);
            contenido.setContenido(res);
            TipoContenido tipoContenido = new TipoContenido();
            tipoContenido.setId(sqlRowSet.getLong("tipoContenido"));
            contenido.setTipoContenido(tipoContenido);
            // Se guarda el registro para ser retornado.
            actividad.setContenido(contenido);
        }
    }
}
