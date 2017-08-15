module.exports = function(){
    console.log('it is a ');
    var h2= document.createElement("h2");
    h2.innerHTML="不是吧，那么快第二个打包程序啦！隐隐约约";
    document.getElementsByTagName('body')[0].appendChild(h2);
	
}

exports.age = function() {
    console.log('My age is 22');
};



