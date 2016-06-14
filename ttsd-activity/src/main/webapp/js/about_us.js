require(['jquery','mustache','text!tpl/notice-list.mustache','load-swiper','layerWrapper','commonFun','pagination','echarts','fancybox','layerWrapper'], function ($,Mustache,ListTemplate,loadSwiper,layer) {
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
        $.ajax({
            url: '/about/operation-data/chart',
            type: 'GET',
            dataType: 'json'
        })
        .done(function(data) {
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
                option = MyChartsObject.ChartOptionTemplates.Bar(dataJson,'YTTTTT'),
                container = $("#dataRecord")[0],
                opt = MyChartsObject.ChartConfig(container, option);
                MyChartsObject.Charts.RenderChart(opt);
        })
        .fail(function() {
            layer.msg('请求数据失败，请刷新页面重试！');
        });
    });
});