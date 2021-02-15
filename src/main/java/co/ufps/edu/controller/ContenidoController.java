package co.ufps.edu.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import co.ufps.edu.constantes.Constantes;
import co.ufps.edu.dao.ContenidoDao;
import co.ufps.edu.dao.TipoContenidoDao;
import co.ufps.edu.dto.Archivo;
import co.ufps.edu.dto.Contenido;
import javax.servlet.http.HttpServletRequest;

/**
 * Controlador de contenidos. Los contenidos son las páginas que se abren cuando
 * se da click a una información.
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
public class ContenidoController {

    @Autowired
    private ContenidoDao contenidoDao;
    @Autowired
    private TipoContenidoDao tipoContenidoDao;
    private AdminController adminController;

    /**
     * Constructor de la clase en donde se inicializan las variables
     */
    public ContenidoController() throws Exception {
        this.adminController = new AdminController();
    }

    /**
     * Método que retorna una página con todos los contenidos en el sistema.
     *
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página principal de contenidos.
     */
    @GetMapping("/contenidos") // Base
    public String index(Model model, HttpServletRequest request) throws Exception {
        // Cargamos los contenidos para poder mostrarlas en el cuadro.
        this.adminController.mostrarSuperAdmin(model, request);
        model.addAttribute("contenidos", contenidoDao.getContenidos());
        return "Administrador/Contenido/Contenidos"; // Nombre del archivo jsp
    }

    /**
     * Modelo con el que se realizara el formulario
     *
     * @return Un objeto para ser llenado desde el archivo .JSP
     */
    @ModelAttribute("contenido")
    public Contenido setUpUserForm() {
        return new Contenido();
    }

    /**
     * Método que retorna una página para realizar el registro de un contenido.
     *
     * @param model Modelo con la información necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página de registro de contenidos.
     */
    @GetMapping("/registrarContenido") // Base
    public String registrarContenido(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se obtienen las asociaciones de los contenidos y se cargan en el modelo.
        model.addAttribute("tiposAsociacion", getAsosiaciones());
        // Se obtienen los contenidos y se cargan en el modelo.
        model.addAttribute("tiposContenido", tipoContenidoDao.getContenidos());
        return "Administrador/Contenido/RegistrarContenido"; // Nombre del archivo jsp.
    }

    /**
     * Método en donde se almacenan las asociaciones existentes.
     *
     * @return Todas las asociaciones existentes.
     */
    private Map<String, String> getAsosiaciones() {
        Map<String, String> mapaDeAsosiaciones = new HashMap<>();
        mapaDeAsosiaciones.put(Constantes.ITEMSUBCATEGORIA, "ItemSubcategoria");
        mapaDeAsosiaciones.put(Constantes.CATEGORIA, "Categoria");
        mapaDeAsosiaciones.put(Constantes.SUBCATEGORIA, "Subcategoria");
        mapaDeAsosiaciones.put(Constantes.NOTICIA, "Noticia");
        mapaDeAsosiaciones.put(Constantes.NOVEDAD, "Novedad");
        mapaDeAsosiaciones.put(Constantes.ACTIVIDAD, "Actividad");
        return mapaDeAsosiaciones;
    }

    /**
     * Método que obtiene la página de actualizar contenido dado un ID.
     *
     * @param idContenido Identificador del contenido
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página con la información del contenido cargada.
     */
    @GetMapping(value = "/actualizarContenido")
    public String actualizarContenido(@RequestParam("id") long idContenido, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta que el Id sea mayor a 0.
        if (idContenido <= 0) {
            // Se cargan los contenidos para poder mostrarlos en el cuadro.
            model.addAttribute("contenidos", contenidoDao.getContenidos());
            return "Administrador/Contenido/Contenidos"; // Nombre del archivo jsp
        }
        // Se obtiene la información del contenido de acuerdo al id.
        Contenido contenido = contenidoDao.obtenerContenidoPorId(idContenido);
        // Se obtiene el tipo de contenido.
        if (contenido.getTipoContenido().getId() == 2) {
            contenido.setUrl(contenido.getContenido());
            contenido.setContenido("");
        }
        // Se obtienen las asociaciones y se cargan en el modelo.
        model.addAttribute("tiposAsociacion", getAsosiaciones());
        // Se obtienen los tipos de contenidos y se cargan en el modelo.
        model.addAttribute("tiposContenido", tipoContenidoDao.getContenidos());
        // Se obtienen los contenidos y se cargan en el modelo.
        model.addAttribute("contenido", contenido);
        return "Administrador/Contenido/ActualizarContenido"; // Nombre del archivo jsp
    }

    /**
     * Método que obtiene las asociaciones dependiendo del tipo suministrado.
     * 
     * @param tipoAsociacion Identificador del tipo.
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return Objeto Entity con la respuesta del sistema.
     */
    @PostMapping(value = "servicios/asosiaciones")
    public @ResponseBody
    ResponseEntity<Map<Integer, String>> getAsosiacionesPorTipo(
            @RequestBody String tipoAsociacion, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        Map<Integer, String> asociaciones = contenidoDao.getAsociaciones(tipoAsociacion);
        return new ResponseEntity<Map<Integer, String>>(asociaciones, HttpStatus.OK);
    }

    /**
     * Método que obtiene las asociaciones por tipo.
     * 
     * @param tipoAsociacion Identificador del tipo.
     * @param asociacion La asociación del contenido.
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return Objeto Entity con la respuesta del sistema.
     */
    @PostMapping(value = "servicios/asosiacionesCompletas")
    public @ResponseBody
    ResponseEntity<Map<Integer, String>> getAsosiacionesPorTipoCompletas(
            @RequestParam("tipoAsociacion") String tipoAsociacion, @RequestParam("asociacion") long asociacion, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        Map<Integer, String> asociaciones = contenidoDao.getAsociacionesCompletas(tipoAsociacion, asociacion);
        return new ResponseEntity<Map<Integer, String>>(asociaciones, HttpStatus.OK);
    }

    /**
     * Método que permite registrar contenido.
     * 
     * @param contenido Objeto con la información del contenido.
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return Objeto Entity con la respuesta del sistema.
     */
    @PostMapping(value = "servicios/recibirInformacion")
    public @ResponseBody
    ResponseEntity<String> recibirInformacion(@RequestBody Contenido contenido, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (contenido.isValidoParaRegistrar()) {
            String mensaje = contenidoDao.registrarContenido(contenido);
            // Se valida si el registro en la BD fue exitoso.
            if (mensaje.equals("Registro exitoso")) {
                // Se muestra el mensaje de éxito.
                return new ResponseEntity<String>("Registrado", HttpStatus.OK);
            } else {
                // Se muestra el mensaje de error.
                return new ResponseEntity<String>("Registro no exitoso.", HttpStatus.OK);
            }
        } else {
            // Se muestra el mensaje de error.
            return new ResponseEntity<String>("Campos no válidos.", HttpStatus.OK);
        }

    }

    /**
     * Método que permite actualizar contenido.
     * 
     * @param contenido Objeto con la información del contenido.
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return Objeto Entity con la respuesta del sistema.
     */
    @PostMapping(value = "servicios/actualizarInformacion")
    public @ResponseBody
    ResponseEntity<String> actualizarInformacion(@RequestBody Contenido contenido, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (contenido.isValidoParaRegistrar()) {
            // Se actualiza la información.
            String mensaje = contenidoDao.actualizarContenido(contenido);
            // Se valida si la información fue editada.
            if (mensaje.equals("Actualizacion exitosa")) {
                // Se muestra el mensaje de éxito.
                return new ResponseEntity<String>("Actualizado", HttpStatus.OK);
            } else {
                // Se muestra el mensaje de error.
                return new ResponseEntity<String>("Actualización no exitosa.", HttpStatus.OK);
            }
        } else {
            // Se muestra el mensaje de error.
            return new ResponseEntity<String>("Campos no válidos.", HttpStatus.OK);
        }

    }

    /**
     * Método que permite registrar archivo.
     * 
     * @param multipartFile OBjeto que representa de un archivo cargado recibido en una solicitud multiparte.
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return Objeto Entity con la respuesta del sistema.
     */
    @PostMapping(value = "servicios/registrarArchivo")
    public @ResponseBody
    ResponseEntity<String> registrarArchivo(@RequestParam("archivo") MultipartFile multipartFile, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se registra el archivo.
        long id = contenidoDao.registrarArchivo(multipartFile);
        return new ResponseEntity<String>(String.valueOf(id), HttpStatus.OK);
    }

    /**
     * Método que permite solicitar imagenes.
     * 
     * @param tipo Tipo de imagen.
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return Objeto Entity con la respuesta del sistema.
     */
    @PostMapping(value = "servicios/solicitarImagen")
    public @ResponseBody
    ResponseEntity<String> solicitarImagen(@RequestParam("tipo") String tipo, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se solicita la imagen de acuerdo al tipo.
        String imagenBase64 = contenidoDao.solicitarImagen(tipo);
        // Se valida si la imagen es vacia.
        if (StringUtils.isEmpty(imagenBase64)) {
            imagenBase64 = contenidoDao.solicitarImagen("RecibirImagenCualquiera");
        }
        return new ResponseEntity<String>(imagenBase64, HttpStatus.OK);
    }

    /**
     * Método que permite descargar los archivos.
     * 
     * @param id Identficador del archivo a descargar. 
     * @param model Objeto para enviar información a los archivos JSP.
     * @param response Objeto de la respuesta de la peticion HTTP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     */
    @GetMapping(value = "servicios/download")
    public void download(@RequestParam("id") long id, HttpServletResponse response, Model model, HttpServletRequest request) throws Exception {
        //this.adminController.mostrarSuperAdmin(model, request);
        Archivo archivo = contenidoDao.obtenerArchivo(id);
        InputStream is = archivo.getContenido();
        try {
            response.setContentType(archivo.getTipo());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + archivo.getNombre() + "\"");
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

    /**
     * Método que permite ver los archivos.
     * 
     * @param id Identficador del archivo a descargar. 
     * @param model Objeto para enviar información a los archivos JSP.
     * @param response Objeto de la respuesta de la peticion HTTP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return Objeto Entity con la respuesta del sistema.
     */
    @GetMapping(value = "servicios/visor")
    public ResponseEntity<byte[]> visor(@RequestParam("id") long id, HttpServletResponse response, Model model, HttpServletRequest request) throws Exception {
      
        Archivo archivo = contenidoDao.obtenerArchivo(id);
        InputStream bos = archivo.getContenido();
        int tamanoInput = bos.available();
        byte[] datosPDF = new byte[tamanoInput];
        bos.read(datosPDF, 0, tamanoInput);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=\"" + archivo.getNombre() + "\"");        
        response.getOutputStream().write(datosPDF);
        return new ResponseEntity<byte[]>(datosPDF, HttpStatus.OK);
    }

    /**
     * Método que permite solicitar imagenes.
     * 
     * @param tipo Tipo de imagen.
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return Objeto Entity con la respuesta del sistema.
     */
    @GetMapping(value = "servicios/solicitarImage")
    public @ResponseBody
    ResponseEntity<String> solicitarImagen2(@RequestParam("tipo") String tipo, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        String imagenBase64 = contenidoDao.solicitarImagen(tipo);
        if (StringUtils.isEmpty(imagenBase64)) {
            imagenBase64 = contenidoDao.solicitarImagen("RecibirImagenCualquiera");
        }
        return new ResponseEntity<String>(imagenBase64, HttpStatus.OK);
    }

    /**
     * Método que obtiene la página de eliminar contenido dado un ID.
     *
     * @param idContenido Identificador del contenido.
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página con la información del contenido cargado.
     */
    @GetMapping(value = "/eliminarContenido")
    public String eliminarContenido(@RequestParam("id") long idContenido, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta que el Id sea mayor a 0.
        if (idContenido <= 0) {
            // Se cargan los contenidos para poder mostrarlos en el cuadro.
            model.addAttribute("contenidos", contenidoDao.getContenidos());
            return "Administrador/Contenido/Contenidos"; // Nombre del archivo jsp
        }
        // Se obtiene la información del conteido de acuerdo al id.
        Contenido contenido = contenidoDao.obtenerContenidoPorId(idContenido);
        // Se carga la información del contacto obtenida.
        model.addAttribute("contenido", contenido);
        // Se obtienen las asociaciones del conteido.
        Map<Integer, String> mapa = contenidoDao.getAsociacionesCompletas(contenido.getTipoAsociacion(), contenido.getAsociacion());
        // Se carga la información de las asociaciones obtenidas.
        model.addAttribute("asociacion", mapa.get(Integer.parseInt(String.valueOf(contenido.getAsociacion()))));
        return "Administrador/Contenido/EliminarContenido"; // Nombre del archivo jsp
    }

    /**
     * Método que permite eliminar un contenido.
     *
     * @param contenido Objeto con la información de contenido a borrar.
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     * 
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/borrarContenido")
    public String borrarContenido(@ModelAttribute("contenido") Contenido contenido, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se elimina la información.
        String mensaje = contenidoDao.eliminarContenido(contenido);
        // Se valida si la información fue eliminada.
        if (mensaje.equals("Eliminacion exitosa")) {
            // Se muestra el mensaje de éxito.
            model.addAttribute("eliminar", "Contenido eliminado con éxito.");
            // Se carga la información de los contactos para que se refleje la información actualizada.
            model.addAttribute("contenidos", contenidoDao.getContenidos());
            return "Administrador/Contenido/Contenidos"; // Nombre del archivo jsp
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", mensaje);
            return "Administrador/Contenido/EliminarContenido"; // Nombre del archivo jsp
        }
    }
}
