QUnit.config.autostart = false;


QUnit.test( "Empty Test", function( assert ) {

    var xhttp=new XMLHttpRequest();
    xhttp.open("GET","xml/conj.xml",false); 
    xhttp.send();
    Jel.importValue = xhttp.responseText;  

    Jel.Router.import();

    var conj = Jel.Canvases[0].canvasShapes.at(0);

    assert.ok( conj.props.actions.input != undefined, "Passed!" );

    assert.ok( conj.props.actions.output == undefined, "Passed!" );

});