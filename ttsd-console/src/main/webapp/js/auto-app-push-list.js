require(['jquery','bootstrap', 'bootstrapDatetimepicker','csrf'], function($) {
    $(function() {
        $('.enabled-link').click(function(){
            if (!confirm("确认要启用吗?")) {
                return false;
            }
        });
        $('.disabled-link').click(function(){
            if (!confirm("确认要停用吗?")) {
                return false;
            }
        });
        $('.edit-content').on('click',function(event) {
            event.preventDefault();
            var $self=$(this),
                $parent=$self.parents('tr'),
                content=$parent.find('td:eq(4)').text();
            $('.content').val(content);
        });
        $('.change-content').on('click',function(event) {
            event.preventDefault();
            $.ajax({
                url: '/announce-manage/announce/'+operate,
                type: 'get',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8'
            }).done()
        });

    });
});