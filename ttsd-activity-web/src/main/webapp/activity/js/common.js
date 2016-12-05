define(['jquery'], function ($) {

    var commonFun={
        //加密
        compile:function (strId,realId) {
            var realId=realId+'';
            var strIdObj=strId.split(''),
                realLen=realId.length;
            for(var i=0;i<11;i++) {
                strIdObj[2*i+2]=realId[i]?realId[i]:'a';
            }
            return strIdObj.join('');

        },
        //解密
        uncompile:function (strId) {
            var strId=strId+'';
            var strIdObj=strId.split(''),
                realId=[];
            for(var i=0;i<11;i++) {
                realId[i]=strIdObj[2*i+2];
            }

            var stringRealId=realId.join(''),
                getNum=stringRealId.match(/\d/gi);
            return getNum.join('');
        },

        /* init radio style */
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
        popWindow:function(contentHtml,area) {
            var $shade=$('<div class="shade-body-mask"></div>');
            var $popWindow=$(contentHtml),
                size= $.extend({width:'460px',height:'370px'},area);
            $popWindow.css({
                width:size.width,
                height:size.height
            });
            var adjustPOS=function() {
                var scrollHeight=document.body.scrollTop || document.documentElement.scrollTop,
                    pTop=$(window).height()-$popWindow.height(),
                    pLeft=$(window).width()-$popWindow.width();
                $popWindow.css({'top':pTop/2,left:pLeft/2});
                $shade.height($('body').height());
                $('body').append($popWindow).append($shade);
            }
            adjustPOS();


            $('.close-btn,.go-close',$popWindow).on('click',function() {
                $popWindow.remove();
                $shade.remove();
            })
        },

        // 验证用户是否处于登陆状态
        isUserLogin:function() {
            var LoginDefer=$.Deferred(); //在函数内部，新建一个Deferred对象
            $.ajax({
                url: '/isLogin',
                type: 'GET'
            })
                .done(function(data) {
                    if(data) {
                        //如果data有值，说明token已经过期，用户处于未登陆状态，并且需要更新token
                        LoginDefer.reject(data);
                        $("meta[name='_csrf']").remove();
                        $('head').append($(data.responseText));

                        var token = $("meta[name='_csrf']").attr("content");
                        var header = $("meta[name='_csrf_header']").attr("content");
                        $(document).ajaxSend(function (e, xhr, options) {
                            xhr.setRequestHeader(header, token);
                        });
                    }
                    else {
                        //如果data为空，说明用户处于登陆状态，不需要做任何处理
                        LoginDefer.resolve();
                    }
                })
                .fail(function() {
                    LoginDefer.reject();
                });

            return LoginDefer.promise(); // 返回promise对象
        }
    };

    return commonFun;
});




