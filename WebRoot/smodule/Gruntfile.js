module.exports = function(grunt) {
	var transport = require('grunt-cmd-transport');
	var style = transport.style.init(grunt);
	var text = transport.text.init(grunt);
	var script = transport.script.init(grunt);

	grunt.initConfig({
		pkg : grunt.file.readJSON("package.json"),

		transport : {
			options : {
				paths : [ '.' ],
				alias : '<%= pkg.spm.alias %>'
			},
			common : {
				options : {
					idleading : 'common/'
				},

				files : [ {
					cwd : 'common',
					src : '**/*',
					filter : 'isFile',
					dest : '.build/dist/common'
				} ]
			},
			jplugin : {
				options : {
					idleading : 'jplugin/'
				},

				files : [ {
					cwd : 'jplugin',
					src : '**/*',
					filter : 'isFile',
					dest : '.build/dist/jplugin'
				} ]
			},
			source : {
				options : {
					idleading : 'source/'// 模块化组合时的前缀，默认是 family/name/version
				},

				files : [ {
					cwd : 'source',
					src : '**/*',
					filter : 'isFile',
					dest : '.build/dist/source'
				} ]
			}
		},

		uglify : {
			common : {
				options : {
					banner : '/*! <%= pkg.author %> | <%= pkg.version %>*/',
					beautify : {
						ascii_only : true
					}
				},
				files : [ {
					expand : true,
					cwd : '.build/dist/',
					src : [ 'common/*.js', '!common/*-debug.js' ],
					dest : '../<%= pkg.version %>/',
					ext : '.js'
				} ]
			},
			jplugin : {
				options : {
					banner : '/*! <%= pkg.author %> | <%= pkg.version %>*/',
					beautify : {
						ascii_only : true
					}
				},
				files : [ {
					expand : true,
					cwd : '.build/dist/',
					src : [ 'jplugin/**/*.js', '!jplugin/**/*-debug.js' ],
					dest : '../<%= pkg.version %>/',
					filter : 'isFile',
					ext : '.js'
				} ]
			},
			source : {
				options : {
					banner : '/*! <%= pkg.author %> | <%= pkg.version %>*/',
					beautify : {
						ascii_only : true
					}
				},
				files : [ {
					expand : true,
					cwd : '.build/dist/',
					src : [ 'source/**/*.js', '!source/**/*-debug.js' ],
					dest : '../<%= pkg.version %>/',
					ext : '.js'
				} ]
			},
			seajs : {
				options : {
					banner : '/*! <%= pkg.author %> | <%= pkg.version %>*/',
					beautify : {
						ascii_only : true
					}
				},
				files : [ {
					expand : true,
					src : [ '../<%= pkg.version %>/sea.js' ],
					dest : '../<%= pkg.version %>/',
					ext : '.js'
				} ]
			}
		},

		copy : {
			base : {
				files : [ {
					expand : true,
					src : [ 'sea.js' ],
					dest : '../<%= pkg.version %>/'
				} ]
			}
		},

		clean : {
			build : [ '.build' ]
		}
	});

	grunt.loadNpmTasks('grunt-cmd-transport');
	grunt.loadNpmTasks('grunt-cmd-concat');
	grunt.loadNpmTasks('grunt-contrib-clean');
	grunt.loadNpmTasks('grunt-contrib-uglify');
	grunt.loadNpmTasks('grunt-contrib-copy');

	grunt.registerTask('default', [ 'transport:common', 'transport:jplugin',
			'transport:source', 'uglify:common', 'uglify:jplugin',
			'uglify:source', 'copy:base', 'uglify:seajs', 'clean' ]);
};