package co.ufps.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import co.ufps.edu.dao.ContactoDao;
import co.ufps.edu.dto.Contacto;
import javax.servlet.http.HttpServletRequest;

/**
 * Controlador de contactos. Los contactos es a donde se pueden comunicar los 
 * usuarios de la aplicación si necesitan información adicional sobre algun tema en particular. 
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
public class ContactoController {

    private ContactoDao contactoDao;
    private AdminController adminController;

    /**
     * Constructor de la clase en donde se inicializan las variables
     */
    public ContactoController() throws Exception {
        contactoDao = new ContactoDao();
        this.adminController = new AdminController();
    }

    /**
     * Modelo con el que se realizara el formulario
     *
     * @return Un objeto para ser llenado desde el archivo .JSP
     */
    @ModelAttribute("contacto")
    public Contacto setUpUserForm() {
        return new Contacto();
    }

    /**
     * Método que retorna una pagina para realizar el registro de contacto.
     *
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página de registro de contactos.
     */
    @GetMapping("/registrarContacto") // Base
    public String registrarContacto(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        return "Administrador/Contacto/RegistrarContacto"; // Nombre del archivo jsp
    }

    /**
     * Método que retorna una pagina con todas los contactos en el sistema.
     *
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página principal de contenidos.
     */
    @GetMapping("/contactos") // Base
    public String index(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se cargan los contactos para poder mostrarlos en el cuadro.
        model.addAttribute("contactos", contactoDao.getContactos());
        return "Administrador/Contacto/Contactos"; // Nombre del archivo jsp
    }

    /**
     * Método que permite guardar contacto
     *
     * @param contacto Objeto con la información a guardar
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/guardarContacto")
    public String guardarContacto(@ModelAttribute("contacto") Contacto contacto, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (contacto.isValidoParaRegistrar()) {
            // Se registra la información.
            String mensaje = contactoDao.registrarConacto(contacto);
            // Se valida si el registro en la BD fue exitoso.
            if (mensaje.equals("Registro exitoso")) {
                // Se muestra el mensaje de éxito.
                model.addAttribute("result", "Contacto registrado con éxito.");
                // Se carga la información de los contactos para que la nueva información se refleje en la tabla.
                model.addAttribute("contactos", contactoDao.getContactos());
                return "Administrador/Contacto/Contactos"; // Nombre del archivo jsp
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", mensaje);
                return "Administrador/Contacto/RegistrarContacto";
            }
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", "Debe llenar todos los campos.");
            return "Administrador/Contacto/RegistrarContacto";
        }
    }

    /**
     * Método que obtiene la pagina de actualizar contacto dado un ID.
     *
     * @param idContacto Identificador del contacto
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página con la información del contacto cargada.
     */
    @GetMapping(value = "/actualizarContacto")
    public String actualizarContacto(@RequestParam("id") long idContacto, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta que el Id sea mayor a 0.
        if (idContacto <= 0) {
            // Se cargan los contactos para poder mostrarlos en el cuadro.
            model.addAttribute("contactos", contactoDao.getContactos());
            return "Administrador/Contacto/Contactos"; // Nombre del archivo jsp
        }
        // Se obtiene la información del contacto de acuerdo al id.
        Contacto contacto = contactoDao.obtenerContactoPorId(idContacto);
        // Se carga la información del contacto obtenida.
        model.addAttribute("contacto", contacto);
        return "Administrador/Contacto/ActualizarContacto"; // Nombre del archivo jsp
    }

    /**
     * Método que permite editar un contacto.
     *
     * @param contacto Objeto con la información a editar.
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/editarContacto")
    public String editarContacto(@ModelAttribute("contacto") Contacto contacto, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (contacto.isValidoParaRegistrar()) {
            // Se edita la información.
            String mensaje = contactoDao.editarContacto(contacto);
            // Se valida si la información fue editada.
            if (mensaje.equals("Actualizacion exitosa")) {
                // Se muestra el mensaje de éxito.
                model.addAttribute("result", "Contacto actualizado con éxito.");
                // Se carga la información de las categorías para que se refleje la información actualizada.
                model.addAttribute("contactos", contactoDao.getContactos());
                return "Administrador/Contacto/Contactos"; // Nombre del archivo jsp
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", mensaje);
                return "Administrador/Contacto/ActualizarContacto";
            }
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", "Debe llenar todos los campos.");
            return "Administrador/Contacto/ActualizarContacto";
        }
    }

    /**
     * Método que obtiene la pagina de eliminar contacto dado un ID.
     *
     * @param idContacto Identificador de contacto
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página con la información del contacto cargada.
     */
    @GetMapping(value = "/eliminarContacto")
    public String eliminarContacto(@RequestParam("id") long idContacto, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta que el Id sea mayor a 0.
        if (idContacto <= 0) {
            // Se cargan los contactos para poder mostrarlas en el cuadro.
            model.addAttribute("contactos", contactoDao.getContactos());
            return "Administrador/Contacto/Contactos"; // Nombre del archivo jsp
        }
        // Se obtiene la información del contacto de acuerdo al id.
        Contacto contacto = contactoDao.obtenerContactoPorId(idContacto);
        // Se carga la información del contacto obtenida.
        model.addAttribute("contacto", contacto);
        return "Administrador/Contacto/EliminarContacto"; // Nombre del archivo jsp
    }

    /**
     * Método que permite eliminar un contacto.
     *
     * @param contacto Objeto con la información a eliminar.
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/borrarContacto")
    public String borrarContacto(@ModelAttribute("contacto") Contacto contacto, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se elimina la información.
        String mensaje = contactoDao.eliminarContacto(contacto);
        // Se valida si la información fue eliminada.
        if (mensaje.equals("Eliminacion exitosa")) {
            // Se muestra el mensaje de éxito.
            model.addAttribute("result", "Contacto eliminado con éxito.");
            // Se carga la información de los contactos para que se refleje la información actualizada.
            model.addAttribute("contactos", contactoDao.getContactos());
            return "Administrador/Contacto/Contactos"; // Nombre del archivo jsp
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", mensaje);
            return "Administrador/Contacto/EliminarContacto";
        }
    }

}
