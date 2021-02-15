<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!doctype html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang=""> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8" lang=""> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9" lang=""> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang=""> <!--<![endif]-->
    <head>
        <%@ include file = "../General/css.jsp" %>
    </head>
    <body>
        <!-- Left Panel -->
        <%@ include file = "../General/LeftPanel.jsp" %>
        <!-- /#left-panel -->
        <!-- Right Panel -->
        <div id="right-panel" class="right-panel">
            <!-- Header-->
            <header id="header" class="header">
                <div class="header-menu">
                    <div class="col-sm-7">
                        <a id="menuToggle" class="menutoggle pull-left"><i class="fa fa fa-tasks"></i></a>
                        <div class="page-header float-left">
                            <div class="page-title">
                                <ol class="breadcrumb text-right">
                                    <li><a href="${contextPath}/indexAdmin">Panel de control</a></li>
                                    <li><a href="${contextPath}/configuracion">Cuenta</a></li>
                                </ol>
                            </div>
                        </div>    
                    </div>
                    <!-- Area en donde se encuentra la foto del usuario y la barra de opciones -->
                    <%@ include file="../General/Configuracion.jsp"%>
                </div>
            </header><!-- /header -->
            <!-- Header-->
            <!-- Contenedor del formulario -->
            <div class="content mt-3">
                <div class="animated fadeIn">
                    <div class="row">
                        <div class="col-md-12">                	
                            <div class="card">
                                <!-- Titulo de la ventana -->
                                <div class="card-header">
                                    <strong class="card-title">Actualizar cuenta</strong>
                                </div>
                                <div class="card-body">
                                    <!-- Si hubo un registro exitoso muestra el mensaje-->
                                    <c:if test="${not empty result}">
                                        <div class="sufee-alert alert with-close alert-success alert-dismissible fade show">
                                            <c:out value='${result}' />
                                            <button id="success" type="button" class="close" data-dismiss="alert" aria-label="Close">
                                                <span aria-hidden="true">&times;</span>
                                            </button>
                                        </div>
                                    </c:if>
                                    <!-- Si hubo un error en el registro muestra el mensaje-->						
                                    <c:if test="${not empty wrong}">		            
                                        <div class="sufee-alert alert with-close alert-danger alert-dismissible fade show">				                   	
                                            <c:out value='${wrong}' />
                                            <button id="fail" type="button" class="close" data-dismiss="alert" aria-label="Close">
                                                <span aria-hidden="true">&times;</span>
                                            </button>
                                        </div>
                                    </c:if>
                                    <!-- Formulario -->
                                    <form:form id="formCuenta" action="editarCuenta" method="post" modelAttribute="usuario">
                                        <form:hidden id="codigo" path="codigo" class="form-control" aria-invalid="false" required = "true" value="${usuario.codigo}"/>
                                        <div class="form-group">
                                            <label for="text-input" class=" form-control-label">Digite la contrasena actual</label>
                                            <form:password path="contrasenaAntigua" class="form-control" aria-invalid="true" placeholder="Contraseña de logeo del usuario"/>     
                                        </div> 
                                        <!-- Campo para digitar el nombre -->
                                        <div class="form-group">
                                            <label for="text-input" class=" form-control-label">Código Actual</label>
                                            <form:input id="codigo" path="codigo" class="form-control" aria-invalid="false" value="${usuario.codigo}" disabled ="true"/>
                                        </div>
                                        <!-- Campo para digitar el nombre -->
                                        <div class="form-group">
                                            <label for="text-input" class=" form-control-label">Código Nuevo</label>
                                            <form:input id="codigoNuevo" path="codigoNuevo" class="form-control" aria-invalid="false" placeholder="Código institucional del usuario"/>
                                        </div>    
                                        <!-- Campo para digitar el nombre -->
                                        <div class="form-group">
                                            <label for="text-input" class=" form-control-label">Correo institucional</label>
                                            <form:input id="correo" path="correo" class="form-control" aria-invalid="false" value="${usuario.correo}"/>
                                        </div> 
                                        <!-- Campo para digitar el nombre -->
                                        <div class="form-group">
                                            <label for="text-input" class=" form-control-label">Correo calendario</label>
                                            <form:input id="correoCalendario" path="correoCalendario" class="form-control" aria-invalid="false" value="${usuario.correoCalendario}"/>
                                        </div> 
                                        <div class="form-group">
                                            <label>Digite la contraseña nueva</label>                            
                                            <form:password id="contrasenaNueva" path="contrasenaNueva" class="form-control" placeholder="Minimo 4 números, 4 letra minusculas y 2 mayusculas"/>     
                                        </div>
                                        <div class="form-group">
                                            <label>Digite de nuevo la contraseña nueva</label>                            
                                            <form:password id="contrasenaNueva2" path="contrasenaNueva2" class="form-control" placeholder="Minimo 4 números, 4 letra minusculas y 2 mayusculas"/>     
                                        </div>
                                        <!-- Boton para actualizar los datos -->
                                        <button id="actualizar" type="submit" style="background: #d34836" class="btn btn-danger">Actualizar cuenta</button>                                 
                                    </form:form>                          
                                </div>
                            </div>
                        </div>
                    </div>
                </div><!-- .animated -->
                <div style="margin-top: 95px"></div>
            </div><!-- .content -->            
         <!-- Carga los datos del footer -->
        <%@ include file="../General/DownPanel.jsp"%>
        </div><!-- /#right-panel -->
        <!-- Right Panel -->
        <!-- Carga de los archivos Javascript -->
        <%@ include file = "../General/scripts.jsp" %>
    </body>
</html>
