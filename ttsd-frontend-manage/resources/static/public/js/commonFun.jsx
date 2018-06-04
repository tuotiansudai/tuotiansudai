function refreshCaptcha(dom, url, flush,callback) {
    let captcha= url +'?'+ new Date().getTime().toString();
    if (flush === false) {
        captcha += '&flush=false'
    }
    dom.setAttribute('src',captcha);
    callback&&callback();
}
/* init radio style */
function initRadio($radio,$radioLabel) {
    let numRadio=$radio.length;
    if(numRadio) {
        $radio.each(function(key,option) {
            let $this=$(this);
            if($this.is(':checked')) {
                $this.next('label').addClass('checked');
            }
            $this.next('label').click(function() {
                let $thisLab=$(this);
                if(!/checked/.test(this.className)) {
                    $radioLabel.removeClass('checked');
                    $thisLab.addClass('checked');
                }
            });
        });

    }
}

// 验证身份证有效性
function IdentityCodeValid(code) {
    let city={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江 ",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北 ",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏 ",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外 "};
    let pass= true;

    if (!code || !/\d{17}[\d|x]/i.test(code)) {
        pass = false;
    }

    else if(!city[code.substr(0,2)]){
        pass = false;
    }
    else{
        //18位身份证需要验证最后一位校验位
        if(code.length == 18){
            code = code.split('');
            //∑(ai×Wi)(mod 11)
            //加权因子
            let factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
            //校验位
            let parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
            let sum = 0;
            let ai = 0;
            let wi = 0;
            for (let i = 0; i < 17; i++)
            {
                ai = code[i];
                wi = factor[i];
                sum += ai * wi;
            }
            if(parity[sum % 11] != code[17]){
                pass =false;
            }
        }
    }
    return pass;
}
//检测年龄是否大于18
function checkedAge(birthday) {
    let getAge=birthday.substring(6,14),
        currentDay=new Date(),
        checkedAge=true;
    let y=currentDay.getFullYear(),
        m=currentDay.getMonth()+ 1,
        d=currentDay.getDate();
    let today = y+''+(m<10?('0'+m):m)+''+(d<10?('0'+d):d);
    let myAge=Math.floor((today-getAge)/10000);
    if(myAge<18) {
        checkedAge=false;
    }
    return checkedAge;
}
//弹框样式
function popWindow(contentHtml,area) {
    let $shade=$('<div class="shade-body-mask"></div>');
    let $popWindow=$(contentHtml),
        size= $.extend({width:'460px',height:'370px'},area);
    $popWindow.css({
        width:size.width,
        height:size.height
    });
    let adjustPOS=function() {
        let scrollHeight=document.body.scrollTop || document.documentElement.scrollTop,
            pTop=$(window).height()-$popWindow.height(),
            pLeft=$(window).width()-$popWindow.width();
        $popWindow.css({'top':pTop/2,left:pLeft/2});
        $shade.height($('body').height());
        $('body').append($popWindow).append($shade);
    }
    adjustPOS();

    $('.close-btn,.go-close',$popWindow).on('click',function() {
        $popWindow.remove();
        $shade.remove();

    })
}

// 验证用户是否处于登陆状态
function isUserLogin() {
    let LoginDefer=$.Deferred(); //在函数内部，新建一个Deferred对象
    $.ajax({
        url: '/is-login',
        type: 'GET'
    })
        .done(function(data) {
            if(data) {
                //如果data有值，说明token已经过期，用户处于未登陆状态，并且需要更新token
                LoginDefer.reject(data);
            } else {
                //如果data为空，说明用户处于登陆状态，不需要做任何处理
                LoginDefer.resolve();
            }
        })
        .fail(function() {
            LoginDefer.reject();
        });

    return LoginDefer.promise(); // 返回promise对象
}

function useAjax(opt,callbackDone,callbackAlways) {
    let defaultOpt={
        type:'POST',
        dataType: 'json'
    };
    let option=$.extend(defaultOpt,opt);

    //防止跨域，只有post请求需要，get请求不需要
    $(document).ajaxSend(function(e, xhr, options) {
        let token = $("meta[name='_csrf']").attr("content");
        let header = $("meta[name='_csrf_header']").attr("content");
        xhr.setRequestHeader(header, token);
    });
    //当ajax请求失败的时候重定向页面
    $(document).ajaxError(function (event, jqXHR, ajaxSettings, thrownError) {
        if (jqXHR.status == 403) {
            if (jqXHR.responseText) {
                let data = JSON.parse(jqXHR.responseText);
                window.location.href = data.directUrl + (data.refererUrl ? "?redirect=" + data.refererUrl : '');
            }
        }
    });

    $.ajax(option)
        .done(function(data) {
            callbackDone && callbackDone(data);
        })
        .fail(function(data) {
            console.error('接口错误，请联系客服');
        })
        .always(function() {
            callbackAlways && callbackAlways();
        });
}

//倒计时
function countDownLoan(option,callback) {
    let defaultOpt={
        btnDom:'',
        time:60,
        textCounting:'秒后重新发送',
        isAfterText:'重新发送'
    };
    let options = $.extend({},defaultOpt,option),
        downtimer;
    let $countBtn= options.btnDom;

    let countDownStart=function() {
        let isInputButton = $countBtn[0].tagName.toUpperCase()=='INPUT';
        if(isInputButton) {
            $countBtn.val(options.time+ options.textCounting).prop('disabled',true).addClass('count-downing');
        } else {
            $countBtn.text(options.time + options.textCounting).prop('disabled',true).addClass('count-downing');
        }

        if(options.time==0) {
            //结束倒计时
            clearInterval(downtimer);
            callback && callback();
            if(isInputButton) {
                $countBtn.val(options.isAfterText);
            } else {
                $countBtn.text(options.isAfterText);
            }
            $countBtn.prop('disabled',false).removeClass('count-downing');
        } else {
            options.time = options.time -1;
        }

    }
    if(options.time>0) {
        countDownStart();//立即调用一次，解决延迟加载的问题
        downtimer=setInterval(function () {
            countDownStart();
        },1000);
    }
}
let MathDecimal={
    MathFloor(math) {
        //小数点保留2位小数，不要四舍五入,但是不强制2位小数点
        let re = /([0-9]+\.[0-9]{2})[0-9]*/;
        let mathString = math + '';
        let aNew = mathString.replace(re, "$1");
        return aNew;
    },
    MathRound(math) {
        //小数点保留2位小数，要四舍五入
        let newNum = Math.round(math * 100) / 100;
        return newNum;
    }
};
let decrypt={
    //加密
    compile:function (strId,realId) {
        let realIdStr=realId+'';
        let strIdObj=strId.split('');
        for(let i=0;i<11;i++) {
            strIdObj[2*i+2]=realIdStr[i]?realIdStr[i]:'a';
        }
        return strIdObj.join('');

    },
    //解密
    uncompile:function (strId) {
        let strIdString=strId+'';
        let strIdObj=strIdString.split(''),
            realId=[];
        for(let i=0;i<11;i++) {
            realId[i]=strIdObj[2*i+2];
        }

        let stringRealId=realId.join(''),
            getNum=stringRealId.match(/\d/gi);
        return getNum.join('');
    },
};
//获取前一天或者后一天的日期
let GetDateStr = function(date,AddDayCount) {
    var dd = new Date(date);
    dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
    var y = dd.getFullYear();
    var m = dd.getMonth()+1;//获取当前月份的日期
    var d = dd.getDate();

    return y + "-" + (m < 10 ? ('0' + m) : m) + "-" + (d < 10 ? ('0' + d) : d);
}
function CommonLayerTip(option,firstCallback,secondCallback) {
    layer.closeAll();
    let defaultOption = {
        btn:['确定', '取消'],
        content:$('#turnOnSendCaptcha'),
        area:['280px', '230px'],
    };

    let optionOk = $.extend({},defaultOption,option);
    layer.open({
        type: 1,
        title: false,
        closeBtn: 0,
        area: optionOk.area,
        shadeClose: false,
        skin: 'tip-square-box',
        btn: optionOk.btn,
        shade: optionOk.shade,
        content: optionOk.content,
        btn1: function () {
            firstCallback && firstCallback();
        },
        btn2: function () {
            secondCallback && secondCallback();
        }
    });
}

//为了添加重复的圆圈背景
var repeatBgSquare=(number=20) => '<i></i>'.repeat(number);
//中奖纪录滚动
function scrollList(domName, length,time) {
    var thisFun = this,
        scrollTimer;

    scrollTimer = setInterval(function () {
        scrollUp(domName, length);
    }, time?time:1500);
};
function scrollUp(domName, length) {

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

}
//活动状态,精确到前八位如2018-01-01 如果活动时间精确到时分秒，则此方法不能用
function activityStatus(dom) {
    //日期格式化
    Date.prototype.Format = function (fmt) { //author: meizz
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }

    let startTime = Number(dom.data('starttime').substring(0, 10).replace(/-/gi, '')),
        endTime = Number(dom.data('endtime').substring(0, 10).replace(/-/gi, '')),
        currentTime = Number(new Date().Format("yyyyMMdd"));

    if (currentTime < startTime) {
        //活动未开始
        return 'activity-noStarted';
    }
    else if (currentTime > endTime) {
        //活动已经结束
       return 'activity-end'

    }  else if(currentTime>=startTime && currentTime<=endTime){
        //活动中
        return 'activity-ing';
    }
};

// rem
function calculationFun(doc, win) {
    let docEl = doc.documentElement,
        resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
        recalc = function () {
            let clientWidth = docEl.clientWidth;
            if (!clientWidth) return;
            let fSize = 20 * (clientWidth /375);
            fSize > 40 && (fSize = 39.36);
            docEl.style.fontSize = fSize + 'px';
        };
    if (!doc.addEventListener) return;
    win.addEventListener(resizeEvt, recalc, false);
    doc.addEventListener('DOMContentLoaded', recalc, false);
};
//把px除以100成rem,用于移动端.调用的时候commonFun.calculationRem(document, window)
function calculationRem(doc, win) {
    let docEl = doc.documentElement,
        resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
        recalc = function () {
            let clientWidth = docEl.clientWidth;
            if (!clientWidth) return;
            let fSize = 100 * (clientWidth /750);
            fSize > 100 && (fSize = 98.4);
            docEl.style.fontSize = fSize + 'px';
        };
    if (!doc.addEventListener) return;
    win.addEventListener(resizeEvt, recalc, false);
    doc.addEventListener('DOMContentLoaded', recalc, false);
};

function phoneModal() {
    var u = navigator.userAgent, app = navigator.appVersion;
    var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //g
    var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
    if (isAndroid) {
        return 'android';
    }
    if (isIOS) {
        //这个是ios操作系统
        return 'ios';
    }
};
function toDownloadApp() {

    globalFun.categoryCodeUrl['android'] = window.commonStaticServer+'/images/apk/tuotiansudai_htracking.apk';
    let equipment=globalFun.equipment();
    if(equipment.wechat && equipment.kind=='android') {
        // 微信,并且是安卓，跳到页面
        window.location.href = "/app/download?app=htracking";
        return;
    } else {
        window.location.href =globalFun.categoryCodeUrl[equipment.kind];
    }

}

function Cycle(options) {
    this.id = options.id;
    this.width = options.width;
    this.height = options.height;
    this.percent = options.percent;
    this.border = options.border;
    this.bgColor = options.bgColor;
    this.barColor = options.barColor;
    this.fillColor = options.fillColor;
};

Cycle.prototype = {
    contructor: Cycle,
    init: function() {
        //创建画布对象
        var html = "<canvas id='canvas_" + this.id + "' width='" + this.width + "' height='" + this.height + "'></canvas>";
        document.getElementById(this.id).innerHTML = html;

        this.setOptions()
    },
    setOptions: function() {
        var degree = this.percent;
        var canvas = document.getElementById('canvas_' + this.id);
        var context = canvas.getContext('2d');
        context.clearRect(0, 0, this.width * 2, this.height * 2);
        //开始绘画
        context.beginPath();
        context.lineWidth = this.border;
        context.strokeStyle = this.bgColor;
        context.arc(this.width / 2, this.height / 2, (this.width / 2 - this.border / 2), 0, 2 * Math.PI);
        context.fillStyle = this.fillColor;
        context.fill();
        context.stroke();
        var deg = degree * 3.6 / 180 * Math.PI;
        context.beginPath();
        context.lineWidth = this.border;
        context.strokeStyle = this.barColor;
        context.arc(this.width / 2, this.height / 2, (this.width / 2 - this.border / 2), 0 - Math.PI / 2, deg - Math.PI / 2);
        context.stroke();
        context.beginPath();
    }
};

exports.refreshCaptcha = refreshCaptcha;
exports.initRadio = initRadio;
exports.IdentityCodeValid = IdentityCodeValid;
exports.checkedAge = checkedAge;
exports.popWindow = popWindow;
exports.isUserLogin = isUserLogin;
exports.useAjax = useAjax;
exports.countDownLoan = countDownLoan;
exports.MathDecimal = MathDecimal;
exports.decrypt = decrypt;
exports.GetDateStr = GetDateStr;
exports.scrollList = scrollList;
exports.activityStatus = activityStatus;
exports.CommonLayerTip = CommonLayerTip;
exports.repeatBgSquare = repeatBgSquare;
exports.calculationFun = calculationFun;
exports.calculationRem = calculationRem;
exports.phoneModal = phoneModal;
exports.Cycle = Cycle;
exports.toDownloadApp = toDownloadApp;



