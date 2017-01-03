require(['jquery', 'layerWrapper','commonFun'], function($,layer,commonFun) {

    $(function() {
        //会员发红包
        (function () {
            var $redEnvelopSplit=$('#redEnvelopSplit');

            $('.envelop-button span',$redEnvelopSplit).on('click',function() {
                var $this=$(this),
                    thisNum=$this.index();

                $this.addClass('active').siblings('span').removeClass('active');
                $('.envelop-box',$redEnvelopSplit).eq(thisNum).show().siblings('.envelop-box').hide();
            });

        })();
    })
});