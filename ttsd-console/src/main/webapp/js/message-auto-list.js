require(['jquery','bootstrap', 'bootstrapDatetimepicker','csrf'], function($) {
    $(function() {
        $('.enabled-link').click(function(){
            if (!confirm("确认要启用吗?")) {
                return false;
            }else{
                var $self = $(this),
                    thisLink = $self.attr('data-link');
                console.log(thisLink);
                $.ajax({
                    url: thisLink,
                    type: 'post',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8'
                }).done(function(response){
                    if(response.data.status){
                        location.href = "/message-manage/auto-message-list";
                    }
                });
            }
        });
        $('.disabled-link').click(function(){
            if (!confirm("确认要停用吗?")) {
                return false;
            }else{
                var $self = $(this),
                    thisLink = $self.attr('data-link');
                console.log(thisLink);
                $.ajax({
                    url: thisLink,
                    type: 'post',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8'
                }).done(function(response){
                    if(response.data.status){
                        location.href = "/message-manage/auto-message-list";
                    }
                });
            }
        });
    });
});