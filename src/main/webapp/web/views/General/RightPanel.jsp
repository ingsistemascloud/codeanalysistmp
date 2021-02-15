<div class="col-md-4">
    <div class="gdl-custom-sidebar">
        <h3 class="gdl-custom-sidebar-title-m">Secciones</h3>
    </div>
    <div class="gdl-custom-sidebar">
        <a href="${contextPath}/servicios/noticias">
            <button class="ufps-btn-accordionlike">
                Noticias
            </button>
        </a>
        <a href="${contextPath}/servicios/novedades">
            <button class="ufps-btn-accordionlike">
                Novedades
            </button>
        </a>
        <a href="${contextPath}/servicios/actividades">
            <button class="ufps-btn-accordionlike">
                Proximas actividades
            </button>
        </a>
    </div>

    <div id="proximasContent" class="gdl-custom-sidebar gdl-custom-sidebar-nofirst">
        <h3 class="gdl-custom-sidebar-title-m">
            Actividades del mes
            <div class="ufps-tooltip" style="float:right;">
                <span style="float:right;">
                    <a>
                        <img src="resources/rsc/img/calendar.png">
                    </a>
                </span>
            </div>
        </h3>

        

        <div class="clear" style="height:10px;">
        </div>

    </div><!--proximasContent-->

    <iframe src="${calendario}&amp;height=300&amp;wkst=1&amp;bgcolor=%23ffffff&amp;color=%23039BE5&amp;showTitle=0&amp;showDate=1&amp;showPrint=0&amp;showTabs=0&amp;showCalendars=0&amp;showTz=0" style="border-width:0" width="350" height="300" frameborder="0" scrolling="no"></iframe>
    
    <div class="gdl-custom-sidebar gdl-custom-sidebar-nofirst">
        <h3 class="gdl-custom-sidebar-title-m">Galer&iacute;as</h3>

        <c:forEach var="galeria" items="${galerias}">																					
            <div class="col-md-6 col-sm-6 col-xs-6 col-md-margin-bottom-30" style="padding-left: 14px; margin-bottom: 14px;">
                <div class="easy-block-v1">
                    <a href="${contextPath}/servicios/galeria?id=${galeria.id}">
                        <img src="${galeria.primeraImagen}" alt="">
                    </a>
                    <div class="easy-block-v1-badge rgba-black" style="z-index:0; width:100%; top: 0px; color:#fff; font-size:0.9em;">
                        ${galeria.nombre}      
                    </div>
                </div>
            </div>
        </c:forEach>		
    </div>


    <div style="clear:both;"></div>


    <div style="clear:both; min-height:26px;">
    </div>
</div><!--col-md-4-->