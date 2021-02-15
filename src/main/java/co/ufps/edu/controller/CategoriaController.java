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
 * Controlador de categorias. Las categorias son las llamadas pestañas en el
 * sitio web. 
 * 
 * Todos los servicios publicados en esta clase serán expuestos para ser 
 * consumidos por los archivos JSP.
 *
 * La etiqueta @Controller escanea todos los servicios para publicarlos según el
 * tipo de método HTTP.
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
     * Método que retorna una página con todas las categorias en el sistema.
     *
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página principal de categorias.
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
     * Método que retorna una página para realizar el registro de una categoria.
     *
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página de registro de categorias.
     */
    @GetMapping("/registrarCategoria") // Base
    public String registrarCategoria(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        return "Administrador/Categoria/RegistrarCategoria"; // Nombre del archivo jsp
    }

    /**
     * Método que permite guardar una categoria.
     *
     * @param categoria Objeto con la información a guardar.
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/guardarCategoria")
    public String guardarCategoria(@ModelAttribute("categoria") Categoria categoria, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (categoria.isValidoParaRegistrar()) {
            // Se registra la información.
            String mensaje = categoriaDao.registrarCategoria(categoria);
            // Se valida si el registro en la BD fue exitoso.
            if (mensaje.equals("Registro exitoso")) {
                // Se muestra el mensaje de éxito.
                model.addAttribute("result", "Categoría registrada con éxito.");
                // Se carga la información de las categorias para que la nueva información se refleje en la tabla.
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
     * Método que permite bajar el número de orden de una categoría.
     *
     * @param idCategoria Idenrificador de la categoría
     * @param orden Número de orden actual.
     * @param model Donde se cargaran las categorías actualizadas.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return El redireccionamiento a la página de categorías.
     */
    @GetMapping(value = "/bajarOrdenCategoria")
    public String bajarOrdenDeCategoria(@RequestParam("id") long idCategoria,
            @RequestParam("orden") int orden, Model model, HttpServletRequest request) throws Exception {
        // Se cambia el orden.
        bajarOrden(idCategoria, orden);
        // Se cargan las categorias en model para que se refleje la información actualizada.
        model.addAttribute("categorias", categoriaDao.getCategorias());
        this.adminController.mostrarSuperAdmin(model, request);
        return "Administrador/Categoria/Categorias";
    }

    /**
     * Método que baja de orden una categoría.
     *
     * @param idCategoria Identificador de la categoría.
     * @param orden Número de orden actual.
     */
    private void bajarOrden(long idCategoria, int orden) {
        // Se consulta que el Id sea mayor a 0.
        if (idCategoria <= 0) {
            return;
        }
        // Se obtiene el número menor de ordenamiento.
        int ordenMaximo = categoriaDao.getUltimoNumeroDeOrden();
        // Si el número de orden es el máximo es por que ya es el ultimo y no se debe hacer nada.
        if (orden == ordenMaximo) {
            return;
        } else {
            // Se cambia el orden.
            categoriaDao.descenderOrden(idCategoria, orden);
        }
    }

    /**
     * Método que permite subir el número de orden de una categoría
     *
     * @param idCategoria Idenrificador de la categoría.
     * @param orden Número de orden actual.
     * @param model Donde se cargaran las categorías actualizadas.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return El redireccionamiento a la pagina de categorias
     */
    @GetMapping(value = "/subirOrdenCategoria")
    public String subirOrdenDeCategoria(@RequestParam("id") long idCategoria,
            @RequestParam("orden") int orden, Model model, HttpServletRequest request) throws Exception {
        // Se cambia el orden.
        subirOrden(idCategoria, orden);
        // Se cargan las categorias en model para que se refleje la información actualizada.
        model.addAttribute("categorias", categoriaDao.getCategorias());
        this.adminController.mostrarSuperAdmin(model, request);
        return "Administrador/Categoria/Categorias";
    }

    /**
     * Método que permite subir una categoría de orden.
     *
     * @param idCategoria Identificador de la categoría.
     * @param orden Número de orden actual.
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
     * Método que obtiene la página de actualizar categoría dado un ID.
     *
     * @param idCategoria Identificador de la categoría.
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página con la información de la categoría cargada.
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
        // Se obtiene la información de la categoría de acuerdo al id.
        Categoria categoria = categoriaDao.obtenerCategoriaPorId(idCategoria);
        // Se carga la información de la categoría obtenida.
        model.addAttribute("categoria", categoria);
        return "Administrador/Categoria/ActualizarCategoria"; // Nombre del archivo jsp
    }

    /**
     * Método que permite editar una categoría.
     *
     * @param categoria Objeto con la información a editar.
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/editarCategoria")
    public String editarCategoria(@ModelAttribute("categoria") Categoria categoria, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (categoria.isValidoParaRegistrar()) {
            // Se edita la información.
            String mensaje = categoriaDao.editarCategoria(categoria);
            // Se valida si la información fue editada.
            if (mensaje.equals("Actualizacion exitosa")) {
                // Se muestra el mensaje de éxito.
                model.addAttribute("result", "Categoría actualizada con éxito.");
                // Se carga la información de las categorías para que se refleje la información actualizada.
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
     * Método que obtiene la página de eliminar categoría dado un ID.
     *
     * @param idCategoria Identificador de la categoría.
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página con la información de la categoría cargada.
     */
    @GetMapping(value = "/eliminarCategoria")
    public String eliminarCategoria(@RequestParam("id") long idCategoria, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta que el Id sea mayor a 0.
        if (idCategoria <= 0) {
            // Se cargan las categorías para poder mostrarlas en el cuadro.
            model.addAttribute("categorias", categoriaDao.getCategorias());
            return "Administrador/Categoria/Categorias"; // Nombre del archivo jsp
        }
        // Se obtiene la información de la categoría de acuerdo al id.
        Categoria categoria = categoriaDao.obtenerCategoriaPorId(idCategoria);
        // Se carga la información de la categoría obtenida.
        model.addAttribute("categoria", categoria);
        return "Administrador/Categoria/EliminarCategoria"; // Nombre del archivo jsp
    }

    /**
     * Método que permite eliminar una categoría.
     *
     * @param categoria Objeto con la información a eliminar.
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/borrarCategoria")
    public String borrarCategoria(@ModelAttribute("categoria") Categoria categoria, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se elimina la información.
        String mensaje = categoriaDao.eliminarCategoria(categoria);
        // Se actualiza el orden de las categorías.
        cambiarOrdenDeCategorias(categoria);
        // Se valida si la información fue eliminada.
        if (mensaje.equals("Eliminacion exitosa")) {
            // Se muestra el mensaje de éxito.
            model.addAttribute("result", "Categoría eliminada con éxito.");
            // Se carga la información de las categorías para que se refleje la información actualizada.
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
     * Método que reordena todas las categorías dado un orden faltante.
     *
     * @param categoria Objeto con la información de la categoría borrada.
     */
    private void cambiarOrdenDeCategorias(Categoria categoria) {
        // Se obtiene el menor número de ordenamiento.
        int ordenMaximo = categoriaDao.getUltimoNumeroDeOrden();
        // Se realiza el reordenamiento.
        for (int i = categoria.getOrden(); i < ordenMaximo; i++) {
            long idCategoria = categoriaDao.getIdCategoriaPorOrden(i + 1);
            categoriaDao.cambiarOrdenDeCategoria(idCategoria, i);
        }
    }

}
