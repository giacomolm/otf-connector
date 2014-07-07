QUnit.config.autostart = false;


QUnit.test( "Empty Test", function( assert ) {

    var xhttp=new XMLHttpRequest();
    xhttp.open("GET","xml/quot.xml",false); //REPLACE YOUR FILE WITH YOUR INPUT FILE
    xhttp.send();
    Jel.importValue = xhttp.responseText;  

    Jel.Router.import();

    var quot = Jel.Canvases[0].canvasShapes.at(0);

    assert.ok( quot.props.actions.output.length == 2, "Passed!" );

    assert.ok( quot.props.actions.input == undefined, "Passed!" );

});