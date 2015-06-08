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
            build: {
                src: 'doMove.js',
                dest: './doMove.min.js'
            }

        },
        sass: {
            dist: {
                files: {

                    "static/style/index.css": "static/style/index.scss"
                },
                options: {
                }
            }
        },
        watch: {

            sass: {
                files: [
                    'static/style/*.scss'
                ],
                tasks: ["sass"]
            },
            client: {

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
                hostname: 'localhost'
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
                    "src/main/webapp/site/themes/default/style/website<%= grunt.template.today('yyyymmddHHMMss') %>.min.css": [
                        "src/main/webapp/site/themes/default/style/website.css"
                    ],
                    "src/main/webapp/site/themes/default/style/footer<%= grunt.template.today('yyyymmddHHMMss') %>.min.css": [
                        "src/main/webapp/site/themes/default/style/footer.css"
                    ]
                }
            }
        },
        'string-replace': {
            dist: {
                files: {
                    'src/': ['src/main/webapp/site/themes/default/templates/website/*.xhtml',
                        'src/main/webapp/site/themes/default/templates/footer.xhtml']
                },
                options: {
                    replacements: [
                        {
                            //pattern:/(\d+){0,1}\.(min){0,1}\.css/ig,
                            pattern: /(\.min|\d*min)?\.css/ig,
                            replacement:"<%= grunt.template.today('yyyymmddHHMMss') %>.min.css"
                        },
                        {
                            //pattern:/(\d+){0,1}\.(min){0,1}\.css/ig,
                            pattern: /(\.min|\d*min)?\.css/ig,
                            replacement:"<%= grunt.template.today('yyyymmddHHMMss') %>.min.css"
                        }
                    ]
                }
            }
        }
    });
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-connect');
    grunt.loadNpmTasks('grunt-contrib-sass');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-string-replace');
    //任务注册：注册一个任务。在 default 上面注册了一个 Uglify 任务，default 就是别名，它是默认的 task，当你在项目目录执行 grunt 的时候，它会执行注册到 default 上面的任务。也就是说，当我们执行 grunt 命令的时候，uglify 的所有代码将会执行。我们也可以注册别的 task，例如：grunt.registerTask('compress', ['uglify:build']);
    grunt.registerTask('default', ['connect', 'cssmin', 'string-replace', 'watch']);
//    grunt.registerTask('jcmin', ['cssmin', 'string-replace']);
    grunt.registerTask('live', ['watch']);

}


