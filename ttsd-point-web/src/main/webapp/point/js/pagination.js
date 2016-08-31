define(['jquery', 'underscore', 'mustache', 'jquery.ajax.extension'], function ($, _, Mustache) {
    (function ($) {
        var template = '<span class="count">共 {{count}} 条，当前第 {{index}} 页</span>'
            + '<span class="prev {{#hasPrev}}active{{/hasPrev}}">上一页</span>'
            + '<span class="next {{#hasNext}}active{{/hasNext}}">下一页</span>';

        $.fn.loadPagination = function (requestData, loadSuccess) {
            var self = $(this);
            var queryParams = '';
            requestData.index = requestData.index || 1;
            requestData.pageSize = requestData.pageSize || self.data('page-size');
            _.each(requestData, function (value, key) {
                queryParams += key + "=" + value + '&';
            });
            $.ajax({
                url: self.data('url') + '?' + queryParams,
                type: 'get',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8'
            }).success(function (response) {
                var data = response.data;
                if (data.status) {
                    var index = data.index;
                    var count = data.count;
                    var html = Mustache.render(template, {count: count, index: index, hasPrev: data.hasPreviousPage, hasNext: data.hasNextPage});
                    self.html(html);
                    if (data.hasPreviousPage) {
                        $('.pagination .prev').click(function () {
                            requestData.index = index - 1;
                            self.loadPagination(requestData, loadSuccess);
                        });
                    }
                    if (data.hasNextPage) {
                        $('.pagination .next').click(function () {
                            requestData.index = index + 1;
                            self.loadPagination(requestData, loadSuccess);
                        });
                    }
                    loadSuccess(data);
                }
            })
        }
    })($);
});