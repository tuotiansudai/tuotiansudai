/**
 * [name]:membership record page
 * [user]:xuqiang
 * [date]:2016-08-08
 */
require(['jquery','template','pagination'],function($, tpl){
	$(function() {
		var $dataList=$('#dataList');
		if($dataList.length) {
            var requestData={"index":1,"pageSize":10};
            $('#pageList').loadPagination(requestData, function (data) {
                $dataList.html(tpl('dataListTpl', data));
            });
        }
	});
})