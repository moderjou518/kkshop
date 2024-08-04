var HMS2_SlideMenu = {
        timer : null,
        aniSpeed:500,
        oriMenuWidth:"",
        oriResultWidth:"",
        easingType:"easeInCubic",
        oriColMum:"",
        oriMenuHeight:"",
        chkVersion:function(){
            var vs = 1;
            if($('head link[href^="/_util_lib/bootstrap/3"]').length===0) {
                vs = 2;
            }
            return vs;
        },
        slideMenuVer1:{
            slideMenuClose : function(speed) {
                $("body").css("overflow-y","hidden");
                HMS2_SlideMenu.oriMenuWidth = $("#hmsSlide_Menu").outerWidth();
                HMS2_SlideMenu.oriResultWidth = $("#hmsSlide_Content").outerWidth();
                HMS2_SlideMenu.oriColMum = $("#hmsSlide_Content").attr("class");
                $("#hmsSlide_Menu").animate({opacity:0,left:"-250px",width : "0px","padding-left":"0px","padding-right":"0px"},
                                            {duration:HMS2_SlideMenu.aniSpeed,
                                             easing:HMS2_SlideMenu.easingType
                });
                $("#hmsSlide_Content").animate({width : "100%","padding-left":"15px"},{duration:HMS2_SlideMenu.aniSpeed,
                                                                                       easing:HMS2_SlideMenu.easingType,
                                                                                       complete:function(){
                                                                                           $("#hmsSlide_Content").attr("class","col-md-12");
                                                                                           $("body").css("overflow-y","auto");
                                                                                       }
                });
                //$("#divOpenBarBtn").show("fade", HMS2_SlideMenu.aniSpeed, false);
                setTimeout(function(){$("#divOpenBarBtn").show("slide",200,false)},HMS2_SlideMenu.aniSpeed-150);            
            },
            slideMenuOpen : function(speed) {     
                $("#divOpenBarBtn").hide("slide", 200, false);
                $("body").css("overflow-y","hidden");
                $("#hmsSlide_Menu").animate({opacity:1,left:"0px",width : HMS2_SlideMenu.oriMenuWidth,"padding-left":"15px","padding-right":"15px"},
                                            {duration:HMS2_SlideMenu.aniSpeed,
                                             easing:HMS2_SlideMenu.easingType,
                                             complete:function(){
                                                 $("#hmsSlide_Menu").attr("style","");
                                             }
                });
                $("#hmsSlide_Content").animate({width : HMS2_SlideMenu.oriResultWidth+"px","padding-left":"0px"},
                                               {duration:HMS2_SlideMenu.aniSpeed,
                                                easing:HMS2_SlideMenu.easingType,
                                                complete:function(){
                                                            $("#hmsSlide_Content").attr("style","");
                                                            $("#hmsSlide_Content").attr("class",HMS2_SlideMenu.oriColMum)
                                                            $("body").css("overflow-y","auto");
                                                        }
                                              });
            }
        },
        slideMenuVer2:{
            slideMenuClose : function(speed) {
                var isIE = HMS2_SlideMenu.isIE();
                var nowMinWidth = $("#hmsSlide_Content").outerWidth();
                HMS2_SlideMenu.oriMenuWidth = $("#hmsSlide_Menu").css("flex-basis");
                HMS2_SlideMenu.oriResultWidth = $("#hmsSlide_Content").css("flex-basis");
                HMS2_SlideMenu.oriMenuHeight = $("#hmsSlide_Menu").outerHeight();
                $("#hmsSlide_Menu").css({"justify-content":"flex-start","overflow":"hidden"});
                $("#hmsSlide_Content").css({"flex-grow":1,"max-width":"none","justify-content":"flex-end"});
                if(isIE){//ie要特別調整內容寬度
                    var adjFBs = HMS2_SlideMenu.oriResultWidth;
                    adjFBs =parseFloat(adjFBs.split("%")[0])-1; 
                    $("#hmsSlide_Content").css({"flex-grow":12,"flex-basis":adjFBs+"%"});
                }                
                $("#hmsSlide_Menu").animate({opacity:0,height:"0px",left:"-250px","flex-basis":"0%","padding-left":"0px","padding-right":"0px"},
                                            {duration:HMS2_SlideMenu.aniSpeed,
                                             easing:HMS2_SlideMenu.easingType}
                );
                $("#hmsSlide_Content").animate({"padding-left":"15px"},{duration:HMS2_SlideMenu.aniSpeed,
                    easing:HMS2_SlideMenu.easingType
                });         
                setTimeout(function(){$("#divOpenBarBtn").show("slide",200,false)},HMS2_SlideMenu.aniSpeed-150);
            },
            slideMenuOpen : function(speed) {     
                $("#divOpenBarBtn").hide("slide", 200, false);
                $("#hmsSlide_Menu").animate({opacity:1,height:HMS2_SlideMenu.oriMenuHeight+"px",left:"0px","flex-basis" : HMS2_SlideMenu.oriMenuWidth,"padding-left":"15px","padding-right":"15px"},
                                            {duration:HMS2_SlideMenu.aniSpeed,
                                             easing:HMS2_SlideMenu.easingType,
                                             complete:function(){
                                                 $("#hmsSlide_Menu").attr("style","");
                                             }
                });
                $("#hmsSlide_Content").animate({"padding-left":"0px"},
                                               {duration:HMS2_SlideMenu.aniSpeed,
                                                easing:HMS2_SlideMenu.easingType,
                                                complete:function(){
                                                            $("#hmsSlide_Content").attr("style","");
                                                        }
                                              });
                }
        },
        slideMenuClose : function() {
            if(this.chkVersion()==1){
                this.slideMenuVer1.slideMenuClose();
            }
            else{
                this.slideMenuVer2.slideMenuClose();
            }
        },
        slideMenuOpen : function() {     
            if(this.chkVersion()==1){
                this.slideMenuVer1.slideMenuOpen();
            }
            else{
                this.slideMenuVer2.slideMenuOpen();
            }
        },
        isIE:function() {
            var ua = window.navigator.userAgent;
            var msie = ua.indexOf('MSIE ');
            if (msie > 0) {
                // 回傳版本 <=10 的版本
                return parseInt(ua.substring(msie + 5, ua.indexOf('.', msie)), 10);
            }
            var trident = ua.indexOf('Trident/');
            if (trident > 0) {
                // 回傳版本 >=11 的版本
                var rv = ua.indexOf('rv:');
                return parseInt(ua.substring(rv + 3, ua.indexOf('.', rv)), 10);
            }
            return false;
        }
    };

$(function(){
    $("#hmsSlider_collapseBtn").on("click",function(){
        var nowOpen = ($(this).attr("class").indexOf("open")!=-1)?false:true;
        if(nowOpen){
            $(this).addClass('open');
            if(v===1){
                HMS2_SlideMenu.slideMenuOpen();    
            }
            else if(v===2){
                HMS2_SlideMenu2.slideMenuOpen();
            }               
        }
        else{
            $(this).removeClass('open');
            if(v===1){
                HMS2_SlideMenu.slideMenuClose("Y");    
            }
            else if(v===2){
                HMS2_SlideMenu2.slideMenuClose("Y");
            }
        }
    })
})