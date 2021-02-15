package co.ufps.edu.controller;

import co.ufps.edu.config.SessionManager;
import co.ufps.edu.dao.AuditoriaDao;
import co.ufps.edu.dto.Auditoria;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Controlador de auditorias. Las auditorias son registros de las actividades
 * que se realizan en la aplicacion, por ejemplo actualización, edición o
 * eliminación.
 *
 * Todos los servicios publicados en esta clase serán expuestos para ser
 * consumidos por los archivos JSP.
 *
 * La etiqueta @Controller escanea todos los servicios para publicarlos segun el
 * tipo de metodo HTTP.
 *
 * @author UFPS
 */
@Controller
public class AuditoriaController {

    private AuditoriaDao auditoriaDao;
    private AdminController adminController;
    private SessionManager sessionManager;

    /**
     * Constructor de la clase en donde se inicializan las variables.
     */
    public AuditoriaController() throws Exception {
        auditoriaDao = new AuditoriaDao();
        this.adminController = new AdminController();
    }

    /**
     * Modelo con el que se realizara el formulario.
     *
     * @return Un objeto para ser llenado desde el archivo JSP.
     */
    @ModelAttribute("autitoria")
    public Auditoria setUpUserForm() {
        return new Auditoria();
    }

    /**
     * Método que retorna las auditorias registradas en la BD de acuerdo a una
     * tabla.
     *
     * @param tabla Nombre de la tabla a la que se desea consultar la auditoria.
     * @param model Modelo con la información necesaria para transportar a los
     * archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     *
     * @return La página principal de contenidos.
     */
    @GetMapping(value = "/consultarAuditorias") // Base
    public String consultarAuditorias(@ModelAttribute("tabla") String tabla, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        if (this.adminController.mostrarRol(model, request).equalsIgnoreCase("SuperAdmin")) {
            // Se cargan las auditorias para poder mostrarlas en el cuadro.
            model.addAttribute("auditorias", auditoriaDao.getAuditoriasPorTabla(tabla));
            return "Administrador/Auditoria/ConsultarAuditorias"; // Nombre del archivo jsp
        } else {
            return "Administrador/indexAdmin";
        }

    }
}
