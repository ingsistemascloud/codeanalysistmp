package co.ufps.edu.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import java.io.Serializable;
import java.util.Date;
import java.util.function.Function;

/**
 * Clase que permite usar servicios para el manejo del JWT.
 * 
 * @author UFPS
 */
public class JwtUtil implements Serializable{

    // @Value("${jwt.secret}")
    private String secret = "secret";
    private static JwtUtil jwtUtil;
    private long expiration = 60000;
    private Clock clock = DefaultClock.INSTANCE;
    private long clockSkew = 0;
    
    /**
     * Get de la instancia del JWT.
     *
     * @return El JWT.
     */
    public static JwtUtil getInstancia() {
        if (jwtUtil == null) {
            jwtUtil = new JwtUtil();
        }
        return jwtUtil;
    }

    /**
     * Intenta analizar la cadena especificada como un token JWT. Si tiene éxito, devuelve
     * Objeto de usuario con nombre de usuario, ID y rol rellenado previamente.
     * Si no tiene éxito (el token no es válido o no contiene todas las
     * propiedades requeridas), simplemente devuelve nulo.
     *
     * @param token el token JWT para analizar.
     * 
     * @return Devuelve el correo del usuario extraído del token especificado o nulo si es un token
     * no es válido.
     */
    public String parseToken(String token) {
        try {
            Claims body = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            String codigo = body.get("correo").toString();
            return codigo;
        } catch (JwtException | ClassCastException e) {
            return "";
        } catch (Exception ex) {
            return "";
        }
        
    }

    /**
     * Genera un token JWT que contiene el correo de usuario como sujeto, el rol y el tiempo de 
     * expedición. Estas propiedades son tomadas del usuario especificado
     *
     * @param rol Del usuario para el que se generará el token.
     * @param correo Del usuario para el que se generará el token.
     *
     * @return El token generado.
     */
    public String generateToken(String rol, String correo) {
        Claims claims = Jwts.claims().setSubject("users/" + rol);
        claims.put("correo", correo);
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        
    }
    /**
     * Método que calcula el tiempo de expedición del token.
     * 
     * @param createdDate Hora actual.
     * 
     * @return La hora en que el token se vence.
     */
    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + (expiration*240));
    }
}
