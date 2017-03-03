function refreshCaptcha(dom,url) {
    let captcha= url +'?'+ new Date().getTime().toString();
    dom.setAttribute('src',captcha);
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
        url: '/isLogin',
        type: 'GET'
    })
        .done(function(data) {
            if(data) {
                //如果data有值，说明token已经过期，用户处于未登陆状态，并且需要更新token
                LoginDefer.reject(data);
            }
            else {
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
        textCounting:'秒后重新发送'
    };
    let options = $.extend({},defaultOpt,option),
        downtimer;
    let $countBtn= options.btnDom;

    let countDownStart=function() {
        $countBtn.text(options.time-- + options.textCounting).prop('disabled',true).addClass('count-downing');
        if(options.time==0) {
            //结束倒计时
            clearInterval(downtimer);
            callback && callback();
            $countBtn.text('重新发送').prop('disabled',false).removeClass('count-downing');
        }
    }
    if(options.time>0) {
        countDownStart();//立即调用一次，解决延迟加载的问题
        $countBtn.val(options.textCounting);
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
        let strIdObj=realIdStr.split(''),
            realLen=realIdStr.length;
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

