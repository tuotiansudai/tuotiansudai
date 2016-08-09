var _ = require('underscore');
var comm={};
comm.pathNameKey=function(key) {
    var parm = location.search.split('?')[1], parmObj;
    if (_.isUndefined(parm)) {
        return '';
    }
    else {
        parmObj = parm.split('&');
        for (var i = 0, len = parmObj.length; i < len; i++) {
            if (parmObj[i].split('=')[0] == key) {
                return parmObj[i].split('=')[1];
            }
        }
    }
}

var $homeTagContainer=$('#homeTagContainer');
if($homeTagContainer.length) {
    debugger
    var $switchMenu=$('.switch-menu li',$homeTagContainer),
     group=comm.pathNameKey('group').toUpperCase();

    if(_.isEmpty(group)) {
        $switchMenu.eq(0).addClass('active');
    }
    else if(/UNRESOLVED/gi.test(group)) {
        $switchMenu.eq(1).addClass('active');
    }
    else if(/HOT/.test(group)) {
        $switchMenu.eq(2).addClass('active');
    }
}
