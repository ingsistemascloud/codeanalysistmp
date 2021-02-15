package co.ufps.edu.config;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import co.ufps.edu.constantes.Constantes;
import co.ufps.edu.util.JwtUtil;

/**
 * Clase que permite manejar las sesiones del sistema.
 *
 * @author UFPS
 */
public class SessionManager implements HandlerInterceptor {

    private JwtUtil jwtUtil;
    public HashMap<String, String> sesiones;

    /**
     * Constructor de la clase que inicializa las variables.
     */
    public SessionManager() {
        sesiones = new HashMap<>();
        jwtUtil = new JwtUtil();
    }

    /**
     * Método que guarda una sesión en el sistema.
     *
     * @param sesion String con la identificación del usuario.
     * @param token JWT generado para la validación de identidad.
     */
    public void guardarSession(String sesion, String token) {
        sesiones.put(sesion, token);
    }

    /**
     * Método que elimina una sesión en el sistema.
     *
     * @param sesion String con la identificación del usuario.
     */
    public void eliminarSesion(String sesion) {
        sesiones.remove(sesion);
    }

    /**
     * Este método se llama antes que el controlador.
     *
     * @param request Objeto de la solicitud de la petición HTTP.
     * @param response Objeto de la respuesta de la peticion HTTP.
     * @param handler Objeto handler.
     * @return Estado de la comprobacion de permisos del token.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        request.setCharacterEncoding("UTF-8");
        if (request.getSession().getAttribute("token") == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.sendRedirect(Constantes.RUTA + "admin");
            return false;
        } else {
            // Se extrae el token de sesión.
            String token = request.getSession().getAttribute("token").toString();
            // Se válida el token.
            boolean permission = validarToken(token);
            // Si el token es válido se permite la continuidad.
            if (permission) {
                return true;
                // Si el token no es válido se envía un mensaje de no autorizado. 
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.sendRedirect(Constantes.SERVER + Constantes.RUTA + "admin");
                return false;
            }
        }

    }

    /**
     * Método encargado de válidar si el token del usuario es correcto.
     *
     * @param token string para la validación de la identidad de un usuario.
     *
     * @return estado actual de la validación.
     */
    private boolean validarToken(String token) {
        String correo = jwtUtil.parseToken(token);
        if (token == null || token.isEmpty() || StringUtils.isEmpty(correo) || sesiones.get("SESSION:" + correo) == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Este método se llama después del controlador.
     *
     *
     * @param request objeto de la solicitud de la petición HTTP.
     * @param response Objeto de la respuesta de la peticion HTTP.
     * @param handler Objeto handler.
     * @param modelAndView Objeto del modelo de la lógica de datos y la vista.
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }

    /**
     * Método de bean de sesión con estado para recibir la devolución de llamada
     * después de la finalización de sesión.
     *
     * @param request objeto de la solicitud de la peticion HTTP
     * @param response Objeto de la respuesta de la peticion HTTP
     * @param handler Objeto handler
     * @param ex Objeto exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) throws Exception {

    }
}
