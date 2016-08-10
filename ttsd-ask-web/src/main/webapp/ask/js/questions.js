var _ = require('underscore');
var comm = require("./commonFun");
console.log(comm);

/* home page for switch menu to show different page */
var $homeTagContainer=$('#homeTagContainer');
if($homeTagContainer.length) {
    var $switchMenu=$('.switch-menu li',$homeTagContainer),
     group=comm.pathNameKey('group').toUpperCase();
    switch(group) {
        case 'UNRESOLVED':
            $switchMenu.eq(1).addClass('active');
            break;
        case 'HOT':
            $switchMenu.eq(2).addClass('active');
            break;
        default:
            $switchMenu.eq(0).addClass('active');
            break;
    }
}




