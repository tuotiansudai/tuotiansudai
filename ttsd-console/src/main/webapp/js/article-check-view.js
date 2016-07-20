require(['jquery', 'csrf'], function ($) {
    $(function () {

        $('#reject').on('click', function () {
            var formDate = $("#queryArticle").serialize();
            var comment = prompt("请输入驳回意见");
            var id = $('#content').attr('data-id');
            var url = '/announce-manage/article/' + id + '/reject';
            if (null != comment) {
                $.ajax({
                    url: url,
                    type: 'POST',
                    dataType: 'json',
                    data: {comment: comment}
                }).done(function () {
                    alert("已驳回");
                    window.location.href = '/announce-manage/article/list?' + formDate;
                });
            }
        });
        $('#checkPass').on('click', function () {
            checkPass();
        });


        function checkPass() {
            if (confirm("确认通过并发布!")) {
                var id = $('#content').attr('data-id');
                var formDate = $("#queryArticle").serialize();
                $.ajax({
                    url: '/announce-manage/article/' + id + '/checkPass',
                    type: 'POST',
                    dataType: 'json'
                }).done(function () {
                    alert("发布成功!");
                    window.location.href = '/announce-manage/article/list?' + formDate;
                });
            }
        }
    });
});