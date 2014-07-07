!function (name, definition) {
    if (typeof define == 'function' && define.amd) define(definition)
    else this[name] = definition()
}('xsdAttr', function() {		        
			var xsdAttr = window.xsdAttr= new Object();		
				
			xsdAttr.fn = xsdAttr.prototype = {
				getAttributes: function(file, attributeName){
					var http; //contains the xmlhttprequest, in a cross way
					
					if (window.XMLHttpRequest){
						xhttp=new XMLHttpRequest();
					}
					else{// for IE 5/6
						xhttp=new ActiveXObject("Microsoft.XMLHTTP");
					}
					
					xhttp.open("GET",file,false);
					xhttp.send();
					
					var txt = xhttp.response || xhttp.responseText;
	
					var xmlDoc;
					if (window.DOMParser){
					  parser=new DOMParser();
					  xmlDoc=parser.parseFromString(txt,"text/xml");					  
					}
					else{ // Internet Explorer
					  xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
					  xmlDoc.async=false;
					  xmlDoc.loadXML(txt);
					} 
					
					var result = xsdAttr.fn.fetchElement(xmlDoc, attributeName, new Object());
					//console.log(attributeName, result);
					return result;
				},
				
				//Internet Explorer doesn't fully support query selector: it returns only the parent element, withuout childs
				//xmlDoc refers to xml document
				//tag name refers to potential tags containing the attribute value
				//attribute containt the value related to the name of the element
				selectElement: function(xmlDoc, tagname, attribute){
					var elements = xmlDoc.getElementsByTagName(tagname);
					var j;
					for(j=0; j<xmlDoc.getElementsByTagName(tagname).length; j++){
						var element = xmlDoc.getElementsByTagName(tagname)[j];
						if(element.getAttribute("name") == attribute) return element;
					} 
				},
				
				fetchElement: function(xmlDoc, elName, result, originalDoc){
					//getting the element with elName
					var xmlElement = xmlDoc.querySelector("[name='"+elName+"']");
					if(xmlElement){
						var xmlQuery;
						
						//refers to attribute element
						if(xmlElement.getElementsByTagName('attribute').length > 0){ //chrome case
							xmlQuery = xmlElement.getElementsByTagName('attribute');
						}
						else if(xmlElement.getElementsByTagName('xs:attribute').length > 0){//firefox,
							xmlQuery = xmlElement.getElementsByTagName('xs:attribute');
						}
						else if(((window.ActiveXObject+"").substring(0,9))!="undefined"){//IE
							xmlQuery = xsdAttr.fn.selectElement(xmlDoc, "xs:complexType",elName);
							if(!xmlQuery){
								xmlQuery = xsdAttr.fn.selectElement(xmlDoc, "xs:element",elName);

								if(xmlQuery.getAttribute("type")) xsdAttr.fn.fetchElement(xmlDoc, xmlQuery.getAttribute("type").split(":")[1], result)
							}
						}
						else{//firefox,
							xmlQuery = xmlElement.getElementsByTagName('xs:attribute');
						}						

						//if the xmlQuery referred to attribute tag contains some element, then grab its attribute 
						if(xmlQuery){
							var k=0;
							for(k=0; k<xmlQuery.length; k++){
								var name = xmlQuery[k].attributes.name;
								result[name.value] = undefined;											
							}	
						}						

						/*
						CHOICE TAG DISABLED
						var xmlChoice;
						if(xmlElement.getElementsByTagName('choice').length > 0){ //chrome case
							xmlChoice = xmlElement.getElementsByTagName('choice');
						}
						else{
							xmlChoice = xmlElement.getElementsByTagName('xs:choice');
						}

						for(j=0; j<xmlChoice.length; j++){
							if(xmlChoice[j].children){
								for(k=0; k<xmlChoice[j].children.length; k++){					
									if(result.choice == undefined){
										result.choice= [];
										result.choice[0] = xmlChoice[j].children[k].attributes[0].nodeValue.split(":")[1];
									}
									else{							
										result.choice[result.choice.length];	
										result.choice[k] = xmlChoice[j].children[k].attributes[0].nodeValue.split(":")[1];
									}
								}
							}					
						}*/

						//Checking if exists same extention tag
						var xmlExtension;
						if(xmlElement.getElementsByTagName('extension').length > 0){ //chrome case
							xmlExstension = xmlElement.getElementsByTagName('extension');
						}
						else{
							xmlExstension = xmlElement.getElementsByTagName('xs:extension');
						}
						
						//if it exists, we can grab attributes of child elements
						if(xmlExstension[0]){
							var ext = xmlExstension[0].attributes[0].nodeValue;
							var ext_split = ext.split(":")[1] || ext;
							xsdAttr.fn.fetchElement(xmlDoc, ext_split, result); //i'm splitting because 
						}

						//if the attribute is of complex type, we have to explode it
						if(xmlQuery && (xmlQuery.length==0)&&(xmlExstension.length==0)){
							if(xmlElement.attributes[1] && xmlElement.attributes[1].nodeValue){
								xsdAttr.fn.fetchElement(xmlDoc, xmlElement.attributes[1].nodeValue.split(":")[1], result);
							}
						}
						
						//Checking if the ELEMENT has sub elements with complex type: if it is composed,
						//then we have to re-create the structure of inner tag
						if(xmlElement.getElementsByTagName('sequence').length > 0){ //chrome case
							xmlQuery = xmlElement.getElementsByTagName('sequence');
						}
						else if(xmlElement.getElementsByTagName('xs:sequence').length > 0){//firefox LINUX,
							xmlQuery = xmlElement.getElementsByTagName('xs:sequence');
						}
						else if(((window.ActiveXObject+"").substring(0,9))!="undefined"){//IE
							xmlQuery = xsdAttr.fn.selectElement(xmlDoc, "xs:complexType",elName);
							if(!xmlQuery){
								xmlQuery = xsdAttr.fn.selectElement(xmlDoc, "xs:sequence",elName);
								if(xmlQuery && xmlQuery.getAttribute("type")) xsdAttr.fn.fetchElement(xmlDoc, xmlQuery.getAttribute("type").split(":")[1], result)
							}
						}
						else{//firefox,
							xmlQuery = xmlElement.getElementsByTagName('xs:sequence');
						}

						
						//if sub elements exist, we have to check if they have complex type
						if(xmlQuery && xmlQuery.length>0 && !xmlElement.getAttribute('abstract')){ //Jel cannot manage abstract element (with abstract 0== true)){

							if(xmlQuery[0].childNodes && xmlQuery[0].childNodes.length >0){

								//xmlQuery =  xmlQuery[0].childNodes
								//Firefox and IE case: take only the first sequence element, the outermost
								var j, found = false;
								for(j=0; j<xmlQuery.length; j++){
									if(xmlQuery[j].nodeName == "xs:element" || xmlQuery[j].nodeName == "element"){
										found = true;
									}
								}
								if (!found){
									xmlQuery =  xmlQuery[0].childNodes
								}
							}
						
							var i=0;
							for(i=0; i<xmlQuery.length; i++){
								//fetch all the element node
								var currentEl  = xmlQuery[i];
								//Chrome case
								/*if(xmlQuery[i].nodeName=="xs:complexType"){
									currentEl = xmlQuery[i].parentElement;
								}*/


								if(currentEl.nodeName=="xs:element"){
									if(currentEl.getAttribute("type")){
										//base type, usually starting with xs
										if(currentEl.getAttribute("type").split(":")[0] == "xs"){
											if(result instanceof Array){
												if(!result[0]) result[0] = new Object();
												
												if(currentEl.getAttribute("maxOccurs") && currentEl.getAttribute("maxOccurs") == "unbounded"){
													result[0][currentEl.getAttribute("name")] = new Array();
												}
												else  result[0][currentEl.getAttribute("name")] = ""; //empty string or undefined?
											}
											else{
												if(currentEl.getAttribute("maxOccurs") && currentEl.getAttribute("maxOccurs") == "unbounded"){
													result[currentEl.getAttribute("name")] = new Array();
												}
												else  result[currentEl.getAttribute("name")] = ""; //empty string or undefined?
											}
										  
									    }
									    //otherwise, if the type is composed, we have to explode it and respect its strucure
									    else if(!currentEl.getAttribute("minOccurs") || currentEl.getAttribute("minOccurs") != 0){
									    	
											if(currentEl.getAttribute("maxOccurs") && currentEl.getAttribute("maxOccurs") == "unbounded"){
												result[currentEl.getAttribute("name")] = new Array();
												result[currentEl.getAttribute("name")].compound = true;
												xsdAttr.fn.fetchElement(originalDoc || xmlDoc,currentEl.getAttribute("type"), result[currentEl.getAttribute("name")]);
											}
											else{
												result[currentEl.getAttribute("name")] = new Object();
												
												xsdAttr.fn.fetchElement(originalDoc || xmlDoc,currentEl.getAttribute("type"), result[currentEl.getAttribute("name")]);
											}
										}
										if(result[currentEl.getAttribute("name")] && currentEl.getAttribute("maxOccurs") && currentEl.getAttribute("maxOccurs") == "unbounded"){
										  	 result[currentEl.getAttribute("name")].unbounded = true;
										}
										//console.log(currentEl.getAttribute("name"),result[currentEl.getAttribute("name")] instanceof Array)
									}
									else{
									   	result[currentEl.getAttribute("name")] = new Object();
									   	//Smart way to obtain a document starting from the currentEl
									    var oSerializer = new XMLSerializer();
										var sXML = oSerializer.serializeToString(currentEl);
										var oParser = new DOMParser();
										var oDOM = oParser.parseFromString(sXML, "text/xml");
										//we need to pass also original xmlDoc parameter, as it can be useful later
									   	xsdAttr.fn.fetchElement(oDOM,currentEl.getAttribute("name"), result[currentEl.getAttribute("name")], xmlDoc);
									}
								}
							}
						}
						//end Element Case	

						return result;
					}
				}
			}		
				
			//shim
			xsdAttr.getAttributes = xsdAttr.fn.getAttributes;
			
			return xsdAttr;
	}
);