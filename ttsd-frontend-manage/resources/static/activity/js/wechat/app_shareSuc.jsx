
let param = JSON.parse('{"' + decodeURI(location.search.substring(1)).replace(/"/g, '\\"').replace(/&/g, '","').replace(/=/g,'":"') + '"}')
$('#register_btn').on('click',() => {
    location.href = '/activity/app-share?referrerMobile=' + param["referrerMobile"];
});
