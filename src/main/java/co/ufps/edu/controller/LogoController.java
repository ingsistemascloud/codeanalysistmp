package co.ufps.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import co.ufps.edu.dao.LogoDao;
import co.ufps.edu.dto.Logo;
import javax.servlet.http.HttpServletRequest;

/**
 * Controlador de logos. Los logos son las imagenes que se observan en el footer. 
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
public class LogoController {

    @Autowired
    private LogoDao logoDao;
    private AdminController adminController;

    /**
     * Constructor de la clase en donde se inicializan las variables
     */
    public LogoController() throws Exception {
        this.adminController = new AdminController();
    }
    
    /**
     * M�todo que retorna una p�gina con los logos registrados en el sistema.
     *
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina principal de logos.
     */
    @GetMapping("/logos") // Base
    public String index(Model model, HttpServletRequest request) throws Exception {
        // Se cargan los logos para poder mostrarlos en el cuadro.
        this.adminController.mostrarSuperAdmin(model, request);
        model.addAttribute("logoHorizontal", logoDao.getLogo("LogoHorizontal"));
        model.addAttribute("logoVertical", logoDao.getLogo("LogoVertical"));
        return "Administrador/Logo/logos"; // Nombre del archivo jsp
    }

    /**
     * Modelo con el que se realizara el formulario
     *
     * @return Un objeto para ser llenado desde el archivo .JSP
     */
    @ModelAttribute("logo")
    public Logo setUpUserForm() {
        return new Logo();
    }

    /**
     * M�todo que permite guardar un logo.
     *
     * @param logo Objeto con la informaci�n a guardar.
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina a donde debe redireccionar despu�s de la acci�n.
     */
    @PostMapping(value = "/guardarLogo")
    public String guardarLogoHorizontal(@ModelAttribute("logo") Logo logo, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Si el identificador es 0 es porque se va a registrar el logo, si no es 0 es porque se va a actualizar.
        if (logo.getId() == 0) {
            // Se inserta el logo.
            String mensaje = logoDao.insertarLogo(logo);
            // Se valida el resultado de la acci�n.
            if (mensaje.equals("Registro exitoso")) {
                // Se muestra el mensaje de exito.
                model.addAttribute("result", "Logo registrado con �xito.");
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", mensaje);
            }
        } else {
            // Se actualiza el logo.
            String mensaje = logoDao.actualizarLogo(logo);
            // Se valida el resultado de la acci�n.
            if (mensaje.equals("Actualizacion exitosa")) {
                // Se muestra el mensaje de exito.
                model.addAttribute("result", "Logo actualizado con �xito.");
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", mensaje);
            }
        }
        return index(model,request);

    }
}
