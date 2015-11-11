/**
 * Created by CBJ on 2015/11/11.
 */
require(['jquery'], function ($) {
    $(function () {
        $.ajax({
            url: 'announce/list',
            type: 'POST',
            dataType: 'json',
        }).done(function(amount){
            console.log('ppp');
        });
    });
});