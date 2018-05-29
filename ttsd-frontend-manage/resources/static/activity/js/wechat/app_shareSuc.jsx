// document.cookie="registerMobile="+18810985132;

let param = JSON.parse('{"' + decodeURI(location.search.substring(1)).replace(/"/g, '\\"').replace(/&/g, '","').replace(/=/g,'":"') + '"}')
$('#register_btn').on('click',() => {
    delCookie('registerMobile');
    location.href = '/activity/app-share?referrerMobile=' + param["referrerMobile"];
});

let getCookie = function(name) {
    let arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
    if(arr=document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
}

let delCookie = function(name) {
    let exp = new Date();
    exp.setTime(exp.getTime() - 1);
    let cval = getCookie(name);
    if(cval!=null)
        document.cookie= name + "="+cval+";expires="+exp.toGMTString();
};

