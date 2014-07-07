Jel.fn.init = function(){

		//Setting the xsd file in order to do validation
		try{
		  Jel.attachXSD('schema/cabac.xsd');
		  Jel.setBaseFile('xml/terms.xml');
		  Jel.setNamespace('http://www.w3.org/2001/XMLSchema-instance');
		  Jel.setBaseElement('terms');
		}
		catch(err){
		  console.log("Problems with http xml request");
		}

		var noop = new Jel.Shape();
		noop.setImage("img/noop.gif");
		noop.setName("Noop");
		noop.setDimension(84,55);
		noop.setMetaelement("noop", "term");	
		Jel.paletteShapes.addShape(noop);	

		var prod = new Jel.Shape();
		prod.setImage("img/prod.gif");
		prod.setName("Prod");
		prod.setDimension(102,54);
		prod.setMetaelement("prod", "term");
		var output_prod = prod.addElement("output", "label","img/arrow_up.png", 11, 27,46,-10);
		output_prod.setBehaviour('click', function(ev){ document.getElementById(ev.target.id+",label").focus()});
		Jel.paletteShapes.addShape(prod);

		var cons = new Jel.Shape();
		cons.setImage("img/cons.gif");
		cons.setName("Cons");
		cons.setDimension(102,54);	
		cons.setMetaelement("cons", "term");
		var input_cons = cons.addElement("input", "label","img/arrow_down.png", 11, 27,46,-10);
		input_cons.setBehaviour('click', function(ev){ document.getElementById(ev.target.id+",label").focus()});
		Jel.paletteShapes.addShape(cons);

		var trans = new Jel.Shape();
		trans.setImage("img/trans.gif");
		trans.setName("Trans");
		trans.setDimension(161,55);
		trans.setMetaelement("trans", "term");
		trans.addElement("input", "label","img/arrow_down.png", 11, 27,55,-10);	
		trans.addElement("output", "label","img/arrow_up.png", 11, 27,70,-10);
		Jel.paletteShapes.addShape(trans);

		var split = new Jel.Shape();
		split.setImage("img/split.gif");
		split.setName("Split");
		split.setDimension(233,54);
		split.setMetaelement("split", "term");
		split.addElement("input", "label","img/arrow_down.png", 11, 27,80,-10);	
		split.addElement("output", "label","img/arrow_up.png", 11, 27,110,-10);
		Jel.paletteShapes.addShape(split);		

		var merge = new Jel.Shape();
		merge.setImage("img/merge.gif");
		merge.setName("Merge");
		merge.setDimension(233,54);
		merge.setMetaelement("merge", "term");
		var merge_input = merge.addElement("input", "label","img/arrow_down.png", 11, 27,80,-10);	
		merge_input.setBehaviour('click', function(ev){document.getElementById(ev.target.id+",label").focus()});
		var merge_output = merge.addElement("output", "label","img/arrow_up.png", 11, 27,110,-10);
		merge_output.setBehaviour('click', function(ev){document.getElementById(ev.target.id+",label").focus()});
		Jel.paletteShapes.addShape(merge);	

		var order = new Jel.Shape();
		order.setImage("img/order.gif");
		order.setName("Order");
		order.setDimension(345,54);
		order.setMetaelement("order", "term");
		order.addElement("input", "label","img/arrow_down.png", 11, 27,80,-10);	
		order.addElement("output", "label","img/arrow_up.png", 11, 27,110,-10);		
		Jel.paletteShapes.addShape(order);		

		//Adding composed algebra terms to palette
		var plug = new Jel.Shape();
		plug.setImage("img/plug.gif");
		plug.setName("Plug");
		plug.setDimension(233,54);	
		plug.setAsComposed();
		plug.setMetaelement("plug", "term");
		plug.addElement("input", "label","img/arrow_down.png", 11, 27,30,-10);
		plug.addElement("output", "label","img/arrow_up.png", 11, 27,60,-10);		
		//setting a custom function avery time that a plug member observe modifications in text-field input
		plug.setBehaviour(function(shape, sourceId){

							var i,j;
							
							shape.props.actions = new Object();
							
							//removing all connections of element we're manipulating
							//console.log(Jel.Canvases[shape.parentCanvas])
							Jel.Router.deleteConnections(shape.id);
							
							for(i=0; i<shape.shapes.length; i++){//output
								for(j=0; j<shape.shapes.length; j++){//input
									if(i!=j){

										if(shape.shapes.at(i).props.actions.output){

											if(shape.shapes.at(j).props.actions.input){
												//there is a matching between ports name 
												if(shape.shapes.at(i).props.actions.output instanceof Array){
													var l,m;
													if(shape.shapes.at(j).props.actions.input instanceof Array){
														for(l=0; l<shape.shapes.at(i).props.actions.output.length; l++){
															for(m=0; m<shape.shapes.at(j).props.actions.input.length; m++)
																if((shape.shapes.at(i).props.actions.output[l].label == shape.shapes.at(j).props.actions.input[m].label)&& shape.shapes.at(i).props.actions.output[l].label!= undefined && shape.shapes.at(i).props.actions.output[l].label!="") {
																	var k,found = false;
																	for(k=0; ((k<Jel.connections.length)&&(!found)); k++){
																		if(Jel.connections.at(k).outbound == shape.shapes.at(i).id && Jel.connections.at(k).inbound == shape.shapes.at(j).id ){
																			found = true;
																		}
																	}
																	if(!found){
																		console.log(shape.shapes.at(i))
																		Jel.Canvases[shape.shapes.at(i).parentCanvas].connect(shape.shapes.at(i), shape.shapes.at(j), Jel.Canvases[shape.shapes.at(i).parentCanvas])
																	}
																}
														}
													}
													else{
														for(l=0; l<shape.shapes.at(i).props.actions.output.length; l++){
															if((shape.shapes.at(i).props.actions.output[l].label == shape.shapes.at(j).props.actions.input.label)&& shape.shapes.at(i).props.actions.output[l].label!= undefined && shape.shapes.at(i).props.actions.output[l].label!="") {
																var k,found = false;
																for(k=0; ((k<Jel.connections.length)&&(!found)); k++){
																	if(Jel.connections.at(k).outbound == shape.shapes.at(i).id && Jel.connections.at(k).inbound == shape.shapes.at(j).id ){
																		found = true;
																	}
																}
																if(!found){
																	//console.log(shape.shapes.at(i))
																	Jel.Canvases[shape.shapes.at(i).parentCanvas].connect(shape.shapes.at(i), shape.shapes.at(j), Jel.Canvases[shape.shapes.at(i).parentCanvas])
																}
															}
														}
													}
												}
												else {

													if(shape.shapes.at(j).props.actions.input instanceof Array){
														for(m=0; m<shape.shapes.at(j).props.actions.input.length; m++)
															if((shape.shapes.at(i).props.actions.output.label == shape.shapes.at(j).props.actions.input[m].label)&& shape.shapes.at(i).props.actions.output.label!= undefined && shape.shapes.at(i).props.actions.output.label!="") {
																var k,found = false;
																for(k=0; ((k<Jel.connections.length)&&(!found)); k++){
																	if(Jel.connections.at(k).outbound == shape.shapes.at(i).id && Jel.connections.at(k).inbound == shape.shapes.at(j).id ){
																		found = true;
																	}
																}
																if(!found){															
																	Jel.Canvases[shape.shapes.at(i).parentCanvas].connect(shape.shapes.at(i), shape.shapes.at(j), Jel.Canvases[shape.shapes.at(i).parentCanvas])
																}
															}
													}
													else if((shape.shapes.at(i).props.actions.output.label == shape.shapes.at(j).props.actions.input.label)&& shape.shapes.at(i).props.actions.output.label!= undefined && shape.shapes.at(i).props.actions.output.label!="") {
														var k,found = false;
														for(k=0; ((k<Jel.connections.length)&&(!found)); k++){
															if(Jel.connections.at(k).outbound == shape.shapes.at(i).id && Jel.connections.at(k).inbound == shape.shapes.at(j).id ){
																found = true;
															}
														}
														if(!found){															
															Jel.Canvases[shape.shapes.at(i).parentCanvas].connect(shape.shapes.at(i), shape.shapes.at(j), Jel.Canvases[shape.shapes.at(i).parentCanvas])
														}
													}
												}
											}

											else{
												var k;
												for(k=0; k<Jel.connections.length; k++){
													//if exists a connection between two elements, the port properties must be the same
													if(Jel.connections.at(k).outbound == shape.shapes.at(i).id && Jel.connections.at(k).inbound == shape.shapes.at(j).id ){
														var other_shape = ((shape.shapes.at(i).id == sourceId) ? shape.shapes.at(j) : shape.shapes.at(i));

														if(other_shape.props.actions.input){
															other_shape.props.actions.input.label = shape.shapes.get(sourceId).props.actions.output.label;
															//updating the graphical element associated to the other shape									
															other_shape.el.updateElement(undefined,{id:"actions,input,label", value:other_shape.props.actions.input.label})
														}
														else{
															other_shape.props.actions.output.label = shape.shapes.get(sourceId).props.actions.input.label;
															//updating the graphical element associated to the other shape
															other_shape.el.updateElement(undefined,{id:"actions,output,label", value:other_shape.props.actions.output.label})
															
														}

														
													}
												}
											}
										}	
									}
								}
							}

							//defining the input/output child behaviour: every non plugged port (and also output of plugged) have to be re-exposed
							shape.props.actions.input = new Array();
							shape.props.actions.output = new Array();			

							var k,l;								

							for(i=0; i<shape.shapes.length; i++){
								if(shape.shapes.at(i).props.actions.input instanceof Array){

									if(shape.shapes.at(i).props.actions.output instanceof Array){

										for(j=0; j<shape.shapes.at(i).props.actions.input.length; j++){

											//searching for input port plugged with other term of the same level
											var found = false;
											for(k=0; k<shape.shapes.length; k++){
												if(k!=i){
													if(shape.shapes.at(k).props.actions.output){
														if(shape.shapes.at(k).props.actions.output instanceof Array){
															for(l=0; l<shape.shapes.at(k).props.actions.output.length; l++){
																if(shape.shapes.at(k).props.actions.output[l].label == shape.shapes.at(i).props.actions.input[j].label){
																	found = true;
																}
															}														
														}
														else{
															if(shape.shapes.at(k).props.actions.output.label == shape.shapes.at(i).props.actions.input[j].label){
																	found = true;
															}
														}
													}													
												}
											}

											//checking if the action is already contained in the actions list
											for(k=0; k<shape.props.actions.input.length; k++){
												if(shape.props.actions.input[k].label == shape.shapes.at(i).props.actions.input[j].label){
													found = true;
												}
											}

											if(!found){
												shape.props.actions.input.push($.extend(true, {}, shape.shapes.at(i).props.actions.input[j]));
												shape.props.actions.input.compound = true;
											}
										}
									
										for(j=0; j<shape.shapes.at(i).props.actions.output.length; j++){
											var found = false;
											//checking if the action is already contained in the actions list
											for(k=0; k<shape.props.actions.output.length; k++){
												if(shape.props.actions.output[k].label == shape.shapes.at(i).props.actions.output[j].label){
													found = true;
												}
											}
											if(!found){
												shape.props.actions.output.push($.extend(true, {}, shape.shapes.at(i).props.actions.output[j]));
												shape.props.actions.output.compound = true;										
											}
										}
										
									}
									else{
										for(j=0; j<shape.shapes.at(i).props.actions.input.length; j++){

											//searching for input port plugged with other term of the same level									
											var found = false;
											for(k=0; k<shape.shapes.length; k++){
												if(k!=i){
													if(shape.shapes.at(k).props.actions.output){
														if(shape.shapes.at(k).props.actions.output instanceof Array){
															for(l=0; l<shape.shapes.at(k).props.actions.output.length; l++){
																if(shape.shapes.at(k).props.actions.output[l].label == shape.shapes.at(i).props.actions.input[j].label){
																	found = true;
																}
															}														
														}
														else{
															if(shape.shapes.at(k).props.actions.output.label == shape.shapes.at(i).props.actions.input[j].label){
																	found = true;
															}
														}
													}												
												}
											}											

											//checking if the action is already contained in the actions list
											for(k=0; k<shape.props.actions.input.length; k++){
												if(shape.props.actions.input[k].label == shape.shapes.at(i).props.actions.input[j].label){
													found = true;
												}
											}

											if(!found){
												shape.props.actions.input.push($.extend(true, {}, shape.shapes.at(i).props.actions.input[j]));
												shape.props.actions.input.compound = true;
											}
										}

										var found = false;
										//checking if the action is already contained in the actions list
										for(k=0; k<shape.props.actions.output.length; k++){
											if(shape.props.actions.output[k].label == shape.shapes.at(i).props.actions.output.label){
												found = true;
											}
										}
										if(!found){
											shape.props.actions.output.push($.extend(true, {}, shape.shapes.at(i).props.actions.output));
											shape.props.actions.output.compound = true;	
										}
									}
								}
								else if(shape.shapes.at(i).props.actions.output instanceof Array){
									if(shape.shapes.at(i).props.actions.input){

										var found = false;
										for(k=0; k<shape.shapes.length; k++){
											
											if(shape.shapes.at(k).props.actions.output instanceof Array){
												for(l=0; l<shape.shapes.at(k).props.actions.output.length; l++){
													if(shape.shapes.at(k).props.actions.output[l].label == shape.shapes.at(i).props.actions.input.label){
														found = true;
													}
												}														
											}
											else{
												if(shape.shapes.at(k).props.actions.output.label == shape.shapes.at(i).props.actions.input.label){
														found = true;
												}
											}
											
										}											

										//checking if the action is already contained in the actions list
										for(k=0; k<shape.props.actions.input.length; k++){
											if(shape.props.actions.input[k].label == shape.shapes.at(i).props.actions.input.label){
												found = true;
											}
										}

										if(!found){
											shape.props.actions.input.push($.extend(true, {}, shape.shapes.at(i).props.actions.input));
											shape.props.actions.input.compound = true;
										}
									}
									
									for(j=0; j<shape.shapes.at(i).props.actions.output.length; j++){
										var found = false;
										//checking if the action is already contained in the actions list
										for(k=0; k<shape.props.actions.output.length; k++){
											if(shape.props.actions.output[k].label == shape.shapes.at(i).props.actions.output[j].label){
												found = true;
											}
										}
										if(!found){
											shape.props.actions.output.push($.extend(true, {}, shape.shapes.at(i).props.actions.output[j]));
											shape.props.actions.output.compound = true;
										}
									}

								}
								else{

									if(shape.shapes.at(i).props.actions.input){										
										var found = false;
										for(k=0; k<shape.shapes.length; k++){
											if(i!=k){
												if(shape.shapes.at(k).props.actions.output instanceof Array){
													for(l=0; l<shape.shapes.at(k).props.actions.output.length; l++){
														if(shape.shapes.at(k).props.actions.output[l].label == shape.shapes.at(i).props.actions.input.label){
															found = true;
														}
													}														
												}
												else{
													if(shape.shapes.at(k).props.actions.output){
														if(shape.shapes.at(k).props.actions.output.label == shape.shapes.at(i).props.actions.input.label){
																found = true;
														}
													}
												}
											}
											
										}

										//checking if the action is already contained in the actions list
										for(k=0; k<shape.props.actions.input.length; k++){
											if(shape.props.actions.input[k].label == shape.shapes.at(i).props.actions.input.label){
												found = true;
											}
										}									

										if(!found){
											shape.props.actions.input.push($.extend(true, {}, shape.shapes.at(i).props.actions.input));
											shape.props.actions.input.compound = true;
										}
										
									}
									if(shape.shapes.at(i).props.actions.output){
										var found = false;
										//checking if the action is already contained in the actions list
										for(k=0; k<shape.props.actions.output.length; k++){
											if(shape.props.actions.output[k].label == shape.shapes.at(i).props.actions.output.label){
												found = true;
											}
										}
										if(!found){
											shape.props.actions.output.push($.extend(true, {}, shape.shapes.at(i).props.actions.output));
											shape.props.actions.output.compound = true;
										}
									}
									
								}
							}

						}
		);

		Jel.paletteShapes.addShape(plug);	

		var altern = new Jel.Shape();
		altern.setImage("img/altern.gif");
		altern.setName("Altern");
		altern.setDimension(233,55);	
		altern.setAsComposed();
		altern.setMetaelement("altern", "term");
		altern.addElement("input", "label","img/arrow_down.png", 11, 27,30,-10);
		altern.addElement("output", "label","img/arrow_up.png", 11, 27,60,-10);
		altern.setBehaviour(function(shape, sourceId){

			shape.props.actions.input = new Array();
			shape.props.actions.output = new Array();			

			var i,j;			
			//altern actions are the union of child actions, both input and output
			for(i=0; i<shape.shapes.length; i++){
				if(shape.shapes.at(i).props.actions.input){
					if(shape.shapes.at(i).props.actions.input instanceof Array){

						var k,j;
						for(k=0; k<shape.shapes.at(i).props.actions.input.length; k++){
							var found = false;
							for(j=0; j<shape.props.actions.input.length; j++){
								if(shape.props.actions.input[j].label == shape.shapes.at(i).props.actions.input[k].label){									
												found = true;
								}
							}

							if(!found)
								shape.props.actions.input.push(shape.shapes.at(i).props.actions.input[k]);
						}

					}
					else{
						var j;
						var found = false;
						for(j=0; j<shape.props.actions.input.length; j++){
							if(shape.props.actions.input[j].label == shape.shapes.at(i).props.actions.input.label){								
								found = true;
							}						
						}
						if(!found)
							shape.props.actions.input.push(shape.shapes.at(i).props.actions.input);
											
					}
					shape.props.actions.input.compound = true;
				}
				if(shape.shapes.at(i).props.actions.output){
					if(shape.shapes.at(i).props.actions.output instanceof Array){

						var k,j;
						for(k=0; k<shape.shapes.at(i).props.actions.output.length; k++){
							var found = false;
							for(j=0; j<shape.props.actions.output.length; j++){
								if(shape.props.actions.output[j].label == shape.shapes.at(i).props.actions.output[k].label){									
												found = true;
								}
							}

							if(!found)
								shape.props.actions.output.push(shape.shapes.at(i).props.actions.output[k]);
						}					
					}
					else{
						var j;
						var found = false;
						for(j=0; j<shape.props.actions.output.length; j++){
							if(shape.props.actions.output[j].label == shape.shapes.at(i).props.actions.output.label){								
								found = true;
							}						
						}
						if(!found)
							shape.props.actions.output.push(shape.shapes.at(i).props.actions.output);						
					}
					shape.props.actions.output.compound = true;
				}
			}
		});
		Jel.paletteShapes.addShape(altern);	

		var conj = new Jel.Shape();
		conj.setImage("img/conj.gif");
		conj.setName("Conj");
		conj.setDimension(233,55);	
		conj.setAsComposed();
		conj.setMetaelement("conj", "term");
		conj.addElement("input", "label","img/arrow_down.png", 11, 27,30,-10);
		conj.addElement("output", "label","img/arrow_up.png", 11, 27,60,-10);	
		conj.setBehaviour(function(shape, sourceId){

			shape.props.actions.input = new Array();
			shape.props.actions.output = new Array();			

			var i,j;			

			for(i=0; i<shape.shapes.length; i++){
				if(shape.shapes.at(i).props.actions.input){
					if(shape.shapes.at(i).props.actions.input instanceof Array){
						var k,j;
						for(k=0; k<shape.shapes.at(i).props.actions.input.length; k++){
							var found = false;
							for(j=0; j<shape.props.actions.input.length; j++){
								if(shape.props.actions.input[j].label == shape.shapes.at(i).props.actions.input[k].label){									
												found = true;
								}
							}

							if(!found)
								shape.props.actions.input.push(shape.shapes.at(i).props.actions.input[k]);
						}

					}
					else{
						var j;
						var found = false;
						for(j=0; j<shape.props.actions.input.length; j++){
							if(shape.props.actions.input[j].label == shape.shapes.at(i).props.actions.input.label){								
								found = true;
							}						
						}
						if(!found)
							shape.props.actions.input.push(shape.shapes.at(i).props.actions.input);
											
					}
					shape.props.actions.input.compound = true;
				}
				if(shape.shapes.at(i).props.actions.output){
					for(j=i; j<shape.shapes.length; j++){							
							if(i!=j){
								var k,l;
								if(shape.shapes.at(i).props.actions.output instanceof Array){
									for(k=0; k<shape.shapes.at(i).props.actions.output.length; k++){
										var found = false;
										if(shape.shapes.at(j).props.actions.output){
											if(shape.shapes.at(j).props.actions.output instanceof Array){
												for(l=k; l<shape.shapes.at(j).props.actions.output.length; l++)
												if(shape.shapes.at(i).props.actions.output[k].label == shape.shapes.at(j).props.actions.output[l].label){
													found = true;							
												}
											}
											else{
												if(shape.shapes.at(i).props.actions.output[k].label == shape.shapes.at(j).props.actions.output.label){
													found = true;							
												}
											}
										}
										if(found) shape.props.actions.output.push(shape.shapes.at(i).props.actions.output[k]);	
									}
								}
								else{
									var found = false;
									if(shape.shapes.at(j).props.actions.output){
										if(shape.shapes.at(j).props.actions.output instanceof Array){
											for(l=0; l<shape.shapes.at(j).props.actions.output.length; l++)
											if(shape.shapes.at(i).props.actions.output.label == shape.shapes.at(j).props.actions.output[l].label){
												found = true;							
											}
										}
										else{
											if(shape.shapes.at(i).props.actions.output.label == shape.shapes.at(j).props.actions.output.label){
												found = true;							
											}
										}
									}								
									if(found) shape.props.actions.output.push(shape.shapes.at(i).props.actions.output);										
								}
							}
					}
				
					shape.props.actions.output.compound = true;
				}
			}
		});
		Jel.paletteShapes.addShape(conj);	

		var quot = new Jel.Shape();
		quot.setImage("img/quot.gif");
		quot.setName("Quot");
		quot.setDimension(233,55);	
		quot.setAsComposed();
		quot.setMetaelement("quot", "term");
		quot.addElement("input", "label","img/arrow_down.png", 11, 27,30,-10);
		quot.addElement("output", "label","img/arrow_up.png", 11, 27,60,-10);
		quot.setBehaviour(function(shape, sourceId){
							
							var i,j;
							//defining the input/output child behaviour: every non plugged port (and also output of plugged) have to be re-exposed
							shape.props.actions.input = new Array();
							shape.props.actions.output = new Array();			

							var k,l;								
							if(shape.shapes.length == 2){
								//inverting the actions of the first operand
								var child_shapes = new Array();
								child_shapes[0] = $.extend(true, {}, shape.shapes.at(0));
								child_shapes[0].props.actions.input =  $.extend(true, {}, shape.shapes.at(0).props.actions.output);
								child_shapes[0].props.actions.output = $.extend(true, {}, shape.shapes.at(0).props.actions.input);
								child_shapes[1] = $.extend(true, {}, shape.shapes.at(1));

								for(i=0; i<2; i++){
									if(child_shapes[i].props.actions.input instanceof Array){
										 
										if(child_shapes[i].props.actions.output instanceof Array){

											for(j=0; j<child_shapes[i].props.actions.input.length; j++){

												//searching for input port plugged with other term of the same level
												var found = false;
												for(k=0; k<2; k++){
													if(k!=i){
														if(child_shapes[k].props.actions.output instanceof Array){
															for(l=0; l<child_shapes[k].props.actions.output.length; l++){
																if(child_shapes[k].props.actions.output[l].label == child_shapes[i].props.actions.input[j].label){
																	found = true;
																}
															}														
														}
														else{
															if(child_shapes[k].props.actions.output.label == child_shapes[i].props.actions.input[j].label){
																	found = true;
															}
														}
																												
													}
												}

												if(!found){
													shape.props.actions.output.push($.extend(true, {}, child_shapes[i].props.actions.input[j]));
													shape.props.actions.output.compound = true;
												}
											}


											for(j=0; j<child_shapes[i].props.actions.output.length; j++){
												shape.props.actions.input.push($.extend(true, {}, child_shapes[i].props.actions.output[j]));
												shape.props.actions.input.compound = true;										
											}
										}
										else{
											for(j=0; j<child_shapes[i].props.actions.input.length; j++){

												//searching for input port plugged with other term of the same level									
												var found = false;
												for(k=0; k<2; k++){
													if(k!=i){
														if(child_shapes[k].props.actions.output){
															if(child_shapes[k].props.actions.output instanceof Array){
																for(l=0; l<child_shapes[k].props.actions.output.length; l++){
																	if(child_shapes[k].props.actions.output[l].label == child_shapes[i].props.actions.input[j].label){
																		found = true;
																	}
																}														
															}
															else{
																if(child_shapes[k].props.actions.output.label == child_shapes[i].props.actions.input[j].label){
																		found = true;
																}
															}
														}												
													}
												}											


												if(!found){
													shape.props.actions.output.push($.extend(true, {}, child_shapes[i].props.actions.input[j]));
													shape.props.actions.output.compound = true;
												}
											}

											shape.props.actions.input.push($.extend(true, {}, child_shapes[i].props.actions.output));
											shape.props.actions.input.compound = true;	
										}
									}
									else if(child_shapes[i].props.actions.output instanceof Array){
										if(child_shapes[i].props.actions.input){

											var found = false;
											for(k=0; k<2; k++){
												
												if(child_shapes[k].props.actions.output instanceof Array){
													for(l=0; l<child_shapes[k].props.actions.output.length; l++){
														if(child_shapes[k].props.actions.output[l].label == child_shapes[i].props.actions.input.label){
															found = true;
														}
													}														
												}
												else{
													if(child_shapes[k].props.actions.output.label == child_shapes[i].props.actions.input.label){
															found = true;
													}
												}
												
											}											

											if(!found){
												shape.props.actions.output.push($.extend(true, {}, child_shapes[i].props.actions.input));
												shape.props.actions.output.compound = true;
											}
										}
										
										for(j=0; j<child_shapes[i].props.actions.output.length; j++){
											shape.props.actions.input.push($.extend(true, {}, child_shapes[i].props.actions.output[j]));
											shape.props.actions.input.compound = true;
										}

									}
									else{

										if(child_shapes[i].props.actions.input){

											var found = false;
											for(k=0; k<2; k++){
												if(i!=k){
													if(child_shapes[k].props.actions.output instanceof Array){
														for(l=0; l<child_shapes[k].props.actions.output.length; l++){
															if(child_shapes[k].props.actions.output[l].label == child_shapes[i].props.actions.input.label){
																found = true;
															}
														}														
													}
													else{
														if(child_shapes[k].props.actions.output){
															if(child_shapes[k].props.actions.output.label == child_shapes[i].props.actions.input.label){
																	found = true;
															}
														}
													}
												}
												
											}											

											if(!found && child_shapes[i].props.actions.input.label){
												shape.props.actions.output.push($.extend(true, {}, child_shapes[i].props.actions.input));
												shape.props.actions.output.compound = true;
											}
											
										}
										if(child_shapes[i].props.actions.output && child_shapes[i].props.actions.output.label){												
											shape.props.actions.input.push($.extend(true, {}, child_shapes[i].props.actions.output));
											shape.props.actions.input.compound = true;
										}
										
									}
								}
							}
						});

		Jel.paletteShapes.addShape(quot);

		var invert = new Jel.Shape();
		invert.setImage("img/inv.gif");
		invert.setName("Invert");
		invert.setDimension(233,55);	
		invert.setAsComposed();		
		invert.setMetaelement("invert", "term");
		invert.addElement("input", "label","img/arrow_down.png", 11, 27,30,-10);
		invert.addElement("output", "label","img/arrow_up.png", 11, 27,60,-10);
		invert.setBehaviour(function(shape, sourceId){

			shape.props.actions.input = new Array();
			shape.props.actions.output = new Array();			

			var i,j;			

			for(i=0; i<shape.shapes.length; i++){
				if(shape.shapes.at(i).props.actions.input){
					if(shape.shapes.at(i).props.actions.input instanceof Array){
						shape.props.actions.output = shape.props.actions.output.concat(shape.shapes.at(i).props.actions.input);
					}
					else{
						shape.props.actions.output.push(shape.shapes.at(i).props.actions.input);						
					}
					shape.props.actions.output.compound = true;
				}
				if(shape.shapes.at(i).props.actions.output){
					if(shape.shapes.at(i).props.actions.output instanceof Array){
						shape.props.actions.input = shape.props.actions.input.concat(shape.shapes.at(i).props.actions.output);						
					}
					else{
						shape.props.actions.input.push(shape.shapes.at(i).props.actions.output);						
					}
					shape.props.actions.input.compound = true;
				}
			}
		});
		Jel.paletteShapes.addShape(invert);

		//overriding the import function (in reality is a recall, with a new parameter)
		Jel.xmlImport = function(){Jel.fn.xmlImport("terms", "term")};
	}
