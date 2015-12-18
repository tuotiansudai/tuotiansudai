define(['jquery','underscore','echarts'], function ($,_) {

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
            FormateNOGroupData: function (data) {
                var categories = [];
                var datas = [];
                for (var i = 0; i < data.length; i++) {
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
                        mark: true,
                        dataView: { readOnly: false },
                        restore: true,
                        saveAsImage: true
                    }
                }
            },
            CommonLineOption: {

                tooltip: {
                    trigger: 'axis',
                    formatter: function(option) {
                        var len=option.length,i= 0,stringObj=[],
                            datetime=option[0].name,dateRange,totalMonArr=[],totalMoney=0;
                        Array.prototype.sum = function(){
                            var sum = 0;
                            for(var i = 0;i<this.length;i++)
                            {
                                sum += parseFloat(this[i]);
                            }
                            return sum;
                        }

                        if(/W/gi.test(datetime)) {
                            var dateFullYear=datetime.split('-W')[0],
                                dateWeek=datetime.split('-W')[1];
                            dateRange=MyChartsObject.datetimeFun.getDateRange(dateFullYear,dateWeek);
                            stringObj.push(dateRange);
                        }
                        else {
                            stringObj.push(datetime);
                        }

                        for(i;i<len;i++) {
                            stringObj.push('<br/>'+option[i].seriesName+':'+option[i].value);
                            totalMonArr.push(option[i].value);
                        }
                        totalMoney=totalMonArr.sum().toFixed(2);
                        stringObj.push('<br/>总计:'+totalMoney);

                        return stringObj.join('');
                    }
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
            Bar: function (data, name) {
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
            Lines: function (data, name, is_stack) {
                var xAxisdata,legendData,seriesData,seriesDataList=[];
                xAxisdata=_.sortBy(_.uniq(_.pluck(data, 'name')));
                legendData=_.uniq(_.pluck(data, 'group'));

                $.each(legendData,function(key,option) {
                    var groupData=_.where(data, {group: option}),
                        groupListKey=_.pluck(groupData,'name'),
                        hackList=[],groupDataOrder;
                    $.each(xAxisdata,function(key,dateOpt) {
                        if(!_.contains(groupListKey, dateOpt)) {
                            hackList.push({group: option,name: dateOpt,value: "0"});
                        }
                    });

                    groupData=_.union(groupData,hackList);
                    groupDataOrder=_.sortBy(groupData,'name');//sort by time

                    var opData=_.pluck(groupDataOrder,'value');
                    seriesDataList.push({
                        name:option,
                        type:'line',
                        stack: '总量',
                        itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        data:opData
                    });
                });

                var option = {

                    legend: {
                        x:'center',
                        y:'bottom',
                        data: legendData
                    },
                    xAxis: [{
                        type: 'category',
                        data: xAxisdata,
                        boundaryGap: false
                    }],
                    yAxis: [{
                        name: name || '',
                        type: 'value',
                        splitArea: { show: true }
                    }],
                    series: seriesDataList
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
        },
        ChartsProvince:function(callback){
            $.ajax({
                type: 'GET',
                url: '/bi/province',
                dataType: 'json'
            }).done(function (data) {
                callback && callback(data);

            });
        },
        datetimeFun: {
            /* 获取当前指定的前几天的日期,n=0为当前日期 */
            getBeforeDate:function(n) {
                var n = n;
                var d = new Date();
                var year = d.getFullYear();
                var mon=d.getMonth()+1;
                var day=d.getDate();
                if(day <= n){
                    if(mon>1) {
                        mon=mon-1;
                    }
                    else {
                        year = year-1;
                        mon = 12;
                    }
                }
                d.setDate(d.getDate()-n);
                year = d.getFullYear();
                mon=d.getMonth()+1;
                day=d.getDate();
                s = year+"-"+(mon<10?('0'+mon):mon)+"-"+(day<10?('0'+day):day);
                return s;
            },
            /*格式化日期*/
            getNowFormatDate:function(theDate){
                 var day = theDate;
                 var Year = 0;
                 var Month = 0;
                 var Day = 0;
                 var CurrentDate = "";

                 Year= day.getFullYear();
                 Month= day.getMonth()+1;
                 Day = day.getDate();
                 CurrentDate += Year + "-";
                 if (Month >= 10 )
                 {
                     CurrentDate += Month + "-";
                 }
                 else
                 {
                     CurrentDate += "0" + Month + "-";
                 }
                 if (Day >= 10 )
                 {
                     CurrentDate += Day ;
                 }
                 else
                 {
                     CurrentDate += "0" + Day ;
                 }
                 return CurrentDate;
             },

            //这个方法将取得某年(year)第几周(weeks)的星期几(weekDay)的日期
            getXDate:function(year,weeks,weekDay) {
                var date = new Date(year,"0","1");
                var time = date.getTime();
                time+=(weeks-2)*7*24*3600000;
                date.setTime(time);
                return this.getNextDate(date,weekDay);
            },

            // 这个方法将取得 某日期(nowDate) 所在周的星期几(weekDay)的日期
            getNextDate:function(nowDate,weekDay){
                // 0是星期日,1是星期一,...
                weekDay%=7;
                var day = nowDate.getDay();
                var time = nowDate.getTime();
                var sub = weekDay-day;
                if(sub <= 0){
                    sub += 7;
                }
                time+=sub*24*3600000;
                nowDate.setTime(time);
                return nowDate;
            },
            // 获取日期范围显示
            getDateRange:function(_year,_week){
                var beginDate;
                var endDate;
                if(_year == null || _year == '' || _week == null || _week == ''){
                    return "";
                }
                beginDate = this.getXDate(_year,_week,1);
                endDate = this.getXDate(_year,_week-0+1,0);

                var duration=this.getNowFormatDate(beginDate) + " 至 "+ this.getNowFormatDate(endDate);
                return duration;
            }
        }
    };
    return MyChartsObject;
});



