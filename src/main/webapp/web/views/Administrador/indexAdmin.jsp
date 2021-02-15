<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!doctype html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang=""> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8" lang=""> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9" lang=""> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang=""> <!--<![endif]-->
    <head>
        <%@ include file = "General/css.jsp" %>
    </head>
    <body>

        <!-- Left Panel -->

        <%@ include file = "General/LeftPanel.jsp" %>

        <!-- Left Panel -->

        <!-- Right Panel -->

        <div id="right-panel" class="right-panel">

            <!-- Header-->
            <header id="header" class="header">

                <div class="header-menu">
                    <div class="col-sm-7">
                        <a id="menuToggle" class="menutoggle pull-left">
                            <i class="fa fa fa-tasks">
                            </i>
                        </a>
                        <div class="page-header float-left">
                            <div class="page-title">
                                <ol class="breadcrumb text-right">
                                    <li class="active">
                                        <a href="#">FINGWEB</a>
                                    </li>                                            
                                </ol>
                            </div>
                        </div>    
                    </div>
                    <%@ include file = "General/Configuracion.jsp" %>
                </div>

            </header><!-- /header -->


            <div class="breadcrumbs">
                <div class="col-sm-4">
                    <div class="page-header float-left">
                        <div class="page-title">
                            <h1>Panel de control</h1>
                        </div>
                    </div>
                </div>
                <div class="col-sm-8">
                    <div class="page-header float-right">
                        <div class="page-title">
                            <ol class="breadcrumb text-right">

                                <li><a class="btn btn-danger" style="background: #d34836" href="${contextPath}/generarInforme">Descargar reporte de conteo</a></li>

                            </ol>
                        </div>
                    </div>
                </div>
            </div>

            <div class="content mt-3">

                <div class="col-lg-3 col-md-6">
                    <a href="${contextPath}/categorias" style="color: black">
                        <div class="social-box google-plus">                        
                            <i class="fa fa-list"></i>
                            <span>Categorías</span>
                            <strong>(<span class="count">${catidadCategorias}</span>)</strong>

                        </div>
                    </a>
                    <!--/social-box-->
                </div><!--/.col--> 


                <div class="col-lg-3 col-md-6">
                    <a href="${contextPath}/subcategorias" style="color: black">
                        <div class="social-box google-plus">
                            <i class="fa fa-list-ol"></i>                        
                            <span>Subcategorias</span>
                            <strong>(<span class="count">${catidadSubCategorias}</span>)</strong>
                        </div>
                    </a>
                    <!--/social-box-->
                </div><!--/.col--> 


                <div class="col-lg-3 col-md-6">
                    <a href="${contextPath}/itemsubcategorias" style="color: black">
                        <div class="social-box google-plus">
                            <i class="fa fa-list-ol"></i>                        
                            <span>ItemSubcategorias</span>
                            <strong>(<span class="count">${catidadItemSubCategorias}</span>)</strong>
                        </div>
                    </a>
                    <!--/social-box-->
                </div><!--/.col--> 


                <div class="col-lg-3 col-md-6">
                    <a href="${contextPath}/contenidos" style="color: black">
                        <div class="social-box google-plus">
                            <i class="fa fa-edit"></i>
                            <span>Contenido</span>
                            <strong>(<span class="count">${catidadContenidos}</span>)</strong>
                        </div>
                    </a>
                    <!--/social-box-->
                </div><!--/.col--> 


                <div class="col-lg-3 col-md-6">
                    <a href="${contextPath}/noticias" style="color: black">
                        <div class="social-box google-plus">
                            <i class="fa fa-newspaper-o"></i>
                            <span>Noticias</span>
                            <strong>(<span class="count">${catidadNoticias}</span>)</strong>
                        </div>
                    </a>
                    <!--/social-box-->
                </div><!--/.col--> 

                <div class="col-lg-3 col-md-6">
                    <a href="${contextPath}/actividades" style="color: black">
                        <div class="social-box google-plus">
                            <i class="fa fa-tasks"></i>                        
                            <span>Próximas actividades</span>
                            <strong>(<span class="count">${catidadActividades}</span>)</strong>
                        </div>
                    </a>
                    <!--/social-box-->
                </div><!--/.col--> 

                <div class="col-lg-3 col-md-6">
                    <a href="${contextPath}/novedades" style="color: black">
                        <div class="social-box google-plus">
                            <i class="fa fa-bullhorn"></i>
                            <span>Novedades</span>
                            <strong>(<span class="count">${catidadNovedades}</span>)</strong>
                        </div>
                    </a>
                    <!--/social-box-->
                </div><!--/.col-->             


                <div class="col-lg-3 col-md-6">
                    <a href="${contextPath}/logos" style="color: black">
                        <div class="social-box google-plus">
                            <i class="fa fa-university"></i>
                            <span>Logos</span>
                            <strong>(<span class="count">${catidadLogos}</span>)</strong>
                        </div>
                    </a>
                    <!--/social-box-->
                </div><!--/.col--> 

                <div class="col-lg-3 col-md-6">
                    <a href="${contextPath}/enlacesDeInteres" style="color: black">
                        <div class="social-box google-plus">
                            <i class="fa fa-link"></i>                        
                            <span>Enlaces de interés</span>
                            <strong>(<span class="count">${catidadEnlaces}</span>)</strong>
                        </div>
                    </a>
                    <!--/social-box-->
                </div><!--/.col-->    

                <div class="col-lg-3 col-md-6">
                    <a href="${contextPath}/galerias" style="color: black">
                        <div class="social-box google-plus">
                            <i class="fa fa-picture-o"></i>                        
                            <span>Galería</span>
                            <strong>(<span class="count">${catidadGalerias}</span>)</strong>
                        </div>
                    </a>
                    <!--/social-box-->
                </div><!--/.col-->                 

                <div class="col-lg-3 col-md-6">
                    <a href="${contextPath}/contactos" style="color: black">
                        <div class="social-box google-plus">
                            <i class="ti-email"></i>
                            <span>Contactos</span>
                            <strong>(<span class="count">${catidadContactos}</span>)</strong>
                        </div>
                    </a>
                    <!--/social-box-->
                </div><!--/.col-->         

                <div class="col-lg-3 col-md-6">
                    <a href="${contextPath}/redes" style="color: black">
                        <div class="social-box google-plus">
                            <i class="fa fa-google-plus"></i>
                            <span>Redes sociales</span>
                            <strong>(<span class="count">${catidadRedesSociales}</span>)</strong>
                        </div>
                    </a>
                    <!--/social-box-->
                </div><!--/.col-->      
                <c:if test="${mostrarSuperAdmin eq 1}">
                <div class="col-lg-3 col-md-6">
                    <a href="${contextPath}/auditorias" style="color: black">
                        <div class="social-box google-plus">
                            <i class="menu-icon fa fa-check-square"></i>
                            <span>Auditorías</span>
                            <strong>(<span class="count">${catidadAuditorias}</span>)</strong>
                        </div>
                    </a>
                    <!--/social-box-->
                </div><!--/.col-->               
                <div class="col-lg-3 col-md-6">
                    <a href="${contextPath}/usuarios" style="color: black">
                        <div class="social-box google-plus">
                            <i class="menu-icon fa fa-users"></i>
                            <span>Usuarios</span>
                            <strong>(<span class="count">${catidadUsuarios}</span>)</strong>
                        </div>
                    </a>
                    <!--/social-box-->
                </div><!--/.col-->
                </c:if>
            </div> <!-- .content -->
            
         <!-- Carga los datos del footer -->        
         <footer>
             <div class="breadcrumbs">
                 <div class="col-sm-12">
                     <div class="page-header float-left">
                         <div class="page-title">
                             <br/>
                             <h6>2019 © All Rights Reserved. Desarrollado por: <a href="#">VAVM - Departamento de Sistemas</a></h6>
                             <br/>
                         </div>
                     </div>
                 </div>
             </div>    
         </footer>

        </div><!-- /#right-panel -->
        <!-- Right Panel -->

        <!-- Carga de los archivos Javascript -->
        <%@ include file = "General/scripts.jsp" %>


    </body>
</html>
