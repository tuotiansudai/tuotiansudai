require(['jquery', 'layerWrapper', 'jquery.ajax.extension', 'coupon-alert', 'red-envelope-float','count_down','assign_coupon'], function ($,layer) {
    var $loan = $('.loan-list-box').find('li');

    //我要投资菜单过滤显示
    (function() {
        var $wrapperList=$('#wrapperList');
        var $showFilterBox=$wrapperList.find('li').filter(function(key) {
            return key>0;
        });
        $wrapperList.find('.show-more').on('click',function(event) {
            var $this=$(this),
                btnText;
            $this.toggleClass('ok');
            $showFilterBox.toggle();
            var isRetract=$this.hasClass('ok');
            if(isRetract) {
                //有样式名ok，展开状态
                btnText='收起 <i class="fa fa fa-angle-up"></i>';
            }
            else {
                //没有样式名ok，关闭状态
                btnText='更多 <i class="fa fa-angle-down"></i> ';
            }
            $this.html(btnText);

        });
    })();


    $loan.click(function () {
        window.location.href = $(this).data('url');
    });

    $('.pagination .prev').click(function () {
        var self = $(this);
        if (self.hasClass('active')) {
            window.location.href = self.data('url');
        }
        return false;
    });

    $('.pagination .next').click(function () {
        var self = $(this);
        if (self.hasClass('active')) {
            window.location.href = self.data('url');
        }
        return false;
    });

    $('.loan-info-dl .fa-mobile').on('mouseover', function(event) {
        event.preventDefault();
        var $self=$(this);
        layer.tips('APP投资该项目享受最高0.8%年化收益奖励', $self, {
            tips: 1
        });
    });

});
