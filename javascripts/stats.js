$(document).ready(function() 
    { 
		$('#stats_table').tablesorter({
		   widgets: ['zebra'],
		   widgetZebra: {css: ["odd","even"]} // css classes to apply to rows
		});
    } 
);