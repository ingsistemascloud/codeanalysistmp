package co.ufps.edu.dao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import co.ufps.edu.bd.SpringDbMgr;
import co.ufps.edu.dto.Galeria;
import co.ufps.edu.dto.Imagen;
import co.ufps.edu.dto.ResultDB;

/**
 * Clase que permite acceder a la capa de datos en el entorno de galer�as.
 * 
 * @author UFPS
 */
@Component
public class GaleriaDao {

    private SpringDbMgr springDbMgr;

    /**
     * Constructor de la clase en donde se inicializan las variables.
     */
    public GaleriaDao() {
        springDbMgr = new SpringDbMgr();
    }

    /**
     * M�todo que consulta en base de datos todas las galer�as existentes.
     *
     * @return Una lista con todas las galer�as.
     */
    public List<Galeria> getGalerias() {
        // Lista para retornar con los datos.
        List<Galeria> galerias = new LinkedList<>();
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM galeria ORDER BY fecha DESC");
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            // Objeto en el que se guardada la informaci�n del registro.
            Galeria galeria = new Galeria();
            galeria.setId(sqlRowSet.getLong("id"));
            galeria.setNombre(sqlRowSet.getString("nombre"));
            galeria.setDescripcion(sqlRowSet.getString("descripcion"));
            galeria.setFecha(sqlRowSet.getDate("fecha"));
            guardarPrimeraImagen(galeria);
            // Se guarda el registro para ser retornado.
            galerias.add(galeria);
        }
        return galerias;
    }

    /**
     * M�todo que consulta en base de datos las ultimas galer�as existentes.
     *
     * @return Una lista con todas las galer�as.
     */
    public List<Galeria> getUltimasGalerias() {
        // Lista para retornar con los datos.
        List<Galeria> galerias = new LinkedList<>();
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM galeria ORDER BY fecha DESC LIMIT 0, 6");
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            // Objeto en el que sera guardada la informaci�n del registro.
            Galeria galeria = new Galeria();
            galeria.setId(sqlRowSet.getLong("id"));
            galeria.setNombre(sqlRowSet.getString("nombre"));
            galeria.setDescripcion(sqlRowSet.getString("descripcion"));
            galeria.setFecha(sqlRowSet.getDate("fecha"));
            guardarPrimeraImagen(galeria);
            // Se guarda el registro para ser retornado.
            galerias.add(galeria);
        }
        return galerias;
    }

    /**
     * M�todo que guarda una imagen en base de datos.
     * 
     * @param galeria Objeto con todos los datos de la galer�a a registrar.
     * 
     */
    private void guardarPrimeraImagen(Galeria galeria) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", galeria.getId());
        // Se arma la sentencia de registro de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM foto WHERE Galeria_id = :id LIMIT 0,1 ", map);
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            // Objeto en el que se guarda la informaci�n del registro.
            Object imagenBlob = sqlRowSet.getObject("contenido");
            galeria.setPrimeraImagen(new String((byte[]) imagenBlob));
        }
    }

    /**
     * M�todo que registra una galer�a en base de datos.
     *
     * @param galeria Objeto con todos los datos de la galer�a a registrar.
     * 
     * @return El resultado de la acci�n.
     */
    public String registrarGaleria(Galeria galeria) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("nombre", galeria.getNombre());
        map.addValue("descripcion", galeria.getDescripcion());
        map.addValue("fecha", galeria.getFecha());
        // Se arma la sentencia de registro de base de datos.
        String query = "INSERT INTO galeria(nombre, descripcion, fecha) VALUES(:nombre, :descripcion, :fecha)";
        ResultDB result = null;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDmlWithKey(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "La galer�a no se puede registrar, revise que se este ingresando informaci�n v�lida.";

        }
        // Se guardan las imagenes asociadas a la galer�a.
        if (guardarImagenes(result.getKey(), galeria.getImagenes()) == 0) {
            return "Error al guardar las imagenes.";
        }
        // Si hay filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result.getKey() > 0) ? "Registro exitoso"
                : "La galer�a no se puede registrar, revise que se este ingresando informaci�n v�lida.";
    }

    /**
     * M�todo que registra una imagen en base de datos.
     * 
     * @param key Llave para guardar la imagen.
     * @param imagenes Lista de imagenes.
     * 
     * @return El resultado de la acci�n en donde si es 1 se realizo la acci�n exitosamente.
     */
    private int guardarImagenes(long key, ArrayList<Imagen> imagenes) {
        int result = 0;
        for (Imagen imagen : imagenes) {
            // Se agregan los datos del registro (nombreColumna/Valor).
            MapSqlParameterSource map = new MapSqlParameterSource();
            map.addValue("Galeria_id", key);
            map.addValue("descripcion", imagen.getDescripcion());
            map.addValue("contenido", imagen.getImagen());
            // Se arma la sentencia de registro de base de datos.
            String query = "INSERT INTO foto(Galeria_id, descripcion, contenido) VALUES(:Galeria_id, :descripcion, :contenido)";
            try {
                // Se ejecuta la sentencia.
                result = springDbMgr.executeDml(query, map);
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
        return result;
    }

    /**
     * M�todo que consulta en base de datos la informaci�n de una galer�a.
     *
     * @param idGaleria Identificador de galer�a.
     * 
     * @return La informaci�n de una galer�a en un objeto.
     */
    public Galeria obtenerGaleriaPorId(long idGaleria) {
        Galeria galeria = new Galeria();
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", idGaleria);
        // Se arma la sentencia de registro de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM galeria WHERE id = :id", map);
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            // Se obtienen los datos de la galer�a.
            galeria.setId(sqlRowSet.getLong("id"));
            galeria.setNombre(sqlRowSet.getString("nombre"));
            galeria.setDescripcion(sqlRowSet.getString("descripcion"));
            galeria.setFecha(sqlRowSet.getDate("fecha"));
        }
        // Lista de imagenes obtenidas.
        List<Imagen> imagenes = getImagenesPorIDCompletas(galeria.getId());
        galeria.setImagenes(new ArrayList<>(imagenes));
        return galeria;
    }

    /**
     * M�todo que consulta en base de datos la informaci�n de una galer�a.
     *
     * @param galeria Identificador de galer�a.
     * 
     * @return La informaci�n de una galer�a en un objeto.
     */
    public String editarGaleria(Galeria galeria) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", galeria.getId());
        map.addValue("nombre", galeria.getNombre());
        map.addValue("descripcion", galeria.getDescripcion());
        map.addValue("fecha", galeria.getFecha());
        // Se arma la sentencia de registro de base de datos.
        String query = "UPDATE galeria SET nombre = :nombre, descripcion = :descripcion, fecha = :fecha  WHERE id = :id";
        int result = 0;
        try {
            // Se ejecutar la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al actualizar la galer�a.";
        }
        if (borrarImagenes(galeria.getId()) == 0) {
            return "Error al eliminar las imagenes.";
        }

        if (guardarImagenes(galeria.getId(), galeria.getImagenes()) == 0) {
            return "Error al guardar las imagenes.";
        }
        // Si hay filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Actualizacion exitosa" : "La galer�a no se puede actualizar, revise que se este ingresando informaci�n v�lida.";
    }

    /**
     * M�todo que elimina de base de datos la informaci�n de una imagen.
     *
     * @param id Identificador de la imagen.
     * 
     * @return Resultado de la acci�n con un n�mero en donde si se retorna 1 la acci�n fue exitosa.
     */
    private int borrarImagenes(long id) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", id);
        // Se arma la sentencia de registro de base de datos.
        String query = "DELETE FROM foto WHERE Galeria_id = :id";
        int result = 0;
        try {
            // Se ejecutar la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return result;
    }

    /**
     * M�todo que elimina de base de datos la informaci�n de una galer�a.
     *
     * @param galeria Objeto con la informaci�n de la galer�a a eliminar.
     * 
     * @return Resultado de la acci�n.
     */
    public String eliminarGaleria(Galeria galeria) {
        // Se verifica que la imagen de la galer�a se puede borrar.
        if (borrarImagenes(galeria.getId()) == 0) {
            return "Error al eliminar las im�genes.";
        }
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", galeria.getId());
        // Se arma la sentencia de registro de base de datos.
        String query = "DELETE FROM galeria WHERE id = :id";
        int result = 0;
        try {
            // Se ejecutar la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al eliminar la galer�a";
        }
        // Si hay filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Eliminacion exitosa" : "No fue posible eliminar la galer�a";
    }

    /*
     * M�todo que obtiene la cantidad de galer�as registradas.
     *   
     * @return Cantidad de registros.
     */
    public int getCantidadGalerias() {
        int cant = 0;
        // Se arma la sentencia de registro de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT COUNT(id) cantidad FROM galeria ");
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            cant = sqlRowSet.getInt("cantidad");
        }
        return cant;
    }

    /**
     * M�todo que obtiene imagenes de base de datos.
     *
     * @param id Identificador de la imagen.
     * 
     * @return Lista de imagenes.
     */
    public List<Imagen> getImagenesPorIDCompletas(long id) {
        // Lista para retornar con los datos
        List<Imagen> imagenes = new LinkedList<>();
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", id);
        // Se arma la sentencia de registro de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM foto WHERE Galeria_id = :id", map);
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            // Objeto en el que sera guardada la informaci�n del registro.
            Imagen imagen = new Imagen();
            imagen.setDescripcion(sqlRowSet.getString("descripcion"));
            Object imagenBlob = sqlRowSet.getObject("contenido");
            imagen.setImagen(new String((byte[]) imagenBlob));
            // Se guarda el registro para ser retornado.
            imagenes.add(imagen);
        }
        return imagenes;

    }
}
