<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="auto-app-push-list.js" headLab="membership-manage" sideLab="membershipQuery" title="会员等级查询">

<!-- content area begin -->
<div class="col-md-10">
    <div class="tip-container">
        <div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
            <span class="txt"></span>
        </div>
    </div>
    <div class="table-responsive">
        <table class="table table-bordered table-hover ">
            <thead>
            <tr>
                <th>
                    用户名
                </th>
                <th>
                    真实姓名
                </th>
                <th>
                    手机号
                </th>
                <th>
                    成长值
                </th>
                <th>
                    会员等级
                </th>
                <th>
                    获取方式
                </th>
                <th>
                    明细查询
                </th>
            </tr>
            </thead>
            <tbody>
                <tr>
                    <td>test</td>
                    <td>张珊珊</td>
                    <td>135292929</td>
                    <td>500000</td>
                    <td>V3</td>
                    <td>sss</td>
                    <td><a href="/membership-manage/membership-detail?loginName=gaoyinglong">查看明细</a></td>
                </tr>
            </tbody>
        </table>
    </div>
</@global.main>