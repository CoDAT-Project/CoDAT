

automaton("P", "i" , "j") { //(1)
	states {
		// (2)
		state = "THINKING"
	}
	//(3) 
	actions {
		
		internal("myInternalAction_i") {//(4)
			pre {
				state == "THINKING" //(5)
			}
			effect {
				state = "HUNGRY" //(6)
			}
		}
		
		output("myOutputAction_i_j") {//(7)
			pre {state=="HUNGRY" } 
			effect {
				reqf.j = false;
			}
		}
		
		input ("myInputAction_j_i") { //(8)
			effect {//(9)
				state = "EATING"
			}
		}
	}
	tasks {
		task("EatingTask_i") {
			action("myInternalAction_i")
			action( "myOutputAction_i_j")
			action("myInputAction_j_i")
		}
	}
}


