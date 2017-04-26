// 可以export一数组，或者直接一对象
module.exports = [{
    path: '/mock',
    method: 'post',
    status: 200,
    response: function(req, res) {
        return {
            "code": "200",
            "msg": "操作成功"
        }
    }
}]
