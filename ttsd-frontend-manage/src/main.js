require.ensure(['./define','./define2'], function(){
  
   var list = require('./define');
    console.log(define.a);
    // console.log(define2.mm);
});




// commonjs同步语法
// var a = require('./a');



// commonjs异步加载
// 在commonjs中有一个Modules/Async/A规范，里面定义了require.ensure语法。
// webpack实现了它，作用是可以在打包的时候进行代码分片，并异步加载分片后的代码。用法如下：

// require.ensure([], function(require){
//     var list = require('./list');
//     list.show();
// });



// commonjs预加载懒执行
// 在上面的用法中，我们给require.ensure的第一个参数传了空数组，实际上这里是可以接收模块名称的，作用就是实现预加载懒执行

// require.ensure(['./list'], function(require){
//     var list = require('./list');
//     list.show();
// });

// webpack自带的require.include
// require.include是webpack自己提供的
// require.ensure([], function(require){
//     require.include('./list');//此处只加载不执行
// });

// require.ensure([], function(require){
//     require.include('./preview'); //加载
//     let p = require('./preview'); //执行
//     p.getUrl(); //使用
// }, 'pre');


// AMD异步加载
// webpack既支持commonjs规范也支持AMD规范，这就意味着AMD的经典语法是可以正常使用的
// require(['./list'], function(list){
//     list.show();
// });

// require(['./list', './edit'], function(list, edit){
//     list.show();
//     edit.display();
// });

