/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    var arr = $(".dropdown-menu");
    var list = jQuery.makeArray(arr);

    for (var i = 0; i < list.length; i++) {
        var $txt = $(list[i]);
        if ($txt.text().trim().length < 1) {
            $txt.parent().removeClass('dropdown');
            $txt.parent().removeClass('dropdown-submenu');
            $txt.parent().addClass('nodropdown');
            $txt.remove();      
        }
    }        
    
    $(".dropdown:nth-last-child(-n+3)").hover(function() {
        $("ul ul", this).css('right', '100%');
    });  
    
     $(".dropdown:not(:nth-last-child(-n+3))").hover(function() {
        $("ul ul", this).css('left', '100%');
    });    

});

