define(function(require, exports, module) {
	var jQuery = require('jquery');

	/***************************************************************************
	 * @title: Colour Picker
	 * 
	 * @version: 2.0
	 * @author: Andreas Lagerkvist
	 * @date: 2008-09-16
	 * @url: http://andreaslagerkvist.com/jquery/colour-picker/
	 * @license: http://creativecommons.org/licenses/by/3.0/
	 * @copyright: 2008 Andreas Lagerkvist (andreaslagerkvist.com)
	 * @requires: jquery, jquery.colourPicker.css, jquery.colourPicker.gif
	 * @does: Use this plug-in on a normal <select>-element filled with colours
	 *        to turn it in to a colour-picker widget that allows users to view
	 *        all the colours in the drop-down as well as enter their own,
	 *        preferred, custom colour. Only about 1k compressed.
	 * 
	 * @howto: jQuery('select[name="colour"]').colourPicker({ico: 'my-icon.gif',
	 *         title: 'Select a colour from the list'}); Would replace the
	 *         select with 'my-icon.gif' which, when clicked, would open a
	 *         dialogue with the title 'Select a colour from the list'.
	 * @exampleJS: jQuery('#jquery-colour-picker-example').colourPicker({ ico:
	 *             WEBROOT +
	 *             'aFramework/Modules/Base/gfx/jquery.colourPicker.gif', title:
	 *             false });
	 * @attention 再原版基础之上改动较大
	 **************************************************************************/
	jQuery.fn.colourPicker = function(conf) {
		// Config for plug

		var _colors = [ '#ffffff', '#ffccc9', '#ffce93', '#fffc9e', '#ffffc7',
				'#9aff99', '#96fffb', '#cdffff', '#cbcefb', '#cfcfcf',
				'#fd6864', '#fe996b', '#fffe65', '#fcff2f', '#67fd9a',
				'#38fff8', '#68fdff', '#9698ed', '#c0c0c0', '#fe0000',
				'#f8a102', '#ffcc67', '#f8ff00', '#34ff34', '#68cbd0',
				'#34cdf9', '#6665cd', '#9b9b9b', '#cb0000', '#f56b00',
				'#ffcb2f', '#ffc702', '#32cb00', '#00d2cb', '#3166ff',
				'#6434fc', '#656565', '#9a0000', '#ce6301', '#cd9934',
				'#999903', '#009901', '#329a9d', '#3531ff', '#6200c9',
				'#343434', '#680100', '#963400', '#986536', '#646809',
				'#036400', '#34696d', '#00009b', '#303498', '#000000',
				'#330001', '#643403', '#663234', '#343300', '#013300',
				'#003532', '#010066', '#340096' ];

		var config = jQuery.extend({
			id : 'jquery-colour-picker', // id of colour-picker container
			ico : '/images/colourPicker.gif', // SRC to colour-picker icon
			title : 'Pick a colour', // Default dialogue title
			inputBG : true, // Whether to change the input's background to the
			// selected colour's
			speed : 500, // Speed of dialogue-animation
			openTxt : '颜色选择器'
		}, conf);

		// Inverts a hex-colour
		var hexInvert = function(hex) {
			// hex --> #ff0022
			var r = hex.substr(1, 2);
			var g = hex.substr(3, 2);
			var b = hex.substr(5, 2);
			// 16进制转10进制
			// parseInt(sixteen,16)
			return (parseInt(r, 16) + parseInt(g, 16) + parseInt(b, 16)) < 400 ? '#ffffff'
					: '#000000';
		};

		// Add the colour-picker dialogue if not added
		var colourPicker = jQuery('#' + config.id);

		if (!colourPicker.length) {
			colourPicker = jQuery('<div id="' + config.id + '"></div>')
					.appendTo(document.body).hide();

			var _hidenPanel = function(event) {
				if (!(jQuery(event.target).is('#' + config.id) || jQuery(
						event.target).parents('#' + config.id).length)) {
					colourPicker.hide(config.speed);
				}
			};

			// Remove the colour-picker if you click outside it (on body)
			jQuery(document.body).click(_hidenPanel);
			jQuery(document).click(_hidenPanel);
		}

		// For every select passed to the plug-in
		return this.each(function() {
			// Insert icon and input
			// var select = jQuery(this);
			var $input = jQuery(this);
			/*
			var icon = jQuery(
					'<a href="#"><img src="' + config.ico + '" alt="'
							+ config.openTxt + '" /></a>').insertAfter($input);
			*/
			$input.attr({
				"size" : 7,
				"readonly" : "readonly"
			});

			// if($input.val()) {
			//				
			// }

			var aloc = [];
			// Build a list of colours based on the colours in the select
			for ( var c in _colors) {
				aloc.push('<li><a href="#" title="' + _colors[c] + '" rel="'
						+ _colors[c] + '" style="background: ' + _colors[c]
						+ '; colour: ' + hexInvert(_colors[c]) + ';">'
						+ _colors[c] + '</a></li>');
			}

			// If user wants to, change the input's BG to reflect the newly
			// selected colour
			if (config.inputBG) {
				$input.change(function() {
					$input.css({
						background : $input.val(),
						color : hexInvert($input.val())
					});
				});

				$input.change();
			}

			// When you click the icon or input text
			var _createColorSheet = function() {
				// Show the colour-picker next to the icon and fill it with the
				// colours in the select that used to be there
				//var iconPos = icon.offset();
				var iconPos = $input.offset();
				var heading = config.title ? '<h2>' + config.title + '</h2>'
						: '';

				colourPicker.html(heading + '<ul>' + aloc.join('') + '</ul>')
						.css({
							position : 'absolute',
							left : iconPos.left + 'px',
							top : iconPos.top + 'px',
							zIndex : 9999999
						/* FIXED ! 如果不设置在jquery ui中弹出后会被弹出框覆盖 */
						}).show(config.speed);

				// When you click a colour in the colour-picker
				jQuery('a', colourPicker).click(function() {
					// The hex is stored in the link's rel-attribute
					var hex = jQuery(this).attr('rel');

					$input.val(hex);

					// If user wants to, change the input's BG to
					// reflect the newly selected colour
					if (config.inputBG) {
						$input.css({
							background : hex,
							color : hexInvert(hex)
						});
					}

					// Trigger change-event on input
					$input.change();

					// Hide the colour-picker and return false
					colourPicker.hide(config.speed);

					return false;
				});

				return false;
			};

			//icon.on('click', _createColorSheet);
			$input.on('click', _createColorSheet);
		});
	};
});