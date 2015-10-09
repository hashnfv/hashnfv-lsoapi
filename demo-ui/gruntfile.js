/*******************************************************************************
* Copyright (c) 2015 CableLabs Inc. and others.
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License, Version 2.0
* which accompanies this distribution, and is available at
* http://www.apache.org/licenses/LICENSE-2.0
*******************************************************************************/

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
