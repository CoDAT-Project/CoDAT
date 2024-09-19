automaton("P", "i" , "j") {
	states {
		dirty = [:]
		reqf = [:]
		fork = [:]
		bottle = [:]
		reqb = [:]
		needb = [:]
		state_drinker ="TRANQUIL"
		state_diner ="THINKING"
		
	}
	actions {
		
		
		/*Drinker actions*/
		internal("thirsty_i") {
			pre {
				boolean canBeThursty = state_drinker == "TRANQUIL" 
				boolean bottleNeeded = false;
				if(canBeThursty) {
					needb.each {
						if(it.value) {
							bottleNeeded = true
						}
					}
				}
				return canBeThursty && bottleNeeded;
			}
			effect {
				state_drinker = "THIRSTY"
				state_diner = "HUNGRY"
			}
		}
					
		internal("need_bottle_i_j") {
			pre {
				state_drinker == "TRANQUIL" && ( needb.j == null)// !setNeedb.j 
			}
			effect {
				needb.j =  true
			}
		}
		
		
		internal("not_need_bottle_i_j") {
			pre {
				state_drinker == "TRANQUIL" && ( needb.j == null)// !setNeedb.j
			}
			effect {
				needb.j = false
			}
		}
		
		output("request_fork_i_j") {
			pre {state_diner=="HUNGRY" && reqf.j && !fork.j }
			effect {
				reqf.j = false; 
			}
		}
		
		output ("release_fork_i_j") {
			pre { state_diner !="EATING" && reqf.j && fork.j && dirty.j 
			}
			effect {
				dirty.j = false; 
				fork.j = false; 
			}
		}
		
		input ("release_fork_j_i") { //recieving a fork
			effect {
				fork.j = true
				dirty.j = false
			}
		}
		
		internal("eat_i") {
			pre {
				boolean allForks = (state_diner == "HUNGRY")
				if(allForks) { 
					fork.each {key, value ->
						allForks = allForks && value && !dirty[key]
						 }
					
				}
				return allForks
			}
			effect {
				state_diner = "EATING"
				dirty.each {
					it.value = true;
				}
			}
		}
		
		input("request_fork_j_i") { //recieving a request
			effect {
				reqf.j = true
			}
		}
	
		
		output("request_bottle_i_j")
		{
			pre{
				state_drinker == "THIRSTY" && needb.j && reqb.j && !bottle.j
			}
			effect {
				reqb.j = false;
			}
		}
		
		output("send_bottle_i_j") {
			pre {
				reqb.j && bottle.j && !(needb.j && (state_drinker == "DRINKING" || fork.j)) 
			}
			effect {
				bottle.j = false; 
			}
		}
		
		input("request_bottle_j_i") {
			effect {
				reqb.j = true
			}
		}
		
		input("send_bottle_j_i") {
			effect {
				bottle.j = true
			}
		}
		
		internal("drink_i") {
			pre {
				boolean allBottles = state_drinker =="THIRSTY" && state_diner=="EATING"
				if(allBottles) { 
				bottle.each {
					allBottles = allBottles && (needb[it.key]!=null && !needb[it.key] || it.value)
					 }
				}
				return allBottles 
			}
			effect {
				state_drinker = "DRINKING"
			}
		}
		
		internal ("finish_drinking_i") {
			pre {
				state_drinker == "DRINKING" 
				
			}
			effect {
				needb.clear()
				state_drinker = "TRANQUIL"
				state_diner="THINKING"
				
			}
		}
		
		
	}
	tasks {
		task("DrinkingTask_i", "S") {
			action("thirsty_i")
			action("eat_i")
			action("drink_i")
			action("finish_drinking_i")
		}
		task("channel_i_j","S") {
			action("need_bottle_i_j")
			action("not_need_bottle_i_j")
			action("request_bottle_i_j")
			action("send_bottle_i_j")
			action("request_fork_i_j")
			action("release_fork_i_j")
		}
	}
}