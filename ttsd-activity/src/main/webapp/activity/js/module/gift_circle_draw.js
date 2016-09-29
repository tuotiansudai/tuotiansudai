define(['jquery', 'rotate', 'layerWrapper','template'], function($, rotate, layer,tpl) {
    var $giftCircleFrame=$('.gift-circle-frame'),
        textTip=$('.text-tip',$giftCircleFrame),
        btnList=$('.btn-list',$giftCircleFrame);

    //allListURL： 中奖纪录的接口链接
    //userListURL：我的奖品的接口链接
    //drawURL：抽奖的接口链接
    //mobileNumber：当前登录用户的手机号

    function giftCircleDraw(allListURL,userListURL,drawURL,paramData) {
        this.bRotate=false;
        this.allListURL=allListURL;
        this.userListURL=userListURL;
        this.drawURL=drawURL;
        this.paramData=paramData;
        //开始抽奖
        //url:抽奖接口路径
        //category:活动类型
        //mobileNumber:活动页面获取用户手机号
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
        //url:抽奖接口路径
        //category:活动类型

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
        //url:抽奖接口路径
        //category:活动类型
        //mobileNumber:活动页面获取用户手机号

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
    //text:提示文字
    //urls:中奖记录和我的奖品的接口对象
    giftCircleDraw.prototype.rotateFn=function(angles,tipMessage,dom) {
        this.bRotate = !this.bRotate;
        $('.rotate-btn',$giftCircleFrame).stopRotate();
        $('.rotate-btn',$giftCircleFrame).rotate({
            angle: 0,
            animateTo: angles + 1800,
            duration: 8000,
            callback: function() {
                this.tipWindowPop(tipMessage,dom);
                this.bRotate = !this.bRotate;
                this.GiftRecord();
                this.MyGift();
                //$('.lottery-time',$giftCircleFrame).each(function(index,el){
                //    $(this).text()>1?$(this).text(function(index,num){return parseInt(num)-1}):$(this).text('0');
                //});
            }
        })
    }

    giftCircleDraw.prototype.scrollList=function(domName) {
        var $self=domName;
        var lineHeight = $self.find("li:first").height();
        if ($self.find('li').length > 10) {
            $self.animate({
                "margin-top": -lineHeight + "px"
            }, 600, function() {
                $self.css({
                    "margin-top": "0px"
                }).find("li:first").appendTo($self);
            })
        }
    }

    //接口调成功以后的弹框显示
    giftCircleDraw.prototype.tipWindowPop=function(tipMessage,dom) {
        dom.find('.text-tip').empty().html(tipMessage.info);
        dom.find('.btn-list').empty().html(tipMessage.button);
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
            content: dom
        });
    }
    //tab switch
    giftCircleDraw.prototype.PrizeSwitch=function(menuCls,contentCls) {
        menuCls.on('click',function(index) {
            var $this=$(this),
                index=$this.index();
        contentCls.eq(index).show().siblings().hide();
        });
    }

    return giftCircleDraw;
});