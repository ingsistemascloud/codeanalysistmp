package co.ufps.edu.dao;

import org.apache.commons.io.Charsets;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import co.ufps.edu.bd.SpringDbMgr;
import co.ufps.edu.constantes.Constantes;
import co.ufps.edu.dto.Contenido;

/**
 * Clase que permite acceder a la capa de datos en el entorno de componente.
 * 
 * @author UFPS
 */
public class ComponenteDao {

    private SpringDbMgr springDbMgr;

    /**
   * Constructor de la clase en donde se inicializan las variables.
   */
    public ComponenteDao() {
        springDbMgr = new SpringDbMgr();
    }

    /**
     * Método que obtiene el contenido de un componente dado el identificador y el tipo de contenido.
     *
     * @param idComponente Identificador del componente.
     * @param tipo Tipo de contenido del componente.
     * 
     * @return El contenido del componenente.
     */
    public Contenido obtenerContenidoComponentePorId(long idComponente, String tipo) {
        // Lista para retornar con los datos
        Contenido contenido = new Contenido();
        String tip = tipo;
        // Se define el tipo de obejto.
        if ((tip.toLowerCase()).equalsIgnoreCase("proximaactividad")) {
            tip = Constantes.ACTIVIDAD;
        } else if ((tip).equalsIgnoreCase("noticia")) {
            tip = Constantes.NOTICIA;
        } else if ((tip).equalsIgnoreCase("subcategoria")) {
            tip = Constantes.SUBCATEGORIA;
        } else if ((tip).equalsIgnoreCase("novedad")) {
            tip = Constantes.NOVEDAD;
        } else if ((tip).equalsIgnoreCase("itemsubcategoria")) {
            tip = Constantes.ITEMSUBCATEGORIA;
        }
        tipo = tipo.toLowerCase();
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", idComponente);
        map.addValue("tip", tip);
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT " + tipo + ".ID id,"
                + " " + tipo + ".NOMBRE nombre,"
                + " contenido.id idContenido,"
                + " contenido.contenido contenido,"
                + " contenido.TipoContenido_id tipoContenido,"
                + " contenido.asociacion asociacion,"
                + " contenido.tipoasociacion tipoasociacion,"
                + " contenido.titulo titulo"
                + " FROM " + tipo + ""
                + " INNER JOIN contenido ON contenido.asociacion = " + tipo + ".id"
                + " WHERE " + tipo + ".ID = :id"
                + " AND contenido.tipoasociacion = :tip ", map);
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            // Objeto en el que sera guardada la informacion del registro.
            contenido = new Contenido();
            contenido.setId(sqlRowSet.getLong("idContenido"));
            byte[] a = (byte[]) sqlRowSet.getObject("contenido");
            String res = new String(a, Charsets.UTF_8);
            contenido.setContenido(res);
            contenido.setAsociacion(sqlRowSet.getLong("asociacion"));
            contenido.setNombre(sqlRowSet.getString("titulo"));
            contenido.setTipoAsociacion(sqlRowSet.getString("tipoasociacion"));
        }
        // Se retorna el contenido desde base de datos.
        return contenido;
    }
}
