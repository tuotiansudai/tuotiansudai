let lotteryGame = {
    init: function (option) {
        let defaultOpt = {
            clicked: false, //防止重复点击
            index: 0,	//当前转动到哪个位置
            count: 0,	//总共有多少个位置
            timer: 0,	//setTimeout的ID，用clearTimeout清除
            speed: 200,	//初始转动速度
            times: 0,	//转动次数
            cycle: 10,	//转动基本次数：即至少需要转动多少次再进入抽奖环节
            prize: -1,	//中奖位置
            elementId: ''
        };

        this.initOpt = $.extend({}, defaultOpt, option);
        let $lottery = $("#" + this.initOpt.elementId);
        this.unitsOption = $lottery.find(".lottery-unit");
        this.obj = $lottery;
        this.initOpt.count = this.unitsOption.length;
    },
    rollStart: function () {
        let thisObj = this.initOpt;
        thisObj.index += 1;
        if (thisObj.index > thisObj.count - 1) {
            thisObj.index = 0;
        }
        let $filterOption = this.unitsOption.filter(function (key, option) {
            $(option).removeClass('active');
            return $(option).data('unit') == thisObj.index;
        });
        $filterOption.addClass("active");
        return false;
    },
    rollResult: function (option, callback) {
        this.rollStart();
        let initOption = this.initOpt;
        initOption.times += 1;
        if (initOption.times > initOption.cycle && initOption.prize == initOption.index) {

            clearTimeout(initOption.timer);
            initOption.prize = -1;
            initOption.times = 0;
            initOption.clicked = false;
            callback && callback();
        } else {
            initOption.timer = setTimeout(this.rollResult.bind(this), initOption.speed);
            initOption.clicked = true;
        }
        return false;
    }

};

module.exports=lotteryGame;

