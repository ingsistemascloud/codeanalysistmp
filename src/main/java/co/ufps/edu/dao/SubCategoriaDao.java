package co.ufps.edu.dao;

import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.Charsets;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import co.ufps.edu.bd.SpringDbMgr;
import co.ufps.edu.constantes.Constantes;
import co.ufps.edu.dto.Categoria;
import co.ufps.edu.dto.Contenido;
import co.ufps.edu.dto.ItemSubCategoria;
import co.ufps.edu.dto.SubCategoria;
import co.ufps.edu.dto.TipoContenido;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase que permite acceder a la capa de datos en el entorno de sub-categorias.
 * 
 * @author UFPS
 */
public class SubCategoriaDao {

    private SpringDbMgr springDbMgr;

    /**
     * Constructor de la clase en donde se inicializan las variables.
     */
    public SubCategoriaDao() {
        springDbMgr = new SpringDbMgr();
    }

    /**
     * Método que consulta en base de datos todas las subcategorias existentes y
     * las devuelve ordenadamente.
     *
     * @return Una lista con todas las subcategorias.
     */
    public List<SubCategoria> getSubCategorias() {
        List<SubCategoria> subCategorias = new LinkedList<>();
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT categoria.ID idCategoria,"
                + " categoria.NOMBRE nombreCategoria,"
                + " categoria.DESCRIPCION descripcionCategoria,"
                + " categoria.ORDEN ordenCategoria,"
                + " subcategoria.ID idSubcategoria,"
                + " subcategoria.NOMBRE nombreSubCategoria,"
                + " subcategoria.DESCRIPCION descripcionSubCategoria,"
                + " subcategoria.ORDEN ordenSubCategoria"
                + " FROM subcategoria"
                + " INNER JOIN categoria ON categoria.id = subcategoria.Categoria_id"
                + " ORDER BY categoria.ID ASC,subcategoria.ORDEN DESC");
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            // Objeto en el que se guarda la información del registro.
            SubCategoria subCategoria = new SubCategoria();

            subCategoria.setId(sqlRowSet.getLong("idSubcategoria"));
            subCategoria.setNombre(sqlRowSet.getString("nombreSubCategoria"));
            subCategoria.setDescripcion(sqlRowSet.getString("descripcionSubCategoria"));
            subCategoria.setOrden(sqlRowSet.getInt("ordenSubCategoria"));

            Categoria categoria = new Categoria();
            categoria.setId(sqlRowSet.getLong("idCategoria"));
            categoria.setNombre(sqlRowSet.getString("nombreCategoria"));
            categoria.setDescripcion(sqlRowSet.getString("descripcionCategoria"));
            categoria.setOrden(sqlRowSet.getInt("ordenCategoria"));
            subCategoria.setCategoria(categoria);
            // Se guarda el registro para ser retornado.
            subCategorias.add(subCategoria);
        }
        for(SubCategoria sub:subCategorias){
            // Se carga el contenido.
            sub.setContenido(cargar(sub));
        }
        return subCategorias;
    }

    /**
     * Método que registra una subcategoria en base de datos.
     *
     * @param subcategoria Objeto con todos los datos de la subcategoria a registrar.
     * 
     * @return El resultado de la acción.
     */
    public String registrarSubCategoria(SubCategoria subcategoria) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("nombre", subcategoria.getNombre());
        map.addValue("descripcion", subcategoria.getDescripcion());
        map.addValue("idCategoria", subcategoria.getCategoria().getId());
        map.addValue("orden", getUltimoNumeroDeOrden(subcategoria.getCategoria().getId()) + 1);
        // Se arma la sentencia de base de datos.
        String query = "INSERT INTO subcategoria(nombre,descripcion,orden,Categoria_id) VALUES(:nombre,:descripcion,:orden,:idCategoria)";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al registrar la subcategoria.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Registro exitoso"
                : "La subcategoria no se puede registrar, revise que se este ingresando información válida.";

    }

    /**
     * Método que consulta en la base de datos cual es el último de número de
     * ordenamiento que hay entre todas las subcategorias.
     *
     * @param idCategoria Idnetificador de la categoria
     *
     * @return El último número de orden de categoria.
     */
    public int getUltimoNumeroDeOrden(long idCategoria) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", idCategoria);
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM subcategoria WHERE Categoria_id = :id ORDER BY ORDEN DESC", map);
        if (sqlRowSet.next()) {
            // Si existe al menos una subcategoria retorna el número.
            return (sqlRowSet.getInt("orden"));
        } else {
            // Si no existen subcategorias retorna el 0.
            return 0;
        }
    }

    /**
     * Método que baja un nivel a la subcategoria en base de datos.
     *
     * @param idCategoria Identificador de la categoria.
     * @param idSubCategoria Identificador de la subcategoria.
     * @param orden Orden de la subcategoria.
     */
    public void descenderOrden(long idCategoria, long idSubCategoria, int orden) {
        // Se extrae el id de la siguiente subcategoria.
        long idSubCategoriaSiguiente = getIdSubCategoriaPorOrden(idCategoria, orden + 1);
        // Se modifica el orden actual de la subcategoria.
        cambiarOrdenDeSubCategoria(idCategoria, idSubCategoria, -1);
        // Se modifica el orden de la siguiente subcategoria.
        cambiarOrdenDeSubCategoria(idCategoria, idSubCategoriaSiguiente, orden);
        // Se modifica el orden de la subcategoria actual.
        cambiarOrdenDeSubCategoria(idCategoria, idSubCategoria, orden + 1);
    }

    /**
     * Método que consulta en base de datos el ID de una subcategoria dado un
     * número de orden.
     *
     * @param idCategoria Identificador de la categoria.
     * @param orden Número de orden por el cual se filtra la busqueda.
     * 
     * @return El ID de la subcategoria.
     */
    public long getIdSubCategoriaPorOrden(long idCategoria, int orden) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("orden", orden);
        map.addValue("cat", idCategoria);
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM subcategoria WHERE orden = :orden AND Categoria_id = :cat", map);
        if (sqlRowSet.next()) {
            // Si existe al menos una subcategoria retorna el número.
            return (sqlRowSet.getLong("id"));
        } else {
            // Si no existen subcategorias retorna el 0.
            return 0;
        }
    }

    /**
     * Método que actualiza el orden de una subcategoria.
     *
     * @param idCategoria Identificador de la categoria.
     * @param idSubCategoria Identificador de la subcategoria.
     * @param orden Orden de la subcategoria.
     */
    public void cambiarOrdenDeSubCategoria(long idCategoria, long idSubCategoria, int orden) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", idSubCategoria);
        map.addValue("orden", orden);
        map.addValue("cat", idCategoria);
        // Se arma la sentencia de base de datos.
        String query = "UPDATE subcategoria SET orden = :orden WHERE id = :id AND Categoria_id = :cat";
        try {
            // Se ejecuta la sentencia.
            springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            new Exception();
        }
    }

    /**
     * Método que permite subir de orden una subcategoria en base de datos.
     *
     * @param idCategoria Identificador de la categoria.
     * @param idSubCategoria Identificador de la subcategoria.
     * @param orden Número de orden.
     */
    public void ascenderOrden(long idCategoria, long idSubCategoria, int orden) {
        // Se extrae el id de la subcategoria anterior.
        long idCategoriaAnterior = getIdSubCategoriaPorOrden(idCategoria, orden - 1);
        // Se modifica el orden actual de la subcategoria.
        cambiarOrdenDeSubCategoria(idCategoria, idSubCategoria, -1);
        // Se modifica el orden de la anterior de la subcategoria.
        cambiarOrdenDeSubCategoria(idCategoria, idCategoriaAnterior, orden);
        // Se modifica el orden de la subcategoria actual.
        cambiarOrdenDeSubCategoria(idCategoria, idSubCategoria, orden - 1);
    }

    /**
     * Método que consulta en base de datos la información de una subcategoria dada.
     *
     * @param idSubCategoria Identificador de la subcategoria.
     * 
     * @return La información de una subcategoria en un objeto.
     */
    public SubCategoria obtenerSubCategoriaPorId(long idSubCategoria) {
        SubCategoria subCategoria = new SubCategoria();
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", idSubCategoria);
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT categoria.ID idCategoria,"
                + " categoria.NOMBRE nombreCategoria,"
                + " categoria.DESCRIPCION descripcionCategoria,"
                + " categoria.ORDEN ordenCategoria,"
                + " subcategoria.ID idSubcategoria,"
                + " subcategoria.NOMBRE nombreSubCategoria,"
                + " subcategoria.DESCRIPCION descripcionSubCategoria,"
                + " subcategoria.ORDEN ordenSubCategoria"
                + " FROM subcategoria"
                + " INNER JOIN categoria ON categoria.id = subcategoria.Categoria_id"
                + " WHERE subcategoria.ID = :id"
                + " ORDER BY categoria.ID ASC,subcategoria.ORDEN ASC", map);
        // Se consulta si la subcategoria existe.
        if (sqlRowSet.next()) {

            subCategoria.setId(sqlRowSet.getLong("idSubcategoria"));
            subCategoria.setNombre(sqlRowSet.getString("nombreSubCategoria"));
            subCategoria.setDescripcion(sqlRowSet.getString("descripcionSubCategoria"));
            subCategoria.setOrden(sqlRowSet.getInt("ordenSubCategoria"));

            Categoria categoria = new Categoria();
            categoria.setId(sqlRowSet.getLong("idCategoria"));
            categoria.setNombre(sqlRowSet.getString("nombreCategoria"));
            categoria.setDescripcion(sqlRowSet.getString("descripcionCategoria"));
            categoria.setOrden(sqlRowSet.getInt("ordenCategoria"));
            subCategoria.setCategoria(categoria);

        }
        return subCategoria;
    }

    /**
     * Método que edita en base de datos la información de una subcategoria dada.
     *
     * @param subcategoria Subcategoria a editar.
     * 
     * @return Mensaje de acuerdo a la ejecución del método.
     */
    public String editarSubCategoria(SubCategoria subcategoria) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", subcategoria.getId());
        map.addValue("nombre", subcategoria.getNombre());
        map.addValue("descripcion", subcategoria.getDescripcion());
        // Se arma la sentencia de base de datos.
        String query = "UPDATE subcategoria SET nombre = :nombre, descripcion = :descripcion WHERE id = :id";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al actualizar la subcategoria.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Actualizacion exitosa"
                : "La subcategoria no se puede actualizar, revise que se este ingresando información válida.";
    }

    /**
     * Método que elimina en base de datos la información de una subcategoria dada.
     *
     * @param subcategoria Subcategoria a editar.
     * 
     * @return Mensaje de acuerdo a la ejecución del método.
     */
    public String eliminarSubCategoria(SubCategoria subcategoria) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", subcategoria.getId());
        // Se arma la sentencia de base de datos.
        String query = "DELETE FROM subcategoria WHERE id = :id";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al eliminar la subcategoria.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Eliminacion exitosa"
                : "La subcategoria tiene contenido asociado. Debe eliminar el contenido y los ítem asociados a esta subcategoria para poder realizar la eliminacíon.";
    }

    /**
     * Método en donde se válida si el nombre de la subcategoria se puede registrar.
     * 
     * @param id Identificador de la subcategoria.
     * @param nombre Nombre de la subcategoria.    
     * 
     * @return True si el nombre es válido.
     */
    public boolean esNombreValido(long id, String nombre) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", id);
        map.addValue("nombre", nombre);
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM subcategoria WHERE Categoria_id = :id AND NOMBRE = :nombre", map);
        if (sqlRowSet.next()) {
            // Si el nombre no es válido retorna false.
            return false;
        } else {
            // Si el nombre es válido retorna true.
            return true;
        }
    }

    /**
     * Método en donde se válida si el nombre de la subcategoria se puede actualizar.
     * 
     * @param id Identificador de la categoria.
     * @param sub Identificador de la subcategoria.
     * @param nombre Nombre de la subcategoria.    
     * 
     * @return True si el nombre es válido.
     */
    public boolean esNombreValidoParaActualizar(long id, long sub, String nombre) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", id);
        map.addValue("nombre", nombre);
        map.addValue("sub", sub);
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM subcategoria WHERE Categoria_id = :id AND NOMBRE = :nombre AND NOT id=:sub", map);
        if (sqlRowSet.next()) {
            // Si el nombre no es válido retorna false.
            return false;
        } else {
            // Si el nombre es válido retorna true.
            return true;
        }
    }

    /**
     * Método que obtiene la cantidad de subCategorias registradas.
     * 
     * @return Cantidad de subcategorias registradas.
     */
    public int getCantidadSubCategorias() {
        int cant = 0;
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT COUNT(id) cantidad FROM subcategoria ");
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            cant = sqlRowSet.getInt("cantidad");
        }
        // Se retorna la cantidad de subcategorias desde base de datos.
        return cant;
    }
   
    /**
     * Método que consulta en base de datos la información de una subcategoria dada.
     *
     * @param idSubCategoria Identificador de la subcategoria.
     * 
     * @return La información de una subcategoria en un objeto.
     */
    public SubCategoria obtenerContenidoSubCategoriaPorId(long idSubCategoria) {
        SubCategoria subCategoria = new SubCategoria();
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", idSubCategoria);
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT subcategoria.ID idSubcategoria,"
                + " subcategoria.NOMBRE nombreSubCategoria,"
                + " subcategoria.DESCRIPCION descripcionSubCategoria,"
                + " subcategoria.ORDEN ordenSubCategoria,"
                + " contenido.id idContenido,"
                + " contenido.contenido contenido,"
                + " contenido.TipoContenido_id tipoContenido,"
                + " contenido.asociacion asociacion,"
                + " contenido.tipoasociacion tipoasociacion,"
                + " contenido.titulo titulo"
                + " FROM subcategoria"
                + " INNER JOIN contenido ON contenido.asociacion = subcategoria.id"
                + " WHERE subcategoria.ID = :id", map);
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {

            subCategoria.setId(sqlRowSet.getLong("idSubcategoria"));
            subCategoria.setNombre(sqlRowSet.getString("nombreSubCategoria"));
            subCategoria.setDescripcion(sqlRowSet.getString("descripcionSubCategoria"));
            subCategoria.setOrden(sqlRowSet.getInt("ordenSubCategoria"));

            Contenido contenido = new Contenido();
            contenido.setId(sqlRowSet.getLong("idContenido"));

            byte[] a = (byte[]) sqlRowSet.getObject("contenido");
            String res = new String(a, Charsets.UTF_8);
            contenido.setContenido(res);
            contenido.setAsociacion(sqlRowSet.getLong("asociacion"));
            contenido.setNombre(sqlRowSet.getString("titulo"));
            contenido.setTipoAsociacion(sqlRowSet.getString("tipoasociacion"));

            subCategoria.setContenido(contenido);
        }
        return subCategoria;
    }
    
    /**
     * Método que obtiene de base de datos la información de las subcategorias.
     * 
     * @return Si mensaje de acuerdo a la ejecución del método.
     */
    public Map<Long, String> getMapaDeSubCategorias() {
        Map<Long, String> subcategorias = new HashMap<Long, String>();
        // Se arma la sentencia de consulta de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM subcategoria ORDER BY ORDEN ASC");
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            subcategorias.put(sqlRowSet.getLong("id"), sqlRowSet.getString("nombre"));
        }
        // Se retornan todas las subcategorias desde base de datos.
        return subcategorias;
    }
    
    /**
     * Método que obtiene de base de datos la información del contenido de las subcategorias.
     * 
     * @param subcategoria Subcategoria a la que se carga contenido.
     * 
     * @return Subcategoria con el contenido.
     */
    public Contenido cargar(SubCategoria subcategoria) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", subcategoria.getId());
        map.addValue("tipo", Constantes.SUBCATEGORIA);
        Contenido contenido = new Contenido();
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT contenido.id idContenido,"
                + " contenido.contenido contenido, "
                + " contenido.asociacion asociacion,"
                + " contenido.TipoContenido_id tipoContenido,"
                + " contenido.titulo titulo"
                + " FROM contenido"
                + " JOIN subcategoria ON contenido.asociacion = subcategoria.id"
                + " WHERE contenido.asociacion = :id"
                + " AND contenido.tipoasociacion = :tipo", map); 
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            // Objeto en el que se guarda la información del registro.
            contenido.setId(sqlRowSet.getLong("idContenido"));
            byte[] a = (byte[]) sqlRowSet.getObject("contenido");
            String res = new String(a, Charsets.UTF_8);
            contenido.setContenido(res);
            
            TipoContenido tipoContenido = new TipoContenido();
            tipoContenido.setId(sqlRowSet.getLong("tipoContenido"));
            contenido.setTipoContenido(tipoContenido);
        }
        return contenido;
    }
}
