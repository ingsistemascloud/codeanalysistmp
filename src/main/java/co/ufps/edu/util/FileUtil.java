package co.ufps.edu.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.springframework.util.ResourceUtils;

/**
 * Clase util con las funcionalidades necesarias para el manejo de archivos.
 *
 * @author UFPS
 */
public class FileUtil {

    private Properties properties;

    /**
     * Constructor de la clase en donde se inicializan las variables y cargan las propiedades.
     */
    public FileUtil() {
        properties = new Properties();
        try {
            File file = ResourceUtils.getFile("classpath:database.properties");
            InputStream in = new FileInputStream(file);
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Get de properties.
     *
     * @return valor de properties.
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Set de properties.
     *
     * @param properties nuevo valor de properties.
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

}
