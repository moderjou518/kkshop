$(function() {
    //多開modal時，覆蓋的透明灰色層可顯示於正確的位置
    $(document).on('show.bs.modal', '.modal', function (event) {
        var zIndex = 1040 + (10 * $('.modal:visible').length);
        $(this).css('z-index', zIndex);
        setTimeout(function() {
            $('.modal-backdrop').not('.modal-stack').css('z-index', zIndex - 1).addClass('modal-stack');
        }, 0);
    });
    
    //ajax執行時出現loading視窗
    if($("#_ajaxLoadingModal").length==0){
        var modalHtml = "";
        modalHtml+='<div id="_ajaxLoadingModal" class="modal fade" tabindex="-1" role="dialog"> ';
        modalHtml+='    <div class="modal-dialog modal-custom"> ';
        modalHtml+='        <div class="modal-content"> ';
        modalHtml+='            <div class="modal-header"> ';
        modalHtml+='                <h4 class="modal-title">Loading</h4> ';
        modalHtml+='                <button class="bootbox-close-button close" aria-hidden="true" type="button" data-dismiss="modal">×</button> ';
        modalHtml+='            </div> ';
        modalHtml+='            <div class="modal-body"> ';
        modalHtml+='                <div class="bootbox-body"> ';
        modalHtml+='                    <div id="loadingMsg" style="padding-left:5px;">Now loading, please wait...</div>';
        modalHtml+='                    <span class="ajax-loader-progress"></span> ';
        modalHtml+='                </div> ';
        modalHtml+='            </div> ';
        modalHtml+='        </div> ';
        modalHtml+='    </div> ';
        modalHtml+='</div> ';
        $("body").append(modalHtml);
    }
    var ajaxCnt = 0;
    $(document).on({
        ajaxStart: function() {
            //ajaxCnt+=1 ;
            $("#_ajaxLoadingModal").modal("show");            
        },
        ajaxStop: function() {
            //ajaxCnt-=1;
            //if(ajaxCnt<=0){
                setTimeout(function(){$("#_ajaxLoadingModal").modal("hide");},500);
                //ajaxCnt = 0;
            //}            
        },
        ajaxError: function() {
            setTimeout(function(){$("#_ajaxLoadingModal").modal("hide");},500);
        }
    });    
});
//表格需要十字標示時call的function
function bindXMark(){
    $('table.hms-xmark td').off("mouseenter");
    $('table.hms-xmark td').off("mouseleave");
    $('table.hms-xmark td').on("mouseenter",function(){
        $(this).parents("table").eq(0).find('td:nth-child(' + ($(this).index() + 1) + ')').addClass('hms-xmark-hover');
    })
    $('table.hms-xmark td').on("mouseleave",function(){
        $(this).parents("table").eq(0).find('td:nth-child(' + ($(this).index() + 1) + ')').removeClass('hms-xmark-hover');
    });    
}