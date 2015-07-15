/**
 * Created by zhaoshuai on 2015/7/8.
 */
$(function(){
    var aBtn=$('.newVersionBtn li a');
    var aDiv=$('.rightContent');

    for(var i=0;i<aBtn.length;i++){
        aBtn[i].index=i;
        aBtn[i].onclick=function(){
            for(var i=0;i<aBtn.length;i++){
                aBtn[i].className='';
                aDiv[i].style.display='none';
            }
            this.className='newVersion_active';
            aDiv[this.index].style.display='block';
        }
    }
});