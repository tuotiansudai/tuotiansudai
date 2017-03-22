require('webStyle/about_us.scss');

let $errorContainer =$('#errorContainer');

if($errorContainer.length) {

    var $jumpTip = $('.jump-tip i',$errorContainer);
    setTimeout(function(){
        window.location="/";
    },10000);

    setInterval(function(){
        $jumpTip.text()<1?window.location="/":$jumpTip.text(function(index,num){return parseInt(num)-1});
    },1000);
}

