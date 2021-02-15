<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!doctype html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang=""> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8" lang=""> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9" lang=""> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js" lang="">
    <!--<![endif]-->
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>Registrar primer usuario</title>
        <meta name="description" content="Sufee Admin - HTML5 Admin Template">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link rel="apple-touch-icon" href="apple-icon.png">
        <link rel="shortcut icon" href="resources/images/favicon.ico">

        <link rel="stylesheet" href="resources/assets/css/normalize.css">
        <link rel="stylesheet" href="resources/assets/css/bootstrap.min.css">
        <link rel="stylesheet" href="resources/assets/css/font-awesome.min.css">
        <link rel="stylesheet" href="resources/assets/css/themify-icons.css">
        <link rel="stylesheet" href="resources/assets/css/flag-icon.min.css">
        <link rel="stylesheet" href="resources/assets/css/cs-skin-elastic.css">
        <!-- <link rel="stylesheet" href="resources/assets/css/bootstrap-select.less"> -->
        <link rel="stylesheet" href="resources/assets/scss/style.css">
        <link type="text/css" rel="stylesheet" href="resources/css/footer-v1.min.css">

        <link
            href='https://fonts.googleapis.com/css?family=Open+Sans:400,600,700,800'
            rel='stylesheet' type='text/css'>

    </head>


    <body class="bg-white">

        <%
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
            response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        %>

        <c:set var="contextPath" value="${pageContext.request.contextPath}" />

        <div class="sufee-login d-flex align-content-center flex-wrap">
            <div class="container">
                <div class="login-content">
                    <div class="login-logo">
                        <a href="${contextPath}/"> <img class="align-content"
                                                        src="resources/images/logoLogin.png" alt="">
                        </a>
                    </div>
                    <!-- Si hubo un registro exitoso muestra el mensaje-->
                    <c:if test="${not empty result}">
                        <div class="sufee-alert alert with-close alert-success alert-dismissible fade show">
                            <c:out value='${result}' />
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </c:if>
                    <c:if test="${not empty wrong}">
                        <div class="sufee-alert alert with-close alert-danger alert-dismissible fade show">
                            <span class="badge badge-pill badge-danger">Error</span>
                            <c:out value='${wrong}' />
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </c:if>



                    <div class="login-form">
                        <!-- <form> -->
                        <form:form action="guardarSuperAdmin" method="post" modelAttribute="usuario">

                            <!-- Campo para digitar el nombre -->
                            <div class="form-group">
                                <label>Código</label>
                                <form:input id="codigo" path="codigo" class="form-control" aria-invalid="true" placeholder="Código institucional del usuario"/>
                            </div>
                            <!-- Campo para digitar el nombre -->
                            <div class="form-group">
                                <label>Correo institucional</label>
                                <form:input id="correo" path="correo" class="form-control" aria-invalid="true" placeholder="ejemplo@ufps.edu.co"/>
                            </div>
                            <!-- Campo para digitar el nombre -->
                            <div class="form-group">
                                <label>Correo calendario</label>
                                <form:input id="correoCalendario" path="correoCalendario" class="form-control" aria-invalid="true" placeholder="ejemplo@gmail.com"/>
                            </div>
                            <div class="form-group">
                                <label>Digite la contraseña</label>                            
                                <form:password id="contrasenaNueva" path="contrasenaNueva" class="form-control" aria-invalid="true" placeholder="Minimo 4 números, 4 letra minusculas y 2 mayusculas"/>     
                            </div>

                            <div class="form-group">
                                <label>Digite de nuevo la contraseña</label>                            
                                <form:password id="contrasenaNueva2" path="contrasenaNueva2" class="form-control" aria-invalid="true" placeholder="Minimo 4 números, 4 letra minusculas y 2 mayusculas"/>     
                            </div>
                            <button id="registrar" type="submit" style="background: #d34836" class="btn btn-danger btn-flat m-b-30 m-t-30">Registrar</button>

                        </form:form>

                    </div>
                </div>
            </div>
        </div>
                                                        
        <div style="margin-top: 25px"></div>                                    
        <!-- Carga los datos del footer -->  
        <div class="footer-v1 off-container">
			
            <!--/footer-->
            <div class="copyright">
                    <div class="container">
                            <div class="row">
                                    <div class="col-md-12 text-center">
                                            <p>
                                                    2019 © All Rights Reserved. Desarrollado por: 
                                                    <a href="#" style="color: #aa1916">VAVM - Departamento de Sistemas</a>
                                            </p>
                                    </div>
                            </div>
                    </div>
            </div>
		<!--/copyright-->
        </div>

        <script src="resources/assets/js/vendor/jquery-2.1.4.min.js"></script>
        <script src="resources/assets/js/popper.min.js"></script>
        <script src="resources/assets/js/plugins.js"></script>
        <script src="resources/assets/js/main.js"></script>

    </body>
</html>