!function(t){function e(n){if(i[n])return i[n].exports;var a=i[n]={exports:{},id:n,loaded:!1};return t[n].call(a.exports,a,a.exports,e),a.loaded=!0,a.exports}var i={};return e.m=t,e.c=i,e.p="http://localhost:3008/prod",e(0)}({0:function(t,e,i){"use strict";function n(){this.trackClick=function(t,e,i){_czc.push(["_trackEvent",t,e,i])}}i(19),Function.prototype.bind||(Function.prototype.bind=function(t){if("function"!=typeof this)throw new TypeError("error");var e=[],i=this,n=function(){return i.apply(t,e.concat(Array.prototype.slice.call(arguments)))};return n.prototype=i.prototype,n});var a=function(){function t(){this.$=function(t){return"string"!=typeof t?t:t.indexOf("#")>=0?(t=t.replace("#",""),document.getElementById(t)?document.getElementById(t):null):void 0},this.browserRedirect=function(){var t=navigator.userAgent.toLowerCase(),e="ipad"==t.match(/ipad/i),i="iphone os"==t.match(/iphone os/i),n="midp"==t.match(/midp/i),a="rv:1.2.3.4"==t.match(/rv:1.2.3.4/i),o="ucweb"==t.match(/ucweb/i),s="android"==t.match(/android/i),r="windows ce"==t.match(/windows ce/i),c="windows mobile"==t.match(/windows mobile/i);return e||i||n||a||o||s||r||c?"mobile":"pc"},this.equipment=function(){var t=navigator.userAgent.toLowerCase(),e={},i="micromessenger"==t.match(/MicroMessenger/i),n="alipayclient"==t.match(/alipayclient/i),a=t.match("android"),o=t.match("iphone")||t.match("ipad");return e.ios=o,e.android=a,e.wechat=i,e.alipay=n,a?e.kind="android":e.kind=i&&o?"weIos":"ios",e},this.categoryCodeUrl={android:"https://tuotiansudai.com/app/tuotiansudai.apk",ios:"http://itunes.apple.com/us/app/id1039233966",weIos:"http://a.app.qq.com/o/simple.jsp?pkgname=com.tuotiansudai.tuotianclient"},this.parseURL=function(t){var e=document.createElement("a");return e.href=t,{source:t,protocol:e.protocol.replace(":",""),host:e.hostname,port:e.port,query:e.search,params:function(){for(var t,i={},n=e.search.replace(/^\?/,"").split("&"),a=n.length,o=0;o<a;o++)n[o]&&(t=n[o].split("="),i[t[0]]=t[1]);return i}(),file:(e.pathname.match(/\/([^\/?#]+)$/i)||[,""])[1],hash:e.hash.replace("#",""),path:e.pathname.replace(/^([^\/])/,"/$1"),relative:(e.href.match(/tps?:\/\/[^\/]+(.+)/)||[,""])[1],segments:e.pathname.replace(/^\//,"").split("/")}},this.addEventHandler=function(t,e,i){t&&(t.addEventListener?t.addEventListener(e,i):t.attachEvent("on"+e,i))},this.hasClass=function(t,e){return t.className.match(new RegExp("(\\s|^)"+e+"(\\s|$)"))},this.addClass=function(t,e){this.hasClass(t,e)||(t.className+=" "+e)},this.removeClass=function(t,e){if(this.hasClass(t,e)){var i=new RegExp("(\\s|^)"+e+"(\\s|$)");t.className=t.className.replace(i," ")}},this.toggleClass=function(t,e){this.hasClass(t,e)?this.removeClass(t,e):this.addClass(t,e)}}return t.prototype={init:function(){this.addEventHandler(this.$("#closeDownloadBox"),"click",this.closeDownLoadBox.bind(this)),this.addEventHandler(this.$("#btnExperience"),"click",this.toExperience.bind(this)),this.addEventHandler(this.$("#getMore"),"click",this.moreFriendLinks.bind(this)),this.addEventHandler(this.$("#showMainMenu"),"click",this.showMainMenu.bind(this)),this.addEventHandler(this.$("#iphone-app-pop"),"mouseover",this.showAppCode.bind(this)),this.addEventHandler(this.$("#iphone-app-pop"),"mouseleave",this.hideAppCode.bind(this)),this.logOut()},showMainMenu:function(t){t.preventDefault();var e=(t.target,this.$("#TopMainMenuList"));""==e.style.WebkitTransform?e.style.WebkitTransform="translate(100%)":e.style.WebkitTransform=""},isViewPort:function(){for(var t=document.getElementsByTagName("meta"),e=t.length,i=0;i<e;i++)if("viewport"==t[i].getAttribute("name"))return!0;return!1},isShowMainMenu:function(){var t=this.isViewPort(),e=this.browserRedirect();t&&"mobile"==e&&(this.$("#closeDownloadBox").style.display="none")},closeDownLoadBox:function(t){t.stopPropagation(),t.preventDefault(),t.currentTarget.parentElement.style.display="none"},toExperience:function(t){t.stopPropagation(),t.preventDefault();var e=this.equipment();return e.wechat&&"android"==e.kind?void(location.href="/app/download"):void(location.href=this.categoryCodeUrl[e.kind])},showAppCode:function(){this.removeClass(this.$("#iphone-app-img"),"hide")},hideAppCode:function(){this.addClass(this.$("#iphone-app-img"),"hide")},moreFriendLinks:function(t){var e=t.currentTarget,i=e.previousElementSibling,n="30px";this.toggleClass(e,"active"),n=this.hasClass(e,"active")?"auto":"30px",i.style.height=n},logOut:function(){this.addEventHandler(this.$("#logout-link"),"click",function(){this.$("#logout-form").submit()})},decorateRadioCheck:function(t,e){var i=t.length;i&&t.each(function(t,i){var n=$(this);n.is(":checked")&&n.next("label").addClass("checked"),n.next("label").click(function(){var t=$(this);/checked/.test(this.className)||(e.removeClass("checked"),t.addClass("checked"))})})},loadCss:function(t){var e=document.createElement("link");e.type="text/css",e.rel="stylesheet",e.href=t,document.getElementsByTagName("head")[0].appendChild(e)},createScript:function(t,e){var i=document.createElement("script");i.type="text/javascript",i.async=!0,i.src=t,i.onload=function(){e&&e()};var n=document.getElementsByTagName("script")[0];n.parentNode.insertBefore(i,n)}},t}(),o=function(){var t;return t||(t=new a),t};window.globalFun=new o,window.globalFun.init(),window.cnzzPush=new n},19:function(t,e){}});