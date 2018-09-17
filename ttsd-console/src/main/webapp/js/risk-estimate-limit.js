require(['jquery', 'csrf', 'jquery-ui', 'bootstrap'], function ($) {
    $('input[type="reset"]').click(function () {
        location.reload();
        return false;
    });

    //开始
    $('#edit-redict').click(function () {
        $('.showForm').hide();
        $('.editForm').show();
    });
    $('#btnCancel').click(function () {
        window.location="/user-manage/risk-estimate/limit";
    });
    $('#btnSave').click(function () {
        var conservative = $('input[name="conservative"]').val()
        var steady = $('input[name="steady"]').val()
        var positive = $('input[name="positive"]').val()
        if(conservative == '' || steady == '' || positive == ''){
            $('.web-error-message').show();
            $('.message').html('请输入数值');
            return false;
        }
        var reg = /^\d{1,12}$/;
        if (!reg.test(conservative) || !reg.test(steady) || !reg.test(positive)) {
            $('.web-error-message').show();
            $('.message').html('请输入适当的数字!');
            return false;
        }
        if(!(Number(conservative)<Number(steady) && Number(steady)<Number(positive))){
            $('.web-error-message').show();
            $('.message').html('输入的金额需满足:保守型<稳健型<积极型!');
            return false;
        }
        $.ajax({
            url: '/user-manage/risk-estimate/limit/edit?conservative='+conservative+'&steady='+steady+'&positive='+positive,
            type: 'GET',
            async:false,
            dataType: 'json',
            data: {}
        })
        .done(function(resData) {
           if(resData.status){
               window.location="/user-manage/risk-estimate/limit";
           }else{
               $('.web-error-message').show();
               $('.message').html(resData.message);
           }

        })
        .fail(function() {
            alert("系统错误!");
        });
    });
    $('.number-input').keyup(function(){
        $(this).val($(this).val().replace(/\D/g,""));
    });

});