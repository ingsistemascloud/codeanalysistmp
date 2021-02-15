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
import co.ufps.edu.dao.CategoriaDao;
import co.ufps.edu.dao.ContenidoDao;
import co.ufps.edu.dao.SubCategoriaDao;
import co.ufps.edu.dto.SubCategoria;
import javax.servlet.http.HttpServletRequest;

/**
 * Controlador de subcategorias. Las subcategorias son las llamadas pestañas
 * hijas de las categorias en el sitio web. 
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
public class SubCategoriaController {

    private SubCategoriaDao subCategoriaDao;
    private CategoriaDao categoriaDao;
    private AdminController adminController;
    @Autowired
    private ContenidoDao contenidoDao;

    /**
     * Constructor de la clase en donde se inicializan las variables
     */
    public SubCategoriaController() throws Exception {
        subCategoriaDao = new SubCategoriaDao();
        categoriaDao = new CategoriaDao();
        this.adminController = new AdminController();
    }

    /**
     * Método que retorna una página con todas las subcategorias en el sistema.
     *
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página principal de categorias.
     */
    @GetMapping("/subcategorias") // Base
    public String index(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se cargan las subcategorias para poder mostrarlas en el cuadro.
        model.addAttribute("subcategorias", subCategoriaDao.getSubCategorias());
        return "Administrador/SubCategoria/SubCategorias"; // Nombre del archivo jsp
    }

    /**
     * Modelo con el que se realizara el formulario.
     *
     * @return Un objeto para ser llenado desde el archivo .JSP
     */
    @ModelAttribute("subcategoria")
    public SubCategoria setUpUserForm() {
        return new SubCategoria();
    }

    /**
     * Método que retorna una página para realizar el registro de una subcategoria.
     * 
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página de registro de subcategorias.
     */
    @GetMapping("/registrarSubCategoria") // Base
    public String registrarSubCategoria(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        model.addAttribute("categorias", categoriaDao.getMapaDeCategorias());
        return "Administrador/SubCategoria/RegistrarSubCategoria"; // Nombre del archivo jsp
    }

    /**
     * Método que permite guardar una subcategoria.
     *
     * @param subCategoria Objeto con la información a guardar.
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/guardarSubCategoria")
    public String guardarSubCategoria(@ModelAttribute("subcategoria") SubCategoria subCategoria,
            Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (subCategoria.isValidoParaRegistrar()) {
            // Se valida si el nombre es valido para registrar.
            if (subCategoriaDao.esNombreValido(subCategoria.getCategoria().getId(),
                    subCategoria.getNombre())) {
                // Se registra la información.
                String mensaje = subCategoriaDao.registrarSubCategoria(subCategoria);
                // Se valida si el registro en la BD fue exitoso.
                if (mensaje.equals("Registro exitoso")) {
                    // Se muestra el mensaje de éxito.
                    model.addAttribute("result", "Subcategoria registrada con éxito.");
                    // Se carga la información de las subcategorias para que la nueva información se refleje en la tabla.
                    model.addAttribute("subcategorias", subCategoriaDao.getSubCategorias());
                    return "Administrador/SubCategoria/SubCategorias"; // Nombre del archivo jsp
                } else {
                    // Se muestra el mensaje de error.
                    model.addAttribute("wrong", mensaje);
                    // Se carga la información de las categorias para que la información se refleje en la tabla.
                    model.addAttribute("categorias", categoriaDao.getMapaDeCategorias());
                    return "Administrador/SubCategoria/RegistrarSubCategoria"; // Nombre del archivo jsp
                }
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong",
                        "El nombre para esta subcategoria ya se encuentra usado dentro de la categoría seleccionada.");
                // Se carga la información de las categorias para que la información se refleje en la tabla.
                model.addAttribute("categorias", categoriaDao.getMapaDeCategorias());
                return "Administrador/SubCategoria/RegistrarSubCategoria"; // Nombre del archivo jsp
            }

        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", "Debe llenar todos los campos.");
            // Se carga la información de las categorias para que la información se refleje en la tabla.
            model.addAttribute("categorias", categoriaDao.getMapaDeCategorias());
            return "Administrador/SubCategoria/RegistrarSubCategoria"; // Nombre del archivo jsp
        }
    }

    /**
     * Método que permite bajar el número de orden de una subcategoria.
     *
     * @param idCategoria Idenrificador de la categoria.
     * @param orden Numero de orden actual.
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return El redireccionamiento a la página de subcategorias
     */
    @GetMapping(value = "/bajarOrdenSubCategoria")
    public String bajarOrdenSubCategoria(@RequestParam("idCat") long idCategoria,
            @RequestParam("id") long idSubCategoria, @RequestParam("orden") int orden, Model model, HttpServletRequest request) throws Exception {
        // Se cambia el orden.
        bajarOrden(idCategoria, idSubCategoria, orden);
        this.adminController.mostrarSuperAdmin(model, request);
        // Se cargan las subcategorias en model para que se refleje la información actualizada.
        model.addAttribute("subcategorias", subCategoriaDao.getSubCategorias());
        return "Administrador/SubCategoria/SubCategorias"; // Nombre del archivo jsp
    }

    /**
     * Método que baja de orden una subcategoria.
     *
     * @param idCategoria Id de la categoria.
     * @param idSubCategoria Id de la subcategoria.
     * @param orden Número de ordenamiento.
     */
    private void bajarOrden(long idCategoria, long idSubCategoria, int orden) {
        // Se consulta que el Id sea mayor a 0.
        if (idCategoria <= 0 || idSubCategoria <= 0) {
            return;
        }
        // Se obtiene el número menor de ordenamiento.
        int ordenMaximo = subCategoriaDao.getUltimoNumeroDeOrden(idCategoria);
        // Si el número de orden es el máximo es por que ya es el ultimo y no se debe hacer nada.
        if (orden == ordenMaximo) {
            return;
        } else {
            // Se cambia el orden.
            subCategoriaDao.descenderOrden(idCategoria, idSubCategoria, orden);
        }
    }

    /**
     * Método que permite subir el número de orden de una subcategoria
     *
     * @param idCategoria Idenrificador de la categoria.
     * @param idSubCategoria Idenrificador de la subcategoria.
     * @param orden Número de orden actual.
     * @param model Donde se cargaran las subcategorias actualizadas.
     * 
     * @return El redireccionamiento a la página de subcategorias.
     */
    @GetMapping(value = "/subirOrdenSubCategoria")
    public String subirOrdenDeSubCategoria(@RequestParam("idCat") long idCategoria,
            @RequestParam("id") long idSubCategoria, @RequestParam("orden") int orden, Model model, HttpServletRequest request) throws Exception {
        // Se cambia el orden.
        subirOrden(idCategoria, idSubCategoria, orden);
        // Se cargan las subcategorias en model para que se refleje la información actualizada.
        model.addAttribute("subcategorias", subCategoriaDao.getSubCategorias());
        this.adminController.mostrarSuperAdmin(model, request);
        return "Administrador/SubCategoria/SubCategorias"; // Nombre del archivo jsp
    }

    /**
     * Método que permite subir una subcategoria de orden
     *
     * @param idCategoria Identificador de la categoria.
     * @param idSubCategoria
     * @param orden Orden de la categoria.
     */
    private void subirOrden(long idCategoria, long idSubCategoria, int orden) {
        // Se consulta que el Id sea mayor a 0.
        if (idCategoria <= 0 || idSubCategoria <= 0) {
            return;
        }
        // Si el numero de orden es el minimo es por que ya es el primero y no se debe hacer nada.
        if (orden == 1) {
            return;
        } else {
            // Se cambia el orden.
            subCategoriaDao.ascenderOrden(idCategoria, idSubCategoria, orden);
        }
    }

    /**
     * Método que obtiene la página de actualizar subcategoria dado un ID.
     *
     * @param idSubCategoria Identificador de la subcategoria
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página con la información de la subcategoria cargada.
     */
    @GetMapping(value = "/actualizarSubCategoria")
    public String actualizarSubCategoria(@RequestParam("id") long idSubCategoria, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta que el Id sea mayor a 0.
        if (idSubCategoria <= 0) {
            // Se cargan las subcategorias para poder mostrarlos en el cuadro.
            model.addAttribute("subcategorias", subCategoriaDao.getSubCategorias());
            return "Administrador/SubCategoria/SubCategorias"; // Nombre del archivo jsp
        }
        // Se obtiene la información de la subcategoria de acuerdo al id.
        SubCategoria subCategoria = subCategoriaDao.obtenerSubCategoriaPorId(idSubCategoria);
        // Se carga la información de la subcategoria obtenido.
        model.addAttribute("subcategoria", subCategoria);
        // Se carga el id de la categoria seleccionada para el ítem.
        model.addAttribute("idCategoriaSeleccionada", subCategoria.getCategoria().getId());
        // Se carga el nobmre de la categoria seleccionada para el ítem.
        model.addAttribute("nombreCategoriaSeleccionada", subCategoria.getCategoria().getNombre());
        Map<Long, String> categorias = categoriaDao.getMapaDeCategorias();
        categorias.remove(subCategoria.getCategoria().getId());
        // Se carga la información de la categorias obtenida.
        model.addAttribute("categorias", categorias);
        return "Administrador/SubCategoria/ActualizarSubCategoria"; // Nombre del archivo jsp
    }

    /**
     * Método que permite editar una subcategoria.
     *
     * @param subcategoria Objeto con la información a editar.
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/editarSubCategoria")
    public String editarSubCategoria(@ModelAttribute("subcategoria") SubCategoria subcategoria,
            Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (subcategoria.isValidoParaRegistrar()) {
            String mensaje = "";
            // Se valida si se puede actualizar.
            if (subCategoriaDao.esNombreValidoParaActualizar(subcategoria.getCategoria().getId(), subcategoria.getId(), subcategoria.getNombre())) {
                // Se obtiene el id de categoria de acuerdo al id de la subcategoria.
                long idCategoriaEnBaseDeDatos = subCategoriaDao.obtenerSubCategoriaPorId(subcategoria.getId()).getCategoria().getId();
                // Se valida que el id de la categoria a la que se va a asociar la subcategoria exista.
                if (subcategoria.getCategoria().getId() == idCategoriaEnBaseDeDatos) {
                    // Se edita la información.
                    mensaje = subCategoriaDao.editarSubCategoria(subcategoria);
                } else {
                    // Se obtiene el id de la categoria correspondiente.
                    long idCategoriaActual = subcategoria.getCategoria().getId();
                    // Se edita el id de la categoria correspondiente.
                    subcategoria.getCategoria().setId(idCategoriaEnBaseDeDatos);
                    // Se elimina la subcategoria.
                    mensaje = subCategoriaDao.eliminarSubCategoria(subcategoria);
                    // Se ajusta el orden.
                    cambiarOrdenDeSubCategorias(subcategoria);
                    // Se edita el id de la categoria correspondiente.
                    subcategoria.getCategoria().setId(idCategoriaActual);
                    // Se registra la información.
                    subCategoriaDao.registrarSubCategoria(subcategoria);
                }
                // Se valida si la información fue editada.
                if (mensaje.equals("Actualizacion exitosa") || mensaje.equals("Eliminacion exitosa")) {
                    // Se muestra el mensaje de éxito.
                    model.addAttribute("result", "Subcategoria actualizada con éxito.");
                    // Se carga la información de la subcategoria  para que se refleje la información actualizada.
                    model.addAttribute("subcategorias", subCategoriaDao.getSubCategorias());
                    return "Administrador/SubCategoria/SubCategorias"; // Nombre del archivo jsp
                } else {
                    // Se muestra el mensaje de error.
                    model.addAttribute("wrong", mensaje);
                    return "Administrador/SubCategoria/ActualizarSubCategoria";
                }
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong",
                        "El nombre para esta subcategoria ya se encuentra usado dentro de la categoría seleccionada.");
                // Se obtiene la información de la subcategoria de acuerdo al id.
                SubCategoria subCategoria = subCategoriaDao.obtenerSubCategoriaPorId(subcategoria.getId());
                // Se carga la información de la subcategoria obtenida.
                model.addAttribute("subcategoria", subCategoria);
                // Se carga el id de la categoria seleccionada para el ítem.
                model.addAttribute("idCategoriaSeleccionada", subCategoria.getCategoria().getId());
                // Se carga el nobmre de la categoria seleccionada para el ítem.
                model.addAttribute("nombreCategoriaSeleccionada", subCategoria.getCategoria().getNombre());
                Map<Long, String> categorias = categoriaDao.getMapaDeCategorias();
                categorias.remove(subCategoria.getCategoria().getId());
                // Se carga la información de la categoría obtenida. 
                model.addAttribute("categorias", categorias);
                return "Administrador/SubCategoria/ActualizarSubCategoria"; // Nombre del archivo jsp
            }
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", "Debe llenar todos los campos.");
            // Se muestra el mensaje de error.
            model.addAttribute("wrong",
                    "El nombre para esta subcategoria ya se encuentra usado dentro de la categoria seleccionada.");
            // Se obtiene la información de la subcategoria de acuerdo al id.
            SubCategoria subCategoria = subCategoriaDao.obtenerSubCategoriaPorId(subcategoria.getId());
            // Se carga la información de la subcategoria obtenida.
            model.addAttribute("subcategoria", subCategoria);
            // Se carga el id de la categoria seleccionada para la subcategoria.
            model.addAttribute("idCategoriaSeleccionada", subCategoria.getCategoria().getId());
            // Se carga el nobmre de la categoria seleccionada para la subcategoria.
            model.addAttribute("nombreCategoriaSeleccionada", subCategoria.getCategoria().getNombre());
            Map<Long, String> categorias = categoriaDao.getMapaDeCategorias();
            categorias.remove(subCategoria.getCategoria().getId());
            // Se carga la información de la categorias obtenida.
            model.addAttribute("categorias", categorias);
            return "Administrador/SubCategoria/ActualizarSubCategoria"; // Nombre del archivo jsp
        }
    }

    /**
     * Método que obtiene la página de actualizar subcategoria dado un ID.
     *
     * @param idSubCategoria Identificador de la categoria
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página con la información de la subcategoria cargada.
     */
    @GetMapping(value = "/eliminarSubCategoria")
    public String eliminarSubCategoria(@RequestParam("id") long idSubCategoria, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta que el Id sea mayor a 0.
        if (idSubCategoria <= 0) {
            // Se cargan las subcategorias para poder mostrarlos en el cuadro.
            model.addAttribute("subcategorias", subCategoriaDao.getSubCategorias());
            return "Administrador/SubCategoria/SubCategorias"; // Nombre del archivo jsp
        }
        // Se obtiene la información de la subcategoria de acuerdo al id.
        SubCategoria subcategoria = subCategoriaDao.obtenerSubCategoriaPorId(idSubCategoria);
        // Se carga la información de la subcategoria obtenida.
        model.addAttribute("subcategoria", subcategoria);
        return "Administrador/SubCategoria/EliminarSubCategoria"; // Nombre del archivo jsp
    }

    /**
     * Método que permite eliminar una subcategoria.
     *
     * @param subcategoria Objeto con la información a eliminar.
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/borrarSubCategoria")
    public String borrarSubCategoria(@ModelAttribute("subcategoria") SubCategoria subcategoria,
            Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se valida si la subcategoria tiene contenido.
        if (!contenidoDao.tieneContenido(subcategoria.getId(), Constantes.SUBCATEGORIA)) {
            // Se elimina la información.
            String mensaje = subCategoriaDao.eliminarSubCategoria(subcategoria);
            // Se actualiza el orden de las subcategorías.
            cambiarOrdenDeSubCategorias(subcategoria);
            // Se valida si la información fue eliminada.
            if (mensaje.equals("Eliminacion exitosa")) {
                // Se muestra el mensaje de éxito.
                model.addAttribute("result", "Subcategoria eliminada con éxito.");
                // Se carga la información de la subcategoria para que se refleje la información actualizada.
                model.addAttribute("subcategorias", subCategoriaDao.getSubCategorias());
                return "Administrador/SubCategoria/SubCategorias"; // Nombre del archivo jsp
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", mensaje);
                return "Administrador/SubCategoria/EliminarSubCategoria";
            }
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", "No es posible eliminar la subcategoria debido a que tiene contenido registrado.");
            return "Administrador/SubCategoria/EliminarSubCategoria";
        }
    }

    /**
     * Método que reordena todas las subcategorias dado un orden faltante.
     *
     * @param subcategoria Objeto con la información de la subcategoria borrada.
     */
    private void cambiarOrdenDeSubCategorias(SubCategoria subcategoria) {
        // Se obtiene el menor número de ordenamiento.
        int ordenMaximo = subCategoriaDao.getUltimoNumeroDeOrden(subcategoria.getCategoria().getId());
        // Se realiza el reordenamiento. 
        for (int i = subcategoria.getOrden(); i < ordenMaximo; i++) {
            long idSubCategoria = subCategoriaDao.getIdSubCategoriaPorOrden(subcategoria.getCategoria().getId(), i + 1);
            subCategoriaDao.cambiarOrdenDeSubCategoria(subcategoria.getCategoria().getId(), idSubCategoria, i);
        }
    }

}
