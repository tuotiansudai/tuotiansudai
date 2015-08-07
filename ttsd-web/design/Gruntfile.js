module.exports = function(grunt) {

  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    // Metadata.
        meta: {
            basePath: '../',
            baseCss: 'static/style/sass',
            baseCssdist : 'static/style/dest',
            baseJs: 'static/js'
        },
        uglify: {
          options: {
            // banner: '/*! <%= pkg.name %> <%= grunt.template.today("yyyymmddHHMM") %> */\n'
          },
          // build: {
          //   // src: '<%= meta.baseJs %>.js',
          //   // dest: '<%= meta.baseJs %>.min.<%= grunt.template.today("yyyymmddHHMM") %>.js'
          // }
          dist :{
              files : [
                  {
                    expand: true,     // Enable dynamic expansion.
                    cwd: '',      // Src matches are relative to this path.
                    src: ['<%= meta.baseJs %>/*.js'], // Actual pattern(s) to match.
                    dest: '<%= meta.baseJs %>/dest/',   // Destination path prefix.
                    ext: '.min.js',   // Dest filepaths will have this extension.
                    extDot: 'first',   // Extensions in filenames begin after the first dot
                    flatten : true
                  }
                ]

            }
        },
    // Task configuration.
        sass: {
            dist: {
                // files: {
                //     '<%= meta.deployPath %>style.css': '<%= meta.baseCss %>style.scss'
                // },
                files : [
                  {
                    expand: true,     // Enable dynamic expansion.
                    cwd: '',      // Src matches are relative to this path.
                    src: ['<%= meta.baseCss %>/*.scss'], // Actual pattern(s) to match.
                    dest: '<%= meta.baseCssdist %>/',   // Destination path prefix.
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
        // watch: {
        //     scripts: {
        //         files: [
        //             '<%= meta.baseCss %>/*.scss'
        //         ],
        //         tasks: ['sass']
        //     }
        // },
      // Task clean  
         clean: {
            dist: {
              files: [{
                dot: true,
                src: [
                  '<%= meta.baseCssdist %>/*',
                  '<%= meta.baseJs %>/dest/*'
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
                    src: ['<%= meta.baseCssdist %>/*.css'], // Actual pattern(s) to match.
                    dest: '<%= meta.baseCssdist %>/',   // Destination path prefix.
                    ext: '.min.css',   // Dest filepaths will have this extension.
                    extDot: 'first' ,  // Extensions in filenames begin after the first dot
                    flatten : true
                  }
                ],
            }
          }



  });


  // load all grunt tasks
  require('matchdep').filterDev('grunt-*').forEach(grunt.loadNpmTasks);

  // 默认被执行的任务列表。
  grunt.registerTask('default', ['clean','uglify','sass','cssmin']);

};
