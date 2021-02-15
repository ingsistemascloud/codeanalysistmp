/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.ufps.edu.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Clase que permite el manejo de excepciones en la aplicaci�n.
 * 
 * @author UFPS
 */
@ControllerAdvice
public class ConfigException {

    /**
     * Este m�todo gestiona excepciones.
     *
     * @return El error de la excepci�n.
     */
    @ExceptionHandler(Exception.class)
    public String exceptionHandler() {
        return "/error";
    }
}
