require(['jquery','bootstrap', 'bootstrapDatetimepicker'], function($) {
    $(function() {
        var $body=$('body'),
            $confirmBtn=$('.confirm-btn'),//conirm button
            $tooltip = $('.add-tooltip'),
            $tipCom=$('.tip-container');

        $tooltip.length?$tooltip.tooltip():false;

        //confirm event
        $confirmBtn.on('click',function(e) {
            e.preventDefault();
            var $self=$(this),
                thisId=$self.attr('data-id');//data id

            $.ajax({
                url: '/path/to/file',
                type: 'POST',
                dataType: 'json',
                data: {id: thisId},
            })
            .done(function(data) {
                $self.text('已生效').addClass('already-btn');
            })
            .fail(function(data) {
                $self.addClass('confirm-btn').text('确认失败');
                $tipCom.show().find('.txt').text('请求发送失败，请刷新重试！');
            })
            .always(function(data) {
                $self.removeClass('confirm-btn').text('正在确认中');
            });
            
        });
        
    });
});