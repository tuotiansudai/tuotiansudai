require(['jquery','mustache','text!tpl/notice-list.mustache','load-swiper','layerWrapper','commonFun','pagination','echarts','fancybox'], function ($,Mustache,ListTemplate,loadSwiper,layer) {
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
            url: '/about/info-publish',
            type: 'GET',
            dataType: 'json'
        })
        .done(function(data) {
            console.log(data);
            require.config({
                paths: {
                    echarts: './js/dist'
                }
            });
            var option = {
                      color:['#ff9c1b'],
                        title : {
                            text: '拓天速贷',
                            subtext: '金额'
                        },
                        tooltip : {
                            trigger: 'axis'
                        },
                        legend: {
                            data:['运营数据']
                        },
                        toolbox: {
                            show : false,
                            feature : {
                                mark : {show: true},
                                dataView : {show: true, readOnly: false},
                                magicType : {show: true, type: ['line', 'bar']},
                                restore : {show: true},
                                saveAsImage : {show: true}
                            }
                        },
                        calculable : true,
                        xAxis : [
                            {
                                type : 'category',
                                data : data.month
                            }
                        ],
                        yAxis : [
                            {
                                type : 'value'
                            }
                        ],
                        series : [
                            {
                                name:'交易额',
                                type:'bar',
                                data:data.money,
                                markPoint : {
                                    data : [
                                        {type : 'max', name: '最大值'},
                                        {type : 'min', name: '最小值'}
                                    ]
                                },
                                markLine : {
                                    data : [
                                        {type : 'average', name: '平均值'}
                                    ]
                                }
                            }
                            
                        ]
                    };
                    var myChart = echarts.init(document.getElementById('dataRecord'));
                    // 为echarts对象加载数据 
                    myChart.setOption(option); 
        })
        .fail(function() {
            console.log("error");
        });
    });
});