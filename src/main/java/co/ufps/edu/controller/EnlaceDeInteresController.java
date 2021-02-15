package co.ufps.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import co.ufps.edu.dao.EnlaceDeInteresDao;
import co.ufps.edu.dto.EnlaceDeInteres;
import javax.servlet.http.HttpServletRequest;

/**
 * Controlador de enlaces de interes. Los enlaces de interes son las páginas de redes sociales por ejemplo 
 * que puede consultar un usuario de la aplicación si necesitan información adicional sobre algun tema en particular. 
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
public class EnlaceDeInteresController {

    private EnlaceDeInteresDao enlaceDeInteresDao;
    private AdminController adminController;
    
    /**
     * Constructor de la clase en donde se inicializan las variables
     */
    public EnlaceDeInteresController() throws Exception {
        enlaceDeInteresDao = new EnlaceDeInteresDao();
        this.adminController = new AdminController();
    }

    /**
     * Modelo con el que se realizara el formulario
     *
     * @return Un objeto para ser llenado desde el archivo .JSP
     */
    @ModelAttribute("enlaceDeInteres")
    public EnlaceDeInteres setUpUserForm() {
        return new EnlaceDeInteres();
    }

    /**
     * Método que retorna una página con todas los enlaces de interes en el sistema.
     * 
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página principal de enlaces de interes.
     */
    @GetMapping("/enlacesDeInteres") // Base
    public String index(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se cargan los enlaces de interes para poder mostrarlos en el cuadro.
        model.addAttribute("enlacesDeInteres", enlaceDeInteresDao.getEnlacesDeInteres());
        return "Administrador/EnlaceDeInteres/EnlacesDeInteres"; // Nombre del archivo jsp
    }

    /**
     * Método que retorna una página para realizar el registro de enlaces de interes.
     *
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página de registro de enlaces de interes.
     */
    @GetMapping("/registrarEnlaceDeInteres") // Base
    public String registrarEnlaceDeIneres(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        return "Administrador/EnlaceDeInteres/RegistrarEnlaceDeInteres"; // Nombre del archivo jsp
    }

    /**
     * Método que permite guardar enlaces de interes.
     *
     * @param enlaceDeInteres Objeto con la información a guardar.
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/guardarEnlaceDeInteres")
    public String guardarEnlaceDeInteres(
            @ModelAttribute("enlaceDeInteres") EnlaceDeInteres enlaceDeInteres, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (enlaceDeInteres.isValidoParaRegistrar()) {
            // Se registra la información.
            String mensaje = enlaceDeInteresDao.registrarEnlaceDeInteres(enlaceDeInteres);
            // Se valida si el registro en la BD fue exitoso.
            if (mensaje.equals("Registro exitoso")) {
                // Se muestra el mensaje de éxito.
                model.addAttribute("result", "Enlace de interés registrado con éxito.");
                // Se carga la información de los enlaces de interes para que la nueva información se refleje en la tabla.
                model.addAttribute("enlacesDeInteres", enlaceDeInteresDao.getEnlacesDeInteres());
                return "Administrador/EnlaceDeInteres/EnlacesDeInteres"; // Nombre del archivo jsp
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", mensaje);
                return "Administrador/EnlaceDeInteres/RegistrarEnlaceDeInteres";
            }
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", "Debe llenar todos los campos.");
            return "Administrador/EnlaceDeInteres/RegistrarEnlaceDeInteres";
        }
    }

    /**
     * Método que obtiene la página de actualizar enlace de interes dado un ID.
     *
     * @param idEnlaceDeInteres Identificador del enlace de interes
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página con la información del enlace de interes cargado.
     */
    @GetMapping(value = "/actualizarEnlaceDeInteres")
    public String actualizarEnlaceDeInteres(@RequestParam("id") long idEnlaceDeInteres, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta que el Id sea mayor a 0.
        if (idEnlaceDeInteres <= 0) {
            // Se cargan los enlaces de interes para poder mostrarlos en el cuadro.
            model.addAttribute("enlacesDeInteres", enlaceDeInteresDao.getEnlacesDeInteres());
            return "Administrador/EnlaceDeInteres/EnlacesDeInteres"; // Nombre del archivo jsp
        }
        // Se obtiene la información de los enlaces de interes de acuerdo al id.
        EnlaceDeInteres enlaceDeInteres = enlaceDeInteresDao.obtenerEnlaceDeInteresPorId(idEnlaceDeInteres);
        // Se carga la información de los enlaces de interes obtenidos.
        model.addAttribute("enlaceDeInteres", enlaceDeInteres);
        return "Administrador/EnlaceDeInteres/ActualizarEnlaceDeInteres"; // Nombre del archivo jsp
    }

    /**
     * Método que permite editar un enlace de interes.
     *
     * @param enlaceDeInteres Objeto con la información a editar.
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/editarEnlaceDeInteres")
    public String editarEnlaceDeInteres(
            @ModelAttribute("enlaceDeInteres") EnlaceDeInteres enlaceDeInteres, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (enlaceDeInteres.isValidoParaRegistrar()) {
            // Se edita la información.
            String mensaje = enlaceDeInteresDao.editarEnlaceDeInteres(enlaceDeInteres);
            // Se valida si la información fue editada.
            if (mensaje.equals("Actualizacion exitosa")) {
                // Se muestra el mensaje de éxito.
                model.addAttribute("result", "Enlace de interés actualizado con éxito.");
                // Se carga la información de los enlaces para que se refleje la información actualizada.
                model.addAttribute("enlacesDeInteres", enlaceDeInteresDao.getEnlacesDeInteres());
                return "Administrador/EnlaceDeInteres/EnlacesDeInteres"; // Nombre del archivo jsp
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", mensaje);
                return "Administrador/EnlaceDeInteres/ActualizarEnlaceDeInteres";
            }
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", "Debe llenar todos los campos.");
            return "Administrador/EnlaceDeInteres/ActualizarEnlaceDeInteres";
        }
    }

    /**
     * Método que obtiene la página de eliminar enlace de interes dado un ID.
     *
     * @param idEnlaceDeInteres Identificador de enlace de interes
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página con la información del enlace de interes cargado.
     */
    @GetMapping(value = "/eliminarEnlaceDeInteres")
    public String eliminarEnlaceDeInteres(@RequestParam("id") long idEnlaceDeInteres, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta que el Id sea mayor a 0.
        if (idEnlaceDeInteres <= 0) {
            // Se cargan los enlaces para poder mostrarlas en el cuadro.
            model.addAttribute("enlacesDeInteres", enlaceDeInteresDao.getEnlacesDeInteres());
            return "Administrador/EnlaceDeInteres/EnlacesDeInteres"; // Nombre del archivo jsp
        }
        // Se obtiene la información del enlace de acuerdo al id.
        EnlaceDeInteres enlaceDeInteres = enlaceDeInteresDao.obtenerEnlaceDeInteresPorId(idEnlaceDeInteres);
        // Se carga la información de los enlaces obtenida.
        model.addAttribute("enlaceDeInteres", enlaceDeInteres);
        return "Administrador/EnlaceDeInteres/EliminarEnlaceDeInteres"; // Nombre del archivo jsp
    }

    /**
     * Método que permite eliminar un enlace de interes.
     *
     * @param enlaceDeInteres Objeto con la información a eliminar.
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/borrarEnlaceDeInteres")
    public String borrarEnlaceDeInteres(
            @ModelAttribute("enlaceDeInteres") EnlaceDeInteres enlaceDeInteres, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se elimina la información.
        String mensaje = enlaceDeInteresDao.eliminarEnlaceDeInteres(enlaceDeInteres);
        // Se valida si la información fue eliminada.
        if (mensaje.equals("Eliminacion exitosa")) {
            // Se muestra el mensaje de éxito.
            model.addAttribute("result", "Enlace de interés eliminado con éxito.");
            // Se carga la información de los enlaces para que se refleje la información actualizada.
            model.addAttribute("enlacesDeInteres", enlaceDeInteresDao.getEnlacesDeInteres());
            return "Administrador/EnlaceDeInteres/EnlacesDeInteres"; // Nombre del archivo jsp
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", mensaje);
            return "Administrador/EnlaceDeInteres/EliminarEnlaceDeInteres";
        }
    }
}
