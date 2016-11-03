<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.anxin_sign}" pageJavascript="${js.anxin_sign}" activeNav="我的账户" activeLeftNav="安心签" title="安心签">
<div class="safety-signed-frame">
    <h2 class="column-title"><em>安心签</em></h2>
    <div class="safety-signed-list">
        <ul class="info-list" >
            <li>
                <span class="info-title"> CFCA安心签服务</span>
                <span class="binding-set">
                    <i class="fa fa-times-circle ok"></i>已开启
                </span>
            </li>
            <li>
                <span class="info-title">安心签免验服务</span>
                <span class="binding-set">
                    <i class="fa fa-check-circle ok"></i>已开启
                    <a class="setlink" href="javascript:void(0);">关闭</a>
                </span>
            </li>
          </ul>
        <p class="operation-info">
            1、用户点击“开启”并成功“保存”才视为开启自动投标设置成功。 <br/>
            2、投资金额：可设置，最小为1元，最高为100万元（取100的整数倍）。<br/>
            3、保留金额：不会加入自动投标的金额。<br/>
            4、本金复投：本金收回后将在次日零点继续自动投标，若想提现请提前设置保留金额或关闭自动投标。<br/>
            5、项目期限：选择您想投资项目的期限，可多选。<br/>
            6、自动投标成交金额：用户可用余额、设置的最大金额、项目剩余金额三者中的最小值（取100的整数倍）。<br/>
            7、用户开通自动投标功能即视为委托拓天速贷平台与达到用户指定标准的借款人签署借款合同，并承担该合同项下的一切权利及义务。<br/>
            8、自动投标会根据您的设置，筛选并自动为您投资，但我们不能保证对所有的项目投资成功。<br/>
            9、全部设置完成后，会在次日零点开启自动投标。<br/>
            10、自动投标开启后，若有多个项目可投，将尽可能优先新手专享进行投资。<br/>
            11、自动投标开启后，如果不想自动投标，请手动关闭。
        </p>
    </div>

</div>
</@global.main>