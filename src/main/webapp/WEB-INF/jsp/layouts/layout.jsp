<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <!--
        ===
        This comment should NOT be removed.

        Charisma v2.0.0

        Copyright 2012-2014 Muhammad Usman
        Licensed under the Apache License v2.0
        http://www.apache.org/licenses/LICENSE-2.0

        http://usman.it
        http://twitter.com/halalit_usman
        ===
    -->
    <meta charset="utf-8">
    <title>Oracle SOA Suite 12c - DBLink to SOA Test</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
    <meta name="author" content="Muhammad Usman">

    <!-- The styles -->
    <link id="bs-css" href="<c:url value='/resources/charisma-master/css/bootstrap-cerulean.min.css'/>" rel="stylesheet">
	<link href="<c:url value='/resources/charisma-master/css/font-awesome.min.css'/>" rel='stylesheet'>
    <link href="<c:url value='/resources/charisma-master/css/charisma-app.css'/>" rel="stylesheet">
	<link href="<c:url value='/resources/charisma-master/css/bootstrap-modal.css' />" rel="stylesheet">
    <link href="<c:url value='/resources/charisma-master/bower_components/fullcalendar/dist/fullcalendar.css'/>" rel='stylesheet'>
    <link href="<c:url value='/resources/charisma-master/bower_components/fullcalendar/dist/fullcalendar.print.css'/>" rel='stylesheet' media='print'>
    <link href="<c:url value='/resources/charisma-master/bower_components/chosen/chosen.min.css'/>" rel='stylesheet'>
    <link href="<c:url value='/resources/charisma-master/bower_components/colorbox/example3/colorbox.css'/>" rel='stylesheet'>
    <link href="<c:url value='/resources/charisma-master/bower_components/responsive-tables/responsive-tables.css'/>" rel='stylesheet'>
    <link href="<c:url value='/resources/charisma-master/bower_components/bootstrap-tour/build/css/bootstrap-tour.min.css'/>" rel='stylesheet'>
    <link href="<c:url value='/resources/charisma-master/css/jquery.noty.css'/>" rel='stylesheet'>
    <link href="<c:url value='/resources/charisma-master/css/noty_theme_default.css'/>" rel='stylesheet'>
    <link href="<c:url value='/resources/charisma-master/css/elfinder.min.css'/>" rel='stylesheet'>
    <link href="<c:url value='/resources/charisma-master/css/elfinder.theme.css'/>" rel='stylesheet'>
    <link href="<c:url value='/resources/charisma-master/css/jquery.iphone.toggle.css'/>" rel='stylesheet'>
    <link href="<c:url value='/resources/charisma-master/css/uploadify.css'/>" rel='stylesheet'>
    <link href="<c:url value='/resources/charisma-master/css/animate.min.css'/>" rel='stylesheet'>
	<link href="<c:url value='/resources/js/qtip2/jquery.qtip.min.css'/>" rel='stylesheet'>
	<link href="<c:url value='/resources/js/jplot1.0.8r1250/jquery.jqplot.min.css' />" rel="stylesheet">
	<link href="<c:url value='/resources/js/treegrid/jquery.treegrid.css' />" rel="stylesheet">
    <!-- jQuery -->
    <script src="<c:url value='/resources/charisma-master/bower_components/jquery/jquery.min.js'/>"></script>

    <!-- The HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <!-- The fav icon -->
    <link rel="shortcut icon" href="<c:url value='/resources/charisma-master/img/favicon.ico'/>">
</head>

<body>
	<tiles:insertAttribute name="header" />
	<div class="ch-container">
	    <div class="row">
			<div id="domainTree" class="col-sm-2 col-lg-2">
	        <tiles:insertAttribute name="menu" />
			</div>
			<div id="content" class="col-lg-10 col-sm-10" style="padding:0px 0px 0px 0px">
            <!-- content starts -->
	        <tiles:insertAttribute name="body" />
			</div>
	    </div><!--/fluid-row-->
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">

			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">×</button>
						<h3>Settings</h3>
					</div>
					<div class="modal-body">
						<p>Here settings can be configured...</p>
					</div>
					<div class="modal-footer">
						<a href="#" class="btn btn-default" data-dismiss="modal">Close</a>
						<a href="#" class="btn btn-primary" data-dismiss="modal">Save changes</a>
					</div>
				</div>
			</div>
		</div>
	    <tiles:insertAttribute name="footer" />
	</div><!--/.fluid-container-->
<!-- external javascript -->

<script src="<c:url value='/resources/charisma-master/bower_components/bootstrap/dist/js/bootstrap.min.js'/>"></script>

<!-- library for cookie management -->
<script src="<c:url value='/resources/charisma-master/js/jquery.cookie.js'/>"></script>
<!-- calender plugin -->
<script src="<c:url value='/resources/charisma-master/bower_components/moment/min/moment.min.js'/>"></script>
<script src="<c:url value='/resources/charisma-master/bower_components/fullcalendar/dist/fullcalendar.min.js'/>"></script>
<!-- data table plugin -->
<script src="<c:url value='/resources/charisma-master/js/jquery.dataTables.min.js'/>"></script>

<!-- modal / dialog library -->
<script src="<c:url value='/resources/charisma-master/bower_components/bootstrap/dist/js/bootstrap-modal.js'/>"></script>
<script src="<c:url value='/resources/charisma-master/bower_components/bootstrap/dist/js/bootstrap-modalmanager.js'/>"></script>
	
<!-- select or dropdown enhancer -->
<script src="<c:url value='/resources/charisma-master/bower_components/chosen/chosen.jquery.min.js'/>"></script>
<!-- plugin for gallery image view -->
<script src="<c:url value='/resources/charisma-master/bower_components/colorbox/jquery.colorbox-min.js'/>"></script>
<!-- notification plugin -->
<script src="<c:url value='/resources/charisma-master/js/jquery.noty.js'/>"></script>
<!-- library for making tables responsive -->
<script src="<c:url value='/resources/charisma-master/bower_components/responsive-tables/responsive-tables.js'/>"></script>
<!-- tour plugin -->
<script src="<c:url value='/resources/charisma-master/bower_components/bootstrap-tour/build/js/bootstrap-tour.min.js'/>"></script>
<!-- star rating plugin -->
<script src="<c:url value='/resources/charisma-master/js/jquery.raty.min.js'/>"></script>
<!-- for iOS style toggle switch -->
<script src="<c:url value='/resources/charisma-master/js/jquery.iphone.toggle.js'/>"></script>
<!-- autogrowing textarea plugin -->
<script src="<c:url value='/resources/charisma-master/js/jquery.autogrow-textarea.js'/>"></script>
<!-- multiple file upload plugin -->
<script src="<c:url value='/resources/charisma-master/js/jquery.uploadify-3.1.min.js'/>"></script>
<!-- history.js for cross-browser state change on ajax -->
<script src="<c:url value='/resources/charisma-master/js/jquery.history.js'/>"></script>
<!-- application script for Charisma demo -->
<script src="<c:url value='/resources/charisma-master/js/charisma.js'/>"></script>
<script src="<c:url value='/resources/js/default.js'/>"></script>
<script src="<c:url value='/resources/js/request.js'/>"></script>
<script src="<c:url value='/resources/js/qtip2/jquery.qtip.min.js'/>"></script>
<script src="<c:url value='/resources/js/jplot1.0.8r1250/jquery.jqplot.min.js'/>"></script>
<script src="<c:url value='/resources/js/jplot1.0.8r1250/plugins/jqplot.barRenderer.min.js'/>"></script>
<script src="<c:url value='/resources/js/jplot1.0.8r1250/plugins/jqplot.categoryAxisRenderer.min.js'/>"></script>
<script src="<c:url value='/resources/js/jplot1.0.8r1250/plugins/jqplot.pointLabels.min.js'/>"></script>
<script src="<c:url value='/resources/js/treegrid/jquery.treegrid.js'/>"></script>

<script type="text/javascript">
	//ajax_jquery_loading("<spring:url value="/soa/compositestree/listbyfilter" />", "GET", "#domainTree","", "");
	//$(function() {
		//ajax_jquery_loading("<spring:url value="/soa/compositestree/listbyfilter" />", "GET", "#domainTree","", "");
	//});
	
	dialogue = function(content, title)  {
		$('<div />').qtip({
			content: {
				text: content,
				title: title,
				button: 'Close'
			},
			position: {
				my: 'center', at: 'center',
				target: $(window)
			},
			show: {
				ready: true,
				modal: {
					on: true,
					blur: false
				}
			},
			hide: false,
			style: {
				classes: 'qtip-light qtip-rounded',
				width:1000,
				height:500
			},
			events: {
				render: function(event, api) {
					$('button', api.elements.content).click(function(e) {
						api.hide(e);
					});
				},
				hide: function(event, api) { api.destroy(); }
			}
		});
	};


	window.Alert = function() {
	  var message = $('.tooltiptext').html();

		dialogue( message, 'Query!' );
	}
	
</script>
</body>
</html>
