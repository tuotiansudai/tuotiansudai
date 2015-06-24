/**
 * Created by zhaoshuai on 2015/4/29.
 */
module.exports = function (grunt) {
    //任务配置：
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),//读取 package.json 文件，并把里面的信息获取出来，方便在后面任务中应用;
        uglify: {//uglify名字是固定的，表示下面任务是调用 uglify 插件的，首先配置了一些全局的 options 然后新建了一个 build 也就是说，在 Uglify 插件下面，有一个 build 任务，内容是把 XX.js 压缩输出到 xx.min.js 里面。如果你需要更多压缩任务，也可以参照 build 多写几个任务。任务：压缩文件
            options: {
                banner: '/*!<%=pkg.name %> <%=grunt.template.today("yyy-mm-dd") %>*/\n'
            },
            build: {//新建了一个基于 uglify 的任务 build，功能是把 src/<%= pkg.name %>.js 压缩输出 build/<%= pkg.name %>.min.js。
                src: 'doMove.js',//需要压缩的文件名，注意路径问题;
                dest: './doMove.min.js'//压缩后输出的文件名，注意路径问题;
            }

        },
        sass: {
            dist: {
                files: {
                    //输出后的文件：要输出的文件，也就是源文件；
                    "static/style/index.css": "static/style/index.scss"
                },
                options: {
                }
            }
        },
        watch: {
            //监视的任务:
            sass: {
                files: [
                    'static/style/*.scss'
                ],
                tasks: ["sass"]
            },
            client: {
                //*.html,*.css代表所有的html和所有的css文件;
                files: ['*.html', 'css/*', 'js/*', 'images/**/*'],
                options: {
                    livereload: true
                }
            }
        },
        connect: {
            options: {
                port: 9099,//端口号;
                open: true,
                livereload: 35729,
                hostname: 'localhost'//最好配置为localhost，这样即使换了IP地址，也能正常使用grunt运行项目;
            },
            server: {
                options: {
                    base: './'
                }
            }
        },

        cssmin: {
            compress: {
                files: {
                    "static/style/index.min<%= grunt.template.today('yyyy-mm-dd') %>.css": [
                        "static/style/index.css"
                    ]
                }
            }
        }

//        'string-replace': {
//            dist: {
//                files: {
//                    'static/': ['static/templates/homepage.html', 'static/templates/cash.html', 'static/templates/email.html', 'static/templates/realname.html', 'static/templates/recharge.html'],
//                    'static/': ['static/templates/homepage.html', 'static/templates/cash.html', 'static/templates/email.html', 'static/templates/realname.html', 'static/templates/recharge.html']
//                },
//                options: {
//                    replacements: [
//                        {
//                            pattern: 'index.min.css',
//                            replacement :"index.min<%= grunt.template.today('yyyy-mm-dd') %>.css"
//                        }
//                    ]
//                }
//            }
//        }
    });
    //插件加载(可选项，如果使用了插件，就要调用，如果没使用插件，可以忽略)
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-connect');
    grunt.loadNpmTasks('grunt-contrib-sass');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    //grunt.loadNpmTasks('grunt-string-replace');
    //任务注册：注册一个任务。在 default 上面注册了一个 Uglify 任务，default 就是别名，它是默认的 task，当你在项目目录执行 grunt 的时候，它会执行注册到 default 上面的任务。也就是说，当我们执行 grunt 命令的时候，uglify 的所有代码将会执行。我们也可以注册别的 task，例如：grunt.registerTask('compress', ['uglify:build']);
    grunt.registerTask('default', ['connect', 'cssmin', 'watch']);
    grunt.registerTask('live', ['watch']);
}

//"connect-livereload": "^0.5.3",/*页面无刷新显示修改后的样式*/
//"grunt": "^0.4.5",
//    "grunt-contirb-cssmin": ">=0.4.0",/css压缩/
//  "grunt-contrib-concat": "^0.5.1",
//  "grunt-contrib-connect": "^0.10.1",
//  "grunt-contrib-jshint": "^0.11.2",/JS语法检查/
//  "grunt-contrib-sass": "^0.9.2",/sass文件转css/
//  "grunt-contrib-uglify": "^0.9.1",/默认/
//  "grunt-contrib-watch": "^0.6.1",/文件修改监听/
//  "grunt-string-replace": "^0.4.0"/实现修改的文件加时间戳/

