automaton ("A", "i", "j"){ 
	states {
		tryMessages = [:] 
		exitMessages = [:]
		state = "REM"; 
		time = new Date().time 
	}
	actions {
	
		output("try_i_j") {
			pre {state=="REM"} 
			effect {
				state = "TRY"
				time = new Date().time 
			}
		}
		
		output ("exit_i_j") {
			pre { state =="CRIT"
			}
			effect {
				state = "REM"
				tryMessages.clear() 
				exitMessages.clear() 
			}
		}
		
		output ("crit_i_j") {
			pre {
				check = ( state == "TRY" ) 
				if(check == true) { 
					tryMessages.each {
						time1 = it.value
						time2 = exitMessages.it
						if(time2!=null && time2>time1) {
						}
						else {
							check = false;
						}
					}
				}
				return check;
			}
			effect {
				state = "CRIT"
			}
		}
	
		
		input("try_j_i") {
			effect {
				tryMessages.j = new Date().time
			}
		}
		
		input("exit_j_i") {
			effect {
				exitMessages.j = new Date().time
			}
		} 
		
	}	
} 


