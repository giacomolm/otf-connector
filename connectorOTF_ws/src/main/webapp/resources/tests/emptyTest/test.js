QUnit.config.autostart = false;


QUnit.test( "Empty Test", function( assert ) {

    var xhttp=new XMLHttpRequest();
    xhttp.open("GET","xml/yourFile.xml",false); //REPLACE YOUR FILE WITH YOUR INPUT FILE
    xhttp.send();
    Jel.importValue = xhttp.responseText;  

    Jel.Router.import();

    //assert.ok( plug.props.actions.input != undefined, "Passed!" );

});