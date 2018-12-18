window.jQuery = window.$ = window.jquery_library(1);
window.layer = window.jquery_library(2);
window.layer.config({
    path: commonStaticServer+'/public/'
});
window._ = window.jquery_library(3);
window.$.fn=window.$.prototype;
window.IScroll = window.jquery_library(4);

require("publicStyle/reset.scss");
require("publicStyle/btn.scss");
require("publicStyle/table.scss");
require("publicStyle/page_frame.scss");
require("publicStyle/pagination.scss");
require("publicStyle/error.scss");
require("publicStyle/global.scss");
require("publicStyle/spritecss/global.css");

require('publicJs/error');
require('babel-polyfill');

window.Middleware= require('publicJs/middleware');

Function.prototype.before = function(beforefn) {
    var self = this; // 保存原函数的引用

    // 返回包含了原函数和新函数的"代理"函数
    return function() {
        var check = beforefn.apply(this, arguments); // 执行新函数,修正 this
        if(check) {
            self.apply(this, arguments); // 执行原函数
        }
    }
};

Function.prototype.after = function(afterfn) {

    var self = this;  //保存原函数的引用，这里指向before里的回调部分

    return function() {
        var ret = self.apply(this, arguments);  //执行原函数,这里指向before里的回调部分
        afterfn.apply(this, arguments);  //执行新函数
        return ret;
    }
};

//ie8不支持bind方法，这里做兼容处理
if (!Function.prototype.bind) {
    Function.prototype.bind = function (oThis) {
        if (typeof this !== "function") {
            throw new TypeError("error");
        }
        var aArgs = [],
            fToBind = this,
            fBound = function () {
                return fToBind.apply(oThis,aArgs.concat(Array.prototype.slice.call(arguments)));
            };
        fBound.prototype=fToBind.prototype;
        return fBound;
    };
}

var web_globalFun = (function() {
    function globalFun() {
        this.$=function(obj) {
            if (typeof(obj) == "string") {
                if (obj.indexOf("#") >= 0) {
                    obj = obj.replace("#", "");
                    if (document.getElementById(obj)) {
                        return document.getElementById(obj);
                    } else {
                        return null;
                    }
                }
            } else {
                return obj;
            }
        }
        //判断浏览器终端是pc还是mobile
        this.browserRedirect=function () {
            var sUserAgent = navigator.userAgent.toLowerCase();
            var bIsIpad = sUserAgent.match(/ipad/i) == "ipad";
            var bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os";
            var bIsMidp = sUserAgent.match(/midp/i) == "midp";
            var bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";
            var bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";
            var bIsAndroid = sUserAgent.match(/android/i) == "android";
            var bIsCE = sUserAgent.match(/windows ce/i) == "windows ce";
            var bIsWM = sUserAgent.match(/windows mobile/i) == "windows mobile";

            if (bIsIpad || bIsIphoneOs || bIsMidp || bIsUc7 || bIsUc || bIsAndroid || bIsCE || bIsWM) {
                //如果是上述设备就会以手机域名打开
                return 'mobile';
            } else {
                //否则就是电脑域名打开
                return 'pc';
            }
        }

        this.equipment=function() {
            var ua = navigator.userAgent.toLowerCase(),
                which={};
            var is_weixin=ua.match(/MicroMessenger/i) == "micromessenger"; //是否微信
            var is_alipay=ua.match(/alipayclient/i) == "alipayclient"; //是否支付宝
            var is_android=ua.match('android');
            var is_ios=ua.match('iphone') || ua.match('ipad');

            which.ios=is_ios;
            which.android=is_android;
            which.wechat=is_weixin;
            which.alipay=is_alipay;

            if(is_android) {
                which.kind='android';
            }
            else {
                which.kind = (is_weixin && is_ios) ? 'weIos' : 'ios';
            }
            return which;
        }

        this.categoryCodeUrl= {
            'android': 'https://tuotiansudai.com/app/tuotiansudai.apk',
            'ios': 'http://itunes.apple.com/us/app/id1039233966',
            'weIos': 'http://a.app.qq.com/o/simple.jsp?pkgname=com.tuotiansudai.tuotianclient'
        }

        //主要用来截取url后的参数
        this.parseURL=function(url) {
            var a =  document.createElement('a');
            a.href = url;
            return {
                source: url,
                protocol: a.protocol.replace(':',''),
                host: a.hostname,
                port: a.port,
                query: a.search,
                params: (function(){
                    var ret = {},
                        seg = a.search.replace(/^\?/,'').split('&'),
                        len = seg.length, i = 0, s;
                    for (;i<len;i++) {
                        if (!seg[i]) { continue; }
                        s = seg[i].split('=');
                        ret[s[0]] = s[1];
                    }
                    return ret;
                })(),
                file: (a.pathname.match(/\/([^\/?#]+)$/i) || [,''])[1],
                hash: a.hash.replace('#',''),
                path: a.pathname.replace(/^([^\/])/,'/$1'),
                relative: (a.href.match(/tps?:\/\/[^\/]+(.+)/) || [,''])[1],
                segments: a.pathname.replace(/^\//,'').split('/')
            };
        }
        //绑定监听事件
        this.addEventHandler=function(target) {
            if(!target) {
                return;
            }
            for(var i = 1; i < arguments.length - 1; i++) {
                if(target.addEventListener){
                    target.addEventListener(arguments[i], arguments[arguments.length - 1]);
                }else{
                    target.attachEvent("on"+arguments[i], arguments[arguments.length - 1]);
                }
            }

        };
        this.hasClass=function(obj, cls) {
            return obj.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'));
        };
        this.addClass=function(obj, cls) {
            if (!this.hasClass(obj, cls)) obj.className += " " + cls;
        };
        this.removeClass=function(obj, cls) {
            if (this.hasClass(obj, cls)) {
                var reg = new RegExp('(\\s|^)' + cls + '(\\s|$)');
                obj.className = obj.className.replace(reg, ' ');
            }
        };
        this.toggleClass=function(obj, cls) {
            if(this.hasClass(obj,cls)){
                this.removeClass(obj, cls);
            }else{
                this.addClass(obj, cls);

            }
        };
    }

    globalFun.prototype={
        init:function() {

            this.addEventHandler(this.$('#closeDownloadBox'),'click',this.closeDownLoadBox.bind(this));
            this.addEventHandler(this.$('#btnExperience'),'click',this.toExperience.bind(this));
            this.addEventHandler(this.$('#getMore'),'click',this.moreFriendLinks.bind(this));

            this.addEventHandler(this.$('#showMainMenu'),'click',this.showMainMenu.bind(this));

            this.addEventHandler(this.$('#iphone-app-pop'),'mouseover',this.showAppCode.bind(this));
            this.addEventHandler(this.$('#iphone-app-pop'),'mouseleave',this.hideAppCode.bind(this));
            this.logOut();

        },

        showMainMenu:function(e) {
            e.preventDefault();
            var target=e.target;
            var container= this.$('#TopMainMenuList');
            if(container.style.WebkitTransform  == ''){
                container.style.WebkitTransform  = 'translate(100%)';
            } else {
                container.style.WebkitTransform  = '';
            }

        },
        //判断是否为viewport
        isViewPort:function() {
            var metaTags=document.getElementsByTagName('meta'),
                metaLen=metaTags.length,i=0;
            for(;i<metaLen;i++) {
                if(metaTags[i].getAttribute('name')=='viewport') {
                    return true;
                }
            }
            return false;
        },
        //是否隐藏主菜单
        isShowMainMenu:function() {
            var isViewport=this.isViewPort();
            var isMobile=this.browserRedirect();
            if(isViewport && isMobile=='mobile') {
                this.$('#closeDownloadBox').style.display='none';
            }
        },
        //关闭立即体验
        closeDownLoadBox:function(e) {
            e.stopPropagation();
            e.preventDefault();
            e.currentTarget.parentElement.style.display='none';
        },
        //去体验
        toExperience:function(e) {
            e.stopPropagation();
            e.preventDefault();
            var equipment=this.equipment();
            if(equipment.wechat && equipment.kind=='android') {
                // 微信,并且是安卓，跳到页面
                location.href = "/app/download";
                return;
            }
            else {
                //非微信
                location.href =this.categoryCodeUrl[equipment.kind];
            }
        },
        //显示app扫描码
        showAppCode:function() {
            this.removeClass(this.$('#iphone-app-img'),'hide');
        },
        //隐藏app扫描码
        hideAppCode:function() {
            this.addClass(this.$('#iphone-app-img'),'hide');
        },
        //友情链接
        moreFriendLinks:function(event) {
            var objBtn=event.currentTarget,
                ulLIst=objBtn.previousElementSibling,
                ulHeight='30px';
            this.toggleClass(objBtn,"active");
            ulHeight=this.hasClass(objBtn,"active")?'auto':'30px';
            ulLIst.style.height=ulHeight;
        },
        //点击注销，退出登陆
        logOut:function() {
            var that=this;
            that.addEventHandler(that.$('#logout-link'),'click',function() {
                that.$('#logout-form').submit();
            });
        },
        decorateRadioCheck:function($input,$radioLabel) {
            var numRadio=$input.length;
            if(numRadio) {
                $input.each(function(key,option) {
                    var $this=$(this);
                    if($this.is(':checked')) {
                        $this.next('label').addClass('checked');
                    }
                    $this.next('label').click(function() {
                        var $thisLab=$(this);
                        if(!/checked/.test(this.className)) {
                            $radioLabel.removeClass('checked');
                            $thisLab.addClass('checked');
                        }
                    });
                });

            }
        },
        // 动态插入Css标签
        loadCss:function(url) {
            var link = document.createElement("link");
            link.type = "text/css";
            link.rel = "stylesheet";
            link.href = url;
            document.getElementsByTagName("head")[0].appendChild(link);
        },
        // 动态插入script标签
        createScript:function(url, callback){
            var oScript = document.createElement('script');
            oScript.type = 'text/javascript';
            oScript.src = commonStaticServer+url;
            oScript.async = false;
            // IE9及以上浏览器，Firefox，Chrome，Opera ,

            //插入到body底部
            var currentScriptElem=this.$('#currentScript');
            currentScriptElem.parentNode.insertBefore(oScript,currentScriptElem);

            window.onload=function() {
                callback && callback();
            }
        }
    }
    return globalFun;
})();

//引入代理类实现单例模式
//globalFun与ProxyGlobalFun组合达到单例模式效果
var Proxy_GlobalFun=function() {
    var instance;
    if(!instance) {
        instance=new web_globalFun();
    }
    return instance;
}
$.fn.initCheckbox = function (callback) {
    return $(this).each(function () {
        $(this).bind('click', function () {
            var $this = $(this);
            var checked = $this.find('input:checkbox').prop('checked');
            if (checked) {
                $this.addClass("on");
            } else {
                $this.removeClass("on");
            }
            callback && callback(this);
        });
    });
};

window.globalFun =new Proxy_GlobalFun();
window.globalFun.init();


//模拟真实的checkbox
$.fn.initCheckbox=function(callback) {
    return $(this).each(function() {
        $(this).bind('click',function() {
            var $this=$(this);
            var checked=$this.find('input:checkbox').prop('checked');
            if(checked) {
                $this.addClass("on");
            }
            else {
                $this.removeClass("on");
            }
            callback && callback(this);
        })
    });
};

$('.subMenuItem').on('click',function () {
    var position = $(this).data('position');
    $(this).addClass("default_light_item").siblings().removeClass("default_light_item");
    $("html,body").animate({scrollTop: $(position).offset().top}, 200);
    // $('.left-nav').animate({marginTop: $(position).offset().top - 161}, 300);
});

$('.totalMenu').on('click',function () {
    if ($(this).find('.text_icon').hasClass('text_icon_open')) {
        $(this).removeClass('menuItem').removeClass('active');
        $(this).find('.text_icon').removeClass('text_icon_open').addClass('text_icon_close');
        $(this).siblings('.subMenu').removeClass('subMenu_show').addClass('subMenu_hide');
    }
    else {
        location.href = '/about/' + $(this).data('current');
    }
});















