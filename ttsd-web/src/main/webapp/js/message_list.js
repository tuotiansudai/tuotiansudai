require(['jquery', 'mustache', 'text!/tpl/message-list.mustache', 'pagination', 'layerWrapper', 'jquery.ajax.extension'], function ($, Mustache, messageListTemplate, pagination, layer) {
    $(function () {
        var $paginationElement = $('.pagination');

        $paginationElement.loadPagination({index: $paginationElement.data("page-index")}, function (data) {
            data.index = $paginationElement.data("page-index");
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
