/**
 * Created by CBJ on 2015/11/11.
 */
require(['jquery','mustache','text!/tpl/notice-list.mustache','commonFun','pagination'], function ($,Mustache,ListTemplate) {
    $(function () {
        var $noticeList=$('#noticeList'),
            $noticeDetail=$('#noticeDetail'),
            $detailHead=$('h2',$noticeDetail),
            $detailCon=$('.detail-content',$noticeDetail),
            $footer=$('footer',$noticeDetail),
            paginationElement = $('.pagination');

        /* notice list*/
        if($noticeList.length) {
            var requestData={"index":1,"pageSize":10};
            paginationElement.loadPagination(requestData, function (data) {
                var html = Mustache.render(ListTemplate, data);
                $noticeList.html(html);
            });
        }
    });
});