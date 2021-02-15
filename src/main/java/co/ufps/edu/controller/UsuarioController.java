package co.ufps.edu.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import co.ufps.edu.dao.UsuarioDao;
import co.ufps.edu.dto.Usuario;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador de usuarios. Los usuarios son los que pueden ingresar al sistema
 * y hacer ajustes de acuerdo a su perfil.
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
public class UsuarioController {

    private UsuarioDao usuarioDao;
    private AdminController adminController;

    /**
     * Constructor de la clase en donde se inicializan las variables
     */
    public UsuarioController() throws Exception {
        usuarioDao = new UsuarioDao();
        this.adminController = new AdminController();
    }

    /**
     * Método que retorna una página con los datos de la cuenta.
     *
     * @param model Modelo con la información necesaria para transportar a los
     * archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     *
     * @return La página principal de configuracion.
     */
    @GetMapping("/usuarios") // Base
    public String index(Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        if (this.adminController.mostrarRol(model, request).equalsIgnoreCase("SuperAdmin")) {
            // Se cargan los usuarios para poder mostrarlos en el cuadro.
            model.addAttribute("usuarios", usuarioDao.getUsuariosAdmin());
            return "Administrador/Usuario/Usuarios"; // Nombre del archivo jsp
        } else {
            return "Administrador/indexAdmin"; // Nombre del archivo jsp
        }

    }

    /**
     * Método que activa un usuario.
     *
     * @param model Modelo con la información necesaria para transportar a los
     * archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     *
     * @return La página de activación.
     */
    @GetMapping("/activar") // Base
    public String activar(Model model, HttpServletRequest request) throws Exception {
        return "Administrador/Activar"; // Nombre del archivo jsp
    }

    /**
     * Método que retorna una página para realizar el registro de un usuario.
     *
     * @param model Modelo con la información necesaria para transportar a los
     * archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     *
     * @return La página de registro de usuarios.
     */
    @GetMapping("/registrarSuperAdmin") // Base
    public String registrarSuperAdmin(Model model, HttpServletRequest request) throws Exception {
        return "Administrador/Usuario/RegistrarSuper"; // Nombre del archivo jsp
    }

    /**
     * Método que obtiene la página de actualizar usuario dado un ID.
     *
     * @param codigo Código del usuario.
     * @param model Modelo con la información necesaria para transportar a los
     * archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     *
     * @return La página con la información del usuario cargada.
     */
    @GetMapping(value = "/actualizarUsuario")
    public String actualizarUsuario(@RequestParam("id") long codigo, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        if (this.adminController.mostrarRol(model, request).equalsIgnoreCase("SuperAdmin")) {
            // Se consulta que el Id sea mayor a 0.
            if (codigo <= 0) {
                // Se cargan los usuarios para poder mostrarlos en el cuadro.
                model.addAttribute("usuarios", usuarioDao.getUsuariosAdmin());
                return "Administrador/Usuario/Usuarios"; // Nombre del archivo jsp
            }
            // Se obtiene la información del usuario de acuerdo al id.
            Usuario usuario = usuarioDao.obtenerUsuarioPorId(codigo);
            // Se carga la información del usuario obtenida.
            model.addAttribute("usuario", usuario);
            return "Administrador/Usuario/ActualizarUsuario";
        } else {
            return "Administrador/indexAdmin"; // Nombre del archivo jsp
        }
    }

    /**
     * Método que obtiene la página de actualizar usuario dado un ID.
     *
     * @param codigo Código del usuairo.
     * @param model Objeto para enviar información a los archivos .JSP
     * @return La página con la información del usuario cargada.
     */
    @GetMapping(value = "/activarDesactivarUsuario")
    public String activarDesactivarUsuario(@RequestParam("id") long codigo, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        if (this.adminController.mostrarRol(model, request).equalsIgnoreCase("SuperAdmin")) {
            // Se consulta que el Id sea mayor a 0.
            if (codigo <= 0) {
                // Se cargan los usuarios para poder mostrarlos en el cuadro.
                model.addAttribute("usuarios", usuarioDao.getUsuariosAdmin());
                return "Administrador/Usuario/Usuarios"; // Nombre del archivo jsp
            } else {
                // Se activa o desactiva el usuario.
                String mensaje = usuarioDao.activarDesactivarUsuario(codigo);
                // Se verica que la acción se haya realizado.
                if (mensaje.equals("Actualizacion exitosa")) {
                    // Se muestra el mensaje de éxito.
                    model.addAttribute("result", "Usuario actualizado con éxito.");
                    // Se carga la información de los usuarios para que la nueva información se refleje en la tabla.
                    model.addAttribute("usuarios", usuarioDao.getUsuariosAdmin());
                    return "Administrador/Usuario/Usuarios"; // Nombre del archivo jsp
                } else {
                    // Se muestra el mensaje de error.
                    model.addAttribute("wrong", mensaje);
                    return "Administrador/Usuario/Usuarios";
                }
            }
        } else {
            return "Administrador/indexAdmin"; // Nombre del archivo jsp
        }

    }

    /**
     * Método que permite editar un usuario.
     *
     * @param usuario Objeto con la información a editar.
     * @param model Modelo con la información necesaria para transportar a los
     * archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     *
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/editarUsuario")
    public String editarUsuario(@ModelAttribute("usuario") Usuario usuario, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        if (this.adminController.mostrarRol(model, request).equalsIgnoreCase("SuperAdmin")) {
            // Se valida si la información cumple con las reglas de actualización.
            if (usuario.isValidoParaActualizar()) {
                String mensaje = usuarioDao.editarUsuario(usuario);
                if (mensaje.equals("Actualizacion exitosa")) {
                    // Se muestra el mensaje de éxito.
                    model.addAttribute("result", "Usuario actualizado con éxito.");
                    // Se carga la información actualizada para ser mostrada en los campos correspondientes.
                    model.addAttribute("usuarios", usuarioDao.getUsuariosAdmin());
                    return "Administrador/Usuario/Usuarios"; // Nombre del archivo jsp
                } else {
                    // Se muestra el mensaje de error.
                    model.addAttribute("wrong", mensaje);
                    return "Administrador/Usuario/ActualizarUsuario";
                }
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", "Debes llenar alguno de los campos editables.");
                return "Administrador/Usuario/ActualizarUsuario";
            }
        } else {
            return "Administrador/indexAdmin"; // Nombre del archivo jsp
        }

    }

    /**
     * Método que obtiene la página de actualizar usuario dado un ID.
     *
     * @param codigo Código del usuario.
     * @param model Objeto para enviar información a los archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     *
     * @return La pagina con la información del usuario cargada.
     */
    @GetMapping(value = "/eliminarUsuario")
    public String eliminarUsuario(@RequestParam("id") long codigo, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        if (this.adminController.mostrarRol(model, request).equalsIgnoreCase("SuperAdmin")) {
            // Se consulta que el Id sea mayor a 0.
            if (codigo <= 0) {
                // Se cargan los usuarios para poder mostrarlas en el cuadro.
                model.addAttribute("usuarios", usuarioDao.getUsuariosAdmin());
                return "Administrador/Usuario/Usuarios"; // Nombre del archivo jsp
            }
            // Se obtiene la información del usuario de acuerdo al id.
            Usuario usuario = usuarioDao.obtenerUsuarioPorId(codigo);
            // Se carga la información del usuarios obtenida.
            model.addAttribute("usuario", usuario);
            return "Administrador/Usuario/EliminarUsuario"; // Nombre del archivo jsp
        } else {
            return "Administrador/indexAdmin"; // Nombre del archivo jsp
        }

    }

    /**
     * Método que permite eliminar un usuario.
     *
     * @param usuario Objeto con la información a eliminar.
     * @param model Modelo con la información necesaria para transportar a los
     * archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     *
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/borrarUsuario")
    public String borrarUsuario(@ModelAttribute("usuario") Usuario usuario, Model model, HttpServletRequest request) throws Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        if (this.adminController.mostrarRol(model, request).equalsIgnoreCase("SuperAdmin")) {
            // Se elimina la información.
            String mensaje = usuarioDao.eliminarUsuario(usuario);
            // Se valida si la información fue eliminada.
            if (mensaje.equals("Eliminacion exitosa")) {
                // Se muestra el mensaje de éxito.
                model.addAttribute("result", "Usuario eliminado con éxito.");
                // Se carga la información de los usuarios para que se refleje la información actualizada.
                model.addAttribute("usuarios", usuarioDao.getUsuariosAdmin());
                return "Administrador/Usuario/Usuarios"; // Nombre del archivo jsp
            } else {
                // Se carga la información del usuario para que se refleje la información actualizada.
                model.addAttribute("usuario", usuarioDao.obtenerUsuarioPorId(Long.parseLong(usuario.getCodigo())));
                // Se muestra el mensaje de error.    
                model.addAttribute("wrong", mensaje);
                return "Administrador/Usuario/EliminarUsuario";
            }
        } else {
            return "Administrador/indexAdmin"; // Nombre del archivo jsp
        }

    }

    /**
     * Método que retorna una página para realizar el registro de un usuaario.
     *
     * @param model Modelo con la información necesaria para transportar a los
     * archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     *
     * @return La página de registro de usuarios.
     */
    @GetMapping("/registrarUsuario") // Base
    public String registrarUsuario(Model model, HttpServletRequest request) throws Exception, Exception {
        this.adminController.mostrarSuperAdmin(model, request);
        if (this.adminController.mostrarRol(model, request).equalsIgnoreCase("SuperAdmin")) {
            return "Administrador/Usuario/RegistrarUsuario"; // Nombre del archivo jsp
        } else {
            return "Administrador/indexAdmin"; // Nombre del archivo jsp
        }
    }

    /**
     * Método que permite guardar un usuario.
     *
     * @param usuario Objeto con la información a guardar.
     * @param model Modelo con la información necesaria para transportar a los
     * archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     *
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/guardarUsuario")
    public String guardarUsuario(@ModelAttribute("usuario") Usuario usuario, Model model, HttpServletRequest request) throws Exception {
        if (this.adminController.mostrarRol(model, request).equalsIgnoreCase("SuperAdmin")) {
            // Se le pasa el rol.
            usuario.setRol("Admin");
            // Se activa el usuario.
            usuario.setActivo(1);
            // Se registra correo para calendario como N/A (No aplica).
            usuario.setCorreoCalendario("N/A");
            this.adminController.mostrarSuperAdmin(model, request);
            // Se consulta si tiene todos los campos llenos.
            if (usuario.isValidoParaRegistrarUsua()) {
                // Se consulta si el correo es valido.
                if (usuario.validarCorreo(usuario.getCorreo())) {
                    // Se valida si las contraseñas coinciden.
                    if (usuario.seRepiten()) {
                        // Se valida que la contraseña cumpla con las reglas.
                        if (usuario.contraValida(usuario.getContrasenaNueva())) {
                            // Se registra el usuario.
                            String mensaje = usuarioDao.registrarUsuario(usuario);
                            // Se valida si la información fue registrada.
                            if (mensaje.equals("Registro exitoso")) {
                                // Se muestra el mensaje de éxito.
                                model.addAttribute("result", "Usuario registrado con éxito.");
                                // Se carga la información de los usuarios para que la nueva información se refleje en la tabla.
                                model.addAttribute("usuarios", usuarioDao.getUsuariosAdmin());
                                return "Administrador/Usuario/Usuarios"; // Nombre del archivo jsp
                            } else {
                                // Se muestra el mensaje de error.
                                model.addAttribute("wrong", mensaje);
                                return "Administrador/Usuario/RegistrarUsuario";
                            }
                        } else {
                            // Se muestra el mensaje de error.
                            model.addAttribute("wrong", "La contraseña debe tener mínimo 4 caracteres númericos, 4 caracteres en minuscula y 2 caracteres en mayúscula.");
                            return "Administrador/Usuario/RegistrarUsuario";
                        }
                    } else {
                        // Se muestra el mensaje de error.
                        model.addAttribute("wrong", "Las contraseñas no coinciden.");
                        return "Administrador/Usuario/RegistrarUsuario";
                    }
                } else {
                    // Se muestra el mensaje de error.
                    model.addAttribute("wrong", "Debe usar el correo institucional.");
                    return "Administrador/Usuario/RegistrarUsuario";
                }

            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", "Debes llenar todos los campos.");
                return "Administrador/Usuario/RegistrarUsuario";
            }
        } else {
            return "Administrador/indexAdmin"; // Nombre del archivo jsp
        }

    }

    /**
     * Método que permite guardar un usuario superAdmin.
     *
     * @param usuario Objeto con la información a guardar.
     * @param model Modelo con la información necesaria para transportar a los
     * archivos JSP.
     * @param request Objeto de la solicitud de la petición HTTP.
     *
     * @return La página a donde debe redireccionar después de la acción.
     */
    @PostMapping(value = "/guardarSuperAdmin")
    public String guardarSuperAdmin(@ModelAttribute("usuario") Usuario usuario, Model model, HttpServletRequest request) throws Exception {
        // Se le pasa el rol.
        usuario.setRol("SuperAdmin");
        // Se activa el usuario.
        usuario.setActivo(0);
        // Se consulta si tiene todos los campos llenos.
        if (usuario.isValidoParaRegistrarUsua()) {
            // Se consulta si el correo es valido.
            if (usuario.validarCorreo(usuario.getCorreo())) {
                // Se consulta si el correo es valido.
                if (usuario.validarCorreoCalendario(usuario.getCorreoCalendario())) {
                    // Se valida si las contraseñas coinciden.
                    if (usuario.seRepiten()) {
                        // Se valida que la contraseña cumpla con las reglas.
                        if (usuario.contraValida(usuario.getContrasenaNueva())) {
                            // Se registra el usuario.
                            String mensaje = usuarioDao.registrarUsuario(usuario);
                            // Se genera el código de activación del usuario.
                            String codigo = usuarioDao.codigoActivar(usuario.getCodigo(), usuario.getCorreo());
                            // Se registra el código de activación del usuario.
                            mensaje = usuarioDao.registrarCodigoActivacion(usuario.getCorreo(), codigo);
                            // Se envia el código de activación del usuario por correo.
                            usuarioDao.enviarCodigo(usuario.getCorreo(), codigo);
                            // Se valida si el registro en la BD fue exitoso.
                            if (mensaje.equals("Actualizacion exitosa")) {
                                // Se muestra el mensaje de éxito.
                                model.addAttribute("result", "Usuario registrado con éxito, se ha enviado un codigo de activación al correo registrado.");
                                // Se carga la información del usuario para que la nueva información se refleje en la tabla.
                                model.addAttribute("usuario", new Usuario());
                                return "Administrador/Activar"; // Nombre del archivo jsp
                            } else {
                                // Se muestra el mensaje de error.
                                model.addAttribute("wrong", mensaje);
                                return "Administrador/Usuario/RegistrarSuper";
                            }
                        } else {
                            // Se muestra el mensaje de error.
                            model.addAttribute("wrong", "La contraseña debe tener mínimo 4 caracteres númericos, 4 caracteres en minuscula y 2 caracteres en mayúscula.");
                            return "Administrador/Usuario/RegistrarSuper";
                        }
                    } else {
                        // Se muestra el mensaje de error.
                        model.addAttribute("wrong", "Las contraseñas no coinciden.");
                        return "Administrador/Usuario/RegistrarSuper";
                    }
                } else {
                    // Se muestra el mensaje de error.
                    model.addAttribute("wrong", "El correo del calendario debe ser tipo ejemplo@gmail.com");
                    return "Administrador/Usuario/RegistrarSuper";
                }
            } else {
                // Se muestra el mensaje de error.
                model.addAttribute("wrong", "El correo institucional debe ser tipo ejemplo@ufps.edu.co");
                return "Administrador/Usuario/RegistrarSuper";
            }
        } else {
            // Se muestra el mensaje de error.
            model.addAttribute("wrong", "Debe llenar todos los campos.");
            return "Administrador/Usuario/RegistrarSuper";
        }
    }

    /**
     * Modelo con el que se realizara el formulario
     *
     * @return Un objeto para ser llenado desde el archivo .JSP
     */
    @ModelAttribute("usuario")
    public Usuario setUpUserForm() {
        return new Usuario();
    }
}
