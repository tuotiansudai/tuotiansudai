/**
 * Created by CBJ on 2015/10/19.
 */
var commonFun={};
commonFun={
    initRadio:function($radio,$radioLabel) {
        var numRadio=$radio.length;
        if(numRadio) {
            $radio.each(function(key,option) {
                var $this=$(this);
                if($this.is(':checked')) {
                    $this.next('label').addClass('checked');
                }
                $this.next('label').click(function() {
                    var $thisLab=$(this);
                    if(!/checked/.test(this.className)) {
                        $radioLabel.removeClass('checked');
                        $thisLab.addClass('checked');
                    }
                });
            });

        }
    },
    checkBoxInit:function($checkbox,$label) {
        var numCheckbox=$checkbox.length;
        if(numCheckbox) {
            $checkbox.each(function(key,option) {
                var $this=$(this);
                if($this.is(':checked')) {
                    $this.next('label').addClass('checked');
                }
                $this.next('label').click(function() {
                    var $thisLab=$(this);
                    if(/checked/.test(this.className)) {
                        $thisLab.removeClass('checked');
                    }
                    else {
                        $thisLab.addClass('checked');
                    }
                });
            });

        }
    },
    parseURL:function(url) {
        var a =  document.createElement('a');
        a.href = url;
        return {
            source: url,
            protocol: a.protocol.replace(':',''),
            host: a.hostname,
            port: a.port,
            query: a.search,
            params: (function(){
                var ret = {},
                    seg = a.search.replace(/^\?/,'').split('&'),
                    len = seg.length, i = 0, s;
                for (;i<len;i++) {
                    if (!seg[i]) { continue; }
                    s = seg[i].split('=');
                    ret[s[0]] = s[1];
                }
                return ret;
            })(),
            file: (a.pathname.match(/\/([^\/?#]+)$/i) || [,''])[1],
            hash: a.hash.replace('#',''),
            path: a.pathname.replace(/^([^\/])/,'/$1'),
            relative: (a.href.match(/tps?:\/\/[^\/]+(.+)/) || [,''])[1],
            segments: a.pathname.replace(/^\//,'').split('/')
        };
    },
    popWindow:function(title,content,size) {
        if(!$('.popWindow').length) {
            var popW=[];
            popW.push('<div class="popWindow">');
            popW.push('<div class="ecope-overlay"></div>');
            popW.push('<div class="ecope-dialog">');
            popW.push('<div class="dg_wrapper">');

            popW.push('<div class="hd"><h3>'+title+' ' +
                '<em class="close" ></em></h3></div>');
            popW.push('<div class="bd">sss</div>');

            popW.push('</div></div></div>');
            $('body').append(popW.join(''));
            var $popWindow=$('.ecope-dialog'),
                size= $.extend({width:'560px'},size);
            $popWindow.css({
                width:size.width
            });
            var adjustPOS=function() {
                var scrollHeight=document.body.scrollTop || document.documentElement.scrollTop,
                    pTop=$(window).height()-$popWindow.height(),
                    pLeft=$(window).width()-$popWindow.width();
                $popWindow.css({'top':pTop/2+scrollHeight,left:pLeft/2});
                $popWindow.find('.bd').empty().append(content);
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
            $('.ecope-overlay,.popWindow').show();
        }

        $popWindow.delegate('.close','click',function() {
            $('.ecope-overlay,.popWindow').hide();
        })
    }
}

