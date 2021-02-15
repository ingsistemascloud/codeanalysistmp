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
 * Todos los servicios publicados en esta clase ser�n expuestos para ser 
 * consumidos por los archivos JSP.
 *
 * La etiqueta @Controller escanea todos los servicios para publicarlos seg�n el
 * tipo de m�todo HTTP.
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
     * M�todo que retorna una p�gina con todas las novedades en el sistema.
     *
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina principal de novedades.
     */
    @GetMapping("/novedades") // Base
    public String index(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se cargan las novedades para poder mostrarlas en el cuadro.
        model.addAttribute("novedades", novedadDao.getNovedades());
        return "Administrador/Novedad/novedades"; // Nombre del archivo jsp
    }

    /**
     * M�todo que retorna una p�gina para realizar el registro de novedad.
     *
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina de registro de novedades.
     */
    @GetMapping("/registrarNovedad") // Base
    public String registrarNovedad(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        return "Administrador/Novedad/RegistrarNovedad"; // Nombre del archivo jsp
    }

    /**
     * M�todo que permite guardar novedad.
     *
     * @param novedad Objeto con la informaci�n a guardar
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina a donde debe redireccionar despu�s de la acci�n.
     */
    @PostMapping(value = "/guardarNovedad")
    public String guardarNovedad(@ModelAttribute("novedad") Novedad novedad, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (novedad.isValidoParaRegistrar()) {
            // Se registra la informaci�n.
            String mensaje = novedadDao.registrarNovedad(novedad);
            // Se verifica si se ejecuta la acci�n.
            if (mensaje.equals("Registro exitoso")) {
                // Se muestra el mensaje de �xito.
                model.addAttribute("result", "Novedad registrada con �xito.");
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
     * M�todo que obtiene la p�gina de actualizar novedad dado un ID.
     *
     * @param idNovedad Identificador de la novedad.
     * @param model Objeto para enviar informaci�n a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina con la informaci�n de la novedad cargada.
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
        // Se obtiene la informaci�n de la novedad de acuerdo al id.
        Novedad novedad = novedadDao.obtenerNovedadPorId(idNovedad);
        // Se carga la informaci�n de la novedad obtenida.
        model.addAttribute("novedad", novedad);
        return "Administrador/Novedad/ActualizarNovedad"; // Nombre del archivo jsp
    }

    /**
     * M�todo que permite editar una novedad.
     *
     * @param novedad Objeto con la informaci�n a editar.
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina a donde debe redireccionar despu�s de la acci�n.
     */
    @PostMapping(value = "/editarNovedad")
    public String editarNovedad(@ModelAttribute("novedad") Novedad novedad, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (novedad.isValidoParaActualizar()) {
            // Se edita la informaci�n.
            String mensaje = novedadDao.editarNovedad(novedad);
            // Se valida si la informaci�n fue editada.
            if (mensaje.equals("Actualizacion exitosa")) {
                // Se muestra el mensaje de �xito.
                model.addAttribute("result", "Novedad actualizada con �xito.");
                // Se carga la informaci�n de las noticias para que se refleje la informaci�n actualizada.
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
     * M�todo que obtiene la p�gina de eliminar novedad dado un ID.
     *
     * @param idNovedad Identificador de la novedad.
     * @param model Objeto para enviar informaci�n a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La pagina con la informaci�n de la novedad cargada.
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
        // Se obtiene la informaci�n de la novedad de acuerdo al id.
        Novedad novedad = novedadDao.obtenerNovedadPorId(idNovedad);
        // Se carga la informaci�n de la novedad obtenida.
        model.addAttribute("novedad", novedad);
        return "Administrador/Novedad/EliminarNovedad"; // Nombre del archivo jsp
    }

    /**
     * M�todo que permite eliminar una novedad.
     *
     * @param novedad Objeto con la informaci�n a eliminar.
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina a donde debe redireccionar despu�s de la acci�n.
     */
    @PostMapping(value = "/borrarNovedad")
    public String borrarNovedad(@ModelAttribute("novedad") Novedad novedad, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se elimina la informaci�n.
        if (!contenidoDao.tieneContenido(novedad.getId(), Constantes.NOVEDAD)) {
            // Se elimina la informaci�n.
            String mensaje = novedadDao.eliminarNovedad(novedad);
            // Se valida si la informaci�n fue eliminada.
            if (mensaje.equals("Eliminacion exitosa")) {
                // Se muestra el mensaje de �xito.
                model.addAttribute("result", "Novedad eliminada con �xito.");
                // Se carga la informaci�n de las novedades para que se refleje la informaci�n actualizada.
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
