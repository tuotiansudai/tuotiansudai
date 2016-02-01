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
                        location.href = "/app-push-manage/auto-app-push-list";
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
                        location.href = "/app-push-manage/auto-app-push-list";
                    }
                });
            }
        });
        $('.edit-content').on('click',function(event) {
            event.preventDefault();
            var $self=$(this),
                $parent=$self.parents('tr'),
                content=$parent.find('td:eq(4)').text();
                jPushAlertId=$parent.find('td:eq(0)').text()
            $('.content').val(content);
            $('.jPushAlertId').val(jPushAlertId);
        });
        $('.change-content').on('click',function(event) {
            event.preventDefault();
            var content = $('.content').val(),
                jPushAlertId =  $('.jPushAlertId').val();
            if(content == ''){
                $('.alertMessage').text('请输入推送模板').show();
                return false;
            }
            $.ajax({
                url: '/app-push-manage/auto-app-push/'+jPushAlertId+'/'+content,
                type: 'post',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8'
            }).done(function(response){
                if(response.data.status){
                    location.href = "/app-push-manage/auto-app-push-list";
                }
            });
        });

    });
});