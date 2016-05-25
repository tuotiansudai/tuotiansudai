var commonFun={};
Array.prototype.contains = function (obj) {
    var i = this.length;
    while (i--) {
        if (this[i] === obj) {
            return true;
        }
    }
    return false;
};
commonFun={
    /* init radio style */
    initRadio:function($radio,$radioLabel) {
        var numRadio=$radio.length;
        if(numRadio) {
            $radio.each(function(key,option) {
                var $this=$(this);
                if($this.is(':checked')) {
                    $this.next('label').addClass('checked');
                }
                $this.next('label').click(function() {
                    var $thisLab=$(this);
                    if(!/checked/.test(this.className)) {
                        $radioLabel.removeClass('checked');
                        $thisLab.addClass('checked');
                    }
                });
            });

        }
    },
    /* init radio style */
    checkBoxInit:function($checkbox,$label) {
        var numCheckbox=$checkbox.length;
        if(numCheckbox) {
            $checkbox.each(function(key,option) {
                var $this=$(this);
                if($this.is(':checked')) {
                    $this.next('label').addClass('checked');
                }
                $this.next('label').click(function() {
                    var $thisLab=$(this);
                    if(/checked/.test(this.className)) {
                        $thisLab.removeClass('checked');
                    }
                    else {
                        $thisLab.addClass('checked');
                    }
                });
            });

        }
    },

    browserRedirect: function () {
        var sUserAgent = navigator.userAgent.toLowerCase();
        var bIsIpad = sUserAgent.match(/ipad/i) == "ipad";
        var bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os";
        var bIsMidp = sUserAgent.match(/midp/i) == "midp";
        var bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";
        var bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";
        var bIsAndroid = sUserAgent.match(/android/i) == "android";
        var bIsCE = sUserAgent.match(/windows ce/i) == "windows ce";
        var bIsWM = sUserAgent.match(/windows mobile/i) == "windows mobile";

        if (bIsIpad || bIsIphoneOs || bIsMidp || bIsUc7 || bIsUc || bIsAndroid || bIsCE || bIsWM) {//如果是上述设备就会以手机域名打开
            return 'mobile';
        } else {
            //否则就是电脑域名打开
            return 'pc';
        }
    },
    loadCss:function(url) {
        var link = document.createElement("link");
        link.type = "text/css";
        link.rel = "stylesheet";
        link.href = url;
        document.getElementsByTagName("head")[0].appendChild(link);
    },
    parseURL:function(url) {
        var a =  document.createElement('a');
        a.href = url;
        return {
            source: url,
            protocol: a.protocol.replace(':',''),
            host: a.hostname,
            port: a.port,
            query: a.search,
            params: (function(){
                var ret = {},
                    seg = a.search.replace(/^\?/,'').split('&'),
                    len = seg.length, i = 0, s;
                for (;i<len;i++) {
                    if (!seg[i]) { continue; }
                    s = seg[i].split('=');
                    ret[s[0]] = s[1];
                }
                return ret;
            })(),
            file: (a.pathname.match(/\/([^\/?#]+)$/i) || [,''])[1],
            hash: a.hash.replace('#',''),
            path: a.pathname.replace(/^([^\/])/,'/$1'),
            relative: (a.href.match(/tps?:\/\/[^\/]+(.+)/) || [,''])[1],
            segments: a.pathname.replace(/^\//,'').split('/')
        };
    },
    popWindow:function(title,content,size) {
        if(!$('.popWindow').length) {
            var popW=[];
            popW.push('<div class="popWindow">');
            popW.push('<div class="ecope-overlay"></div>');
            popW.push('<div class="ecope-dialog">');
            popW.push('<div class="dg_wrapper">');

            popW.push('<div class="hd"><h3>'+title+' ' +
                '<em class="close" ></em></h3></div>');
            popW.push('<div class="bd">sss</div>');

            popW.push('</div></div></div>');
            $('body').append(popW.join(''));
            var $popWindow=$('.ecope-dialog'),
                size= $.extend({width:'560px'},size);
            $popWindow.css({
                width:size.width
            });
            var adjustPOS=function() {
                var scrollHeight=document.body.scrollTop || document.documentElement.scrollTop,
                    pTop=$(window).height()-$popWindow.height(),
                    pLeft=$(window).width()-$popWindow.width();
                $popWindow.css({'top':pTop/2+scrollHeight,left:pLeft/2});
                $popWindow.find('.bd').empty().append(content);
            }
            adjustPOS();
            $(window).resize(function() {
                adjustPOS();
            });
            var mousewheel = document.all?"mousewheel":"DOMMouseScroll";
            $(window).bind('mousewheel',function() {
                adjustPOS();
            })
        }
        else {
            $('.ecope-overlay,.popWindow').show();
        }

        $popWindow.delegate('.close','click',function() {
            $('.ecope-overlay,.popWindow').hide();
        })
    }

};

var MyChartsObject={
    ChartConfig: function (container, option) {
        this.Colors = ['#910000', '#1aadce', '#492970', '#f28f43', '#77a1e5', '#c42525', '#a6c96a', '#6f2fd8', '#531750', '#2f7ed8', '#0d233a', '#8bbc21', '#d7c332', '#9a7400', '#5ace1a', '#910044', '#ffb81c', '#e5e65b', '#d12270', '#6ad0f0', '#3337e2', '#770808', '#df6237', '#07799e', '#f5b688', '#004b91', '#c340e3', '#4b9cad', '#cc4800', '#ff91c2', '#00913d', '#145207', '#2f5bfc', '#e34063', '#b794f1', '#4900c2', '#f09797', '#66892a', '#5d68f8', '#c577e5']; //默认配色
        // 路径配置
        require.config({
            paths:{
                echarts: '/js/libs/echarts/dist'
            }
        });
        //配置主题
        var theme = 'defalut';
        require(['echarts/theme/macarons'], function(curTheme){
            theme = curTheme;
        });
        this.option = {
            chart: {},
            option: option,
            container: container
        };
        return this.option;
    },

    ChartDataFormate: {
        FormateNOGroupData: function (data) {
            var categories = [];
            var datas = [];
            for (var i = 0; i < data.length; i++) {
                categories.push(data[i].name || "");
                datas.push({ name: data[i].name, value: data[i].value || 0 });
            }
            return { category: categories, data: datas };
        },
        FormateBarGroupData: function (data) {
            return {title:data.title,sub:data.sub,name:data.name,month:data.month,money:data.money}
        },
        FormateGroupData: function (data, type, is_stack) {
            var chart_type = 'line';
            if (type)
                chart_type = type || 'line';
            var xAxis = [];
            var group = [];
            var series = [];
            for (var i = 0; i < data.length; i++) {
                for (var j = 0; j < xAxis.length && xAxis[j] != data[i].name; j++);
                if (j == xAxis.length)
                    xAxis.push(data[i].name);
                for (var k = 0; k < group.length && group[k] != data[i].group; k++);
                if (k == group.length)
                    group.push(data[i].group);
            }

            for (var i = 0; i < group.length; i++) {
                var temp = [];
                for (var j = 0; j < data.length; j++) {
                    if (group[i] == data[j].group) {
                        if (type == "map")
                            temp.push({ name: data[j].name, value: data[i].value });
                        else
                            temp.push(data[j].value);
                    }
                }
                switch (type) {
                    case 'bar':
                        var series_temp = { name: group[i], data: temp, type: chart_type };
                        if (is_stack)
                            series_temp = $.extend({}, { stack: 'stack' }, series_temp);
                        break;
                    case 'map':
                        var series_temp = {
                            name: group[i], type: chart_type, mapType: 'china', selectedMode: 'single',
                            itemStyle: {
                                normal: { label: { show: true} },
                                emphasis: { label: { show: true} }
                            },
                            data: temp
                        };
                        break;
                    case 'line':
                        var series_temp = { name: group[i], data: temp, type: chart_type };
                        if (is_stack)
                            series_temp = $.extend({}, { stack: 'stack' }, series_temp);
                        break;
                    default:
                        var series_temp = { name: group[i], data: temp, type: chart_type };
                }
                series.push(series_temp);
            }
            return { category: group, xAxis: xAxis, series: series };
        }
    },

    ChartOptionTemplates: {
        CommonOption: {
            tooltip: {
                trigger: 'axis'
            },
            toolbox: {
                show: true,
                feature: {
                    mark: true,
                    dataView: { readOnly: false },
                    restore: true,
                    saveAsImage: true
                }
            }
        },
        CommonLineOption: {

            tooltip: {
                trigger: 'axis'
            },
            toolbox: {
                show: true,
                feature: {
                    dataView: { readOnly: false },
                    restore: true,
                    saveAsImage: true,
                    magicType: ['line', 'bar']
                }
            }
        },
        Pie: function (data, name) {
            var pie_datas = MyChartsObject.ChartDataFormate.FormateNOGroupData(data);
            var option = {
                legend:{
                    orient: 'vertical',
                    x: 'left',
                    y:'center',
                    data:pie_datas.category
                },
                tooltip: {
                    trigger: 'item',
                    formatter: '{b} : {c} ({d}%)',
                    show: true
                },
                series: [
                    {
                        name: name || "",
                        type: 'pie',
                        //radius: '65%',
                        radius : ['50%', '95%'],
                        // center: ['30%', '48%'],
                        center: ['70%', '48%'],
                        itemStyle : {
                            normal : {
                                label : {
                                    show : false
                                },
                                labelLine : {
                                    show : false
                                }
                            },
                            emphasis : {
                                label : {
                                    show : false,
                                    position : 'center',
                                    textStyle : {
                                        fontSize : '11',
                                        fontWeight : 'normal'
                                    }
                                }
                            }
                        },
                        data: pie_datas.data
                    }
                ]
            };
            return $.extend({}, MyChartsObject.ChartOptionTemplates.CommonOption, option);
        },
        Bar: function (data, name) {
            var bar_datas = MyChartsObject.ChartDataFormate.FormateBarGroupData(data),
                option = {
                    backgroundColor:'#f7f7f7',
                    color:['#ff9c1b'],
                    title : {
                        text: bar_datas.title,
                        subtext: bar_datas.sub,
                        textStyle:{
                            color: '#ff9c1b'
                        }
                    },
                    tooltip : {
                        trigger: 'axis'
                    },
                    legend: {
                        data:[bar_datas.name],
                        selectedMode:false
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
                    calculable : false,
                    xAxis : [
                        {
                            type : 'category',
                            data : bar_datas.month
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
                            data:bar_datas.money,
                            tooltip : {
                                formatter: "时间:{b}<br/>交易额:{c}"
                            }
                        }

                    ]
                };
            return $.extend({}, MyChartsObject.ChartOptionTemplates.CommonOption, option);
        }
    },

    Charts: {
        RenderChart: function (option) {
            require(
                [
                    'echarts',
                    'echarts/chart/pie',
                    'echarts/chart/bar'
                ],
                function (ec) {
                    var echarts = ec;
                    if (option.chart && option.chart.dispose)
                        option.chart.dispose();
                    option.chart = echarts.init(option.container);
                    option.chart.setOption(option.option, true);
                    window.onresize = option.chart.resize;
                });
        }
    }
};