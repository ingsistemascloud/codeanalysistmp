package co.ufps.edu.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import co.ufps.edu.constantes.Constantes;
import co.ufps.edu.dao.ContenidoDao;
import co.ufps.edu.dao.ItemSubCategoriaDao;
import co.ufps.edu.dao.SubCategoriaDao;
import co.ufps.edu.dto.ItemSubCategoria;
import javax.servlet.http.HttpServletRequest;

/**
 * Controlador de �tem de subcategorias. Los �tem de las subcategorias son las llamadas pesta�as
 * hijas de las subcategorias en el sitio web.
 * 
 * Todos los servicios publicados en esta clase ser�n expuestos para ser 
 * consumidos por los archivos JSP.
 *
 * La etiqueta @Controller escanea todos los servicios para publicarlos seg�n el
 * tipo de m�todo HTTP.
 * 
 * @author UFPS
 */
@Controller
public class ItemSubCategoriaController {

    private ItemSubCategoriaDao itemSubCategoriaDao;
    private SubCategoriaDao subcategoriaDao;
    private AdminController adminController;
    @Autowired
    private ContenidoDao contenidoDao;

    /**
     * Constructor de la clase en donde se inicializan las variables
     */
    public ItemSubCategoriaController() throws Exception {
        itemSubCategoriaDao = new ItemSubCategoriaDao();
        subcategoriaDao = new SubCategoriaDao();
        this.adminController = new AdminController();
    }
    
    /**
     * M�todo que permite bajar el numero de orden del �tem.
     *
     * @param idSubCategoria Idenrificador de la subcategoria.
     * @param idItemSubCategoria Idenrificador del item.
     * @param orden N�mero de orden actual.
     * @param model Donde se cargaran las categor�as actualizadas.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return El redireccionamiento a la p�gina del item.
     */
    @GetMapping(value = "/bajarOrdenItemSubCategoria")
    public String bajarOrdenDeItemSubCategoria(@RequestParam("idSubCat") long idSubCategoria,
            @RequestParam("id") long idItemSubCategoria, @RequestParam("orden") int orden, Model model, HttpServletRequest request) throws Exception {
        // Se cambia el orden.
        bajarOrden(idSubCategoria, idItemSubCategoria, orden);
        // Se cargan loss items en model para que se refleje la informaci�n actualizada.
        model.addAttribute("itemsubcategorias", itemSubCategoriaDao.getItemSubCategorias());
        this.adminController.mostrarSuperAdmin(model, request);
        return "Administrador/ItemSubCategoria/ItemSubCategorias"; // Nombre del archivo jsp
    }
    
    /**
     * M�todo que baja de orden el �tem.
     *
     * @param idSubCategoria Id de la subcategoria.
     * @param idItemSubCategoria Id del item.
     * @param orden N�mero de orden actual.
     */
    public void bajarOrden(long idSubCategoria, long idItemSubCategoria, int orden) {
        // Se consulta que el Id sea mayor a 0.
        if (idSubCategoria <= 0 || idItemSubCategoria <= 0) {
            return;
        }
        // Se obtiene el n�mero menor de ordenamiento.
        int ordenMaximo = itemSubCategoriaDao.getUltimoNumeroDeOrden(idSubCategoria);
        // Si el n�mero de orden es el m�ximo es por que ya es el ultimo y no se debe hacer nada.
        if (orden == ordenMaximo) {
            return;
        } else {
            // Se cambia el orden.
            itemSubCategoriaDao.descenderOrden(idSubCategoria, idItemSubCategoria, orden);
        }
    }
    
    /**
     * M�todo que permite subir el n�mero de orden de un �tem.
     *
     * @param idSubCategoria Idenrificador de la subcategoria.
     * @param idItemSubCategoria Idenrificador del item. 
     * @param orden N�mero de orden actual.
     * @param model Donde se cargaran los �tem actualizados.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return El redireccionamiento a la p�gina de �tem.
     */
    @GetMapping(value = "/subirOrdenItemSubCategoria")
    public String subirOrdenItemSubCategoria(@RequestParam("idSubCat") long idSubCategoria,
            @RequestParam("id") long idItemSubCategoria, @RequestParam("orden") int orden, Model model, HttpServletRequest request) throws Exception {
        // Se cambia el orden.
        subirOrden(idSubCategoria, idItemSubCategoria, orden);
        // Se cargan loss �tems en model para que se refleje la informaci�n actualizada.
        model.addAttribute("itemsubcategorias", itemSubCategoriaDao.getItemSubCategorias());
        this.adminController.mostrarSuperAdmin(model, request);
        return "Administrador/ItemSubCategoria/ItemSubCategorias"; // Nombre del archivo jsp
    }
    
    /**
     * Metodo que permite subir una categoria de orden
     *
     * @param idSubCategoria Identificador de la subcategoria.
     * @param idItemSubCategoria Identificador del item.
     * @param orden N�mero de orden actual.
     */
    private void subirOrden(long idSubCategoria, long idItemSubCategoria, int orden) {
        // Se consulta que el Id sea mayor a 0.
        if (idSubCategoria <= 0 || idItemSubCategoria <= 0) {
            return;
        }
        // Si el numero de orden es el minimo es por que ya es el primero y no se debe hacer nada.
        if (orden == 1) {
            return;
        } else {
            // Se cambia el orden.
            itemSubCategoriaDao.ascenderOrden(idSubCategoria, idItemSubCategoria, orden);
        }
    }
    
    /**
     * M�todo que retorna una p�gina para realizar el registro de un �tem.
     * 
     * @param model Objeto para enviar informaci�n a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina de registro de �tem.
     */
    @GetMapping("/RegistrarItemSubCategoria") // Base
    public String registrarItemSubCategoria(Model model, HttpServletRequest request) throws Exception {
        model.addAttribute("subcategorias", subcategoriaDao.getMapaDeSubCategorias());
        this.adminController.mostrarSuperAdmin(model, request);
        return "Administrador/ItemSubCategoria/RegistrarItemSubCategoria"; // Nombre del archivo jsp
    }
    
    /**
     * M�todo que permite guardar un �tem.
     *
     * @param itemSubCategoria Objeto con la informaci�n a guardar
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina a donde debe redireccionar despu�s de la acci�n.
     */
    @PostMapping(value = "/guardarItemSubCategoria")
    public String guardarItemSubCategoria(@ModelAttribute("itemsubcategoria") ItemSubCategoria itemSubCategoria,
            Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (itemSubCategoria.isValidoParaRegistrar()) {
            // Se valida si el nombre es valido para registrar.
            if (itemSubCategoriaDao.esNombreValido(itemSubCategoria.getSubcategoria().getId(),itemSubCategoria.getNombre())) {
                // Se registra la informaci�n.
                String mensaje = itemSubCategoriaDao.registrarItemSubCategoria(itemSubCategoria);
                // Se valida si el registro en la BD fue exitoso.
                if (mensaje.equals("Registro exitoso")) {
                    // Se muestra el mensaje de �xito.
                    model.addAttribute("result", "El item de subcategoria se registr� con �xito.");
                    // Se carga la informaci�n de los �tems para que la nueva informaci�n se refleje en la tabla.
                    model.addAttribute("itemsubcategorias", itemSubCategoriaDao.getItemSubCategorias());
                    return "Administrador/ItemSubCategoria/ItemSubCategorias"; // Nombre del archivo jsp
                } else {
                    // Se muestra el mensaje de error.
                    model.addAttribute("wrong", mensaje);
                    // Se carga la informaci�n de las subcategorias para que la informaci�n se refleje en la tabla.
                    model.addAttribute("subcategorias", subcategoriaDao.getMapaDeSubCategorias());
                    return "Administrador/ItemSubCategoria/RegistrarItemSubCategoria"; // Nombre del archivo jsp
                }
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", "El nombre para este item de subcategoria ya se encuentra usado dentro de la categoria seleccionada.");
                // Se carga la informaci�n de las subcategorias para que la informaci�n se refleje en la tabla.
                model.addAttribute("subcategorias", subcategoriaDao.getMapaDeSubCategorias());
                return "Administrador/ItemSubCategoria/RegistrarItemSubCategoria"; // Nombre del archivo jsp
            }
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", "Debe llenar todos los campos.");
            // Se carga la informaci�n de las subcategorias para que la informaci�n se refleje en la tabla.
            model.addAttribute("subcategorias", subcategoriaDao.getMapaDeSubCategorias());
            return "Administrador/ItemSubCategoria/RegistrarItemSubCategoria"; // Nombre del archivo jsp
        }
    }
    
    /**
     * M�todo que obtiene la p�gina de actualizar �tem dado un ID.
     *
     * @param idItemSubCategoria Identificador de la subcategoria
     * @param model Objeto para enviar informaci�n a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina con la informaci�n del �tem cargada.
     */
    @GetMapping(value = "/actualizarItemSubCategoria")
    public String actualizarItemSubCategoria(@RequestParam("id") long idItemSubCategoria, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta que el Id sea mayor a 0.
        if (idItemSubCategoria <= 0) {
            // Se cargan los �tem para poder mostrarlos en el cuadro.
            model.addAttribute("itemsubcategorias", itemSubCategoriaDao.getItemSubCategorias());
            return "Administrador/ItemSubCategoria/ItemSubCategorias"; // Nombre del archivo jsp
        }
        // Se obtiene la informaci�n del �tem de acuerdo al id.
        ItemSubCategoria itemSubCategoria = itemSubCategoriaDao.obtenerItemSubCategoriaPorId(idItemSubCategoria);
        // Se carga la informaci�n del �tem obtenido.
        model.addAttribute("itemsubcategoria", itemSubCategoria);
        // Se carga el id de la subcategoria seleccionada para el �tem.
        model.addAttribute("idSubCategoriaSeleccionada", itemSubCategoria.getSubcategoria().getId());
        // Se carga el nobmre de la subcategoria seleccionada para el �tem.
        model.addAttribute("nombreSubCategoriaSeleccionada", itemSubCategoria.getSubcategoria().getNombre());
        Map<Long, String> subcategorias = subcategoriaDao.getMapaDeSubCategorias();
        subcategorias.remove(itemSubCategoria.getSubcategoria().getId());
        // Se carga la informaci�n de la subcategor�a obtenida.
        model.addAttribute("subcategorias", subcategorias);
        return "Administrador/ItemSubCategoria/ActualizarItemSubCategoria"; // Nombre del archivo jsp
    }
    
    /**
     * M�todo que permite editar una �tem.
     *
     * @param itemsubcategoria Objeto con la informaci�n a editar.
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina a donde debe redireccionar despu�s de la acci�n.
     */
    @PostMapping(value = "/editarItemSubCategoria")
    public String editarItemSubCategoria(@ModelAttribute("itemsubcategoria") ItemSubCategoria itemsubcategoria,
            Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (itemsubcategoria.isValidoParaRegistrar()) {
            String mensaje = "";
            // Se valida si se puede actualizar.
            if (itemSubCategoriaDao.esNombreValidoParaActualizar(itemsubcategoria.getSubcategoria().getId(), itemsubcategoria.getId(), itemsubcategoria.getNombre())) {
                // Se obtiene el id de subcategoria de acuerdo al id del �tem.
                long idSubCategoriaEnBaseDeDatos = itemSubCategoriaDao.obtenerItemSubCategoriaPorId(itemsubcategoria.getId()).getSubcategoria().getId();
                // Se valida que el id de la subcategoria a la que se va a asociar el �tem exista.
                if (itemsubcategoria.getSubcategoria().getId() == idSubCategoriaEnBaseDeDatos) {
                    // Se edita la informaci�n.
                    mensaje = itemSubCategoriaDao.editarItemSubCategoria(itemsubcategoria);
                } else {
                    // Se obtiene el id de la subcategoria correspondiente.
                    long idSubCategoriaActual = itemsubcategoria.getSubcategoria().getId();
                    // Se edita el id de la subcategoria correspondiente.
                    itemsubcategoria.getSubcategoria().setId(idSubCategoriaEnBaseDeDatos);
                    // Se elimina el �tem.
                    mensaje = itemSubCategoriaDao.eliminarItemSubCategoria(itemsubcategoria);
                    // Se ajusta el orden.
                    cambiarOrdenDeItemSubCategorias(itemsubcategoria);
                    // Se edita el id de la subcategoria correspondiente.
                    itemsubcategoria.getSubcategoria().setId(idSubCategoriaActual);
                    // Se registra la informaci�n.
                    itemSubCategoriaDao.registrarItemSubCategoria(itemsubcategoria);
                }
                // Se valida si la informaci�n fue editada.
                if (mensaje.equals("Actualizacion exitosa") || mensaje.equals("Eliminacion exitosa")) {
                    // Se muestra el mensaje de �xito.
                    model.addAttribute("result", "ItemSubCategoria actualizado con �xito.");
                    // Se carga la informaci�n del �tem  para que se refleje la informaci�n actualizada.
                    model.addAttribute("itemsubcategorias", itemSubCategoriaDao.getItemSubCategorias());
                    return "Administrador/ItemSubCategoria/ItemSubCategorias"; // Nombre del archivo jsp
                } else {
                    // Se muestra el mensaje de error.
                    model.addAttribute("wrong", mensaje);
                    return "Administrador/ItemSubCategoria/ActualizarItemSubCategoria";
                }
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", "El nombre para este item de subcategoria ya se encuentra usado dentro de la subcategoria seleccionada.");
                // Se obtiene la informaci�n del �tem de acuerdo al id.
                ItemSubCategoria itemsubCategoria = itemSubCategoriaDao.obtenerItemSubCategoriaPorId(itemsubcategoria.getId()); 
                // Se carga la informaci�n del �tem obtenido.
                model.addAttribute("itemsubcategoria", itemsubCategoria);
                // Se carga el id de la subcategoria seleccionada para el �tem.
                model.addAttribute("idSubCategoriaSeleccionada", itemsubCategoria.getSubcategoria().getId());
                // Se carga el nobmre de la subcategoria seleccionada para el �tem.
                model.addAttribute("nombreSubCategoriaSeleccionada", itemsubCategoria.getSubcategoria().getNombre());
                Map<Long, String> subcategorias = subcategoriaDao.getMapaDeSubCategorias();
                subcategorias.remove(itemsubCategoria.getSubcategoria().getId());
                // Se carga la informaci�n de la subcategor�a obtenida. 
                model.addAttribute("subcategorias", subcategorias);
                return "Administrador/ItemSubCategoria/ItemActualizarSubCategoria"; // Nombre del archivo jsp
            }
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", "Debe llenar todos los campos.");
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", "El nombre para este item de subcategoria ya se encuentra usado dentro de la subcategoria seleccionada.");
            // Se obtiene la informaci�n del �tem de acuerdo al id.
            ItemSubCategoria itemsubCategoria = itemSubCategoriaDao.obtenerItemSubCategoriaPorId(itemsubcategoria.getId());
            // Se carga la informaci�n del �tem obtenido.
            model.addAttribute("itemsubcategoria", itemsubCategoria);
            // Se carga el id de la subcategoria seleccionada para el �tem.
            model.addAttribute("idSubCategoriaSeleccionada", itemsubCategoria.getSubcategoria().getId());
            // Se carga el nobmre de la subcategoria seleccionada para el �tem.
            model.addAttribute("nombreSubCategoriaSeleccionada", itemsubCategoria.getSubcategoria().getNombre());
            Map<Long, String> subcategorias = subcategoriaDao.getMapaDeSubCategorias();
            subcategorias.remove(itemsubCategoria.getSubcategoria().getId());
            // Se carga la informaci�n de la subcategor�a obtenida.
            model.addAttribute("subcategorias", subcategorias);
            return "Administrador/ItemSubCategoria/ActualizarItemSubCategoria"; // Nombre del archivo jsp
        }
    }
    
    /**
     * M�todo que reordena todos los �tem dado un orden faltante.
     *
     * @param itemsubcategoria Objeto con la informaci�n del �tem borrado.
     */
    private void cambiarOrdenDeItemSubCategorias(ItemSubCategoria itemsubcategoria) {
        // Se obtiene el menor n�mero de ordenamiento.
        int ordenMaximo = itemSubCategoriaDao.getUltimoNumeroDeOrden(itemsubcategoria.getSubcategoria().getId());
        // Se realiza el reordenamiento. 
        for (int i = itemsubcategoria.getOrden(); i < ordenMaximo; i++) {
            long idItemSubCategoria = itemSubCategoriaDao.getIdItemSubCategoriaPorOrden(itemsubcategoria.getSubcategoria().getId(), i + 1);
            itemSubCategoriaDao.cambiarOrdenDeItemSubCategoria(itemsubcategoria.getSubcategoria().getId(), idItemSubCategoria, i);
        }
    }
    
    /**
     * Modelo con el que se realizara el formulario
     *
     * @return Un objeto para ser llenado desde el archivo .JSP
     */
    @ModelAttribute("itemsubcategoria")
    public ItemSubCategoria setUpUserForm() {
        return new ItemSubCategoria();
    }
    
    /**
     * M�todo que retorna una p�gina con todos los �tem en el sistema.
     *
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina principal del �tem.
     */
    @GetMapping("/itemsubcategorias") // Base
    public String index(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se cargan las categorias para poder mostrarlas en el cuadro.
        model.addAttribute("itemsubcategorias", itemSubCategoriaDao.getItemSubCategorias());
        return "Administrador/ItemSubCategoria/ItemSubCategorias"; // Nombre del archivo jsp
    }
    
    /**
     * M�todo que obtiene la p�gina de actualizar �tem dado un ID.
     *
     * @param idItemSubCategoria Identificador del �tem.
     * @param model Objeto para enviar informaci�n a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina con la informaci�n del �tem cargada.
     */
    @GetMapping(value = "/eliminarItemSubCategoria")
    public String eliminarItemSubCategoria(@RequestParam("id") long idItemSubCategoria, Model model, HttpServletRequest request) throws Exception {
        // Se consulta que el Id sea mayor a 0.
        this.adminController.mostrarSuperAdmin(model, request);
        if (idItemSubCategoria <= 0) {
            // Se cargan los �tem para poder mostrarlos en el cuadro.
            model.addAttribute("itemsubcategorias", itemSubCategoriaDao.getItemSubCategorias());
            return "Administrador/ItemSubCategoria/ItemSubCategorias"; // Nombre del archivo jsp
        }
        // Se obtiene la informaci�n del �tem de acuerdo al id.
        ItemSubCategoria itemsubcategoria = itemSubCategoriaDao.obtenerItemSubCategoriaPorId(idItemSubCategoria);
        // Se carga la informaci�n del �tem obtenida.
        model.addAttribute("itemsubcategoria", itemsubcategoria);
        return "Administrador/ItemSubCategoria/EliminarItemSubCategoria"; // Nombre del archivo jsp
    }
    
    /**
     * M�todo que permite eliminar un �tem.
     *
     * 
     * @param itemsubcategoria Idetificador del �tem.
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina a donde debe redireccionar despu�s de la acci�n.
     */
    @PostMapping(value = "/borrarItemSubCategoria")
    public String borrarItemSubCategoria(@ModelAttribute("itemsubcategoria") ItemSubCategoria itemsubcategoria,
            Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se valida si el �tem tiene contenido.
        if (!contenidoDao.tieneContenido(itemsubcategoria.getId(), Constantes.ITEMSUBCATEGORIA)) {
            // Se elimina la informaci�n.
            String mensaje = itemSubCategoriaDao.eliminarItemSubCategoria(itemsubcategoria);
            // Se actualiza el orden de las categor�as.
            cambiarOrdenDeItemSubCategorias(itemsubcategoria);
            // Se valida si la informaci�n fue eliminada.
            if (mensaje.equals("Eliminacion exitosa")) {
                // Se muestra el mensaje de �xito.
                model.addAttribute("result", "ItemSubcategoria eliminado con �xito.");
                // Se carga la informaci�n del �tem para que se refleje la informaci�n actualizada.
                model.addAttribute("itemsubcategorias", itemSubCategoriaDao.getItemSubCategorias());
                return "Administrador/ItemSubCategoria/ItemSubCategorias"; // Nombre del archivo jsp
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", mensaje);
                return "Administrador/ItemSubCategoria/EliminarItemSubCategoria";
            }
        } else {
            // Se muestra el mensaje de error. 
            model.addAttribute("wrong", "No es posible eliminar el item de subcategoria debido a que tiene un contenido registrado.");
            return "Administrador/ItemSubCategoria/EliminarItemSubCategoria";
        }
    }
}
