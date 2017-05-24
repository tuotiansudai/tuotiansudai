/* hot question for mobile */
var $hotCategory=$('.hot-question-category');
$hotCategory.find('.m-title').on('click',function() {
    $hotCategory.find('.qa-list').toggle();
});

/* home page for switch menu to show different page */
var $homeTagContainer = $('#homeTagContainer');
var locationUrl=window.location.href ;
var urlParams=globalFun.parseURL(locationUrl);

if($homeTagContainer.length) {

    var $switchMenu = $('.switch-menu li', $homeTagContainer),
        group;
    if(urlParams.params.group) {
        group = urlParams.params.group.toUpperCase();
    }else {
        group='';
    }
    switch (group) {
        case 'ALL':
            $switchMenu.eq(0).addClass('active');
            break;
        case 'UNRESOLVED':
            $switchMenu.eq(1).addClass('active');
            break;
        default:
            $switchMenu.eq(2).addClass('active');
            break;
    }
}

//搜索结果高亮显示
var $searchResultBox=$('#searchResultBox');
if($searchResultBox.length) {
    var $answersList=$('.answers-list',$searchResultBox);
    var keyword=urlParams.params.keyword; //关键字
    if(keyword) {
        $('#searchBoxTool .input-search').val(decodeURI(keyword));
    }

    $answersList.find('dt a').each(function(key,option) {
        var $this=$(this),
            title=$(option).html();
        var reg = new RegExp("(" + keyword + ")","gi");
        var optionWord=title.replace(reg, "<i style='color:red;'>$1</i>");

        $this.html(optionWord);

    });

}









