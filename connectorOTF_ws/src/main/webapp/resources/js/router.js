define(["jquery", "underscore", "backbone", "models/Log", "collections/Shapes", "collections/Connections","views/canvasView", "jel", "scrollbar", "utils", "toastr", "views/menuView", "views/paletteView", "views/tabView", "views/propertiesView", "views/dslView", "views/dialogView", "views/notificationView", "views/treeView", "views/anteprimaView", "views/outputView"],
    function ($, _,Backbone, Log, Shapes, Connections, canvasView, Jel, scrollbar, Utils, toastr, menuView, paletteView, tabView, propertiesView, dslView, dialogView, notificationView, treeView, anteprimaView, outputView) {

    var AppRouter = Backbone.Router.extend({

      routes: {
        "": "index",
		"props/:id" : "changeProperties",
		"canvas/:id" : "getCanvas",
		"tab/:id" : "changeTab",
		"text": "convert",
		"save": "saveFile",
		"load": "load",
		"notificate/:word" : "openNotification",
		"import" : "import",
		"exportSVG" : "exportSVG",
		"exportXML" : "exportXML",
		"addShape/:id": "addShape",
		"deleteShape/:id" : "deleteShape",
		"closeTab/:id" : "deleteTab",
		"addConnection" : "addConnection",
		"deleteConnection/:id" : "deleteConnection", 
		"deleteConnections/:id" : "deleteConnections",
		"validate/:ts": "validate",
		"checkStatus/:id/:ts" : "checkStatus"
      },

      initialize: function (paletteShapes, canvasShapes, connections,canvas) {
      	//currentView will contain main views of the application: in this case are canvas and dslTextEditor
		this.currentView = undefined;

		Jel.Canvases = this.contents = []; //will contain all the main content active view, like canvas and xml editor
		
		//make the user defined shapes as default
		this.paletteShapes = paletteShapes;
		this.canvasShapes = canvasShapes;
		//setting the default connections collection
		this.connections = connections;

		//set the initial canvas
		this.canvas = canvas;
		      
		//adding the menu interface
		this.menuView = new menuView(this.canvasShapes, this.connections);
		$('#menu').append($(this.menuView.el));

		//initialize the editor dialog
		this.dialog = new dialogView();

		this.notification = new notificationView();
		$('#notification').append($(this.notification.el));
		      
		this.tabView = new tabView();
		$('#tab').append($(this.tabView.el));
		      
		//adding the palette with the default shapes
		this.paletteView =new paletteView(this.paletteShapes);
		$('#palette').append($(this.paletteView.el));
		//setting perfect scrollbar in order to manage in a better way the overflow
		$('#basepalette').perfectScrollbar({ wheelPropagation: true, useBothWheelAxes : true});
		$('#composedpalette').perfectScrollbar();
		      
		//adding the default text editor view
		this.dslView = new dslView();
		$('#dsl').append($(this.dslView.el));

		//initializing the scrollbar on the empty container
		$('#properties_ul').perfectScrollbar();
                
                //initiliazing the ouput  view, alias the sandbox
                var logModel = new Log();
                logModel.fetch();
                this.outputView = new outputView({model : logModel});
                $('#sandbox').append($(this.outputView.el));
                
		//this.addCustomEvents();

      },
     
      //like the main function: intitilize the canvas view with the right value
      index: function(){	
      	//if the canvas was specified in the init 
      	if(! this.canvas){
			this.canvas = new canvasView(this.paletteShapes, this.canvasShapes, this.connections, 0, 0);
		}
		//Root canvas is specified in also in position 0: it's useful when we need the root during the execution
		this.contents[this.canvas.id] = this.contents[0] = this.canvas;
		this.tabView.addTab(this.canvas.id, "canvas"+this.tabView.tabs.length);
		Jel.Canvas = this.canvas;
		this.changePage(this.canvas);
		//we cannot initialize the tree editor in the initialice since it can be replaced in the loading phase
		this.treeView = new treeView({collection:this.canvasShapes, canvas: this.canvas});
		$('#tree').empty();
		$('#tree').append($(this.treeView.el));
		$('#tree').perfectScrollbar();


		this.refresh();
      },
      
      addCustomEvents: function(){
		$(document).change(function(ev) {
				//Utils.appRouter.getCurrentView().trigger("change", ev);
		}, this);	
       },
	
	   //It's called when a composed shape it's explored: we have to create a new canvas or we have to reopen it
       //@id related to composed shape that we are searching or creating 
       getCanvas: function(id){
      	//if i'm exploding a composed shape, i've to create a new canvas
      	var currentComposed;
      	if(this.currentView && this.currentView.canvasShapes && (currentComposed = this.currentView.canvasShapes.get(id))){
      		//we need to get the shape from the previous canvas, where the composed shape is placed on 
			var currentComposed = this.currentView.canvasShapes.get(id); 
			if(currentComposed && currentComposed.isComposed()){	
				if(!this.tabView.inTab(currentComposed.canvas)){
					//it's the first that i'm exploding a composed shape, so let's create a new canvas
					this.createCanvas(currentComposed, id);
				}
				else{
					//if the element is yet in the tabs, we don't need to create a new tab
					this.changeTab(currentComposed.canvas);
				}
			}
			else{
				//TO MODIFY: we need a strategy in order to treat the root canvas in a separate way
				if(this.contents[id]){
					if(this.tabView.inTab(id)|| this.canvasShapes.getShape(id).isComposed()) this.changeTab(id);
				}
			}
		}
		//the canvas is yet created and opened, we've to change tab
		else if(this.contents[id]){
			this.changeTab(id);
		}
		//otherwise, if we re-opening a composed shape
		else{
			//we have to search the right shape in the canvas and reopen it
			var currentComposed = this.canvasShapes.getShape(id);	
			if(currentComposed && currentComposed.isComposed()){
				//the current composed shape was exploded yet, so we have to reopen its canvas
				this.createCanvas(currentComposed, id);
			}
			else{
				//we're reopening the main canvas
				this.canvas = undefined;
				this.index();
			}
		}
      },

      //create a new canvas starting from the shape we're exploding
      //shape that we are exploding
      //id of the parent element
      createCanvas : function(shape, id){
      		var  currentComposed = shape;

      		if(!currentComposed.shapes) currentComposed.shapes = new Shapes();
      		
			this.canvas = new canvasView(this.paletteShapes, currentComposed.shapes, this.connections, id, this.canvas.id);
			//add this canvas to the current collection of existing canvas
			this.contents[this.canvas.id] = this.canvas;
			currentComposed.canvas = this.canvas.id
			//if the canvas we're creating has an id props, we use it for the tab name
			if(currentComposed.props && currentComposed.props.id) this.tabView.addTab(this.canvas.id, currentComposed.props.id);
			else this.tabView.addTab(this.canvas.id,"canvas"+this.tabView.getHlength()); //otherwise we use 'canvas#'
			//Setting as current main canvas
			Jel.Canvas = this.canvas;
			this.changePage(this.canvas);

			//initializing graphical element is we are reopening an existing canvas
			var i;
			for(i=0; i<this.canvas.canvasShapes.length; i++){
				this.changeProperties(this.canvas.canvasShapes.at(i));
				this.checkStatus(this.canvas.canvasShapes.at(i).id, undefined, undefined, undefined,  this.canvas.canvasShapes.at(i).parentCanvas);
			}


      },
      
      changeTab: function(id){	
      	//if the current view has an id different      	
		var current = this.contents[id];
		//i need to bind the original event handler
		if(current instanceof canvasView) Jel.Canvas = this.canvas = current;

		this.changePage(current);

		var i;
		for(i=0; i<this.canvas.canvasShapes.length; i++){
			this.changeProperties(this.canvas.canvasShapes.at(i));
		}
		
		this.tabView.changeTab(id);
      },

      deleteTab: function(id){
      	delete this.contents[id];
      	this.changeTab(this.tabView.getLatestTab());
      },
      
      changeProperties: function(shapeId){
      	//if currentView is undefined, it means that we have to create it
		if(this.currentView == undefined) this.index();
		var currentModel = Jel.Canvas.canvasShapes.get(shapeId);
		if(currentModel) {
			$('#properties').empty();
			this.propertiesView = new propertiesView({model : currentModel});
			$('#properties').append($(this.propertiesView.el));
			$('#properties_ul').scrollTop(0);
			$('#properties_ul').perfectScrollbar('update');
		}
      },

      convert: function(){
      	//Conversion is possible only if the user has defined a basefile and a baseelement
      	if(Jel.baseFile && Jel.baseElement){
			var conversionRes;
			//Conversion phase: the result of conversion is contained in conversionRes
			if(Jel.wrapper) conversionRes = Jel.convert(Jel.baseFile, Jel.baseElement, Jel.wrapper);
			else conversionRes = Jel.convert(Jel.baseFile, Jel.baseElement, undefined);

			this.dslView.setText(conversionRes);
			
			var validateRes = Jel.validate(conversionRes, Jel.getSchema());
			this.notification.warning(validateRes);
			//if it's the first conversion, we need to add the dsl editor to the main div
			if(!this.contents[this.dslView.id]){
				this.contents[this.dslView.id] = this.dslView;
				this.tabView.addTab(this.dslView.id,"result.xml");	
				this.changePage(this.dslView);			
			}
			else this.changeTab(this.dslView.id);
			//Codemirror doesn't refresh its context after changes, so we do manually
			this.dslView.refresh();
		}
		else console.log("You have to configure an xsd file and at least a base file that will wrap your code");
      },

      validate: function(){
      	if(this.dslView && (this.dslView.getText()!="")){
      		var validateRes = Jel.validate(this.dslView.getText(), Jel.getSchema());
			this.notification.warning(validateRes);
      	}
      	else this.notification.warning("Convert your draw before validating it");
      },

      liveValidate: function(){
      		if(Jel.liveValidation){
      			var conversionRes;
				//Conversion phase: the result of conversion is contained in conversionRes
				if(Jel.wrapper) conversionRes = Jel.convert(Jel.baseFile, Jel.baseElement, Jel.wrapper);
				else conversionRes = Jel.convert(Jel.baseFile, Jel.baseElement, undefined);
				
				var validateRes = Jel.validate(conversionRes, Jel.getSchema());
				// Clears the current list of toasts
				toastr.clear();
				if(validateRes.indexOf('validates') != -1)
					toastr.success(validateRes);
				else {
					var i=0;
					var messages = validateRes.split(":") ;
					
					for(i=5; i<messages.length; i+=5)
						toastr.warning(messages[i-1]+messages[i].split(".")[0]);
				}
			}
      },

      saveFile: function(){
      	this.dialog.file(this.canvasShapes, this.connections);
      },

      load: function(){
      	//Jel.input contains the object obtained in the loading phase
      	if(Jel.input){
			var currentShapes = Jel.input["shapes"];
			var currentConnections = Jel.input["connections"];

			var shapes = new Shapes();
			shapes.createShapes(currentShapes);
			var connections = new Connections();
			connections.createConnections(currentConnections);

			//re-estabilsh the new canvas shapes and connection
			this.canvasShapes = Jel.canvasShapes = shapes;
			this.connections = Jel.connections =connections;
	      	this.canvas = undefined;
	      	//remove all previous tab, due the fact that we are loading another draw
	      	this.tabView.closeAllTabs();
	      	//we have to delete all the current contents, so we've to empty the current contents
	      	this.contents = [];
	      	//like a restart
	      	this.index();
	    }
      },

		addShape: function(id){
			this.canvasShapes.trigger("addShape");
			this.changeProperties(id);
			this.refreshAnteprima();
			this.liveValidate();
		},

		deleteShape: function(id){
			
			var parent_canvas = this.canvas.canvasShapes.get(id).parentCanvas;
			//delete the graphicalElement of the currentCanvas!
			this.canvas.canvasShapes.get(id).el.removeShape();
			//delete from canvasShapes
			this.canvas.canvasShapes.remove(id);
			this.deleteConnections(id);
			this.canvasShapes.trigger("deleteShape");
			this.refreshAnteprima();
			//we have to delete relations with its ancestor
			this.checkStatus(id, undefined, undefined, undefined,  parent_canvas);

			
		},

		addConnection: function(){
			this.refreshAnteprima();
		},

		deleteConnections: function(id){
			var i;	
			var toRemove = new Backbone.Collection();
			//I can't search and remove collection member at the same time
			for(i=0; i<this.connections.length; i++){
				if(this.connections.at(i).inbound == id || this.connections.at(i).outbound ==id){
					this.connections.at(i).el.remove();
					toRemove.add(this.connections.at(i));
				}
			}

			for(i=0; i<toRemove.length; i++){
				this.connections.remove(toRemove.at(i));
			}

			delete toRemove;
			this.refreshAnteprima();
		},

		//Delete a single collection, based on id of the connection
		deleteConnection: function(id){
			var i;
			//I'm removing only on connections, so i don't need of an array of copies
			for(i=0; i<this.connections.length; i++){
				if(this.connections.at(i).getId() == id){
					this.connections.at(i).el.remove();
					this.connections.remove(id);
				}
			}
			this.refreshAnteprima();
		},

		openNotification: function(word){
			if(word == "info"){
				this.notification.info();
			}
		},

		refreshAnteprima : function(){
			$('#anteprima').empty();
			this.anteprima = new anteprimaView();
			$('#anteprima').append($(this.anteprima.el));

			this.anteprima.arrange(this.canvasShapes, this.connections);
		},

		refresh: function(){
			var j;
			this.refreshAnteprima();
			//we need to initialize also properties, useful after an import  operation
			for(j=0; j<this.canvasShapes.length; j++){
				this.changeProperties(this.canvasShapes.at(j).id)
			}
		},

		import: function(){
			if(Jel.importValue){
				var input = Jel.xmlImport();
				//Jel.input = input;
				this.index();
			}
		},

		exportSVG : function(){
			this.anteprima.exportSVG(this.contents[0].paper);
		},

		exportXML : function(){
			//if the current container is composed of a dslView, i get it
			if(!this.dslView || (this.dslView.getText()=="")) this.convert();
			this.dialog.xml(this.dslView.getText());
		},

		//perform custom user operation on the current canvas (root canvas or composed shape)
		//sourceId contains the id referring to the modified shape
		checkStatus: function(sourceId, ts, curr_canv, prev_canv, parent_canvas){
			var i,source_shape, parent_shape, previous_canvas = prev_canv || this.canvas.previousCanvas,
											  current_canvas =  curr_canv || this.canvas.id;	

			//get the composed shape, 
			if(parent_canvas){
				parent_shape = this.contents[previous_canvas].canvasShapes.get(parent_canvas);
			}
			else{
				source_shape = this.contents[current_canvas].canvasShapes.get(sourceId);	
				parent_shape = this.contents[previous_canvas].canvasShapes.get(source_shape.parentCanvas);
			}

			if(parent_shape){
				var palette_sh = Utils.searchShape(this.paletteShapes, "name", parent_shape.name);
				//temporanely removing additional graphical elements
				parent_shape.el.removeElements();
				if(palette_sh && palette_sh.definition) palette_sh.definition(parent_shape, sourceId);
			}
			

			/*var previous_shape = Utils.searchShape(this.contents[curr_shape.parentCanvas], "id", curr_shape.parentCanvas);*/			
			if(parent_shape){
				//console.log(this.contents[previous_canvas].previousCanvas);
				this.checkStatus(parent_shape.id, 0, parent_shape.parentCanvas, this.contents[previous_canvas].previousCanvas);
			}
			else this.liveValidate();
		},

		changePage: function(page){
			$('#main').empty();
			this.currentView = page;
			$('#main').append($(this.currentView.el));
		}
      

    });

    return AppRouter;

  });
