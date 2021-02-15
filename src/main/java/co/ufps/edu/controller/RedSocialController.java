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
 * Todos los servicios publicados en esta clase ser�n expuestos para ser 
 * consumidos por los archivos JSP.
 *
 * La etiqueta @Controller escanea todos los servicios para publicarlos seg�n el
 * tipo de m�todo HTTP.
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
     * M�todo que retorna una p�gina con todas los redes sociales en el sistema.
     *
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina principal de redes sociales.
     */
    @GetMapping("/redes") // Base
    public String index(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se cargan las las redes sociales para poder mostrarlas en el cuadro.
        model.addAttribute("redesSociales", redSocialDao.getRedesSociales());
        return "Administrador/RedSocial/redes"; // Nombre del archivo jsp
    }

    /**
     * M�todo que retorna una p�gina para realizar el registro de una red social.
     *
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina de registro de redes sociales.
     */
    @GetMapping("/registrarRedSocial") // Base
    public String registrarRedSocial(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        return "Administrador/RedSocial/RegistrarRedSocial"; // Nombre del archivo jsp
    }

    /**
     * M�todo que permite guardar una red social.
     *
     * @param redSocial Objeto con la informaci�n a guardar
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina a donde debe redireccionar despu�s de la acci�n.
     */
    @PostMapping(value = "/guardarRedSocial")
    public String registrarRedSocial(@ModelAttribute("redSocial") RedSocial redSocial, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (redSocial.isValidoParaRegistrar()) {
            // Se registra la informaci�n.
            String mensaje = redSocialDao.registrarRedSocial(redSocial);
            // Se verifica si se ejecuta la acci�n.
            if (mensaje.equals("Registro exitoso")) {
                // Se muestra el mensaje de �xito.
                model.addAttribute("result", "Red social registrada con �xito.");
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
     * M�todo que obtiene la p�gina de actualizar red social dado un ID.
     *
     * @param idRedSocial Identificador de la red social
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La pagina con la informaci�n de la red social cargada.
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
        // Se obtiene la informaci�n de la red social de acuerdo al id.
        RedSocial redSocial = redSocialDao.obtenerRedSocialPorId(idRedSocial);
        // Se carga la informaci�n de la novedad obtenida.
        model.addAttribute("redSocial", redSocial);
        return "Administrador/RedSocial/ActualizarRedSocial"; // Nombre del archivo jsp
    }

    /**
     * M�todo que permite editar una red social.
     *
     * @param redSocial Objeto con la informaci�n a editar.
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina a donde debe redireccionar despu�s de la acci�n.
     */
    @PostMapping(value = "/editarRedSocial")
    public String editarRedSocial(@ModelAttribute("redSocial") RedSocial redSocial, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (redSocial.isValidoParaRegistrar()) {
            // Se edita la informaci�n.
            String mensaje = redSocialDao.editarRedSocial(redSocial);
            // Se valida si la informaci�n fue editada.
            if (mensaje.equals("Actualizacion exitosa")) {
                // Se muestra el mensaje de �xito.
                model.addAttribute("result", "Red social actualizada con �xito.");
                // Se carga la informaci�n de las redes sociales para que se refleje la informaci�n actualizada.
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
     * M�todo que obtiene la p�gina de eliminar red social dado un ID.
     *
     * @param idRedSocial Identificador de red social
     * @param model Objeto para enviar informaci�n a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La pagina con la informaci�n de la red social cargada.
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
        // Se obtiene la informaci�n de la red social de acuerdo al id.
        RedSocial redSocial = redSocialDao.obtenerRedSocialPorId(idRedSocial);
        // Se carga la informaci�n de la red social obtenida.
        model.addAttribute("redSocial", redSocial);
        return "Administrador/RedSocial/EliminarRedSocial"; // Nombre del archivo jsp
    }

    /**
     * M�todo que permite eliminar una red social.
     *
     * @param redSocial Objeto con la informaci�n a eliminar.
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina a donde debe redireccionar despu�s de la acci�n.
     */
    @PostMapping(value = "/borrarRedSocial")
    public String borrarRedSocial(@ModelAttribute("redSocial") RedSocial redSocial, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se elimina la informaci�n.
        String mensaje = redSocialDao.eliminarRedSocial(redSocial);
        // Se valida si la informaci�n fue eliminada.
        if (mensaje.equals("Eliminacion exitosa")) {
            // Se muestra el mensaje de �xito.
            model.addAttribute("result", "Red social eliminada con �xito.");
            // Se carga la informaci�n de las redes sociales para que se refleje la informaci�n actualizada.
            model.addAttribute("redesSociales", redSocialDao.getRedesSociales());
            return "Administrador/RedSocial/redes"; // Nombre del archivo jsp
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", mensaje);
            return "Administrador/RedSocial/EliminarRedSocial";
        }
    }
}
