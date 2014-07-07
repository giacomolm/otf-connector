!function (name, definition) {
    if (typeof define == 'function' && define.amd) define(definition)
    else this[name] = definition()
}('utils', function() {		        
			var Utils = new Object();

			//search a shape in the shapes collection with the id 
			Utils.searchShape = function(shapes, propName, propValue){
				var i, curr_shape=undefined;

				for(i=0; i<shapes.length; i++){
					if(shapes.at(i)[propName] == propValue) return shapes.at(i)
					if(shapes.at(i).shapes) curr_shape = Utils.searchShape(shapes.at(i).shapes, propValue);
					if(curr_shape) return curr_shape;
				}
				return undefined;
			}
			
			return Utils;
	}
);