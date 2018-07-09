require('webStyle/about_assurance.scss');
let assureImgUrl = require('webImages/sign/assure/assure-bar.jpg');
let guideImgUrl = require('webImages/sign/assure/newer-bar.jpg');

let $guideBar = $('#guideBar'),
    $assureBar = $('#assureBar');

globalFun.$('#assureBar') && globalFun.$('#assureBar').setAttribute('src',assureImgUrl);


$guideBar.length && $guideBar.append('<img src="'+guideImgUrl+'" />');
let $riskFlow=$('#riskFlow');
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

