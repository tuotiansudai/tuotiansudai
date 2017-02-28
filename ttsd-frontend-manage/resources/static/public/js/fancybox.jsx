define([],function() {
    function fancybox(callback) {
        require.ensure(['publicJs/plugins/fancybox/jquery.fancybox'],function() {
            require('publicJs/plugins/fancybox/jquery.fancybox.scss');
            require('publicJs/plugins/fancybox/jquery.fancybox');
            callback && callback();
        },'jquery_fancybox');
    }
    return fancybox;

})

