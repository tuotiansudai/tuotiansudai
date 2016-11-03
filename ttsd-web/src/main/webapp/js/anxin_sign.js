require(['jquery', 'layerWrapper'], function ($, layer) {
    function ajaxOuterFun(option,callback) {
        var defaults={
            type:'POST',
            data:'',
            url:''
        };
        var options=$.extend(defaults,option);
        $.ajax({
            type:options.type,
            data:options.data,
            dataType: 'json',
            contentType: 'application/json; charset=UTF-8'
        }).done(function(data) {
            callback && callback(data);
        }).fail(function() {
            layer.msg('请求数据失败，请刷新页面重试！');
        });
    }

    //模拟真实的checkbox
    $.fn.initCheckbox=function() {
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

            })
        });
    };

    (function() {
        var $safetyFrame=$('#safetySignedFrame');
            $getHidParam=$('input.bind-data',$safetyFrame),
            $closed=$('.safety-status-box.closed',$getHidParam),
            $opened=$('.safety-status-box.opened',$getHidParam);

        // 开启安心签服务
        $('#openSafetySigned').on('click',function() {
            ajaxOuterFun({
                type:'POST',
                url:' /anxinSign/createAccount'
            },function(response) {

                layer.msg('<span class="layer-msg-tip"><i></i>开启成功!</span>',{
                    skin:'msg-tip-box',
                    time: 1500,
                    area:['290px','90px']
                },function() {
                    $closed.hide();
                    $opened.show();
                });
            });
        });

        $('.init-checkbox-style',$safetyFrame).initCheckbox();

        // 立即开通免短信授权服务
        $('#openAuthorization').on('click',function() {

        });

    })();

    //安心签列表
    (function() {
        var $safetyList=$('#safetySignedList');


    })();

});


