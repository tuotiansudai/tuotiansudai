//adjustMobileHideHack();
//function adjustMobileHideHack() {
//
//    //this function will be remove when all pages are responsive
//    var bodyDom=document.getElementsByTagName("body")[0],
//        userAgent = navigator.userAgent.toLowerCase(),
//        metaTags=document.getElementsByTagName('meta'),
//        metaLen=metaTags.length,isResponse=false,isPC=false,i=0;
//    isPC = !(userAgent.indexOf('android') > -1 || userAgent.indexOf('iphone') > -1 || userAgent.indexOf('ipad') > -1);
//    for(;i<metaLen;i++) {
//        if(metaTags[i].getAttribute('name')=='viewport') {
//            isResponse=true;
//        }
//    }
//    bodyDom.className=(!isResponse&&!isPC)?'page-width':'';
//}

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
        this.$('#iphone-app-pop').addEventListener('click',this.showAppCode.bind(this),false);
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
    showAppCode:function() {
        var obg=this.$('#iphone-app-img');
        this.toggleClass(obg,'hide');

    }

}

var globalFun=new globalFun();
globalFun.init();

//window.$ = function(id) {
//    return document.getElementById(id);
//};
//
//
//var imgDom=window.$('iphone-app-img'),
//    TopMainMenuList=window.$('TopMainMenuList');
//
//if (window.$('iphone-app-pop')) {
//    window.$('iphone-app-pop').onclick=function(event) {
//        if(imgDom.style.display == "block") {
//            imgDom.style.display='none';
//        }
//        else {
//            imgDom.style.display='block';
//        }
//        if (event.stopPropagation) {
//            event.stopPropagation();
//        }
//        else if (window.event) {
//            window.event.cancelBubble = true;
//        }
//    };
//}

//document.getElementsByTagName("body")[0].onclick=function(e) {
//    var userAgent = navigator.userAgent.toLowerCase(),
//        event = e || window.event,
//        target = event.srcElement || event.target;
//    if(target.tagName=='LI' ) {
//        return;
//    }
//    imgDom.style.display='none';
//    if(userAgent.indexOf('android') > -1 || userAgent.indexOf('iphone') > -1 || userAgent.indexOf('ipad') > -1) {
//
//        //判断是否为viewport
//        var metaTags=document.getElementsByTagName('meta'),
//            metaLen=metaTags.length,i=0;
//        for(;i<metaLen;i++) {
//            if(metaTags[i].getAttribute('name')=='viewport') {
//                TopMainMenuList.style.display='none';
//            }
//        }
//    }
//
//};
//
//
//if(window.$('getMore')){
//    document.getElementById('getMore').onclick=function(){
//        var obj = document. getElementById('getMore');
//        toggleClass(obj,"active");
//    }
//}

