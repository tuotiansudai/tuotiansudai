require(['jquery','bootstrap', 'bootstrapDatetimepicker','csrf'], function($) {
    $(function() {
        var $body=$('body'),
            $confirmBtn=$('.confirm-btn'),//conirm button
            $tooltip = $('.add-tooltip'),
            $couponDelete = $('.coupon-delete'),
            $tipCom=$('.tip-container');

        $tooltip.length?$tooltip.tooltip():false;

        $couponDelete.on('click',function(){
            var $self = $(this),
                thisLink = $self.attr('data-link');
            if (!confirm("是否确认执行此操作?")) {
                return;
            } else {
                $.ajax({
                    url: thisLink,
                    type: 'DELETE',
                    dataType: 'json'
                })
                .done(function(res){
                    if (res.data.status) {
                        $self.closest('tr').remove();
                    } else {
                        $tipCom.show().find('.txt').text('操作失败！');
                    }
                })
                .fail(function(res){
                     $tipCom.show().find('.txt').text('请求发送失败，请刷新重试！');
                });
            }
        });

        //confirm event
        $confirmBtn.on('click',function(e) {
            e.preventDefault();
            var $self=$(this),
                $parentTd = $self.parents('td'),
                thisId=$self.attr('data-id');//data id
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
                        $parentTd.html('<i class="check-btn add-check"></i><button class="loan_repay already-btn btn-link" disabled>已生效</button>');
                        $parentTd.prev().html('<a href="/activity-manage/coupon/'+thisId+'/detail" class="btn-link">查看详情</a>');
                    }else{
                        $tipCom.show().find('.txt').text('操作失败！');
                    }
                })
                .fail(function(res) {
                    $self.addClass('confirm-btn').text('操作失败');
                    $tipCom.show().find('.txt').text('请求发送失败，请刷新重试！');
                });
            }
            
        });
        
    });
});