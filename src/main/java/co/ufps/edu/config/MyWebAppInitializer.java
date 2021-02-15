package co.ufps.edu.config;

import java.io.File;
import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/*
 * Clase que permite inicializar la aplicación.
 *
 * @author UFPS
 */
public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     * El tamaño maximo que podra contener un archivo.
     */
    private final long maxUploadSizeInMb = Long.MAX_VALUE;

    /**
     * Método para obtener la configuración root de clases.
     *
     * @return Objeto de clase de tipo RootConfig.
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{RootConfig.class};
    }

    /**
     * Método para obtener la configuración servlet de clases.
     *
     * @return Objeto de clases de tipo WebConfig.
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        // 1. Enciende la configuracion
        return new Class[]{WebConfig.class};
    }

    /**
     * Método para obtener los mapeos de los servlets.
     *
     * @return Objeto String instanciado.
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    /**
     * Método para la configuración personalizada de file.
     *
     * @param registration Servlet dinámico.
     */
    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        File uploadDirectory = new File(System.getProperty("java.io.tmpdir"));
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(uploadDirectory.getAbsolutePath(),
                maxUploadSizeInMb, maxUploadSizeInMb, (Integer.MAX_VALUE / 2));
        registration.setMultipartConfig(multipartConfigElement);

    }

    /**
     * Método para obtener los filtros de los servlets.
     * 
     * @return Arreglo de filtros.
     */
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return new Filter[]{characterEncodingFilter};
    }
}
