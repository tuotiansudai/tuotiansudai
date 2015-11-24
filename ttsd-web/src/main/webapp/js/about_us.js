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

        if($('#registerFlowStep').length) {
            var $registerFlowStep=$('#registerFlowStep'),
                $stepTab=$('.step-register-tab',$registerFlowStep),
                $slideImgBox=$('.slide-img-box',$registerFlowStep),
                $btnLast=$('.last',$registerFlowStep),
                $btnNext=$('.next',$registerFlowStep),
                cNum= 0,len=$stepTab.find('li').length;

            $stepTab.find('li').click(function(index) {
                var $this=$(this),
                    num=$stepTab.find('li').index(this);
                $this.addClass('on').siblings('li').removeClass('on');
                $slideImgBox.find('li').eq(num).show().siblings('li').hide();
            });
            $btnNext.click(function() {
                cNum=$stepTab.find('li.on').index();
                var aNum=(cNum<len-1)?(cNum+1):0;
                $stepTab.find('li').eq(aNum).addClass('on').siblings('li').removeClass('on');
                $slideImgBox.find('li').eq(aNum).show().siblings('li').hide();
            });
            $btnLast.click(function() {
                cNum=$stepTab.find('li.on').index();
                var aNum=(cNum==0)?(len-1):(cNum-1);
                $stepTab.find('li').eq(aNum).addClass('on').siblings('li').removeClass('on');
                $slideImgBox.find('li').eq(aNum).show().siblings('li').hide();
            });

        }
    });
});