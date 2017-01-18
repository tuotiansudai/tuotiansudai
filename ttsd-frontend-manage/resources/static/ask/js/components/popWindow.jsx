function popWindow(title,content,size,load) {
    var isload=load?true:false;
    if(!$('.popWindow').length) {
        var popW=[];
        popW.push('<div class="popWindow-overlay"></div>');
        popW.push('<div class="popWindow"> <div class="pop-title">'+title+'<em class="close" ></em></div> <p>'+content+'</p></div>');

        $('body').append(popW.join(''));
        var $popWindow=$('.popWindow'),
            size= $.extend({width:'560px'},size);
        $popWindow.css({
            width:size.width
        });
        var adjustPOS=function() {
            var scrollHeight=document.body.scrollTop || document.documentElement.scrollTop,
                pTop=$(window).height()-$popWindow.height(),
                pLeft=$(window).width()-$popWindow.width();
            $popWindow.css({'top':pTop/2+scrollHeight,left:pLeft/2});
            $('.popWindow-overlay').css({'height':$('body').height()});

        }
        adjustPOS();
        $(window).resize(function() {
            adjustPOS();
        });
        var mousewheel = document.all?"mousewheel":"DOMMouseScroll";
        $(window).bind('mousewheel',function() {
            adjustPOS();
        })

        $popWindow.on('click', '.close', function () {
            $('.popWindow-overlay,.popWindow').hide();
            if (isload) {
                window.location.reload();
            }
        })
    }
    else {
        $('.popWindow-overlay,.popWindow').show();
    }
};

export default popWindow;
