automaton ("A", "i", "j"){ 
	states {
		a1_j = 0
		
	}
	actions {
		input ('m1_i_j') {
			effect { 
				a1_j = 1
				println 'Input action m1 called in A'
			}
		}
		
		output ('m2_i_j') {
			pre {a1_j == 4}
			effect { 
				notexist = 3
				a1_j = 2
				println 'Output action m2 called in A'
				} 
		}
		
		internal('m3_i_j') {
			pre{a1_j == 2}
			effect {
				a1_j = 3
				println 'Internal action m3 called in A'
			}
		}
		
	}	
} 

automaton ("B"){ 
	states {
		b1 = 0
	}
	actions {
		input ('m1') {
			effect { 
				b1 = 6
				println 'Input action m1 called in B'
			} 
		}
		
		input ('m2_0_1') {
			effect { 
				b1 = 2
				println 'Input action m2 called in B'
			}
		}
		
		internal ('m4') {
			effect { 
				b1 = 3
				println 'Internal action m4 called in B'
			} 
		}
		
		internal ('m5') {
			effect { 
				b1 = 1
				println 'Internal action m5 called in B'
			} 
		}
		
	}	
} 