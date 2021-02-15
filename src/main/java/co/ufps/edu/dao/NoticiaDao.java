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
import co.ufps.edu.dto.TipoContenido;
import co.ufps.edu.util.ImagenUtil;

/**
 * Clase que permite acceder a la capa de datos en el entorno de noticias.
 * 
 * @author UFPS
 */
@Component
public class NoticiaDao {

    private SpringDbMgr springDbMgr;
    private ImagenUtil imagenUtil;

    /**
     * Constructor de la clase en donde se inicializan las variables.
     */
    public NoticiaDao() {
        springDbMgr = new SpringDbMgr();
        imagenUtil = new ImagenUtil();
    }

    /**
     * Método que obtiene la cantidad de noticias registradas.
     *
     * @return Un entero con la cantidad de noticias creadas en el sistema.
     */
    public int getCantidadNoticias() {
        int cant = 0;
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT COUNT(id) cantidad FROM noticia");
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            // Se retorna la cantidad de noticias desde base de datos.
            cant = sqlRowSet.getInt("cantidad");
        }
        return cant;
    }

    /**
     * Método que consulta en base de datos todas las noticias existentes y las
     * devuelve ordenadamente.
     *
     * @return Una lista con todas las noticias
     */
    public List<Noticia> getNoticias() {
        // Lista para retornar con los datos.
        List<Noticia> noticias = new LinkedList<>();
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM noticia ORDER BY ORDEN DESC");
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            // Objeto en el que se guarda la información del registro.
            Noticia noticia = new Noticia();

            noticia.setId(sqlRowSet.getLong("id"));
            noticia.setNombre(sqlRowSet.getString("nombre"));
            noticia.setDescripcion(sqlRowSet.getString("descripcion"));
            noticia.setOrden(sqlRowSet.getInt("orden"));
            noticia.setFecha(sqlRowSet.getDate("fecha"));
            Object imagen1Blob = sqlRowSet.getObject("imagen1");
            noticia.setIm1Base64image(imagenUtil.convertirImagen((byte[]) imagen1Blob));

            cargarContenido(noticia);
            // Se guarda el registro para ser retornado.
            noticias.add(noticia);
        }
        return noticias;
    }

    /**
     * Método que registra una noticia en base de datos.
     *
     * @param noticia Objeto con todos los datos de la noticia a registrar.
     * 
     * @return El resultado de la acción.
     */
    public String registrarNoticia(Noticia noticia) {
        // Se arma la sentencia de base de datos.
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("nombre", noticia.getNombre());
        map.addValue("descripcion", noticia.getDescripcion());
        map.addValue("orden", 1);
        map.addValue("fecha", noticia.getFecha());
        try {
            map.addValue("imagen1",
                    new SqlLobValue(new ByteArrayInputStream(noticia.getImagen1().getBytes()),
                            noticia.getImagen1().getBytes().length, new DefaultLobHandler()),
                    Types.BLOB);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al registrar la noticia.";
        }
        map.addValue("orden", 1);
        // Se arma la sentencia de base de datos.
        String query = "INSERT INTO noticia(nombre,descripcion,orden,fecha,imagen1) VALUES(:nombre,:descripcion,:orden,:fecha,:imagen1)";
        int result = 0;
        try {
            // Se ejecuta a sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al registrar la noticia.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Registro exitoso"
                : "La noticia no se puede registrar, revise que se este ingresando información válida.";

    }

    /**
     * Método en el que se cambia el orden de las noticias.
     */
    public void cambiarOrden() {
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM noticia ORDER BY ORDEN DESC");
        // Se ontiene el maximo orden.
        int ultimoNumeroDeOrden = getUltimoNumeroDeOrden();
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            // Se ajusta el orden.
            cambiarOrdenDeNoticia(sqlRowSet.getLong("id"), ultimoNumeroDeOrden + 1);
            // Se disminuye el orden de la noticia con mayor orden.
            ultimoNumeroDeOrden--;
        }
    }

    /**
     * Método que consulta en la base de datos cual es el ultimo de número de
     * ordenamiento que hay entre todas las noticias.
     *
     * @return El último número de orden de noticia.
     */
    public int getUltimoNumeroDeOrden() {
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM noticia ORDER BY ORDEN DESC");
        // Si existe al menos una noticias retorna el número.
        if (sqlRowSet.next()) {
            // Se retorna el orden de cada noticia.
            return (sqlRowSet.getInt("orden"));
        } else {
            // Si no existen noticias retorna el 0.
            return 0;
        }
    }

    /**
     * Metodo que actualiza el orden de una noticia.
     *
     * @param id de la noticia.
     * @param orden para actualizar a la noticia.
     */
    public void cambiarOrdenDeNoticia(long id, int orden) {
        SpringDbMgr springDbMgr = new SpringDbMgr();
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", id);
        map.addValue("orden", orden);
        // Se arma la sentencia de base de datos.
        String query = "UPDATE noticia SET orden = :orden WHERE id = :id";
        try {
            // Se ejecutar la sentencia.
            springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            new Exception();
        }
    }

    /**
     * Método que baja un nivel a la noticia en base de datos.
     *
     * @param idNoticia Identificador de la noticia.
     * @param orden Orden de la noticia.
     */
    public void descenderOrden(long idNoticia, int orden) {
        // Se extrae el id de la siguiente noticia.
        long idNoticiaSiguiente = getIdNoticiaPorOrden(orden + 1);
        // Se modifica el orden actual de noticia.
        cambiarOrdenDeNoticia(idNoticia, -1);
        // Se modifica el orden de la siguiente noticia.
        cambiarOrdenDeNoticia(idNoticiaSiguiente, orden);
        // Se modifica el orden de la noticia actual.
        cambiarOrdenDeNoticia(idNoticia, orden + 1);
    }

    /**
     * Método que consulta en base de datos el ID de una noticia dado un número
     * de orden.
     *
     * @param orden Número de orden por el cual se filtrara la busqueda.
     * 
     * @return El ID de la noticia.
     */
    public long getIdNoticiaPorOrden(int orden) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("orden", orden);
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM noticia WHERE orden = :orden ", map);
        if (sqlRowSet.next()) {
            // Si existen noticias retorna el id.
            return (sqlRowSet.getLong("id"));
        } else {
            // Si no existen noticias retorna el 0.
            return 0;
        }
    }

    /**
     * Método que permite subir de orden una noticia en base de datos.
     *
     * @param idNoticia Identificador de la noticia.
     * @param orden Número de orden.
     */
    public void ascenderOrden(long idNoticia, int orden) {
        // Se extrae el id de la noticia anterior.
        long idNoticiaAnterior = getIdNoticiaPorOrden(orden - 1);
        // Se modifica el orden actual de la noticia.
        cambiarOrdenDeNoticia(idNoticia, -1);
        // Se modifica el orden de la anterior noticia.
        cambiarOrdenDeNoticia(idNoticiaAnterior, orden);
        // Se modifa el orden de la noticia actual.
        cambiarOrdenDeNoticia(idNoticia, orden - 1);
    }

    /**
     * Método que consulta en base de datos la información de una noticia dada.
     *
     * @param idNoticia Identificador de la noticia.
     * 
     * @return La información de una noticia en un objeto.
     */
    public Noticia obtenerNoticiaPorId(long idNoticia) {
        Noticia noticia = new Noticia();
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", idNoticia);
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM noticia WHERE id = :id", map);
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            // Se almacenan los datos de la noticia.
            noticia.setId(sqlRowSet.getLong("id"));
            noticia.setNombre(sqlRowSet.getString("nombre"));
            noticia.setDescripcion(sqlRowSet.getString("descripcion"));
            noticia.setOrden(sqlRowSet.getInt("orden"));
            noticia.setFecha(sqlRowSet.getDate("fecha"));

            Object imagen1Blob = sqlRowSet.getObject("imagen1");
            noticia.setIm1Base64image(imagenUtil.convertirImagen((byte[]) imagen1Blob));
        }
        return noticia;
    }

    /**
     * Método que edita en base de datos la información de una noticia dada.
     *
     * @param noticia Noticia a editar.
     * 
     * @return El resultado de la acción.
     */
    public String editarNoticia(Noticia noticia) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", noticia.getId());
        map.addValue("nombre", noticia.getNombre());
        map.addValue("descripcion", noticia.getDescripcion());
        map.addValue("fecha", noticia.getFecha());
        String sqlImagen1 = "";
        if (noticia.getImagen1().getSize() > 0) {
            try {
                map.addValue("imagen1",
                        new SqlLobValue(new ByteArrayInputStream(noticia.getImagen1().getBytes()),
                                noticia.getImagen1().getBytes().length, new DefaultLobHandler()),
                        Types.BLOB);
                sqlImagen1 = ", imagen1 = :imagen1";
            } catch (IOException e) {
                e.printStackTrace();
                return "Error al actualizar la noticia.";
            }
        }
        // Se arma la sentencia de base de datos.
        String query = "UPDATE noticia SET nombre = :nombre, descripcion = :descripcion, fecha = :fecha "
                + sqlImagen1 + " WHERE id = :id";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al actualizar la noticia.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Actualizacion exitosa"
                : "La noticia no se puede editar, revise que se este ingresando información válida.";
    }

    /**
     * Método que elimina en base de datos la información de una noticia dada.
     *
     * @param noticia Noticia a editar.
     * 
     * @return El resultado de la acción.
     */
    public String eliminarNoticia(Noticia noticia) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", noticia.getId());
        // Se arma la sentencia de base de datos.
        String query = "DELETE FROM noticia WHERE id = :id";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al eliminar la noticia.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Eliminacion exitosa"
                : "La noticia no se puede eliminar.";
    }

    /**
     * Método que obtiene de base de datos la información de las noticias.
     * 
     * @return Lista con los datos por noticias.
     */
    public List<Noticia> getUltimasNoticias() {
        List<Noticia> noticias = new LinkedList<>();
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM noticia ORDER BY ORDEN ASC LIMIT 0, 4");
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            // Objeto en el que se guarda la información del registro.
            Noticia noticia = new Noticia();

            noticia.setId(sqlRowSet.getLong("id"));
            noticia.setNombre(sqlRowSet.getString("nombre"));
            noticia.setDescripcion(sqlRowSet.getString("descripcion"));
            noticia.setOrden(sqlRowSet.getInt("orden"));
            noticia.setFecha(sqlRowSet.getDate("fecha"));

            Object imagen1Blob = sqlRowSet.getObject("imagen1");
            noticia.setIm1Base64image(imagenUtil.convertirImagen((byte[]) imagen1Blob));

            cargarContenido(noticia);
            // Se guarda el registro para ser retornado.
            noticias.add(noticia);
        }
        return noticias;
    }

    /**
     * Método que obtiene de base de datos la información del contenido de las noticias.
     * 
     * @param noticia Noticia a la que se carga contenido.
     * 
     * @return Contenido de la noticia.
     */
    private void cargarContenido(Noticia noticia) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", noticia.getId());
        map.addValue("tipo", Constantes.NOTICIA);
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT contenido.id idContenido,"
                + " contenido.contenido contenido,"
                + " contenido.TipoContenido_id tipoContenido"
                + " FROM noticia"
                + " INNER JOIN contenido ON contenido.asociacion = noticia.id"
                + " WHERE noticia.id = :id"
                + " AND contenido.tipoasociacion = :tipo", map);
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            // Objeto en el que se guarda la información del registro.
            Contenido contenido = new Contenido();

            contenido.setId(sqlRowSet.getLong("idContenido"));
            byte[] a = (byte[]) sqlRowSet.getObject("contenido");
            String res = new String(a, Charsets.UTF_8);
            contenido.setContenido(res);

            TipoContenido tipoContenido = new TipoContenido();
            tipoContenido.setId(sqlRowSet.getLong("tipoContenido"));

            contenido.setTipoContenido(tipoContenido);

            // Se guarda el registro para ser retornado.
            noticia.setContenido(contenido);
        }
    }

}
