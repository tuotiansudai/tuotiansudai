require(['jquery','bootstrap', 'bootstrapDatetimepicker','csrf'], function($) {
    $(function() {
        var $body=$('body'),
            $confirmBtn=$('.confirm-btn'),//conirm button
            $inactiveBtn = $('.inactive-btn'),
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

        $('body').delegate('.inactive-btn','click',function(e) {
            e.preventDefault();
            var $self = $(this),
                $parentTd = $self.parents('td'),
                thisId = $self.attr('data-id');//data id
            var couponType = $self.attr('data-type');
            if (!confirm("是否确认执行此操作?")) {
                return;
            } else {
                $.ajax({
                    url:'/activity-manage/coupon/'+thisId+'/inactive',
                    type:'POST',
                    dataType:'json'
                })
                    .done(function(res) {
                        if (res.data.status) {
                            $parentTd.html('<i class="check-btn"></i><a class="loan_repay confirm-btn" href="javascript:void(0)" data-type="'+couponType+'" data-id="'+thisId+'">确认生效</a>');
                            $parentTd.prev().html('<a href="/activity-manage/coupon/'+thisId+'/edit" class="btn-link">编辑</a> / <button class="btn-link coupon-delete" data-link="/activity-manage/coupon/'+thisId+'" >删除</button>');
                        } else {
                            $tipCom.show().find('.txt').text('操作失败！');
                        }
                    })
                    .fail(function(res) {
                        $self.addClass('confirm-btn').text('操作失败');
                        $tipCom.show().find('.txt').text('请求发送失败，请刷新重试！');
                    });
            }
        });

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
                            $parentTd.html('<i class="check-btn add-check"></i><button class="loan_repay already-btn btn-link inactive-btn" data-id="'+thisId+'">已生效</button>');
                            if (couponType != 'NEWBIE_COUPON') {
                                $parentTd.find('button').attr('disabled');
                            }
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
        })

    });

    $('.detail-redis').on('click',function(){
        var $this = $(this);
        var link = $this.attr('data-url');
        $.ajax({
            url:link,
            type:'POST',
            dataType:'JSON'
        })
        .done(function(res){
            $('.see-detail').show();
            $('.see-detail').find('span:eq(0)').text(res[0]);
            $('.see-detail').find('span:eq(1)').text(res[1]);
        })
        .fail(function(res) {
           $this.addClass('confirm-btn').text('操作失败');
        });
    });

    $('.close-btn').on('click',function(){
        $(this).parents('.see-detail').hide();
    });

});