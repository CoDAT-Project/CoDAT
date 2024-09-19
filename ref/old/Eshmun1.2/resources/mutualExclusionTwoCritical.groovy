iProgram("twoCriticalSectionsIProgram") {
	pair {
		I = [[1,2], [1,,3]]

	    varRegex = "_(.)+"
		apRegex = "[a-z]+"

		//mpctl spec declaration
		//spec.initial = "ANDi[Ni]"
		//spec.Ni_To_Ti = "ANDi[A[G((Ni)=>(E[Xi(Ti)]))]]"
		//spec.Ti_To_Ci = "ANDi[A[G((Ti)=>(A[Xi(Ci|Di)]))]]"
		//spec.Ci_To_Ni = "ANDi[A[G((Ci)=>(A[Xi(Ni)]))]]"
	    spec.safety = "ANDij[A[G(!(Ci & Cj) & !(Di & Dj))]]"
		spec.starvationAbsence = "ANDi[A[G((Ti) => (A[F(Ci|Di)]))]]"
		//spec.P_In_One_State_Ni = "ANDi[A[G((Ni) <=> (!(Ti | Ci | Di)))]]"	
		//spec.P_In_One_State_Ti = "ANDi[A[G((Ti) <=> (!(Ni | Ci | Di)))]]"
		//spec.P_In_One_State_Ci = "ANDi[A[G((Ci) <=> (!(Ti | Ni | Di)))]]"
		//spec.P_In_One_State_Di = "ANDi[A[G((Di) <=> (!(Ti | Ni | Ci)))]]"
		//spec.Atomic_transitions_N = "ANDij[A[G((Ni) => (A[Xj(Ni)]))] & A[G((Nj) => (A[Xi(Nj)]))]]"
		//spec.Atomic_transitions_T = "ANDij[A[G((Ti) => (A[Xj(Ti)]))] & A[G((Tj) => (A[Xi(Tj)]))]]"
		//spec.Atomic_transitions_C = "ANDij[A[G((Ci) => (A[Xj(Ci)]))] & A[G((Cj) => (A[Xi(Cj)]))]]"
		//spec.Atomic_transitions_D = "ANDij[A[G((Di) => (A[Xj(Di)]))] & A[G((Dj) => (A[Xi(Dj)]))]]"

		pairProgram("twoCriticalSections", "i", "j") {
			//define shared memory variables modifiable by both processes
			x_i_j = i

			pairProcess("i") {
				AP = [Ni,Ti,Ci,Di]

				pairStates {
					state("s0", true) {
						Ni = true				
					}
					state("s1") {
						Ti = true
					}
					state("s2") {
						Ci = true
					}
					state("s3") {
						Di = true
					}
				}
				
				pairArcs {
					arc("s0", "s1") {
						guard {
							Tj  
						}
						action {
							x_i_j =j
						}	
					}
					arc("s0", "s1") {
						guard {
							Nj || Cj || Dj
						}
						action {
						}	
					}
					arc("s1", "s2") {
						guard {
							Dj || Nj || (Tj && x_i_j==i)  
						}
						action {
						}	
					}
					arc("s1", "s3") {
						guard {
							Cj || Nj || (Tj && x_i_j==i)  
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
					arc("s3", "s0") {
						guard {  
						}
						action {
						}	
					}
				}
			}

			pairProcess("j") {
				AP = [Nj, Tj, Cj, Dj]
			      
				pairStates {
					state("s0", true) {
						Nj = true
					}
					state("s1") {
						Tj = true
					}
					state("s2") {
						Cj = true
					}
					state("s3") {
						Dj = true
					}
				}

				pairArcs {
					arc("s0", "s1") {
						guard {
							Ti  
						}
						action {
							x_i_j=i
						}	
					}
					arc("s0", "s1") {
						guard {
							Ni || Ci || Di 
						}
						action {
						}	
					}
					arc("s1", "s2") {
						guard {
							Di || Ni || (Ti && x_i_j==j)
						}
						action {
						}	
					}
					arc("s1", "s3") {
						guard {
							Ci || Ni || (Ti && x_i_j==j)
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
					arc("s3", "s0") {
						guard {  
						}
						action {
						}	
					}
				}
			}
		}
	}
}