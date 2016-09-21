require(['jquery', 'template', 'csrf', 'bootstrap', 'bootstrapDatetimepicker', 'jquery-ui', 'bootstrapSelect'], function ($) {
    $(function () {
        $('.give-membership').on('change', function (e) {
            e.preventDefault();
            var $self = $(this),
                id = $self.attr('data-id'),
                type = $self.attr('data-type'),
                checked = $self.prop('checked');

            var url = "";
            if (checked) {
                url = "/membership-manage/give/approve/" + id;
            } else {
                if ("IMPORT_USER" == type) {
                    alert("已生效的导入用户发放计划无法取消");
                    return;
                } else {
                    url = "/membership-manage/give/cancel/" + id;
                }
            }

            $.ajax({
                url: url,
                type: 'POST',
                dataType: 'json',
                data: {}
            }).done(function (data) {
                if (data.data.status) {
                    if (!checked) {
                        $self.parent('td').siblings('.edit-list').find('.edit-btn').addClass('active');
                    } else if ("IMPORT_USER" == type) {
                        $self.prop('disabled', true);
                        $self.parent('td').siblings('.edit-list').find('.edit-btn').removeClass('active');
                    } else {
                        $self.parent('td').siblings('.edit-list').find('.edit-btn').removeClass('active');
                    }
                } else {
                    $self.prop('checked', !checked);
                    alert(data.data.message);
                }
            }).fail(function () {
                console.log("error");
            });
        })
    });
});