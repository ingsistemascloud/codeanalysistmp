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
 * usuarios de la aplicaci�n si necesitan informaci�n adicional sobre algun tema en particular. 
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
     * M�todo que retorna una pagina para realizar el registro de contacto.
     *
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina de registro de contactos.
     */
    @GetMapping("/registrarContacto") // Base
    public String registrarContacto(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        return "Administrador/Contacto/RegistrarContacto"; // Nombre del archivo jsp
    }

    /**
     * M�todo que retorna una pagina con todas los contactos en el sistema.
     *
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina principal de contenidos.
     */
    @GetMapping("/contactos") // Base
    public String index(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se cargan los contactos para poder mostrarlos en el cuadro.
        model.addAttribute("contactos", contactoDao.getContactos());
        return "Administrador/Contacto/Contactos"; // Nombre del archivo jsp
    }

    /**
     * M�todo que permite guardar contacto
     *
     * @param contacto Objeto con la informaci�n a guardar
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina a donde debe redireccionar despu�s de la acci�n.
     */
    @PostMapping(value = "/guardarContacto")
    public String guardarContacto(@ModelAttribute("contacto") Contacto contacto, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (contacto.isValidoParaRegistrar()) {
            // Se registra la informaci�n.
            String mensaje = contactoDao.registrarConacto(contacto);
            // Se valida si el registro en la BD fue exitoso.
            if (mensaje.equals("Registro exitoso")) {
                // Se muestra el mensaje de �xito.
                model.addAttribute("result", "Contacto registrado con �xito.");
                // Se carga la informaci�n de los contactos para que la nueva informaci�n se refleje en la tabla.
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
     * M�todo que obtiene la pagina de actualizar contacto dado un ID.
     *
     * @param idContacto Identificador del contacto
     * @param model Objeto para enviar informaci�n a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina con la informaci�n del contacto cargada.
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
        // Se obtiene la informaci�n del contacto de acuerdo al id.
        Contacto contacto = contactoDao.obtenerContactoPorId(idContacto);
        // Se carga la informaci�n del contacto obtenida.
        model.addAttribute("contacto", contacto);
        return "Administrador/Contacto/ActualizarContacto"; // Nombre del archivo jsp
    }

    /**
     * M�todo que permite editar un contacto.
     *
     * @param contacto Objeto con la informaci�n a editar.
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina a donde debe redireccionar despu�s de la acci�n.
     */
    @PostMapping(value = "/editarContacto")
    public String editarContacto(@ModelAttribute("contacto") Contacto contacto, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (contacto.isValidoParaRegistrar()) {
            // Se edita la informaci�n.
            String mensaje = contactoDao.editarContacto(contacto);
            // Se valida si la informaci�n fue editada.
            if (mensaje.equals("Actualizacion exitosa")) {
                // Se muestra el mensaje de �xito.
                model.addAttribute("result", "Contacto actualizado con �xito.");
                // Se carga la informaci�n de las categor�as para que se refleje la informaci�n actualizada.
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
     * M�todo que obtiene la pagina de eliminar contacto dado un ID.
     *
     * @param idContacto Identificador de contacto
     * @param model Objeto para enviar informaci�n a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina con la informaci�n del contacto cargada.
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
        // Se obtiene la informaci�n del contacto de acuerdo al id.
        Contacto contacto = contactoDao.obtenerContactoPorId(idContacto);
        // Se carga la informaci�n del contacto obtenida.
        model.addAttribute("contacto", contacto);
        return "Administrador/Contacto/EliminarContacto"; // Nombre del archivo jsp
    }

    /**
     * M�todo que permite eliminar un contacto.
     *
     * @param contacto Objeto con la informaci�n a eliminar.
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina a donde debe redireccionar despu�s de la acci�n.
     */
    @PostMapping(value = "/borrarContacto")
    public String borrarContacto(@ModelAttribute("contacto") Contacto contacto, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se elimina la informaci�n.
        String mensaje = contactoDao.eliminarContacto(contacto);
        // Se valida si la informaci�n fue eliminada.
        if (mensaje.equals("Eliminacion exitosa")) {
            // Se muestra el mensaje de �xito.
            model.addAttribute("result", "Contacto eliminado con �xito.");
            // Se carga la informaci�n de los contactos para que se refleje la informaci�n actualizada.
            model.addAttribute("contactos", contactoDao.getContactos());
            return "Administrador/Contacto/Contactos"; // Nombre del archivo jsp
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", mensaje);
            return "Administrador/Contacto/EliminarContacto";
        }
    }

}
