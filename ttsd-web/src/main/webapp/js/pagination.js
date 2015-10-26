define(['jquery', 'text!/tpl/pagination.mustache', 'underscore'], function ($, template, _) {
    (function ($) {
        $.fn.loadPagination = function (requestData, loadSuccess) {
            var self = $(this);
            var queryParams = '';
            _.each(requestData, function (value, key) {
                queryParams += key + "=" + value + '&';
            });
            $.ajax({
                url: self.data('url') + '?' + queryParams,
                type: 'get',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8'
            }).success(function(response) {
                if (response.status) {
                    loadSuccess(response.data);
                }
            })
        }
    })($);
});