define(['jquery', 'rotate', 'layerWrapper'], function($, rotate, layer) {

    //allListURL： 中奖纪录的接口链接
    //userListURL：我的奖品的接口链接
    //drawURL：抽奖的接口链接
    //paramData：接口传数据

    function giftCircleDraw(allListURL,userListURL,drawURL,paramData,tipList,giftCircleFrame) {
        this.bRotate=false;
        this.allListURL=allListURL;
        this.userListURL=userListURL;
        this.drawURL=drawURL;
        this.paramData=paramData;
        this.tipList=tipList;
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
                    layer.msg('请求失败');
                });
        }
        //中奖记录
        this.GiftRecord=function() {
            var thisFun=this;
            $.ajax({
                url: this.allListURL,
                data:this.paramData,
                type: 'GET',
                dataType: 'json'
            })
                .done(function(data) {
                    //console.log('中奖记录'+data);
                    var UlList=[];
                    var data=[
                        {"mobile":"137****7702","userName":null,"prize":"TELEPHONE_FARE_10","prizeValue":"rtrt","lotteryTime":"2016-09-29 16:09:05"},
                        {"mobile":"182****5693","userName":null,"prize":"RED_INVEST_50","prizeValue":"rtrt","lotteryTime":"2016-09-29 16:05:44"},
                        {"mobile":"182****5693","userName":null,"prize":"RED_INVEST_15","prizeValue":"rtrt","lotteryTime":"2016-09-29 15:56:41"},
                        {"mobile":"188****0000","userName":null,"prize":"RED_INVEST_15","prizeValue":"rtrt","lotteryTime":"2016-09-26 16:36:57"},
                        {"mobile":"188****0000","userName":null,"prize":"CINEMA_TICKET","prizeValue":"rtrt","lotteryTime":"2016-09-26 16:35:40"},
                        {"mobile":"188****0000","userName":null,"prize":"RED_INVEST_15","prizeValue":"rtrt","lotteryTime":"2016-09-26 16:35:29"}];

                    for(var i=0,len=data.length;i<len;i++) {
                        UlList.push('<li>恭喜'+data[i].mobile+'抽中了'+data[i].prize+'</li>');
                    }

                    thisFun.giftCircleFrame.find('.user-record').empty().append(UlList.join(''));
                    thisFun.hoverScrollList(thisFun.giftCircleFrame.find('.user-record'));
                });
        }

        //我的奖品
        this.MyGift=function() {
            var thisFun=this;
            $.ajax({
                url: this.userListURL,
                data:this.paramData,
                type: 'GET',
                dataType: 'json'
            })
                .done(function(data) {
                    var UlList=[];
                    var data=[
                        {"mobile":"137****7702","userName":null,"prize":"TELEPHONE_FARE_10","prizeValue":"rtrt","lotteryTime":"2016-09-29 16:09:05"},
                        {"mobile":"182****5693","userName":null,"prize":"RED_INVEST_50","prizeValue":"rtrt","lotteryTime":"2016-09-29 16:05:44"},
                        {"mobile":"182****5693","userName":null,"prize":"RED_INVEST_15","prizeValue":"rtrt","lotteryTime":"2016-09-29 15:56:41"}
                    ];

                    for(var i=0,len=data.length;i<len;i++) {
                        UlList.push('<li>恭喜'+data[i].mobile+'抽中了'+data[i].prizeValue+'</li>');
                    }
                    thisFun.giftCircleFrame.find('.own-record').empty().append(UlList.join(''));
                    thisFun.scrollList(thisFun.giftCircleFrame.find('.own-record'));
                    thisFun.hoverScrollList(thisFun.giftCircleFrame.find('.own-record'));
                });
        }
    }

    //转盘旋转
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

    giftCircleDraw.prototype.scrollList=function(domName) {
        var $self=domName;
        var lineHeight = $self.find("li:first").height();
        if ($self.find('li').length > 10) {
            console.log('pop');
            $self.animate({
                "margin-top": -lineHeight + "px"
            }, 600, function() {
                $self.css({
                    "margin-top": "0px"
                }).find("li:first").appendTo($self);
            });
        }
    }
    giftCircleDraw.prototype.hoverScrollList=function(domName) {
        var thisFun=this,
            scrollTimer;
        domName.hover(function() {
            clearInterval(scrollTimer);
        }, function() {
            scrollTimer = setInterval(function() {
                thisFun.scrollList(domName);
            }, 2000);
        }).trigger("mouseout");
    }

    //接口调成功以后的弹框显示
    giftCircleDraw.prototype.tipWindowPop=function(tipMessage) {
        this.tipList.find('.text-tip').empty().html(tipMessage.info);
        this.tipList.find('.btn-list').empty().html(tipMessage.button);
        if(tipMessage.area.length==0) {
            tipMessage.area=['460px', '370px'];
        }
        layer.open({
            type: 1,
            title: false,
            area: tipMessage.area,
            shade: 0.8,
            closeBtn: 0,
            shadeClose: true,
            content: this.tipList
        });
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