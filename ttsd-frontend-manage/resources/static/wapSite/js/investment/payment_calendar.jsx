require('wapSiteStyle/investment/payment_calendar.scss');

require('wapSiteJsModule/calendar.scss');
require('wapSiteJsModule/calendar');


var data = [{
    date: '2016-10-31',
    value: 'ppp'
}];


let screenWid = document.body.clientWidth;
$('#calendarTree').calendar({
    // view: 'month',
    width: screenWid,
    height: 320,
    // startWeek: 0,
    // selectedRang: [new Date(), null],
    data: data,
    date: new Date(2017, 9, 5),
    onSelected: function (view, date, data) {

        console.log('view:' + view)
        console.log('date:' + date)
        console.log('data:' + (data || '无'));

        $(this).addClass('now').siblings('li').removeClass('now')
    },
    viewChange: function (view, y, m) {
        console.log(view, y, m)

        updatePayment(y,m);

    }
});

function updatePayment(year, month) {
    //通过接口 获取 当月 或者当日的 回款情况

}

