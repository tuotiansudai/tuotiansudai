require(['jquery', 'csrf'], function ($) {
    $(function () {
        if ($('#content').attr('data-id') == '-1') {
            alert('文章不存在!');
        }
    });
});

function reject() {
    var comment = prompt("请输入驳回意见");
    var id = $('#content').attr('data-id');
    var url = '/announce-manage/article/' + id + '/reject/';

    if(null != comment)
    {
        $.ajax({
            url: url,
            type: 'POST',
            dataType: 'json',
            data: {comment: comment}
        }).done(function () {
            alert("已驳回");
            window.location.href="/announce-manage/article/list";
        });
    }
}

function checkPass() {
    if(confirm("确认通过并发布!")){
        var id = $('#content').attr('data-id');
        window.location.href = '/announce-manage/article/checkPass/' + id;
    }
}