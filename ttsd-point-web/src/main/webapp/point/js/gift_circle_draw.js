define(['jquery', 'rotate', 'lottery_unit','commonFun'], function($,rotate,lotteryUnit,commonFun) {

    //allListURL： 中奖纪录的接口链接
    //userListURL：我的奖品的接口链接
    //drawURL：抽奖的接口链接
    //paramData：接口传数据

    function giftCircleDraw(allListURL,userListURL,drawURL,paramData,giftCircleFrame) {
        this.bRotate=false;
        this.allListURL=allListURL;
        this.userListURL=userListURL;
        this.drawURL=drawURL;
        this.paramData=paramData;
        this.giftCircleFrame=giftCircleFrame;
        //开始抽奖
        this.beginLotteryDraw=function(callback) {
            if (this.bRotate) return;
            $.ajax({
                url: this.drawURL,
                data:this.paramData,
                type: 'POST',
                dataType: 'json'
            })
                .done(function(data) {
                    callback(data);
                })
                .fail(function() {
                    commonFun.popWindow('错误','请求失败',{width:'260px'});
                });
        }
        //中奖记录
        this.GiftRecord=function(length) {
            $.ajax({
                url: this.allListURL,
                data:this.paramData,
                type: 'GET',
                dataType: 'json'
            })
                .done(function(data) {
                    var UlList=[];
                    for(var i=0,len=data.length;i<len;i++) {
                        UlList.push('<li>恭喜'+data[i].mobile+'抽中了'+data[i].prizeValue+'</li>');
                    }

                    this.giftCircleFrame.find('.user-record').empty().append(UlList.join(''));
                    this.hoverScrollList(this.giftCircleFrame.find('.user-record'),length);
                }.bind(this));
        }

        //我的奖品
        this.MyGift=function(length) {
            $.ajax({
                url: this.userListURL,
                data:this.paramData,
                type: 'GET',
                dataType: 'json'
            })
                .done(function(data) {
                    var UlList=[];
                    for(var i=0,len=data.length;i<len;i++) {
                        UlList.push('<li>'+data[i].prizeValue+'<time>'+data[i].lotteryTime+'</time></li>');
                    }
                    this.giftCircleFrame.find('.own-record').empty().append(UlList.join(''));
                    this.hoverScrollList(this.giftCircleFrame.find('.own-record'),length);
                }.bind(this));
        }
    }

    //抽奖方式1-----旋转转盘抽奖,抽奖接口调用成功后使用
    //angles:奖项对应的角度，
    //Drawplate:转盘的dom
    //data:抽奖成功后返回的数据
    giftCircleDraw.prototype.rotateFn=function(angles,tipMessage) {
        var thisFun=this;
        thisFun.bRotate = !this.bRotate;
        thisFun.giftCircleFrame.find('.rotate-btn').stopRotate();
        thisFun.giftCircleFrame.find('.rotate-btn').rotate({
            angle: 0,
            animateTo: angles +1800,
            duration: 8000,
            callback: function() {
                thisFun.GiftRecord();
                thisFun.MyGift();
                thisFun.bRotate = !thisFun.bRotate;
                thisFun.tipWindowPop(tipMessage);
            }
        })
    }
    //抽奖方式2-----没有任何转盘效果,抽奖接口调用成功后使用
    giftCircleDraw.prototype.noRotateFn=function(tipMessage) {
        this.GiftRecord();
        this.MyGift();
        this.tipWindowPop(tipMessage);
    }

    //类似九分隔的变换效果
    giftCircleDraw.prototype.lotteryRoll=function(opt,tipMessage) {

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
    }

    giftCircleDraw.prototype.beginLuckDraw=function(callback) {
        var self=this;
        self.beginLotteryDraw(function(data) {
            callback && callback(data);
        });
    }


    giftCircleDraw.prototype.scrollList=function(domName,length) {
        var $self=domName;
        var lineHeight = $self.find("li:first").height();
        if ($self.find('li').length > (length!=''?length:10)) {
            $self.animate({
                "margin-top": -lineHeight + "px"
            }, 600, function() {
                $self.css({
                    "margin-top": "0px"
                }).find("li:first").appendTo($self);
            });
        }
    }
    giftCircleDraw.prototype.hoverScrollList=function(domName,length) {
        var thisFun=this,
            scrollTimer;
        domName.hover(function() {
            clearInterval(scrollTimer);
        }, function() {
            scrollTimer = setInterval(function() {
                thisFun.scrollList(domName,length);
            }, 2000);
        }).trigger("mouseout");
    }

    //接口调成功以后的弹框显示
    giftCircleDraw.prototype.tipWindowPop=function(tipMessage,callback) {
        $(tipMessage).show();
        commonFun.popWindow(tipMessage.outerHTML);
        callback && callback();
    }
    //tab switch
    giftCircleDraw.prototype.PrizeSwitch=function() {
        var menuCls=this.giftCircleFrame.find('.gift-record').find('li'),
            contentCls=this.giftCircleFrame.find('.record-list ul');
        menuCls.on('click',function(index) {
            var $this=$(this),
                index=$this.index();
        $this.addClass('active').siblings().removeClass('active');
        contentCls.eq(index).show().siblings().hide();
        });
    }

    return giftCircleDraw;
});