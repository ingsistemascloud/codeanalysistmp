<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!doctype html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang=""> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8" lang=""> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9" lang=""> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js" lang="">
    <!--<![endif]-->
    <head>
        <!-- Assets for editor-->
        <link href='https://fonts.googleapis.com/css?family=Dosis|Candal' rel='stylesheet' type='text/css'>
        <link href='resources/assets/css/editor.css' rel='stylesheet' type='text/css'>
        <link href='resources/assets/css/font-awesome.css' rel='stylesheet' type='text/css'>
        <%@ include file="../General/css.jsp"%>
        <link rel="stylesheet" href="resources/assets/css/load.css">
    </head>
    <body>
        <!-- Left Panel -->
        <%@ include file="../General/LeftPanel.jsp"%>
        <!-- /#left-panel -->
        <!-- Right Panel -->
        <div id="right-panel" class="right-panel">
            <!-- Header-->
            <header id="header" class="header">
                <div class="header-menu">
                    <div class="col-sm-7">
                        <a id="menuToggle" class="menutoggle pull-left"><i
                                class="fa fa fa-tasks"></i></a>
                        <div class="page-header float-left">
                            <div class="page-title">
                                <ol class="breadcrumb text-right">
                                    <li><a href="${contextPath}/indexAdmin">Panel de
                                            control</a></li>
                                    <li><a href="${contextPath}/contenidos">Contenidos</a></li>
                                    <li class="active"><a href="#">Actualizar contenido</a></li>
                                </ol>
                            </div>
                        </div>
                    </div>
                    <!-- Area en donde se encuentra la foto del usuario y la barra de opciones -->
                    <%@ include file="../General/Configuracion.jsp"%>
                </div>
            </header>
            <!-- /header -->
            <!-- Header-->
            <!-- Contenedor del formulario -->
            <div class="content mt-3">
                <div class="animated fadeIn">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="card">
                                <!-- Titulo de la ventana -->
                                <div class="card-header">
                                    <strong class="card-title">Actualizar contenido</strong>
                                </div>
                                <div class="card-body">
                                    <!-- Si hubo un error en el registro muestra el mensaje-->
                                    <div id="exito">
                                    </div>
                                    <div id="error">
                                    </div>
                                    <!-- Formulario -->
                                    <form:form id="formContenido" method="post" modelAttribute="contenido">
                                        <input type="hidden" id="ruta" class="form-control" value = "${contextPath}" />
                                        <input type="hidden" id="asoc" name="asoc" value="${contenido.asociacion}"> 
                                        <form:input type="hidden" id="id" path="id" class="form-control" value = "${contenido.id}" />
                                        <!-- Campo para escoger el tipo de asosiacion -->
                                        <div class="form-group">
                                            <label for="text-input" class=" form-control-label">Tipo de asociación</label>
                                            <form:select path="tipoAsociacion" id="tipoAsociacion" class="form-control" onchange="cambiarDeTipoDeAsosiacion();">
                                                <form:option value="0"
                                                             label="Seleccione el tipo de asociación" />
                                                <form:options items="${tiposAsociacion}" />
                                            </form:select>
                                        </div>
                                        <!-- Campo para escoger la asosiacion -->
                                        <div class="form-group">
                                            <label for="text-input" class=" form-control-label">Asociación</label>
                                            <form:select path="asociacion" id="asociacion" name="asociacion"
                                                         class="form-control">
                                                <form:option value="0" label="Seleccione la asociación" />
                                            </form:select>
                                        </div>
                                        <!-- Campo para digitar el tipo de contenido -->
                                        <div class="form-group">
                                            <label for="textarea-input" class=" form-control-label">Tipo de contenido</label>
                                            <form:select path="tipoContenido.id" id="tipoContenido"
                                                         class="form-control" onchange="cambiarDeTipoDeContenido();">
                                                <form:options items="${tiposContenido}" />
                                            </form:select>
                                        </div>
                                        <!-- Campo para digitar el nombre -->
                                        <div id="titulo" class="form-group">
                                            <label for="text-input" class=" form-control-label">Título o nombre</label>
                                            <form:input id="nombre" path="nombre" class="form-control"	value = "${contenido.nombre}" />
                                        </div>
                                        <div id="urll" class="form-group" style="display: none;">
                                            <label for="text-input" class=" form-control-label">URL a direccionar</label> 
                                            <input id="url" name="url" type="text"
                                                   class="form-control" path="url"
                                                   placeholder="http://gidis.ufps.edu.co:8080/SeGre/"
                                                   aria-required="true" aria-invalid="false" value="${contenido.url}">
                                        </div>
                                        <div style="display: none;">
                                            <input type="file" id="imagen" name="imagen" onchange="cargarImagen()" accept="images/*">
                                        </div>
                                        <div style="display: none;">
                                            <input type="file" id="video" name="video" onchange="revisarVideo(this);" accept="video/*">
                                        </div>
                                        <div style="display: none;">
                                            <input type="file" id="archivo" name="archivo" onchange="revisarArchivo(this);" accept=".xlsx,.xls,image/*,.doc, .docx,.ppt, .pptx,.txt,.pdf">
                                        </div>			
                                        <div style="display: none;">
                                            <input type="file" id="archivoSom" name="archivoSom" onchange="revisarFraseSombreada(this);" accept=".xlsx,.xls,image/*,.doc, .docx,.ppt, .pptx,.txt,.pdf">
                                        </div>																
                                        <div id="pagina" class="form-group" >
                                            <label for="textarea-input" class=" form-control-label">Contenido</label>
                                            <div class="toolbar">
                                                <a href="#" data-command='undo' title="Deshacer" style="width: 40px;height: 40px;margin: 2px;"><i class='fa fa-undo'></i></a>
                                                <a href="#" data-command='redo' title="Repetir" style="width: 40px;height: 40px;margin: 2px;"><i class='fa fa-repeat'></i></a>
                                                <div class="fore-wrapper" style="width: 40px;height: 40px;margin: 2px;">
                                                    <i class='fa fa-font' style='color:#C96;'></i>
                                                    <div class="fore-palette">
                                                    </div>
                                                </div>
                                                <div class="back-wrapper" style="width: 40px;height: 40px;margin: 2px;"><i class='fa fa-font' style='background:#C96;'></i>
                                                    <div class="back-palette">
                                                    </div>
                                                </div>
                                                <a href="#" data-command='bold' title="Negrita" style="width: 40px;height: 40px;margin: 2px;"><i class='fa fa-bold'></i></a>
                                                <a href="#" data-command='italic' title="Cursiva" style="width: 40px;height: 40px;margin: 2px;"><i class='fa fa-italic'></i></a>
                                                <a href="#" data-command='underline' title="Subrayado" style="width: 40px;height: 40px;margin: 2px;"><i class='fa fa-underline'></i></a>
                                                <a href="#" data-command='strikeThrough' title="Tachado" style="width: 40px;height: 40px;margin: 2px;"><i class='fa fa-strikethrough'></i></a>
                                                <a href="#" data-command='justifyLeft' title="Alinear a la izquierda" style="width: 40px;height: 40px;margin: 2px;"><i class='fa fa-align-left'></i></a>
                                                <a href="#" data-command='justifyCenter' title="Centrar" style="width: 40px;height: 40px;margin: 2px;"><i class='fa fa-align-center'></i></a>
                                                <a href="#" data-command='justifyRight' title="Alinear a la derecha" style="width: 40px;height: 40px;margin: 2px;"><i class='fa fa-align-right'></i></a>
                                                <a href="#" data-command='justifyFull' title="Justificar" style="width: 40px;height: 40px;margin: 2px;"><i class='fa fa-align-justify'></i></a>
                                                <a href="#" data-command='outdent' title="Disminuir sangria" style="width: 40px;height: 40px;margin: 2px;"><i class='fa fa-outdent'></i></a>
                                                <a href="#" data-command='indent' title="Aumentar sangria" style="width: 40px;height: 40px;margin: 2px;"><i class='fa fa-indent'></i></a>
                                                <a href="#" data-command='insertUnorderedList' title="Viñetas" style="width: 40px;height: 40px;margin: 2px;"><i class='fa fa-list-ul'></i></a>
                                                <a href="#" data-command='insertOrderedList' title="Numeración" style="width: 40px;height: 40px;margin: 2px;"><i class='fa fa-list-ol'></i></a>
                                                <a href="#" data-command='h1' title="Título" style="width: 40px;height: 40px;margin: 2px;">H1</a>
                                                <a href="#" data-command='h2' title="Subtítulo" style="width: 40px;height: 40px;margin: 2px;">H2</a>
                                                <a href="#" data-command='p' title="Texto" style="width: 40px;height: 40px;margin: 2px;">P</a>
                                                <a href="#" data-command='createlink' title="Crear vínculo" style="width: 40px;height: 40px;margin: 2px;"><i class='fa fa-link'></i></a>
                                                <a href="#" data-command='unlink' title="Quitar vínculo" style="width: 40px;height: 40px;margin: 2px;"><i class='fa fa-unlink'></i></a>
                                                <a href="#" data-command='insertFileLink' title="Insertar enlace de archivo" style="width: 40px;height: 40px;margin: 2px;"><i class='fa fa-file-text-o'></i></a>
                                                <a href="#" data-command='insertFile' title="Insertar archivo" style="width: 40px;height: 40px;margin: 2px;"><i class='fa fa-file-archive-o'></i></a>
                                                <a href="#" data-command='insertimageU' title="Insertar imagen" style="width: 40px;height: 40px;margin: 2px;"><i class='fa fa-file-image-o'></i></a>
                                                <a href="#" data-command='insertVideo' title="Insertar video" style="width: 40px;height: 40px;margin: 2px;"><i class='fa fa-file-video-o'></i></a>
                                                <a href="#" data-command='insertVideoYoutube' title="Insertar video de youtube" style="width: 40px;height: 40px;margin: 2px;"><i class='fa fa-youtube'></i></a>
                                                <a href="#" data-command='subscript' title="Subíndice" style="width: 40px;height: 40px;margin: 2px;"><i class='fa fa-subscript'></i></a>
                                                <a href="#" data-command='superscript' title="Sobrescrito" style="width: 40px;height: 40px;margin: 2px;"><i class='fa fa-superscript'></i></a>
                                            </div>
                                            <div name="tt"> 
                                                <div id='editor' contenteditable>
                                                    ${contenido.contenido}
                                                </div>									
                                            </div>
                                        </div>
                                        <!-- Boton para registrar los datos -->
                                        <button id='editar' type="button" onclick="enviarDatos()" style="background: #d34836" class="btn btn-danger">Actualizar</button>
                                    </form:form>
                                </div>
                            </div>
                        </div>
                    </div>
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
        <script src="resources/assets/js/editor.js"></script>  	
        <script type="text/javascript">
                                            cambiarDeTipoDeAsosiacion();
                                            cambiarTDA();
                                            cambiarDeTipoDeContenido();
                                            
                                            function cambiarTDA() {
                                                document.getElementById('asociacion').value = document.getElementById('asoc').value;
                                            }
                                            
                                            function enviarDatos() {
                                                // Aqui se obtiene el tipo de asociacion		
                                                var selectBoxTA = document.getElementById("tipoAsociacion");
                                                var selectedValueTA = selectBoxTA.options[selectBoxTA.selectedIndex].value;
                                                // Aqui se obtiene la asociacion
                                                var selectBoxA = document.getElementById("asociacion");
                                                var selectedValueA = selectBoxA.options[selectBoxA.selectedIndex].value;
                                                // Aqui se obtiene el tipo de contenido
                                                var selectBoxTC = document.getElementById("tipoContenido");
                                                var selectedValueTC = selectBoxTC.options[selectBoxTC.selectedIndex].value;
                                                // Aqui se obtiene el nombre o titlo
                                                var titulo = document.getElementById("nombre").value;
                                                // Aqui se obtiene el nombre o titlo
                                                var idContenido = document.getElementById("id").value;
                                                // Variable para guardar el contenido
                                                var content = "";
                                                var conn = [];
                                                // Variable para guardar el tipo de contenido
                                                var tipoCon = "";
                                                document.getElementsByName("tt")[0].setAttribute("id", "loader");
                                                document.getElementById("editor").setAttribute("style", "display:none;");
                                                // Aqui consulto si es link o pagina normal
                                                if (selectedValueTC == 2) {
                                                    content = document.getElementById("url").value;
                                                    titulo = "LINK";
                                                    tipoCon = "LINK";
                                                } else {
                                                    // Aqui obtengo el contenido
                                                    content = document.getElementById('editor').innerHTML;
                                                    // Aqui lo segmento
                                                    var size = content.length;
                                                    var di = 1000;
                                                    for (var i = 0; size > 0; ) {
                                                        if (size > di) {
                                                            conn.push(content.substring(i, (i + di)));
                                                            size = size - di;
                                                            i = i + di;
                                                        } else {
                                                            conn.push(content.substring(i, (i + size)));
                                                            size = size - di;
                                                        }
                                                    }
                                                    tipoCon = "PAGINA NORMAL";
                                                }
                                                var ruta = document.getElementById("ruta").value;
                                                var formData = {
                                                    tipoAsociacion: selectedValueTA,
                                                    asociacion: selectedValueA,
                                                    tipoContenido: {
                                                        id: selectedValueTC,
                                                        nombre: tipoCon,
                                                        description: ''
                                                    },
                                                    nombre: titulo,
                                                    contenido: content,
                                                    url: "",
                                                    id: idContenido,
                                                    conn: conn
                                                };
                                                $.ajax({
                                                    type: "POST",
                                                    contentType: "application/json",
                                                    url: ruta + "/servicios/actualizarInformacion",
                                                    data: JSON.stringify(formData),
                                                    success: function (result) {
                                                        var contextPath = document.getElementById("ruta").value;
                                                        if (result == 'Actualizado') {
                                                            window.location.replace(`${contextPath}/contenidos?resultado=actualizado`);
                                                        } else {
                                                            pintarRegistroNoExitoso(result.trim());
                                                        }
                                                        document.getElementsByName("tt")[0].setAttribute("id", "");
                                                        document.getElementById("editor").setAttribute("style", "block");
                                                    },
                                                    error: function (e) {
                                                        pintarRegistroNoExitoso("Error en el sistema. Contacte al administrador.");
                                                        console.log("ERROR: ", e);
                                                        document.getElementsByName("tt")[0].setAttribute("id", "");
                                                        document.getElementById("editor").setAttribute("style", "block");
                                                    }
                                                });

                                            }
                                            
                                            function cambiarDeTipoDeAsosiacion() {
                                                var selectBox = document.getElementById("tipoAsociacion");
                                                var selectedValue = selectBox.options[selectBox.selectedIndex].value;
                                                var idAsociacion = document.getElementById("asoc").value;
                                                var ruta = document.getElementById("ruta").value;
                                                $.ajax({
                                                    type: "POST",
                                                    contentType: "application/json",
                                                    url: ruta + "/servicios/asosiacionesCompletas?tipoAsociacion=" + selectedValue + "&asociacion=" + idAsociacion,
                                                    async: false,
                                                    success: function (result) {
                                                        var resultado = JSON.stringify(result);
                                                        var asosiaciones = resultado.split("\",");
                                                        eliminarOpciones(document.getElementById("asociacion"));
                                                        agregarOpcion(document.getElementById("asociacion"), 0, "Seleccione la asociacion");
                                                        for (var i = 0; i < asosiaciones.length; i++) {
                                                            var id = asosiaciones[i].substring(1, asosiaciones[i].length).split(":")[0].replace('\"', '').replace('\"', '');
                                                            var te = asosiaciones[i].substring(1, asosiaciones[i].length).split(":")[1];
                                                            if (te != "") {
                                                                var nombre = te.replace('\"', '').replace('\"', '');
                                                                if (nombre.indexOf("}") != -1) {
                                                                    nombre = nombre.replace('}', '');
                                                                }
                                                                agregarOpcion(document.getElementById("asociacion"), id, nombre);
                                                            }
                                                        }
                                                    },
                                                    error: function (e) {
                                                        alert("Error!")
                                                        console.log("ERROR: ", e);
                                                    }
                                                });
                                            }
                                            
                                            function cambiarDeTipoDeContenido() {
                                                var selectBox = document.getElementById("tipoContenido");
                                                var selectedValue = selectBox.options[selectBox.selectedIndex].value;
                                                if (selectedValue == 2) {
                                                    document.getElementById("urll").setAttribute("style", "");
                                                    document.getElementById("pagina").setAttribute("style", "display: none;");
                                                    document.getElementById("titulo").setAttribute("style", "display: none;");
                                                } else {
                                                    document.getElementById("urll").setAttribute("style", "display: none;");
                                                    document.getElementById("pagina").setAttribute("style", "");
                                                    document.getElementById("titulo").setAttribute("style", "");
                                                }
                                            }

                                            function eliminarOpciones(selectbox) {
                                                var i;
                                                for (i = selectbox.options.length - 1; i >= 0; i--) {
                                                    selectbox.remove(i);
                                                }
                                            }

                                            function agregarOpcion(selectbox, id, nombre) {
                                                var opt = document.createElement("option");
                                                opt.value = id;
                                                opt.text = nombre;
                                                selectbox.options.add(opt);
                                            }

                                            function pintarRegistroExitoso() {
                                                var exito = document.getElementById("exito");
                                                var div = document.createElement("DIV");
                                                div.setAttribute("class", "sufee-alert alert with-close alert-success alert-dismissible fade show");
                                                var texto = document.createTextNode("Contenido actualizado con éxito");
                                                div.appendChild(texto);
                                                var boton = document.createElement("BUTTON");
                                                boton.setAttribute("type", "button");
                                                boton.setAttribute("class", "close");
                                                boton.setAttribute("data-dismiss", "alert");
                                                boton.setAttribute("aria-label", "Close");
                                                var span = document.createElement("span");
                                                span.setAttribute("aria-hidden", "true");
                                                var textos = document.createTextNode("X");
                                                span.appendChild(textos);
                                                boton.appendChild(span);
                                                div.appendChild(boton);
                                                exito.appendChild(div);
                                            }

                                            function pintarRegistroNoExitoso(mensaje) {
                                                var error = document.getElementById("error");
                                                var div = document.createElement("DIV");
                                                div.setAttribute("class", "sufee-alert alert with-close alert-danger alert-dismissible fade show");
                                                var texto = document.createTextNode(mensaje);
                                                div.appendChild(texto);
                                                var boton = document.createElement("BUTTON");
                                                boton.setAttribute("type", "button");
                                                boton.setAttribute("class", "close");
                                                boton.setAttribute("data-dismiss", "alert");
                                                boton.setAttribute("aria-label", "Close");
                                                var span = document.createElement("span");
                                                span.setAttribute("aria-hidden", "true");
                                                var textos = document.createTextNode("X");
                                                span.appendChild(textos);
                                                boton.appendChild(span);
                                                div.appendChild(boton);
                                                error.appendChild(div);
                                            }
        </script>   
    </body>
</html>
