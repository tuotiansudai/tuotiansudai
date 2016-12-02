define(['jquery','underscore','echarts'], function ($,_) {

    // 目前web项目中只用到了柱状图Bar和饼状图Pie

    var MyChartsObject={
        ChartConfig: function (container, option) {
            this.Colors = ['#910000', '#1aadce', '#492970', '#f28f43', '#77a1e5', '#c42525', '#a6c96a', '#6f2fd8', '#531750', '#2f7ed8', '#0d233a', '#8bbc21', '#d7c332', '#9a7400', '#5ace1a', '#910044', '#ffb81c', '#e5e65b', '#d12270', '#6ad0f0', '#3337e2', '#770808', '#df6237', '#07799e', '#f5b688', '#004b91', '#c340e3', '#4b9cad', '#cc4800', '#ff91c2', '#00913d', '#145207', '#2f5bfc', '#e34063', '#b794f1', '#4900c2', '#f09797', '#66892a', '#5d68f8', '#c577e5']; //默认配色
            require.config({
                paths:{
                    echarts: 'libs/echarts/dist'
                }
            });
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
        ChartDataFormate:{
            FormateNOGroupData: function (data,cate) {
                var categories = [];
                var datas = [],
                    dataLen=data.length;

                for (var i = 0; i < dataLen; i++) {
                    categories.push(data[i].name || "");
                    datas.push({ name: data[i].name, value: data[i].value || 0 });
                }
                return { category: categories, data: datas };

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
                        mark: {show: false},
                        dataView: {show: true, readOnly: false},
                        magicType: {show: true, type: ['line', 'bar']},
                        restore: {show: true},
                        saveAsImage: {show: true}
                    }
                }
            },
            CommonLineOption: {

                tooltip: {
                    trigger: 'axis',
                    formatter: function(option) {

                    }
                },
                toolbox: {
                    show : true,
                    feature: {
                        mark: {show: false},
                        dataView: {show: true, readOnly: false},
                        magicType: {show: true, type: ['line', 'bar']},
                        restore: {show: true},
                        saveAsImage: {show: true}
                    }
                }
            },
            Bar: function (data, name,xAxisName) {
                var bar_datas = MyChartsObject.ChartDataFormate.FormateNOGroupData(data, 'bar');
                var option = {
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

                return $.extend({}, MyChartsObject.ChartOptionTemplates.CommonLineOption, option);
            },
            Pie: function (data, name) {
                var pie_datas = MyChartsObject.ChartDataFormate.FormateNOGroupData(data,'pie');
                var total = 0;
                $.each(pie_datas.data,function (i,item){
                    total += Number(item.value);
                });
                var option = {
                    title:{
                        text: '总计:' + total,
                        x:'50',
                        y:'15'
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} 人 ({d}%)"
                    },
                    legend: {
                        orient : 'vertical',
                        x:'left',
                        y:'center',
                        data:pie_datas.category
                    },
                    calculable : true,
                    series : [
                        {
                            name:name,
                            type:'pie',
                            radius : ['40%', '60%'],
                            center: ['60%', '48%'],
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
                                        show : true,
                                        position : 'center',
                                        textStyle : {
                                            fontSize : '30',
                                            fontWeight : 'bold'
                                        }
                                    }
                                }
                            },
                            data:pie_datas.data
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
                        'echarts/chart/bar',
                        'echarts/chart/line',
                        'echarts/chart/k',
                        'echarts/chart/scatter'
                    ],
                    function (ec) {
                        var echarts = ec;
                        var ecConfig = require('echarts/config');
                        if (option.chart && option.chart.dispose)
                            option.chart.dispose();
                        option.chart = echarts.init(option.container);

                        option.chart.setOption(option.option, true);

                        window.onresize = option.option.resize;
                    });
            }
        }
    };
    return MyChartsObject;
});