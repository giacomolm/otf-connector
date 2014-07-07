define(["jquery", "underscore", "backbone", "ractive", "xsdAttr", "jel", "text!templates/properties.html", "text!templates/propertyHeader.html"],
    function ($, _, Backbone, Ractive, xsdAttr, Jel, template, header) {

    var propertiesView = Backbone.View.extend({
	
	events :{
		"keyup input" : "updateProps",
        "click .add" : "addProperty",
        "click .remove" : "removeProperty",

	},
	    
        initialize: function (shapeId){
            this.shapeId = this.model.id;            
            this.render();
            //$(this.el).on("keydown", {context : this}, this.updateProps);
        },

        updateProps: function(ev){
            var i,curr_path = ev.target.id.split(","), curr_attr;
            var context = this; //||ev.data.context;
            curr_attr = context.model.props;

            for(i=0; i<curr_path.length-1; i++){
                if(curr_attr) curr_attr = curr_attr[curr_path[i]];
            }

            if(curr_attr && curr_path[i])
                curr_attr[curr_path[i]] = ev.target.value;

            var j, curr_label=context.model.props;
            for(j=0; j<curr_path.length-2; j++){
                if(curr_label) curr_label = curr_label[curr_path[j]];
            }

            //we need to update also the graphical element associated, is there is something to update
            var curr_el;
            if(curr_el = context.getPaletteElement("name",context.model.name)){

                if(curr_el.elements && curr_el.elements[curr_path[j]]){ //this element isn't included in an array
                    context.model.el.updateElement(curr_el.elements[curr_path[j]], ev.target)
                }
                else if(curr_el.elements && curr_el.elements[curr_path[j-1]]){ //Array case, where we have *put.[0].label
                    context.model.el.updateElement(curr_el.elements[curr_path[j-1]], ev.target)
                }
            }

            Backbone.history.navigate("checkStatus/"+context.model.id+"/"+(new Date()).getTime(), {trigger:true});
        },

        addProperty: function(ev){
            var i,curr_path = ev.target.id.split(","), curr_attr = this.model.props;
            for(i=0; i<curr_path.length; i++){
                if(curr_attr) curr_attr = curr_attr[curr_path[i]];
            }

            var new_el = _.clone(curr_attr[curr_attr.length-1]);
            curr_attr.push(new_el);
            this.render();
            Backbone.history.navigate("checkStatus/"+this.model.id+"/"+(new Date()).getTime(), {trigger:true});
        },

        removeProperty: function(ev){
            var i,curr_path = ev.target.id.split(","), curr_attr = this.model.props;

                for(i=0; i<=curr_path.length-2; i++){
                    if(curr_attr) curr_attr = curr_attr[curr_path[i]];
                }
                var curr_ind = parseInt(curr_path[curr_path.length-1]);
                curr_attr.splice(curr_ind, 1);
                this.render();
                Backbone.history.navigate("checkStatus/"+this.model.id+"/"+(new Date()).getTime(), {trigger:true});
        },

        getPaletteElement: function(attr_name, value){
            var i;
            for(i=0; i<Jel.paletteShapes.length; i++){
                if(Jel.paletteShapes.at(i)[attr_name] == value)
                    return $.extend(true, {}, Jel.paletteShapes.at(i))
                    //return Jel.paletteShapes.at(i);
            }
            return undefined;
        },
	
        render: function (eventName) {
            $(this.el).empty();
            this.number = new Object();
            this.model.el.removeElements();

            for(var propName in this.model.props){
                if(this.model.props.hasOwnProperty(propName)){
                    var curr_prop = new Object();
                    curr_prop.id = propName;
                    curr_prop.name = propName;
                    if(this.model.props[propName] instanceof Array){
                        this.template = new Ractive({el : $(this.el), template: header, data: curr_prop, append:true});
                    } 
                    else if(this.model.props[propName] instanceof Object){
                        curr_prop.class = "object";
                        this.template = new Ractive({el : $(this.el), template: header, data: curr_prop, append:true});
                        this.subRender(this.model.props[propName], propName);
                    } 
                    else{
                        curr_prop = _.clone(this.model.props);
                        //we need to redefine some properties, as the clone process can override fields
                        curr_prop.id = propName;
                        curr_prop.name = propName;
                        curr_prop.value = this.model.props[propName];
                        this.template = new Ractive({el : $(this.el), template: template, data : curr_prop, append:true});
                    }
                }
                this.updateCanvas(this.model.props, propName)
            }

            return this;
        },

        subRender: function(model, parentName, parentType){

            for(var propName in model){
                if(model.hasOwnProperty(propName)){
                    //model.name = propName;
                    if(model[propName] instanceof Array){
                        //the attribute unbounded allows to understand if sub elements can be added at runtime, with the + option
                        var curr_prop = new Object();
                        curr_prop.id = parentName+","+propName;
                        curr_prop.name = propName;
                        curr_prop.class = "array";
                        curr_prop.unbounded = true;
                        
                        this.template = new Ractive({el : $(this.el), template: header, data: curr_prop, append:true});
                        this.subRender(model[propName], parentName+","+propName, "Array");
                    }
                    else if(model[propName] instanceof Object){
                        var curr_prop = new Object();
                        curr_prop.id = parentName+","+propName;
                        curr_prop.name = propName;
                        curr_prop.class = "sub_object";
                        curr_prop.unbounded = model[propName].unbounded;
                        if(parentType && parentType=="Array") curr_prop.minus = true;
                        this.template = new Ractive({el : $(this.el), template: header, data: curr_prop, append:true});
                        this.subRender(model[propName], parentName+","+propName);
                    } 
                    else if(propName !='unbounded'){ //Base element, it's not an object, neither an Array
                        var curr_prop = new Object();
                        curr_prop.id = parentName+","+propName;
                        curr_prop.name = propName;
                        curr_prop.value = model[propName];                        
                        if(parentType && parentType=="Array") curr_prop.minus = true;
                        this.template = new Ractive({el : $(this.el), template: template, data : curr_prop, append:true});
                    }
                }
                this.updateCanvas(model,propName, parentName+","+propName);
            }
        },

        updateCanvas: function(model, propName, id){
            var curr_el;
            if(curr_el = this.getPaletteElement("name",this.model.name)){
                if(curr_el.elements && curr_el.elements[propName]){

                    
                    //if(this.model.shapes) this.model.el.removeElements();

                    curr_el.elements[propName].id = id
                    //we're adding graphically elements to the canvas
                    
                    if(model[propName] instanceof Array ){
                        if(model[propName].length>0)
                            this.model.el.addElement(curr_el.elements[propName], model[propName].length, model[propName], true, curr_el.elements[propName].behaviour)
                    }                        
                    else this.model.el.addElement(curr_el.elements[propName], 1, model[propName], false, curr_el.elements[propName].behaviour);
                }
            }
        }
       
      });

    return propertiesView;

  });
