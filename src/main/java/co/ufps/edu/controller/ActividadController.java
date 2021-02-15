package co.ufps.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import co.ufps.edu.constantes.Constantes;
import co.ufps.edu.dao.ActividadDao;
import co.ufps.edu.dao.ContenidoDao;
import co.ufps.edu.dto.Actividad;
import javax.servlet.http.HttpServletRequest;

/**
 * Controlador de actividades. Las actividades son los eventos programador y que
 * serán realizados en una fecha específica. 
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
public class ActividadController {

    @Autowired
    private ActividadDao actividadDao;
    @Autowired
    private ContenidoDao contenidoDao;
    private AdminController adminController;

    /**
     * Constructor de la clase en donde se inicializan las variables.
     */
    public ActividadController() throws Exception {
        this.adminController = new AdminController();
    }

    /**
     * Método que retorna una página con todas las actividades del sistema.
     *
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página principal de actividades.
     */
    @GetMapping("/actividades") // Base
    public String index(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se cargan las actividades para poder mostrarlas en el cuadro.
        model.addAttribute("actividades", actividadDao.getActividades());
        return "Administrador/Actividad/Actividades"; // Nombre del archivo jsp
    }

    /**
     * Modelo con el que se realizara el formulario.
     *
     * @return Un objeto para ser llenado desde el archivo JSP.
     */
    @ModelAttribute("actividad")
    public Actividad setUpUserForm() {
        return new Actividad();
    }

    /**
     * Método que retorna una página para realizar el registro de una actividad.
     *
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página de registro de actividades.
     */
    @GetMapping("/registrarActividad") // Base
    public String registrarActividad(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        return "Administrador/Actividad/RegistrarActividad"; // Nombre del archivo jsp
    }

    /**
     * Método que permite guardar una actividad.
     *
     * @param actividad Objeto con la información a guardar.
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/guardarActividad")
    public String guardarActividad(@ModelAttribute("actividad") Actividad actividad, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (actividad.isValidoParaRegistrar()) {
            if (!actividad.HaySolapamiento()) {
                // Se registra la información.
                String mensaje = actividadDao.registrarActividad(actividad);
                // Se valida si el registro en la BD fue exitoso.
                if (mensaje.equals("Registro exitoso")) {
                    // Se muestra el mensaje de éxito.
                    model.addAttribute("result", "Actividad registrada con éxito.");
                    // Se carga la información de las actividades para que la nueva información se refleje en la tabla.
                    model.addAttribute("actividades", actividadDao.getActividades());
                    return "Administrador/Actividad/Actividades"; // Nombre del archivo jsp
                } else {
                    // Se muestra el mensaje de error.
                    model.addAttribute("wrong", mensaje);
                    return "Administrador/Actividad/RegistrarActividad"; // Nombre del archivo jsp
                }
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", "Las fechas no pueden solaparse.");
                return "Administrador/Actividad/RegistrarActividad"; // Nombre del archivo jsp
            }
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", "Debe llenar todos los campos.");
            return "Administrador/Actividad/RegistrarActividad"; // Nombre del archivo jsp
        }
    }

    /**
     * Método que obtiene la página de actualizar actividad dado un ID.
     *
     * @param idActividad Identificador de la actividad.
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página con la información de la actividad cargada.
     */
    @GetMapping(value = "/actualizarActividad")
    public String actualizarActividad(@RequestParam("id") long idActividad, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta que el Id sea mayor a 0.
        if (idActividad <= 0) {
            // Se cargan las actividades para poder mostrarlas en el cuadro.
            model.addAttribute("actividades", actividadDao.getActividades());
            return "Administrador/Actividad/Actividades"; // Nombre del archivo jsp
        }
        // Se obtiene la información de la actividad de acuerdo al id.
        Actividad actividad = actividadDao.obtenerActividadPorId(idActividad);
        // Se carga la información de la actividad obtenida.
        model.addAttribute("actividad", actividad);
        return "Administrador/Actividad/ActualizarActividad"; // Nombre del archivo jsp
    }

    /**
     * Método que permite editar una actividad.
     *
     * @param actividad Objeto con la información a editar.
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/editarActividad")
    public String editarActividad(@ModelAttribute("actividad") Actividad actividad, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si todos los campos estan llenos.
        if (actividad.isValidoParaActualizar()) {
            if (!actividad.HaySolapamiento()) {
                // Se edita la información.
                String mensaje = actividadDao.editarActividad(actividad);
                // Se valida si la información fue editada.
                if (mensaje.equals("Actualizacion exitosa")) {
                    // Se muestra el mensaje de éxito.
                    model.addAttribute("result", "Actividad actualizada con éxito.");
                    // Se carga la información de las actividades para que se refleje la información actualizada.
                    model.addAttribute("actividades", actividadDao.getActividades());
                    return "Administrador/Actividad/Actividades"; // Nombre del archivo jsp
                } else {
                    // Se muestra el mensaje de error.
                    model.addAttribute("wrong", mensaje);
                    return "Administrador/Actividad/ActualizarActividad";
                }
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", "Las fechas no pueden solaparse.");
                return "Administrador/Actividad/RegistrarActividad"; // Nombre del archivo jsp
            }
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", "Debe llenar todos los campos.");
            return "Administrador/Actividad/ActualizarActividad";
        }
    }

    /**
     * Método que obtiene la página de eliminar actividad dado un ID.
     *
     * @param idActividad Identificador de la actividad.
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página con la información de la actividad cargada.
     */
    @GetMapping(value = "/eliminarActividad")
    public String eliminarActividad(@RequestParam("id") long idActividad, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);        
        // Se consulta que el Id sea mayor a 0.
        if (idActividad <= 0) {
            // Se cargan las actividades para poder mostrarlas en el cuadro.
            model.addAttribute("actividades", actividadDao.getActividades());
            return "Administrador/Actividad/Actividades"; // Nombre del archivo jsp
        }
        // Se obtiene la información de la actividad de acuerdo al id.
        Actividad actividad = actividadDao.obtenerActividadPorId(idActividad);
        // Se carga la información de la actividad obtenida.
        model.addAttribute("actividad", actividad);
        return "Administrador/Actividad/EliminarActividad"; // Nombre del archivo jsp
    }

    /**
     * Método que permite eliminar una actividad.
     *
     * @param actividad Objeto con la información a eliminar.
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/borrarActividad")
    public String borrarActividad(@ModelAttribute("actividad") Actividad actividad, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se valida si la actividad tiene contenido ligado.
        if (!contenidoDao.tieneContenido(actividad.getId(), Constantes.ACTIVIDAD)) {
            // Se elimina la información.
            String mensaje = actividadDao.eliminarActividad(actividad);
            // Se valida si la información fue eliminada.
            if (mensaje.equals("Eliminacion exitosa")) {
                // Se muestra el mensaje de éxito.
                model.addAttribute("result", "Actividad eliminada con éxito.");
                // Se carga la información de las actividades para que se refleje la información actualizada.
                model.addAttribute("actividades", actividadDao.getActividades());
                return "Administrador/Actividad/Actividades"; // Nombre del archivo jsp
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", mensaje);
                return "Administrador/Actividad/EliminarActividad";
            }
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", "No es posible eliminar la actividad debido a que tiene contenido registrado.");
            return "Administrador/Actividad/EliminarActividad";
        }
    }
}
