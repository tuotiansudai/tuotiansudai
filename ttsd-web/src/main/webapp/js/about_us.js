require(['jquery','mustache','text!/tpl/notice-list.mustache','load-swiper','layerWrapper','commonFun','pagination'], function ($,Mustache,ListTemplate,loadSwiper,layer) {
    $(function () {
        var $noticeList=$('#noticeList'),
            $noticeDetail=$('#noticeDetail'),
            $detailHead=$('h2',$noticeDetail),
            $detailCon=$('.detail-content',$noticeDetail),
            $footer=$('footer',$noticeDetail),
            $problemList=$('.problem-list dt span'),
            paginationElement = $('.pagination');

        if($noticeList.length) {
            var requestData={"index":1,"pageSize":10};
            paginationElement.loadPagination(requestData, function (data) {
                var html = Mustache.render(ListTemplate, data);
                $noticeList.html(html);
                $noticeList.find('time').each(function(key,option) {
                    var getTime=$(option).text();
                    $(option).text(getTime.substr(0,10));
                });
                if(/app/gi.test(location.search)) {
                    var noticeList=$('.notice-list');
                    noticeList.find('li a').each(function(key,option) {
                       var thisURL= $(option).attr('href')+'?source=app';
                        $(option).attr('href',thisURL);
                    });
                }
            });
        }

        if($('#registerFlowStep').length) {
            var $registerFlowStep=$('#registerFlowStep'),
                $stepTab=$('.step-register-tab',$registerFlowStep),
                $slideImgBox=$('.slide-img-box',$registerFlowStep),
                $btnLast=$('img.last-step',$registerFlowStep),
                $btnNext=$('img.next-step',$registerFlowStep),
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

        if($('#errorContainer').length) {
            setTimeout(function(){
                window.location="/";
            },10000);
        }

        if($('#WhetherApp').length) {
            var viewport=commonFun.browserRedirect();
            if(/app/gi.test(location.search)) {
                if($('#WhetherApp').find('.res-no-app').length) {
                    $('#WhetherApp').find('.res-no-app').remove();
                }
                $('.header-container,.nav-container,.footer-container').remove();
                if($('.left-nav').length) {
                    $('.left-nav').remove();
                }
            }
            if(viewport=='mobile') {
                if($('#noticeDetail').length || $('#noticeList').length) {
                    $('.header-container,.nav-container,.footer-container').remove();
                    $('.left-nav').remove();
                }
            }
        }

        if($('#activityAwardBox').length) {

            var $activityAwardBox=$('#activityAwardBox'),
                screenWid=$(window).width(),
                viewport=commonFun.browserRedirect();
            if(viewport=='pc') {
                $activityAwardBox.find('.wide-screen-left,.wide-screen-right').width((screenWid-1000)/2).show();
            }
        }

        if($problemList.length) {
            $problemList.on('click', function(e) {
                e.preventDefault();
                var $self=$(this),
                    $dtDom=$self.parent('dt'),
                    $parents=$dtDom.parent();

                if($dtDom.next().hasClass('active')){
                    $dtDom.next().removeClass('active');
                    $dtDom.find('i').removeClass('fa-toggle-down').addClass('fa-toggle-up');
                }else{
                    $parents.find('dd').removeClass('active');
                    $parents.find('i').removeClass('fa-toggle-down').addClass('fa-toggle-up');
                    $dtDom.next().addClass('active');
                    $dtDom.find('i').removeClass('fa-toggle-up').addClass('fa-toggle-down');
                }
            });
        }

        if($('#redEnvelope').length) {
            $('#redEnvelope').click(function(event) {
                var serverTime = new Date($(event.currentTarget).data("kick-off-date")).getTime();
                if (serverTime > new Date("2016-02-02 00:00:00").getTime()) {
                    return;
                }
                layer.open({
                    type: 1,
                    title: '',
                    area: ['490px', '240px'],
                    skin:'red-envelope-box-out',
                    closeBtn: 0,
                    shadeClose: true,
                    move: false,
                    content: $('#redEnvelopePopWindow'),
                    success: function (layero, index) {
                        $('#redEnvelopePopWindow').find('.close-tip').click(function() {
                            layer.closeAll();
                        });
                    }
                });
                return false;
            });
        }

    });
});