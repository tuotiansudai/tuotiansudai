<#import "macro/global-dev.ftl" as global>
<@global.main pageCss="${css.loanDetail}" pageJavascript="${js.loanDetail}">
    <div class="global-loan-detail">
        <div class="container-fluid loan-info">
            <div class="row">
                <div class="col-md-8 pr10">
                    <div class="container-block">
                        <div class="title-block clearfix">
                            <h2 class="fl title">房产抵押借款</h2>
                            <div class="fl orange extra-rate">投资加息+0.4%~0.5% <i class="fa fa-question-circle-o" aria-hidden="true"></i></div>
                            <span class="fr boilerplate">借款协议样本</span>
                        </div>
                        <div class="content">
                            <div class="row loan-number-detail">
                                <div class="col-md-4">
                                    <div class="title">预期年化收益</div>
                                    <div class="number">11<span>%</span></div>
                                </div>
                                <div class="col-md-4">
                                    <div class="title">项目期限</div>
                                    <div class="number">90<span>天</span></div>
                                </div>
                                <div class="col-md-4">
                                    <div class="title">项目金额</div>
                                    <div class="number">100<span>万元</span></div>
                                </div>
                            </div>
                            <div class="row loan-active-detail">
                                <div class="col-md-6">
                                    投资进度：
                                    <div class="progress-bar">
                                        <div class="progress-inner" style="width: 44.55%">
                                        </div>
                                    </div>
                                    <#-- 这里的百分比要和上面 .progress-inner的style里的百分比一样 -->
                                    44.55%
                                </div>
                                <div class="col-md-6">
                                    可投金额：33,333,333元
                                </div>
                                <div class="col-md-6">
                                    募集截止时间：6天12小时58分(标满即放款)
                                </div>
                                <div class="col-md-6">
                                    还款方式：按月付息，到期还本
                                </div>
                            </div>
                        </div> <#-- .content end tag -->
                    </div> <#-- .container-block end tag -->
                </div>
                <div class="col-md-4">
                    <div class="container-block loan-operation">
                        <div class="title-block clearfix">
                            <div class="fr">
                                拓天速贷提醒您：投资非存款，投资需谨慎！
                            </div>
                        </div>
                        <div class="content">
                            <div class="pre">
                                <div class="time">
                                    <span>22:33:12</span> 后可投资
                                </div>
                                <a href="#" class="btn-invest btn-normal">预热中</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div> <#-- .row end tag -->
            <div class="row container-block mt10 main-content">
                <div class="title-block clearfix">
                    <div class="item fl">
                        借款详情
                    </div>
                    <div class="item fl">
                        投资记录
                    </div>
                </div>
                <div class="content">
                    网络技术老地方进度了
                </div>
            </div>
        </div> <#-- .loan-info end tag -->
    </div> <#-- .global-loan-detail end tag -->
</@global.main>
