require('webStyle/help_center.scss');
require('webJsModule/touch_menu');
let commonFun= require('publicJs/commonFun');

let helpCenterImgBarUrl = require('webImages/helpcenter/help-center.png');
let $helpCenter = $("#helpCenter");
let tpl = require('art-template/dist/template');
var sourceKind = globalFun.parseURL(location.href);

//左侧菜单栏
let $leftBox = $('#helpLeftMenuBox');
let $titleBox = $leftBox.find('.swiper-slide').find('>a');
let $childBox = $leftBox.find('.swiper-slide').find('ul');

var helperCenterData = {
    registerBar:{
        register:[
            {
                title:"如何成为拓天速贷的注册用户？",
                answer:"（1）进入拓天速贷官网首页，点击右上角“注册”；<br/>（2）根据提示信息，填写您的手机号、密码、验证码等信息，点击“立即注册”。"
            },
            {
                title:"如何在拓天速贷设置密码更安全？",
                answer:"使用较长、字母+数字组合的设置来提高密码的强度，不要用生日、电话号码、名字缩写等容易被别人猜中的信息作为密码；且不要告知他人您的密码。"
            },
            {
                title:"注册时，为什么收不到短信验证码？",
                answer:"注册时，手机收不到短信验证码，请检查以下设置：<br/>①手机号是否输入错误；<br/>②手机信号是否畅通，手机是否欠费停机；<br/>③手机内是否安装短信拦截软件，是否举报投诉过拓天速贷类似的短信（原因：举报投诉过会被列入黑名单，需要重新申请解封）；<br/>④受短信通道及运营商的政策监管影响。目前同一手机号10分钟之内 （联通及电信只能接收到3条平台发出的短信，移动为10条；需查看自己手机是否超出该限制，超出请10分钟后再试。）<br/>如果还是收不到验证码，请致电拓天速贷客服：400-169-1188。"
            },
            {
                title:"注册时，推荐人这一栏该怎么填？",
                answer:"推荐朋友投资时，朋友投资金额的年化1%现金将作为奖励给予推荐人。推荐人这一栏可作为奖励发放的根据之一。此项为选填项，可跳过不填，您也可直接填写推荐人手机号。"
            },
            {
                title:"注册时，推荐人忘记填写怎么办？",
                answer:"不能补填（温馨提示：注册步骤请细心填写）。"
            }
        ],
        account:[
            {
                title:"为什么要进行实名认证？",
                answer:"拓天速贷为保障用户账户资金安全，以及保证投资借款合同的有效性与合法性，需用户进行实名认证。实名认证环节简单快捷，且拓天速贷会严格保密用户信息。"
            },
            {
                title:"如何进行实名认证？",
                answer:"①PC端：登录官方网站—我的账户—个人资料—实名认证；<br/>②手机端：登录官方APP—个人中心—账户名右侧箭头—实名认证。"
            },
            {
                title:"可以更改绑定的实名认证吗？",
                answer:"实名认证一经绑定后不可更改。"
            },
            {
                title:"实名认证时，身份证被占用，怎么办？",
                answer:"如身份证为您本人所有，可以联系我们的在线客服，或者拨打客服电话：400-169-1188，联系客服，向我们提供您手举身份证的半身照，或者发送到拓天速贷客服邮箱：kefu@tuotiansudai.com。（温馨提示：照片需清晰，保证能看到照片中身份证的相关信息）。"
            },
            {
                title:"身份证被盗用怎么办？",
                answer:"需本人挂失，提供丢失身份证证明，发送到拓天速贷客服邮箱：kefu@tuotiansudai.com或者联系拓天速贷客服中心（客服热线400-169-1188）申请冻结账户资金。"
            },
            {
                title:"认证提示失败怎么办？",
                answer:"①检查输入有无错误，不能包含空格、特殊符号（，。等）；<br/>②目前仅支持大陆地区身份认证；<br/>③未满18岁不支持认证；<br/>④检查无误仍提示错误，请联系客服热线400-169-1188获取帮助。"
            },
            {
                title:"账号如何绑定到富滇银行？",
                answer:"当您进行实名认证时，系统会将您的帐号绑定到富滇银行，资金由富滇银行全面存管。"
            }
        ]
    },
    accountBar:{
        password:[
            {
                title:"登录时，忘记账户密码了，怎么找回？",
                answer:"您可以直接在登录界面，点击“忘记密码”，根据提示直接找回您的密码。"
            },
            {
                title:"如何修改登录密码？",
                answer:"点击“我的账户”，进入“个人资料”，如要修改登录密码，可点击“登录密码”后的“修改”，输入原密码与新密码，点击“确认修改”，修改密码完成。"
            },
            {
                title:"我的账户被锁了，怎么办？",
                answer:"连续输入错误密码3次，系统将会将你的账户进行锁定；账户被锁定0.5小时后系统会自动解锁。你也可以与在线客服进行联系，或者拨打客服热线400-169-1188。"
            },
            {
                title:"支付密码是什么?",
                answer:"当您注册时，资金存管方（富滇银行）会以短信的形式将支付密码发送到您的注册手机号上，投资标的时需要验证后方可支付成功。"
            },
            {
                title:"支付密码怎么修改？",
                answer:"方式一：登录官方网站—我的账户—个人资料—支付密码—重置，输入身份证号，进行修改。<br/>方式二：通过发送短信修改，具体操作步骤请参照“我想通过短信修改或重设支付密码，怎么操作”。"
            },
            {
                title:"我想通过短信修改或重设支付密码，怎么操作？",
                answer:"富滇银行为您的资金提供存管服务，如果您需要更改或重设富滇银行的支付密码，可直接在富滇银行支付页面修改。"
            },
            {
                title:"免密投资怎么设置？",
                answer:"登录官方网站—我的账户—个人资料—免密投资—开启，进行设置。"
            },
            {
                title:"开启免密投资有什么作用？",
                answer:"开启免密投资后，投资时不再需要繁琐的过程即可成功，抢标快人一步。开启免密投资是免费的，且您的资金不会有任何安全隐患。"
            },
            {
                title:"更换绑定的银行卡一般需要多久？",
                answer:"更换银行卡需要先解绑旧银行卡，然后再绑定新银行卡，更换绑定的银行卡需要1-3个工作日。需要注意的是，解绑的要求是余额为零，要没有任何的资金交易才能解绑成功。"
            }
        ],
        vip:[
            {
                title:"VIP会员分为哪几个等级？",
                answer:"拓天会员是为给处于不同成长阶段的投资用户提供差异化的专享特权而设置的。会员目前总计分为六个等级，依次为：V0、V1、V2、V3、V4、V5。会员等级越高，享有的特权越多。"
            },
            {
                title:"VIP会员享有哪些特权？",
                answer:"多重保障：银行存管、抵押模式、严格风控体系、平台信息透明、数据信息安全等保障；<br/>服务费折扣：平台向V0、V1会员收取利息的10%作为服务费，V2收取9%，V3、V4收取8%，V5仅收取7%；<br/>贵宾专线：贵宾级客服服务，投资问题，意见建议专享直达；<br/>专享投资顾问：发标时间，平台活动，投资顾问第一时间通知到您；<br/>生日福利：V5专享，平台将会在会员生日时送上神秘礼包；<br/>注：会员权益会不断提升，敬请期待。"
            },
            {
                title:"如何提高会员等级？",
                answer:"方式一：投资，投资额越高，所获得的成长值（按照投资金额年化1：1转化为成长值，不足1元的舍去不计）越高，每达到规定的成长值，会员等级会提高；<br/>方式二：购买，25元/30天，120元/180天，180元/360天（目前仅限APP购买）。"
            }
        ],
        bankAccount:[
            {
                title:"银行卡如何绑定？",
                answer:"我的账户—个人资料—绑定银行卡，点击“绑定银行卡”后的“绑定”，选择银行，输入银行卡，点击“确认绑定”，输入支付密码（富滇银行支付密码）以及短信验证码即可。"
            },
            {
                title:"邮箱如何绑定？",
                answer:"登录后，我的账户—个人资料—邮箱，点击“邮箱”后的“绑定”，输入邮箱，点击“绑定”，验证邮件发送至绑定的邮箱中，进入验证即可。"
            },
            {
                title:"绑卡时提示该银行卡不支持，怎么回事？",
                answer:"此银行卡富滇银行暂不支持使用，可更换其他类型银行卡操作。"
            },
            {
                title:"绑定银行卡只能绑定唯一一张银行卡吗？",
                answer:"是的，拓天速贷规定每个用户只能绑定一张银行卡，且不支持绑定信用卡。"
            },
            {
                title:"如何更换绑定的银行卡？",
                answer:"1）未开通快捷支付的情况：登录官方网站—我的账户—个人资料—修改（绑定银行卡右侧）—输入待更改银行卡号即可。<br/>（2）开通快捷支付的情况：如您的账户没有任何余额，情况同（1），否则需要按如下步骤操作：①提供本人手持身份证正反面照片，本人手持旧银行卡正反面照片，本人手持新银行卡正反面照片（需露出头部和手臂，证件号清晰）共六张照片发送到拓天速贷客服邮箱kefu@tuotiansudai.com (mailto:kefu@tuotiansudai.com)；②登录官方网站—我的账户—个人资料—修改（绑定银行卡右侧）—提交换卡申请。"
            },
            {
                title:"更换绑定的银行卡，一般需要多久？",
                answer:"更换银行卡需要先解绑旧银行卡，然后再绑定新银行卡，更换绑定的银行卡需要1-3个工作日。需要注意的是，解绑的要求是余额为零，要没有任何的资金交易才能解绑成功。"
            },
        ]

    },
    moneyBar:{
        recharge:[
            {
                title:"充值是否有上下限？",
                answer:"单笔充值金额必须大于或等于1元，网银支付限额由银行或您个人设置。值得注意的是，当日充值的金额需要等到下一工作日中午12点之后才能提现。"
            },
            {
                title:"充值的资金，一般多久可以到账？",
                answer:"网银或快捷充值都是实时到账的。"
            },
            {
                title:"银行卡扣费成功但充值未到账的原因及处理方法？",
                answer:"银行卡扣费成功但充值未到账可能有银行处理超时、掉单、通讯问题等原因；您可以稍等会儿再刷新账户余额，如果确实没有到账，您可以联系客服将您的手机号、充值时间、充值金额、银行卡以及银行网银流水以截图的方式发送至在线客服，由客服审核。如为银行掉单，即这笔充值记录在富滇银行没有查询到相关的充值记录，需要在充值时间的第二个工作日银行进行对账，差异账会通过3-7个工作日内退款至您的银行卡；若确定银行已扣款，需富滇银行补单，拓天速贷会追加您的账户余额，若富滇银行不补单，银行直接退款到本人的账户，视为充值失败。"
            },
            {
                title:"绑定的银行卡以前都能充值，为什么这次充值不了？",
                answer:"①银行卡余额不足；<br/>②电脑端更换浏览器重新操作，手机端退出APP重新操作；<br/>③如仍无法解决，请联系客服热线400-169-1188。"
            }
        ],
        invest:[
            {
                title:"什么是约定年化利率益？",
                answer:"“约定年化利率”是指平台与出借用户之间约定在一定期限内按照披露的年化利率进行收益结算，不包括服务费用和由于大小月产生的收益差别。还款方式的差异会产生不同的收益。"
            },
            {
                title:"什么是实际收益？",
                answer:"“实际收益”是指按实际收益天数实际回款后，扣除相关费用，得到的实际收益，可在交易记录中查询。"
            },
            {
                title:"在拓天速贷上投标后是否可以取消？",
                answer:"不可以取消，如有需要可进行债权转让。"
            },
            {
                title:"投资成功后，什么时候开始计算利息？",
                answer:"标满放款后生息。"
            },
            {
                title:"投标成功后，回款计划中为什么没有记录显示？",
                answer:"满标放款后才生成回款计划。"
            },
            {
                title:"投资到期后，什么时候回款？",
                answer:"当日回款。"
            },
            {
                title:"手头资金短缺，如何把投在平台投资项目里的资金快速取出来？",
                answer:"可以选择债权转让，如有特殊需要，可折价转让。"
            },
            {
                title:"如何查看电子合同？",
                answer:"登陆官方网站—我的账户—我的投资—直接项目/转让项目—全部（直接项目/转让项目下一行）—合同（即可看到每笔投资的合同）。"
            },
            {
                title:"如何将电子合同变成纸质合同？",
                answer:"如您需要纸质合同，在“我的投资”中将电子版合同下载后以电子或文件形式提供到拓天速贷客户服务中心，拓天速贷客服打印并盖章后交于您的手中。"
            }
        ],
        payments:[
            {
                title:"目前有哪几种回款方式？",
                answer:"2种，一种为：先付收益后还投资本金，按天计息，放款后生息；另一种为：到期还本付息，按天计息，放款后生息。"
            },
            {
                title:"投资回款有邮件、短信通知吗？",
                answer:"投资回款有短信通知的服务。"
            },
            {
                title:"借款人逾期还款怎么办？",
                answer:"会有相应的罚息。拓天速贷平台具有22道风控手续，上线至今，逾期率一直保持为零。"
            },
            {
                title:"投资后能不能提前收款？",
                answer:"投资人成功投标后是无法申请提前收款的，但可以通过债权转让的方式出让债权，快速实现变现。"
            },
            {
                title:"借款者提前还款，投资人实际收益怎么算？",
                answer:"按照实际的收益天数计算。"
            }
        ],
        cash:[
            {
                title:"申请提现后，资金到账时间一般是多久？",
                answer:"5万元以下的提现，实时到账；5万元（包括）以上的提现，工作日8:30-17:00可申请提现（其他时间不允许提现），实时到账。"
            },
            {
                title:"提现手续费是多少？",
                answer:"每笔提现手续费2.00元（富滇银行收取）。"
            },
            {
                title:"提现受理成功是到账了吗？",
                answer:"用户在平台发起账户资金提现至银行卡，页面输入支付密码和验证码确认后，资金存管方返回受理成功结果，即提现申请受理成功，具体提现时间以及到账因银行结算时间存在差异。"
            },
            {
                title:"提现失败怎么办？",
                answer:"建议重新操作，如多次失败，请拨打客服热线400-169-1188。"
            },
            {
                title:"提现最小金额是多少钱？",
                answer:"提现金额需大于2元。"
            },
            {
                title:"提现银行卡是否可以更换？",
                answer:"提现银行卡即您绑定的银行卡，换卡情况参照问题“如何更换绑定的银行卡”。"
            }
        ],
        found:[
            {
                title:"为什么开通银行资金存管？",
                answer:"（1）合规性要求，平台升级存管符合合规性要求，平台账户资金在银行监督下；<br/>（2）开通存管可以保障用户资金不被平台挪用，有利于保障用户的资金安全；<br/>（3）开通存管用户可以享受更多功能及福利。"
            },
            {
                title:"银行存管后有什么优势？",
                answer:"(1)系统分账监管<br/>拓天速贷接入富滇银行存管系统后，将由富滇银行对您的交易资金及平台自有运营资金进行分账监管，二者完全独立且相互隔离，平台无法触碰您的资金。<br/>(2)用户资金存管<br/>您进行的充值、绑卡、提现等每一笔与资金有关的操作，均需通过富滇银行资金存管账户，由富滇银行对您的资金信息进行管理，避免您的资金出现被挪用风险。<br/>(3)用户授权操作<br/>您需要开通富滇银行存管账户，并在富滇银行存管页面单独设立交易密码。在进行任何与资金相关操作时，系统会验证密码，在得到您的授权后，由银行根据交易指令进行资金划转。<br/>(4)交易真实有效<br/>富滇银行根据合同约定及您发出的交易指令，对交易流程进行管理并对所有资金流水进行存档记录，确保借贷双方的资金流转和债权关系清晰明确。"
            },
            {
                title:"银行存管上线以后对我们的资金有什么影响？",
                answer:"存管后，您的交易资金将直接流向富滇银行，由银行进行监管，平台无法触碰您的账户资金；您进行的任何充值、绑卡、提现等与资金相关的操作，均在富滇银行存管页面进行，由银行对您的资金信息进行管理，资金及操作更加安全无忧。"
            },
            {
                title:"富滇银行介绍？",
                answer:"富滇银行的前身是1911年蔡锷创办的云南全省公钱局，作为有着100多年历史的城市商业银行，富滇银行总资产规模接近1600亿，在全国近500家商业银行中名列前茅，作为网贷平台资金存管行业领先者之一，富滇银行对合作的互金平台筛选尤为严格，不仅要考察平台各项资质、风控能力、运营数据、平台高管履历等多个指标，还需要考察股东背景、实缴注册资本等硬性指标。"
            }
        ]
    },
    productBar:{
        mortgage:[
            {
                title:"抵押贷简介",
                answer:"拓天速贷主要产品为房屋抵押贷、车辆抵押贷，具体可以细分为：90天、180天、360天的抵押贷，约定年化利率8%-10%；50元起投；还款方式为：先付收益后还投资本金，按天计息，放款后生息。适用于有资金需求的小微企业、个体以及有出借需求的个人及家庭。"
            }

        ],
        transferRights:[
            {
                title:"债权转让在哪里购买？",
                answer:"登录官方网站—我要投资—转让项目（即可看到可购买的转让项目）。"
            },
            {
                title:"债权转让是否可以多人购买一个份？",
                answer:"债权转让为一对一转让，不能一份转让债权多人购买（整笔债权整笔转让，不可拆分）。"
            },
            {
                title:"债权转让购买后什么时候开始算利息？",
                answer:"接受转让成功后当日即可生息。"
            },
            {
                title:"债权转让是否可以获取活动奖励？",
                answer:"债权转让购买者无法获得活动、推荐人、财豆等奖励。"
            },
            {
                title:"债权转让如何出售？",
                answer:"①PC端：登录官方网站—我的账户—债权转让—可转让债权（选择您需要转让的债权进行操作即可）；<br/>②电脑端：登录官方APP—我的财富—债权转让—可转让债权（选择您需要转让的债权进行操作即可）。"
            },
            {
                title:"购买投资标的后多久可以债权转让？",
                answer:"标满放款后即可转让。"
            },
            {
                title:"债权转让手续费如何收取？",
                answer:"持有30天（含30天）以内扣除本金的1%；<br/>持有30-90天（含90天）扣除本金的0.5%；<br/>持有90天以上转让的不扣除手续费。"
            },
            {
                title:"债权转让折扣是什么意思？",
                answer:"例：500元债权，可以按照497.5的价格出售，购买者花费497.5元购买到一份500元的债权，手续费按500元的百分比扣除。"
            }
        ]

    },
    otherBar:{
        app:[
            {
                title:"怎么下载拓天速贷手机APP客户端？",
                answer:"两种方式：<br/>（1）到手机应用市场搜索下载：Android系统可到“应用商城”进行搜索（拓天速贷）下载即可；IOS系统可到“App Store”进行搜索（拓天速贷）下载即可；<br/>（2）点击平台右上角“手机APP”，扫描二维码即可下载；"
            }
        ]
    }
}


$helpCenter.find("img.help-center-bar").attr("src", helpCenterImgBarUrl);

let $changeBtn = $('.problem-title-item span'),
    $contentList = $('.problem-content-item'),
    $problem = $('.problem-single-item'),
    page = location.href.split('#')[1] ? parseInt(location.href.split('#')[1].slice(0, 1)) - 1 : false,
    index = location.href.split('#')[1] ? parseInt(location.href.split('#')[1].slice(1, 2)) - 1 : false;

$(window).on('hashchange',function () {
    whichShow();
})
whichShow();

let $resolvedBtn = $('.resolved-btn');
let $unsolvedBtn = $('.unsolved-btn');
//切换类型
// $changeBtn.on('click', function (event) {
//     event.preventDefault();
//     let $self = $(this),
//         index = $self.index();
//     $self.addClass('active').siblings().removeClass('active');
//     $contentList.find('.list-group:eq(' + index + ')').addClass('active')
//         .siblings('.list-group').removeClass('active');
// });



$problem.on('click', '.single-title', function (event) {
    event.preventDefault();
    var $self = $(this);
    $self.siblings('.single-answer').slideToggle('fast', function () {
        $self.parent().hasClass('active') ? $self.parent().removeClass('active').find('.fa').removeClass('fa-angle-up').addClass('fa-angle-down') : $self.parent().addClass('active').find('.fa').removeClass('fa-angle-down').addClass('fa-angle-up');
    });
}).on('click', '.fa', function (event) {
    event.preventDefault();
    $(this).siblings('.single-title').trigger('click')
});

$('.problem-title-item span:eq(' + page + ')').trigger('click');
$('.list-group:eq(' + page + ')').find('.problem-single-item:eq(' + index + ') .single-title').trigger('click');

getData($('#registerList'),'registerBar','register');
getData($('#accountList'),'registerBar','account');
getData($('#passwordList'),'accountBar','password');
getData($('#vipList'),'accountBar','vip');
getData($('#bankAccountList'),'accountBar','bankAccount');

getData($('#rechargeList'),'moneyBar','recharge');
getData($('#investList'),'moneyBar','invest');
getData($('#paymentsList'),'moneyBar','payments');
getData($('#cashList'),'moneyBar','cash');
getData($('#fundList'),'moneyBar','found');

getData($('#mortgageList'),'productBar','mortgage');
getData($('#transferRightsList'),'productBar','transferRights');

getData($('#otherList'),'otherBar','app');

// passwordList

function getData(rootDOM,type,question) {

    var str = '';

    var list = helperCenterData[type][question];
    for(var i = 0;i<list.length;i++){
        str+='<li class="problem-single-item">\n' +
            '\n' +
            '    <p class="single-title" data-type="'+type+'" data-question="'+question+'" data-index="'+i+'"><a href="help-content?type='+type+'&question='+question+'&index='+i+'">'+(i+1)+'、'+list[i].title+'</a></p>\n' +
            '\n' +
            '    </li>';
    }
    rootDOM.html(str);
}

var $helpContainer = $('#helpContentContainer');

if($helpContainer.length){
    var type = sourceKind.params.type;
    var question = sourceKind.params.question;
    var indexs = sourceKind.params.index;
    var typeStr = '';
    var typeHref = '';
    var questionHref = '';
    var questionStr = '';
    let isSolution = -1;
    let contentId = type+','+question+','+indexs;
    $('.left-nav').find('li a').removeClass('active');
    switch (type) {
        case 'registerBar':
            $('.left-nav').find('li').eq(0).find('a').addClass('active');
            typeStr='注册认证';
            typeHref= '/help/account';
            if(question == 'register'){
                questionHref = '/help/account#11';
                questionStr = '注册';
            }else if(question == 'account'){
                questionHref = '/help/account#21';
                questionStr = '认证';
            }
            break;
        case 'moneyBar':
            $('.left-nav').find('li').eq(2).find('a').addClass('active');
            typeStr='资金相关';
            typeHref= '/help/money';
            if(question == 'recharge'){
                questionHref = '/help/money#11';
                questionStr = '充值';
            }else if(question == 'invest'){
                questionHref = '/help/money#21';
                questionStr = '投资';
            }else if(question == 'payments'){
                questionHref = '/help/money#31';
                questionStr = '回款';
            }else if(question == 'cash'){
                questionHref = '/help/money#41';
                questionStr = '提现';
            }else if(question == 'found'){
                questionHref = '/help/money#51';
                questionStr = '资金存管';
            }
            break;
        case 'productBar':
            $('.left-nav').find('li').eq(3).find('a').addClass('active');
            typeStr='产品类型';
            typeHref= '/help/product';
            if(question == 'mortgage'){
                questionHref = '/help/product#11';
                questionStr = '抵押贷';
            }else if(question == 'transferRights'){
                questionHref = '/help/product#21';
                questionStr = '债权转让';
            }
            break;
        case 'otherBar':
            $('.left-nav').find('li').eq(4).find('a').addClass('active');
            typeStr='其他问题';
            typeHref= '/help/other';
            if(question == 'app'){
                questionHref = '/help/other#11';
                questionStr = '手机客户端';
            }
            break;
        case 'accountBar':
            $('.left-nav').find('li').eq(1).find('a').addClass('active');
            typeStr='账户管理';
            typeHref= '/help/user';
            if(question == 'password'){
                questionHref = '/help/user#11';
                questionStr = '密码设置';
            }else if(question == 'vip'){
                questionHref = '/help/user#21';
                questionStr = 'VIP会员';
            }else if(question == 'bankAccount'){
                questionHref = '/help/user#31';
                questionStr = '银行卡认证及更换';
            }
            break;

    }

    var navStr = '<a href="/help/help-center">帮助中心></a><a href="'+typeHref+'">'+typeStr+'></a><a href="'+questionHref+'">'+questionStr+'></a>'+helperCenterData[type][question][indexs].title;
    $('#questionTitle').html(helperCenterData[type][question][indexs].title);
    $('#helpContent').html(helperCenterData[type][question][indexs].answer);
   $('#nav').html(navStr);
    $('.solve-btn').on('click',function () {
        let _self = $(this);


        if(_self.hasClass('resolved-btn')){
            isSolution = 1;
        }else if(_self.hasClass('unsolved-btn')){
            isSolution = 0;
        }
        if(_self.hasClass('disabled')||_self.hasClass('selected')){
            layer.msg('您已参与过投票');
            return;
        }

        selectSquare(contentId,isSolution)


    })
    selectSquare(contentId)

}

function selectSquare(contentId,isSolution) {
    let url;
    if(isSolution!==undefined){
        url = '/help/help-content/vote?contentId='+contentId+'&isSolution='+isSolution;
    }else {
        url = '/help/help-content/vote?contentId='+contentId
    }
    commonFun.useAjax({
            url: url,
            type: 'POST'
        }, function (res) {
            if(res.status ===true){
                $('#person').text(res.data.voteNumber);
                if(res.data.isSolution===1){
                    $resolvedBtn.addClass('selected');
                    $unsolvedBtn.addClass('disabled');
                }else if(res.data.isSolution===0){
                    $unsolvedBtn.addClass('selected');
                    $resolvedBtn.addClass('disabled');
                }
            }else {
                layer.msg(res.message)
            }

        }
    )
}



$titleBox.on('click',function (e) {
    e.stopPropagation();
    e.preventDefault();
    let self = $(this);
    if(self.parent().find('ul').hasClass('show')){
        self.parent().find('ul').removeClass('show')
    }else {
        self.parent().find('ul').addClass('show')
    }
    if(self.find('.icon-arrow').hasClass('down')){
        self.find('.icon-arrow').removeClass('down')
    }else {
        self.find('.icon-arrow').addClass('down')
    }

    if(self.find('.icon-arrow').hasClass('whiteDown')){
        self.find('.icon-arrow').removeClass('whiteDown').addClass('whiteRight');
    }else if(self.find('.icon-arrow').hasClass('whiteRight')){
        self.find('.icon-arrow').removeClass('whiteRight').addClass('whiteDown');
    }
})

function whichShow() {
    let title = location.href.split('#')[0].split('/')[location.href.split('#')[0].split('/').length-1],
        page2 = location.href.split('#')[1] ? parseInt(location.href.split('#')[1].slice(0, 1)) - 1 : false

    $contentList.find('.list-group:eq(' + page2 + ')').addClass('active')
        .siblings('.list-group').removeClass('active');
    $changeBtn.eq(page2).addClass('active').siblings().removeClass('active');


    switch (title) {
        case 'account':
            $leftBox.find('.swiper-slide').eq(0).find('>a').find('.icon-arrow').addClass('whiteDown');
            $leftBox.find('.swiper-slide').eq(0).find('>a').addClass('active');
            $leftBox.find('.swiper-slide').eq(0).find('ul').addClass('show');
            $leftBox.find('.ulSection').eq(0).find('li').eq(page2).find('a').addClass('active').end().siblings().find('a').removeClass('active');
            break;
        case 'user':
            $leftBox.find('.swiper-slide').eq(1).find('>a').find('.icon-arrow').addClass('whiteDown');
            $leftBox.find('.swiper-slide').eq(1).find('>a').addClass('active');
            $leftBox.find('.swiper-slide').eq(1).find('ul').addClass('show');
            $leftBox.find('.ulSection').eq(1).find('li').eq(page2).find('a').addClass('active').end().siblings().find('a').removeClass('active');
            break;
        case 'money':
            $leftBox.find('.swiper-slide').eq(2).find('>a').find('.icon-arrow').addClass('whiteDown');
            $leftBox.find('.swiper-slide').eq(2).find('>a').addClass('active');
            $leftBox.find('.swiper-slide').eq(2).find('ul').addClass('show');
            $leftBox.find('.ulSection').eq(2).find('li').eq(page2).find('a').addClass('active').end().siblings().find('a').removeClass('active');
            break;
        case 'product':
            $leftBox.find('.swiper-slide').eq(3).find('>a').find('.icon-arrow').addClass('whiteDown');
            $leftBox.find('.swiper-slide').eq(3).find('>a').addClass('active');
            $leftBox.find('.swiper-slide').eq(3).find('ul').addClass('show');
            $leftBox.find('.ulSection').eq(3).find('li').eq(page2).find('a').addClass('active').end().siblings().find('a').removeClass('active');
            break;
        case 'other':
            $leftBox.find('.swiper-slide').eq(4).find('>a').find('.icon-arrow').addClass('whiteDown');
            $leftBox.find('.swiper-slide').eq(4).find('a').addClass('active');
            $leftBox.find('.swiper-slide').eq(4).find('ul').addClass('show');
            $leftBox.find('.ulSection').eq(4).find('li').eq(page2).find('a').addClass('active').end().siblings().find('a').removeClass('active');
            break;
    }
}