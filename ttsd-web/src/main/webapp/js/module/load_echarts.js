define(['jquery','underscore','echarts'], function ($,_) {

    // 目前web项目中只用到了柱状图Bar和饼状图Pie

    // 为模块加载器配置echarts的路径，从当前页面链接到echarts.js，定义所需图表路径
    require.config({
        paths: {
            echarts: 'libs/echarts/dist'
        }
    });

    var MyChartsObject={
        //
        ChartConfig: function (container, option) {
            this.option = {
                chart: {},
                option: option,
                container: container
            };
            return this.option;
        },
        // 动态加载echarts然后在回调函数中开始使用，注意保持按需加载结构定义图表路径
        RenderChart: function (option) {
            require(
                [
                    'echarts',
                    'echarts/chart/bar',
                    'echarts/chart/line',
                    'echarts/chart/map'
                ],
                function (ec) {

                    var echarts = ec;
                    if (option.chart && option.chart.dispose) {
                        option.chart.dispose();
                    }
                    option.chart = echarts.init(option.container);
                    option.chart.setOption(option.option, true);
                    window.onresize = option.option.resize;
                });
        },
        optionCategory:{
            //一般共有的选项
            CommonOption: {
                tooltip: {
                    trigger: 'axis'
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
                }
            },
            // 饼状图选项
            PieOption:function(data,name) {
                var report_data = MyChartsObject.ChartDataFormate(data);
                var thisOption = {
                    legend:{
                        orient: 'vertical',
                        x: 'left',
                        y:'center',
                        data:report_data.category
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
                            radius : ['50%', '95%'],
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
                            data: report_data.data
                        }
                    ]
                };
                var PieOpt=$.extend({}, this.CommonOption, thisOption);
                return PieOpt;
            },
            BarOption:function(data) {
                var report_data = MyChartsObject.ChartDataFormate(data);
                var thisOption = {
                    backgroundColor:'#f7f7f7',
                    color:['#ff9c1b'],
                    title : {
                        text: report_data.title,
                        subtext: report_data.sub,
                        textStyle:{
                            color: '#ff9c1b'
                        }
                    },
                    tooltip : {
                        trigger: 'axis'
                    },
                    legend: {
                        data:[report_data.name],
                        selectedMode:false
                    },
                    calculable : false,
                    xAxis : [
                        {
                            type : 'category',
                            data : report_data.month
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
                            data:report_data.money,
                            tooltip : {
                                formatter: "时间:{b}<br/>交易额:{c}"
                            }
                        }

                    ]
                };
                var BarOpt=$.extend({}, this.CommonOption, thisOption);
                return BarOpt;
            }
        },
        ChartDataFormate:function(data) {
            var categories = [];
            var datas = [],
                dataLen=data.length;

            for (var i = 0; i < dataLen; i++) {
                categories.push(data[i].name || "");
                datas.push({ name: data[i].name, value: data[i].value || 0 });
            }
            return { category: categories, data: datas };
        }
    }

    return MyChartsObject;
});