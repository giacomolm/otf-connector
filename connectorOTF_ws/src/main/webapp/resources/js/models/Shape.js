define(["jquery", "underscore", "backbone", "jel"],
  function ($, _, Backbone, Jel) {
    var Shape = Backbone.Model.extend({
      defaults: {
          id: undefined,
		  url : undefined,
		  x : 0,
		  y : 0,
		  width : 0,
		  height : 0,
		  props : undefined, //properties associated to shape, retrieved with the help of the meta-element attribute
		  el : undefined, //represent the grafical element associated
		  metaelement : undefined, // if an xsd is attached, represents the corresponding element,
		  xsi : undefined,
		  type : "base",
		  shapes : undefined, //if the shape is composed, it's composed of subelement, included in the shapes attr
		  name : undefined, //name (alias) associated to the shape
		  elements: new Object(), //elements related to the current shape
      },
      
      initialize: function(shape){
		if(shape instanceof Shape){
			this.id = shape.id;
			this.url = shape.url;
			this.x = shape.x;
			this.y = shape.y;
			this.props = shape.props;
			this.el = shape.el;
			this.metaelement = shape.metaelement;
			this.xsi = shape.xsi;
			this.name = shape.name;
			this.width = shape.width;
			this.height = shape.height;
			this.type = shape.type;
		}
      },

      setId: function(id){
      	this.id = id;
      },
      
      setImage: function(url){
		this.url = url;
		//this.set("url", url);
      },
      
      setProperties: function(props){
      	//if props are explicitily passed, are used
      	if(props) {
      		this.props = _.clone(props); 
      	}
		else if(Jel.xsdFile){ //otherwise we check if an XSD file was passed
				var result= xsdAttr.getAttributes(Jel.xsdFile,this.metaelement);
				this.props = new Object();
				for(var propName in result) {
				    if(result.hasOwnProperty(propName)) {
					//define its value as an empty string
					this.props[propName] = result[propName];   
				    }
				}
			}
      },
      
      setPosition: function(x,y){
		this.x = x;
		this.y = y;
      },
      
      //element is a string
      //xsi indicate if the element is a complex type, and contain the tag that includes the element
      setMetaelement: function(element, xsi){
		this.metaelement = element;
		if(xsi){
			this.xsi = xsi;
		}
		this.setProperties();
      },

      setName: function(name){
      	this.name = name;
      },
      
      setType: function(type){
      	this.type = type;
      },

      setAsComposed: function(){
		this.type = "composed";
      },

      setDimension: function(width,height){
      	this.width = width;
      	this.height = height;
      },

      setXsi: function(xsi){
      	this.xsi = xsi;
      },

      //Label contains the attribute of the element that contains a textual refence
      addElement : function(name, label, url, width, height, shiftX, shiftY){
      	if(!this.elements) this.elements = new Object();
      	this.elements[name] = new Object();
      	this.elements[name].url = url;
      	this.elements[name].label = label;
      	this.elements[name].width = width;
      	this.elements[name].height = height;
      	this.elements[name].shiftX = shiftX;
      	this.elements[name].shiftY = shiftY;
      	this.elements[name].setBehaviour = function(eventName, definition){
      		if(!this.behaviour) this.behaviour = new Object();
      		this.behaviour[eventName] = definition;
      	}
      	return this.elements[name];
      },
      
      isComposed: function(){
		if(this.type && this.type == "composed") return true;
		else return false;	
      },

      updateProp: function(propName, propValue){
      	this.props[propName] = propValue;
      	$(this.el).trigger("propsChanged",[propName, propValue]);
      },

      //Set a shape standard behaviour: it's executed every time a shape attribute (under props) is changed
      setBehaviour: function(definition){
      	this.definition = definition;
      },

      //Exporting raw object
      //see here http://stackoverflow.com/questions/10262498/backbone-model-tojson-doesnt-render-all-attributes-to-json
    	toJSON: function(options) {
    	  var shape = new Object();
    	  shape.id = this.id;
		  shape.url = this.url;
		  if(this.el){
		  	shape.x = this.el.attrs["x"];
		  	shape.y = this.el.attrs["y"];
		  }
		  else{
			  shape.x = this.x;
			  shape.y = this.y;
		  }

		  shape.props = _.clone(this.props);
		  console.log(this.props, JSON.stringify(shape.props))
		  shape.metaelement = this.metaelement;
		  shape.xsi = this.xsi;
		  shape.name = this.name;
		  shape.type = this.type;
		  shape.width = this.width;
		  shape.height = this.height;
		  if(this.shapes){
		  	var i;
		  	shape.shapes = new Array(this.shapes.length);
		  	for(i=0; i<this.shapes.length; i++)
		  		shape.shapes[i] = this.shapes.at(i).toJSON(); 
		  }
		  return _.clone(shape);
		}

      });

    return Shape;

  });
