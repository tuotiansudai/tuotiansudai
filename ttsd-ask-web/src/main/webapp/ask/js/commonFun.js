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

//匹配这些中文标点符号 。 ？ ！ ， 、 ； ： “ ” ‘ ' （ ） 《 》 〈 〉 【 】 『 』 「 」 ﹃ ﹄ 〔 〕 … — ～ ﹏ ￥ ／
//匹配英文符号 . ? ! , ; : "  ( )   [ ] { } $ %  ‰  ＋ @ ＃ ^ & ＊ ＝ － — /
//只能输入汉字，数字，字母，下划线，以及中文标点符号英文标点符号,中英文空格
//罗马数字 ⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩⅪⅫⅰⅱⅲⅳⅴⅵⅶⅷⅸⅹ  \u2160-\u2179
//①②③④⑤⑥⑦⑧⑨⑩⑪⑫⑬⑭⑮⑯⑰⑱ -- \u2460-\u2473
//⒈⒉⒊⒋⒌⒍⒎⒏⒐⒑⒒⒓⒔⒕⒖⒗⒘⒙⒚⒛  \u2488-\u249b
//ⒶⒷⒸⒹⒺⒻⒼⒽⒾⒿⓀⓁⓂⓃⓄⓅⓆⓇⓈⓉⓊⓋⓌⓍⓎⓏ  \u24b6-\u24cf
//ⓐⓑⓒⓓⓔⓕⓖⓗⓘⓙⓚⓛⓜⓝⓞⓟⓠⓡⓢⓣⓤⓥⓦⓧⓨⓩ  \u24d0-\u0020
//⒜⒝⒞⒟⒠⒡⒢⒣⒤⒥⒦⒧⒨⒩⒪⒫⒬⒭⒮⒯⒰⒱⒲⒳⒴⒵  \u249c-\u24b5

comm.matchiChSymbol=function(str) {
  var reg =  /^[a-zA-Z0-9_\u2460-\u2473\u2160-\u2179\s\u4e00-\u9fa5\uff0f\u3002\uff1f\uff01\uff0c\u3001\uff1b\uff1a\u201c\u201d\u2018\u2019\uff08\uff09\u300a\u300b\u3008\u3009\u3010\u3011\u300e\u300f\u300c\u300d\ufe43\ufe44\u3014\u3015\u2026\u2014\uff5e\ufe4f\uffe5,\/\.;\?\:!\(\)\{\}\$%‰\+\@\#\^\&\*=-\\"]+$/

    //如何含有特殊字符返回true
    return !reg.test(str);
}

$('#logout-link').click(function() {
    $('#logout-form').submit();
    return false;
});

module.exports = comm;
