require(['jquery', 'underscore', 'jquery-ui', 'bootstrap', 'bootstrapSelect', 'csrf'], function ($, _) {
    $(function () {
        var mobileElement = $('#mobile');
        var checkallElement = $("#checkall");
        var approveButtonElement = $("form button.approve");
        var unapprovedQuestionElement = $('input[name="unapproved"]');
        var confirmModal = $('#confirm-modal');

        $('form select.selectpicker').selectpicker();

        $('#questionsReset').click(function () {
            location.href = "/ask-manage/questions";
        });

        $('#answersReset').click(function () {
            location.href = "/ask-manage/answers";
        });

        //自动完成提示
        mobileElement.autocomplete({
            source: function (query, process) {
                //var matchCount = this.options.items;//返回结果集最大数量
                $.get('/user-manage/mobile/' + query.term + '/search', function (respData) {
                    return process(respData);
                });
            }
        });

        checkallElement.click(function () {
            unapprovedQuestionElement.prop('checked', $(this).prop('checked'));
        });

        unapprovedQuestionElement.click(function () {
            checkallElement.prop("checked", $('input[name="unapproved"]:not(:checked)').length === 0);
        });

        approveButtonElement.click(function () {
            var checkedItems = _.map($('input[name="unapproved"]:checked'), function (value) {
                return parseInt($(value).val());

            });

            if (checkedItems.length > 0) {
                confirmModal.find(".confirm-title").html("审核" + checkedItems.length + "条记录");
                confirmModal.modal('show');
            }

            var formData = new FormData();
            formData.append("ids", checkedItems);
            confirmModal.find('button.approve').unbind("click").click(function () {
                confirmModal.modal('hide');
                var self = $(this);
                $.ajax({
                    url: self.data('url'),
                    type: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false
                }).done(function () {
                    window.location.reload();
                });
            });

            confirmModal.find('button.reject').unbind("click").click(function () {
                confirmModal.modal('hide');
                var self = $(this);
                $.ajax({
                    url: self.data('url'),
                    type: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false
                }).done(function () {
                    window.location.reload();
                });
            });
        });

        $('.down-load').click(function () {
            location.href = "/export/questions?" + $('form').serialize();
        });
    });
});
