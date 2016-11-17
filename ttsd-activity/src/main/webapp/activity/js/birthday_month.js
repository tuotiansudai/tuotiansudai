require(['jquery'], function ($) {

        var $productFrame=$('#productListFrame'),
            $leftArrow=$('.arrow-left',$productFrame),
            $rightArrow=$('.arrow-right',$productFrame),
            $productBox=$('.product-box',$productFrame);

    var productBoxW=$productBox.width();

    $productBox.find('li').css({"width":productBoxW});

});