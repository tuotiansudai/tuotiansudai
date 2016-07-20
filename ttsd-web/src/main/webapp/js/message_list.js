require(['jquery', 'mustache', 'text!/tpl/message-list.mustache', 'pagination', 'layerWrapper', 'jquery.ajax.extension'], function ($, Mustache, messageListTemplate, pagination, layer) {
    $(function () {
        var $paginationElement = $('.pagination');

        var index = $paginationElement.data("page-index");
        var hash = window.location.hash;
        if (hash) {
            index = hash.substr(1, hash.length);
        }
        $paginationElement.loadPagination({index: index}, function (data) {
            window.location.hash = data.index;
            data.messageTypeHandler =  function () {
                return function (text, render) {
                    return render(text) === 'MANUAL' ? "拓天速贷" : "系统消息";
                }
            };
            var html = Mustache.render(messageListTemplate, data);
            $('.list-container .global-message-list.active').html(html);
            $('.read-all-messages').click(function () {
                $.ajax({
                    url: "/message/read-all",
                    type: 'POST',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8'
                }).done(function (response) {
                    if (response.data.status) {
                        window.location.reload();
                    }
                })
            });
        });
    });
});
