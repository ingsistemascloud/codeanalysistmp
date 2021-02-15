package co.ufps.edu.dao;

import java.util.LinkedList;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import co.ufps.edu.bd.SpringDbMgr;
import co.ufps.edu.dto.Contacto;

/**
 * Clase que permite acceder a la capa de datos en el entorno de contacto.
 * 
 * @author UFPS
 */
public class ContactoDao {

    private SpringDbMgr springDbMgr;
    
    /**
   * Constructor de la clase en donde se inicializan las variables.
   */
    public ContactoDao() {
        springDbMgr = new SpringDbMgr();
    }

    /**
     * Método que consulta en base de datos todos los contactos existentes.
     *
     * @return Una lista con todos los contactos.
     */
    public List<Contacto> getContactos() {
        // Lista para retornar con los datos
        List<Contacto> contactos = new LinkedList<>();
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM contacto ORDER BY id DESC");
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            // Objeto en el que sera guardada la informacion del registro
            Contacto contacto = new Contacto();
            contacto.setId(sqlRowSet.getLong("id"));
            contacto.setNombre(sqlRowSet.getString("nombre"));
            // Se guarda el registro para ser retornado.
            contactos.add(contacto);
        }
        // Se retornan todos los contactos desde base de datos.
        return contactos;
    }

    /**
     * Método que registra un contacto en base de datos
     *
     * @param contacto Objeto con todos los datos del contacto a registrar.
     * 
     * @return El resultado de la acción.
     */
    public String registrarConacto(Contacto contacto) {
        SpringDbMgr springDbMgr = new SpringDbMgr();
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("nombre", contacto.getNombre());
        // Se arma la sentencia de base de datos.
        String query = "INSERT INTO contacto(nombre) VALUES(:nombre)";
        int result = 0;
        try {
            // Se ejecuta la sentencia
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al registrar el contacto.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Registro exitoso"
                : "El contacto no se puede registrar, revise que se este ingresando información válida.";

    }

    /**
     * Método que consulta en base de datos la información de un contacto.
     *
     * @param idContacto Identificador de contacto.
     * 
     * @return La información de un contacto en un objeto.
     */
    public Contacto obtenerContactoPorId(long idContacto) {
        // Objeto para retornar con los datos
        Contacto contacto = new Contacto();
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", idContacto);
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM contacto WHERE id = :id", map);
        // Se valida si hubo registro.
        if (sqlRowSet.next()) {
            // Se almacenan los datos de contacto.
            contacto.setId(sqlRowSet.getLong("id"));
            contacto.setNombre(sqlRowSet.getString("nombre"));
        }
        // Se retorna el contacto desde base de datos
        return contacto;
    }

    /**
     * Método que edita en base de datos la información de un contacto.
     *
     * @param contacto Objeto del contacto a editar.
     * 
     * @return La información de un contacto en un objeto.
     */
    public String editarContacto(Contacto contacto) {
        SpringDbMgr springDbMgr = new SpringDbMgr();
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", contacto.getId());
        map.addValue("nombre", contacto.getNombre());
        // Se arma la sentencia de base de datos.
        String query = "UPDATE contacto SET nombre = :nombre  WHERE id = :id";
        int result = 0;
        try {
            // Se ejecutar la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al actualizar el contacto.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Actualizacion exitosa"
                : "El contacto no se puede actualizar, revise que se este ingresando información válida.";
    }

    /**
     * Método que elimina en base de datos la información de un contacto.
     *
     * @param contacto Objeto del contacto a eliminar.
     * 
     * @return La información de un contacto en un objeto.
     */
    public String eliminarContacto(Contacto contacto) {
        SpringDbMgr springDbMgr = new SpringDbMgr();
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", contacto.getId());
        // Se arma la sentencia de base de datos.
        String query = "DELETE FROM contacto WHERE id = :id";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al eliminar el contacto.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Eliminacion exitosa" : "No fue posible eliminar el contacto";
    }

    /*
     * Método que obtiene la cantidad de contactos registrados.
     *
     * @return Cantidad de contactos registrados.
     */
    public int getCantidadContactos() {
        int cant = 0;
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT COUNT(id) cantidad FROM contacto");
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            cant = sqlRowSet.getInt("cantidad");
        }
        return cant;
    }
}
