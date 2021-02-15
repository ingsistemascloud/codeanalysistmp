package co.ufps.edu.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import co.ufps.edu.config.SessionManager;
import co.ufps.edu.dao.ActividadDao;
import co.ufps.edu.dao.AuditoriaDao;
import co.ufps.edu.dao.CategoriaDao;
import co.ufps.edu.dao.ComponenteDao;
import co.ufps.edu.dao.ContactoDao;
import co.ufps.edu.dao.ContenidoDao;
import co.ufps.edu.dao.EnlaceDeInteresDao;
import co.ufps.edu.dao.GaleriaDao;
import co.ufps.edu.dao.ItemSubCategoriaDao;
import co.ufps.edu.dao.LoginDao;
import co.ufps.edu.dao.LogoDao;
import co.ufps.edu.dao.NoticiaDao;
import co.ufps.edu.dao.NovedadDao;
import co.ufps.edu.dao.RedSocialDao;
import co.ufps.edu.dao.SubCategoriaDao;
import co.ufps.edu.dao.UsuarioDao;
import co.ufps.edu.dao.VisitaDao;
import co.ufps.edu.dto.Actividad;
import co.ufps.edu.dto.Contenido;
import co.ufps.edu.dto.Galeria;
import co.ufps.edu.dto.Login;
import co.ufps.edu.dto.Noticia;
import co.ufps.edu.dto.Novedad;
import co.ufps.edu.dto.Usuario;
import co.ufps.edu.dto.Visita;
import co.ufps.edu.util.FileUtil;
import co.ufps.edu.util.JwtUtil;

/**
 * Controlador de administrador. Contiene las funcionalidades para cargar el
 * modelo y parte de la gestión de usuario como autenticación, cerrar sesión,
 * etc.
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
public class AdminController {

    @Autowired
    private SessionManager sessionManager;
    private JwtUtil jwtUtil;
    private LoginDao loginDao;
    private CategoriaDao categoriaDao;
    private SubCategoriaDao subCategoriaDao;
    private ContactoDao contactoDao;
    private RedSocialDao redSocialDao;
    private ComponenteDao componenteDao;
    private EnlaceDeInteresDao enlaceDeInteresDao;
    private VisitaDao visitaDao;
    private UsuarioDao usuarioDao;
    private AuditoriaDao auditoriaDAO;
    private ItemSubCategoriaDao itemSubCategoriaDao;
    private FileUtil fileUtil;
    @Autowired
    private ContenidoDao contenidoDao;
    @Autowired
    private NoticiaDao noticiaDao;
    @Autowired
    private ActividadDao actividadDao;
    @Autowired
    private NovedadDao novedadDao;
    @Autowired
    private LogoDao logoDao;
    @Autowired
    private GaleriaDao galeriaDao;

    /**
     * Constructor de la clase para inicializar los dao.
     */
    public AdminController() throws Exception {
        jwtUtil = new JwtUtil();
        loginDao = new LoginDao();
        categoriaDao = new CategoriaDao();
        subCategoriaDao = new SubCategoriaDao();
        enlaceDeInteresDao = new EnlaceDeInteresDao();
        contactoDao = new ContactoDao();
        redSocialDao = new RedSocialDao();
        componenteDao = new ComponenteDao();
        visitaDao = new VisitaDao();
        usuarioDao = new UsuarioDao();
        auditoriaDAO = new AuditoriaDao();
        itemSubCategoriaDao = new ItemSubCategoriaDao();
        fileUtil = new FileUtil();
    }

    /**
     * Método principal para cargar el modelo y guardar la visita en el index
     * del visitante.
     *
     * @param model Modelo con la información necesaria para transportar a los
     * archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     *
     * @return La página a donde debe redireccionar después de la acción.
     */
    @GetMapping("/") // Base
    public String main(Model model, HttpServletRequest request) throws Exception {
        guardarVisita(request);
        cargarModelo(model, request);
        return "index"; // Nombre del archivo jsp
    }

    /**
     * Método para guardar la visita al sistema para el contador de visitantes.
     *
     * @param request Objeto de la solicitud de la petición HTTP.
     */
    private void guardarVisita(HttpServletRequest request) {
        Visita visita = new Visita();
        visita.setIp(getClientIpAddr(request));
        visitaDao.registrarVisita(visita);
    }

    /**
     * Método que carga la información actual de las secciones para mostrarlas
     * en el index del sistema
     *
     * @param model Modelo con la información necesaria para transportar a los
     * archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     */
    private void cargarModelo(Model model, HttpServletRequest request) throws Exception {
        model.addAttribute("categoriasContenido", categoriaDao.getCategorias());
        model.addAttribute("subcategoriasContenido", subCategoriaDao.getSubCategorias());
        model.addAttribute("itemsubcategoriasContenido", itemSubCategoriaDao.getItemSubCategorias());
        model.addAttribute("noticias", noticiaDao.getUltimasNoticias());
        model.addAttribute("novedades", novedadDao.getUltimasNovedades());
        model.addAttribute("actividades", actividadDao.getUltimasActividades());
        model.addAttribute("galerias", galeriaDao.getUltimasGalerias());
        model.addAttribute("redes", redSocialDao.getRedesSociales());
        model.addAttribute("auditorias", auditoriaDAO.getAuditorias());
        model.addAttribute("enlaces", enlaceDeInteresDao.getEnlacesDeInteres());
        model.addAttribute("contactos", contactoDao.getContactos());
        model.addAttribute("logoHorizontal", logoDao.getLogo("LogoHorizontal"));
        model.addAttribute("logoVertical", logoDao.getLogo("LogoVertical"));
        model.addAttribute("dependencia", fileUtil.getProperties().getProperty("name"));
        model.addAttribute("descripcionDependencia", fileUtil.getProperties().getProperty("namedescription"));
        model.addAttribute("visitasDia", visitaDao.getCantidadVisitasDia());
        model.addAttribute("visitasMes", visitaDao.getCantidadVisitasMes());
        model.addAttribute("visitasSiempre", visitaDao.getCantidadVisitas());
        model.addAttribute("calendario", generarCalendario());
    }

    /**
     * Método que retorna una página para realizar el logeo de un usuario.
     *
     * @param model Modelo con la información necesaria para transportar a los
     * archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     *
     * @return La página de login.
     */
    @GetMapping("/admin") // Base
    public String index(Model model, HttpServletRequest request) throws Exception {
        mostrarRegistroActivacionSuperAdmin(model, request);
        return "Administrador/Login";
    }

    /**
     * Método que verifica si el usuario logeado tiene rol SuperAdmin de acuerdo
     * al correo registrado en el token.
     *
     * @param model Modelo con la información necesaria para transportar a los
     * archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     */
    public void mostrarSuperAdmin(Model model, HttpServletRequest request) throws Exception {
        String correo = jwtUtil.parseToken(request.getSession().getAttribute("token").toString());
        Usuario u = this.usuarioDao.getUsuarioSuperAdmin();
        if (u.getCorreo().equals(correo)) {
            model.addAttribute("mostrarSuperAdmin", 1);
        } else {
            model.addAttribute("mostrarSuperAdmin", 0);
        }
    }

    /**
     * Método que genera la ruta del calendario de visitantes.
     *
     * @return Url del calendario a exponer para los visitantes.
     */
    public String generarCalendario() throws Exception {
        List<Usuario> usuarios = this.usuarioDao.getUsuarios();
        String usuario = "";
        for (Usuario u : usuarios) {
            if (u.getRol().equalsIgnoreCase("SuperAdmin")) {
                usuario = u.getCorreoCalendario();
                break;
            }
        }
        usuario = usuario.replace("@", "%40");
        usuario = "https://calendar.google.com/calendar/embed?src=" + usuario + "&ctz=America%2FBogota";
        return usuario;
    }

    /**
     * Método que verifica el rol del usuario de acuerdo al correo registrado en
     * el token.
     *
     * @param model Modelo con la información necesaria para transportar a los
     * archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     */
    public String mostrarRol(Model model, HttpServletRequest request) throws Exception {
        String correo = jwtUtil.parseToken(request.getSession().getAttribute("token").toString());
        return this.usuarioDao.obtenerUsuarioPorCorreo(correo).getRol();
    }

    /**
     * Método que verifica si hay SuperAdmin registrado en la BD, también
     * verifica si el SuperAdmin está activo y carga las respuestas (1 o 0) en
     * el modelo.
     *
     * @param model Modelo con la información necesaria para transportar a los
     * archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     */
    private void mostrarRegistroActivacionSuperAdmin(Model model, HttpServletRequest request) throws Exception {
        Usuario u = this.usuarioDao.getUsuarioSuperAdmin();
        if (u.getCodigo() != null) {
            model.addAttribute("usuariosSuperAdmin", 1);
            if (u.getCodigo() != null && u.getActivo() == 1) {
                model.addAttribute("usuariosSuperAdminActivo", 1);
            } else {
                model.addAttribute("usuariosSuperAdminActivo", 0);
            }
        } else {
            model.addAttribute("usuariosSuperAdmin", 0);
        }

    }

    /**
     * Método que retorna una página con todas las cantidades de registros del
     * sistema.
     *
     * @param model Modelo con la información necesaria para transportar a los
     * archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     *
     * @return La página de indexAdmin.
     */
    @GetMapping("/indexAdmin") // Base
    public String indexAdmin(Model model, HttpServletRequest request) throws Exception {
        getCantidadRegistros(model, request);
        return "Administrador/indexAdmin"; // Nombre del archivo jsp
    }

    /**
     * Método que retorna una página con todas las auditorias en el sistema.
     *
     * @param model Modelo con la información necesaria para transportar a los
     * archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     *
     * @return La página principal de auditorias.
     */
    @GetMapping(value = "/auditorias") // Base
    public String indexAuditoria(Model model, HttpServletRequest request) throws Exception {
        mostrarSuperAdmin(model, request);
        if (mostrarRol(model, request).equalsIgnoreCase("SuperAdmin")) {
            model.addAttribute("auditorias", auditoriaDAO.getAuditorias());
            return "Administrador/Auditoria/Auditorias"; // Nombre del archivo jsp
        } else {
            return "Administrador/indexAdmin"; // Nombre del archivo jsp
        }

    }

    /**
     * Método solicitado por los formularios de los archivos JSP.
     *
     * Este metodo es usado en la etiqueta form de la siguiente manera:
     * modelAttribute="login".
     *
     * @return class Login.
     */
    @ModelAttribute("login")
    public Login setUpUserForm() {
        return new Login();
    }

    /**
     * Método para autenticar a un usuario.
     *
     * @param login Objeto con los datos de autenticación.
     * @param model Clase para enviar datos desde los servicios a los archivos
     * JSP.
     * @param request Objeto con los datos de sesión que por el instante es
     * nulo.
     *
     * @return La página a donde fue redireccionado.
     */
    @PostMapping("/autenticar")
    public String authenticateUser(@ModelAttribute("login") Login login, Model model,
            HttpServletRequest request) throws Exception {
        mostrarRegistroActivacionSuperAdmin(model, request);
        // Se consulta si los datos no vienen nulos.
        if (!StringUtils.isEmpty(login.getCorreoInstitucional()) && !StringUtils.isEmpty(login.getContrasena())) {
            Usuario u = usuarioDao.obtenerUsuarioPorCorreo(login.getCorreoInstitucional());
            if (u.getActivo() == 1) {
                // Se consulta en base de datos si se encuentra ese correo y esa contraseña.
                String resultado = loginDao.authenticate(login.getCorreoInstitucional(), login.getContrasena());
                // Si el resultado no es vacio es por que si existe ese correo y esa contraseña.
                if (!resultado.isEmpty()) {
                    // Se crea un Json Web Token para validar si la sesión esta activa.
                    String jwt = jwtUtil.generateToken(resultado, login.getCorreoInstitucional());
                    // Se guarda el JWT como atributo de sesión.
                    request.getSession().setAttribute("token", jwt);
                    // Se guarda la sesión en el manejador de sesiones.
                    sessionManager.guardarSession("SESSION:" + login.getCorreoInstitucional(), jwt);
                    this.getCantidadRegistros(model, request);
                    // Se redirige al index debido a que el usuario ya fue autenticado con éxito.
                    return "Administrador/indexAdmin";
                } else {
                    /**
                     * Se guarda en una variable el mensaje de error indicando
                     * que el usuario o la contraseña fueron incorrectos debido
                     * a que no se encuentran en la base de datos y así pueda
                     * ser entendida por los archivos JSP.
                     */
                    model.addAttribute("wrong", "Usuario o contraseña incorrectos.");
                }
                // Se redirecciona al login debido a que la autenticación fue incorrecta.
                return "Administrador/Login";
            } else {
                model.addAttribute("wrong", "El usuario no puede logearse porque esta inactivo.");
                return "Administrador/Login";
            }
        } else {
            /**
             * Se guarda en una variable el mensaje de error indicando que el
             * usuario o la contraseña son nulos siendo estos datos son
             * obligatorios, y así pueda ser entendida por los archivos .JSP
             */
            model.addAttribute("wrong", "El usuario y la contraseña no pueden ser nulos.");
            // Se redirecciona al login debido a que la autenticación fue incorrecta.
            return "Administrador/Login";
        }
    }

    /**
     * Método para eliminar la sesión del usuario logueado.
     *
     * @param token JWT para la validación de la identidad de un usuario.
     * @param request Objeto de la solicitud de la petición HTTP.
     *
     * @return Página donde será redireccionado.
     */
    @GetMapping("/logout")
    private String getLogOut(String token, HttpServletRequest request) {
        request.getSession().invalidate();
        String correo = jwtUtil.parseToken(token);
        sessionManager.eliminarSesion("SESSION:" + correo);
        return "Administrador/Login"; // Nombre del archivo jsp
    }

    /**
     * Método para obtener la cantidad de registros de cada sección del sistema.
     *
     * @param model Modelo con la información necesaria para transportar a los
     * archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     */
    private void getCantidadRegistros(Model model, HttpServletRequest request) throws Exception {
        model.addAttribute("catidadCategorias", this.categoriaDao.getCantidadCategorias());
        model.addAttribute("catidadSubCategorias", this.subCategoriaDao.getCantidadSubCategorias());
        model.addAttribute("catidadItemSubCategorias", this.itemSubCategoriaDao.getCantidadItemSubCategorias());
        model.addAttribute("catidadContenidos", this.contenidoDao.getCantidadContenidos());
        model.addAttribute("catidadNoticias", this.noticiaDao.getCantidadNoticias());
        model.addAttribute("catidadActividades", this.actividadDao.getCantidadActividades());
        model.addAttribute("catidadNovedades", this.novedadDao.getCantidadNovedades());
        model.addAttribute("catidadLogos", this.logoDao.getCantidadLogos());
        model.addAttribute("catidadEnlaces", this.enlaceDeInteresDao.getCantidadEnlacesDeInteres());
        model.addAttribute("catidadGalerias", this.galeriaDao.getCantidadGalerias());
        model.addAttribute("catidadContactos", this.contactoDao.getCantidadContactos());
        model.addAttribute("catidadRedesSociales", this.redSocialDao.getCantidadRedesSociales());
        model.addAttribute("catidadAuditorias", this.auditoriaDAO.getCantidadAuditorias());
        model.addAttribute("catidadUsuarios", this.usuarioDao.getCantidadUsuarios());
        mostrarSuperAdmin(model, request);
    }

    /**
     * Método que obtiene la página de obtener un componente dado un ID.
     *
     * @param idComponente Identificador del componente.
     * @param tipo Tipo del componente.
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     *
     * @return La página con la información del contenido cargado.
     */
    @GetMapping(value = "/servicios/componente")
    public String obtenerContenido(@RequestParam("id") long idComponente,
            @RequestParam("componente") String tipo, Model model, HttpServletRequest request) throws Exception {
        // Consulto que el Id sea mayor a 0.
        if (idComponente <= 0) {
            return "index";
        }
        Contenido contenido = componenteDao.obtenerContenidoComponentePorId(idComponente, tipo);
        cargarModelo(model, request);
        model.addAttribute("titulo", (contenido == null) ? "" : contenido.getNombre());
        model.addAttribute("codigo", (contenido == null) ? "" : contenido.getContenido());
        if (contenido.getId() != 0) {
            return "contenido"; // Nombre del archivo jsp
        } else {
            return "index";
        }
    }

    /**
     * Método que obtiene la página de una galería dado un ID.
     *
     * @param idGaleria Identificador de la galería
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     *
     * @return La página con la información de la galería cargada.
     */
    @GetMapping(value = "/servicios/galeria")
    public String obtenerContenido(@RequestParam("id") long idGaleria, Model model, HttpServletRequest request) throws Exception {
        // Consulto que el Id sea mayor a 0.
        if (idGaleria <= 0) {
            return "index";
        }
        Galeria galeria = galeriaDao.obtenerGaleriaPorId(idGaleria);
        cargarModelo(model, request);
        model.addAttribute("galeria", galeria);
        return "galeria"; // Nombre del archivo jsp
    }

    /**
     * Método que obtiene la página de todas las novedades.
     *
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     *
     * @return La página con la información de las novedades cargada.
     */
    @GetMapping(value = "/servicios/novedades")
    public String obtenerNovedades(Model model, HttpServletRequest request) throws Exception {
        List<Novedad> novedades = novedadDao.getNovedades();
        cargarModelo(model, request);
        model.addAttribute("novedadesCom", novedades);
        return "novedades"; // Nombre del archivo jsp
    }

    /**
     * Método que obtiene la página de todas las galerías.
     *
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     *
     * @return La página con la información de las galerías cargada.
     */
    @GetMapping(value = "/servicios/galerias")
    public String obtenerGalerias(Model model, HttpServletRequest request) throws Exception {
        List<Galeria> galerias = galeriaDao.getGalerias();
        cargarModelo(model, request);
        model.addAttribute("galeriasCom", galerias);
        return "galerias"; // Nombre del archivo jsp
    }

    /**
     * Método que obtiene la página de todas las actividades.
     *
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     *
     * @return La pagina con la información de las actividades cargada.
     */
    @GetMapping(value = "/servicios/actividades")
    public String obtenerActividades(Model model, HttpServletRequest request) throws Exception {
        List<Actividad> galerias = actividadDao.getActividades();
        cargarModelo(model, request);
        model.addAttribute("actividadCom", galerias);
        return "actividades"; // Nombre del archivo jsp
    }

    /**
     * Método que obtiene la página de todas las noticias.
     *
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     *
     * @return La pagina con la información de las actividades cargada.
     */
    @GetMapping(value = "/servicios/noticias")
    public String obtenerNoticias(Model model, HttpServletRequest request) throws Exception {
        List<Noticia> noticias = noticiaDao.getNoticias();
        cargarModelo(model, request);
        model.addAttribute("noticiasCom", noticias);
        return "noticias"; // Nombre del archivo jsp
    }

    /**
     * Método para generar el reporte de la información actualmente registrada
     * en el sistema.
     *
     * @return El servicio donde se generará el reporte.
     */
    @GetMapping("/generarInforme")
    private String generarInforme() {
        return "xlsView"; // Nombre del archivo jsp
    }

    /**
     * Método para obtener la dirección ip del usuario.
     *
     * @param request Objeto de la solicitud de la petición HTTP.
     *
     * @return Ip obtenida del request.
     */
    public static String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_X_FORWARDED");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_FORWARDED");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_VIA");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * Método que retorna una página para realizar la recuperación de
     * contraseña.
     *
     * @return La página de recordar contraseña.
     */
    @GetMapping("/recordar") // Base
    public String recordar() {
        return "Administrador/Recordar"; // Nombre del archivo jsp
    }

    /**
     * Método que permite recuperar una contraseña de un usuario.
     *
     * @param login Objeto con la información del usuario.
     * @param model Modelo con la información necesaria para transportar a los
     * archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     *
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping("/recordarContraseña")
    public String recordarContraseña(@ModelAttribute("login") Login login, Model model, HttpServletRequest request) throws Exception {
        if (login.getCorreoInstitucional().equals("")) {
            model.addAttribute("wrong", "Debes anotar por lo menos el correo.");
            return "Administrador/Recordar";
        } else {
            String mensaje = usuarioDao.enviarCorreo(login.getCorreoInstitucional());
            if (mensaje.equals("Actualizacion")) {
                model.addAttribute("result", "Contraseña recuparada con éxito");
                return "Administrador/Login";
            } else {
                model.addAttribute("wrong", mensaje);
                return "Administrador/Recordar";
            }
        }
    }

    /**
     * Método que permite activar un usuario SuperAdmin.
     *
     * @param usuario Objeto con la información del usuario
     * @param model Modelo con la información necesaria para transportar a los
     * archivos JSP.
     *
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping("/activarUsuario")
    public String activarUsuario(@ModelAttribute("usuario") Usuario usuario, Model model) throws Exception {
        String mensaje = usuarioDao.activarUsuario(usuario);
        if (mensaje.equals("Activacion exitosa")) {
            model.addAttribute("result", "Usuario activado con exito");
            model.addAttribute("usuario", new Usuario());
            return "Administrador/Login";
        } else {
            model.addAttribute("wrong", mensaje);
            return "Administrador/Activar";
        }
    }

    /**
     * Método que retorna una página para realizar la configuración de la
     * información de un usuario.
     *
     * @param model Modelo con la información necesaria para transportar a los
     * archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     *
     * @return La página a donde debe redireccionar después de la acción.
     */
    @GetMapping("/configuracion") // Base
    public String configuracion(Model model, HttpServletRequest request) throws Exception {
        // Se cargan los contenidos para poder mostrarlas en el cuadro.
        String correo = jwtUtil.parseToken(request.getSession().getAttribute("token").toString());
        Usuario usuario = usuarioDao.obtenerUsuarioPorCorreo(correo);
        mostrarSuperAdmin(model, request);
        if (mostrarRol(model, request).equalsIgnoreCase("SuperAdmin")) {
            model.addAttribute("usuario", usuario);
            return "Administrador/Cuenta/ActualizarCuenta"; // Nombre del archivo jsp
        } else {
            model.addAttribute("usuario", usuario);
            return "Administrador/Cuenta/ActualizarCuentaAdmin"; // Nombre del archivo jsp
        }

    }

    /**
     * Método que permite editar la cuenta de un usuario.
     *
     * @param usuario Objeto con la información del usuario.
     * @param model Modelo con la información necesaria para transportar a los
     * archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     *
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/editarCuenta")
    public String editarCuenta(@ModelAttribute("usuario") Usuario usuario, Model model, HttpServletRequest request) throws Exception {
        mostrarSuperAdmin(model, request);
        if (mostrarRol(model, request).equalsIgnoreCase("Admin")) {
            usuario.setCodigoNuevo("");
            usuario.setCorreoCalendario("");
        }
        // Se valida si la información cumple con las reglas de actualización.
        if (usuario.isValidoParaActualizar()) {
            String correo = jwtUtil.parseToken(request.getSession().getAttribute("token").toString());
            // Se valida si la contraseña antigua es correcta.
            if (!loginDao.authenticate(correo, usuario.getContrasenaAntigua()).equals("")) {
                // Se reliza la actualización de la información.
                String mensaje = usuarioDao.editarUsuario(usuario);
                // Se elimina la sesión del usuario.
                sessionManager.eliminarSesion("SESSION:" + correo);
                // Se autentica al usuario con la nueva información.
                Login l = new Login();
                Usuario u = new Usuario();
                if(usuario.getCodigoNuevo().isEmpty()){
                    u = usuarioDao.obtenerUsuarioPorId(Long.parseLong(usuario.getCodigo()));
                }else{
                    u = usuarioDao.obtenerUsuarioPorId(Long.parseLong(usuario.getCodigoNuevo()));
                }
                l.setContrasena(u.getContrasena());
                l.setCorreoInstitucional(u.getCorreo());
                authenticateUser(l, model, request);
                // Se valida si la actualización en la BD fue exitosa.
                if (mensaje.equals("Actualizacion exitosa")) {
                    // Se muestra el mensaje de éxito.
                    model.addAttribute("result", "Usuario actualizado con éxito.");
                    String c1 = jwtUtil.parseToken(request.getSession().getAttribute("token").toString());
                    // Se carga la información actualizada para ser mostrada en los campos correspondientes.
                    model.addAttribute("usuario", usuarioDao.obtenerUsuarioPorCorreo(c1));
                    if (mostrarRol(model, request).equalsIgnoreCase("SuperAdmin")) {
                        return "Administrador/Cuenta/ActualizarCuenta"; // Nombre del archivo jsp
                    } else {
                        return "Administrador/Cuenta/ActualizarCuentaAdmin"; // Nombre del archivo jsp
                    }
                } else {
                    // Se muestra el mensaje de error.
                    model.addAttribute("wrong", mensaje);
                    if (mostrarRol(model, request).equalsIgnoreCase("SuperAdmin")) {
                        return "Administrador/Cuenta/ActualizarCuenta"; // Nombre del archivo jsp
                    } else {
                        return "Administrador/Cuenta/ActualizarCuentaAdmin"; // Nombre del archivo jsp
                    }
                }
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", "La contraseña actual no es correcta.");
                if (mostrarRol(model, request).equalsIgnoreCase("SuperAdmin")) {
                    return "Administrador/Cuenta/ActualizarCuenta"; // Nombre del archivo jsp
                } else {
                    return "Administrador/Cuenta/ActualizarCuentaAdmin"; // Nombre del archivo jsp
                }
            }
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", "Ha olvidado diligenciar la información requerida.");
            if (mostrarRol(model, request).equalsIgnoreCase("SuperAdmin")) {
                return "Administrador/Cuenta/ActualizarCuenta"; // Nombre del archivo jsp
            } else {
                return "Administrador/Cuenta/ActualizarCuentaAdmin"; // Nombre del archivo jsp
            }
        }

    }
}
