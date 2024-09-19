automaton("P", "i" , "j") {
	states {
		dirty = [:]
		reqf = [:]
		fork = [:]
		//clockfaciline
		//fucidine
		state = "THINKING"
	}
	actions {
		
		internal("hungry_i") {
			pre {
				state == "THINKING"
			}
			effect {
				state = "HUNGRY"
			}
		}
		
		output("request_i_j") {
			pre {state=="HUNGRY" && reqf.j && !fork.j }
			effect {
				reqf.j = false; 
			}
		}
		
		output ("release_i_j") {
			pre { state !="EATING" && reqf.j && fork.j && dirty.j 
			}
			effect {
				dirty.j = false; 
				fork.j = false; 
			}
		}
		
		input ("release_j_i") { //recieving a fork 
			effect {
				fork.j = true
				dirty.j = false
				
				//check if I have all forks, then I can become EATING
				boolean allForks = true;
				fork.each {
					allForks = allForks && it.value 
					 }
				
				if(allForks) {
					state = "EATING"
					dirty.each {
						it.value = true;
					}
				}
			}
		}
		
		internal("full_i"){
			pre {
				state == "EATING"
			}
			effect {
				state = "THINKING"
			}
		}
		
		input("request_j_i") { //recieving a request
			effect {
				reqf.j = true
			}
		}
	}
	tasks {
		task("EatingTask_i", "S") {
			action("hungry_i")
			action( "full_i")
		}
		task ("release_i_j", "S") {
			action("release_i_j")
		}
		task ("request_i_j", "S") {
			action("request_i_j")
		}
	}
}