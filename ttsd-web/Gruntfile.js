module.exports = function (grunt) {
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        // Metadata.
        meta: {
            basePath: '../',
            baseSassPath: 'src/main/webapp/style/sass',
            baseCssPath: 'src/main/webapp/style',
            baseCssMinPath: 'src/main/webapp/style/dest',
            baseJsPath: 'src/main/webapp/js',
            baseJsMinPath: 'src/main/webapp/js/dest'
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
            }
        },
        uglify: {
            options: {
                // banner: '/*! <%= pkg.name %> <%= grunt.template.today("yyyymmddHHMM") %> */\n'
            },
            dist: {
                files: [
                    {
                        expand: true,     // Enable dynamic expansion.
                        cwd: '',      // Src matches are relative to this path.
                        src: ['<%= meta.baseJsPath %>/*.js'], // Actual pattern(s) to match.
                        dest: '<%= meta.baseJsMinPath %>/',   // Destination path prefix.
                        ext: '.min.js',   // Dest filepaths will have this extension.
                        extDot: 'first',   // Extensions in filenames begin after the first dot
                        flatten: true
                    }
                ]

            }
        },
        sass: {
            dist: {
                files: [
                    {
                        expand: true,     // Enable dynamic expansion.
                        cwd: '',      // Src matches are relative to this path.
                        src: ['<%= meta.baseSassPath %>/*.scss'], // Actual pattern(s) to match.
                        dest: '<%= meta.baseCssPath %>/',   // Destination path prefix.
                        ext: '.css',   // Dest filepaths will have this extension.
                        extDot: 'first',   // Extensions in filenames begin after the first dot
                        flatten: true
                    }
                ]
            }
        },
        cssmin: {
            dist: {
                files: [
                    {
                        expand: true,     // Enable dynamic expansion.
                        cwd: '',      // Src matches are relative to this path.
                        src: ['<%= meta.baseCssPath %>/*.css'], // Actual pattern(s) to match.
                        dest: '<%= meta.baseCssMinPath %>/',   // Destination path prefix.
                        ext: '.min.css',   // Dest filepaths will have this extension.
                        extDot: 'first',  // Extensions in filenames begin after the first dot
                        flatten: true
                    }
                ]
            }
        },
         watch: {
             sass: {
                 files: [
                     '<%= meta.baseSassPath %>/*.scss'
                 ],
                 tasks: ['clean:css','sass']
             },
             cssmin:{
                 files:[
                     ['<%= meta.baseCssPath %>/*.css']
                 ],
                 tasks: ['cssmin']
             },
             uglify:{
                 files:[
                     ['<%= meta.baseJsPath %>/*.js']
                 ],
                 tasks: ['clean:js','uglify']
             }
         }
    });

    // load all grunt tasks
    require('matchdep').filterDev('grunt-*').forEach(grunt.loadNpmTasks);

    // 默认被执行的任务列表。
    grunt.registerTask('default', ['clean', 'uglify', 'sass', 'cssmin','watch']);

};
