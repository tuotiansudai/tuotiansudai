/**
 * Created by CBJ on 2015/11/11.
 */
require(['jquery','mustache','text!/tpl/notice-list.mustache','commonFun',,'pagination'], function ($,Mustache,ListTemplate) {
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
        /* notice detail*/
        if($noticeDetail.length) {
            var url=location.href,
             id=commonFun.parseURL(url).params.id;

            $.ajax({
                url: '../announce/'+id,
                type: 'GET',
                dataType: 'json'
            }).done(function(notice){
                $detailHead.find('.title').text(notice.title);
                $detailHead.find('time i').text(notice.createdTime.split(' ')[0]);
                $detailCon.html(notice.content);

                 //时间转换为年月日
                var updateTime=notice.updateTime.split(' ')[0],
                    thisDay=new Date(updateTime),
                    tYear=thisDay.getFullYear(),
                    tMonth=thisDay.getMonth(),
                    tDay=thisDay.getDate(),
                    _updateTime=tYear+'年'+tMonth+'月'+tDay+'日';
                $footer.find('span.update-time').text(_updateTime);


            });
        }
    });
});