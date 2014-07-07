define("raphaelext", ['raphael'], function(){
	Raphael.fn.connection = function (id, obj1, obj2, line, bg, context) {

	    if (obj1.line && obj1.from && obj1.to) {
		line = obj1;
		obj1 = line.from;
		obj2 = line.to;
	    }
	    var bb1 = obj1.getBBox(),
		bb2 = obj2.getBBox(),
		p = [{x: bb1.x + bb1.width / 2, y: bb1.y - 1},
		{x: bb1.x + bb1.width / 2, y: bb1.y + bb1.height + 1},
		{x: bb1.x - 1, y: bb1.y + bb1.height / 2},
		{x: bb1.x + bb1.width + 1, y: bb1.y + bb1.height / 2},
		{x: bb2.x + bb2.width / 2, y: bb2.y - 1},
		{x: bb2.x + bb2.width / 2, y: bb2.y + bb2.height + 1},
		{x: bb2.x - 1, y: bb2.y + bb2.height / 2},
		{x: bb2.x + bb2.width + 1, y: bb2.y + bb2.height / 2}],
		d = {}, dis = [];
	    for (var i = 0; i < 4; i++) {
		for (var j = 4; j < 8; j++) {
		    var dx = Math.abs(p[i].x - p[j].x),
			dy = Math.abs(p[i].y - p[j].y);
		    if ((i == j - 4) || (((i != 3 && j != 6) || p[i].x < p[j].x) && ((i != 2 && j != 7) || p[i].x > p[j].x) && ((i != 0 && j != 5) || p[i].y > p[j].y) && ((i != 1 && j != 4) || p[i].y < p[j].y))) {
			dis.push(dx + dy);
			d[dis[dis.length - 1]] = [i, j];
		    }
		}
	    }
	    if (dis.length == 0) {
		var res = [0, 4];
	    } else {
		res = d[Math.min.apply(Math, dis)];
	    }
	    var x1 = p[res[0]].x,
		y1 = p[res[0]].y,
		x4 = p[res[1]].x,
		y4 = p[res[1]].y;
	    dx = Math.max(Math.abs(x1 - x4) / 2, 10);
	    dy = Math.max(Math.abs(y1 - y4) / 2, 10);
	    var x2 = [x1, x1, x1 - dx, x1 + dx][res[0]].toFixed(3),
		y2 = [y1 - dy, y1 + dy, y1, y1][res[0]].toFixed(3),
		x3 = [0, 0, 0, 0, x4, x4, x4 - dx, x4 + dx][res[1]].toFixed(3),
		y3 = [0, 0, 0, 0, y1 + dy, y1 - dy, y4, y4][res[1]].toFixed(3);
	    var path = ["M", x1.toFixed(3), y1.toFixed(3), "C", x2, y2, x3, y3, x4.toFixed(3), y4.toFixed(3)].join(",");
	    
	    var size = 5; //image size in px
	    triangle =  ["M", x4, y4, "L", (x4  - size),(y4  - size),"L" ,(x4  - size),(y4  + size),"L",x4 ,y4];
	    var angle = ((bb2.x+bb2.width)<bb1.x) ? 180 : ((bb2.x>bb1.x+bb1.width) ? 0 : ((bb2.y<bb1.y)?270:90));
	    

	    if (line && line.line) {
		line.bg && line.bg.attr({path: path});
		line.line.attr({path: path});
		line.tri.attr({path: triangle});
		line.tri.transform("r"+angle);
	
	    } else {
		var color = typeof line == "string" ? line : "#000";
		var obj = {
			id : id,
		    bg: bg && bg.split && this.path(path).attr({stroke: bg.split("|")[0], fill: "none", "stroke-width": bg.split("|")[1] || 3}),
		    line: this.path(path).attr({stroke: color, fill: "none"}),
		    tri: this.path(triangle).attr({stroke: color,"fill":"#000"}), 
		    remove : (function(){				
				   if (this && this.line) {					   
					this.line.remove();
					this.tri.remove();
				   }
				}),
		    from: obj1,
		    to: obj2
		};
		obj.tri.mousedown(function(event){
				obj.line.attr("stroke", "#aaa");
				obj.tri.attr("stroke", "#aaa");
			}
		);

		obj.tri.mouseup(function(event){
				obj.line.attr("stroke", "#000");
				obj.tri.attr("stroke", "#000");
			}
		);

		obj.tri.click(function(event){
			$(obj).trigger("moving",[event.clientX, event.clientY]);
		});

		obj.tri.contextmenu(function(event){
			//clean the current canvas from other pending context menu
			context.paper.canvas.dispatchEvent(new CustomEvent('click'));
			// the context menu must be over other canvas elements
			obj.menu.toFront();
			obj.menu.show(event.layerX,event.layerY);
			event.preventDefault(true);
			event.stopImmediatePropagation();
		});

		obj.menu = this.menu([{"name":"removeArrow", "type":"base"}],context, obj);

		obj.tri.transform("r"+angle);
		if(angle == 90) obj.tri.translate(-2,-2);

		return obj;
	    }
	};
	
	Raphael.fn.getCanvas = function(){
		return this.canvas;
	};
	
	Raphael.fn.bind = function(eventType, definition){
		$(this.getCanvas()).bind(eventType, definition);
	};
	
	//Update the element current position when is dragger into the canvas
	Raphael.fn.dragger = function () {
		this.ox = this.type == "ellipse" ? this.attr("cx") : this.attr("x");
		this.oy = this.type == "ellipse" ? this.attr("cy") : this.attr("y");
		
		this.animate({"fill-opacity": .2}, 500);
	};
	
    Raphael.fn.up = function () {
		this.animate({"fill-opacity": 0}, 500);
    };
	
	Raphael.fn.moveShape = function (context) {	
	    return function(dx, dy){
		var att = this.type == "ellipse" ? {cx: this.ox + dx, cy: this.oy + dy} : {x: this.ox + dx, y: this.oy + dy};
		
		//if the element is part of a complex system, we hat o propagate for all element in the parent
		if(this.elements){
			this.elements.forEach(function (el) {
				el.attr(att);
				if(el.getTransform()) el.transform(el.getTransform());			
			});
		}
		this.attr(att); // if no parent is defined, set the current coords for the current element 
		
		var connections = context.connections;		
		for (var i = connections.length; i--;) {
		    context.paper.connection(connections.at(i).id, connections.at(i).el);
		}
		context.paper.safari();
	    }
    };
	
	Raphael.fn.shape = function(url, x, y, width, height, context, definition){
		var shapeEl = context.paper.image(url, x, y, width, height);
		
				//enabling drag events only on the palette element
		shapeEl.drag(context.paper.moveShape(context), context.paper.dragger, context.paper.up);
		
		shapeEl.mouseover(function(ev){
			this.elements.forEach(function (el) {					
				if(el.isHideable()) el.transform(el.getTransform());			
				clearTimeout(el.timeout);////clear a potential pending timeout event
				el.show();	
				
			});
			//if(context.arrowActive.active) this.animate({"opacity": .4}, 800); //when the element is selected
		});
			
		shapeEl.mouseout(function(ev){
			this.elements.forEach(function (el) {
				if(el.isHideable()) el.timeout = setTimeout(function(el){el.hide()},2000,el);
			});
			//if(context.arrowActive.active) this.animate({"opacity": 1}, 200);
		});
			
		//click event binding on shape element
		shapeEl.mousedown(definition(context, shapeEl));	

		//dbclick event binding
		shapeEl.setDblclick = function(definition){
			shapeEl.dblclick(definition(context, shapeEl));
		};
		
		shapeEl.elements = context.paper.set();

		shapeEl.removeShape = function(){
			var i;
			for(i=0; i<shapeEl.elements.length; i++){
				shapeEl.elements[i].remove();
			}
			shapeEl.remove();
		};

		shapeEl.menu = this.menu([{"name":"removeShape", "type":"base"},
								  {"name":"addArrow", "type":"base"},
								  {"name":"removeArrows", "type":"base"}
								 ], 
								 context, shapeEl);
		shapeEl.contextmenu(function(event){
			//clean the current canvas from other pending context menu
			context.paper.canvas.dispatchEvent(new CustomEvent('click'));
			// the context menu must be over other canvas elements
			shapeEl.menu.toFront();
			shapeEl.menu.show(event.layerX,event.layerY);
			event.preventDefault(true);
		});

		shapeEl.additionalEl = context.paper.set();
		shapeEl.label = context.paper.set();
		//adding graphical element in addition to the main one
		shapeEl.addElement = function(obj, number, labels, is_array, definition){ 
			if(!number) number = 1;
			var i;

			for(i=0; i<Math.min(2,number); i++){
				var curr_el = context.paper.shapeElement(obj.url, this.attrs.x, this.attrs.y, obj.width, obj.height, 0, context, undefined, (this.additionalEl.length*(obj.width+10))+obj.shiftX, obj.shiftY);
				if(is_array)				
					curr_el.id = curr_el.node.id = obj.id+','+i;
				else curr_el.id = curr_el.node.id = obj.id;

				curr_el.transform(curr_el.getTransform());
				this.elements.push(curr_el);
				this.additionalEl.push(curr_el)

				for(var eventName in definition){
					$(curr_el.node).on(eventName, definition[eventName])
				}
				//Adding a label with the element
				if(labels){
					var curr_txt; 
					if(labels[i]) curr_txt = context.paper.shapeText(labels[i][obj.label], undefined,  curr_el.getBBox().x+5, curr_el.getBBox().y-6,this, context,false);
					else curr_txt = context.paper.shapeText(labels[obj.label], undefined, curr_el.getBBox().x+5, curr_el.getBBox().y-6, this, context,false);
					curr_txt.id = curr_el.id+",label";
					//forcing the transform: maybe there is a bug in Raphael with chrome and ie
					curr_txt.setInitTransform();
					this.elements.push(curr_txt);
					this.label.push(curr_txt);
				}
			}
			if(number>2){
				var url_path = obj.url.split('.'), url_attr="", k;
				for(k=0; k<url_path.length-1; k++){
					url_attr+=url_path[k];
				}
				if(obj.url.split('.').length>0) url_attr = url_attr+'_right.'+url_path[k];
				else url_attr = obj.url;

				var curr_el = context.paper.shapeElement(url_attr, this.attrs.x, this.attrs.y, obj.width+20, obj.height, 0, context, undefined, (this.additionalEl.length*(obj.width+10))+obj.shiftX, obj.shiftY);
				if(is_array)				
					curr_el.id = curr_el.node.id = obj.id+','+(number-1);
				else curr_el.id = curr_el.node.id = obj.id;
				curr_el.transform(curr_el.getTransform());
				this.elements.push(curr_el);
				this.additionalEl.push(curr_el)

				for(var eventName in definition){
					$(curr_el.node).on(eventName, definition[eventName])
				}

				//Adding a label with the element
				if(labels){
					var curr_txt; 
					if(labels[number-1]) curr_txt = context.paper.shapeText(labels[number-1][obj.label], undefined,  curr_el.getBBox().x+25, curr_el.getBBox().y-6,this, context,false);
					else curr_txt = context.paper.shapeText(labels[obj.label], undefined, curr_el.getBBox().x+5, curr_el.getBBox().y-6, this, context,false);
					curr_txt.id = curr_el.id+",label";

					this.elements.push(curr_txt);
					this.label.push(curr_txt);
				}
			}

			//call the definition for the add element
			if(definition && definition['add']) definition['add']();
		}

		shapeEl.updateElement = function(obj, ev){
			var context = this;

			this.label.forEach(function(e){
					if(e.id == ev.id){
						e.update(ev.value);
					}
			});
			/*
			FOR ALL ADDITIONAL ELEMENTS !!
			this.additionalEl.forEach(function(e){
					if(e.id == obj.id) console.log(e)
			});

			this.elements.forEach(function(e){
					if(e.id == obj.id) console.log(e);
			});*/

		}

		shapeEl.removeElements = function(){
			var context = this;
			this.additionalEl.forEach(function(e){
				context.elements.forEach(function(l){
					if(e.id == l.id)
						l.remove();
				});
			    e.remove()
			});

			this.label.forEach(function(e){
				context.elements.forEach(function(l){
					if(e.id == l.id)
						l.remove();
				});
			    e.remove()
			});

			this.additionalEl.splice(0,this.additionalEl.length);
			this.label.splice(0,this.label.length)

		}
		
		return shapeEl;
	};

	//add a menu related to a canvas element
	Raphael.fn.shapeElement = function(uri, x, y, width, height,imgWidth, context, definition, shiftx, shifty){

		var element = context.paper.image(uri, x, y, width, height);//"img/arrow.png", shape.x, shape.y, 21, 25);	

		if(definition) element.mousedown(definition(context));

		element.hideable = false;
		element.isHideable = function(){
			return element.hideable;
		};

		element.getTransform = function(){
			if(shiftx) return "t"+(imgWidth+shiftx)+","+(shifty-10);
			return "t"+imgWidth+",-10";
		};
		
		return element;
	};

	//add a menu related to a canvas element
	Raphael.fn.shapeMenu = function(uri, x, y, width, height,imgWidth, context, definition, shiftx, shifty, id){

		var element = context.paper.image(uri, x, y, width, height);//"img/arrow.png", shape.x, shape.y, 21, 25);	

		element.mousedown(definition(context, id));
		
		element.mouseover(function(ev){			
			clearTimeout(this.timeout);
		});
		
		element.mouseout(function(ev){			
			this.timeout = setTimeout(function(el){el.hide()},2000,this);
		});
		
		element.hideable = true;
		element.isHideable = function(){
			return element.hideable;
		};

		if(element.isHideable()) element.hide();

		element.getTransform = function(){
			if(shiftx) return "t"+(imgWidth+shiftx)+","+(shifty-10);
			return "t"+imgWidth+",-10";
		};
		
		return element;
	};

	Raphael.fn.shapeText = function(text, value, x, y, shapeEl, context, transform){
		var original_text = text;
		var element, shiftx=x-shapeEl.attrs.x,shifty=y-shapeEl.attrs.y;
		if(value) element = context.paper.text(x,y, text+": "+value);
		else element = context.paper.text(x,y, text);
		if(transform){
			element.attr({"transform" : "t"+shapeEl.attrs['width']/3+","+(shapeEl.attrs['height']+16)});
			//Raphael Bug #491. Works on IE ?
			$('tspan', element.node).attr('dy', -8);
			shiftx = shapeEl.attrs['width']/3;
			shifty = (shapeEl.attrs['height']+4);
		}

		//refresh the default attribute in the canvas
		$(shapeEl).on("propsChanged", 
					function(ev, name, value){					
						if(name =='id'){
							if(value!="") element.attr({text : text+': '+value});
							else element.attr({text : text});
							if(element.getTransform()) element.transform(element.getTransform());			
						}
					}
		);

		element.hideable = false;
		element.isHideable = function(){
			return false;
		};

		element.getTransform = function(){
			return "t"+shiftx+","+shifty;
		};

		element.setInitTransform = function(){
				this.transform("t0,0");
		};

		

		element.update = function(new_value){
			this.attr('text',new_value);
		};	
				
		return element;
	};
	
	Raphael.fn.wrap = function(context, targetId, definition){
		return definition
	},

	Raphael.fn.menu = function (itemList, context, obj){
		var raphael = context.paper;
		//Constructing the context menu based on RaphaelJS
		obj.menuEl = raphael.set();
		//Box simulating the context menu
		var box = raphael.rect(0,0,100,25*itemList.length);
		box.attr({fill:'#EEE'});
		box.node.setAttribute("id", "contextMenu");
		obj.menuEl.push(box);

		var i;
		for(i=0; i<itemList.length; i++){
			var boxtext = raphael.text(0,20,itemList[i].name);
			boxtext.type = 'text'; 
			boxtext.name = boxtext.node.name = itemList[i].name;
			boxtext.attr({ "font-size": '10pt', "font-family": "Consolas, Arial, Helvetica, sans-serif" });
			boxtext.node.setAttribute("id", "textMenu");
			boxtext.click(function(event){
							//here i can trigger the arrow active event
							$(obj).trigger(event.target.parentElement.name, [obj.id]);
						}
			);
			if(itemList[i].type == "composed"){
				boxtext.box = raphael.menu(itemList[i].childs, context, boxtext);
				boxtext.box.hide();
				boxtext.mouseover(function(e){
					if(boxtext.box[0].node.style.display == "none"){
						boxtext.box.toFront();
						boxtext.box.show(e.layerX, e.layerY);
					}
				});
				boxtext.mouseout(function(e){
					 boxtext.box.timeout = setTimeout(function(){boxtext.box.hide()},2000);
					}
				);

				boxtext.box.mouseover(function(e){
					clearTimeout(boxtext.box.timeout);
				});

				boxtext.box.mouseout(function(e){
					 boxtext.box.timeout = setTimeout(function(){boxtext.box.hide()},2000);
					}
				);
			}
			obj.menuEl.push(boxtext);
		}
		
		obj.menuEl.hide = function(){
			obj.menuEl.forEach(function(el){
				el.hide();
				//if the element is composed of drop-down menu, we've to hide also them
				if(el.box) el.box.hide();
			});
		};
		//obj.menuEl.parentShow = this.el.show;
		
		obj.menuEl.show = function(posx,posy){	
			var i=0;
			obj.menuEl.forEach(function(el){
				if(el.type == 'text'){
					el.attr({x:(posx+40),y:posy+10+(20*(i++))});
				}
				else  el.attr({x:posx,y:posy});
				el.show();
				}
			);
			
			//this.node.style.display = "block";
		};

		obj.menuEl.hide();
		
		raphael.safari();
		
		return obj.menuEl;
	};
	
	return Raphael;
});