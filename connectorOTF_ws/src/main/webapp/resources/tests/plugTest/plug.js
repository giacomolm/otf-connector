QUnit.config.autostart = false;


QUnit.test( "Plug Test", function( assert ) {

    var xhttp=new XMLHttpRequest();
    xhttp.open("GET","xml/plug.xml",false);
    xhttp.send();
    Jel.importValue = xhttp.responseText;  

    Jel.Router.import();

    //getting the current canvas shape (plug), starting from canvas 0 containing the root
    var plug = Jel.Canvases[0].canvasShapes.at(0);

    assert.ok( plug.props.actions.input == undefined, "Passed!" );

    assert.ok( plug.props.actions.output != undefined, "Passed!" );
});