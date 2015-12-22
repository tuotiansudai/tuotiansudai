require(['jquery', 'bootstrap','Validform','Validform_Datatype','jquery-ui','csrf'], function ($) {
    $(function () {

        $('.jq-loginName').autocomplete({
            minLength: 3,
            source: function (query, process) {
                //var matchCount = this.options.items;//返回结果集最大数量
                $.get('/user-manage/staff/' + query.term + '/search', function (respData) {
                    return process(respData);
                });
            },
            change: function (event, ui) {
                if (!ui.item) {
                    this.value = '';
                }
            }
        });
    });
})