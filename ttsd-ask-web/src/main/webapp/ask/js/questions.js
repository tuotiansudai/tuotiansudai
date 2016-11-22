var comm = require("./commonFun");

/* hot question for mobile */
var $hotCategory=$('.hot-question-category');
$hotCategory.find('.m-title').on('click',function() {
    $hotCategory.find('.qa-list').toggle();
});

/* home page for switch menu to show different page */
var $homeTagContainer = $('#homeTagContainer');
var locationUrl=window.location.href ;

var urlParams=comm.parseURL(locationUrl);
console.log(urlParams);
if($homeTagContainer.length) {

    var $switchMenu = $('.switch-menu li', $homeTagContainer),
        group;
    if(urlParams.params.group) {
        group = urlParams.params.group.toUpperCase();
    }else {
        group='';
    }
    switch (group) {
        case 'UNRESOLVED':
            $switchMenu.eq(1).addClass('active');
            break;
        case 'HOT':
            $switchMenu.eq(2).addClass('active');
            break;
        default:
            $switchMenu.eq(0).addClass('active');
            break;
    }
}

//全部搜索框
var $searchBoxTool = $('#searchBoxTool');
var $searchResultBox=$('#searchResultBox');
if ($searchBoxTool.length) {
    var $btnsearch = $('.btn-search', $searchBoxTool);
    $btnsearch.on('click',function(){
        var value=$.trim($(this).val());
        if(!value) {
            window.location.href='/?group=HOT&index=1';
        }
    })
}

//搜索结果高亮显示
if($searchResultBox.length) {
    var $answersList=$('.answers-list',$searchResultBox);
    var keyword=urlParams.params.keyword; //关键字
    $answersList.find('dt a').each(function(key,option) {
        var $this=$(this),
            title=$(option).html();
        var reg = new RegExp("(" + keyword + ")","gi");
        var optionWord=title.replace(reg, "<i style='color:red;'>$1</i>");

        $this.html(optionWord);

    });

}









