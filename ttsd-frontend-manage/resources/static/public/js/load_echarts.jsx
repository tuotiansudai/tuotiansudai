// 目前web项目中只用到了柱状图Bar和饼状图Pie

// 引入 ECharts 主模块
var echarts = require('echarts/lib/echarts');

// 引入柱状图
require('echarts/lib/chart/bar');

// 引入饼状图
require('echarts/lib/chart/pie');

// 引入提示框和标题组件
require('echarts/lib/component/tooltip');
require('echarts/lib/component/title');
require('echarts/lib/component/legend');
require('echarts/lib/component/axis');
require('echarts/lib/component/toolbox');

var MyChartsObject={
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
        if (option.chart && option.chart.dispose) {
            option.chart.dispose();
        }
        option.chart = echarts.init(document.getElementById(option.container));
        option.chart.setOption(option.option, true);
        window.onresize = option.option.resize;

    },
    // 各种图表类型的配置初始化
    ChartOptionTemplates:{

        ////通用的图表基本配置
        CommonOption: {
            tooltip: {
                trigger: 'axis'
                //tooltip触发方式:axis以X轴线触发,item以每一个数据项触发
            },
            toolbox: {
                show : false, //是否显示工具栏
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
        PieOption:function(data,option) {
            var report_data = MyChartsObject.ChartDataFormate(data);
            var option = option || {};
            var thisOption = {
                legend:{
                    orient: 'vertical',
                    x: 'left',
                    y:'center',
                    // left: 'left',
                    data:report_data.category
                },
                tooltip: {
                    trigger: 'item',
                    formatter: '{b} : {c} ({d}%)',
                    show: true
                },
                series: [
                    {
                        name: option.name || "",
                        type: 'pie',
                        // radius : ['50%', '80%'],
                        // center: ['70%', '48%'],
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
                ],
                color: ['rgb(255,117,42)','rgb(119,205,249)','rgb(227,109,213)','rgb(200,200,169)','rgb(131,175,155)']
            };
            var initOption=$.extend({}, this.CommonOption, thisOption);
            var PieOpt = $.extend({},initOption,option);
            return PieOpt;
        },
        // 饼状图选项
        PieOptionBaseInfo:function(data,option) {
            var report_data = MyChartsObject.myChartDataFormate(data,'name','scale');
            var option = option || {};
            var thisOption = {
                legend:{
                    orient: 'horizontal',
                    x: 'center',
                    y:'bottom',
                    data:report_data.category
                },
                tooltip: {
                    trigger: 'item',
                    formatter: '{b} : {c} ({d}%)',
                    show: true
                },
                series: [
                    {
                        name: option.name || "",
                        type: 'pie',
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
                ],
                color: ['#84a2ff','#ff6ecb','rgb(227,109,213)','rgb(200,200,169)','rgb(131,175,155)']
            };
            var initOption=$.extend({}, this.CommonOption, thisOption);
            var PieOpt = $.extend({},initOption,option);
            return PieOpt;
        },
        //环形图
        AnnularOption:function(data,option) {
            var report_data = MyChartsObject.myChartDataFormate(data,'name','scale');
            console.log(report_data)
            var option = option || {};
            var thisOption = {
                legend:{
                    orient: 'horizontal',
                    x: 'center',
                    y:'bottom',
                    // left: 'left',
                    data:report_data.category
                },
                tooltip: {
                    trigger: 'item',
                    formatter: '投资用户(人)<br/>{b} : {d}%',
                    show: true,
                    enterable:true
                },
                series: [
                    {
                        name: option.name || "",
                        type: 'pie',
                        radius : ['50%', '80%'],
                        center: ['50%', '50%'],
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
                ],
                color: ['#ff7e50', '#86cffa', '#da70d6', '#32cd32']
            };
            var initOption=$.extend({}, this.CommonOption, thisOption);
            var PieOpt = $.extend({},initOption,option);
            return PieOpt;
        },
        // 柱状图选项
        BarOption:function(data) {
            var thisOption = {
                backgroundColor:'#f7f7f7',
                color:['#ff9c1b'],
                title : {
                    text: data.title,
                    subtext: data.sub,
                    textStyle:{
                        color: '#ff9c1b'
                    }
                },
                tooltip : {
                    trigger: 'axis'
                },

                legend: {
                    data:[data.name],
                    selectedMode:false,
                    orient: 'vertical',
                    left: 'left'
                },
                calculable : false,
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
                        tooltip : {
                            formatter: "时间:{b}<br/>交易额:{c}"
                        }
                    }

                ]
            };

            var BarOpt=$.extend({}, this.CommonOption, thisOption);
            return BarOpt;
        },
        // 横向柱状图选项
        BarOptionXAxis:function(data) {
            var thisOption = {
                backgroundColor:'#f7f7f7',
                color:['#ff9c1b'],
                tooltip : {
                    trigger: 'axis'
                },
                calculable : false,
                xAxis : [
                    {
                        type : 'value'
                    }
                ],
                yAxis : [
                    {
                        type : 'category',
                        data : data.city,
                        axisLine: {show: false},
                        axisTick: {show: false},
                        axisLabel: {show: false}
                    }
                ],
                series : [
                    {
                        name:'交易额',
                        type:'bar',
                        data:data.scale,
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
    },
    myChartDataFormate:function(data,name,value) {
        var categories = [];
        var datas = [],
            dataLen=data.length;

        for (var i = 0; i < dataLen; i++) {
            categories.push(data[i].name || "");
            datas.push({ name: data[i][name], value: data[i][value] || 0 });
        }
        return { category: categories, data: datas };
    }
}

module.exports=MyChartsObject;

