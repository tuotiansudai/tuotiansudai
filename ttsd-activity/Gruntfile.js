var fs = require('fs');
var path = require('path');

var getJSModules = function() {
    var arr = fs.readdirSync(path.join(__dirname, 'src/main/webapp/activity/js'));
    var modules = [];
    var exclude = ['cnzz_statistics.js', 'config.js'];
    return arr.reduce(function(modules, val) {
        if (/\.js$/.test(val) && exclude.indexOf(val) === -1) {
            modules.push({
                name: 'js/' + val.substring(0, val.length - 3)
            });
        }
        return modules;
    }, []);
};

module.exports = function(grunt) {
    // load all grunt tasks
    require('load-grunt-tasks')(grunt);

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        // Metadata.
        meta: {
            basePath: '../',
            baseSassPath: 'src/main/webapp/activity/style/sass',
            baseCssPath: 'src/main/webapp/activity/style',
            baseCssMinPath: 'src/main/webapp/activity/style/dest',
            baseJsPath: 'src/main/webapp/activity/js',
            baseJsMinPath: 'src/main/webapp/activity/js/dest'
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
                        '<%= meta.baseJsMinPath %>/*',
                        '<%= meta.baseJsPath %>/debug'
                    ]
                }]
            },
            debug: {
                files: [{
                    dot: true,
                    src: [
                        '<%= meta.baseJsPath %>/debug'
                    ]
                }]
            },
            base64: {
                files: [{
                    dot: true,
                    src: [
                        '<%= meta.baseCssPath %>/base64'
                    ]
                }]
            }
        },
        copy: {
            options: {
                // banner: '/*! <%= pkg.name %> <%= grunt.template.today("yyyymmddHHMM") %> */\n'
            },
            dist: {
                files: [{
                    expand: true, // Enable dynamic expansion.
                    cwd: '', // Src matches are relative to this path.
                    src: ['<%= meta.baseJsPath %>/debug/js/*.js'], // Actual pattern(s) to match.
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
            base64: {
                files: [{
                    expand: true, // Enable dynamic expansion.
                    cwd: '', // Src matches are relative to this path.
                    src: ['<%= meta.baseCssPath %>/base64/*.css'], // Actual pattern(s) to match.
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
            },
            // cssmin: {
            //     files: [
            //         ['<%= meta.baseCssPath %>/*.css']
            //     ],
            //     tasks: ['cssmin:dist']
            // },
            // uglify: {
            //     files: [
            //         ['<%= meta.baseJsPath %>/*.js']
            //     ],
            //     tasks: ['clean:js', 'uglify']
            // }
        },
        connect: {
            server: {
                options: {
                    port: 8088,
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
                dest: '<%= meta.baseCssPath %>/base64',
                options: {
                    target: ['./src/main/webapp/activity/images/**/*.*'],
                    fixDirLevel: false,
                    maxBytes: 1024 * 8,
                    baseDir: './src/main/webapp/activity'
                }
            }
        },
        filerev: {
            options: {
                algorithm: 'md5',
                length: 8,
                process: function(basename, name, extension) {
                    if (/\.min/.test(basename)) {
                        return basename.match(/(.*)\.min$/)[1] + '.' + name + '.min.' + extension;
                    } else {
                        return basename + '.' + name + '.' + extension;
                    }
                }
            },
            assets: {
                files: [{
                    src: [
                        '<%= meta.baseJsMinPath %>/*.js',
                        '<%= meta.baseCssMinPath %>/*.css'
                    ]
                }]
            }
        },
        requirejs: {
            compile: {
                options: {
                    baseUrl: 'src/main/webapp/activity',
                    mainConfigFile: '<%= meta.baseJsPath %>/config.js',
                    // optimize: 'none',
                    stubModules: ['text'],
                    modules: getJSModules(),
                    dir: '<%= meta.baseJsPath %>/debug'
                }
            }
        }
    });

    // 默认被执行的任务列表。
    grunt.registerTask('default', ['clean', 'sass', 'connect', 'watch']);
    grunt.registerTask('dist', ['clean', 'sass', 'dataUri', 'cssmin:base64', 'clean:base64', 'requirejs', 'copy', 'filerev', 'clean:debug']);
};