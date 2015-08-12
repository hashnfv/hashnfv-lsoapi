module.exports = function (grunt) {

  grunt.initConfig({
    less: {
      app: {
        files: {
          'app/views/css/vcpe.css': 'app/views/less/vcpe.less'
        }
      }
    },

    watch: {
      less: {
        files: 'app/views/less/*.less',
        tasks: ['css']
      }
    }
  });

  grunt.loadNpmTasks('grunt-contrib-less');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.registerTask('css', ['less']);
  grunt.registerTask('default', ['css']);

};
