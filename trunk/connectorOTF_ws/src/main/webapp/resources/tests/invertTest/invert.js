QUnit.config.autostart = false;


QUnit.test( "Empty Test", function( assert ) {

    var xhttp=new XMLHttpRequest();
    xhttp.open("GET","xml/invert.xml",false); //REPLACE YOUR FILE WITH YOUR INPUT FILE
    xhttp.send();
    Jel.importValue = xhttp.responseText;  

    Jel.Router.import();

    var invert = Jel.Canvases[0].canvasShapes.at(0);

    assert.ok( invert.props.actions.output != undefined, "Passed!" );

    assert.ok( invert.props.actions.input != undefined, "Passed!" );

});