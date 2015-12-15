require(['jquery','bootstrap', 'bootstrapDatetimepicker','csrf'], function($) {
    $(function() {
        var $body=$('body'),
            $confirmBtn=$('.confirm-btn'),//conirm button
            $tooltip = $('.add-tooltip'),
            $tipCom=$('.tip-container');

        $tooltip.length?$tooltip.tooltip():false;

        //confirm event
        $confirmBtn.on('click',function(e) {
            e.preventDefault();
            var $self=$(this),
                thisId=$self.attr('data-id');//data id
            if (!confirm("是否确认执行此操作?")) {
                return;
            }else{
                $.ajax({
                    url: '/activity-manage/coupon/'+thisId+'/active',
                    type: 'POST',
                    dataType: 'json'
                })
                .done(function(data) {
                    if(data == 'success'){
                        $self.text('已生效').addClass('already-btn')
                            .siblings('.check-btn').addClass('add-check');
                    }else if(data == 'fail'){
                        $self.text('确认生效').removeClass('already-btn')
                            .siblings('.check-btn').removeClass('add-check');
                    }else{
                        $tipCom.show().find('.txt').text('操作失败！');
                    }
                })
                .fail(function(data) {
                    $self.addClass('confirm-btn').text('操作失败');
                    $tipCom.show().find('.txt').text('请求发送失败，请刷新重试！');
                });
            }
        });
    });
});