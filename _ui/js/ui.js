(function(global, factory) {
    if (typeof global.jQuery !== 'undefined') { // jQuery is loaded
        factory(global);
    } else {
        throw new Error( "jQuery is not loaded" );
    }
})(typeof window !== "undefined" ? window : this, function(window, noGlobal) {
    // private屬性與方法
    var version = 0.1,
        strundefined = typeof undefined,
        evg = window.evg || {}; // 定義namespace, 名稱固定為evg
 
    evg.util = evg.util || {}; // 定義namespace, 名稱util可自定為其他單字
    evg.util.ui = evg.util.ui || function() { // 定義物件名稱, 名稱fn可自定為其他單字
        // your code
    };
         
    evg.util.ui.prototype = { // 定義public屬性與方法
        lightbox: function() {
            $(document).delegate('*[data-toggle="lightbox"]', 'click', function(event) {
                event.preventDefault();
                $(this).ekkoLightbox();
            }); 
        },
        wizard_steps: function() {
             $(".div-wizard-basic").steps({
                     headerTag: "h3",
                     bodyTag: "section",
                     transitionEffect: "slideLeft",
                     autoFocus: true
                 });
             $(".div-wizard-vertical").steps({
                     headerTag: "h3",
                     bodyTag: "section",
                     transitionEffect: "slideLeft",
                     stepsOrientation: "vertical"
                 });
        },
        panel_accordion: function() {
            var acc = document.getElementsByClassName("accordion-meun");
            var i;
        
            for (i = 0; i < acc.length; i++) {
                acc[i].onclick = function(){
                    this.classList.toggle("active");
                    this.nextElementSibling.classList.toggle("show");
              }
            }
        
            var acc2 = document.getElementsByClassName("accordion");
            var i;
        
            for (i = 0; i < acc2.length; i++) {
                acc2[i].onclick = function(){
                    this.classList.toggle("active");
                    this.nextElementSibling.classList.toggle("show");
              }
            }
        }
    };
     
    // Self-Executing Anonymous Function 
    (function() {
        // your code
    })();
     
    evg.util.ui = new evg.util.ui(); // 產生evg.util.ui物件
     
    // 將evg, evg.util, evg.util.ui暴露為全域變數
    if ( typeof noGlobal === strundefined ) {
        window.evg = evg || {};
        window.evg.util = evg.util || {};
        window.evg.util.ui = evg.util.ui;
    }
    return evg.util.ui;
});