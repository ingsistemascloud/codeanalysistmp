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
     * M�todo que guarda una sesi�n en el sistema.
     *
     * @param sesion String con la identificaci�n del usuario.
     * @param token JWT generado para la validaci�n de identidad.
     */
    public void guardarSession(String sesion, String token) {
        sesiones.put(sesion, token);
    }

    /**
     * M�todo que elimina una sesi�n en el sistema.
     *
     * @param sesion String con la identificaci�n del usuario.
     */
    public void eliminarSesion(String sesion) {
        sesiones.remove(sesion);
    }

    /**
     * Este m�todo se llama antes que el controlador.
     *
     * @param request Objeto de la solicitud de la petici�n HTTP.
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
            // Se extrae el token de sesi�n.
            String token = request.getSession().getAttribute("token").toString();
            // Se v�lida el token.
            boolean permission = validarToken(token);
            // Si el token es v�lido se permite la continuidad.
            if (permission) {
                return true;
                // Si el token no es v�lido se env�a un mensaje de no autorizado. 
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.sendRedirect(Constantes.SERVER + Constantes.RUTA + "admin");
                return false;
            }
        }

    }

    /**
     * M�todo encargado de v�lidar si el token del usuario es correcto.
     *
     * @param token string para la validaci�n de la identidad de un usuario.
     *
     * @return estado actual de la validaci�n.
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
     * Este m�todo se llama despu�s del controlador.
     *
     *
     * @param request objeto de la solicitud de la petici�n HTTP.
     * @param response Objeto de la respuesta de la peticion HTTP.
     * @param handler Objeto handler.
     * @param modelAndView Objeto del modelo de la l�gica de datos y la vista.
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }

    /**
     * M�todo de bean de sesi�n con estado para recibir la devoluci�n de llamada
     * despu�s de la finalizaci�n de sesi�n.
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
