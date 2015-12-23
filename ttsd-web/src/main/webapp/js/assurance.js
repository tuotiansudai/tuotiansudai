require(['jquery','fullPage'], function ($) {

    $('#assuranceEffect').fullpage({
        sectionsColor: ['#d9ac52', '#50b281', '#9676d6'],
        'navigation': true,
        afterLoad: function(anchorLink, index){
            var $fpNav=$('#fp-nav');
            if(index==1) {
                $fpNav.find('li').each(function(key,option) {
                    var $this=$(this);
                   switch(key){
                       case 0:
                           $this.find('span').text('益');
                           break;
                       case 1:
                           $this.find('span').text('财');
                           break;
                       case 2:
                           $this.find('span').text('保');
                           break;
                   }

                });
            }
        }
    });

});