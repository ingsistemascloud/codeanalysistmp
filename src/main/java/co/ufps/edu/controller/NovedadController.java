package co.ufps.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import co.ufps.edu.constantes.Constantes;
import co.ufps.edu.dao.ContenidoDao;
import co.ufps.edu.dao.NovedadDao;
import co.ufps.edu.dto.Novedad;
import javax.servlet.http.HttpServletRequest;

/**
 * Controlador de novedades. Las novedades son sucesos importantes que ocurren y
 * hay que destacarlos para que las personas puedan observarlo.
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
public class NovedadController {

    @Autowired
    private NovedadDao novedadDao;
    @Autowired
    private ContenidoDao contenidoDao;
    private AdminController adminController;

    /**
     * Constructor de la clase en donde se inicializan las variables
     */
    public NovedadController() throws Exception {
        this.adminController = new AdminController();
    }
    
    /**
     * Modelo con el que se realizara el formulario.
     *
     * @return Un objeto para ser llenado desde el archivo .JSP
     */
    @ModelAttribute("novedad")
    public Novedad setUpUserForm() {
        return new Novedad();
    }

    /**
     * Método que retorna una página con todas las novedades en el sistema.
     *
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página principal de novedades.
     */
    @GetMapping("/novedades") // Base
    public String index(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se cargan las novedades para poder mostrarlas en el cuadro.
        model.addAttribute("novedades", novedadDao.getNovedades());
        return "Administrador/Novedad/novedades"; // Nombre del archivo jsp
    }

    /**
     * Método que retorna una página para realizar el registro de novedad.
     *
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página de registro de novedades.
     */
    @GetMapping("/registrarNovedad") // Base
    public String registrarNovedad(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        return "Administrador/Novedad/RegistrarNovedad"; // Nombre del archivo jsp
    }

    /**
     * Método que permite guardar novedad.
     *
     * @param novedad Objeto con la información a guardar
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/guardarNovedad")
    public String guardarNovedad(@ModelAttribute("novedad") Novedad novedad, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (novedad.isValidoParaRegistrar()) {
            // Se registra la información.
            String mensaje = novedadDao.registrarNovedad(novedad);
            // Se verifica si se ejecuta la acción.
            if (mensaje.equals("Registro exitoso")) {
                // Se muestra el mensaje de éxito.
                model.addAttribute("result", "Novedad registrada con éxito.");
                // Se cargan las noticias para poder mostrarlas en el cuadro.
                model.addAttribute("novedades", novedadDao.getNovedades());
                return "Administrador/Novedad/novedades"; // Nombre del archivo jsp
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", mensaje);
                return "Administrador/Novedad/RegistrarNovedad";
            }
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", "Debe llenar todos  los campos.");
            return "Administrador/Novedad/RegistrarNovedad";
        }
    }

    /**
     * Método que obtiene la página de actualizar novedad dado un ID.
     *
     * @param idNovedad Identificador de la novedad.
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página con la información de la novedad cargada.
     */
    @GetMapping(value = "/actualizarNovedad")
    public String actualizarNovedad(@RequestParam("id") long idNovedad, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta que el Id sea mayor a 0.
        if (idNovedad <= 0) {
            // Se cargan las novedades para poder mostrarlas en el cuadro.
            model.addAttribute("novedades", novedadDao.getNovedades());
            return "Administrador/Novedad/novedades"; // Nombre del archivo jsp
        }
        // Se obtiene la información de la novedad de acuerdo al id.
        Novedad novedad = novedadDao.obtenerNovedadPorId(idNovedad);
        // Se carga la información de la novedad obtenida.
        model.addAttribute("novedad", novedad);
        return "Administrador/Novedad/ActualizarNovedad"; // Nombre del archivo jsp
    }

    /**
     * Método que permite editar una novedad.
     *
     * @param novedad Objeto con la información a editar.
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/editarNovedad")
    public String editarNovedad(@ModelAttribute("novedad") Novedad novedad, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (novedad.isValidoParaActualizar()) {
            // Se edita la información.
            String mensaje = novedadDao.editarNovedad(novedad);
            // Se valida si la información fue editada.
            if (mensaje.equals("Actualizacion exitosa")) {
                // Se muestra el mensaje de éxito.
                model.addAttribute("result", "Novedad actualizada con éxito.");
                // Se carga la información de las noticias para que se refleje la información actualizada.
                model.addAttribute("novedades", novedadDao.getNovedades());
                return "Administrador/Novedad/novedades"; // Nombre del archivo jsp
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", mensaje);
                return "Administrador/Novedad/ActualizarNovedad";
            }
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", "Debe llenar todos los campos.");
            return "Administrador/Novedad/ActualizarNovedad";
        }
    }

    /**
     * Método que obtiene la página de eliminar novedad dado un ID.
     *
     * @param idNovedad Identificador de la novedad.
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La pagina con la información de la novedad cargada.
     */
    @GetMapping(value = "/eliminarNovedad")
    public String eliminarNovedad(@RequestParam("id") long idNovedad, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta que el Id sea mayor a 0.
        if (idNovedad <= 0) {
            // Se cargan las novedades para poder mostrarlas en el cuadro.
            model.addAttribute("novedades", novedadDao.getNovedades());
            return "Administrador/Novedad/novedades"; // Nombre del archivo jsp
        }
        // Se obtiene la información de la novedad de acuerdo al id.
        Novedad novedad = novedadDao.obtenerNovedadPorId(idNovedad);
        // Se carga la información de la novedad obtenida.
        model.addAttribute("novedad", novedad);
        return "Administrador/Novedad/EliminarNovedad"; // Nombre del archivo jsp
    }

    /**
     * Método que permite eliminar una novedad.
     *
     * @param novedad Objeto con la información a eliminar.
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/borrarNovedad")
    public String borrarNovedad(@ModelAttribute("novedad") Novedad novedad, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se elimina la información.
        if (!contenidoDao.tieneContenido(novedad.getId(), Constantes.NOVEDAD)) {
            // Se elimina la información.
            String mensaje = novedadDao.eliminarNovedad(novedad);
            // Se valida si la información fue eliminada.
            if (mensaje.equals("Eliminacion exitosa")) {
                // Se muestra el mensaje de éxito.
                model.addAttribute("result", "Novedad eliminada con éxito.");
                // Se carga la información de las novedades para que se refleje la información actualizada.
                model.addAttribute("novedades", novedadDao.getNovedades());
                return "Administrador/Novedad/novedades"; // Nombre del archivo jsp
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", mensaje);
                return "Administrador/Novedad/EliminarNovedad";
            }
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", "No es posible eliminar la novedad debido a que tiene contenido registrado.");
            return "Administrador/Novedad/EliminarNovedad";
        }
    }

}
