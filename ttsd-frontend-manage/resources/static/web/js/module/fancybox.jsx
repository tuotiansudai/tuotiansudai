define([],function() {
    function fancybox(callback) {
        require.ensure(['webJs/plugins/jquery.fancybox.min'],function() {
            require('webStyle/plugins/fancybox.scss');
            require('webJs/plugins/jquery.fancybox.min');
            callback && callback();
        },'jquery_fancybox');
    }
    return fancybox;

})

