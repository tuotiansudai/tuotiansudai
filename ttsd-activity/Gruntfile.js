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
                        '<%= meta.baseCssPath %>/*.css',
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
            },
            base64: {
                files: [{
                    dot: true,
                    src: [
                        '<%= meta.base64CssPath %>'
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
                    src: ['<%= meta.baseJsPath %>/*.js'], // Actual pattern(s) to match.
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
        cssmin: {
            dist: {
                files: [{
                    expand: true, // Enable dynamic expansion.
                    cwd: '', // Src matches are relative to this path.
                    src: ['<%= meta.baseCssPath %>/*.css'], // Actual pattern(s) to match.
                    dest: '<%= meta.baseCssMinPath %>/', // Destination path prefix.
                    ext: '.min.css', // Dest filepaths will have this extension.
                    extDot: 'first', // Extensions in filenames begin after the first dot
                    flatten: true
                }]
            },
            base64: {
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
                tasks: ['newer:clean:css', 'newer:sass']
            },
            cssmin: {
                files: [
                    ['<%= meta.base64CssPath %>/*.css']
                ],
                tasks: ['newer:cssmin:dist']
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
        }
    });

    //转化成base64
    grunt.registerTask('base64', ['dataUri', 'cssmin:base64']);
    //,'clean:base64'

    // 默认被执行的任务列表。
    grunt.registerTask('default', [
        'clean',
        'newer:uglify',
        'sass',
        'base64',
        'cssmin:base64',
        'connect',
        'watch'

    ]);

    /*前端人员开发的时候用，最后发布的时候执行一次 grunt */
    grunt.registerTask('dev',
        [
            'newer:clean',
            'newer:uglify',
            'newer:sass',
            'connect',
            'watch:sass'
        ]);


};