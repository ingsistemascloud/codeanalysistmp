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
 * Controlador de galerías. Las galerias son contenedores de imagenes.
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
     * Método que retorna una página con todas las galerías en el sistema.
     * 
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página principal de galerías.
     */
    @GetMapping("/galerias") // Base
    public String index(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se cargan las galerías para poder mostrarlas en el cuadro.
        model.addAttribute("galerias", galeriaDao.getGalerias());
        return "Administrador/Galeria/Galerias"; // Nombre del archivo jsp
    }

    /**
     * Método que retorna una página para realizar el registro de galería.
     * 
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página de registro de galerías.
     */
    @GetMapping("/registrarGaleria") // Base
    public String registrarGaleria(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        return "Administrador/Galeria/RegistrarGaleria"; // Nombre del archivo jsp
    }

    /**
     * Método que permite guardar galerías.
     *
     * @param galeria Objeto con la información a guardar.
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return Objeto Entity con la respuesta del sistema.
     */
    @PostMapping(value = "servicios/guardarGaleria")
    public @ResponseBody
    ResponseEntity<String> guardarGaleria(@RequestBody Galeria galeria, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (galeria.isValidoParaRegistrar()) {
            // Se registra la información.
            String mensaje = galeriaDao.registrarGaleria(galeria);
            // Se valida si el registro en la BD fue exitoso.
            if (mensaje.equals("Registro exitoso")) {
                // Se muestra el mensaje de éxito.
                return new ResponseEntity<String>("Registrada", HttpStatus.OK);
            } else {
                // Se muestra el mensaje de error.
                return new ResponseEntity<String>("Galería no registrada.", HttpStatus.OK);
            }
        } else {
            // Se muestra el mensaje de error.
            return new ResponseEntity<String>("Campos no válidos.", HttpStatus.OK);
        }
    }

    /**
     * Método que obtiene la página de actualizar galería dado un ID.
     *
     * @param idGaleria Identificador de la galería.
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página con la información de la galería cargada.
     */
    @GetMapping(value = "/actualizarGaleria")
    public String actualizarGaleria(@RequestParam("id") long idGaleria, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta que el Id sea mayor a 0.
        if (idGaleria <= 0) {
            // Se cargan las galerías para poder mostrarlas en el cuadro.
            model.addAttribute("galerias", galeriaDao.getGalerias());
            return "Administrador/Galeria/Galerias"; // Nombre del archivo jsp
        }
        // Se obtiene la información de las galerías de acuerdo al id.
        Galeria galeria = galeriaDao.obtenerGaleriaPorId(idGaleria);
        // Se carga la información de las galerías obtenidas.
        model.addAttribute("galeria", galeria);
        return "Administrador/Galeria/ActualizarGaleria"; // Nombre del archivo jsp
    }

    /**
     * Método que obtiene las fotos de las galerías de acuerdo al id.
     *
     * @param id Identificador de la galería.
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
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
     * Método que permite editar una galería.
     *
     * @param galeria Objeto con la información a editar.
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return Objeto Entity con la respuesta del sistema.
     */
    @PostMapping(value = "servicios/editarGaleria")
    public @ResponseBody
    ResponseEntity<String> editarGaleria(@RequestBody Galeria galeria, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (galeria.isValidoParaRegistrar()) {
            // Se edita la información.
            String mensaje = galeriaDao.editarGaleria(galeria);
            // Se valida si la información fue editada.
            if (mensaje.equals("Actualizacion exitosa")) {
                // Se muestra el mensaje de éxito.
                return new ResponseEntity<String>("Actualizada", HttpStatus.OK);
            } else {
                // Se muestra el mensaje de error.
                return new ResponseEntity<String>("Galería no actualizada.", HttpStatus.OK);
            }
        } else {
            // Se muestra el mensaje de error.
            return new ResponseEntity<String>("Campos no validos.", HttpStatus.OK);
        }
    }

    /**
     * Método que obtiene la página de eliminar galería dado un ID.
     *
     * @param idGaleria Identificador de la galería
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página con la información de la galería cargada.
     */
    @GetMapping(value = "/eliminarGaleria")
    public String eliminarGaleria(@RequestParam("id") long idGaleria, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta que el Id sea mayor a 0.
        if (idGaleria <= 0) {
            // Se cargan las galerías para poder mostrarlas en el cuadro.
            model.addAttribute("galerias", galeriaDao.getGalerias());
            return "Administrador/Galeria/Galerias"; // Nombre del archivo jsp
        }
        // Se obtiene la información de la galería de acuerdo al id.
        Galeria galeria = galeriaDao.obtenerGaleriaPorId(idGaleria);
        // Se carga la información de la galería obtenida.
        model.addAttribute("galeria", galeria);
        return "Administrador/Galeria/EliminarGaleria"; // Nombre del archivo jsp
    }

    /**
     * Método que permite eliminar una galería.
     *
     * @param galeria Objeto con la información a eliminar.
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/borrarGaleria")
    public String borrarGaleria(@ModelAttribute("galeria") Galeria galeria, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se elimina la información.
        String mensaje = galeriaDao.eliminarGaleria(galeria);
        // Se valida si la información fue eliminada.
        if (mensaje.equals("Eliminacion exitosa")) {
            // Se muestra el mensaje de éxito.
            model.addAttribute("eliminar", "Galeria eliminada con éxito.");
            // Se carga la información de las galerías para que se refleje la información actualizada.
            model.addAttribute("galerias", galeriaDao.getGalerias());
            return "Administrador/Galeria/Galerias"; // Nombre del archivo jsp
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", mensaje);
            return "Administrador/Galeria/EliminarGaleria";
        }
    }
}
