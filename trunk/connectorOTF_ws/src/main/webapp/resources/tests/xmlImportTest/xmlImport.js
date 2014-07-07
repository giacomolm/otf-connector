QUnit.config.autostart = false;


QUnit.test( "Import Test", function( assert ) {

    var xhttp=new XMLHttpRequest();
    xhttp.open("GET","../../xml/result.xml",false);
    xhttp.send();
    Jel.importValue = xhttp.responseText;  

    Jel.Router.import();

    //var conversionRes = Jel.convert(Jel.baseFile, Jel.baseElement, undefined);

    Jel.Router.convert();

    assert.ok( Jel.importValue == Jel.Router.dslView.getText(), "Passed!" );
});