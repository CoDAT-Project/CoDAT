concurrentProgram("cpName") {
	x12 = 1

	process(1) {
		AP = [N1,T1,C1]
		
		states {
			state("s0", true) {
				N1 = true				
			}
			state("s1") {
				T1 = true
			}
			state("s2") {
				C1 = true
			}
		}
		
		arcs {
			arc("s0", "s1") {
				guard {
					T2  
				}
				action {
					x12=2
				}	
			}
			arc("s0", "s1") {
				guard {
					N2 || C2  
				}
				action {
				}	
			}
			arc("s1", "s2") {
				guard {
					N2 || (T2 && x12==1)  
				}
				action {
				}	
			}

			arc("s2", "s0") {
				guard {  
				}
				action {
				}	
			}
		}
	}

	process(2) {
		AP = [N2,T2,C2]
	      
		states {
			state("s0", true) {
				N2 = true
			}
			state("s1") {
				T2 = true
			}
			state("s2") {
				C2 = true
			}
		}

		arcs {
			arc("s0", "s1") {
				guard {
					T1  
				}
				action {
					x12=1
				}	
			}
			arc("s0", "s1") {
				guard {
					N1 || C1  
				}
				action {
				}	
			}
			arc("s1", "s2") {
				guard {
					N1 || (T1 && x12==2)  
				}
				action {
				}	
			}

			arc("s2", "s0") {
				guard {  
				}
				action {
				}	
			}
		}
	}
}