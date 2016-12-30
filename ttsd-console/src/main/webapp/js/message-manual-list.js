require(['jquery', 'csrf', 'bootstrap', 'bootstrapSelect', 'bootstrapDatetimepicker', 'jquery-ui'], function ($) {
    $(function () {
        //渲染select表单
        $('.selectpicker').selectpicker();

        var confirmModal = $("#confirm-modal");

        //提交表单
        confirmModal.find('.btn-submit').click(function () {
            confirmModal.modal('hide');
            $.ajax({
                url: $(this).data('post-url'),
                type: 'POST',
                dataType: 'json'
            }).complete(function (data) {
                if (data.status) {
                    location.reload();
                } else {
                    showErrorMessage(data.data.message);
                }
            });
        });

        $('.approve-btn').on('click', function () {
            confirmModal.find(".btn-submit").data('post-url', "/message-manage/approve/" + $(this).attr('data-messageId'));
            confirmModal.find('.modal-body H5').html("确认审核？");
            confirmModal.modal('show');
            return false;
        });

        $('.reject-btn').on('click', function () {
            confirmModal.find(".btn-submit").data('post-url', "/message-manage/reject/" + $(this).attr('data-messageId'));
            confirmModal.find('.modal-body H5').html("确认驳回？");
            confirmModal.modal('show');
            return false;
        });

        $('.delete-btn').on('click', function () {
            confirmModal.find(".btn-submit").data('post-url', "/message-manage/delete/" + $(this).attr('data-messageId'));
            confirmModal.find('.modal-body H5').html("确认删除？");
            confirmModal.modal('show');
            return false;
        });

        var showErrorMessage = function (message) {
            var errorModal = $('#error-modal');
            errorModal.find('.modal-body H5').html(message);
            errorModal.modal('show');
        }
    });
});