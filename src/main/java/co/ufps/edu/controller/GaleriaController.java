package co.ufps.edu.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import co.ufps.edu.dao.GaleriaDao;
import co.ufps.edu.dto.Galeria;
import co.ufps.edu.dto.Imagen;
import javax.servlet.http.HttpServletRequest;

/**
 * Controlador de galer�as. Las galerias son contenedores de imagenes.
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
public class GaleriaController {

    @Autowired
    private GaleriaDao galeriaDao;
    private AdminController adminController;

    /**
     * Constructor de la clase en donde se inicializan las variables
     */
    public GaleriaController() throws Exception {
        this.adminController = new AdminController();
    }
    
    /**
     * Modelo con el que se realizara el formulario
     *
     * @return Un objeto para ser llenado desde el archivo .JSP
     */
    @ModelAttribute("galeria")
    public Galeria setUpUserForm() {
        return new Galeria();
    }

    /**
     * M�todo que retorna una p�gina con todas las galer�as en el sistema.
     * 
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina principal de galer�as.
     */
    @GetMapping("/galerias") // Base
    public String index(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se cargan las galer�as para poder mostrarlas en el cuadro.
        model.addAttribute("galerias", galeriaDao.getGalerias());
        return "Administrador/Galeria/Galerias"; // Nombre del archivo jsp
    }

    /**
     * M�todo que retorna una p�gina para realizar el registro de galer�a.
     * 
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina de registro de galer�as.
     */
    @GetMapping("/registrarGaleria") // Base
    public String registrarGaleria(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        return "Administrador/Galeria/RegistrarGaleria"; // Nombre del archivo jsp
    }

    /**
     * M�todo que permite guardar galer�as.
     *
     * @param galeria Objeto con la informaci�n a guardar.
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return Objeto Entity con la respuesta del sistema.
     */
    @PostMapping(value = "servicios/guardarGaleria")
    public @ResponseBody
    ResponseEntity<String> guardarGaleria(@RequestBody Galeria galeria, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (galeria.isValidoParaRegistrar()) {
            // Se registra la informaci�n.
            String mensaje = galeriaDao.registrarGaleria(galeria);
            // Se valida si el registro en la BD fue exitoso.
            if (mensaje.equals("Registro exitoso")) {
                // Se muestra el mensaje de �xito.
                return new ResponseEntity<String>("Registrada", HttpStatus.OK);
            } else {
                // Se muestra el mensaje de error.
                return new ResponseEntity<String>("Galer�a no registrada.", HttpStatus.OK);
            }
        } else {
            // Se muestra el mensaje de error.
            return new ResponseEntity<String>("Campos no v�lidos.", HttpStatus.OK);
        }
    }

    /**
     * M�todo que obtiene la p�gina de actualizar galer�a dado un ID.
     *
     * @param idGaleria Identificador de la galer�a.
     * @param model Objeto para enviar informaci�n a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina con la informaci�n de la galer�a cargada.
     */
    @GetMapping(value = "/actualizarGaleria")
    public String actualizarGaleria(@RequestParam("id") long idGaleria, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta que el Id sea mayor a 0.
        if (idGaleria <= 0) {
            // Se cargan las galer�as para poder mostrarlas en el cuadro.
            model.addAttribute("galerias", galeriaDao.getGalerias());
            return "Administrador/Galeria/Galerias"; // Nombre del archivo jsp
        }
        // Se obtiene la informaci�n de las galer�as de acuerdo al id.
        Galeria galeria = galeriaDao.obtenerGaleriaPorId(idGaleria);
        // Se carga la informaci�n de las galer�as obtenidas.
        model.addAttribute("galeria", galeria);
        return "Administrador/Galeria/ActualizarGaleria"; // Nombre del archivo jsp
    }

    /**
     * M�todo que obtiene las fotos de las galer�as de acuerdo al id.
     *
     * @param id Identificador de la galer�a.
     * @param model Objeto para enviar informaci�n a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return Objeto Entity con la respuesta del sistema.
     */
    @PostMapping(value = "servicios/obtenerFotos")
    public @ResponseBody
    ResponseEntity<List<Imagen>> getAsosiacionesPorTipoCompletas(
            @RequestParam("id") long id, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        List<Imagen> imagenes = galeriaDao.getImagenesPorIDCompletas(id);
        return new ResponseEntity<List<Imagen>>(imagenes, HttpStatus.OK);
    }

    /**
     * M�todo que permite editar una galer�a.
     *
     * @param galeria Objeto con la informaci�n a editar.
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return Objeto Entity con la respuesta del sistema.
     */
    @PostMapping(value = "servicios/editarGaleria")
    public @ResponseBody
    ResponseEntity<String> editarGaleria(@RequestBody Galeria galeria, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (galeria.isValidoParaRegistrar()) {
            // Se edita la informaci�n.
            String mensaje = galeriaDao.editarGaleria(galeria);
            // Se valida si la informaci�n fue editada.
            if (mensaje.equals("Actualizacion exitosa")) {
                // Se muestra el mensaje de �xito.
                return new ResponseEntity<String>("Actualizada", HttpStatus.OK);
            } else {
                // Se muestra el mensaje de error.
                return new ResponseEntity<String>("Galer�a no actualizada.", HttpStatus.OK);
            }
        } else {
            // Se muestra el mensaje de error.
            return new ResponseEntity<String>("Campos no validos.", HttpStatus.OK);
        }
    }

    /**
     * M�todo que obtiene la p�gina de eliminar galer�a dado un ID.
     *
     * @param idGaleria Identificador de la galer�a
     * @param model Objeto para enviar informaci�n a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina con la informaci�n de la galer�a cargada.
     */
    @GetMapping(value = "/eliminarGaleria")
    public String eliminarGaleria(@RequestParam("id") long idGaleria, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta que el Id sea mayor a 0.
        if (idGaleria <= 0) {
            // Se cargan las galer�as para poder mostrarlas en el cuadro.
            model.addAttribute("galerias", galeriaDao.getGalerias());
            return "Administrador/Galeria/Galerias"; // Nombre del archivo jsp
        }
        // Se obtiene la informaci�n de la galer�a de acuerdo al id.
        Galeria galeria = galeriaDao.obtenerGaleriaPorId(idGaleria);
        // Se carga la informaci�n de la galer�a obtenida.
        model.addAttribute("galeria", galeria);
        return "Administrador/Galeria/EliminarGaleria"; // Nombre del archivo jsp
    }

    /**
     * M�todo que permite eliminar una galer�a.
     *
     * @param galeria Objeto con la informaci�n a eliminar.
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina a donde debe redireccionar despu�s de la acci�n.
     */
    @PostMapping(value = "/borrarGaleria")
    public String borrarGaleria(@ModelAttribute("galeria") Galeria galeria, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se elimina la informaci�n.
        String mensaje = galeriaDao.eliminarGaleria(galeria);
        // Se valida si la informaci�n fue eliminada.
        if (mensaje.equals("Eliminacion exitosa")) {
            // Se muestra el mensaje de �xito.
            model.addAttribute("eliminar", "Galeria eliminada con �xito.");
            // Se carga la informaci�n de las galer�as para que se refleje la informaci�n actualizada.
            model.addAttribute("galerias", galeriaDao.getGalerias());
            return "Administrador/Galeria/Galerias"; // Nombre del archivo jsp
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", mensaje);
            return "Administrador/Galeria/EliminarGaleria";
        }
    }
}
