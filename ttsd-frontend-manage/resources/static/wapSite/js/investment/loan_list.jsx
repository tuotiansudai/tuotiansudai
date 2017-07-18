require('wapSiteStyle/investment/loan_list.scss');

let menuClick = require('wapSiteJsModule/menuClick');

let $loanList = $('#loanList'),
    $targetCategoryBox = $('.target-category-box',$loanList);

menuClick({
    pageDom:$loanList
});

let myScroll = new IScroll('#wrapperOut', {
    probeType: 2,
    mouseWheel: true
});

$targetCategoryBox.on('click',function() {

    let $this = $(this),
        url = $this.data('url');

    location.href = url;
});



