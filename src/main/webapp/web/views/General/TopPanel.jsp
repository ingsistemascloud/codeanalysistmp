<div id="barra-superior" class="header-v8">
    <!-- Topbar blog -->
    <div class="blog-topbar">
        <div class="topbar-search-block">
            
            <c:set var="urlapp" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}" />

            <div class="container">
                <form method="GET" action="https://www.google.es/search">
                    <input type="hidden" name="domains" value="${urlapp}">
                    <input type="hidden" name="sitesearch" value="${urlapp}" checked="">
                    <input type="text" id="s" name="q" class="form-control" placeholder="Buscar...">
                    <div class="search-close"><i class="icon-close"></i></div>
                </form>
            </div>



        </div>
        <div class="container">
            <div class="row">
                <div class="col-sm-7 col-xs-7">
                    <div class="topbar-toggler" style="font-size: 10px; color: #eee; letter-spacing: 1px; text-transform: uppercase;">
                        <span class="fa fa-angle-down"></span> PERFILES
                    </div>
                    <ul class="topbar-list topbar-menu">
                        <li>
                            <a href="https://ww2.ufps.edu.co/universidad/perfiles/aspirantes/952" target="_blank">
                                <i class="fa fa-users"></i>
                                Aspirantes
                            </a>
                        </li>
                        <li>
                            <a href="https://ww2.ufps.edu.co/universidad/perfiles/estudiantes/953" target="_blank">
                                <i class="fa fa-user"></i>
                                Estudiantes
                            </a>
                        </li>
                        <li>
                            <a href="https://ww2.ufps.edu.co/universidad/perfiles/egresados/954" target="_blank">
                                <i class="fa fa-graduation-cap"></i>
                                Graduados
                            </a>
                        </li>
                        <li>
                            <a href="https://docentes.ufps.edu.co/" target="_blank">
                                <i class="fa fa-user-circle"></i>
                                Docentes
                            </a>
                        </li>
                        <li>
                            <a href="">
                                <i class="fa fa-briefcase"></i>
                                Empresarios
                            </a>
                        </li>
                        <li class="cd-log_reg hidden-sm hidden-md hidden-lg">
                            <strong>
                                <a class="cd-signup" href="javascript:void(0);">
                                    Lenguaje
                                </a>
                            </strong>
                            <ul class="topbar-dropdown">
                                <li>
                                    <a href="#">
                                        Ingles
                                    </a>
                                </li>
                                <li>
                                    <a href="#">
                                        Espa�ol
                                    </a>
                                </li>
                            </ul>
                        </li>
                       
                    </ul>
                </div>
                <div class="col-sm-5 col-xs-5 clearfix">
                    <i class="fa fa-search search-btn pull-right">
                    </i>
                    <ul class="topbar-list topbar-log_reg pull-right visible-sm-block visible-md-block visible-lg-block">
                        <li class="cd-log_reg home" style="padding: 0px 12px;">
                            <div id="google_translate_element">
                            </div>
                            <script type="text/javascript">
                                function googleTranslateElementInit() {
                                    new google.translate.TranslateElement(
                                            {
                                                pageLanguage: 'es',
                                                includedLanguages: 'en,fr,it',
                                                layout: google.translate.TranslateElement.InlineLayout.SIMPLE,
                                                autoDisplay: false
                                            },
                                            'google_translate_element');
                                }
                            </script>
                            <script type="text/javascript" src="//translate.google.com/translate_a/element.js?cb=googleTranslateElementInit"></script>
                        </li>
                    </ul>
                </div>
            </div>
            <!--/end row-->
        </div>
        <!--/end container-->
    </div>
    <!-- End Topbar blog -->
</div>

