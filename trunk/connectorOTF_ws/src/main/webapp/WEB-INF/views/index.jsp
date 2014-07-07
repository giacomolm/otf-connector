<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<title>Jel</title>

<link rel="stylesheet" href="css/codemirror.css" type="text/css" media="screen">
<link rel="stylesheet" href="css/bootstrap.css" type="text/css" media="screen">
<link rel="stylesheet" href="css/jstree/jstree.min.css" type="text/css" media="screen">
<link rel="stylesheet" href="css/style.css" type="text/css" media="screen">
<link rel="stylesheet" href="css/scrollbar.css" type="text/css" media="screen">
<link rel="stylesheet" href="css/raphael.pan-zoom.css" type="text/css" media="screen">
<link rel="stylesheet" href="css/toastr.css" type="text/css" media="screen">

<script type="text/javascript" src="lib/jel/jel.js"></script>
<script data-main="js/main" src="lib/require/require.js"></script>
<script type="text/javascript" src="js/cabac.js"></script>

</head>

<body>
	<div id="menu"></div>
	<div id="sandbox"></div>
	<div id="dialog"></div>
	<div id="notification"></div>
	<div id="middle">
		<div id="palette"><div id="paletteheader"><h5>Palette</h5></div></div>
		<div id="tab"></div>
		<div id="container">
			<div id="main" ondragover="event.preventDefault()"></div>
			<div id="dsl"></div>
		</div>
		<div id="rightMenu">
			<div id="treeheader"><h5>Tree</h5></div>
			<div id="tree" class="jstree jstree-1 jstree-default jstree-default-responsive" style="position: relative; overflow:hidden"></div>
			<div class="properties"><h5>Properties</h5><div id="prop_container"><div id="properties_ul" style="position:relative"><ul id="properties"></ul></div></div></div>
			<div id="anteprimaheader"><h5>Preview</h5></div>
			<div id="anteprima"></div>
		</div>
	</div>
	
</body>
</html>
