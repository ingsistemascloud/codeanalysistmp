package co.ufps.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import co.ufps.edu.dao.CategoriaDao;
import co.ufps.edu.dto.Categoria;
import javax.servlet.http.HttpServletRequest;

/**
 * Controlador de categorias. Las categorias son las llamadas pesta�as en el
 * sitio web. 
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
public class CategoriaController {

    private CategoriaDao categoriaDao;
    private AdminController adminController;

    /**
     * Constructor de la clase en donde se inicializan las variables.
     */
    public CategoriaController() throws Exception {
        categoriaDao = new CategoriaDao();
        this.adminController = new AdminController();
    }

    /**
     * M�todo que retorna una p�gina con todas las categorias en el sistema.
     *
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina principal de categorias.
     */
    @GetMapping("/categorias") // Base
    public String index(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se cargan las categorias para poder mostrarlas en el cuadro.
        model.addAttribute("categorias", categoriaDao.getCategorias());
        return "Administrador/Categoria/Categorias"; // Nombre del archivo jsp
    }

    /**
     * Modelo con el que se realizara el formulario.
     *
     * @return Un objeto para ser llenado desde el archivo .JSP
     */
    @ModelAttribute("categoria")
    public Categoria setUpUserForm() {
        return new Categoria();
    }

    /**
     * M�todo que retorna una p�gina para realizar el registro de una categoria.
     *
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina de registro de categorias.
     */
    @GetMapping("/registrarCategoria") // Base
    public String registrarCategoria(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        return "Administrador/Categoria/RegistrarCategoria"; // Nombre del archivo jsp
    }

    /**
     * M�todo que permite guardar una categoria.
     *
     * @param categoria Objeto con la informaci�n a guardar.
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina a donde debe redireccionar despu�s de la acci�n.
     */
    @PostMapping(value = "/guardarCategoria")
    public String guardarCategoria(@ModelAttribute("categoria") Categoria categoria, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (categoria.isValidoParaRegistrar()) {
            // Se registra la informaci�n.
            String mensaje = categoriaDao.registrarCategoria(categoria);
            // Se valida si el registro en la BD fue exitoso.
            if (mensaje.equals("Registro exitoso")) {
                // Se muestra el mensaje de �xito.
                model.addAttribute("result", "Categor�a registrada con �xito.");
                // Se carga la informaci�n de las categorias para que la nueva informaci�n se refleje en la tabla.
                model.addAttribute("categorias", categoriaDao.getCategorias());
                return "Administrador/Categoria/Categorias"; // Nombre del archivo jsp
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", mensaje);
                return "Administrador/Categoria/RegistrarCategoria";
            }
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", "Debe llenar todos los campos.");
            return "Administrador/Categoria/RegistrarCategoria";
        }
    }

    /**
     * M�todo que permite bajar el n�mero de orden de una categor�a.
     *
     * @param idCategoria Idenrificador de la categor�a
     * @param orden N�mero de orden actual.
     * @param model Donde se cargaran las categor�as actualizadas.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return El redireccionamiento a la p�gina de categor�as.
     */
    @GetMapping(value = "/bajarOrdenCategoria")
    public String bajarOrdenDeCategoria(@RequestParam("id") long idCategoria,
            @RequestParam("orden") int orden, Model model, HttpServletRequest request) throws Exception {
        // Se cambia el orden.
        bajarOrden(idCategoria, orden);
        // Se cargan las categorias en model para que se refleje la informaci�n actualizada.
        model.addAttribute("categorias", categoriaDao.getCategorias());
        this.adminController.mostrarSuperAdmin(model, request);
        return "Administrador/Categoria/Categorias";
    }

    /**
     * M�todo que baja de orden una categor�a.
     *
     * @param idCategoria Identificador de la categor�a.
     * @param orden N�mero de orden actual.
     */
    private void bajarOrden(long idCategoria, int orden) {
        // Se consulta que el Id sea mayor a 0.
        if (idCategoria <= 0) {
            return;
        }
        // Se obtiene el n�mero menor de ordenamiento.
        int ordenMaximo = categoriaDao.getUltimoNumeroDeOrden();
        // Si el n�mero de orden es el m�ximo es por que ya es el ultimo y no se debe hacer nada.
        if (orden == ordenMaximo) {
            return;
        } else {
            // Se cambia el orden.
            categoriaDao.descenderOrden(idCategoria, orden);
        }
    }

    /**
     * M�todo que permite subir el n�mero de orden de una categor�a
     *
     * @param idCategoria Idenrificador de la categor�a.
     * @param orden N�mero de orden actual.
     * @param model Donde se cargaran las categor�as actualizadas.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return El redireccionamiento a la pagina de categorias
     */
    @GetMapping(value = "/subirOrdenCategoria")
    public String subirOrdenDeCategoria(@RequestParam("id") long idCategoria,
            @RequestParam("orden") int orden, Model model, HttpServletRequest request) throws Exception {
        // Se cambia el orden.
        subirOrden(idCategoria, orden);
        // Se cargan las categorias en model para que se refleje la informaci�n actualizada.
        model.addAttribute("categorias", categoriaDao.getCategorias());
        this.adminController.mostrarSuperAdmin(model, request);
        return "Administrador/Categoria/Categorias";
    }

    /**
     * M�todo que permite subir una categor�a de orden.
     *
     * @param idCategoria Identificador de la categor�a.
     * @param orden N�mero de orden actual.
     */
    private void subirOrden(long idCategoria, int orden) {
        // Se consulta que el Id sea mayor a 0.
        if (idCategoria <= 0) {
            return;
        }
        // Si el numero de orden es el minimo es por que ya es el primero y no se debe hacer nada.
        if (orden == 1) {
            return;
        } else {
            // Se cambia el orden.
            categoriaDao.ascenderOrden(idCategoria, orden);
        }
    }

    /**
     * M�todo que obtiene la p�gina de actualizar categor�a dado un ID.
     *
     * @param idCategoria Identificador de la categor�a.
     * @param model Objeto para enviar informaci�n a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina con la informaci�n de la categor�a cargada.
     */
    @GetMapping(value = "/actualizarCategoria")
    public String actualizarCategoria(@RequestParam("id") long idCategoria, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta que el Id sea mayor a 0.
        if (idCategoria <= 0) {
            // Se cargan las categorias para poder mostrarlas en el cuadro.
            model.addAttribute("categorias", categoriaDao.getCategorias());
            return "Administrador/Categoria/Categorias"; // Nombre del archivo jsp
        }
        // Se obtiene la informaci�n de la categor�a de acuerdo al id.
        Categoria categoria = categoriaDao.obtenerCategoriaPorId(idCategoria);
        // Se carga la informaci�n de la categor�a obtenida.
        model.addAttribute("categoria", categoria);
        return "Administrador/Categoria/ActualizarCategoria"; // Nombre del archivo jsp
    }

    /**
     * M�todo que permite editar una categor�a.
     *
     * @param categoria Objeto con la informaci�n a editar.
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina a donde debe redireccionar despu�s de la acci�n.
     */
    @PostMapping(value = "/editarCategoria")
    public String editarCategoria(@ModelAttribute("categoria") Categoria categoria, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (categoria.isValidoParaRegistrar()) {
            // Se edita la informaci�n.
            String mensaje = categoriaDao.editarCategoria(categoria);
            // Se valida si la informaci�n fue editada.
            if (mensaje.equals("Actualizacion exitosa")) {
                // Se muestra el mensaje de �xito.
                model.addAttribute("result", "Categor�a actualizada con �xito.");
                // Se carga la informaci�n de las categor�as para que se refleje la informaci�n actualizada.
                model.addAttribute("categorias", categoriaDao.getCategorias());
                return "Administrador/Categoria/Categorias"; // Nombre del archivo jsp
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", mensaje);
                return "Administrador/Categoria/ActualizarCategoria";
            }
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", "Debe llenar todos los campos.");
            return "Administrador/Categoria/ActualizarCategoria";
        }
    }

    /**
     * M�todo que obtiene la p�gina de eliminar categor�a dado un ID.
     *
     * @param idCategoria Identificador de la categor�a.
     * @param model Objeto para enviar informaci�n a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina con la informaci�n de la categor�a cargada.
     */
    @GetMapping(value = "/eliminarCategoria")
    public String eliminarCategoria(@RequestParam("id") long idCategoria, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta que el Id sea mayor a 0.
        if (idCategoria <= 0) {
            // Se cargan las categor�as para poder mostrarlas en el cuadro.
            model.addAttribute("categorias", categoriaDao.getCategorias());
            return "Administrador/Categoria/Categorias"; // Nombre del archivo jsp
        }
        // Se obtiene la informaci�n de la categor�a de acuerdo al id.
        Categoria categoria = categoriaDao.obtenerCategoriaPorId(idCategoria);
        // Se carga la informaci�n de la categor�a obtenida.
        model.addAttribute("categoria", categoria);
        return "Administrador/Categoria/EliminarCategoria"; // Nombre del archivo jsp
    }

    /**
     * M�todo que permite eliminar una categor�a.
     *
     * @param categoria Objeto con la informaci�n a eliminar.
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina a donde debe redireccionar despu�s de la acci�n.
     */
    @PostMapping(value = "/borrarCategoria")
    public String borrarCategoria(@ModelAttribute("categoria") Categoria categoria, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se elimina la informaci�n.
        String mensaje = categoriaDao.eliminarCategoria(categoria);
        // Se actualiza el orden de las categor�as.
        cambiarOrdenDeCategorias(categoria);
        // Se valida si la informaci�n fue eliminada.
        if (mensaje.equals("Eliminacion exitosa")) {
            // Se muestra el mensaje de �xito.
            model.addAttribute("result", "Categor�a eliminada con �xito.");
            // Se carga la informaci�n de las categor�as para que se refleje la informaci�n actualizada.
            model.addAttribute("categorias", categoriaDao.getCategorias());
            return "Administrador/Categoria/Categorias"; // Nombre del archivo jsp
        } else {
            model.addAttribute("categoria", categoriaDao.obtenerCategoriaPorId(categoria.getId()));
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", mensaje);
            return "Administrador/Categoria/EliminarCategoria";
        }

    }

    /**
     * M�todo que reordena todas las categor�as dado un orden faltante.
     *
     * @param categoria Objeto con la informaci�n de la categor�a borrada.
     */
    private void cambiarOrdenDeCategorias(Categoria categoria) {
        // Se obtiene el menor n�mero de ordenamiento.
        int ordenMaximo = categoriaDao.getUltimoNumeroDeOrden();
        // Se realiza el reordenamiento.
        for (int i = categoria.getOrden(); i < ordenMaximo; i++) {
            long idCategoria = categoriaDao.getIdCategoriaPorOrden(i + 1);
            categoriaDao.cambiarOrdenDeCategoria(idCategoria, i);
        }
    }

}
