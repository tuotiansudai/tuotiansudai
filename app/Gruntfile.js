module.exports = function(grunt) {

  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    uglify: {
      options: {
        banner: '/*! <%= pkg.name %> <%= grunt.template.today("yyyymmddHHMM") %> */\n'
      },
      build: {
        src: 'src/<%= pkg.name %>.js',
        dest: 'build/<%= pkg.name %>.min.<%= grunt.template.today("yyyymmddHHMM") %>.js'
      }
    },
    // Metadata.
        meta: {
            basePath: '../',
            srcPath: 'assets/sass/',
            deployPath: 'assets/css/'
        },
    // Task configuration.
        sass: {
            dist: {
                // files: {
                //     '<%= meta.deployPath %>style.css': '<%= meta.srcPath %>style.scss'
                // },
                files : [
                  {
                    expand: true,     // Enable dynamic expansion.
                    cwd: '',      // Src matches are relative to this path.
                    src: ['<%= meta.srcPath %>*.scss'], // Actual pattern(s) to match.
                    dest: 'dist/css',   // Destination path prefix.
                    ext: '.css',   // Dest filepaths will have this extension.
                    extDot: 'first',   // Extensions in filenames begin after the first dot
                    flatten : true
                  }
                ],
                options: {
                    sourcemap: 'true'
                }
            }
        },
        watch: {
            scripts: {
                files: [
                    '<%= meta.srcPath %>/**/*.scss'
                ],
                tasks: ['sass']
            }
        },
      // Task clean  
         clean: {
            dist: {
              files: [{
                dot: true,
                src: [
                  '.tmp',
                  'build/*',
                  'dist/',
                  '<%= meta.deployPath %>/*',
                  '!<%= meta.deployPath %>/.git*'
                ]
              }]
            },
            // server: '.tmp'
          },
        
        cssmin: {
            dist: {
              // files: {
              //   '<%= meta.deployPath %>style.min.<%= grunt.template.today("yyyymmddHHMM") %>.css': '<%= meta.deployPath %>style.css',
              
              // }
              files : [
                  {
                    expand: true,     // Enable dynamic expansion.
                    cwd: '',      // Src matches are relative to this path.
                    src: ['dist/css/*.css'], // Actual pattern(s) to match.
                    dest: 'dist/',   // Destination path prefix.
                    ext: '.min.<%= grunt.template.today("yyyymmddHHMM") %>.css',   // Dest filepaths will have this extension.
                    extDot: 'first' ,  // Extensions in filenames begin after the first dot
                    flatten : true
                  }
                ],
            }
          }



  });

  

  // 加载包含 "uglify" 任务的插件。

  // grunt.loadNpmTasks('grunt-contrib-uglify');
  // grunt.loadNpmTasks('grunt-contrib-sass');
  // grunt.loadNpmTasks('grunt-contrib-watch');


  // load all grunt tasks
  require('matchdep').filterDev('grunt-*').forEach(grunt.loadNpmTasks);

  // 默认被执行的任务列表。
  grunt.registerTask('default', ['clean','uglify','sass','cssmin']);

};
