package co.ufps.edu.dao;

import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import co.ufps.edu.bd.SpringDbMgr;

/**
 * Clase que permite acceder a la capa de datos en el entorno de tipos de contenido.
 * 
 * @author UFPS
 */
public class TipoContenidoDao {

    private SpringDbMgr springDbMgr;

    /**
     * Constructor de la clase en donde se inicializan las variables.
     */
    public TipoContenidoDao() {
        springDbMgr = new SpringDbMgr();
    }

    /**
     * Método que consulta en base de datos todos los contenidos existentes y
     * los devuelve ordenadamente.
     *
     * @return Una lista con todos los contenidos.
     */
    public Map<Long, String> getContenidos() {
        // Lista para retornar con los datos.
        Map<Long, String> mapaDeTiposDeContenido = new HashMap<>();
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM tipocontenido");
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            // Se carga el contenido.
            mapaDeTiposDeContenido.put(sqlRowSet.getLong("id"), sqlRowSet.getString("nombre"));
        }
        return mapaDeTiposDeContenido;
    }
}
