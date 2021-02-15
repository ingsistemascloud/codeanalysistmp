<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang=""> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8" lang=""> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9" lang=""> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js" lang="">
    <!--<![endif]-->
    <head>
        <%@ include file="../General/css.jsp"%>
    </head>
    <body>
        <!-- Left Panel -->
        <%@ include file="../General/LeftPanel.jsp"%>
        <!--/ Left Panel -->
        <!-- Right Panel -->
        <div id="right-panel" class="right-panel">
            <!-- Header-->
            <header id="header" class="header">
                <!-- Header menu -->
                <div class="header-menu">
                    <div class="col-sm-7">
                        <a id="menuToggle" class="menutoggle pull-left"> <i
                                class="fa fa fa-tasks"> </i>
                        </a>
                        <div class="page-header float-left">
                            <div class="page-title">
                                <ol class="breadcrumb text-right">
                                    <li><a href="${contextPath}/indexAdmin">Panel de
                                            control</a></li>
                                    <li class="active"><a href="#">Auditorías</a></li>
                                </ol>
                            </div>
                        </div>
                    </div>
                    <!-- Area en donde se encuentra la foto del usuario y la barra de opciones -->
                    <%@ include file="../General/Configuracion.jsp"%>
                </div>
                <!-- /Header menu -->
            </header>
            <!-- /header -->
            <div class="content mt-3">
                <!-- Div animated -->
                <div class="animated fadeIn">
                    <!-- Row -->
                    <div class="row">
                        <!-- Col 12 -->
                        <div class="col-md-12">
                            <!-- Card -->
                            <div class="card">
                                <div class="card-header">
                                    <strong class="card-title">Auditorías</strong>
                                </div>
                                <div class="card-body">
                                    <!-- Tabla con las novedades -->
                                    <table id="bootstrap-data-table"
                                           class="table table-striped table-bordered">
                                        <div style="display: -webkit-inline-box;">
                                            <select id="tabla" name="tabla" class="form-control" style="width:300px;margin-bottom: 20px;margin-right: 10px;">
                                                <option value="0">Seleccione filtro</option>
                                                <option value="1">Archivo</option>
                                                <option value="2">Categoria</option>
                                                <option value="3">Contacto</option>
                                                <option value="4">Contenido</option>
                                                <option value="5">Enlace interes</option>
                                                <option value="6">Galeria</option>
                                                <option value="7">Noticia</option>
                                                <option value="8">Novedad</option>
                                                <option value="9">Proxima actividad</option>
                                                <option value="10">Red Social</option>
                                                <option value="11">Subcategoria</option>
                                                <option value="12">Tipo contenido</option>
                                                <option value="13">Usuario</option>
                                            </select>
                                            <!-- Boton que indica la accion para registrar una subcategoria -->
                                            <a id="consultar" href="javascript:ShowSelected();" style="background: #d34836" class="btn btn-danger">Consultar</a> <br> <br>                                 
                                        </div>
                                        <script type="text/javascript">
                                            function ShowSelected()
                                            {
                                                /* Para obtener el texto */
                                                var combo = document.getElementById("tabla");
                                                var selected = combo.options[combo.selectedIndex].text;
                                                location.href = "${contextPath}/consultarAuditorias?tabla=" + selected + "";
                                            }
                                        </script>
                                        <thead>
                                            <tr>
                                                <th scope="col" style="width: 2%">Identificador</th>
                                                <th scope="col" style="width: 26%">Operación</th>
                                                <th scope="col" style="width: 26%">Descripción</th>
                                                <th scope="col" style="width: 26%">Fecha y hora del suceso</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="auditoria" items="${auditorias}">
                                                <tr>
                                                    <th style="font-weight: normal;">${auditoria.idAuditoria}</th>
                                                    <th style="font-weight: normal;">${auditoria.accion} </td>											
                                                    <th style="font-weight: normal;">De la tabla "${auditoria.tabla}" la información con id "${auditoria.idRegistro}" y nombre o correo "${auditoria.nombre}".</th>
                                                    <th style="font-weight: normal;">${auditoria.fecha} ${auditoria.hora}</th>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                    <!-- /Tabla -->
                                </div>
                                <!-- /card-body -->
                            </div>
                            <!-- /card -->
                        </div>
                        <!-- /Col 12 -->
                    </div>
                    <!-- /Row -->
                </div>
                <!-- .animated -->
                <div style="margin-top: 95px"></div>
            </div>
            <!-- .content -->            
         <!-- Carga los datos del footer -->
        <%@ include file="../General/DownPanel.jsp"%>
        </div>
        <!-- /#right-panel -->
        <!-- Right Panel -->
        <!-- Carga de los archivos Javascript -->
        <%@ include file="../General/scripts.jsp"%>
        <script type="text/javascript">
            $(document).ready(function () {
                $('#bootstrap-data-table-export').DataTable();
            });
        </script>
    </body>
</html>