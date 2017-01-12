require(['jquery'], function($) {

    var $riskFlow=$('#riskFlow');
    if($riskFlow.length) {

        var $projectList=$('.project-info-list',$riskFlow);
        setInterval(function() {
            var num=$projectList.find('dl.active').data('num'),
                next;
            if(num<6) {
                 next=num+1;
            }
            else {
                next=1;
            }
            $projectList.find('dl[data-num="'+next+'"]').addClass('active').siblings('dl').removeClass('active');
        },3000);
    }

    var $bottomClose= $('.bottom-close'),
        $bottomOpen=$('.bottom-open');
    $('.to-close').on('click',function() {
        $bottomClose.show();
        $bottomOpen.hide();
    });
    $bottomClose.on('click',function() {
        $bottomClose.hide();
        $bottomOpen.show();
    });
});