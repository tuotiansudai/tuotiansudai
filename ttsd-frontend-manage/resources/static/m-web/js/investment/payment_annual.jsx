require('mWebStyle/investment/payment_annual.scss');

let parseURL = globalFun.parseURL(location.href),
    year = parseURL.params.year;


let $paymentAnnualTpl = $('#paymentAnnualTpl'),
    tplTemplate = $paymentAnnualTpl.html();

let render = _.template(tplTemplate);

let html = render();
$('#paymentAnnual').html(html);



//根据年份 调取接口并渲染页面
