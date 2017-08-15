

// var test=require('./second.js');
// console.log(test.age);

require('./main.scss');
require.ensure([], function() { // 语法奇葩, 但是有用
   
    var a = require('./first'); // 函数调用后, 模块保证在同步请求下可用
		console.log(a());
  });