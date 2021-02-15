package co.ufps.edu.bd;

import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import co.ufps.edu.dto.ResultDB;
import co.ufps.edu.util.FileUtil;


/**
 * Clase con operaciones básicas para interactuar con el sistema de base de datos.
 *
 * <p>
 * Esta clase contiene implementación específica utilizando el proyecto Spring-JDBC. por
 * Más información consulte la documentación de referencia de Spring Framework.
 *
 * @see <a href =
 * "https://docs.spring.io/spring/docs/current/spring-framework-reference/html/spring-data-tier.html">
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/html/spring-data-tier.html
 * </a>
 * 
 * @author UFPS
 */
public class SpringDbMgr {

    private DataSource dataSource;
    private FileUtil fileUtil;

    /**
     * Constructor de la clase en donde se inicializan las variables.
     */
    public SpringDbMgr() {
        fileUtil = new FileUtil();
        initDataSource2();
    }

    /**
     * Método inicializador del dataSource, usa las propiedades ingresadas en el
     * database.properties
     */
    private void initDataSource2() {

        try {
            Class.forName(fileUtil.getProperties().getProperty("driver"));
        } catch (ClassNotFoundException e) {
            new Exception();
        }
        Properties p = new Properties();
        p.setProperty("user", fileUtil.getProperties().getProperty("user"));
        p.setProperty("password", fileUtil.getProperties().getProperty("password"));
        p.setProperty("driverClassName", fileUtil.getProperties().getProperty("driver"));

        dataSource = new DriverManagerDataSource(
                "jdbc:" + fileUtil.getProperties().getProperty("databasetype") + "://"
                + fileUtil.getProperties().getProperty("ip") + ":" + fileUtil.getProperties().getProperty("portdabase")
                + "/" + fileUtil.getProperties().getProperty("databasename")
                + "?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false", p);

    }

    /**
     * Este método implementa la lógica de ejecución de consulta SELECT sin
     * parámetros en Sistema de base de datos utilizando Spring-JDBC.
     *
     * @param query Texto que representa la consulta a ejecutar.
     *
     * @return Conjunto de filas devueltas por la consulta.
     */
    public SqlRowSet executeQuery(String query) {

        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
            SqlRowSet sqlRowSet = namedParameterJdbcTemplate.queryForRowSet(query, mapSqlParameterSource);
            return sqlRowSet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Este método implementa la lógica de ejecución de consulta SELECT en el
     * sistema de base de datos utilizando Spring-JDBC.
     *
     * @param query Texto que representa la consulta a ejecutar.
     * @param parameterMap Objeto que contiene todos los parámetros necesarios
     * para enlazar SQL variables.
     *
     * @return Conjunto de filas devueltas por la consulta.
     */
    public SqlRowSet executeQuery(String query, MapSqlParameterSource parameterMap) {

        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
            SqlRowSet sqlRowSet = namedParameterJdbcTemplate.queryForRowSet(query, parameterMap);
            return sqlRowSet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Este método implementa la lógica de ejecución de consulta INSERT / UPDATE
     * / DELETE sin Parámetros en el sistema de base de datos utilizando
     * Spring-JDBC.
     *
     * @param query Texto que representa la consulta a ejecutar.
     *
     * @return El número de filas afectadas.
     */
    public int executeDml(String query) {

        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
            int affectedRows = namedParameterJdbcTemplate.update(query, mapSqlParameterSource);
            return affectedRows;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }

    /**
     * Este método implementa la lógica de ejecución de consulta INSERT / UPDATE
     * / DELETE en Sistema de base de datos utilizando Spring-JDBC.
     *
     * @param query Texto que representa DML a ejecutar.
     * @param parameterMap Objeto que contiene todos los parámetros necesarios
     * para enlazar SQL variables.
     *
     * @return El número de filas afectadas.
     */
    public int executeDml(String query, MapSqlParameterSource parameterMap) {

        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
            KeyHolder keyHolder = new GeneratedKeyHolder();
            int affectedRows = namedParameterJdbcTemplate.update(query, parameterMap, keyHolder);
            return affectedRows;
        } catch (Exception e) {
        }
        return 0;

    }

    /**
     * Este método implementa la lógica de ejecución de consulta INSERT / UPDATE
     * / DELETE en Sistema de base de datos utilizando Spring-JDBC.
     *
     * @param query Texto que representa DML a ejecutar.
     * @param parameterMap Objeto que contiene todos los parámetros necesarios
     * para enlazar SQL variables.
     * 
     * @ Devolver el número de filas afectadas.
     */
    public ResultDB executeDmlWithKey(String query, MapSqlParameterSource parameterMap) {

        ResultDB resultDB = new ResultDB();
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int affectedRows = namedParameterJdbcTemplate.update(query, parameterMap, keyHolder);
        Long generatedId = keyHolder.getKey().longValue();
        resultDB.setResult(affectedRows);
        resultDB.setKey(generatedId);
        return resultDB;

    }

    /**
     * Set de dataSource.
     *
     * @param dataSource Nuevo valor del datasource.
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
