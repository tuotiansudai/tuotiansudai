require(['jquery'], function ($) {

        var $productFrame=$('#productListFrame'),
            $leftBtn=$('.arrow-left',$productFrame),
            $rightBtn=$('.arrow-right',$productFrame),
            $productBox=$('.product-box',$productFrame);

    var productBoxW=$productBox.width(),
        productLen=$productBox.find('li').length;
    var redirect=globalFun.browserRedirect();
    if(redirect=='mobile') {
        $productBox.find('li').css({"width":productBoxW});
        var recordNum=0; //用来记录当前活动图片

        function animateImg(record) {
            $productBox.find('ul').stop().animate({
                marginLeft: -(productBoxW * record)
            },500,function() {
                var length=productLen-1;
                if(record==0) {
                    $leftBtn.hide();
                    $rightBtn.show();
                }
                else if(record>0 && record<length) {
                    $leftBtn.show();
                    $rightBtn.show();
                }
                else if(record==length) {
                    $rightBtn.hide();
                    $leftBtn.show();
                }
            });
        }
        $rightBtn.on('touchstart', function () {
            recordNum++;
            animateImg(recordNum);
        });

        $leftBtn.on('touchstart', function () {
            recordNum--;
            animateImg(recordNum);
        });

    }

});