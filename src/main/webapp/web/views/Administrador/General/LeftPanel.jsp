<!-- 
                Se crea una variable con la url actual 
                debido a que esta puede cambiar segun la 
                direccion de despligue EJE= "https://graduados.ufps.edu.co"
-->
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
    <!-- Left Panel -->
    <aside id="left-panel" class="left-panel ">
        <nav class="navbar navbar-expand-sm navbar-default " >
            <div class="navbar-header ">
                <button class="navbar-toggler" type="button" data-toggle="collapse" style="background: #d34836"
                        data-target="#main-menu" aria-controls="main-menu"
                        aria-expanded="false" aria-label="Toggle navigation">
                    <i class="fa fa-bars"></i>
                </button>
                <a class="navbar-brand " href="./" style="padding:10px;"><img
                        src="resources/images/logo.png" alt="Logo"></a> <a
                    class="navbar-brand hidden" href="./"><img
                        src="resources/images/logo2.png" alt="Logo"></a>
            </div>
            <div id="main-menu" class="main-menu collapse navbar-collapse ">
                <ul class="nav navbar-nav">
                    <li><a href="${contextPath}/indexAdmin"> <i
                                class="menu-icon fa fa-dashboard"></i>Panel de control
                        </a></li>
                    <h3 class="menu-title">Negocio</h3>
                    <!-- /.menu-title -->
                    <li><a id="categorias" href="${contextPath}/categorias"> <i
                                class="menu-icon ti-list"></i>Categorias
                        </a></li>
                    <li><a id="subcategorias" href="${contextPath}/subcategorias"> <i class="menu-icon ti-list-ol"></i>Subcategorias
                        </a></li>
                    <li><a id="itemsubcategorias" href="${contextPath}/itemsubcategorias"> <i class="menu-icon ti-list-ol"></i>Item de subcategoria
                        </a></li>
                    <li><a id="contenidos" href="${contextPath}/contenidos"> <i class="menu-icon fa fa-edit"></i>Contenido
                        </a></li>
                    <li><a id="noticias" href="${contextPath}/noticias"> <i
                                class="menu-icon fa fa-newspaper-o"></i>Noticias
                        </a></li>
                    <li><a id="actividades" href="${contextPath}/actividades"> <i
                                class="menu-icon fa fa-tasks"></i>Próximas actividades
                        </a></li>
                    <li><a id="novedades" href="${contextPath}/novedades"> <i
                                class="menu-icon fa fa-bullhorn"></i>Novedades
                        </a></li>
                    <li><a id="logos" href="${contextPath}/logos"> <i
                                class="menu-icon fa fa-university"></i>Logos
                        </a></li>
                    <li><a id="enlacesDeInteres" href="${contextPath}/enlacesDeInteres"> <i class="menu-icon ti-link"></i>Enlaces
                            de interés
                        </a></li>
                    <li><a id="galeriastest" href="${contextPath}/galerias"> <i
                                class="menu-icon fa fa-picture-o"></i>Galería
                        </a></li>
                    <li><a id="contactos" href="${contextPath}/contactos"> <i class="menu-icon ti-email"></i>Contactos
                        </a></li>
                    <li><a id="redes" href="${contextPath}/redes"> <i
                                class="menu-icon fa fa-facebook"></i>Redes sociales
                        </a></li>
                    <c:if test="${mostrarSuperAdmin eq 1}">
                        <li><a id="auditorias" href="${contextPath}/auditorias"> <i
                                    class="menu-icon fa fa-check-square"></i>Auditorías
                            </a></li>
                        <li><a id="usuarios" href="${contextPath}/usuarios"> <i
                                    class="menu-icon fa fa-users"></i>Usuarios
                            </a></li>
                    </c:if>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </nav>
    </aside>
    <!-- /#left-panel -->
    <!-- Left Panel -->