define(["jquery", "underscore", "backbone", "ractive", "text!templates/output.html"],
    function ($, _, Backbone, Ractive, template) {

    var outputView = Backbone.View.extend({
	
    	events :{

    	},
    	    
        initialize: function(){ 
            setInterval(this.fetch, 3000, this);
            this.render();
        },
        
        fetch: function(context){
            
            $.ajax({
                    url: "http://localhost:8080/sprint_at/log",
                })
                .done(function( data ) {                    
                    context.log = { 'output' : data };
                    context.render();
            });
        },

        render: function (eventName) {            
            this.template = new Ractive({el : $(this.el), template: template, data : this.log});           
            
            return this;
        },
       
      });

    return outputView;

  });