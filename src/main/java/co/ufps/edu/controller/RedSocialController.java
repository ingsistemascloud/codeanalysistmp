package co.ufps.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import co.ufps.edu.dao.RedSocialDao;
import co.ufps.edu.dto.RedSocial;
import javax.servlet.http.HttpServletRequest;

/**
 * Controlador de red social. Las redes sociales son las paginas que puede visitar
 * el usuario en las diferentes redes sociales existentes.
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
public class RedSocialController {

    private RedSocialDao redSocialDao;
    private AdminController adminController;
    
    /**
     * Constructor de la clase en donde se inicializan las variables
     */
    public RedSocialController() throws Exception {
        redSocialDao = new RedSocialDao();
        this.adminController = new AdminController();
    }

    /**
     * Modelo con el que se realizara el formulario
     *
     * @return Un objeto para ser llenado desde el archivo .JSP
     */
    @ModelAttribute("redSocial")
    public RedSocial setUpUserForm() {
        return new RedSocial();
    }

    /**
     * Método que retorna una página con todas los redes sociales en el sistema.
     *
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página principal de redes sociales.
     */
    @GetMapping("/redes") // Base
    public String index(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se cargan las las redes sociales para poder mostrarlas en el cuadro.
        model.addAttribute("redesSociales", redSocialDao.getRedesSociales());
        return "Administrador/RedSocial/redes"; // Nombre del archivo jsp
    }

    /**
     * Método que retorna una página para realizar el registro de una red social.
     *
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página de registro de redes sociales.
     */
    @GetMapping("/registrarRedSocial") // Base
    public String registrarRedSocial(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        return "Administrador/RedSocial/RegistrarRedSocial"; // Nombre del archivo jsp
    }

    /**
     * Método que permite guardar una red social.
     *
     * @param redSocial Objeto con la información a guardar
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/guardarRedSocial")
    public String registrarRedSocial(@ModelAttribute("redSocial") RedSocial redSocial, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (redSocial.isValidoParaRegistrar()) {
            // Se registra la información.
            String mensaje = redSocialDao.registrarRedSocial(redSocial);
            // Se verifica si se ejecuta la acción.
            if (mensaje.equals("Registro exitoso")) {
                // Se muestra el mensaje de éxito.
                model.addAttribute("result", "Red social registrada con éxito.");
                // Se cargan las redes sociales para poder mostrarlas en el cuadro.
                model.addAttribute("redesSociales", redSocialDao.getRedesSociales());
                return "Administrador/RedSocial/redes"; // Nombre del archivo jsp
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", mensaje);
                return "Administrador/RedSocial/RegistrarRedSocial";
            }
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", "Debe llenar todos los campos.");
            return "Administrador/RedSocial/RegistrarRedSocial";
        }
    }

    /**
     * Método que obtiene la página de actualizar red social dado un ID.
     *
     * @param idRedSocial Identificador de la red social
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La pagina con la información de la red social cargada.
     */
    @GetMapping(value = "/actualizarRedSocial")
    public String actualizarRedSocial(@RequestParam("id") long idRedSocial, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta que el Id sea mayor a 0.
        if (idRedSocial <= 0) {
            // Se cargan las redes sociales para poder mostrarlas en el cuadro.
            model.addAttribute("redesSociales", redSocialDao.getRedesSociales());
            return "Administrador/RedSocial/redes"; // Nombre del archivo jsp
        }
        // Se obtiene la información de la red social de acuerdo al id.
        RedSocial redSocial = redSocialDao.obtenerRedSocialPorId(idRedSocial);
        // Se carga la información de la novedad obtenida.
        model.addAttribute("redSocial", redSocial);
        return "Administrador/RedSocial/ActualizarRedSocial"; // Nombre del archivo jsp
    }

    /**
     * Método que permite editar una red social.
     *
     * @param redSocial Objeto con la información a editar.
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/editarRedSocial")
    public String editarRedSocial(@ModelAttribute("redSocial") RedSocial redSocial, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (redSocial.isValidoParaRegistrar()) {
            // Se edita la información.
            String mensaje = redSocialDao.editarRedSocial(redSocial);
            // Se valida si la información fue editada.
            if (mensaje.equals("Actualizacion exitosa")) {
                // Se muestra el mensaje de éxito.
                model.addAttribute("result", "Red social actualizada con éxito.");
                // Se carga la información de las redes sociales para que se refleje la información actualizada.
                model.addAttribute("redesSociales", redSocialDao.getRedesSociales());
                return "Administrador/RedSocial/redes"; // Nombre del archivo jsp
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", mensaje);
                return "Administrador/RedSocial/ActualizarRedSocial";
            }
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", "Debe llenar todos los campos.");
            return "Administrador/RedSocial/ActualizarRedSocial";
        }
    }

    /**
     * Método que obtiene la página de eliminar red social dado un ID.
     *
     * @param idRedSocial Identificador de red social
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La pagina con la información de la red social cargada.
     */
    @GetMapping(value = "/eliminarRedSocial")
    public String eliminarRedSocial(@RequestParam("id") long idRedSocial, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta que el Id sea mayor a 0.
        if (idRedSocial <= 0) {
            // Se cargan las redes sociales para poder mostrarlas en el cuadro.
            model.addAttribute("redesSociales", redSocialDao.getRedesSociales());
            return "Administrador/RedSocial/redes"; // Nombre del archivo jsp
        }
        // Se obtiene la información de la red social de acuerdo al id.
        RedSocial redSocial = redSocialDao.obtenerRedSocialPorId(idRedSocial);
        // Se carga la información de la red social obtenida.
        model.addAttribute("redSocial", redSocial);
        return "Administrador/RedSocial/EliminarRedSocial"; // Nombre del archivo jsp
    }

    /**
     * Método que permite eliminar una red social.
     *
     * @param redSocial Objeto con la información a eliminar.
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/borrarRedSocial")
    public String borrarRedSocial(@ModelAttribute("redSocial") RedSocial redSocial, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se elimina la información.
        String mensaje = redSocialDao.eliminarRedSocial(redSocial);
        // Se valida si la información fue eliminada.
        if (mensaje.equals("Eliminacion exitosa")) {
            // Se muestra el mensaje de éxito.
            model.addAttribute("result", "Red social eliminada con éxito.");
            // Se carga la información de las redes sociales para que se refleje la información actualizada.
            model.addAttribute("redesSociales", redSocialDao.getRedesSociales());
            return "Administrador/RedSocial/redes"; // Nombre del archivo jsp
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", mensaje);
            return "Administrador/RedSocial/EliminarRedSocial";
        }
    }
}
