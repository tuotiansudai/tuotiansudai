define(['jquery', 'rotate', 'layerWrapper','template'], function($, rotate, layer,tpl) {

    //allListURL： 中奖纪录的接口链接
    //userListURL：我的奖品的接口链接
    //drawURL：抽奖的接口链接
    //paramData：接口传数据

    function giftCircleDraw(allListURL,userListURL,drawURL,paramData,tipList) {
        this.bRotate=false;
        this.allListURL=allListURL;
        this.userListURL=userListURL;
        this.drawURL=drawURL;
        this.paramData=paramData;
        this.tipList=tipList;
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
            $.ajax({
                url: this.allListURL,
                data:this.paramData,
                type: 'GET',
                dataType: 'json'
            })
                .done(function(data) {
                    //$('#GiftRecord').html(tpl('GiftRecordTpl', {record:data}));
                });
        }

        //我的奖品
        this.MyGift=function() {
            $.ajax({
                url: this.userListURL,
                data:this.paramData,
                type: 'GET',
                dataType: 'json'
            })
                .done(function(data) {
                    //$('#MyGift').html(tpl('MyGiftTpl', {gift:data}));
                });
        }
    }

    //转盘旋转
    //angles:奖项对应的角度，
    //Drawplate:转盘的dom
    //data:抽奖成功后返回的数据
    giftCircleDraw.prototype.rotateFn=function(Drawplate,angles,data) {
        this.bRotate = !this.bRotate;
        Drawplate.stopRotate();
        Drawplate.rotate({
            angle: 0,
            animateTo: angles + 1800,
            duration: 8000,
            callback: function(data) {

                this.tipWindowPop(tipMessage,dom);
                this.bRotate = !this.bRotate;
                this.GiftRecord();
                this.MyGift();
            }
        })
    }

    giftCircleDraw.prototype.scrollList=function(domName) {
        var $self=domName,
            scrollTimer;
        var lineHeight = $self.find("li:first").height();
        if ($self.find('li').length > 10) {
            $self.animate({
                "margin-top": -lineHeight + "px"
            }, 600, function() {
                $self.css({
                    "margin-top": "0px"
                }).find("li:first").appendTo($self);
            })

            domName.hover(function() {
                clearInterval(scrollTimer);
            }, function() {
                scrollTimer = setInterval(function() {
                    domName.scrollList(domName);
                }, 2000);
            }).trigger("mouseout");
        }

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
    giftCircleDraw.prototype.PrizeSwitch=function(menuCls,contentCls) {
        menuCls.on('click',function(index) {
            var $this=$(this),
                index=$this.index();
        $this.addClass('active').siblings().removeClass('active');
        contentCls.eq(index).show().siblings().hide();
        });
    }

    return giftCircleDraw;
});