define([],function() {
    function autoNumeric(callback) {
        require.ensure(['webJs/plugins/autoNumeric'],function() {
            require('webJs/plugins/autoNumeric');
            callback && callback();

        },'jquery_autoNumeric');
    }
    return autoNumeric;

})
