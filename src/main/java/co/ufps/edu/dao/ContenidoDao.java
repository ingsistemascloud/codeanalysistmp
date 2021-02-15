package co.ufps.edu.dao;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.Charsets;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import co.ufps.edu.bd.SpringDbMgr;
import co.ufps.edu.constantes.Constantes;
import co.ufps.edu.dto.Archivo;
import co.ufps.edu.dto.Contenido;
import co.ufps.edu.dto.Noticia;
import co.ufps.edu.dto.ResultDB;
import co.ufps.edu.dto.TipoContenido;
import co.ufps.edu.util.ImagenUtil;

/**
 * Clase que permite acceder a la capa de datos en el entorno de contenido.
 *
 * @author UFPS
 */
@Component
public class ContenidoDao {

    private SpringDbMgr springDbMgr;
    private ImagenUtil imagenUtil;

    /**
     * Constructor de la clase en donde se inicializan las variables.
     */
    public ContenidoDao() {
        springDbMgr = new SpringDbMgr();
        imagenUtil = new ImagenUtil();
    }

    /**
     * Método que consulta en base de datos todas los contenidos existentes.
     *
     * @return Una lista con todos los contenidos.
     */
    public List<Contenido> getContenidos() {
        // Lista para retornar con los datos
        List<Contenido> contenidos = new LinkedList<>();
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT contenido.id id,"
                + " contenido.titulo nombre,"
                + " tipocontenido.nombre tipo_con,"
                + " contenido.tipocontenido_id tipo_con_id,"
                + " contenido.asociacion asociacion,"
                + " CASE WHEN contenido.tipoasociacion = 'ItemSubcategoria'"
                + " THEN (SELECT CONCAT('ItemSubcategoria: ',itemsubcategoria.nombre) FROM itemsubcategoria WHERE itemsubcategoria.id = contenido.asociacion)"
                + " WHEN contenido.tipoasociacion = 'Categoria'"
                + " THEN (SELECT CONCAT('Categoria: ',categoria.nombre) FROM categoria WHERE categoria.id = contenido.asociacion)"
                + " WHEN contenido.tipoasociacion = 'Subcategoria'"
                + " THEN (SELECT CONCAT('Subcategoria: ',subcategoria.nombre) FROM subcategoria WHERE subcategoria.id = contenido.asociacion)"
                + " WHEN contenido.tipoasociacion = 'Noticia'"
                + " THEN (SELECT CONCAT('Noticia: ',noticia.nombre) FROM noticia WHERE noticia.id = contenido.asociacion)"
                + " WHEN contenido.tipoasociacion = 'Novedad'"
                + " THEN (SELECT CONCAT('Novedad: ',novedad.nombre) FROM novedad WHERE novedad.id = contenido.asociacion)"
                + " WHEN contenido.tipoasociacion = 'Actividad'"
                + " THEN (SELECT CONCAT('Actividad: ',proximaactividad.nombre) FROM proximaactividad WHERE proximaactividad.id = contenido.asociacion)"
                + " END AS tipo_asoc"
                + " FROM contenido, tipocontenido"
                + " WHERE contenido.TipoContenido_id = tipocontenido.id ORDER BY contenido.id DESC");
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            // Objeto en el que se guarda la información del registro.
            Contenido contenido = new Contenido();
            contenido.setId(sqlRowSet.getLong("id"));
            contenido.setNombre(sqlRowSet.getString("nombre"));
            contenido.setTipoAsociacion(sqlRowSet.getString("tipo_asoc"));
            contenido.setAsociacion(sqlRowSet.getLong("asociacion"));
            contenido.setTipocontenido_id(sqlRowSet.getLong("tipo_con_id"));
            // Objeto en el que se guarda el tipo de contenido del registro.
            TipoContenido tipoContenido = new TipoContenido();
            tipoContenido.setNombre(sqlRowSet.getString("tipo_con"));
            contenido.setTipoContenido(tipoContenido);
            // Se guarda el registro para ser retornado.
            contenidos.add(contenido);
        }
        // Se retornan los contenidos obtenidos desde base de datos.
        return contenidos;
    }

    /*
     * Método que obtiene la cantidad de contenidos registrados.
     * @return Cantidad de registros.
     */
    public int getCantidadContenidos() {
        int cant = 0;
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery(" SELECT COUNT(id) cantidad FROM contenido ");
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            cant = sqlRowSet.getInt("cantidad");
        }
        return cant;
    }

    /**
     * Método que registra en base de datos contenido.
     *
     * @param contenido Objeto que contiene la información del contenido a registrar.
     * 
     * @return Una lista con todos los contenidos.
     */
    public String registrarContenido(Contenido contenido) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("titulo", contenido.getNombre());
        map.addValue("tipoasociacion", contenido.getTipoAsociacion());
        map.addValue("TipoContenido_id", contenido.getTipoContenido().getId());
        map.addValue("asociacion", contenido.getAsociacion());
        String conn = "";
        if (contenido.getTipoContenido().getId() == 1) {
            conn = String.join("", contenido.getConn());
        } else {
            conn = contenido.getContenido();
        }
        map.addValue("contenido", conn.getBytes(Charsets.UTF_8));
        // Se arma la sentencia de registro de base de datos.
        String query = "INSERT INTO contenido(titulo,tipoasociacion,TipoContenido_id,asociacion,contenido) VALUES(:titulo,:tipoasociacion,:TipoContenido_id,:asociacion,:contenido)";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al registrar el contenido.";
        }
        // Si hay filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Registro exitoso"
                : "El contenido no se puede registrar, revise que se este ingresando información válida.";
    }

    /**
     * Método que obtiene de base de datos las asociaciones de los contenidos.
     *
     * @param tipoAsociacion Tipo de asociación.
     * 
     * @return Una lista con todas las asociaciónes.
     */
    public Map<Integer, String> getAsociaciones(String tipoAsociacion) {
        Map<Integer, String> asociaciones = new HashMap<>();
        String tabla = (tipoAsociacion.equalsIgnoreCase(Constantes.ACTIVIDAD)) ? "proximaactividad" : tipoAsociacion;
        tabla = tabla.toLowerCase();
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("tipo", tipoAsociacion.toLowerCase());
        // Se arma la sentencia de registro de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM " + tabla + " WHERE ID NOT IN (SELECT asociacion FROM contenido WHERE upper(tipoasociacion) = :tipo)", map);
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            asociaciones.put(sqlRowSet.getInt("id"), sqlRowSet.getString("nombre"));
        }
        // Se retornan las asociaciones desde base de datos.
        return asociaciones;
    }

    /**
     * Método que obtiene de base de datos las asociaciones de los contenidos.
     *
     * @param tipoAsociacion Tipo de asociación.
     * @param idAsociacion Idnetificador del tipo de asociación.
     * 
     * @return Una lista con todas las asociaciónes.
     */
    public Map<Integer, String> getAsociacionesCompletas(String tipoAsociacion, long idAsociacion) {
        Map<Integer, String> asociaciones = new HashMap<>();
        String tabla = (tipoAsociacion.equalsIgnoreCase(Constantes.ACTIVIDAD)) ? "proximaactividad" : tipoAsociacion;
        tabla = tabla.toLowerCase();
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", idAsociacion);
        // Se arma la sentencia de registro de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM " + tabla + " WHERE ID NOT IN (SELECT asociacion FROM contenido) OR ID = :id", map);
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            asociaciones.put(sqlRowSet.getInt("id"), sqlRowSet.getString("nombre"));
        }
        // Se retornan las asociaciones desde base de datos.
        return asociaciones;
    }

    /**
     * Método que registra en base de datos un archivo.
     * 
     * @param archivo Archivo que se registra.
     * 
     * @return Una lista con todas las asociaciónes.
     */
    public long registrarArchivo(MultipartFile archivo) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("nombre", archivo.getOriginalFilename());
        map.addValue("tamaño", archivo.getSize());
        map.addValue("tipo", archivo.getContentType());
        try {
            map.addValue("contenido",
                    new SqlLobValue(new ByteArrayInputStream(archivo.getBytes()),
                            archivo.getBytes().length, new DefaultLobHandler()),
                    Types.BLOB);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        // Se arma la sentencia de registro de base de datos.
        String query = "INSERT INTO archivo(nombre,contenido,tamaño,tipo) VALUES(:nombre,:contenido,:tamaño,:tipo)";
        ResultDB result = null;
        try {
            // Se ejecuta la sentecia de base de datos.
            result = springDbMgr.executeDmlWithKey(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        // Si hay filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return result.getKey();
    }

    /**
     * Método que obtiene de base de datos una imagen.
     * 
     * @param tipo Tipo de imagen.
     * 
     * @return La imagen convertida en String.
     */
    public String solicitarImagen(String tipo) {
        String contenido = "";
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("tipo", tipo);
        // Se arma la sentencia de registro de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM archivo WHERE nombre = :tipo", map);
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            // Objeto en donde se guarda la imagen.
            Object imagen1Blob = sqlRowSet.getObject("contenido");
            // Se convierte la imagen en String.
            contenido = (imagenUtil.convertirImagen((byte[]) imagen1Blob));
        }
        return contenido;
    }

    /**
     * Método que obtiene de base de datos un archivo.
     * 
     * @param id Identificador del archivo
     * 
     * @return El archivo obtenido.
     */
    public Archivo obtenerArchivo(long id) {
        // Objeto tipo archivo.
        Archivo archivo = new Archivo();
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", id);
        // Se arma la sentencia de registro de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM archivo WHERE id = :id", map);
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            // Se obtienen los datos necesarios de acuerdo a la imagen.
            Object imagen1Blob = sqlRowSet.getObject("contenido");
            archivo.setContenido(new ByteArrayInputStream((byte[]) imagen1Blob));
            archivo.setNombre(sqlRowSet.getString("nombre"));
            archivo.setTipo(sqlRowSet.getString("tipo"));
        }
        return archivo;
    }

    /**
     * Método que consulta en base de datos la informacion de un contenido dado.
     *
     * @param idContenido Identificador del contenido.
     * 
     * @return La información de una actividad en un objeto.
     */
    public Contenido obtenerContenidoPorId(long idContenido) {
        Contenido contenido = new Contenido();
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", idContenido);
        // Se arma la sentencia de registro de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM contenido WHERE id = :id", map);
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            // Se almacen los datos del contenido.
            contenido.setId(sqlRowSet.getLong("id"));
            contenido.setNombre(sqlRowSet.getString("titulo"));
            contenido.setTipoAsociacion(sqlRowSet.getString("tipoasociacion"));
            byte[] a = (byte[]) sqlRowSet.getObject("contenido");
            String res = new String(a, Charsets.UTF_8);
            contenido.setContenido(res);
            TipoContenido tipoContenido = new TipoContenido();
            tipoContenido.setId(sqlRowSet.getLong("TipoContenido_id"));
            contenido.setTipoContenido(tipoContenido);
            contenido.setAsociacion(sqlRowSet.getLong("asociacion"));

        }
        // Se retorna el contenido desde base de datos.
        return contenido;
    }

    /**
     * Método que actualiza en base de datos la informacion de un contenido.
     *
     * @param contenido Objeto que contiene la información del contenido a actualizar.
     * 
     * @return La información de una actividad en un objeto.
     */
    public String actualizarContenido(Contenido contenido) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", contenido.getId());
        map.addValue("titulo", contenido.getNombre());
        map.addValue("tipoasociacion", contenido.getTipoAsociacion());
        map.addValue("TipoContenido_id", contenido.getTipoContenido().getId());
        map.addValue("asociacion", contenido.getAsociacion());
        String conn = "";
        if (contenido.getTipoContenido().getId() == 1) {
            conn = String.join("", contenido.getConn());
        } else {
            conn = contenido.getContenido();
        }
        map.addValue("contenido", conn.getBytes(Charsets.UTF_8));
        // Se arma la sentencia de registro de base de datos.
        String query = "UPDATE contenido SET titulo = :titulo,tipoasociacion = :tipoasociacion,TipoContenido_id = :TipoContenido_id,asociacion = :asociacion,contenido = :contenido WHERE id = :id";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al actualizar el contenido.";
        }
        // Si hay filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Actualizacion exitosa"
                : "El contenido no se puede actualizar, revise que se este ingresando información válida.";
    }

    /**
     * Método que elimina en base de datos un contenido.
     *
     * @param contenido Objeto que contiene la información del contenido a eliminar.
     * 
     * @return Una lista con todas las actividades
     */
    public String eliminarContenido(Contenido contenido) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", contenido.getId());
        // Se arma la sentencia de registro de base de datos.
        String query = "DELETE FROM contenido WHERE id = :id";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al eliminar el contenido.";
        }
        // Si hay filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Eliminacion exitosa"
                : "El contenido no se puede eliminar.";
    }

    /**
     * Método que verifica si hay contenido.
     * 
     * @param id Identificador de la asociación.
     * @param tipoAsociacion Tipo de asociacióm del contenido.
     * 
     * @return Respuesta a si hay contendio.
     */
    public boolean tieneContenido(long id, String tipoAsociacion) {
        String tabla = (tipoAsociacion.equalsIgnoreCase(Constantes.ACTIVIDAD)) ? "proximaactividad" : tipoAsociacion;
        tabla = tabla.toLowerCase();
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("tipo", tipoAsociacion.toLowerCase());
        map.addValue("id", id);
        // Se arma la sentencia de registro de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT id FROM contenido WHERE asociacion = :id AND upper(tipoasociacion) = :tipo", map);
        // Si hay contenido retorna true.
        return (sqlRowSet.next());
    }

}
