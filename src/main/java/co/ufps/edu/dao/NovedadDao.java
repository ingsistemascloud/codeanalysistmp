package co.ufps.edu.dao;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.Charsets;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import co.ufps.edu.bd.SpringDbMgr;
import co.ufps.edu.constantes.Constantes;
import co.ufps.edu.dto.Contenido;
import co.ufps.edu.dto.Noticia;
import co.ufps.edu.dto.Novedad;
import co.ufps.edu.dto.TipoContenido;
import co.ufps.edu.util.ImagenUtil;

/**
 * Clase que permite acceder a la capa de datos en el entorno de novedades.
 *
 * @author UFPS
 */
@Component
public class NovedadDao {

    private SpringDbMgr springDbMgr;
    private ImagenUtil imagenUtil;

    /**
     * Constructor de la clase en donde se inicializan las variables.
     */
    public NovedadDao() {
        springDbMgr = new SpringDbMgr();
        imagenUtil = new ImagenUtil();
    }

    /**
     * Método que consulta en base de datos todas las novedades existentes.
     *
     * @return Una lista con todas las novedades.
     */
    public List<Novedad> getNovedades() {
        List<Novedad> novedades = new LinkedList<>();
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM novedad ORDER BY FECHA DESC");
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            // Objeto en el que se guarda la información del registro.
            Novedad novedad = new Novedad();

            novedad.setId(sqlRowSet.getLong("id"));
            novedad.setNombre(sqlRowSet.getString("nombre"));
            novedad.setFecha(sqlRowSet.getDate("fecha"));
            Object imagen1Blob = sqlRowSet.getObject("imagen");
            novedad.setImBase64image(imagenUtil.convertirImagen((byte[]) imagen1Blob));
            // Se guarda el registro para ser retornado.
            novedades.add(novedad);
        }
        return novedades;
    }

    /**
     * Método que registra una novedad en base de datos.
     *
     * @param novedad Objeto con todos los datos de la novedad a registrar.
     *
     * @return El resultado de la acción.
     */
    public String registrarNovedad(Novedad novedad) {
        // Se arma la sentencia de base de datos.
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("nombre", novedad.getNombre());
        map.addValue("fecha", novedad.getFecha());
        try {
            map.addValue("imagen",
                    new SqlLobValue(new ByteArrayInputStream(novedad.getImagen().getBytes()),
                            novedad.getImagen().getBytes().length, new DefaultLobHandler()),
                    Types.BLOB);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al registrar la novedad.";
        }
        // Se arma la sentencia de base de datos.
        String query = "INSERT INTO novedad(nombre, fecha, imagen) VALUES(:nombre, :fecha, :imagen)";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al registrar la novedad.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Registro exitoso"
                : "La novedad no se puede registrar, revise que se este ingresando información válida.";
    }

    /**
     * Método que consulta en base de datos la información de una novedad.
     *
     * @param idNovedad Identificador de novedad.
     *
     * @return La información de una novedad en un objeto.
     */
    public Novedad obtenerNovedadPorId(long idNovedad) {
        Novedad novedad = new Novedad();
        // Se arma la sentencia de base de datos.
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", idNovedad);
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM novedad WHERE id = :id", map);
        // Si existe al menos una noticia retorna el número.
        if (sqlRowSet.next()) {
            // Se almacenan los datos de contacto.
            novedad.setId(sqlRowSet.getLong("id"));
            novedad.setNombre(sqlRowSet.getString("nombre"));
            novedad.setFecha(sqlRowSet.getDate("fecha"));

            Object imagen1Blob = sqlRowSet.getObject("imagen");
            novedad.setImBase64image(imagenUtil.convertirImagen((byte[]) imagen1Blob));
        }
        return novedad;
    }

    /**
     * Método que edita en base de datos la información de una novedad.
     *
     * @param novedad Objeto con la información a actualizar.
     *
     * @return La información de una novedad en un objeto.
     */
    public String editarNovedad(Novedad novedad) {
        // Se arma la sentencia de base de datos.
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", novedad.getId());
        map.addValue("nombre", novedad.getNombre());
        map.addValue("fecha", novedad.getFecha());
        String sqlImagen = "";
        if (novedad.getImagen().getSize() > 0) {
            try {
                map.addValue("imagen",
                        new SqlLobValue(new ByteArrayInputStream(novedad.getImagen().getBytes()),
                                novedad.getImagen().getBytes().length, new DefaultLobHandler()),
                        Types.BLOB);
                sqlImagen = ", imagen = :imagen";
            } catch (IOException e) {
                e.printStackTrace();
                return "Error al actualizar la novedad.";
            }
        }
        // Se arma la sentencia de base de datos.
        String query = "UPDATE novedad SET nombre = :nombre, fecha = :fecha " + sqlImagen + "  WHERE id = :id";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al actualizar la novedad.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Actualizacion exitosa" : "La novedad no se puede actualizar, revise que se este ingresando información válida.";
    }

    /**
     * Método que elimina en base de datos la información de una novedad.
     *
     * @param novedad Objeto con la información a eliminar.
     *
     * @return La información de una novedad en un objeto.
     */
    public String eliminarNovedad(Novedad novedad) {
        // Se arma la sentencia de base de datos.
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", novedad.getId());
        // Se arma la sentencia de base de datos.
        String query = "DELETE FROM novedad WHERE id = :id";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al eliminar la novedad.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Eliminacion exitosa" : "La noticia no se puede eliminar.";
    }


    /*
     * Método que obtiene la cantidad de novedades registradas.
     *
     * @return Un entero con la cantidad de novedades creadas en el sistema.
     */
    public int getCantidadNovedades() {
        int cant = 0;
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT COUNT(id) cantidad FROM novedad ");
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            // Se retorna la cantidad de noticias desde base de datos.
            cant = sqlRowSet.getInt("cantidad");
        }
        return cant;
    }

    /**
     * Método que obtiene de base de datos la información de las novedades.
     * 
     * @return Lista con los datos por noticias.
     */
    public List<Novedad> getUltimasNovedades() {
        List<Novedad> novedades = new LinkedList<>();
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM novedad ORDER BY FECHA DESC LIMIT 0, 4");
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            // Objeto en el que se guarda la información del registro
            Novedad novedad = new Novedad();

            novedad.setId(sqlRowSet.getLong("id"));
            novedad.setNombre(sqlRowSet.getString("nombre"));
            novedad.setFecha(sqlRowSet.getDate("fecha"));
            Object imagen1Blob = sqlRowSet.getObject("imagen");
            novedad.setImBase64image(imagenUtil.convertirImagen((byte[]) imagen1Blob));

            cargarContenido(novedad);

            // Se guarda el registro para ser retornado.
            novedades.add(novedad);
        }
        return novedades;
    }

    /**
     * Método que obtiene de base de datos la información del contenido de las novedades.
     * 
     * @param novedad Novedad a la que se carga contenido.
     * 
     * @return Contenido de la novedad.
     */
    private void cargarContenido(Novedad novedad) {
        // Se arma la sentencia de base de datos.
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", novedad.getId());
        map.addValue("tipo", Constantes.NOVEDAD);
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT contenido.id idContenido,"
                + " contenido.contenido contenido,"
                + " contenido.TipoContenido_id tipoContenido"
                + " FROM novedad"
                + " INNER JOIN contenido ON contenido.asociacion = novedad.id"
                + " WHERE novedad.id = :id"
                + " AND contenido.tipoasociacion = :tipo", map);
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            // Objeto en el que se guarda la informacion del registro.
            Contenido contenido = new Contenido();

            contenido.setId(sqlRowSet.getLong("idContenido"));
            byte[] a = (byte[]) sqlRowSet.getObject("contenido");
            String res = new String(a, Charsets.UTF_8);
            contenido.setContenido(res);

            TipoContenido tipoContenido = new TipoContenido();
            tipoContenido.setId(sqlRowSet.getLong("tipoContenido"));

            contenido.setTipoContenido(tipoContenido);

            // Se guarda el registro para ser retornado.
            novedad.setContenido(contenido);
        }
    }
}
