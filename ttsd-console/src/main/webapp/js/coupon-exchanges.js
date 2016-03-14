require(['jquery','bootstrap', 'bootstrapDatetimepicker','csrf'], function($) {
    $(function() {
        var $tooltip = $('.add-tooltip'),
             $tipCom=$('.tip-container');

        $tooltip.length?$tooltip.tooltip():false;

        //confirm event
        $('body').delegate('.confirm-btn','click',function(e) {
            e.preventDefault();
            var $self=$(this),
                $parentTd = $self.parents('td'),
                thisId=$self.attr('data-id');//data id
            var couponType = $self.attr('data-type');
            if (!confirm("是否确认执行此操作?")) {
                return;
            }else{
                $.ajax({
                    url: '/activity-manage/coupon/'+thisId+'/active',
                    type: 'POST',
                    dataType: 'json'
                })
                    .done(function(res) {
                        if(res.data.status){
                            $parentTd.html('<label><i class="check-btn add-check"></i><button class="loan_repay already-btn btn-link inactive-btn" data-id="'+thisId+'">已生效</button></label>');
                            $parentTd.prev().html('-');
                        }else{
                            $tipCom.show().find('.txt').text('操作失败！');
                        }
                    })
                    .fail(function(res) {
                        $self.addClass('confirm-btn').text('操作失败');
                        $tipCom.show().find('.txt').text('请求发送失败，请刷新重试！');
                    });
            }
        })

    });
});