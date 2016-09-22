module.exports = function(grunt) {
    require('time-grunt')(grunt);
    require('load-grunt-tasks')(grunt);  //load all grunt tasks
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        // Metadata.
        meta: {
            basePath: '../',
            baseSassPath: 'src/main/webapp/activity/style/sass',
            baseCssPath: 'src/main/webapp/activity/style/css',
            base64CssPath: 'src/main/webapp/activity/style/base64',
            baseCssMinPath: 'src/main/webapp/activity/style/dest',
            baseJsPath: 'src/main/webapp/activity/js',
            baseJsMinPath: 'src/main/webapp/activity/js/dest',
            baseImagePath:'src/main/webapp/activity/images',
        },
        clean: {
            css: {
                files: [{
                    dot: true,
                    src: [
                        '<%= meta.baseCssPath %>/*',
                        '<%= meta.base64CssPath %>/*',
                        '<%= meta.baseCssPath %>/*.map',
                        '<%= meta.baseCssMinPath %>/*'
                    ]
                }]
            },
            js: {
                files: [{
                    dot: true,
                    src: [
                        '<%= meta.baseJsMinPath %>/*'
                    ]
                }]
            }
        },
        uglify: {
            options: {
                // banner: '/*! <%= pkg.name %> <%= grunt.template.today("yyyymmddHHMM") %> */\n'
            },
            dist: {
                files: [{
                    expand: true, // Enable dynamic expansion.
                    cwd: '', // Src matches are relative to this path.
                    src: ['<%= meta.baseJsPath %>/*.js','<%= meta.baseJsPath %>/module/*.js'], // Actual pattern(s) to match.
                    dest: '<%= meta.baseJsMinPath %>/', // Destination path prefix.
                    ext: '.min.js', // Dest filepaths will have this extension.
                    extDot: 'first', // Extensions in filenames begin after the first dot
                    flatten: true
                }]

            }
        },
        sass: {
            dist: {
                files: [{
                    expand: true, // Enable dynamic expansion.
                    cwd: '', // Src matches are relative to this path.
                    src: ['<%= meta.baseSassPath %>/*.scss'], // Actual pattern(s) to match.
                    dest: '<%= meta.baseCssPath %>/', // Destination path prefix.
                    ext: '.css', // Dest filepaths will have this extension.
                    extDot: 'first', // Extensions in filenames begin after the first dot
                    flatten: true
                }]
            }
        },
        dataUri: {
            dist: {
                src: ['<%= meta.baseCssPath %>/*.css'],
                dest: '<%= meta.base64CssPath %>',
                options: {
                    target: ['<%=meta.baseImagePath %>/**/*.*'],
                    fixDirLevel: false,
                    maxBytes: 1024 * 8   //小于8k的图片会生成base64 ,并且需要是相对路径
                }
            }
        },
        cssmin: {
            //dist: {
            //    files: [{
            //        expand: true, // Enable dynamic expansion.
            //        cwd: '', // Src matches are relative to this path.
            //        src: ['<%= meta.baseCssPath %>/*.css'], // Actual pattern(s) to match.
            //        dest: '<%= meta.baseCssMinPath %>/', // Destination path prefix.
            //        ext: '.min.css', // Dest filepaths will have this extension.
            //        extDot: 'first', // Extensions in filenames begin after the first dot
            //        flatten: true
            //    }]
            //},
            dist: {
                files: [{
                    expand: true, // Enable dynamic expansion.
                    cwd: '', // Src matches are relative to this path.
                    src: ['<%= meta.base64CssPath %>/*.css'], // Actual pattern(s) to match.
                    dest: '<%= meta.baseCssMinPath %>/', // Destination path prefix.
                    ext: '.min.css', // Dest filepaths will have this extension.
                    extDot: 'first', // Extensions in filenames begin after the first dot
                    flatten: true
                }]
            }
        },
        watch: {
            sass: {
                files: [
                    '<%= meta.baseSassPath %>/*.scss'
                ],
                tasks: ['sass']
                //如果scss文件没有import别的scss文件，可以加newer，效率快
                //如果scss有import别的scss文件，不要加newer,不然监听不到import里文件的变化
                //tasks: ['newer:sass']
            },
            dataUri:{
                files: [
                    '<%= meta.baseCssPath %>/*.css'
                ],
                tasks: ['dataUri']
            },
            cssmin: {
                files: ['<%= meta.base64CssPath %>/*.css'],
                tasks: ['newer:cssmin']
            },

            uglify: {
                files: [
                    ['<%= meta.baseJsPath %>/*.js']
                ],
                tasks: ['newer:clean:js', 'newer:uglify']
            }
        },
        connect: {
            server: {
                options: {
                    port: 5088,
                    hostname: '*',
                    base: 'src/main/webapp',
                    middleware: function(connect, options, middlewares) {
                        middlewares.unshift(function(req, res, next) {
                            res.setHeader('Access-Control-Allow-Origin', '*');
                            return next();
                        });
                        return middlewares;
                    }
                }
            }
        },
        imagemin: {
            /* 压缩图片大小 */
            dist: {
                options: {
                    // cache:false,
                    optimizationLevel: 5,
                    progressive: true
                },
                files: [
                    {
                        expand: true,
                        cwd: '<%=meta.baseImagePath%>/',
                        src: ['**/*.{png,jpg,jpeg}'],   // 优化 img 目录下所有 png/jpg/jpeg 图片
                        dest:'<%=meta.baseImagePath%>/' // 优化后的图片保存位置，覆盖旧图片，并且不作提示
                    }
                ]
            }
        },
    });

    //转化成base64
    grunt.registerTask('base64', ['dataUri', 'newer:cssmin']);
    //,

    //压缩图片，需要压缩图片的时候单独执行 grunt imagemin

    //grunt.registerTask('imagemin', ['newer:imagemin']);

    // 默认被执行的任务列表。
    grunt.registerTask('default', [
        'clean',
        'uglify',
        'sass',
        'cssmin',
        'base64',
        'connect',
        'watch'
    ]);

    /* 前端人员开发的时候用，最后发布的时候执行一次 grunt */
    grunt.registerTask('dev',
        [
            'newer:clean',
            'newer:uglify',
            'newer:sass',
            'connect',
            'watch:sass'
        ]);
};