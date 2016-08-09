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

/* create question */
var $createQuestion=$('#createQuestion');
if($createQuestion) {
    var $askQuestion=$('.askQuestion',$createQuestion);

    $.ajax({
        url:url,

    });
}
