require(['jquery', 'layerWrapper','jquery.ajax.extension','anxin_qian'], function ($, layer) {
    function ajaxOuterFun(option,callback,alwaysFun,failFun) {
        var defaults={
            type:'POST',
            url:'',
            data:{}
        };
        var options=$.extend(defaults,option);
        $.ajax({
            type:options.type,
            data:options.data,
            url:options.url,
            dataType: 'json'
        }).done(function(data) {
            callback && callback(data);
        }).fail(function() {
            layer.msg('请求数据失败，请刷新页面重试！');
            options.thisDom.prop('disabled',false);

        });
    }
    //模拟真实的checkbox
    $.fn.initCheckbox=function(callback) {
        return $(this).each(function() {
            $(this).bind('click',function() {
                var $this=$(this);
                var checked=$this.find('input:checkbox').prop('checked');
                if(checked) {
                    $this.addClass("on");
                }
                else {
                    $this.removeClass("on");
                }
                callback && callback(this);
            })
        });
    };

    //安心初始
    (function() {
        var $safetyFrame=$('#safetySignedFrame');
            isAnxinUser=$('input.bind-data',$safetyFrame).data('is-anxin-user'),
            $closed=$('.safety-status-box.closed',$safetyFrame),
            $opened=$('.safety-status-box.opened',$safetyFrame);

        if(!isAnxinUser) {
            $closed.show();
        }
        else {
            $opened.show();
        }
        // 开启安心签服务
        $('#openSafetySigned').on('click',function() {
            var $this=$(this);
            $this.prop('disabled',true);
            ajaxOuterFun({
                thisDom:$this,
                url:' /anxinSign/createAccount'
            },function(response) {
                layer.msg('<span class="layer-msg-tip"><i></i>开启成功!</span>',{
                    skin:'msg-tip-box',
                    time: 1500,
                    area:['290px','90px']
                },function() {
                    //done
                    $closed.hide();
                    $opened.show();
                });
            },function() {
                //always
                $this.prop('disabled',true);
            },function() {
                //fail
                $this.prop('disabled',false);
            });
        });

        //
        $('.init-checkbox-style',$safetyFrame).initCheckbox(function(element) {
            var $parentBox=$(element).parents('.safety-status-box');
            //点击我已阅读并同意是否disable按钮
            var isCheck=$(element).hasClass('on'),
                $btnNormal=$parentBox.find('button.btn-open');

                if(isCheck) {
                    $btnNormal.prop('disabled',false);
                }
                else {
                    $btnNormal.prop('disabled',true);
                }

        });

        // 立即开通免短信授权服务
        $('#openAuthorization').on('click',function() {
            layer.open({
                type:1,
                title:'安心签免短信授权服务',
                area:['400px','220px'],
                shadeClose: false,
                content: $('#getSkipPhone')
            });
        });

        //倒计时函数
        var num = 60,Down;
        function countDown() {
            $('#getSkipCode').val(num + '秒后可重新获取').prop('disabled',true);
            $('#microPhone').css('visibility', 'hidden');
            if (num == 0) {
                clearInterval(Down);
                num = 60;
                $('#getSkipCode').val('重新获取验证码').prop('disabled',false);
                $('#microPhone').css('visibility', 'visible');
            }
            num--;
        }

        //获取验证码
        $('#getSkipPhone').on('click',function(event) {
            var getId=event.target.id,
                $this=$(this),
                isVoice; //是否语音获取
            if(!getId) {
                return;
            }
            else if(getId=='getSkipCode') {
                isVoice=false;
            }
            else if(getId=='microPhone') {
                isVoice=true;
            }
            if(getId=='getSkipCode' || getId=='microPhone') {
                ajaxOuterFun({
                    thisDom:$this,
                    url:'anxinSign/sendCaptcha',
                    data:{
                        isVoice:isVoice
                    }
                },function(data) {
                    if(data.success) {
                        countDown();
                        Down = setInterval(countDown, 1000);
                    }
                    else {
                        layer.msg('请求失败，请重试或联系客服！');
                    }

                })
            }

        });

        //验证验证码并开通短信服务
        $('#toOpenSMS').on('click',function() {
            var $this=$(this);
            $this.prop('disabled',true);
            ajaxOuterFun({
                thisDom:$this,
                url:'anxinSign/verifyCaptcha',
                data:{
                    captcha: $('#skipPhoneCode').val(),
                    skipAuth:true
                }
            },function(data) {
               if(data.success) {
                   layer.closeAll();
                   layer.msg('<span class="layer-msg-tip"><i></i>授权成功!</span>',{
                       skin:'msg-tip-box',
                       time: 1500,
                       area:['290px','90px']
                   },function() {
                        location.reload();
                   });
               }
               else {
                   $this.parents().find('.error').show();
                   $this.prop('disabled',false);
               }
            })
        })

    })();

    //安心签列表
    (function() {
        var $safetyList=$('#safetySignedList'),
            bindingSet=$('.binding-set',$safetyList),
            isSkipAuth=$('.bind-data',$safetyList).data('skip-auth'); //是否开启免验
        if(isSkipAuth) {
            $safetyList.find('.sms-open').show();
        }
        else {
            $safetyList.find('.sms-close').show();
        }

        $('.init-checkbox-style',$safetyList).initCheckbox(function(element) {
            var isCheck=$(element).hasClass('on'),
                $btnOK=$('#safetyToOpen ').find('.ok');
            if(isCheck) {
                $btnOK.prop('disabled',false);
            }
            else {
                $btnOK.prop('disabled',true);
            }
        });
        bindingSet.find('.setlink').on('click',function () {
            var $this=$(this),
                isOpen=$this.prev('i').hasClass('ok'),
                btnGroup,
                content;
            content=isOpen?$('#safetyToClose'):$('#safetyToOpen');
            layer.open({
                type:1,
                title:'安心签免短信授权服务',
                area:['400px','200px'],
                shadeClose: false,
                content: content
            });

        });

        $('.open-safety-box',$safetyList).on('click',function(event) {
            var $target=$(event.target),
                $this=$(this),
                isOpen,
                tipMsg;
            if(/cancel/.test(event.target.className)) {
                layer.closeAll();
                return;
            }
            else if(/ok/.test(event.target.className)) {
                var domID=this.id;
                if(domID=='safetyToOpen') {
                    //去开启
                    isOpen=true;
                    tipMsg='开启成功';
                }
                else if(domID=='safetyToClose') {
                    //去关闭
                    isOpen=false;
                    tipMsg='关闭成功';
                }

                ajaxOuterFun({
                    thisDom:$this,
                    url:'/anxinSign/switchSkipAuth',
                    data:{
                        "open":isOpen
                    }
                },function(data) {
                    if(data.success) {
                        layer.closeAll();
                        layer.msg(tipMsg,function() {
                            location.reload();
                        });
                    }
                    else {
                        layer.msg('请求失败，请重试或联系客服！');
                    }

                });
            }


        })

    })();

});


