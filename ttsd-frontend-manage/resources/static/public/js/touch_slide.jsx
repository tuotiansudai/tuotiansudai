
//定义slide全局
var sliderObj = {

    //判断设备是否支持touch事件
    isTouch:('ontouchstart' in window) || window.DocumentTouch && document instanceof DocumentTouch,
    options:{
        sliderDom:document.getElementById('sliderFrame'),
        startPos:0,
        endPos:0,
        isHorizontal:false,
        duration:10,  //判断是左移还是右移，当偏移量大于10时执行
        moveDirection:{
            ltr:false,
            rtl:false,
            ttb:false,
            btt:false
        }
    },
    //数判断是垂直滚动还是水平滚动,
    //true为垂直方向，false为水平方向

    isVertical:function(event) {

        //当屏幕有多个touch或者页面被缩放过，就不执行move操作
        if(event.targetTouches.length > 1 || event.scale && event.scale !== 1) return;

        var touch = event.targetTouches[0];
        sliderObj.options.endPos = {
            x:touch.pageX,
            y:touch.pageY
        };

        var isBool = Math.abs(sliderObj.options.endPos.x) < Math.abs(sliderObj.options.endPos.y) ? true:false;
        return isBool;
    },

    handleEvent:function(event) {
        var event = event || window.event;

        switch(event.type){
            case "touchstart":
                var touch = event.targetTouches[0];
                sliderObj.options.startPos = {x:touch.pageX,y:touch.pageY,time:+new Date};
                break;
            case "touchend":
                //阻止触摸事件的默认行为，即阻止滚屏
                event.preventDefault();
                sliderObj.finish();
                break;
            case "touchmove":
                event.preventDefault();
                var isScrolling = sliderObj.isVertical(event);
                if(isScrolling){
                    //垂直
                    sliderObj.verticalAxisMove();
                } else {
                    //水平方向
                    sliderObj.horizontalAxisMove();
                }

                break;
        }
    },
    //水平方向移动
    horizontalAxisMove:function() {

        var x_start = sliderObj.options.startPos.x,
            x_end = sliderObj.options.endPos.x,
            duration = this.options.duration;
        if(x_start + duration < x_end) {
            //从左往右的方向
            this.options.moveDirection = {
                ltr:true,
                rtl:false,
                ttb:false,
                btt:false
            }
        } else if(x_start -duration > x_end) {
            //从右往左的方向
            this.options.moveDirection = {
                ltr:false,
                rtl:true,
                ttb:false,
                btt:false
            }
        }
    },
    //垂直方向移动
    verticalAxisMove:function() {
        var y_start = sliderObj.options.startPos.y,
            y_end = sliderObj.options.endPos.y,
            duration = this.options.duration;

         if(y_start + duration < y_end) {
            //从上往下的方向
            this.options.moveDirection = {
                ltr:false,
                rtl:false,
                ttb:true,
                btt:false
            }
        } else if(y_start -duration > y_end) {
            //从下往上的方向
            this.options.moveDirection = {
                ltr:false,
                rtl:true,
                ttb:false,
                btt:true
            }
        }
    },
    finish:function() { },
    init:function() {
        if(!!this.isTouch) {
            this.options.sliderDom.addEventListener('touchstart',sliderObj.handleEvent, false);
            this.options.sliderDom.addEventListener('touchmove',sliderObj.handleEvent, false);
            this.options.sliderDom.addEventListener('touchend',sliderObj.handleEvent, false);
        }

    }
};

module.exports = sliderObj;




