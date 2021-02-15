package co.ufps.edu.config;

import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.ResourceBundleViewResolver;
import co.ufps.edu.dao.ActividadDao;
import co.ufps.edu.dao.ContenidoDao;
import co.ufps.edu.dao.GaleriaDao;
import co.ufps.edu.dao.LogoDao;
import co.ufps.edu.dao.NoticiaDao;
import co.ufps.edu.dao.NovedadDao;
import co.ufps.edu.dao.TipoContenidoDao;

/**
 * Clase que permite realizar la configuración web del sistema.
 *
 * @author UFPS
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"co.ufps.edu.*"})
@EnableAsync
public class WebConfig extends WebMvcConfigurerAdapter {

    /**
     * Bean Contenedor de Spring para obtener la configuración de java Mail.
     *
     * @return Objeto JavaMailSender configurado.
     */
    @Bean
    public JavaMailSender getJavaMailSender() {
        
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("recuperacion.webapp@gmail.com");
        mailSender.setPassword("Recu1234US");
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        return mailSender;
        
    }

    /**
     * Bean Contenedor de Spring para obtener el LogoDao.
     *
     * @return Instancia del objeto LogoDao.
     */
    @Bean
    public LogoDao getLogoDao() {
        return new LogoDao();
    }

    /**
     * Bean Contenedor de Spring para obtener el NoticiaDao.
     *
     * @return Instancia del objeto NoticiaDao.
     */
    @Bean
    public NoticiaDao getNoticiaDao() {
        return new NoticiaDao();
    }

    /**
     * Contenedor de Spring para obtener la novedadDao.
     *
     * @return Instancia del objeto novedadDao.
     */
    @Bean
    public NovedadDao getNovedadDao() {
        return new NovedadDao();
    }

    /**
     * Contenedor de Spring para obtener la actividadDao.
     *
     * @return Instancia del objeto actividadDao.
     */
    @Bean
    public ActividadDao getActividadDao() {
        return new ActividadDao();
    }

    /**
     * Contenedor de Spring para obtener el contenidoDao.
     *
     * @return Instancia del objeto contenidoDao.
     */
    @Bean
    public ContenidoDao getContenidoDao() {
        return new ContenidoDao();
    }

    /**
     * Contenedor de Spring para obtener el tipo de contenidoDao.
     *
     * @return Instancia del objeto tipoContenidoDao.
     */
    @Bean
    public TipoContenidoDao getTipoContenidoDao() {
        return new TipoContenidoDao();
    }

    /**
     * Contenedor de Spring para obtener la galeriaDao.
     *
     * @return Instancia del objeto galeriaDao.
     */
    @Bean
    public GaleriaDao getGaleriaDao() {
        return new GaleriaDao();
    }

    /**
     * Contenedor de Spring para obtener multipartResolver.
     *
     * @return Resolución de multiparte de servlet estándar.
     */
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    /**
     * Contenedor de Spring para obtener las vistas.
     * 
     * @return Vista de paquete de recursos.
     */
    @Bean
    public ViewResolver resourceBundleViewResolver() {
        ResourceBundleViewResolver viewResolver = new ResourceBundleViewResolver();
        viewResolver.setBasename("views");
        viewResolver.setOrder(1);
        return viewResolver;
    }

    /**
     * Contenedor de Spring para registrar los jsp.
     * 
     * @return Resolución de recursos internos.
     */
    @Bean
    public InternalResourceViewResolver resolver() {
        // 2. Registra los jsp.
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/web/views/");
        resolver.setSuffix(".jsp");
        resolver.setOrder(2);
        return resolver;
    }

    /**
     * Contenedor de Spring para registrar los recursos Css,JS,font,entre otros.
     * 
     * @param registry Registro de manejador de recursos,
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 3. Registrar los Recursos (Css,JS,font,entre otros).
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        registry.addResourceHandler("/servicios/resources/**").addResourceLocations("/resources/");
    }

    /**
     * Método que mapea la clase para la seguridad.
     * 
     * @param registry Inteceptor del registro.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Mapea la clase para la seguridad.
        registry.addInterceptor(getSessionManager()).addPathPatterns("/**")
                .excludePathPatterns("/resources/**", "/activar", "/activarUsuario", "/error", "/admin", "/registrarSuperAdmin", "/guardarSuperAdmin", "/autenticar", "/recordar", "/recordarContraseña", "/servicios/*", "/");
    }

    /**
     * Contenedor de Spring para obtener las sesiones.
     * 
     * @return La sesión del usuario.
     */
    @Bean
    public SessionManager getSessionManager() {
        return new SessionManager();
    }

    /**
     * Contenedor de Spring para registrar el StandardServletMultipartResolver
     * 
     * @return Multiparte de servlet estándar.
     */
    @Bean(name = "multipartResolver")
    public StandardServletMultipartResolver resolvermu() {
        return new StandardServletMultipartResolver();
    }

}
