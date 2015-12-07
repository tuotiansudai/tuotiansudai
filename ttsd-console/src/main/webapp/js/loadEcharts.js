define(['jquery','echarts'], function ($) {

    var MyChartsObject={
        ChartConfig: function (container, option) {
            this.Colors = ['#910000', '#1aadce', '#492970', '#f28f43', '#77a1e5', '#c42525', '#a6c96a', '#6f2fd8', '#531750', '#2f7ed8', '#0d233a', '#8bbc21', '#d7c332', '#9a7400', '#5ace1a', '#910044', '#ffb81c', '#e5e65b', '#d12270', '#6ad0f0', '#3337e2', '#770808', '#df6237', '#07799e', '#f5b688', '#004b91', '#c340e3', '#4b9cad', '#cc4800', '#ff91c2', '#00913d', '#145207', '#2f5bfc', '#e34063', '#b794f1', '#4900c2', '#f09797', '#66892a', '#5d68f8', '#c577e5']; //默认配色
            // 路径配置
            require.config({
                paths:{
                    echarts: 'libs/echarts/dist'
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
                            var series_temp = { name: group[i], data: temp, type: chart_type, itemStyle: {normal: {areaStyle: {type: 'default'}}}  };
                            if (is_stack)
                                series_temp = $.extend({}, { stack: 'stack' }, series_temp);
                            break;
                        default:
                            var series_temp = { name: group[i], data: temp, type: chart_type};
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
                    show : true,
                    feature : {
                        mark : {show: false},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
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
            Line: function (data, name) {//data:数据格式：{name：xxx,value:xxx}...
                var line_datas = MyChartsObject.ChartDataFormate.FormateNOGroupData(data, 'line');
                var option = {
                    tootip: {
                        trigger: 'axis',
                        formatter: '{b}: {c}'
                    },
                    xAxis: [{
                        type: 'category', //X轴均为category，Y轴均为value
                        data: line_datas.category,
                        boundaryGap: false//数值轴两端的空白策略
                    }],
                    yAxis: [{
                        name: name || '',
                        type: 'value',
                        splitArea: { show: true }
                    }],
                    series: [{
                        type: 'line',
                        name: name || '',
                        axisLabel: { interval: 0 },
                        data: line_datas.data
                    }]
                };
                return $.extend({}, MyChartsObject.ChartOptionTemplates.CommonLineOption, option);
            },
            Lines: function (data, name, is_stack) { //data:数据格式：{name：xxx,group:xxx,value:xxx}...
                var stackline_datas = MyChartsObject.ChartDataFormate.FormateGroupData(data, 'line', is_stack);
                var option = {

                    legend: {
                        x: 'center',
                        y:'bottom',
                        data: stackline_datas.category
                    },
                    xAxis: [{
                        type: 'category', //X轴均为category，Y轴均为value
                        data: stackline_datas.xAxis,
                        boundaryGap: false//数值轴两端的空白策略
                    }],
                    yAxis: [{
                        name: name || '',
                        type: 'value',
                        splitArea: { show: true }
                    }],
                    series: stackline_datas.series
                };
                return $.extend({}, MyChartsObject.ChartOptionTemplates.CommonLineOption, option);
            },
            Bar: function (data, name) {//data:数据格式：{name：xxx,value:xxx}...
                var bar_datas = MyChartsObject.ChartDataFormate.FormateNOGroupData(data, 'bar');
                var option = {
                    tooltip: {
                        trigger: 'item',
                        formatter: "{b}:{c}"
                    },
                    xAxis: [{
                        type: 'category',
                        data: bar_datas.category,
                        axisLabel: {
                            show: true,
                            interval: 'auto',
                            rotate: 0, //旋转角度
                            margin: 8//距离X轴的距离
                        }
                    }],
                    yAxis: [{
                        name: name || '',
                        type: 'value',
                        nameLocation: 'end',
                        boundaryGap: [0, 0.01]
                    }],
                    series: [{
                        name: name || '',
                        axisLabel: { interval: 0 },
                        type: 'bar',
                        data: bar_datas.data
                    }]

                };
                return $.extend({}, MyChartsObject.ChartOptionTemplates.CommonLineOption, option);
            },
            Bars: function (data, name, is_stack,title) {//data:数据格式：{name：xxx,group:xxx,value:xxx}...
                var bars_dates = MyChartsObject.ChartDataFormate.FormateGroupData(data, 'bar', is_stack);
                var option = {
                    legend:{
                        data:bars_dates.category
                    },
                    title: {
                        textStyle:{
                            color: '#c9c29d',
                            fontSize: 18
                        },
                        text: title
                    },
                    xAxis: [{
                        type: 'category',
                        data: bars_dates.xAxis,
                        axisLabel: {
                            show: true,
                            interval: 'auto',
                            rotate: 0,
                            margion: 8
                        }
                    }],
                    yAxis: [{
                        type: 'value',
                        name: name || '',
                        splitArea: { show: true }
                    }],
                    series: bars_dates.series
                };
                return $.extend({}, MyChartsObject.ChartOptionTemplates.CommonLineOption, option);
            },
            BarLine: function (data, name1, name2) {//data:数据格式：{name：xxx,value:xxx}...
                var barline_dates = MyChartsObject.ChartDataFormate.FormateBarLineData(data, name1, name2);
                var option = {
                    legend: {
                        data: barline_dates.category
                    },
                    xAxis: [{
                        type: 'category',
                        data: barline_dates.xAxis,
                        trigger: 'axis'
                    }],
                    yAxis: [{
                        type: 'value',
                        name: name1 || '',
                        splitArea: { show: false }
                    },
                        {
                            type: 'value',
                            name: name2 || '',
                            axisLabel: {
                                boundaryGap: [0, 0.01],
                                formatter: "{value}%"
                            },
                            splitLine: { show: true }
                        }],
                    series: barline_dates.series
                };
                return $.extend({}, MyChartsObject.ChartOptionTemplates.CommonLineOption, option);
            }
        },

        Charts: {
            RenderChart: function (option) {
                require(
                    [
                        'echarts',
                        'echarts/chart/pie',
                        'echarts/chart/bar',
                        'echarts/chart/line',
                        'echarts/chart/scatter'
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

    return MyChartsObject;
});



