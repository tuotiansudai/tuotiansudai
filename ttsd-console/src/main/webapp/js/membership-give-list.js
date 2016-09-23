require(['jquery', 'layerWrapper', 'template', 'csrf', 'bootstrap', 'bootstrapDatetimepicker', 'jquery-ui', 'bootstrapSelect'], function ($, layer) {

    var $membershipList = $('#membershipList');
    $membershipList.find('.give-membership').on('change', function (e) {
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


    $membershipList.find('.import-user-list').on('click', function (event) {
        var $this = $(this),
            thisID = $this.data('id');
        $.ajax({
            url: '/membership-manage/give/importUsersList/' + thisID,
            type: 'GET',
            dataType: 'json'
        }).done(function (data) {
            var dataGroup = [],
                len = data.length;
            dataGroup.push('<ul class="pop-user-list-box">');

            for (var i = 0; i < len; i++) {
                dataGroup.push('<li>' + data[i] + '</li>');
            }

            dataGroup.push('</ul>');

            layer.open({
                type: 1,
                title: '用户列表',
                area: ['560px', '390px'],
                shadeClose: false,
                content: dataGroup.join('')
            });


        })
    });

});