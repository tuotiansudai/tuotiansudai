require(['jquery','template','load-swiper','layerWrapper','load_echarts','pagination','fancybox'], function ($,tpl,loadSwiper,layer,loadEcharts) {
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
                $noticeList.html(tpl('noticeListTpl', data));
                $noticeList.find('span').each(function(key,option) {
                    var getTime=$(option).text();
                    $(option).text(getTime.substr(0,10));
                });
                if(/app/gi.test(location.search)) {
                    $noticeList.find('li a').each(function(key,option) {
                       var thisURL= $(option).attr('href')+'?source=app';
                        $(option).attr('href',thisURL);
                    });
                }
            });
        }

        if($('#errorContainer').length) {
            setTimeout(function(){
                window.location="/";
            },10000);
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
        $("#companyPhoto li a").fancybox({
            'titlePosition' : 'over',
            'cyclic'        : false,
            'showCloseButton':true,
            'showNavArrows' : true,
            'titleFormat'   : function(title, currentArray, currentIndex, currentOpts) {
                return '<span id="fancybox-title-over">' + (currentIndex + 1) + ' / ' + currentArray.length + (title.length ? ' &nbsp; ' + title : '') + '</span>';
            }
        });
        if ($("#dataRecord").length) {
            $.ajax({
                url: '/about/operation-data/chart',
                type: 'GET',
                dataType: 'json'
            }).done(function(data) {
                $('#operationDays').text(data.operationDays+'天');
                $('#usersCount').text(data.usersCount+'人');
                $('#tradeAmount').text(data.tradeAmount+'元');
                var dataJson = {
                        title:'拓天速贷',
                        sub:'金额',
                        name:'运营数据',
                        month:data.month,
                        money:data.money
                    },
                    option = loadEcharts.optionCategory.BarOption(dataJson),
                    opt = loadEcharts.ChartConfig('dataRecord', option);
                loadEcharts.RenderChart(opt);
            }).fail(function() {
                layer.msg('请求数据失败，请刷新页面重试！');
            });
        }
        if($('.jump-tip').length>0){
            setInterval(function(){
                $('.jump-tip i').text()<1?window.location="/":$('.jump-tip i').text(function(index,num){return parseInt(num)-1});
            },1000);
        }
    });
});