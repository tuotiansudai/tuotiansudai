require('pointStyle/point_record.scss');
let tpl = require('art-template/dist/template');
require('webJsModule/pagination');

let $dataList=$('#dataList');
if($dataList.length) {
    var requestData={"index":1,"pageSize":10};
    $('#pageList').loadPagination(requestData, function (data) {
        $dataList.html(tpl('dataListTpl', data));
    });
}
