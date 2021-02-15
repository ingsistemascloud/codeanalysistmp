package co.ufps.edu.dao;

import java.util.LinkedList;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import co.ufps.edu.bd.SpringDbMgr;
import co.ufps.edu.dto.Auditoria;

/**
 * Clase que permite acceder a la capa de datos en el entorno de auditorias.
 * 
 * @author UFPS
 */
public class AuditoriaDao {

    private SpringDbMgr springDbMgr;

    /**
    * Constructor de la clase en donde se inicializan las variables.
    */
    public AuditoriaDao() {
        springDbMgr = new SpringDbMgr();
    }

    /**
     * Método que consulta en base de datos todas las auditorias existentes.
     *
     * @return Una lista con todas las auditorias.
     */
    public List<Auditoria> getAuditorias() {
        // Lista para retornar con los datos.
        List<Auditoria> auditorias = new LinkedList<>();
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM auditoria ORDER BY idAuditoria DESC");
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            // Objeto en el que sera guardada la informacion del registro.
            Auditoria auditoria = new Auditoria();
            auditoria.setIdAuditoria(sqlRowSet.getLong("idAuditoria"));
            auditoria.setTabla(sqlRowSet.getString("tabla"));
            auditoria.setIdRegistro(sqlRowSet.getString("idRegistro"));
            auditoria.setNombre(sqlRowSet.getString("nombre"));
            auditoria.setAccion(sqlRowSet.getString("accion"));
            auditoria.setFecha(sqlRowSet.getString("fecha"));
            auditoria.setHora(sqlRowSet.getString("hora"));
            // Se guarda el registro para ser retornado.
            auditorias.add(auditoria);
        }
        // Se retornan todas las auditorias desde base de datos.
        return auditorias;
    }

    /**
     * Método que consulta en base de datos la informacion de las auditorias de
     * acuerdo a una operacion.
     *
     * @param tabla Operación a consultar.
     * 
     * @return Una lista con todas las auditorias.
     */
    public List<Auditoria> getAuditoriasPorTabla(String tabla) {
        if (!tabla.equalsIgnoreCase("Seleccione una operación")) {
            // Lista para retornar con los datos.
            List<Auditoria> auditorias = new LinkedList<>();
            // Se arma la sentencia de base de datos.
            SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM auditoria");
            tabla = tabla.replace(" ", "");
            // Se recorre cada registro obtenido de base de datos.
            while (sqlRowSet.next()) {
                // Objeto en el que sera guardada la informacion del registro.
                Auditoria auditoria = new Auditoria();
                auditoria.setIdAuditoria(sqlRowSet.getLong("idAuditoria"));
                auditoria.setTabla(sqlRowSet.getString("tabla"));
                auditoria.setIdRegistro(sqlRowSet.getString("idRegistro"));
                auditoria.setNombre(sqlRowSet.getString("nombre"));
                auditoria.setAccion(sqlRowSet.getString("accion"));
                auditoria.setFecha(sqlRowSet.getString("fecha"));
                auditoria.setHora(sqlRowSet.getString("hora"));
                if (auditoria.getTabla().equalsIgnoreCase(tabla)) {
                    // Se guarda el registro para ser retornado.
                    auditorias.add(auditoria);
                }
            }
            // Se retornan todas las auditorias desde base de datos.
            return auditorias;
        }else{
            // Se retornan todas las auditorias desde base de datos.
            return getAuditorias();
        }
    }

    /**
     * Método que obtiene la cantidad de auditorias registradas.
     *
     * @return Cantidad de auditorias registradas.
     */
    public int getCantidadAuditorias() {
        int cant = 0;
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT COUNT(idAuditoria) cantidad FROM auditoria");
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            cant = sqlRowSet.getInt("cantidad");
        }
        return cant;
    }
}
