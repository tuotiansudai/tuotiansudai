/**
 * Created by zhaoshuai on 2015/6/26.
 */
require.config({
    baseUrl: "../js",//此处存放文件路径
    paths: {
        "jquery": "libs/jquery-1.10.1.min"//此处存放baseURL路径下要引入的JS文件
    }
});
require(['jquery'], function ($) {
    $(function(){
        var aBtn=$('.project_btn ul li');
        var aProject_type=$('.project_type ul');
        for( var i=0;i<aBtn.length;i++){
            aBtn[i].index=i;
            aBtn[i].onclick=function(){
                for(var i=0;i<aBtn.length;i++){
                    aBtn[i].className='';
                    aProject_type[i].style.display='none';
                }
                this.className='project_active';
                aProject_type[this.index].style.display='block';
            }
        }
    })
})