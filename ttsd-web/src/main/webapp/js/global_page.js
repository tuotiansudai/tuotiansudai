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
    //cnzz统计
    this.trackClick=function(category, action, label) {
        _czc.push(['_trackEvent', category, action, label]);
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
        this.$('#closeDownloadBox').addEventListener('click',this.closeDownLoadBox.bind(this),false);
        this.$('#btnExperience').addEventListener('click',this.toExperience.bind(this),false);
        this.$('#getMore').addEventListener('click',this.moreFriendLinks.bind(this),false);

        this.$('#showMainMenu').addEventListener('touchstart',this.showMainMenu.bind(this),false); //app点击显示菜单
        //显示手机app二维码
        document.addEventListener('click',function(event) {
            var target = event.target;
            if(event.target.offsetParent) {
                var offsetID=event.target.offsetParent.id;
                this.showAppCode(offsetID);
            }
        }.bind(this),false);
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
    //是否显示隐藏app扫描码
    showAppCode:function(id) {
        var objBox=this.$('#'+id);
        if(objBox && id=='iphone-app-pop') {
               var obg=this.$('#'+id).children[2];
                this.toggleClass(obg,'hide');
        }
        else {
            this.addClass(this.$('#iphone-app-img'),'hide');
        }
    },
    moreFriendLinks:function(event) {
        var objBtn=event.currentTarget,
            ulLIst=objBtn.previousElementSibling,
            ulHeight='30px';
        this.toggleClass(objBtn,"active");
        ulHeight=this.hasClass(objBtn,"active")?'auto':'30px';
        ulLIst.style.height=ulHeight;
    }
}

var cnzzPush=new globalFun();
cnzzPush.init();


