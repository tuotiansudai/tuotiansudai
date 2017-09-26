let leftMenuBox = globalFun.$('#leftMenuBox');
//手机端菜单滑动

if(leftMenuBox) {
    let browser = $(window).width();
    if(browser<1000) {
        let menuLen = $(leftMenuBox).find('li:visible').length;
        let screenW = $(window).width(),
            showMenuNum = 3, //希望一屏展示3个菜单
            someLiW = screenW/showMenuNum,
            totalWidth = someLiW * menuLen;
        $(leftMenuBox).find('ul').width(totalWidth);
        $(leftMenuBox).find('li').css({"width":someLiW});

        //判断当前激活的菜单在可视区域
        let slipAway = (function() {
            let currentLi = $(leftMenuBox).find('li').filter(function(key,option) {
                return $(option).find('a').hasClass('active');
            });
            let hideLi = $(leftMenuBox).find('li:hidden').index();

            let currentOrder = currentLi.index();
            if(currentOrder>hideLi) {
                currentOrder = currentOrder -1;
            }
            let curOrder = parseInt(currentOrder/showMenuNum);
            let moveInit = -curOrder*screenW + 'px';
            $(leftMenuBox).find('ul').css({
                '-webkit-transform':"translate("+moveInit+")",
                '-webkit-transition':'10ms linear'
            });
            return curOrder;
        })();

        let touchSlide = require('publicJs/touch_slide');
        let num=slipAway * showMenuNum;
        touchSlide.options.sliderDom = leftMenuBox;
        touchSlide.finish = function() {

            let direction = touchSlide.options.moveDirection,
                moveDistance;
            //如果没有任何滑动迹象，不左处理
            if(this.options.moveDirection.horizontal) {
                return;
            }
            if(direction.rtl && num<menuLen-showMenuNum) {
                //从右到左
                num++;

            } else if(direction.ltr && num>0) {
                //从左到右
                num--;
            }

            moveDistance = - someLiW*num + 'px';
            $(leftMenuBox).find('ul').css({
                '-webkit-transform':"translate("+moveDistance+")",
                '-webkit-transition':'100ms linear'
            });
        }
        touchSlide.init();
    }
}


