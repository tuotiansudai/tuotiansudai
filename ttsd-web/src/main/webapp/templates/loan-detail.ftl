<#import "macro/global-dev.ftl" as global>
<@global.main pageCss="${css.loanDetail}" pageJavascript="${js.loanDetail}">
    <div class="global-loan-detail">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-8 pr10">
                    <div class="container-block loan-info">
                        <div class="title-block clearfix">
                            <h2 class="fl title">房产抵押借款</h2>
                            <div class="fl orange extra-rate">投资加息+0.4%~0.5% <i class="fa fa-question-circle-o" aria-hidden="true"></i></div>
                            <span class="fr boilerplate">借款协议样本</span>
                        </div>
                        <div class="content">
                            <div class="row loan-number-detail">
                                <div class="col-md-4">
                                    <div class="title">预期年化收益</div>
                                    <div class="number red">11<span>%</span></div>
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
                                    <span class="title">投资进度：</span>
                                    <div class="progress-bar">
                                        <div class="progress-inner" style="width: 44.55%">
                                        </div>
                                    </div>
                                    <#-- 这里的百分比要和上面 .progress-inner的style里的百分比一样 -->
                                    <span class="orange2">44.55%</span>
                                </div>
                                <div class="col-md-6">
                                    <span class="title">可投金额：</span>
                                    33,333,333元
                                </div>
                                <div class="col-md-6">
                                    <span class="title">募集截止时间：</span>
                                    6天12小时58分(标满即放款)
                                </div>
                                <div class="col-md-6">
                                    <span class="title">还款方式：</span>
                                    按月付息，到期还本
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
                            <#-- 预热状态  -->
                            <#--
                            <div class="pre">
                                <div class="time">
                                    <span>22:33:12</span> 后可投资
                                </div>
                                <a href="#" class="btn-invest btn-normal">预热中</a>
                             </div>
                              -->

                             <#-- 等待审核  -->
                             <#--
                             <div class="waiting">
                                 <img src="${staticServer}/images/sign/loan/recheck.png" alt="" />
                                 <a href="#" class="btn-invest btn-normal">查看其它项目</a>
                             </div>
                             -->

                             <div class="usable">
                                <div class="account-balance clearfix">
                                    <div class="title fl">
                                        账户余额：
                                    </div>
                                    <div class="fr">
                                        0.00元
                                        <a href="#">去充值>></a>
                                    </div>
                                </div>
                                <div class="invest-amount clearfix mt10">
                                    <div class="title fl">
                                        投资金额：
                                    </div>
                                    <div class="fr">
                                        <input type="text" />
                                    </div>
                                </div>
                                <div class="invest-amount clearfix mt10">
                                    <div class="title fl">
                                        优 惠 券：
                                    </div>
                                    <div class="fr">
                                        <input type="text" />
                                    </div>
                                </div>
                                <div class="expected-interest mt10 clearfix">
                                    <div class="fr w188">
                                        <span>预计总收益：</span>
                                        <span class="principal-income">0.00</span>
                                        <span class="experience-income"></span>
                                        元
                                    </div>
                                </div>
                                <a href="#" class="btn-invest btn-normal mt15">马上投资</a>
                             </div>
                        </div>
                    </div>
                </div>
            </div> <#-- .row end tag -->
            <div class="row container-block mt10 main-content">
                <div class="title-block clearfix">
                    <div class="item fl active">
                        借款详情
                    </div>
                    <div class="item fl">
                        投资记录
                    </div>
                </div>
                <div class="content">
                    <div class="subtitle">
                        <h3>借款基本信息</h3>
                    </div>
                    <div class="container-fluid list-block">
                        <div class="row">
                            <div class="col-md-4">借款人：刘某某</div>
                            <div class="col-md-4">平台ID：1212</div>
                            <div class="col-md-4">性别：男</div>
                            <div class="col-md-4">年龄：22</div>
                            <div class="col-md-4">婚姻状况：已婚</div>
                            <div class="col-md-4">身份证号：212121212121212</div>
                            <div class="col-md-4">申请地区：北京</div>
                            <div class="col-md-4">收入水平：5万元</div>
                        </div>
                    </div>
                    <div class="subtitle">
                        <h3>抵押档案</h3>
                    </div>
                    <div class="container-fluid list-block">
                        <div class="row">
                            <div class="col-md-4">借款人：刘某某</div>
                            <div class="col-md-4">平台ID：1212</div>
                            <div class="col-md-4">性别：男</div>
                            <div class="col-md-4">年龄：22</div>
                            <div class="col-md-4">婚姻状况：已婚</div>
                            <div class="col-md-4">身份证号：212121212121212</div>
                            <div class="col-md-4">申请地区：北京</div>
                            <div class="col-md-4">收入水平：5万元</div>
                        </div>
                    </div>
                    <div class="subtitle">
                        <h3>风控审核</h3>
                    </div>
                    <div class="container-fluid danger-control">
                        <div class="row">
                            <div class="col-md-6">
                                <div class="container-fluid table">
                                    <div class="row">
                                        <div class="col-xs-6 bg">1212</div>
                                        <div class="col-xs-6 br-r"><i class="fa fa-check-circle-o" aria-hidden="true"></i>1212</div>
                                        <div class="col-xs-6 bg">1212</div>
                                        <div class="col-xs-6 br-r"><i class="fa fa-check-circle-o" aria-hidden="true"></i>1212</div>
                                        <div class="col-xs-6 br-b bg">1212</div>
                                        <div class="col-xs-6 br-r br-b"><i class="fa fa-check-circle-o" aria-hidden="true"></i>1212</div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="container-fluid table">
                                    <div class="row">
                                        <div class="col-xs-6 bg">1212</div>
                                        <div class="col-xs-6 br-r"><i class="fa fa-check-circle-o" aria-hidden="true"></i>1212</div>
                                        <div class="col-xs-6 bg">1212</div>
                                        <div class="col-xs-6 br-r"><i class="fa fa-check-circle-o" aria-hidden="true"></i>1212</div>
                                        <div class="col-xs-6 br-b bg">1212</div>
                                        <div class="col-xs-6 br-r br-b"><i class="fa fa-check-circle-o" aria-hidden="true"></i>1212</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div> <#-- .danger-control end tag -->
                    <div class="subtitle">
                        <h3>申请资料</h3>
                    </div>
                </div>
            </div>
        </div> <#-- .loan-info end tag -->
    </div> <#-- .global-loan-detail end tag -->
</@global.main>
