automaton("R", "i" , "j") { // i index of reader, j index of writer
	states {
		dirty = [:]
		reqf = [:]
		fork = [:]
		state = "THINKING"
	}
	actions {
		
		internal("r_hungry_i") {
			pre {
				state == "THINKING"
			}
			effect {
				state = "HUNGRY"
			}
		}
		
		output("r_request_i_j") {
			pre {state=="HUNGRY" && reqf.j && !fork.j }
			effect {
				reqf.j = false; 
			}
		}
		
		output ("r_release_i_j") {
			pre { state !="EATING" && reqf.j && fork.j && dirty.j 
			}
			effect {
//				dirty.j = false; 
				fork.j = false; 
			}
		}
		
		input ("rw_release_j_i") { //recieving a fork 
			effect {
				fork.j = true
//				dirty.j = false : the fork is not cleaned when recieved
			}
		}
		
		internal("r_eat_i") {
			pre { 
				//check if I have all forks, then I can become EATING
				boolean allForks = state == "HUNGRY";
				fork.each {key,value->
					allForks = allForks && value && !dirty[key]
					 }
				return allForks
			}
			effect { 
				state = "EATING"
				dirty.each {
					it.value = true;
				}
			}
		}
		
		internal("r_full_i"){
			pre {
				state == "EATING"
			}
			effect {
				state = "THINKING"
			}
		}
		
		input("rw_request_j_i") { //recieving a request
			effect {
				reqf.j = true
			}
		}
	}
	tasks {
		task("Reading_i","W") {
			action("r_hungry_i")
			action("r_eat_i")
			action("r_full_i")
		}
		task ("channel_i_j","W") {
			action("r_request_i_j")
			action("r_release_i_j")
		}
	}
}



automaton("W", "i" , "j", "k") { // i index of writer, j index of another writer, k index of reader
	states {
		state = "THINKING"
	}
	states ("j") {
		dirty_w = [:]
		reqf_w = [:]
		fork_w = [:]
		
	}
	
	states ("k") {
		dirty_r = [:]
		reqf_r = [:]
		fork_r = [:]
		
	}
	actions {
		
		internal("w_hungry_i") {
			pre {
				state == "THINKING"
			}
			effect {
				state = "HUNGRY"
			}
		}
		
		output("w_request_i_j") {
			pre {state=="HUNGRY" && reqf_w.j && !fork_w.j }
			effect {
				reqf_w.j = false;
			}
		}
		
		
		output("rw_request_i_k") {
			pre {state=="HUNGRY" && reqf_r.k && !fork_r.k }
			effect {
				reqf_r.k = false;
			}
		}
		
		output ("w_release_i_j") {
			pre { state !="EATING" && reqf_w.j && fork_w.j && dirty_w.j
			}
			effect {
				dirty_w.j = false;
				fork_w.j = false;
			}
		}
		
		
		output ("rw_release_i_k") {
			pre { state =="THINKING" && reqf_r.k && fork_r.k && dirty_r.k
			}
			effect {
//				dirty_w.j = false; // we do not clean the fork when sending it to a reader
				fork_r.k = false;
			}
		}
		
		input ("w_release_j_i") { //recieving a fork
			effect {
				fork_w.j = true
				dirty_w.j = false		
			}
		}
		
		input ("r_release_k_i") { //recieving a fork
			effect {
				fork_r.k = true
				dirty_r.k = false
			}
		}
		
		internal("w_eat_i") {
			pre {
				//check if I have all forks, then I can become EATING
				boolean allForks = state == "HUNGRY";
				fork_w.each {key, value ->
//					println "$key: $value : dirty"
					boolean dirty = dirty_w[key];
//					println "$dirty_w + $dirty" 
					allForks = allForks && value && !(dirty_w[key])
					 }
				fork_r.each {key, value ->
					allForks = allForks && value //&& !(dirty_r[key])
				}
				return allForks; 
			}
			effect {
				state = "EATING"
				dirty_w.each {
					it.value = true;
				}
				dirty_r.each {
					it.value = true;
				}
			}
		}
		
		internal("w_full_i"){
			pre {
				state == "EATING"
			}
			effect {
				state = "THINKING"
			}
		}
		
		input("w_request_j_i") { //recieving a request
			effect {
				reqf_w.j = true
			}
		}
		
		input("r_request_k_i") { //recieving a request
			effect {
				reqf_r.k = true
			}
		}
		
	}
	tasks {
		task("Writing_i", "W") {
			action("w_hungry_i")
			action("w_eat_i")
			action("w_full_i")
		}
		task("w_channel_i_j", "W") {
			action("w_request_i_j")
			action("w_release_i_j")
		}
		task("r_channel_i_k", "W") {
			action("rw_request_i_k")
			action("rw_release_i_k")
		}
	}
}