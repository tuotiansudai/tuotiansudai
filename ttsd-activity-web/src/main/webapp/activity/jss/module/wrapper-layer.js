define(['layer1', 'layer-extend'], function (layer) {
        layer.config({
            path: staticServer+'/activity/js/libs/layer/'
        });
    console.log(staticServer);
    return layer;
});
