<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<div id="menu-principal" class="header-v6 header-white-transparent header-sticky" style="position: relative;">

    <%@ include file="../General/TopPanel.jsp"%>

    <div class="header-v8 img-logo-superior" style="background-color: #aa1916;">
        <!--=== Parallax Quote ===-->
        <div class="parallax-quote parallaxBg" style="padding: 30px 30px;">
            <div class="parallax-quote-in" style="padding: 0px;">
                <div class="row">
                    <div class="col-md-6 col-sm-4 col-xs-5 text-left">
                        <a href="${contextPath}/"> 
                            <img id="logo-header" src="${logoHorizontal.imBase64image}" alt="" style="max-height: 180px;">
                        </a>
                    </div>
                    <div class="col-md-6 col-ms-8 col-xs-8 text-right">
                        <a href="http://www.ufps.edu.co/">
                            <img class="header-banner" src="resources/rsc/img/logo_ufps.png" style="max-height: 160px;" alt="Escudo de la Universidad Francisco de Paula Santander">
                        </a>
                    </div>
                </div>
            </div>
        </div>
        <!--=== End Parallax Quote ===-->
    </div>

    <div class="menu-responsive">
        <!-- Logo -->
        <a class="logo logo-responsive" href="#" style="margin-left:5px;">

            <img src="${logoHorizontal.imBase64image}" alt="Logo" style="max-height: 50px;">

        </a>
        <!-- End Logo -->
        <!-- Toggle get grouped for better mobile display -->
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-responsive-collapse">
            <span class="sr-only">Cambiar navegaci&oacute;n</span>
            <span class="fa fa-bars"></span>
        </button>
        <!-- End Toggle -->
    </div>


    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse mega-menu navbar-responsive-collapse">
        <div class="containermenu">
            <ul class="nav navbar-nav" style="float: left;">
                <c:forEach var="categoria" items="${categoriasContenido}">
                    <c:if test="${not empty categoria.contenido.tipoContenido.id}">
                            <c:if test="${categoria.contenido.tipoContenido.id eq 2}">
                                <li class="nodropdown">
                                    <a href="${categoria.contenido.contenido}" class="dropdown-toggle disabled" data-toggle="dropdown">
                                        ${categoria.nombre}
                                    </a>		
                                </li>
                            </c:if>
                            <c:if test="${categoria.contenido.tipoContenido.id eq 1}">
                                <li class="nodropdown">
                                    <a href="${contextPath}/servicios/componente?id=${categoria.id}&componente=categoria" class="dropdown-toggle disabled" data-toggle="dropdown">${categia.nombre}</a>
                                </li>
                            </c:if>
                                           
                    </c:if>            
                    <c:if test="${empty categoria.contenido.tipoContenido.id}">
                        <li class="dropdown">
                            <a href="${categoria.contenido.contenido}" class="dropdown-toggle" data-toggle="dropdown">${categoria.nombre}</a>
                            <ul class="dropdown-menu">
                            <c:forEach var="subcategoria" items="${subcategoriasContenido}">
                                    <c:if test="${subcategoria.categoria.id == categoria.id}">
                                        <c:if test="${not empty subcategoria.contenido.tipoContenido.id}">
                                            <c:if test="${subcategoria.contenido.tipoContenido.id eq 2}">
                                                <li class="nodropdown-submenu">
                                                    <a href="${subcategoria.contenido.contenido}" class="dropdown-toggle disabled" data-toggle="dropdown">
                                                        ${subcategoria.nombre}
                                                    </a>
                                                </li>    
                                            </c:if>
                                            <c:if test="${subcategoria.contenido.tipoContenido.id eq 1}">
                                                <li class="nodropdown-submenu">
                                                    <a href="${contextPath}/servicios/componente?id=${subcategoria.id}&componente=subcategoria" class="dropdown-toggle disabled" data-toggle="dropdown">
                                                        ${subcategoria.nombre}
                                                    </a>
                                                </li>    
                                            </c:if>  
                                        </c:if>    
                                    </c:if>
                                    <c:if test="${subcategoria.categoria.id == categoria.id}">
                                        <c:if test="${empty subcategoria.contenido.tipoContenido.id}">
                                            <li class="dropdown-submenu">
                                                <a>
                                                    ${subcategoria.nombre}
                                                </a>
                                                <ul class="dropdown-menu">
                                                <c:forEach var="itemsubcategoria" items="${itemsubcategoriasContenido}">
                                                    <c:if test="${itemsubcategoria.subcategoria.id == subcategoria.id}">
                                                        <c:if test="${not empty itemsubcategoria.contenido.tipoContenido.id}">
                                                            <c:if test="${itemsubcategoria.contenido.tipoContenido.id eq 2}">
                                                                <li class="nodropdown-menu">
                                                                    <a href="${itemsubcategoria.contenido.contenido}" class="dropdown-toggle disabled" data-toggle="dropdown">
                                                                        <i class="fa fa-angle-double-right" aria-hidden="true"></i>
                                                                        ${itemsubcategoria.nombre}
                                                                    </a>
                                                                </li>    
                                                            </c:if>
                                                            <c:if test="${itemsubcategoria.contenido.tipoContenido.id eq 1}">
                                                                <li class="nodropdown-menu">
                                                                    <a href="${contextPath}/servicios/componente?id=${itemsubcategoria.id}&componente=itemsubcategoria" class="dropdown-toggle disabled" data-toggle="dropdown">
                                                                        <i class="fa fa-angle-double-right" aria-hidden="true"></i>
                                                                        ${itemsubcategoria.nombre}
                                                                    </a>
                                                                </li>    
                                                            </c:if>
                                                        </c:if>        
                                                    </c:if>
                                                    <c:if test="${itemsubcategoria.subcategoria.id == subcategoria.id}">            
                                                        <c:if test="${empty itemsubcategoria.contenido.tipoContenido.id}">
                                                            <li class="nodropdown-menu">
                                                                <a href="${itemsubcategoria.contenido.contenido}">
                                                                    <i class="fa fa-angle-double-right" aria-hidden="true"></i>
                                                                    ${itemsubcategoria.nombre}
                                                                </a>
                                                            </li>       
                                                        </c:if>            
                                                    </c:if>    
                                                </c:forEach>
                                                </ul>                
                                            </li>
                                        </c:if>
                                            
                                    </c:if>            
                            </c:forEach>
                            </ul>    
                        </li>        
                    </c:if>
                </c:forEach>                        
            </ul>                                            
        </div>
    </div>
    <!--/navbar-collapse-->
    
</div>
