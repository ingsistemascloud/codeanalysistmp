package co.ufps.edu.dao;

import co.ufps.edu.bd.SpringDbMgr;
import co.ufps.edu.constantes.Constantes;
import co.ufps.edu.dto.Contenido;
import java.util.LinkedList;
import java.util.List;
import co.ufps.edu.dto.ItemSubCategoria;
import co.ufps.edu.dto.SubCategoria;
import co.ufps.edu.dto.TipoContenido;
import org.apache.commons.io.Charsets;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * Clase que permite acceder a la capa de datos en el entorno de ítem de sub-categorias.
 * 
 * @author UFPS
 */
public class ItemSubCategoriaDao {

    private SpringDbMgr springDbMgr;
    /**
    * Constructor de la clase en donde se inicializan las variables.
    */
    public ItemSubCategoriaDao() {
        springDbMgr = new SpringDbMgr();
    }
    
    /**
     *  Método que obtiene la cantidad de ítem de subCategorias registradas.
     * 
     * @return Cantidad de ítem de subcategorias registradas.
     */
    public int getCantidadItemSubCategorias() {
        int cant = 0;
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT COUNT(id) item FROM itemsubcategoria ");
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            cant = sqlRowSet.getInt("item");
        }
        // Se retorna la cantidad de ítems desde base de datos.
        return cant;
    }

    /**
     * Método que consulta en base de datos todos los ítems de subcategorias existentes y
     * las devuelve ordenadamente.
     *
     * @return Una lista con todos los ítem de subcategorias.
     */
    public List<ItemSubCategoria> getItemSubCategorias() {
        // Lista para retornar con los datos.
        List<ItemSubCategoria> itemSubCategorias = new LinkedList<>();
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT subcategoria.ID idSubCategoria, subcategoria.NOMBRE nombreSubCategoria, subcategoria.DESCRIPCION descripcionSubCategoria, subcategoria.ORDEN ordenSubCategoria,"
                + " itemsubcategoria.ID idItemSubcategoria, itemsubcategoria.NOMBRE nombreItemSubCategoria, itemsubcategoria.DESCRIPCION descripcionItemSubCategoria,"
                + " itemsubcategoria.ORDEN ordenItemSubCategoria,"
                + " itemsubcategoria.subcategoria_id idsubcategoria"
                + " FROM itemsubcategoria"
                + " INNER JOIN subcategoria ON subcategoria.id = itemsubcategoria.subcategoria_id"
                + " ORDER BY subcategoria.ID ASC,itemsubcategoria.ORDEN DESC ");
        // Se recorre cada registro obtenido de base de datos.
        while (sqlRowSet.next()) {
            // Objeto en el que sera guardada la información del registro.
            ItemSubCategoria itemSubCategoria = new ItemSubCategoria();

            itemSubCategoria.setId(sqlRowSet.getLong("idItemSubcategoria"));
            itemSubCategoria.setNombre(sqlRowSet.getString("nombreItemSubCategoria"));
            itemSubCategoria.setDescripcion(sqlRowSet.getString("descripcionItemSubCategoria"));
            itemSubCategoria.setOrden(sqlRowSet.getInt("ordenItemSubCategoria"));
            itemSubCategoria.setSubcategoria_id(sqlRowSet.getLong("idsubcategoria"));
            // Objeto en el que sera guardada la información del registro.
            SubCategoria subcategoria = new SubCategoria();
            subcategoria.setId(sqlRowSet.getLong("idSubCategoria"));
            subcategoria.setNombre(sqlRowSet.getString("nombreSubCategoria"));
            subcategoria.setDescripcion(sqlRowSet.getString("descripcionSubCategoria"));
            subcategoria.setOrden(sqlRowSet.getInt("ordenSubCategoria"));
            itemSubCategoria.setSubcategoria(subcategoria);
            // Se guarda el registro para ser retornado.
            itemSubCategorias.add(itemSubCategoria);
        }
        // Se carga el contenido a cada actividad.
        for(ItemSubCategoria itemsub:itemSubCategorias){
            itemsub.setContenido(cargar(itemsub));
        }
        // Se retorna todos los ítems desde base de datos.
        return itemSubCategorias;
    }
    
    /**
     * Método que obtiene de base de datos la información del contenido de los ítems.
     * 
     * @param itemsubcategoria Item al que se carga contenido.
     * 
     * @return Contenido del item.
     */
    public Contenido cargar(ItemSubCategoria itemsubcategoria) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", itemsubcategoria.getId());
        map.addValue("tipo", Constantes.ITEMSUBCATEGORIA);
        Contenido contenido = new Contenido();
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT contenido.id idContenido,"
                + " contenido.contenido contenido, "
                + " contenido.asociacion asociacion,"
                + " contenido.TipoContenido_id tipoContenido,"
                + " contenido.titulo titulo"
                + " FROM contenido"
                + " JOIN itemsubcategoria ON contenido.asociacion = itemsubcategoria.id"
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
    
    /**
     * Método que consulta en la base de datos cual es el ultimo de número de
     * ordenamiento que hay entre todos los ítems.
     *
     * @param idItem Identificador del ítem.
     *
     * @return El último número de orden del ítem.
     */
    public int getUltimoNumeroDeOrden(long idItem) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", idItem);
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM itemsubcategoria WHERE subcategoria_id = :id ORDER BY ORDEN DESC", map);
        // Si existe al menos un ítem retorna el número.
        if (sqlRowSet.next()) {
            return (sqlRowSet.getInt("orden"));
        } else {
            // Si no existen ítems retorna el 0.
            return 0;
        }
    }
    
    /**
     * Método que baja un nivel al ítem en base de datos.
     *
     * @param idSubCategoria Identificador de la subcategoria.
     * @param idItemSubCategoria Identificador del ítem.
     * @param orden Orden al que va a pasar el ítem.
     */
    public void descenderOrden(long idSubCategoria, long idItemSubCategoria, int orden) {
        // Se extrae el id del ítem siguiente.
        long idItemSubCategoriaSiguiente = getIdItemSubCategoriaPorOrden(idSubCategoria, orden + 1);
        // Se modifica el orden actual.
        cambiarOrdenDeItemSubCategoria(idSubCategoria, idItemSubCategoria, -1);
        // Se modifica el orden del siguiente ítem.
        cambiarOrdenDeItemSubCategoria(idSubCategoria, idItemSubCategoriaSiguiente, orden);
        // Se modifica el orden del ítem actual.
        cambiarOrdenDeItemSubCategoria(idSubCategoria, idItemSubCategoria, orden + 1);
    }
    
    /**
     * Método que sube un nivel al ítem en base de datos.
     *
     * @param idSubCategoria Identificador de la subcategoria.
     * @param idItemSubCategoria Identificador del ítem.
     * @param orden Orden al que va a pasar el ítem.
     */
    public void ascenderOrden(long idSubCategoria, long idItemSubCategoria, int orden) {
        // Se extrae el id del ítem anterior.
        long idItemSubCategoriaAnterior = getIdItemSubCategoriaPorOrden(idSubCategoria, orden - 1);
        // Se modifica el orden actual.
        cambiarOrdenDeItemSubCategoria(idSubCategoria, idItemSubCategoria, -1);
        // Se modifica el orden del siguiente ítem.
        cambiarOrdenDeItemSubCategoria(idSubCategoria, idItemSubCategoriaAnterior, orden);
        // Se modifica el orden del ítem actual.
        cambiarOrdenDeItemSubCategoria(idSubCategoria, idItemSubCategoria, orden - 1);
    }
    
    /**
     * Método que consulta en base de datos el ID de un ítem dado un
     * numero de orden.
     *
     * @param idItem Identificador del ítem.
     * @param orden Número de orden por el cual se filtrará la busqueda.
     * 
     * @return El ID del ítem.
     */
    public long getIdItemSubCategoriaPorOrden(long idItem, int orden) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("orden", orden);
        map.addValue("subcat", idItem);
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM itemsubcategoria WHERE orden = :orden AND subcategoria_id = :subcat", map);
        // Si existe al menos una categoria retorna el número.
        if (sqlRowSet.next()) {
            return (sqlRowSet.getLong("id"));
        } else {
            // Si no existen ítem retorna el 0.
            return 0;
        }
    }
    
    /**
     * Método que actualiza el orden del ítem.
     *
     * @param idSubCategoria Identificador de la subcategoria.
     * @param idItemSubCategoria Identificador del ítem.
     * @param orden Número para actualizar el ítem.
     */
    public void cambiarOrdenDeItemSubCategoria(long idSubCategoria, long idItemSubCategoria, int orden) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", idItemSubCategoria);
        map.addValue("orden", orden);
        map.addValue("subcat", idSubCategoria);
        // Se arma la sentencia de base de datos.
        String query = "UPDATE itemsubcategoria SET orden = :orden WHERE id = :id AND subcategoria_id = :subcat";
        try {
            // Se ejecuta la sentencia.
            springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            new Exception();
        }
    }
    
    /**
     * Método que válida si el nombre del ítem se puede registrar.
     *
     * @param id Identificador del ítem.
     * @param nombre Nombre del ítem a registrar.
     * 
     * @return Retorna true si es válido el nombre del ítem, si no retorna false.
     */
    public boolean esNombreValido(long id, String nombre) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", id);
        map.addValue("nombre", nombre);
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM itemsubcategoria WHERE subcategoria_id = :id AND NOMBRE = :nombre", map);
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            // Si no es válido retorna el false.
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * Método que registra un ítem en base de datos.
     *
     * @param itemSubCategoria Objeto con todos los datos del ítem a registrar.
     * 
     * @return El resultado de la acción.
     */
    public String registrarItemSubCategoria(ItemSubCategoria itemSubCategoria) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("nombre", itemSubCategoria.getNombre());
        map.addValue("descripcion", itemSubCategoria.getDescripcion());
        map.addValue("idSubCategoria", itemSubCategoria.getSubcategoria().getId());
        map.addValue("orden", getUltimoNumeroDeOrden(itemSubCategoria.getSubcategoria().getId()) + 1);
        // Se arma la sentencia de base de datos.
        String query = "INSERT INTO itemsubcategoria(nombre,descripcion,orden,subcategoria_id) VALUES(:nombre,:descripcion,:orden,:idSubCategoria)";
        int result = 0;
        try {
             // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al registrar el ítem de subcategoria.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Registro exitoso"
                : "El ítem de subcategoria no se puede registrar, revise que se este ingresando información válida.";

    }
    
    /**
     * Método que consulta en base de datos la información de un ítem dado.
     *
     * @param idItemSubCategoria Identificador del ítem.
     * 
     * @return La información de un ítem en un objeto.
     */
    public ItemSubCategoria obtenerItemSubCategoriaPorId(long idItemSubCategoria) {
        ItemSubCategoria itemSubCategoria = new ItemSubCategoria();
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", idItemSubCategoria);
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT subcategoria.ID idSubCategoria, subcategoria.NOMBRE nombreSubCategoria, subcategoria.DESCRIPCION descripcionSubCategoria, subcategoria.ORDEN ordenSubCategoria,"
                + " itemsubcategoria.ID idItemSubcategoria, itemsubcategoria.NOMBRE nombreItemSubCategoria, itemsubcategoria.DESCRIPCION descripcionItemSubCategoria,"
                + " itemsubcategoria.ORDEN ordenItemSubCategoria"
                + " FROM itemsubcategoria"
                + " INNER JOIN subcategoria ON subcategoria.id = itemsubcategoria.subcategoria_id"
                + " WHERE itemsubcategoria.ID = :id"
                + " ORDER BY subcategoria.ID ASC,itemsubcategoria.ORDEN ASC", map);
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            // Se almacenan los datos del ítem.
            itemSubCategoria.setId(sqlRowSet.getLong("idItemSubcategoria"));
            itemSubCategoria.setNombre(sqlRowSet.getString("nombreItemSubCategoria"));
            itemSubCategoria.setDescripcion(sqlRowSet.getString("descripcionItemSubCategoria"));
            itemSubCategoria.setOrden(sqlRowSet.getInt("ordenItemSubCategoria"));

            SubCategoria subcategoria = new SubCategoria();
            subcategoria.setId(sqlRowSet.getLong("idSubCategoria"));
            subcategoria.setNombre(sqlRowSet.getString("nombreSubCategoria"));
            subcategoria.setDescripcion(sqlRowSet.getString("descripcionSubCategoria"));
            subcategoria.setOrden(sqlRowSet.getInt("ordenSubCategoria"));
            // Se guarda el registro para ser retornado.
            itemSubCategoria.setSubcategoria(subcategoria);
        }
        // Se retorna el ítem desde base de datos.
        return itemSubCategoria;
    }
    
    /**
     * Método que válida si el nombre del ítem se puede actualizar.
     *
     * @param id Identificador de la subcategoria.
     * @param item Identificador del ítem.
     * @param nombre Nombre del ítem a registrar.
     * 
     * @return Retorna true si es válido el nombre, si no retorna false.
     */
    public boolean esNombreValidoParaActualizar(long id, long item, String nombre) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", id);
        map.addValue("nombre", nombre);
        map.addValue("item", item);
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM itemsubcategoria WHERE subcategoria_id = :id AND NOMBRE = :nombre AND NOT id=:item", map);
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            // Si es válido retorna false.
            return false;
        } else {
            // Si es válido retorna true.
            return true;
        }
    }
    
    /**
     * Método que edita en base de datos la información de un ítem dado.
     *
     * @param itemsubcategoria ´Item a editar.
     * 
     * @return El resultado de la acción.
     */
    public String editarItemSubCategoria(ItemSubCategoria itemsubcategoria) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", itemsubcategoria.getId());
        map.addValue("nombre", itemsubcategoria.getNombre());
        map.addValue("descripcion", itemsubcategoria.getDescripcion());
        // Se arma la sentencia de base de datos.
        String query = "UPDATE itemsubcategoria SET nombre = :nombre, descripcion = :descripcion WHERE id = :id";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al actualizar el ítem de subcategoria.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Actualizacion exitosa"
                : "El ítem de subcategoria no se puede actualizar, revise que se este ingresando información válida.";
    }
    
    /**
     * Método que elimina en base de datos la información de un ítem dado.
     *
     * @param itemSubCategoria Item a editar.
     * 
     * @return El resultado de la acción.
     */
    public String eliminarItemSubCategoria(ItemSubCategoria itemSubCategoria) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", itemSubCategoria.getId());
        // Se arma la sentencia de base de datos.
        String query = "DELETE FROM itemsubcategoria WHERE id = :id";
        int result = 0;
        try {
            // Se ejecuta la sentencia.
            result = springDbMgr.executeDml(query, map);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al eliminar el ítem de subcategoria.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Eliminacion exitosa"
                : "El ítem de subcategoria tiene contenido asociado. Debe eliminar el contenido y las subcategorias asociadas a este ítem para poder realizar la eliminación.";
    }
}
