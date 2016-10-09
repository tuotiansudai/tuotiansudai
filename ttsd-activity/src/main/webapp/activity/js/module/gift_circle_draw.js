define(['jquery', 'rotate', 'layerWrapper'], function($, rotate, layer) {

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
                    for(var i=0,len=data.length;i<len;i++) {
                        UlList.push('<li>恭喜'+data[i].mobile+'抽中了'+data[i].prizeValue+'</li>');
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
    giftCircleDraw.prototype.beginLuckDraw=function(callback) {
        var self=this;
        self.giftCircleFrame.find('.pointer-img').on('click', function(event) {
            self.beginLotteryDraw(function(data) {
                callback && callback(data);
            });
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
            skin:'layer-gift-draw',
            content: '<div class="tip-list">' +
                '<div class="close-btn go-close"></div>' +
                '<div class="text-tip">'+tipMessage.info+'</div>' +
                '<div class="btn-list">'+tipMessage.button+'</div></div>'
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

    $('body').on('click', '.go-close', function(event) {
        event.preventDefault();
        var $self = $(this);
        window.location.reload();
        layer.closeAll();
    });

    return giftCircleDraw;
});