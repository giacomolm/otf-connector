QUnit.config.autostart = false;


QUnit.test( "Empty Test", function( assert ) {

    var xhttp=new XMLHttpRequest();
    xhttp.open("GET","xml/altern.xml",false);
    xhttp.send();
    Jel.importValue = xhttp.responseText;  

    Jel.Router.import();

    var altern = Jel.Canvases[0].canvasShapes.at(0);

    assert.ok( altern.props.actions.input != undefined, "Passed!" );

    assert.ok( altern.props.actions.output != undefined, "Passed!" );

});