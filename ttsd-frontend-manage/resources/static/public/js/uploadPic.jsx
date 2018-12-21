let commonFun= require('publicJs/commonFun');

class uploadPic {

    constructor(input) {
        this.input = input;
    }

    init(callBack) {
        this.uploadPic(callBack);
    }

    uploadPic(callBack) {
        let AllImgExt = ".jpg|.jpeg|.gif|.bmp|.png|",
            file_input = this.input,
            type = file_input[0].files[0].type.split('/')[1],
            data = new FormData();


        if (AllImgExt.indexOf(type) == -1) {
            layer.msg('请上传正确的格式');
            file_input.val('');
            return;
        }

        data.append('upfile', file_input[0].files[0]);

        commonFun.useAjax({
            type:'POST',
            url:'/loan-application/upload',
            data: data,
            dataType: 'JSON',
            processData: false,
            cache: false,
            contentType: false
        },function(data) {
            if(data.state === 'SUCCESS') {
                callBack(data.url);
            }
        });
    }
}

module.exports = uploadPic;