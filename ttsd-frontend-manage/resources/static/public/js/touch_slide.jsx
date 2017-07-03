
//定义slide全局
var sliderObj = {

    //判断设备是否支持touch事件
    isTouch:('ontouchstart' in window) || window.DocumentTouch && document instanceof DocumentTouch,
    options:{
        sliderDom:document.getElementById('sliderFrame'),
        startPos:0,
        endPos:0,
        isHorizontal:false,
        duration:6,  //判断是左移还是右移，当偏移量大于10时执行
        leftSide:true, //是非滑到左边缘
        rightSide:false,  //是非滑到右边缘
        moveDirection:{
            ltr:false,
            rtl:false,
            ttb:false,
            btt:false,
            horizontal:true, //代表水平滑动
            vertical:false   //代表垂直滑动
        }
    },
    handleEvent:function(event) {
        var event = event || window.event;

        switch(event.type){
            case "touchstart":
                var touch = event.targetTouches[0];
                sliderObj.options.startPos = {x:touch.pageX,y:touch.pageY,time:+new Date};
                sliderObj.AxisMoveing();
                break;
            case "touchend":
                sliderObj.finish();
                break;
            case "touchmove":
                //阻止触摸事件的默认行为，即阻止滚屏
                event.preventDefault();
                sliderObj.AxisMoveing();
                break;
        }
    },
    //水平方向移动
    AxisMoveing:function() {
        let touchChanged = event.changedTouches[0];
        sliderObj.options.endPos = {x:touchChanged.pageX,y:touchChanged.pageY,time:+new Date};
        let x_start = sliderObj.options.startPos.x,
            x_end = sliderObj.options.endPos.x,
            y_start = sliderObj.options.startPos.y,
            y_end = sliderObj.options.endPos.y,
            duration = this.options.duration;
        let boolObj ={
            ltr:false,
            rtl:false,
            ttb:false,
            btt:false,
            horizontal:false,
            vertical:false
        };
        let oldObj = boolObj;
        boolObj = oldObj;
        if(x_start + duration < x_end) {
            //从左往右的方向
            boolObj.horizontal = true;
            boolObj.ltr = true;

        } else if(x_start -duration > x_end) {
            //从右往左的方向
            boolObj.horizontal = true;
            boolObj.rtl = true;
        } else if(Math.abs(x_end - x_start)< duration || Math.abs(x_end - x_start) == duration) {
            //水平方向没有任何移动
            boolObj.horizontal = false;
            boolObj.vertical = false;
        }

        if(y_start + duration < y_end) {
            //从上往下的方向
            boolObj.ttb = true;
            boolObj.vertical = true;

        } else if(y_start -duration > y_end) {
            //从下往上的方向
            boolObj.btt = true;
            boolObj.vertical = true;

        } else if(Math.abs(y_end-y_start) < duration || Math.abs(y_end-y_start) == duration ) {
            //上下方向没有任何移动
            boolObj.vertical = false;
            boolObj.horizontal = false;
        }

        this.options.moveDirection = $.extend({},this.options.moveDirection,{
            ltr:boolObj.ltr,
            rtl:boolObj.rtl,
            ttb:boolObj.ttb,
            btt:boolObj.btt,
            verticalNo:boolObj.verticalNo,
            horizontalNo:boolObj.horizontalNo
        })

    },

    finish:function() {

    },
    init:function() {
        if(!!this.isTouch) {
            this.options.sliderDom.addEventListener('touchstart',sliderObj.handleEvent, false);
            this.options.sliderDom.addEventListener('touchmove',sliderObj.handleEvent, false);
            this.options.sliderDom.addEventListener('touchend',sliderObj.handleEvent, false);
        }

    }
};

module.exports = sliderObj;




