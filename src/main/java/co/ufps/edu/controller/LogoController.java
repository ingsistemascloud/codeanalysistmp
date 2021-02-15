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
 * Todos los servicios publicados en esta clase serán expuestos para ser 
 * consumidos por los archivos JSP.
 *
 * La etiqueta @Controller escanea todos los servicios para publicarlos según el
 * tipo de método HTTP.
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
     * Método que retorna una página con los logos registrados en el sistema.
     *
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página principal de logos.
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
     * Método que permite guardar un logo.
     *
     * @param logo Objeto con la información a guardar.
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/guardarLogo")
    public String guardarLogoHorizontal(@ModelAttribute("logo") Logo logo, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Si el identificador es 0 es porque se va a registrar el logo, si no es 0 es porque se va a actualizar.
        if (logo.getId() == 0) {
            // Se inserta el logo.
            String mensaje = logoDao.insertarLogo(logo);
            // Se valida el resultado de la acción.
            if (mensaje.equals("Registro exitoso")) {
                // Se muestra el mensaje de exito.
                model.addAttribute("result", "Logo registrado con éxito.");
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", mensaje);
            }
        } else {
            // Se actualiza el logo.
            String mensaje = logoDao.actualizarLogo(logo);
            // Se valida el resultado de la acción.
            if (mensaje.equals("Actualizacion exitosa")) {
                // Se muestra el mensaje de exito.
                model.addAttribute("result", "Logo actualizado con éxito.");
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", mensaje);
            }
        }
        return index(model,request);

    }
}
