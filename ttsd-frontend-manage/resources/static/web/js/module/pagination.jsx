let commonFun= require('publicJs/commonFun');
$.fn.loadPagination = function (requestData, loadSuccess) {
    var self = $(this);
    var queryParams = '';
    requestData.index = requestData.index || 1;
    requestData.pageSize = requestData.pageSize || self.data('page-size');
    _.each(requestData, function (value, key) {
        queryParams += key + "=" + value + '&';
    });

    commonFun.useAjax({
        type:'GET',
        url:self.data('url') + '?' + queryParams
    },function(response) {
        var data = response.data;
        if (data.status) {
            let paramGroup={
                count: data.count,
                index: data.index,
                hasPrev: data.hasPreviousPage,
                hasNext: data.hasNextPage
            };
            let hasPrev=paramGroup.hasPrev ? 'active' :'';
            let hasNext=paramGroup.hasNext ? 'active' :'';
            let paginatonHtml = `<span class="count">共${paramGroup.count}条，当前第${paramGroup.index}页</span>
                                <span class="prev ${hasPrev}">上一页</span>
                                <span class="next ${hasNext}">下一页</span>`;

            self.html(paginatonHtml);
            if (data.hasPreviousPage) {
                $('.pagination .prev').click(function () {
                    requestData.index = paramGroup.index - 1;
                    self.loadPagination(requestData, loadSuccess);
                });
            }
            if (data.hasNextPage) {
                $('.pagination .next').click(function () {
                    requestData.index = paramGroup.index + 1;
                    self.loadPagination(requestData, loadSuccess);
                });
            }
            loadSuccess(data);
        }
    });
}

