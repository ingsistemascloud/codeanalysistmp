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
 * Clase que permite acceder a la capa de datos en el entorno de �tem de sub-categorias.
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
     *  M�todo que obtiene la cantidad de �tem de subCategorias registradas.
     * 
     * @return Cantidad de �tem de subcategorias registradas.
     */
    public int getCantidadItemSubCategorias() {
        int cant = 0;
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT COUNT(id) item FROM itemsubcategoria ");
        // Se recorre cada registro obtenido de base de datos.
        if (sqlRowSet.next()) {
            cant = sqlRowSet.getInt("item");
        }
        // Se retorna la cantidad de �tems desde base de datos.
        return cant;
    }

    /**
     * M�todo que consulta en base de datos todos los �tems de subcategorias existentes y
     * las devuelve ordenadamente.
     *
     * @return Una lista con todos los �tem de subcategorias.
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
            // Objeto en el que sera guardada la informaci�n del registro.
            ItemSubCategoria itemSubCategoria = new ItemSubCategoria();

            itemSubCategoria.setId(sqlRowSet.getLong("idItemSubcategoria"));
            itemSubCategoria.setNombre(sqlRowSet.getString("nombreItemSubCategoria"));
            itemSubCategoria.setDescripcion(sqlRowSet.getString("descripcionItemSubCategoria"));
            itemSubCategoria.setOrden(sqlRowSet.getInt("ordenItemSubCategoria"));
            itemSubCategoria.setSubcategoria_id(sqlRowSet.getLong("idsubcategoria"));
            // Objeto en el que sera guardada la informaci�n del registro.
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
        // Se retorna todos los �tems desde base de datos.
        return itemSubCategorias;
    }
    
    /**
     * M�todo que obtiene de base de datos la informaci�n del contenido de los �tems.
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
            // Objeto en el que se guarda la informaci�n del registro.
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
     * M�todo que consulta en la base de datos cual es el ultimo de n�mero de
     * ordenamiento que hay entre todos los �tems.
     *
     * @param idItem Identificador del �tem.
     *
     * @return El �ltimo n�mero de orden del �tem.
     */
    public int getUltimoNumeroDeOrden(long idItem) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", idItem);
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM itemsubcategoria WHERE subcategoria_id = :id ORDER BY ORDEN DESC", map);
        // Si existe al menos un �tem retorna el n�mero.
        if (sqlRowSet.next()) {
            return (sqlRowSet.getInt("orden"));
        } else {
            // Si no existen �tems retorna el 0.
            return 0;
        }
    }
    
    /**
     * M�todo que baja un nivel al �tem en base de datos.
     *
     * @param idSubCategoria Identificador de la subcategoria.
     * @param idItemSubCategoria Identificador del �tem.
     * @param orden Orden al que va a pasar el �tem.
     */
    public void descenderOrden(long idSubCategoria, long idItemSubCategoria, int orden) {
        // Se extrae el id del �tem siguiente.
        long idItemSubCategoriaSiguiente = getIdItemSubCategoriaPorOrden(idSubCategoria, orden + 1);
        // Se modifica el orden actual.
        cambiarOrdenDeItemSubCategoria(idSubCategoria, idItemSubCategoria, -1);
        // Se modifica el orden del siguiente �tem.
        cambiarOrdenDeItemSubCategoria(idSubCategoria, idItemSubCategoriaSiguiente, orden);
        // Se modifica el orden del �tem actual.
        cambiarOrdenDeItemSubCategoria(idSubCategoria, idItemSubCategoria, orden + 1);
    }
    
    /**
     * M�todo que sube un nivel al �tem en base de datos.
     *
     * @param idSubCategoria Identificador de la subcategoria.
     * @param idItemSubCategoria Identificador del �tem.
     * @param orden Orden al que va a pasar el �tem.
     */
    public void ascenderOrden(long idSubCategoria, long idItemSubCategoria, int orden) {
        // Se extrae el id del �tem anterior.
        long idItemSubCategoriaAnterior = getIdItemSubCategoriaPorOrden(idSubCategoria, orden - 1);
        // Se modifica el orden actual.
        cambiarOrdenDeItemSubCategoria(idSubCategoria, idItemSubCategoria, -1);
        // Se modifica el orden del siguiente �tem.
        cambiarOrdenDeItemSubCategoria(idSubCategoria, idItemSubCategoriaAnterior, orden);
        // Se modifica el orden del �tem actual.
        cambiarOrdenDeItemSubCategoria(idSubCategoria, idItemSubCategoria, orden - 1);
    }
    
    /**
     * M�todo que consulta en base de datos el ID de un �tem dado un
     * numero de orden.
     *
     * @param idItem Identificador del �tem.
     * @param orden N�mero de orden por el cual se filtrar� la busqueda.
     * 
     * @return El ID del �tem.
     */
    public long getIdItemSubCategoriaPorOrden(long idItem, int orden) {
        // Se agregan los datos del registro (nombreColumna/Valor).
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("orden", orden);
        map.addValue("subcat", idItem);
        // Se arma la sentencia de base de datos.
        SqlRowSet sqlRowSet = springDbMgr.executeQuery("SELECT * FROM itemsubcategoria WHERE orden = :orden AND subcategoria_id = :subcat", map);
        // Si existe al menos una categoria retorna el n�mero.
        if (sqlRowSet.next()) {
            return (sqlRowSet.getLong("id"));
        } else {
            // Si no existen �tem retorna el 0.
            return 0;
        }
    }
    
    /**
     * M�todo que actualiza el orden del �tem.
     *
     * @param idSubCategoria Identificador de la subcategoria.
     * @param idItemSubCategoria Identificador del �tem.
     * @param orden N�mero para actualizar el �tem.
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
     * M�todo que v�lida si el nombre del �tem se puede registrar.
     *
     * @param id Identificador del �tem.
     * @param nombre Nombre del �tem a registrar.
     * 
     * @return Retorna true si es v�lido el nombre del �tem, si no retorna false.
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
            // Si no es v�lido retorna el false.
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * M�todo que registra un �tem en base de datos.
     *
     * @param itemSubCategoria Objeto con todos los datos del �tem a registrar.
     * 
     * @return El resultado de la acci�n.
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
            return "Error al registrar el �tem de subcategoria.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Registro exitoso"
                : "El �tem de subcategoria no se puede registrar, revise que se este ingresando informaci�n v�lida.";

    }
    
    /**
     * M�todo que consulta en base de datos la informaci�n de un �tem dado.
     *
     * @param idItemSubCategoria Identificador del �tem.
     * 
     * @return La informaci�n de un �tem en un objeto.
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
            // Se almacenan los datos del �tem.
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
        // Se retorna el �tem desde base de datos.
        return itemSubCategoria;
    }
    
    /**
     * M�todo que v�lida si el nombre del �tem se puede actualizar.
     *
     * @param id Identificador de la subcategoria.
     * @param item Identificador del �tem.
     * @param nombre Nombre del �tem a registrar.
     * 
     * @return Retorna true si es v�lido el nombre, si no retorna false.
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
            // Si es v�lido retorna false.
            return false;
        } else {
            // Si es v�lido retorna true.
            return true;
        }
    }
    
    /**
     * M�todo que edita en base de datos la informaci�n de un �tem dado.
     *
     * @param itemsubcategoria �Item a editar.
     * 
     * @return El resultado de la acci�n.
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
            return "Error al actualizar el �tem de subcategoria.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Actualizacion exitosa"
                : "El �tem de subcategoria no se puede actualizar, revise que se este ingresando informaci�n v�lida.";
    }
    
    /**
     * M�todo que elimina en base de datos la informaci�n de un �tem dado.
     *
     * @param itemSubCategoria Item a editar.
     * 
     * @return El resultado de la acci�n.
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
            return "Error al eliminar el �tem de subcategoria.";
        }
        // Si hubieron filas afectadas es por que si hubo registro, en caso contrario muestra el mensaje de error.
        return (result == 1) ? "Eliminacion exitosa"
                : "El �tem de subcategoria tiene contenido asociado. Debe eliminar el contenido y las subcategorias asociadas a este �tem para poder realizar la eliminaci�n.";
    }
}
