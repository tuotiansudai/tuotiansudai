let commonFun= require('publicJs/commonFun');

class uploadPic {

    constructor(input) {
        this.input = input;
    }

    init() {
        this.uploadPic();
    }

    uploadPic() {
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
        // $.ajax({
        //
        //     type: 'POST',
        //     url:'/loan-application/upload',
        //     data: data,
        //     processData: false,
        //     cache: false,
        //     contentType: false,
        // }).done(function () {
        //
        // })

        commonFun.useAjax({
            type:'POST',
            url:'/loan-application/upload',
            data: data,
            processData: false,
            cache: false,
            contentType: false
        },function(data) {
            if(data.success) {

            }
            else {

            }

        });
    }
}

module.exports = uploadPic;