require(['jquery', 'layerWrapper'], function ($, layer) {

    (function() {
        var $safetyFrame=$('#safetySignedFrame');
            $getHidParam=$('input.bind-data',$safetyFrame),
            $closed=$('.safety-status-box.closed',$getHidParam),
            $opened=$('.safety-status-box.opened',$getHidParam);


        function ajaxOuterFun(option,callback) {
            var defaults={
                type:'GET',
                data:''
            };
            this.options=$.extend(defaults,option);
            $.ajax({
                type:this.options.type,
                data:this.options.data,
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8'
            }).done(function(data) {
                callback && callback(data);
            }).fail(function(response) {
                layer.msg('请求数据失败，请刷新页面重试！');
            });
        }
        // 开启爱心签服务
        $('#openSafetySigned').on('click',function() {
            ajaxOuterFun({data:''},function(response) {
                var data;
                layer.msg('<span class="layer-msg-tip"><i></i>授权成功!</span>');
                $closed.hide();
                $opened.show();
            });
        });

        //模拟真实的checkbox
        $('.init-checkbox-style').on('click', function () {
            var $this = $(this),
                checked=$this.find('input:checkbox').prop('checked');
            $this.toggleClass("on");
        });

        // 立即开通免短信授权服务
        $('#openAuthorization').on('click',function() {

        });

    })();

});


