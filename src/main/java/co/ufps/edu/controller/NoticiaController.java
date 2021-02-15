package co.ufps.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import co.ufps.edu.constantes.Constantes;
import co.ufps.edu.dao.ContenidoDao;
import co.ufps.edu.dao.NoticiaDao;
import co.ufps.edu.dto.Noticia;
import javax.servlet.http.HttpServletRequest;

/**
 * Controlador de noticias. Las noticias son publicaciones que permiten informar
 * a las personas de los sucesos que acontecen.
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
public class NoticiaController {

    @Autowired
    private NoticiaDao noticiaDao;
    @Autowired
    private ContenidoDao contenidoDao;
    private AdminController adminController;
    
    /**
     * Constructor de la clase en donde se inicializan las variables
     */
    public NoticiaController() throws Exception {
        this.adminController = new AdminController();
    }
    
    /**
     * M�todo que retorna una p�gina con todas las noticias en el sistema.
     *
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina principal de noticias.
     */
    @GetMapping("/noticias") // Base
    public String index(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se cargan las noticias para poder mostrarlas en el cuadro.
        model.addAttribute("noticias", noticiaDao.getNoticias());
        return "Administrador/Noticia/Noticias"; // Nombre del archivo jsp
    }

    /**
     * Modelo con el que se realizara el formulario.
     *
     * @return Un objeto para ser llenado desde el archivo .JSP
     */
    @ModelAttribute("noticia")
    public Noticia setUpUserForm() {
        return new Noticia();
    }

    /**
     * M�todo que retorna una p�gina para realizar el registro de una noticia.
     *
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina de registro de noticias.
     */
    @GetMapping("/registrarNoticia") // Base
    public String registrarNoticia(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        return "Administrador/Noticia/RegistrarNoticia"; // Nombre del archivo jsp
    }

    /**
     * M�todo que permite guardar una noticia.
     *
     * @param noticia Objeto con la informaci�n a guardar
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina a donde debe redireccionar despu�s de la acci�n.
     */
    @PostMapping(value = "/guardarNoticia")
    public String guardarNoticia(@ModelAttribute("noticia") Noticia noticia, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (noticia.isValidoParaRegistrar()) {
            // Se cambia el orden de las noticias.
            noticiaDao.cambiarOrden();
            // Se registra la informaci�n.
            String mensaje = noticiaDao.registrarNoticia(noticia);
            // Se valida si el registro en la BD fue exitoso.
            if (mensaje.equals("Registro exitoso")) {
                // Se muestra el mensaje de �xito.
                model.addAttribute("result", "Noticia registrada con �xito.");
                // Se cargan las noticias para poder mostrarlas en el cuadro.
                model.addAttribute("noticias", noticiaDao.getNoticias());
                return "Administrador/Noticia/Noticias"; // Nombre del archivo jsp
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", mensaje);
                return "Administrador/Noticia/RegistrarNoticia"; // Nombre del archivo jsp
            }
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", "Debe llenar todos los campos.");
            return "Administrador/Noticia/RegistrarNoticia"; // Nombre del archivo jsp
        }
    }

    /**
     * M�todo que permite bajar el n�mero de orden de una noticia
     *
     * @param idNoticia Idenrificador de la noticia.
     * @param orden Numero de orden actual.
     * @param model Donde se cargaran las categor�as actualizadas.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return El redireccionamiento a la pagina de noticias
     */
    @GetMapping(value = "/bajarOrdenNoticia")
    public String bajarOrdenDeNoticia(@RequestParam("id") long idNoticia,
            @RequestParam("orden") int orden, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se cambia el orden.
        bajarOrden(idNoticia, orden);
        // Se cargan las noticias en el model para que se refleje la informaci�n actualizada.
        model.addAttribute("noticias", noticiaDao.getNoticias());
        return "Administrador/Noticia/Noticias";
    }

    /**
     * Metodo que baja de orden una noticia.
     *
     * @param idNoticia Identificador de la noticia
     * @param orden Numero de orden
     */
    private void bajarOrden(long idNoticia, int orden) {
        // Se consulta que el Id sea mayor a 0.
        if (idNoticia <= 0) {
            return;
        }
        // Se obtiene el n�mero menor de ordenamiento.
        int ordenMaximo = noticiaDao.getUltimoNumeroDeOrden();
        // Si el n�mero de orden es el m�ximo es por que ya es el ultimo y no se debe hacer nada.
        if (orden == ordenMaximo) {
            return;
        } else {
            // Se cambia el orden.
            noticiaDao.descenderOrden(idNoticia, orden);
        }
    }

    /**
     * M�todo que permite subir el n�mero de orden de una noticia
     *
     * @param idNoticia Idenrificador de la noticia
     * @param orden Numero de orden actual
     * @param model Donde se cargaran las noticias actualizadas
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * @return El redireccionamiento a la pagina de noticias
     */
    @GetMapping(value = "/subirOrdenNoticia")
    public String subirOrdenDeNoticia(@RequestParam("id") long idNoticia,
            @RequestParam("orden") int orden, Model model, HttpServletRequest request) throws Exception {
        // Se cambia el orden.
        subirOrden(idNoticia, orden);
        this.adminController.mostrarSuperAdmin(model, request);
        // Se cargan las noticias en el model para que se refleje la informaci�n actualizada.
        model.addAttribute("noticias", noticiaDao.getNoticias());
        return "Administrador/Noticia/Noticias";
    }

    /**
     * Metodo que permite subir una noticia de orden.
     *
     * @param idNoticia Identificador de la noticia.
     * @param orden Orden de la noticia.
     */
    private void subirOrden(long idNoticia, int orden) {
        // Se consulta que el Id sea mayor a 0.
        if (idNoticia <= 0) {
            return;
        }
        // Si el numero de orden es el minimo es por que ya es el primero y no se debe hacer nada.
        if (orden == 1) {
            return;
        } else {
            // Se cambia el orden.
            noticiaDao.ascenderOrden(idNoticia, orden);
        }
    }

    /**
     * M�todo que obtiene la p�gina de actualizar noticia dado un ID.
     *
     * @param idNoticia Identificador de la noticia
     * @param model Objeto para enviar informaci�n a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La pagina con la informaci�n de la noticia cargada.
     */
    @GetMapping(value = "/actualizarNoticia")
    public String actualizarnoticia(@RequestParam("id") long idNoticia, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta que el Id sea mayor a 0.
        if (idNoticia <= 0) {
            // Se cargan las noticias para poder mostrarlas en el cuadro.
            model.addAttribute("noticias", noticiaDao.getNoticias());
            return "Administrador/Noticia/Noticias"; // Nombre del archivo jsp
        }
        // Se obtiene la informaci�n de la noticia de acuerdo al id.
        Noticia noticia = noticiaDao.obtenerNoticiaPorId(idNoticia);
        // Se carga la informaci�n de la noticia obtenida.
        model.addAttribute("noticia", noticia);
        return "Administrador/Noticia/ActualizarNoticia"; // Nombre del archivo jsp
    }

    /**
     * M�todo que permite editar una noticia.
     *
     * @param noticia Objeto con la informaci�n a editar.
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina a donde debe redireccionar despu�s de la acci�n.
     */
    @PostMapping(value = "/editarNoticia")
    public String editarNoticia(@ModelAttribute("noticia") Noticia noticia, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta si tiene todos los campos llenos.
        if (noticia.isValidoParaActualizar()) {
            // Se edita la informaci�n.
            String mensaje = noticiaDao.editarNoticia(noticia);
            // Se valida si la informaci�n fue editada.
            if (mensaje.equals("Actualizacion exitosa")) {
                // Se muestra el mensaje de �xito.
                model.addAttribute("result", "Noticia actualizada con �xito.");
                // Se carga la informaci�n de las noticias para que se refleje la informaci�n actualizada.
                model.addAttribute("noticias", noticiaDao.getNoticias());
                return "Administrador/Noticia/Noticias"; // Nombre del archivo jsp
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", mensaje);
                Noticia noti = noticiaDao.obtenerNoticiaPorId(noticia.getId());
                Noticia no = (Noticia) model.asMap().get("noticia");
                no.setIm1Base64image(noti.getIm1Base64image());
                return "Administrador/Noticia/ActualizarNoticia";
            }
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", "Debe llenar todos los campos.");
            Noticia noti = noticiaDao.obtenerNoticiaPorId(noticia.getId());
            Noticia no = (Noticia) model.asMap().get("noticia");
            no.setIm1Base64image(noti.getIm1Base64image());
            return "Administrador/Noticia/ActualizarNoticia";
        }
    }

    /**
     * M�todo que obtiene la p�gina de actualizar noticia dado un ID.
     *
     * @param idNoticia Identificador de la noticia.
     * @param model Objeto para enviar informaci�n a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La pagina con la informaci�n de la noticia cargada.
     */
    @GetMapping(value = "/eliminarNoticia")
    public String eliminarNoticia(@RequestParam("id") long idNoticia, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se consulta que el Id sea mayor a 0.
        if (idNoticia <= 0) {
            // Se cargan las noticias para poder mostrarlas en el cuadro.
            model.addAttribute("noticias", noticiaDao.getNoticias());
            return "Administrador/Noticia/Noticias"; // Nombre del archivo jsp
        }
        // Se obtiene la informaci�n de la noticia de acuerdo al id.
        Noticia noticia = noticiaDao.obtenerNoticiaPorId(idNoticia);
        // Se carga la informaci�n de la noticia obtenida.
        model.addAttribute("noticia", noticia);
        return "Administrador/Noticia/EliminarNoticia"; // Nombre del archivo jsp
    }

    /**
     * M�todo que permite eliminar una noticia.
     *
     * @param noticia Objeto con la informaci�n a eliminar.
     * @param model Modelo con la informaci�n necesaria para transportar a los archivos JSP.
     * @param request Objeto de la solicitud de la petici�n HTTP.
     * 
     * @return La p�gina a donde debe redireccionar despu�s de la acci�n.
     */
    @PostMapping(value = "/borrarNoticia")
    public String borrarNoticia(@ModelAttribute("noticia") Noticia noticia, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        // Se verifica si tiene contenido.
        if (!contenidoDao.tieneContenido(noticia.getId(), Constantes.NOTICIA)) {
            // Se elimina la informaci�n.
            String mensaje = noticiaDao.eliminarNoticia(noticia);
            // Se cambia el orden de la noticia.
            cambiarOrdenDeNoticias(noticia);
            // Se valida si la informaci�n fue eliminada.
            if (mensaje.equals("Eliminacion exitosa")) {
                // Se muestra el mensaje de �xito.
                model.addAttribute("result", "Noticia eliminada con �xito.");
                // Se carga la informaci�n de las noticias para que se refleje la informaci�n actualizada.
                model.addAttribute("noticias", noticiaDao.getNoticias());
                return "Administrador/Noticia/Noticias"; // Nombre del archivo jsp
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", mensaje);
                return "Administrador/Noticia/EliminarNoticia";
            }
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", "No es posible eliminar la noticia debido a que tiene contenido registrado.");
            return "Administrador/Noticia/EliminarNoticia";
        }

    }

    /**
     * M�todo que reordena todas las noticias dado un orden faltante.
     *
     * @param noticia Objeto con la informaci�n de la noticia borrada.
     */
    private void cambiarOrdenDeNoticias(Noticia noticia) {
        // Se obtiene el menor n�mero de ordenamiento.
        int ordenMaximo = noticiaDao.getUltimoNumeroDeOrden();
        // Se realiza el reordenamiento.
        for (int i = noticia.getOrden(); i < ordenMaximo; i++) {
            long idnoticia = noticiaDao.getIdNoticiaPorOrden(i + 1);
            noticiaDao.cambiarOrdenDeNoticia(idnoticia, i);
        }
    }

}
