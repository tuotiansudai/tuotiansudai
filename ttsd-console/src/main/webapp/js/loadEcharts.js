define(['jquery','underscore','echarts','pageNumber'], function ($,_) {

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
                    feature: {
                        mark: {show: false},
                        dataView: {show: true, readOnly: false},
                        magicType: {show: true, type: ['line', 'bar']},
                        restore: {show: true},
                        saveAsImage: {show: true}
                    }
                }
            },
            Bar: function (data, name) {
                var bar_datas = MyChartsObject.ChartDataFormate.FormateNOGroupData(data, 'bar');
                var option = {
                    tooltip: {
                        trigger: 'item',
                        formatter: "投资次数{b}:{c}人"
                    },
                    xAxis: [{
                        type: 'category',
                        name:'投资次数',
                        data: bar_datas.category
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
            Pie: function (data, name) {
                var pie_datas = MyChartsObject.ChartDataFormate.FormateNOGroupData(data,'pie');
                var option = {
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
        eConsole: function (param) {
            var $formUserReport=$('#formUserInvestViscosityReport'),
                $boxUserInvest=$('#boxUserInvest');
            $.pageflip.defaultVal = {
                pageIndex: 1,
                pageSize: 10,
                PageCallback: loadReportData,
                PagePosition: "#boxUserInvest table .pageNumber span.pageBtn",
                pageLocalized: {
                    first: "<<",
                    prev: "<",
                    next: ">",
                    last: ">>"
                }
            }
            function loadReportData() {
                if (typeof param.seriesIndex == 'undefined') {
                    return;
                }
                var loanCount= /[0-9]/.exec(param.name)[0];
                var formData=$formUserReport.serialize()+'&loanCount='+loanCount+'&pageNo='+$.pageflip.defaultVal.pageIndex+'&pageSize='+$.pageflip.defaultVal.pageSize;
                $.ajax({
                    url: '/bi/user-invest-viscosity-detail',
                    type: 'GET',
                    dataType: 'json',
                    data:formData,
                    contentType: 'application/json; charset=UTF-8'
                }).done(function (data) {
                    var dataObj=[], TotalRecord=data.totalCount;

                    $boxUserInvest.find('.sumAmount').text(parseFloat(data.sumAmount/100).toFixed(2));
                    $.each(data.items,function(key,option) {
                        var isReferrerStaff=(option.isReferrerStaff==1)?'是':'否';
                        var getDate=new Date(option.lastInvestTime),
                            Hours=getDate.getHours(),
                            minutes=getDate.getMinutes(),
                            seconds=getDate.getSeconds();

                        Hours=(Hours >= 10)?(Hours + ":"):("0" + Hours + ":");
                        minutes=(minutes >= 10)?(minutes + ":"):("0" + minutes + ":");
                        seconds=(seconds >= 10)?seconds:("0" + seconds);
                            showDate=MyChartsObject.datetimeFun.getNowFormatDate(getDate)+' '+Hours+minutes+seconds;

                        dataObj.push('<tr> ' +
                            '<td>'+option.loginName+'</td> ' +
                            '<td>'+option.userName+'</td> ' +
                            '<td>'+option.mobile+'</td> ' +
                            '<td>'+isReferrerStaff+'</td> ' +
                            '<td>'+((_.isNull(option.referrer))?'':option.referrer)+'</td> ' +
                            '<td>'+((_.isNull(option.referrerUserName))?'':option.referrerUserName)+'</td> ' +
                            '<td>'+((option.isStaff==1)?'是':'否')+'</td> ' +
                            '<td>'+parseFloat(option.totalAmount/100).toFixed(2)+'</td> ' +
                            '<td>'+option.loanCount+'</td> ' +
                            '<td>'+showDate+'</td> ' +
                            '</tr>');
                    })
                    $boxUserInvest.show().find('tbody').empty().append(dataObj.join(''));

                    $.pageflip.paging(TotalRecord,{ PageCallback: loadReportData,
                        PagePosition: "#boxUserInvest table .pageNumber span.pageBtn"
                    });
                    $boxUserInvest.find('.TotalRecords').text(TotalRecord);
                });
            }
            if (param.type == 'click') {
                loadReportData();
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
                        var ecConfig = require('echarts/config');
                        if (option.chart && option.chart.dispose)
                            option.chart.dispose();
                        option.chart = echarts.init(option.container);

                        option.chart.setOption(option.option, true);
                        if(/userInvestViscosity/.test(option.chart.dom.id)) {
                            option.chart.on(ecConfig.EVENT.CLICK, MyChartsObject.eConsole); //添加点击事件
                        }
                        window.onresize = option.chart.resize;


                    });
            }
        },
        ChartsProvince:function(callback){
            $.ajax({
                type: 'GET',
                url: '/bi/province',
                dataType: 'json',
                async:false
            }).done(function (data) {
                callback && callback(data);

            });
        },
        ChartsChannels:function(callback){
            $.ajax({
                type: 'GET',
                url: '/bi/channels',
                dataType: 'json',
                async:false
            }).done(function(data) {
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



