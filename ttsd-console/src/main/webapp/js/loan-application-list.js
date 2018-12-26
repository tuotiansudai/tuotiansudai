require(['jquery', 'jquery-ui', 'layer', 'layer-extend', 'layerWrapper', 'bootstrapSelect', 'csrf', 'bootstrapDatetimepicker'], function ($) {

    $('.selectpicker').selectpicker();
    $('#datepickerBegin,#datepickerEnd').datetimepicker({
        format: 'YYYY-MM-DD'
    });
    $("#datepickerBegin").on("dp.change", function (e) {
        $('#datepickerEnd').data("DateTimePicker").minDate(e.date);
    });
    $("#datepickerEnd").on("dp.change", function (e) {
        $('#datepickerBegin').data("DateTimePicker").maxDate(e.date);
    });

    $(".loanApplication-comment").click(function (e) {
        e.preventDefault();
        var $self = $(this),
            loanApplicationId = $self.attr('data-loanApplication-id'),
            comment = $self.parents('tr').find('.loanApplication-comment').text();
        //向模态框中传值
        $('#loanApplicationId').val(loanApplicationId);
        $('#comment').val(comment);
        $('#update').modal('show');
    });
});

function update() {
    //获取模态框数据
    var loanApplicationId = $('#loanApplicationId').val();
    var comment = $('#comment').val();

    if (comment.trim() == '') {
        alert('备注不能为空');
        return false;
    }

    var dataDto = {
        id: loanApplicationId,
        comment: comment
    };

    var dataForm = JSON.stringify(dataDto);

    $.ajax({
        type: "POST",
        url: "/loan-application/comment",
        data: dataForm,
        dataType: 'json',
        contentType: 'application/json; charset=UTF-8'
    }).done(function (data) {
        if (data.data.status) {
            location.href = "/loan-application/list";
        } else {
            alert(data.data.message);
        }
    }).fail(function () {
        alert("提交备注失败");
    });
    $('#update').modal('hide');
}