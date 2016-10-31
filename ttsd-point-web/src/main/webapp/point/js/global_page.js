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
    //绑定监听事件
    this.addEventHandler=function(target,type,fn) {
        if(!target) {
            return;
        }
        if(target.addEventListener){
            target.addEventListener(type,fn);
        }else{
            target.attachEvent("on"+type,fn);
        }
    }
    this.hasClass=function(obj, cls) {
        return obj.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'));
    }
    this.addClass=function(obj, cls) {
        if (!this.hasClass(obj, cls)) obj.className += " " + cls;
    }
    this.removeClass=function(obj, cls) {
        if (this.hasClass(obj, cls)) {
            var reg = new RegExp('(\\s|^)' + cls + '(\\s|$)');
            obj.className = obj.className.replace(reg, ' ');
        }
    }
    this.toggleClass=function(obj, cls) {
        if(this.hasClass(obj,cls)){
            this.removeClass(obj, cls);
        }else{
            this.addClass(obj, cls);

        }
    }
}

globalFun.prototype={
    init:function() {

        this.addEventHandler(this.$('#closeDownloadBox'),'click',this.closeDownLoadBox.bind(this));
        this.addEventHandler(this.$('#btnExperience'),'click',this.toExperience.bind(this));
        this.addEventHandler(this.$('#getMore'),'click',this.moreFriendLinks.bind(this));

        this.addEventHandler(this.$('#showMainMenu'),'click',this.showMainMenu.bind(this));

        this.addEventHandler(this.$('#iphone-app-pop'),'mouseover',this.showAppCode.bind(this));
        this.addEventHandler(this.$('#iphone-app-pop'),'mouseleave',this.hideAppCode.bind(this));

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
        e.parentElement.style.display='none';
    },
    //去体验
    toExperience:function(e) {
        e.stopPropagation();
        e.preventDefault();
        location.href = "/app/download";
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
    }

}

var globalFun=new globalFun();
globalFun.init();

//cnzz统计
function cnzzPushConstructor() {
    this.trackClick=function(category, action, label) {
        var test=window.testlaney;
        _czc.push(['_trackEvent', category, action, label]);
    }
}
cnzzPush = new cnzzPushConstructor();




