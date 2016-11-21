var comm = {};
comm.pathNameKey = function (key) {
    var parm = location.search.split('?')[1], parmObj;
    if (parm==undefined) {
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

    $popWindow.on('click','.close',function() {
        $('.popWindow-overlay,.popWindow').hide();
        if(isload) {
            window.location.reload();
        }
    })
};

$('#logout-link').click(function() {
    $('#logout-form').submit();
    return false;
});

$.fn.checkFrom = function () {
    return this.each(function () {
        var $ele = $(this);
        var name=this.name,
            value=$ele.val();
        switch(name) {
            case 'question':
                return utils.validLen($ele,30);
                break;
            case 'addition':
                return utils.validLen($ele,10000);
                break;
            case 'captcha':
                return utils.validLen($ele,5);
                break;
            case 'answer':
                return utils.validLen($ele,5);
                break;
            default:
                return utils.radioChecked($ele)
                break;
        }

    });
};

$.fn.autoTextarea = function(options) {
    var defaults={
        maxHeight:null,
        minHeight:$(this).height()
    };
    var opts = $.extend({},defaults,options);
    return $(this).each(function() {
        $(this).bind("paste cut keydown keyup focus blur",function(){
            var height,style=this.style;
            this.style.height = opts.minHeight + 'px';
            if (this.scrollHeight > opts.minHeight) {
                if (opts.maxHeight && this.scrollHeight > opts.maxHeight) {
                    height = opts.maxHeight;
                    style.overflowY = 'scroll';
                } else {
                    height = this.scrollHeight;
                    style.overflowY = 'hidden';
                }
                style.height = height + 'px';
            }
        });
    });
};

module.exports = comm;

