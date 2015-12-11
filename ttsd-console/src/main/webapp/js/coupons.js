require(['jquery','bootstrap', 'bootstrapDatetimepicker'], function($) {
    $(function() {
        var $body=$('body'),
            $confirmBtn=$('.confirm-btn'),//确认生效按钮
            $tooltip = $('.add-tooltip');

        $tooltip.length?$tooltip.tooltip():false;

        /**
         * @msg  {[string]} //文字信息
         * @return {[html]} //生成html
         */
        function showErrorMessage(string) {
            var html = '<div class="tip-container">';
            html += '<div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert">';
            html += '<button type="button" class="close" data-dismiss="alert" aria-label="Close">';
            html += '<span aria-hidden="true">&times;</span>';
            html += '</button>';
            html += '<span class="txt">创建失败：' + string + '</span>';
            html += '</div>';
            html += '</div>';
            $body.append(html);
        }

        //关闭警告提示
        // $body.on('click', '.alert-dismissible .close', function () {
        //     $submitBtn.removeAttr('disabled');
        //     if (!!currentErrorObj) {
        //         currentErrorObj.focus();
        //     }
        // });

        //确认生效事件
        $confirmBtn.on('click',function(e) {
            e.preventDefault();
            var $self=$(this),
                thisId=$self.attr('data-id');//当前数据

            $.ajax({
                url: '/path/to/file',
                type: 'POST',
                dataType: 'json',
                data: {id: thisId},
            })
            .done(function(data) {
                $self.text('已生效').addClass('already-btn');
            })
            .fail(function(data) {
                $self.addClass('confirm-btn').text('确认失败');
                showErrorMessage('请求发送失败，请刷新重试！');
            })
            .always(function(data) {
                $self.removeClass('confirm-btn').text('正在确认中');
            });
            
        });
        
    });
});