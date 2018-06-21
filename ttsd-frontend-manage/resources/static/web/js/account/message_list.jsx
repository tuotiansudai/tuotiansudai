//消息中心
require('webStyle/account/message_list.scss');
require('publicJs/pagination');
let commonFun= require('publicJs/commonFun');
let $userMessageList=$('#userMessageList'),
    $userMessageDetail=$('#userMessageDetail');

if($userMessageList.length) {
    var $paginationElement = $('.pagination');

    var index = $paginationElement.data("page-index");
    var hash = window.location.hash;
    if (hash) {
        index = hash.substr(1, hash.length);
    }
    $paginationElement.loadPagination({index: index}, function (data) {
        window.location.hash = data.index;
        //获取模版内容
        let $messageListTemplate=$('#messageListTemplate'),
            messageTpl=$messageListTemplate.html();
        // 解析模板, 返回解析后的内容
        let render = _.template(messageTpl);
        let html = render(data);

        $('.list-container .global-message-list.active').html(html);

        $('.read-all-messages').click(function () {
            commonFun.useAjax({
                url: "/message/read-all",
                type: 'POST'
            },function(response) {
                if (response.data.status) {
                    window.location.reload();
                }
            });

        });
        //点击标题跳转详情,阻止默认的a标签连接
        (function() {
            var $userMessageList=$('#userMessageList');
            $userMessageList.on('click','.jump-detail',function() {
                var $this=$(this),
                    url=$this.data('url');
                location.href=url;
            });
            $userMessageList.on('click','a',function(event) {
                event.preventDefault();
            })

        })();

    });
}

if($userMessageDetail.length) {
    $('.go-back a',$userMessageDetail).click(function (event) {
        event.preventDefault();
        var index = window.location.hash;
        window.location = "/message/user-messages" + index;
        return false;
    });
}

let metaViewPort = $('meta[name=viewport]');//
metaViewPort.remove()
$('head').prepend($('<meta name="viewport" content="width=1024,user-scalable=yes" />'));