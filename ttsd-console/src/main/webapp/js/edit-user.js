require(['jquery', 'csrf', 'jquery-ui'], function ($) {

    $('#referrer').autocomplete({
        minLength: 0,
        source: function (query, process) {
            //var matchCount = this.options.items;//返回结果集最大数量
            $.get('/user/' + query.term + '/search', function (respData) {
                return process(respData);
            });
        },
        change: function (event, ui) {
            if (!ui.item) {
                this.value = '';
            }
        }
    });

    $("input[type='reset']").click(function() {
        location.reload();
        return false;
    });
});