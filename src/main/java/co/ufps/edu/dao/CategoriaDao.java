package co.ufps.edu.dao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.Charsets;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import co.ufps.edu.bd.SpringDbMgr;
import co.ufps.edu.constantes.Constantes;
import co.ufps.edu.dto.Categoria;
import co.ufps.edu.dto.Contenido;
import co.ufps.edu.dto.SubCategoria;
import co.ufps.edu.dto.TipoContenido;

/**
 * Clase que permite acceder a la capa de datos en el entorno de categorias.
 * 
 * @author UFPS
 */
public class CategoriaDao {

    private SpringDbMgr springDbMgr;

    /**
     * Constructor de la clase en donde se inicializan las variables.
     */
    public CategoriaDao() {
        springDbMgr = new SpringDbMgr();
    }

    /**
     * Método que consulta en base de datos todas las categorias existentes y
     * las devuelve ordenadamente.
     *
     * @return Una lista con todas las categorias.
     */
    public List<Categoria> getCategorias() {
        // Lista para retornar con los datos.
        List<Categoria> categorias = new LinkedList<>();
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM categoria ORDER BY ORDEN DESC");
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            // Objeto en el que sera guardada la información del registro.
            Categoria categoria = new Categoria();
            categoria.setId(sqlRowSet.getLong("id"));
            categoria.setNombre(sqlRowSet.getString("nombre"));
            categoria.setDescripcion(sqlRowSet.getString("descripcion"));
            categoria.setOrden(sqlRowSet.getInt("orden"));
            // Se guarda el registro para ser retornado.
            categorias.add(categoria);
        }
        // Se carga el contenido a cada actividad.
        for(Categoria ca:categorias){
            ca.setContenido(cargar(ca)); 
        }
        // Se retorna todos las categorias desde base de datos.
        return categorias;
    }

    /**
     * Método que registra una categoria en base de datos.
     *
     * @param categoria Objeto con todos los datos de la categoria a registrar.
     * 
     * @return El resultado de la acción.
     */
    public String registrarCategoria(Categoria categoria) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("nombre", categoria.getNombre());
        map.addValue("descripcion", categoria.getDescripcion());
        map.addValue("orden", getUltimoNumeroDeOrden() + 1);
        // Se arma la sentencia de base de datos.
        String query = "INSERT INTO categoria(nombre,descripcion,orden) VALUES(:nombre,:descripcion,:orden)";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al registrar la categoría.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Registro exitoso"
                : "La categoría no se puede registrar, revise que se este ingresando información válida.";

    }

    /**
     * Método que consulta en la base de datos cual es el ultimo de número de
     * ordenamiento que hay entre todas las categorias.
     *
     * @return El último número de orden de categoria.
     */
    public int getUltimoNumeroDeOrden() {
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM categoria ORDER BY ORDEN DESC");
        // Si existe al menos una categoria retorna el número.
        if (sqlRowSet.next()) {
            // Si no existen categorias retorna el 0.
            return (sqlRowSet.getInt("orden"));
        } else {
            return 0;
        }
    }

    /**
     * Método que baja un nivel a la categoria en base de datos.
     *
     * @param idCategoria Identificador de la categoria.
     * @param orden Orden al que va a pasar la categoria.
     */
    public void descenderOrden(long idCategoria, int orden) {
        // Se extrae el id de la siguiente.
        long idCategoriaSiguiente = getIdCategoriaPorOrden(orden + 1);
        // Se modifica el orden actual.
        cambiarOrdenDeCategoria(idCategoria, -1);
        // Se modifica el orden de la siguiente categoria.
        cambiarOrdenDeCategoria(idCategoriaSiguiente, orden);
        // Se modifica el orden de la categoria actual.
        cambiarOrdenDeCategoria(idCategoria, orden + 1);
    }

    /**
     * Método que consulta en base de datos el ID de una categoria dado un
     * número de orden.
     *
     * @param orden Número de orden por el cual se filtrara la busqueda.
     * 
     * @return El ID de la categoria.
     */
    public long getIdCategoriaPorOrden(int orden) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("orden", orden);
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM categoria WHERE orden = :orden ", map);
        // Si existe al menos una categoria retorna el número.
        if (sqlRowSet.next()) {
            return (sqlRowSet.getLong("id"));
        } else {
            // Si no existen categorias retorna el 0.
            return 0;
        }
    }

    /**
     * Método que actualiza el orden de una categoria.
     *
     * @param id Identificador de la categora.
     * @param orden Orden para actualizar a la categoria.
     */
    public void cambiarOrdenDeCategoria(long id, int orden) {
        SpringDbMgr springDbMgr = new SpringDbMgr();
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", id);
        map.addValue("orden", orden);
        // Se arma la sentencia de base de datos.
        String query = "UPDATE categoria SET orden = :orden WHERE id = :id";
        try {
            // Se ejecuta la sentencia.
            springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            new Exception();
        }
    }

    /**
     * Método que permite subir de orden una categoria en base de datos.
     *
     * @param idCategoria Identificador de la categoria.
     * @param orden Número de orden.
     */
    public void ascenderOrden(long idCategoria, int orden) {
        // Se extrae el id de la categoria anterior.
        long idCategoriaAnterior = getIdCategoriaPorOrden(orden - 1);
        // Se modifica el orden actual.
        cambiarOrdenDeCategoria(idCategoria, -1);
        // Se modifica el orden de la anterior categoria.
        cambiarOrdenDeCategoria(idCategoriaAnterior, orden);
        // Se ,odifica el orden de la categoria actual.
        cambiarOrdenDeCategoria(idCategoria, orden - 1);
    }

    /**
     * Método que consulta en base de datos la información de una categoria dada.
     *
     * @param idCategoria Identificador de la categoria.
     * 
     * @return La información de una categoria en un objeto.
     */
    public Categoria obtenerCategoriaPorId(long idCategoria) {
        Categoria categoria = new Categoria();
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", idCategoria);
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM categoria WHERE id = :id", map);
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            // Se almacenan los datos de la categoria.
            categoria.setId(sqlRowSet.getLong("id"));
            categoria.setNombre(sqlRowSet.getString("nombre"));
            categoria.setDescripcion(sqlRowSet.getString("descripcion"));
            categoria.setOrden(sqlRowSet.getInt("orden"));
        }
        // Se retorna la categoria desde base de datos.
        return categoria;
    }

    /**
     * Método que edita en base de datos la información de una categoria dada.
     *
     * @param categoria Categoria a editar.
     * 
     * @return Si mensaje de acuerdo a la ejecución del método.
     */
    public String editarCategoria(Categoria categoria) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", categoria.getId());
        map.addValue("nombre", categoria.getNombre());
        map.addValue("descripcion", categoria.getDescripcion());
        // Se arma la sentencia de base de datos.
        String query = "UPDATE categoria SET nombre = :nombre, descripcion = :descripcion WHERE id = :id";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al actualizar la categoría.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Actualizacion exitosa"
                : "La categoría no se puede editar, revise que se este ingresando información válida.";
    }

    /**
     * Método que elimina en base de datos la información de una categoria dada.
     *
     * @param categoria Categoria a editar.
     * 
     * @return Si mensaje de acuerdo a la ejecución del método.
     */
    public String eliminarCategoria(Categoria categoria) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", categoria.getId());
        // Se arma la sentencia de base de datos.
        String query = "DELETE FROM categoria WHERE id = :id";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al eliminar la categoría.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Eliminacion exitosa"
                : "La categoría no se puede eliminar, verifique que no tenga contenido asociado para realizar la acción.";
    }

    /**
     * Método que obtiene de base de datos la información de las categorias.
     * 
     * @return Si mensaje de acuerdo a la ejecución del método.
     */
    public Map<Long, String> getMapaDeCategorias() {
        // Lista para retornar con los datos.
        Map<Long, String> categorias = new HashMap<Long, String>();
        // Se arma la sentencia de consulta de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM categoria ORDER BY ORDEN ASC");
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            categorias.put(sqlRowSet.getLong("id"), sqlRowSet.getString("nombre"));
        }
        // Se retornan todas las categorias desde base de datos.
        return categorias;
    }

    /*
    * Método que obtiene la cantidad de categorias registradas.
    *
    * @return Cantidad de categorias registradas.
    */
    public int getCantidadCategorias() {
        int cant = 0;
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT COUNT(id) cantidad FROM categoria");
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            cant = sqlRowSet.getInt("cantidad");
        }
        // Se retorna la cantidad de categorias desde base de datos.
        return cant;
    }

    /**
     * Método que obtiene de base de datos la información de las categorias que tiene subcategorias.
     * 
     * @return Lista de categorias que tienen subcategorias.
     */
    public List<Categoria> getCategoriasConSubcategorias() {
        // Lista para retornar con los datos
        List<Categoria> categorias = new LinkedList<>();
        // Se obtienen la categorias.
        categorias = getCategorias();
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT categoria.id idCategoria,"
                + " categoria.nombre nombreCategoria,"
                + " categoria.descripcion descripcionCategoria,"
                + " categoria.orden ordenCategoria,"
                + " subcategoria.id idSubcategoria,"
                + " subcategoria.nombre nombreSubCategoria,"
                + " subcategoria.descripcion descripcionSubCategoria,"
                + " subcategoria.orden ordenSubCategoria"
                + " FROM subcategoria"
                + " INNER JOIN categoria ON categoria.id = subcategoria.Categoria_id"
                + " ORDER BY categoria.orden ASC,subcategoria.orden ASC");
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            // Objeto en el que sera guardada la informacion del registro.
            SubCategoria subCategoria = new SubCategoria();
            subCategoria.setId(sqlRowSet.getLong("idSubcategoria"));
            subCategoria.setNombre(sqlRowSet.getString("nombreSubCategoria"));
            subCategoria.setDescripcion(sqlRowSet.getString("descripcionSubCategoria"));
            subCategoria.setOrden(sqlRowSet.getInt("ordenSubCategoria"));
            // Se carga el contenido por cada categoria.
            cargarContenido(subCategoria);
            Categoria categoria = getCategoria(categorias, sqlRowSet.getLong("idCategoria"), sqlRowSet);
            // Se guarda el registro para ser retornado.
            categoria.agregarSubcategoria(subCategoria);
        }
        // Se retornan todas las categorias desde base de datos.
        return categorias;
    }

    /**
     * Método que obtiene de base de datos la información del contenido de las subcategorias.
     * 
     * @param subcategoria Subcategoria a la que se carga contenido.
     */
    private void cargarContenido(SubCategoria subCategoria) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", subCategoria.getId());
        map.addValue("tipo", Constantes.SUBCATEGORIA);
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT contenido.id idContenido,"
                + " contenido.contenido contenido, "
                + " contenido.TipoContenido_id tipoContenido,"
                + " subcategoria.id idSubcategoria,"
                + " subcategoria.nombre nombreSubCategoria,"
                + " subcategoria.descripcion descripcionSubCategoria,"
                + " subcategoria.orden ordenSubCategoria"
                + " FROM subcategoria"
                + " INNER JOIN contenido ON contenido.asociacion = subcategoria.id"
                + " WHERE subcategoria.id = :id"
                + " AND contenido.tipoasociacion = :tipo", map);

        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            // Objeto en el que sera guardada la informacion del registro
            Contenido contenido = new Contenido();
            contenido.setId(sqlRowSet.getLong("idContenido"));
            byte[] a = (byte[]) sqlRowSet.getObject("contenido");
            String res = new String(a, Charsets.UTF_8);
            contenido.setContenido(res);
            TipoContenido tipoContenido = new TipoContenido();
            tipoContenido.setId(sqlRowSet.getLong("tipoContenido"));
            contenido.setTipoContenido(tipoContenido);
            // Se guarda el contenido del registro.
            subCategoria.setContenido(contenido);
        }
    }

    /**
     * Método que obtiene de base de datos una categoria de acuerdo a un id.
     * 
     * @param categorias Lista de categorias a comparar.
     * @param idCategorias Identificador de la categoria que se quiere encontrar.
     * 
     * @return Categoria que se obtiene de base de datos.
     */
    private Categoria getCategoria(List<Categoria> categorias, long idCategoria, SqlRowSet sqlRowSet) {
        // Se recorre la lista de categorias.
        for (Categoria cat : categorias) {
            // Se valida si se encuenta la categoria deseada.
            if (cat.getId() == idCategoria) {
                // Se retorna la categorias si se encuentra.
                return cat;
            }
        }
        // Se carga información de la categoria si no se encuentra en la lista. 
        Categoria categoria = new Categoria();
        categoria.setId(sqlRowSet.getLong("idCategoria"));
        categoria.setNombre(sqlRowSet.getString("nombreCategoria"));
        categoria.setDescripcion(sqlRowSet.getString("descripcionCategoria"));
        categoria.setOrden(sqlRowSet.getInt("ordenCategoria"));
        categorias.add(categoria);
        return categoria;
    }
    
    /**
     * Método que obtiene de base de datos la información del contenido de las categorias.
     * 
     * @param categoria Categoria a la que se carga contenido.
     * 
     * @return Contenido de la categoria.
     */
    public Contenido cargar(Categoria categoria) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", categoria.getId());
        map.addValue("tipo", Constantes.CATEGORIA);
        Contenido contenido = new Contenido();
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT contenido.id idContenido,"
                + " contenido.contenido contenido, "
                + " contenido.asociacion asociacion,"
                + " contenido.TipoContenido_id tipoContenido,"
                + " contenido.titulo titulo"
                + " FROM contenido"
                + " JOIN categoria ON contenido.asociacion = categoria.id"
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
