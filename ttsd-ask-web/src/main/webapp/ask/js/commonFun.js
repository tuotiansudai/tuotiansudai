var _ = require('underscore');
var comm = {};
comm.pathNameKey = function (key) {
    var parm = location.search.split('?')[1], parmObj;
    if (_.isUndefined(parm)) {
        return '';
    }
    else {
        parmObj = parm.split('&');
        for (var i = 0, len = parmObj.length; i < len; i++) {
            if (parmObj[i].split('=')[0] == key) {
                return parmObj[i].split('=')[1];
            }
        }
    }
};

comm.serializeObject = function (formData) {

    var o = {};
    $.each(formData, function () {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

comm.initToken = function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $(document).ajaxError(function (event, jqXHR, ajaxSettings, thrownError) {
        if (jqXHR.status == 403) {
            if (jqXHR.responseText) {
                var data = JSON.parse(jqXHR.responseText);
                window.location.href = data.directUrl + (data.refererUrl ? "?redirect=" + data.refererUrl : '');
            }
        }
    });
};

comm.popWindow=function(title,content,size,load) {
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
        }
        else {
             $('.popWindow-overlay,.popWindow').show();
        }
    
        $popWindow.delegate('.close','click',function() {
           $('.popWindow-overlay,.popWindow').hide();
           if(isload) {
                window.location.reload();
           }
        })
};

$('#logout-link').click(function() {
    $('#logout-form').submit();
});

module.exports = comm;
