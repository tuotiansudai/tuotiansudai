function Middleware(){
    this.cache = [];
    this.options = null;//缓存options
}

Middleware.prototype.use = function(fn){
    if(typeof fn !== 'function'){
        throw 'middleware must be a function';
    }
    this.cache.push(fn);
    return this;
}

Middleware.prototype.next = function(fn){
    if(this.middlewares && this.middlewares.length > 0 ){
        var ware = this.middlewares.shift();

        ware.call(this, this.options, this.next.bind(this));//传入options与next
    }
}
Middleware.prototype.handleRequest = function(options){
    //执行请求
    this.middlewares = this.cache.map(function(fn){//复制
        return fn;
    });
    this.options = options;//缓存数据
    this.next();
}

module.exports = Middleware;