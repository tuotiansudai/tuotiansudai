require('mWebStyle/investment/payment_calendar.scss');

require('mWebJsModule/calendar.scss');
require('mWebJsModule/calendar');

let $paymentCalendar = $('#paymentCalendar');
let $paymentTpl = $('#paymentTpl'),
    tplTemplate = $paymentTpl.html();


   let renderPayment = _.template(tplTemplate);


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

    },
    onClickYear:function(date) {
        location.href='payment-annual.ftl?year='+date[0];
    }
});

function updatePayment(year, month) {
    //通过接口 获取 当月 或者当日的 回款情况

    var dataTest  = {
        fund:"840,031.28 ",
        year:'2009',
        month:'9',
        day:'9',
        finish:'840,031.28',
        total:'12,300.00',
        count:1,
        list:[{
            name:'车辆抵押借款',
            amount:'2323',
            status:'待回款1'
        }]
    }

    var html = renderPayment(dataTest);

    $('.payment-section-info',$paymentCalendar).html(html);

}

updatePayment();

