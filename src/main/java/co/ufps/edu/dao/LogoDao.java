package co.ufps.edu.dao;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Types;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import co.ufps.edu.bd.SpringDbMgr;
import co.ufps.edu.dto.Logo;
import co.ufps.edu.util.ImagenUtil;

/**
 * Clase que permite acceder a la capa de datos en el entorno de logo.
 * 
 * @author UFPS
 */
@Component
public class LogoDao {

    private SpringDbMgr springDbMgr;
    private ImagenUtil imagenUtil;

    /**
     * Constructor de la clase en donde se inicializan las variables.
     */
    public LogoDao() {
        springDbMgr = new SpringDbMgr();
        imagenUtil = new ImagenUtil();
    }

    /**
     * Método que obtiene la cantidad de logos registrados.
     */
    public int getCantidadLogos() {
        int cant = 0;
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT COUNT(id) cantidad FROM logo");
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            cant = sqlRowSet.getInt("cantidad");
        }
        // Se retorna la cantidad de logos desde base de datos.
        return cant;
    }

    /**
     * Método que retorna un log de acuerdo al tipo del mismo.
     *
     * @param tipo Tipo del logo a obtener.
     * 
     * @return Logo obenido desde base de datos.
     */
    public Logo getLogo(String tipo) {
        Logo logo = new Logo();
        // Se arma la sentencia de base de datos.
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("tipo", tipo);
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM logo WHERE tipo = :tipo", map);
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            // Objeto en el que se guarda la información del registro.
            logo.setId(sqlRowSet.getLong("id"));
            logo.setTipo(sqlRowSet.getString("tipo"));

            Object imagenBlob = sqlRowSet.getObject("contenido");
            logo.setImBase64image(imagenUtil.convertirImagen((byte[]) imagenBlob));
        }
        return logo;
    }

    /**
     * Método que inserta un log en la base de datos.
     *
     * @param logo Objeto con la información a registrar.
     * 
     * @return Resultado de la acción.
     */
    public String insertarLogo(Logo logo) {
        // Se arma la sentencia de base de datos.
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("tipo", logo.getTipo());
        try {
            map.addValue("contenido",
                    new SqlLobValue(new ByteArrayInputStream(logo.getContenido().getBytes()),
                            logo.getContenido().getBytes().length, new DefaultLobHandler()),
                    Types.BLOB);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al registrar el logo.";
        }
        // Se arma la sentencia de base de datos.
        String query = "INSERT INTO logo(tipo,contenido) VALUES(:tipo,:contenido)";
        int result = 0;
        try {
            // Se ejecuta a sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al registrar el logo.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Registro exitoso"
                : "El logo no se puede registrar";
    }

    /**
     * Método que actualiza un log en la base de datos.
     *
     * @param logo Objeto con la información a actualizar.
     * 
     * @return Resultado de la acción.
     */
    public String actualizarLogo(Logo logo) {
        // Se arma la sentencia de base de datos.
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", logo.getId());
        map.addValue("tipo", logo.getTipo());
        try {
            map.addValue("contenido",
                    new SqlLobValue(new ByteArrayInputStream(logo.getContenido().getBytes()),
                            logo.getContenido().getBytes().length, new DefaultLobHandler()),
                    Types.BLOB);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al actualizar el logo.";
        }
        // Se arma la sentencia de base de datos.
        String query = "UPDATE logo SET tipo = :tipo , contenido = :contenido  WHERE id = :id";
        int result = 0;
        try {
            // Se ejecuta a sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al actualizar el logo.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Actualizacion exitosa"
                : "El logo no se puede actualizar.";
    }

}
