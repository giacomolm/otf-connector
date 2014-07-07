!function (name, definition) {
    if (typeof define == 'function' && define.amd) define(definition)
    else this[name] = definition()
}('Jel', function() {
			//if yet defined, return that
		    if(window.Jel) return window.Jel;
			var Jel = window.Jel= {};		
			
			//model
			Jel.Shape = {};
			// collection related to palette
			Jel.paletteShapes = {};
			//instances into the canvas
			Jel.canvasShapes = {};
			//instances of canvases connections
			Jel.connections = {};
			//contains the result string after the import process
			Jel.importValue = undefined;
				
			Jel.fn = Jel.prototype = {
				init: function(initial) {
				    console.log('Empty initialization. You have to override the Jel.fn.init method');
				},
				
				setPalette: function(palette){
					this.palette = palette;
				},
				
				attachXSD: function(file){
					this.xsdFile = file;
				},

				getSchema: function(){
					if(Jel.xsdFile){
						if (window.XMLHttpRequest){
						  xhttp=new XMLHttpRequest();
						}
						else{// code for IE5 and IE6						
						  xhttp=new ActiveXObject("Microsoft.XMLHTTP");
						}
						xhttp.open("GET",Jel.xsdFile,false);
						xhttp.send();
						return xhttp.responseText;	
					}
					else console.log('Problems with schema file. Have you loaded it?');
				},

				setBaseFile: function(filename){
					this.baseFile = filename;
				},

				setBaseElement : function(element){
					this.baseElement = element;
				},

				setXMLWrapper : function(wrapper){
					this.wrapper = wrapper;
				},

				setNamespace: function(namespace){
					this.namespace = namespace
				},
					
				//converts the current instances shapes in an xml representation
				//basefile: xml file containing the base structure of output result
				//baseElement: contain the base element where new nodes are attached
				//wrapper: if specified, a new node is created and nodes converted are appended to this element
				convert: function(baseFile, baseElement, wrapper){					
					
					var createTagStructure = function(xmlDoc, property, name){
						var i,el;
						
						if(property instanceof Object){
							el = xmlDoc.createElement(name);
							for(var propName in property) {								
								if(property[propName] instanceof Array){
									var k;
									if(property[propName].compound){
										for(k=0; k<property[propName].length; k++){
											el.appendChild(createTagStructure(xmlDoc, property[propName][k], propName));
										}
									}
									else{
										for(k=0; k<property[propName].length; k++){
											var curr_el  = xmlDoc.createElement(propName);
											curr_el.appendChild(xmlDoc.createTextNode(property[propName][k]));
											el.appendChild(curr_el);
										}
									}
								}
						        else if(property[propName] instanceof Object) {
						           el.appendChild(createTagStructure(xmlDoc, property[propName], propName));
						        }
						        else{
						        	var curr_el  = xmlDoc.createElement(propName);
									curr_el.appendChild(xmlDoc.createTextNode(property[propName]));
									el.appendChild(curr_el);
						        }
						    }
						}
						else {
							//console.log(property,name)
							el = xmlDoc.createElement(name);
							el.appendChild(xmlDoc.createTextNode(property));
						}
						return el;
					};


					//append in the xmldoc the set of shapes under the parent el
					var getXML = function(xmlDoc, parentEl, shapes){

						var i=0;
						for(i=0; i<shapes.length; i++){
							var currentXML, resultXML;
							if(shapes.at(i).metaelement){
								//checking if the xsi is defined (the xsi define the type of composed shape)
								if(shapes.at(i).xsi){
									resultXML = xmlDoc.createElement(shapes.at(i).xsi);
									resultXML.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance","xsi:type", shapes.at(i).metaelement);
									if(shapes.at(i).type && shapes.at(i).type=="composed"){
										
										for(var propName in shapes.at(i).props) {
											//if the type is composed, then with treat object childs in a separate way 

									        if(shapes.at(i).props.hasOwnProperty(propName) && !(shapes.at(i).props[propName] instanceof Object)) {
									        	var xmlChild = createTagStructure(xmlDoc, shapes.at(i).props[propName], propName);
									            resultXML.appendChild(xmlChild);   
									        }
									        else if(shapes.at(i).props.hasOwnProperty(propName) && shapes.at(i).props[propName] != "") {
									        	
									        	var xmlChild = createTagStructure(xmlDoc, shapes.at(i).props[propName], propName);
									            resultXML.appendChild(xmlChild); 
									        }
									    } 
									}
									else{
										for(var propName in shapes.at(i).props) {
									        if(shapes.at(i).props.hasOwnProperty(propName) && shapes.at(i).props[propName] != "") {
									        	var xmlChild = createTagStructure(xmlDoc, shapes.at(i).props[propName], propName);
									            resultXML.appendChild(xmlChild);   
									        }
									    }
									}
									
								}
								else{
									resultXML = xmlDoc.createElementNS(Jel.namespace, shapes.at(i).metaelement);
									for(var propName in shapes.at(i).props) {
								        if(shapes.at(i).props.hasOwnProperty(propName) && shapes.at(i).props[propName] != "") {
								            resultXML.setAttribute(propName, shapes.at(i).props[propName]);   
								        }
								    }
								}
								parentEl.appendChild(resultXML);		
								//composed case						
								if(shapes.at(i).shapes){
									getXML(xmlDoc, resultXML, shapes.at(i).shapes);	
								}
							}										
						}
					};

					//load the bas structure of the xml file
					if (window.XMLHttpRequest){
					  xhttp=new XMLHttpRequest();
					}
					else{// code for IE5 and IE6						
					  xhttp=new ActiveXObject("Microsoft.XMLHTTP");
					}
					xhttp.open("GET",baseFile,false);
					xhttp.send();
					xmlDoc = xhttp.responseXML;		

					var sortedShapes = this.topologicalSort(Jel.canvasShapes, Jel.connections);	
					
					var wrapperXML;
					if(wrapper){
						
						//in need to specify that the root element belong to the same namespace 
						var ns1 = Jel.namespace;
						wrapperXML = xmlDoc.createElementNS(ns1,wrapper); 

						getXML(xmlDoc, wrapperXML, sortedShapes);
						//console.log(baseElement);
						xmlDoc.getElementsByTagName(baseElement)[0].appendChild(wrapperXML);
							
					}
					else{
						//setting the default namespace attribute for the root element
						var rootEl = xmlDoc.getElementsByTagName(baseElement)[0];//.setAttribute('xmlns', Jel.namespace);
						rootEl.setAttribute('xmlns:xsi', "http://www.w3.org/2001/XMLSchema-instance");
						//rootEl.setAttributeNS('http://www.w3.org/2000/xmlns/', 'xmlns:xsi', 'http://www.w3.org/1999/xlink');

						getXML(xmlDoc, rootEl, sortedShapes);
					}
					
					if (window.ActiveXObject &&  xmlDoc.xml) {
						return xmlDoc.xml;
					}
					else { // code for Chrome, Safari, Firefox, Opera, etc.
						var result = (new XMLSerializer()).serializeToString(xmlDoc);
						return result;
					}
				},

				//sort the shape collection depending on graph connections
				//shapes: shapes involveed 
				//connection: input shapes collections
				topologicalSort: function(shapes, connections){
					var unmarked = shapes;
					var temporary = new Backbone.Collection();
					var marked = new Backbone.Collection();
					var list = new Backbone.Collection();

					var getOutboundEdges = function(shape){
						result = [];
						var i;
						for(i=0; i<connections.length; i++){
							if(connections.at(i).outbound == shape.id){	
								result.push(connections.at(i).inbound);
							}
						}
						return result;
					};

					var visit = function(shape){
						if(temporary.get(shape.id)) console.log("Not a DAG", shape.id);
						else{
							if(!marked.get(shape.id)){
								temporary.add(shape);
								var edges = getOutboundEdges(shape);
								var i=0;
								for(i=0; i<edges.length; i++){
									visit(shapes.get(edges[i]));
								}
								temporary.remove(shape.id);
								marked.push(shape);							
								//fast object cloning
								var newShape = new Jel.Shape(shape);
								list.unshift(newShape);
							}
						}
					};
					var i=0;
					for(i=0; i<shapes.length; i++){
						if(!temporary.get(shapes.at(i))) visit(shapes.at(i));
						//composed shape
						if(shapes.at(i).shapes){
							var shape = list.get((shapes.at(i).id));
							if(shape) shape.shapes = this.topologicalSort(shapes.at(i).shapes, connections);
						}
					}
					return list;
				},
				
				validate: function(model, metamodel) {
				    console.log('Empty valitdation Function. You have to override the validate function in this way: Jel.fn.validate = urValFunct');
				},

				xmlImport: function(wrapper, baseEl){
					var xmlDoc, xml = Jel.importValue;
					//if the building process is fast, we need do manually increment ids in order to avoid clash
					var starting_id = (new Date()).getTime();

					var setAttrs = function(props, element){
						//console.log(props)
						for(var propName in props){
														
							if(props[propName] instanceof Array){		
												
								var y=0,z;
								var curr_model = $.extend(true, {}, props[propName][0]);								
								for(z=0; z<element.length; z++){

									if(element[z].nodeName == propName){
										if(element[z].children && element[z].children.length>0){
											
											if(!curr_model[element[z].children[0].nodeName]){												
													
												var w;
												for(w=0; w<element[z].children.length; w++)
													curr_model[element[z].children[w].nodeName] = new Object();
									
											}

											var curr_result = $.extend(true, {}, setAttrs(curr_model,element[z].children));
											
											props[propName][y++]= curr_result;
										}
										else if(element[z].innerHTML){
											props[propName].push(element[z].innerHTML.trim())
										}
										else if(element[z].childNodes){

											if(element[z].childNodes.length>1){
												setAttrs(props[propName],element[z].childNodes)
											}
											else props[propName].push(element[z].childNodes[0].nodeValue.trim());
										}
									}

								}
								
							}
							else{								
								if(element.length && element.length>0){
									var z;

									for(z=0; z<element.length; z++){



										if(element[z].nodeName == propName){
											if(element[z].children && element[z].children.length>0){
												
												
												if(!props[propName][element[z].children[0].nodeName]){
													
													if(element[z].getElementsByTagName(element[z].children[0].nodeName).length>1){
														var w;
														props[propName][element[z].children[0].nodeName] = new Array();
														//setting the current element as compound - can ghave child
														props[propName][element[z].children[0].nodeName].compound = true;
														for(w=0; w<element[z].children.length; w++)
													    	props[propName][element[z].children[0].nodeName][w] = new Object();													     
													}
													else {
														var w;
														for(w=0; w<element[z].children.length; w++)
															props[propName][element[z].children[w].nodeName] = new Object();

													}													
												}

												setAttrs(props[propName],element[z].children);
											}
											else if(element[z].innerHTML){
												props[propName] = element[z].innerHTML.trim()
											}
											else if(element[z].childNodes &&  element[z].childNodes.length){
												//if the element has a number of child greater than 1, it can cointains some other elements
												if(element[z].childNodes.length>1){
													setAttrs(props[propName],element[z].childNodes)
												}
												else props[propName] = element[z].childNodes[0].nodeValue.trim();
											}
										}
									}
								}
								else{
	
								}
							}
						}
						return props;
					};

					var getChild = function(rootEl ,baseEl){
						var curr_shapes = new Array();														

						if(rootEl.length){
							var i=0;
							for(i=0; i<rootEl.length; i++){
								//if a baseEl exists, then we have to start converting from it 
								if(baseEl){
									var j, elements = rootEl[i].getElementsByTagName(baseEl);
									
									for(j=0; j<elements.length; j++){
										//we can create here the shape and adjust its attributes/innerTag
										var k, curr_shape;
										for(k=0; k<Jel.paletteShapes.length; k++){
											if(Jel.paletteShapes.at(k).metaelement == elements[j].getAttribute("xsi:type"))
												curr_shape = _.clone(Jel.paletteShapes.at(k));
												curr_shape.id = starting_id++; 	
										}

										setAttrs(curr_shape.props, elements[j].childNodes);

										if(curr_shape.isComposed()){
											curr_shape.shapes = getChild(rootEl, baseEl)
										}
									}
								}
								else{
									//otherwise we can convert any element the we encounter
								}
							}
						}
						else{
							if(baseEl){
								var j, elements = new Array();

								for(j=0; j<rootEl.childNodes.length; j++){
									if(rootEl.childNodes[j].nodeName == "term"){
										elements.push(rootEl.childNodes[j])
									}
								}

								for(j=0; j<elements.length; j++){
									//we can create here the shape and adjust its attributes/innerTag
									var k, curr_shape;
									for(k=0; k<Jel.paletteShapes.length; k++){
										if(Jel.paletteShapes.at(k).metaelement == elements[j].getAttribute("xsi:type"))
											curr_shape = $.extend(true, {}, Jel.paletteShapes.at(k));	
											if(curr_shape) curr_shape.id = starting_id++;
									}

									
									setAttrs(curr_shape.props, elements[j].childNodes);

									curr_shapes.push(curr_shape);
									
									if(curr_shape.isComposed()){
										curr_shape.shapes = getChild(elements[j], baseEl);
									}
								}
							}
						}
						return curr_shapes;
					};

					if (window.DOMParser){
					  var parser=new DOMParser();
					  xmlDoc=parser.parseFromString(xml,"text/xml");	  
					}
					else{ // Internet Explorer
					  xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
					  xmlDoc.async=false;
					  xmlDoc.loadXML(xml);
					}
					//xmlDoc = xmlDoc.documentElement;
					var rootEl;
					if(wrapper){
						rootEl = xmlDoc.getElementsByTagName(wrapper)
					}
					else rootEl = xmlDoc;

					if(rootEl.length){
						var i=0;
						for(i=0; i<rootEl.length; i++){
							//if a baseEl exists, then we have to start converting from it 
							if(baseEl){
								var j, elements = new Array();
								
								for(j=0; j<rootEl[i].childNodes.length; j++){
									if(rootEl[i].childNodes[j].nodeName == "term"){
										elements.push(rootEl[i].childNodes[j])
									}
								}
								

								for(j=0; j<elements.length; j++){
									//we can create here the shape and adjust its attributes/innerTag
									var k, curr_shape;
									for(k=0; k<Jel.paletteShapes.length; k++){
										if(Jel.paletteShapes.at(k).metaelement == elements[j].getAttribute("xsi:type"))
											curr_shape = $.extend(true, {}, Jel.paletteShapes.at(k));
											if(curr_shape) curr_shape.id = starting_id++; 	
									}

									setAttrs(curr_shape.props, elements[j].childNodes);

									if(curr_shape.isComposed()){
										var curr_childs = getChild(elements[j], baseEl);
										curr_shape.shapes = new Jel.Shapes();
										curr_shape.shapes.createShapes(curr_childs)
									}

									Jel.Canvas.addShape(curr_shape);
									
								}
							}
							else{
								//otherwise we can convert any element the we encounter
							}
						}
					}

				}
				
			};
				
			Jel.fn.init.prototype = Jel.fn;
			
			//shim
			Jel.attachXSD = Jel.fn.attachXSD;
			Jel.setBaseFile= Jel.fn.setBaseFile;
			Jel.setBaseElement= Jel.fn.setBaseElement;
			Jel.setXMLWrapper = Jel.fn.setXMLWrapper;
			Jel.convert = Jel.fn.convert;
			Jel.topologicalSort = Jel.fn.topologicalSort;
			Jel.getSchema = Jel.fn.getSchema;
			Jel.setNamespace = Jel.fn.setNamespace;
			Jel.xmlImport = Jel.fn.xmlImport;

			return Jel;
	}
);

