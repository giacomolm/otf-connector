define(["jquery", "underscore", "backbone"],
  function ($, _, Backbone) {
    var Log = Backbone.Model.extend({
      defaults: {
          text: undefined,          
      },
      
      urlRoot: 'http://localhost:8080/sprint_at/log',
      
        url: function() {
            return this.urlRoot;
        },
      
      initialize: function(){
       
      },

    });
    
    return Log;

  });