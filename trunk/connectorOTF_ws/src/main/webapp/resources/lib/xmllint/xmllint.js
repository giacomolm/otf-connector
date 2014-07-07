!function (name, definition) {
    if (typeof define == 'function' && define.amd) define(definition)
    else this[name] = definition()
}('xmllint', function() {

	var xmllint = new Object();

	xmllint.validateXML = function(xml, schema) {
	  var Module = new Object();
	  Module.xml= xml;
	  Module.schema= schema;
	  Module.arguments= ["--noout", "--schema", "file.xsd", "file.xml"];
	  
  
  	  return Module["return"];
	};
	return xmllint;
});