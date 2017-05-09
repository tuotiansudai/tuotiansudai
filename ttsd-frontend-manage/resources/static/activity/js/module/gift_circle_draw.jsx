require('activityStyle/module/gift_circle_tip.scss');
let rotate = require('publicJs/plugins/jqueryrotate.min');
let lotteryUnit = require('activityJsModule/lottery_unit');
let commonFun = require('publicJs/commonFun');
//allListURL： 中奖纪录的接口链接
//userListURL：我的奖品的接口链接
//drawURL：抽奖的接口链接
//paramData：接口传数据

function giftCircleDraw(allListURL, userListURL, drawURL, paramData, giftCircleFrame) {
    this.bRotate = false;
    this.allListURL = allListURL;
    this.userListURL = userListURL;
    this.drawURL = drawURL;
    this.paramData = paramData;
    this.giftCircleFrame = giftCircleFrame;
    //开始抽奖
    this.beginLotteryDraw = function (callback) {
        if (this.bRotate) return;
        commonFun.useAjax({
            url: this.drawURL,
            data: this.paramData,
            type: 'POST'
        }, callback);
    };

    //中奖记录
    this.GiftRecord = function () {
        let self = this;
        commonFun.useAjax({
            url: this.allListURL,
            data: this.paramData,
            type: 'GET'
        }, function (data) {
            data=[{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_INTEREST_COUPON_5","lotteryTime":"2017-05-09 17:49:56","prizeValue":"0.5%加息券","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_20","lotteryTime":"2017-05-09 17:49:56","prizeValue":"20元红包","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_INTEREST_COUPON_5","lotteryTime":"2017-05-09 17:47:20","prizeValue":"0.5%加息券","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_INTEREST_COUPON_5","lotteryTime":"2017-05-09 17:47:08","prizeValue":"0.5%加息券","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_5","lotteryTime":"2017-05-09 17:46:52","prizeValue":"5元红包","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_20","lotteryTime":"2017-05-09 17:42:31","prizeValue":"20元红包","loginName":null,"investFlag":null},{"mobile":"178****7064","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_5","lotteryTime":"2017-05-09 17:18:29","prizeValue":"5元红包","loginName":null,"investFlag":null},{"mobile":"178****7064","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_5","lotteryTime":"2017-05-09 17:08:42","prizeValue":"5元红包","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_20","lotteryTime":"2017-05-09 16:28:42","prizeValue":"20元红包","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_10","lotteryTime":"2017-05-09 16:19:01","prizeValue":"10元红包","loginName":null,"investFlag":null},{"mobile":"178****7064","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_10","lotteryTime":"2017-05-09 16:11:03","prizeValue":"10元红包","loginName":null,"investFlag":null},{"mobile":"178****7064","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_10","lotteryTime":"2017-05-09 16:10:52","prizeValue":"10元红包","loginName":null,"investFlag":null},{"mobile":"178****7064","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_5","lotteryTime":"2017-05-09 16:10:37","prizeValue":"5元红包","loginName":null,"investFlag":null},{"mobile":"178****7064","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_INTEREST_COUPON_5","lotteryTime":"2017-05-09 16:10:11","prizeValue":"0.5%加息券","loginName":null,"investFlag":null},{"mobile":"178****7064","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_INTEREST_COUPON_5","lotteryTime":"2017-05-09 16:08:54","prizeValue":"0.5%加息券","loginName":null,"investFlag":null},{"mobile":"178****7064","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_20","lotteryTime":"2017-05-09 16:07:33","prizeValue":"20元红包","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_INTEREST_COUPON_5","lotteryTime":"2017-05-09 15:51:03","prizeValue":"0.5%加息券","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_INTEREST_COUPON_5","lotteryTime":"2017-05-09 15:47:50","prizeValue":"0.5%加息券","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_10","lotteryTime":"2017-05-09 15:45:09","prizeValue":"10元红包","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_EXPERIENCE_GOLD_8888","lotteryTime":"2017-05-09 15:44:59","prizeValue":"8888元体验金","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_5","lotteryTime":"2017-05-09 15:44:49","prizeValue":"5元红包","loginName":null,"investFlag":null},{"mobile":"178****7064","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_10","lotteryTime":"2017-05-09 15:43:18","prizeValue":"10元红包","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_10","lotteryTime":"2017-05-09 15:43:18","prizeValue":"10元红包","loginName":null,"investFlag":null},{"mobile":"178****7064","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_20","lotteryTime":"2017-05-09 15:43:06","prizeValue":"20元红包","loginName":null,"investFlag":null},{"mobile":"178****7064","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_5","lotteryTime":"2017-05-09 15:42:56","prizeValue":"5元红包","loginName":null,"investFlag":null},{"mobile":"178****7064","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_20","lotteryTime":"2017-05-09 15:42:45","prizeValue":"20元红包","loginName":null,"investFlag":null},{"mobile":"178****7064","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_EXPERIENCE_GOLD_888","lotteryTime":"2017-05-09 15:42:35","prizeValue":"888元体验金","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_5","lotteryTime":"2017-05-09 15:41:19","prizeValue":"5元红包","loginName":null,"investFlag":null},{"mobile":"178****7064","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_10","lotteryTime":"2017-05-09 15:41:11","prizeValue":"10元红包","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_EXPERIENCE_GOLD_888","lotteryTime":"2017-05-09 15:41:07","prizeValue":"888元体验金","loginName":null,"investFlag":null},{"mobile":"178****7064","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_20","lotteryTime":"2017-05-09 15:40:57","prizeValue":"20元红包","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_20","lotteryTime":"2017-05-09 15:40:56","prizeValue":"20元红包","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_INTEREST_COUPON_5","lotteryTime":"2017-05-09 15:40:46","prizeValue":"0.5%加息券","loginName":null,"investFlag":null},{"mobile":"178****7064","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_20","lotteryTime":"2017-05-09 15:40:46","prizeValue":"20元红包","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_INTEREST_COUPON_5","lotteryTime":"2017-05-09 15:40:34","prizeValue":"0.5%加息券","loginName":null,"investFlag":null},{"mobile":"178****7064","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_5","lotteryTime":"2017-05-09 15:40:32","prizeValue":"5元红包","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_INTEREST_COUPON_1","lotteryTime":"2017-05-09 15:40:22","prizeValue":"1%加息券","loginName":null,"investFlag":null},{"mobile":"178****7064","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_INTEREST_COUPON_1","lotteryTime":"2017-05-09 15:40:21","prizeValue":"1%加息券","loginName":null,"investFlag":null},{"mobile":"178****7064","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_20","lotteryTime":"2017-05-09 15:30:49","prizeValue":"20元红包","loginName":null,"investFlag":null}]
            let UlList = [];
            for (let i = 0, len = data.length; i < len; i++) {
                UlList.push('<li>恭喜' + data[i].mobile + '抽中了' + data[i].prizeValue + '</li>');
            }
            self.giftCircleFrame.find('.user-record').empty().append(UlList.join(''));
        });
    };

    //我的奖品
    this.MyGift = function () {
        let self = this;
        commonFun.useAjax({
            url: this.userListURL,
            data: this.paramData,
            type: 'GET',
        }, function (data) {
            data=[{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_INTEREST_COUPON_5","lotteryTime":"2017-05-09 17:49:56","prizeValue":"0.5%加息券","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_20","lotteryTime":"2017-05-09 17:49:56","prizeValue":"20元红包","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_INTEREST_COUPON_5","lotteryTime":"2017-05-09 17:47:20","prizeValue":"0.5%加息券","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_INTEREST_COUPON_5","lotteryTime":"2017-05-09 17:47:08","prizeValue":"0.5%加息券","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_5","lotteryTime":"2017-05-09 17:46:52","prizeValue":"5元红包","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_20","lotteryTime":"2017-05-09 17:42:31","prizeValue":"20元红包","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_20","lotteryTime":"2017-05-09 16:28:42","prizeValue":"20元红包","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_10","lotteryTime":"2017-05-09 16:19:01","prizeValue":"10元红包","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_INTEREST_COUPON_5","lotteryTime":"2017-05-09 15:51:03","prizeValue":"0.5%加息券","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_INTEREST_COUPON_5","lotteryTime":"2017-05-09 15:47:50","prizeValue":"0.5%加息券","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_10","lotteryTime":"2017-05-09 15:45:09","prizeValue":"10元红包","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_EXPERIENCE_GOLD_8888","lotteryTime":"2017-05-09 15:44:59","prizeValue":"8888元体验金","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_5","lotteryTime":"2017-05-09 15:44:49","prizeValue":"5元红包","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_10","lotteryTime":"2017-05-09 15:43:18","prizeValue":"10元红包","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_5","lotteryTime":"2017-05-09 15:41:19","prizeValue":"5元红包","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_EXPERIENCE_GOLD_888","lotteryTime":"2017-05-09 15:41:07","prizeValue":"888元体验金","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_ENVELOP_20","lotteryTime":"2017-05-09 15:40:56","prizeValue":"20元红包","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_INTEREST_COUPON_5","lotteryTime":"2017-05-09 15:40:46","prizeValue":"0.5%加息券","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_INTEREST_COUPON_5","lotteryTime":"2017-05-09 15:40:34","prizeValue":"0.5%加息券","loginName":null,"investFlag":null},{"mobile":"133****8657","userName":null,"prize":"MOTHERS_DAY_ACTIVITY_INTEREST_COUPON_1","lotteryTime":"2017-05-09 15:40:22","prizeValue":"1%加息券","loginName":null,"investFlag":null}]
            let UlList = [];
            for (let i = 0, len = data.length; i < len; i++) {
                UlList.push('<li>' + data[i].prizeValue + '<time>' + data[i].lotteryTime + '</time></li>');
            }
            self.giftCircleFrame.find('.own-record').empty().append(UlList.join(''));
        });
    }
}

//抽奖方式1-----旋转转盘抽奖,抽奖接口调用成功后使用
//angles:奖项对应的角度，
//Drawplate:转盘的dom
//data:抽奖成功后返回的数据
giftCircleDraw.prototype.rotateFn = function (angles, tipMessage) {
    var thisFun = this;
    thisFun.bRotate = !this.bRotate;
    thisFun.giftCircleFrame.find('.rotate-btn').stopRotate();
    thisFun.giftCircleFrame.find('.rotate-btn').rotate({
        angle: 0,
        animateTo: angles + 1800,
        duration: 8000,
        callback: function () {
            thisFun.GiftRecord();
            thisFun.MyGift();
            thisFun.bRotate = !thisFun.bRotate;
            thisFun.tipWindowPop(tipMessage);
        }
    })
};

//抽奖方式2-----没有任何转盘效果,抽奖接口调用成功后使用
giftCircleDraw.prototype.noRotateFn = function (tipMessage) {
    this.GiftRecord();
    this.MyGift();
    this.tipWindowPop(tipMessage);
};

//类似九分隔的变换效果
giftCircleDraw.prototype.lotteryRoll = function (opt, tipMessage) {

    // opt参数的格式为
    // elementId为抽奖部分最外层dom的ID
    //  {
    //  elementId:'lottery',
    //  speed:100,
    //  prize:prize
    // }
    var thisFun = this;
    lotteryUnit.init(opt);
    if (!lotteryUnit.initOpt.clicked) {
        lotteryUnit.rollResult(function () {
            thisFun.GiftRecord();
            thisFun.MyGift();
            thisFun.tipWindowPop(tipMessage);
        });
    }
};

giftCircleDraw.prototype.beginLuckDraw = function (callback) {
    var self = this;
    self.beginLotteryDraw(function (data) {
        callback && callback(data);
    });
};

giftCircleDraw.prototype.scrollList = function (domName, length) {
    var $self = domName;
    var lineHeight = $self.find("li:first").height();
    if ($self.find('li').length > (length != '' ? length : 10)) {
        $self.animate({
            "margin-top": -lineHeight + "px"
        }, 600, function () {
            $self.css({
                "margin-top": "0px"
            }).find("li:first").appendTo($self);
        });
    }
};

giftCircleDraw.prototype.hoverScrollList = function (domName, length) {
    var thisFun = this,
        scrollTimer;

    domName.hover(function () {
        clearInterval(scrollTimer);
    }, function () {
        scrollTimer = setInterval(function () {
            thisFun.scrollList(domName, length);
        }, 2000);
    }).trigger("mouseout");
};

//接口调成功以后的弹框显示
giftCircleDraw.prototype.tipWindowPop = function (tipMessage, callback) {
    $(tipMessage).show();
    commonFun.popWindow(tipMessage.outerHTML);
    callback && callback();
};

//tab switch
giftCircleDraw.prototype.PrizeSwitch = function () {
    var menuCls = this.giftCircleFrame.find('.gift-record').find('li'),
        contentCls = this.giftCircleFrame.find('.record-list ul');
    menuCls.on('click', function (index) {
        var $this = $(this),
            index = $this.index();
        $this.addClass('active').siblings().removeClass('active');
        contentCls.eq(index).show().siblings().hide();
    });
};

module.exports = giftCircleDraw;