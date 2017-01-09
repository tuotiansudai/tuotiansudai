/**
 * [name]:spring festival activity
 * [author]:xuqiang
 * [date]:2017-01-09
 */
require(['jquery', 'layerWrapper', 'template','jquery.ajax.extension'], function ($,layer,tpl) {
    $(function() {
         layer.open({
          type: 1,
          move:false,
          area:['400px','300px'],
          title:false,
          content: $('#moneyTip')
        });       
    });        
});