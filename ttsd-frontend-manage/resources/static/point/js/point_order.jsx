require('pointStyle/point_order.scss');
let tpl = require('art-template/dist/template');
let ValidatorObj= require('publicJs/validator');
let commonFun= require('publicJs/commonFun');
var orderPlaceForm = globalFun.$('#addressForm'),
	$btnAddressSubit=$('#btnAddressSubit');


var $pointOrder = $('#pointOrder');
var $exchangeTip = $('#exchangeTip');
var $countList = $('.order-number',$pointOrder),
	$numText = $countList.find('.num-text'),
	currentNum=parseInt($numText.val()), //目前已选商品的个数
	$orderBtn = $('#orderBtn'),
	$addPlace = $('#addPlace'),
	$updatePlace = $('#updatePlace'),
	$addressForm = $('#addressForm');

//该用户总共可以兑换商品的数量
var myLimit = parseInt($countList.data('mylimit'));
//该用户本月已兑换的数量
var buyCount = parseInt($countList.data('buycount'));
var myRest;
if(myLimit) {
	//本月该用户剩下能兑换的数量
	myRest = myLimit-buyCount;
	$exchangeTip.find('i').text(myRest);
	if(myRest==0) {
		$orderBtn.prop('disabled',true);
	}
} else {
	myRest = parseInt($countList.data('overplus'));
}


$countList.on('click',function(event) {
	var target = event.target;
	var overplus = parseInt($countList.data('overplus'));  //剩余商品的数量

	var compareNum = (myRest==0) ? overplus :Math.min(overplus,myRest);
	currentNum=parseInt($numText.val());
	if(overplus<1) {
		return;
	}
	//点击减少－
	if(/low-btn/.test(target.className) && currentNum>1) {
		$numText.val(--currentNum);

	} else if(/add-btn/.test(target.className) && currentNum < compareNum) {
		$numText.val(++currentNum);
	}
	changeCount();
});

changeCount();

function changeCount() {
	var totalPoint = parseInt($('.count-num',$pointOrder).data('num')) * currentNum;
	$('.count-num',$pointOrder).text(totalPoint);
}

$orderBtn.on('click', function(event) { //立即兑换
	event.preventDefault();
	var $self = $(this),
		idString = $self.attr('data-id'),
		typeString = $self.attr('data-type');
    commonFun.useAjax({
        url:'/point-shop/order',
        data: {
            id: idString,
            goodsType: typeString,
            number: $numText.val(),
            userAddressId: $('#updatePlace').attr('data-id'),
			comment:$('#comment').val()
        },
        type:'POST',
    },function(data){
        if (data.data.status) {
            layer.msg('兑换成功！3秒后自动跳转到兑换记录页面。');
            setTimeout(function(){
                location.href = '/point-shop/record';
            },3000)

        } else {
            errorTip(data.data);
        }
    });
});
//add adress
$addPlace.on('click', function(event) {
	event.preventDefault();
	$addressForm.attr('data-type', 'add');
	layer.open({
		type: 1,
		title: '添加地址',
		area: ['700px', 'auto'],
		content: $('#fixAdress')
	});
});

//update place
$updatePlace.on('click', function(event) {
	event.preventDefault();
	var $self = $(this),
		user = $self.attr('data-user'),
		phone = $self.attr('data-phone'),
		address = $self.attr('data-address');
	$('#Recipient').val(user),
	$('#Phone').val(phone),
	$('#AddRess').val(address)
	$addressForm.attr('data-type', 'update');
	layer.open({
		type: 1,
		title: '添加地址',
		area: ['700px', 'auto'],
		content: $('#fixAdress')
	});
});
if(orderPlaceForm){
    let validator = new ValidatorObj.ValidatorForm();
    validator.add(orderPlaceForm.Recipient, [{
        strategy: 'isNonEmpty',
        errorMsg: '请输入收件人',
    }],true);

    validator.add(orderPlaceForm.Phone, [{
        strategy: 'isNonEmpty',
        errorMsg: '请输入手机号码',
    },{
        strategy:'isMobile',
        errorMsg: '请输入正确的手机号码'
    }],true);

    validator.add(orderPlaceForm.AddRess, [{
        strategy: 'isNonEmpty',
        errorMsg: '请输入收货地址',
    }],true);

    let reInputs=$(orderPlaceForm).find('.int-text');
	for(let i=0,len=reInputs.length; i<len;i++) {
		globalFun.addEventHandler(reInputs[i],"keyup", "blur", function() {
			validator.start(this);
			isDisabledButton();
		})
	}
//用来判断按钮 是否可点击
    function isDisabledButton() {
        let Recipient = orderPlaceForm.Recipient,
            Phone = orderPlaceForm.Phone,
            AddRess = orderPlaceForm.AddRess;

        let isRecipientValid=!globalFun.hasClass(Recipient,'error') && Recipient.value;
        let isPhoneValid = !globalFun.hasClass(Phone,'error') && Phone.value;
        let isAddRessValid=!globalFun.hasClass(AddRess,'error') && AddRess.value;
        if(isRecipientValid && isPhoneValid && isAddRessValid) {
            $btnAddressSubit.prop('disabled',false);
        }
        else {
            $btnAddressSubit.prop('disabled',true);
        }
    }

//点击立即注册按钮
    orderPlaceForm.onsubmit = function(event) {
        event.preventDefault();
        $btnAddressSubit.prop('disabled', true);
        submitPlace($(orderPlaceForm).attr('data-type'));
    }

}




$('body').on('click', '.close-layer',function(event) {
	event.preventDefault();
	layer.closeAll();
});
//submit place data
function submitPlace(type) {
	var dataList = {};
	if (type == 'add') {
		dataList = {
			url: '/point-shop/add-address',
			data: {
				realName: $('#Recipient').val(),
				mobile: $('#Phone').val(),
				address: $('#AddRess').val()
			}
		};
	} else if (type == 'update') {
		dataList = {
			url: '/point-shop/update-address',
			data: {
				id: $('#updatePlace').attr('data-id'),
				realName: $('#Recipient').val(),
				mobile: $('#Phone').val(),
				address: $('#AddRess').val()
			}
		};
	}
    commonFun.useAjax({
        url:dataList.url,
        type:'POST',
        data:dataList.data
    },function(data) {
        if (data.data.status) {
            location.reload();
        } else {
            errorTip(data.data);
        }
    });
}
//error tip
function errorTip(msg){
	$('#errorTip').html(tpl('errorTipTpl', msg));
	layer.open({
		type: 1,
		title: false,
		area: ['300px', '180px'],
		content: $('#errorTip')
	});
}
