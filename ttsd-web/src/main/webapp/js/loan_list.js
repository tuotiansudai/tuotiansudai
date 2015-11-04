require(['jquery', 'csrf'], function ($) {
    var $loanLi=$('.loan-list').find('li');
    $loanLi.click(function() {
        var $this=$(this),
            thisUrl=$this.attr('urlLink');
        window.open(thisUrl); //new window tab
        //location.href=thisUrl;
    });

})
