package co.ufps.edu.util;

import org.apache.commons.codec.binary.Base64; 

/**
 * Clase que permite usar servicios para el tratamiento de imagenes.
 * 
 * @author UFPS
 */
@SuppressWarnings("restriction")
public class ImagenUtil {

    /**
     * Método que permite convertir una imagen de un vector de bytes a una
     * cadena de string.
     *
     * @param imagen Contenido de la imagen en bytes.
     * 
     * @return Una cadena de caracteres con la imagen codificada en base 64.
     */
    public String convertirImagen(byte[] imagen) {
        Base64 base64Encoder = new Base64();
        String encodedString = base64Encoder.encodeAsString(imagen);
        StringBuilder imageString = new StringBuilder();
        imageString.append("data:image/png;base64,");
        imageString.append(encodedString);
        return imageString.toString();
    }
}
