var clientH = $(window).height();
var url = '/user/center';
if(!document.createElement('canvas').getContext && window.location.href.indexOf(url) == -1){
    $(function(){
        $('.warning_mask').css('display', 'block').css('height', clientH);
        $('.Warning').css('display', 'block');
    });
}
