<#import "macro/global-dev.ftl" as global>
<@global.main pageCss="${css.loanDetail}" pageJavascript="${js.loanDetail}">
    <div class="global-loan-detail">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-8 pr10">
                    <div class="container-block loan-info">
                        <div class="title-block clearfix">
                            <h2 class="fl title">房产抵押借款</h2>
                            <div class="fl orange extra-rate" id="extra-rate">投资加息+0.4%~0.5% <i class="fa fa-question-circle-o" aria-hidden="true"></i></div>
                            <script>
                                var __extraRate = [
                                       {
                                           minInvestAmount: 100,
                                           maxInvestAmount: 999,
                                           rate: 0.3
                                       }, {
                                           minInvestAmount: 1000,
                                           maxInvestAmount: 9999,
                                           rate: 0.4
                                       }, {
                                           minInvestAmount: 10000,
                                           maxInvestAmount: 0,
                                           rate: 0.5
                                       }
                                   ];
                            </script>
                            <script type="text/template" id="extra-rate-popup-tpl">
                               <div class="extra-rate-popup" id="extra-rate-popup">
                                   <div class="header clearfix">
                                       <div class="td fl">投资金额</div>
                                       <div class="td fl">加息</div>
                                   </div>
                                   <% _.each(__extraRate, function(value){ %>
                                       <div class="clearfix">
                                           <div class="td fl"><%= value.minInvestAmount %>元 ≤ 投资额</div>
                                           <div class="td fl"><%= value.rate %>%</div>
                                       </div>
                                   <% }) %>
                               </div>
                            </script>
                            <span class="fr boilerplate">借款协议样本</span>
                        </div>
                        <div class="content">
                            <div class="row loan-number-detail">
                                <div class="col-md-4">
                                    <div class="title">预期年化收益</div>
                                    <div class="number red">11<i class="data-extra-rate"></i><span>%</span></div>
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
                                        <#-- data-loan-id -->
                                        <input id="invest-input" type="text" data-loan-id="2121" />
                                    </div>
                                </div>
                                <div class="invest-amount clearfix mt10">
                                    <div class="title fl">
                                        优 惠 券：
                                    </div>
                                    <div class="fr">
                                        <div class="dropdown" id="ttsd-dropdown">
                                            <div class="dropdown-main">
                                                <span class="text">斯蒂芬斯拉夫</span>
                                                <i class="fa fa-sort-desc" aria-hidden="true"></i>
                                            </div>
                                            <div class="dropdown-list">
                                                <div class="item no-ticket clearfix" data-text="不使用优惠券" data-id="no">
                                                    <div class="input fl">
                                                        <input type="radio" />
                                                    </div>
                                                    <label class="fl">不使用优惠券</label>
                                                </div>
                                                <div class="item clearfix active" data-text="新手体验券1000元" data-id="11">
                                                    <div class="input fl">
                                                        <input type="radio" />
                                                    </div>
                                                    <div class="type fl">
                                                        新手
                                                    </div>
                                                    <div class="fl text-inf">
                                                        <p class="text-t">新手体验券1000元</p>
                                                        <p class="text-b">[投资满1000元可用]</p>
                                                    </div>
                                                </div>
                                                <div class="item clearfix disabled" data-text="新手体验券1000元" data-id="11">
                                                    <div class="input fl">
                                                        <input type="radio" />
                                                    </div>
                                                    <div class="type fl">
                                                        新手
                                                    </div>
                                                    <div class="fl text-info">
                                                        <p class="text-t">新手体验券1000元</p>
                                                        <p class="text-b">[投资满1000元可用]</p>
                                                    </div>
                                                </div>
                                                <div class="item clearfix" data-text="新手体验券1000元" data-id="11">
                                                    <div class="input fl">
                                                        <input type="radio" />
                                                    </div>
                                                    <div class="type fl">
                                                        新手
                                                    </div>
                                                    <div class="fl text-info">
                                                        <p class="text-t">新手体验券1000元</p>
                                                        <p class="text-b">[投资满1000元可用]</p>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
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
                                <button id="invest-submit-btn" class="btn-invest btn-normal mt15" data-user-role="INVESTOR" data-no-password-remind="true" data-no-password-invest="false">马上投资</button>
                             </div>
                        </div>
                    </div>
                </div>
            </div> <#-- .row end tag -->
            <div class="row container-block mt10 main-content" data-tab>
                <div class="title-block clearfix">
                    <div class="item fl active">
                        借款详情
                    </div>
                    <div class="item fl">
                        投资记录
                    </div>
                </div>
                <div class="content detail">
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
                    <div class="apply-data">
                        <h5>1、身份证</h5>
                        <div class="scroll-wrap" scroll-carousel>
                            <div class="scroll-content">
                                <div class="row">
                                    <a class="col" href="http://placekitten.com/200/125" rel="example_group">
                                        <img class="img" src="http://placekitten.com/200/125" layer-src="http://placekitten.com/200/120" />
                                    </a>
                                    <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                        <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                    </a>
                                    <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                        <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                    </a>
                                    <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                        <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                    </a>
                                    <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                        <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                    </a>
                                    <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                        <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                    </a>
                                    <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                        <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                    </a>
                                    <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                        <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                    </a>
                                    <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                        <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                    </a>
                                </div>
                            </div>
                            <div class="left-button">
                            </div>
                            <div class="right-button">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="content record">
                    <div class="row title-list">
                        <div class="col-md-4">
                            <div class="br">
                                <div class="item">
                                    <h4>拓荒先锋 >></h4>
                                    <p>恭喜yyh***2016-05-21 10:49:21 拔得头筹奖励0.2％加息券＋50元红包</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="br">
                                <div class="item">
                                    <h4>拓天标王 >></h4>
                                    <p>恭喜yyh****以累计投资 60000.00元 夺得标王奖励0.2％加息券＋50元红包</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="br">
                                <div class="item">
                                    <h4>一锤定音 >></h4>
                                    <p>恭喜yyh***2016-05-21 10:49:21 终结此标奖励0.2％加息券＋50元红包</p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div id="invests-list-pagination" class="pagination" data-url="/loan/39963386690640/invests" data-page-size="10">
                    </div>
                </div>
            </div>
        </div> <#-- .loan-info end tag -->
    </div> <#-- .global-loan-detail end tag -->
</@global.main>
