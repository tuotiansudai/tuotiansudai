require(['jquery'], function($) {
    $(function() {
        var $moreBtn=$('.get-more');
        $moreBtn.on('click', function(event) {
            event.preventDefault();
            var $self=$(this),
                $linkList=$('#linkList'),
                listLen=$linkList.height();

        });
    });
});