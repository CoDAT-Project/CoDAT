iProgram("drinkingPhiloIProgram") {
	pair {
		I = [[1,2], [2,3]/*, [3,1]*/]

	    varRegex = "_(.)+"
		apRegex = "[a-z]+"

	     
	    spec.initial = "ANDi[TRANQUILi]"
		spec.TRANQUILi_To_THIRSTYi = "ANDi[A[G((TRANQUILi)=>(E[Xi(THIRSTYi)]))]]"
		spec.THIRSTYi_To_DRINKINGi = "ANDi[A[G((THIRSTYi)=>(A[Xi(DRINKINGi)]))]]"
		spec.DRINKINGi_To_TRANQUILi = "ANDi[A[G((DRINKINGi)=>(A[Xi(TRANQUILi)]))]]"
		spec.safety1 = "ANDij[A[G(!(DRINKINGi & DRINKINGj & wantBottle_i_j & wantBottle_j_i))]]"
		spec.safety = "ANDij[A[G(!(DRINKINGi & DRINKINGj))]]"
		spec.starvationAbsence = "ANDi[A[G((THIRSTYi) => (A[F(DRINKINGi)]))]]"
		spec.P_In_One_State_TRANQUILi = "ANDi[A[G((TRANQUILi) <=> (!(THIRSTYi | DRINKINGi)))]]"	
		spec.P_In_One_State_THIRSTYi = "ANDi[A[G((THIRSTYi) <=> (!(TRANQUILi | DRINKINGi)))]]"
		spec.P_In_One_State_DRINKINGi = "ANDi[A[G((DRINKINGi) <=> (!(THIRSTYi | TRANQUILi)))]]"
		spec.Atomic_transitions_N = "ANDij[A[G((TRANQUILi) => (A[Xj(TRANQUILi)]))] & A[G((TRANQUILj) => (A[Xi(TRANQUILj)]))]]"
		spec.Atomic_transitions_T = "ANDij[A[G((THIRSTYi) => (A[Xj(THIRSTYi)]))] & A[G((THIRSTYj) => (A[Xi(THIRSTYj)]))]]"
		spec.Atomic_transitions_C = "ANDij[A[G((DRINKINGi) => (A[Xj(DRINKINGi)]))] & A[G((DRINKINGj) => (A[Xi(DRINKINGj)]))]]"
		
		pairProgram("drinkingPhilo", "i", "j") {
			fork_i_j = i
			wantBottle_i_j = true
			wantBottle_j_i = false

			pairProcess("i") {
				AP = [TRANQUILi,THIRSTYi,DRINKINGi]
				
				pairStates {
					state("s0", true) {
						TRANQUILi = true				
					}
					state("s1") {
						THIRSTYi = true
					}
					state("s2") {
						DRINKINGi = true
					}
				}
				
				pairArcs {
					arc("s0", "s1") {
						guard {
							THIRSTYj  
						}
						action {
							fork_i_j=j;
							wantBottle_i_j = true;
						}	
					}
					arc("s0", "s1") {
						guard {
							THIRSTYj  
						}
						action {
							fork_i_j= j;
							wantBottle_i_j = false;
						}	
					}
					arc("s0", "s1") {
						guard {
							TRANQUILj || DRINKINGj  
						}
						action {
							wantBottle_i_j = true;
						}	
					}
					arc("s0", "s1") {
						guard {
							TRANQUILj || DRINKINGj  
						}
						action {
							wantBottle_i_j = false;
						}	
					}
					arc("s1", "s2") {
						guard {
							TRANQUILj || (THIRSTYj && fork_i_j==i) || (!(wantBottle_i_j && wantBottle_j_i) && DRINKINGj)  
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

			pairProcess("j") {
				AP = [TRANQUILj,THIRSTYj,DRINKINGj]
				
				pairStates {
					state("s0", true) {
						TRANQUILj = true				
					}
					state("s1") {
						THIRSTYj = true
					}
					state("s2") {
						DRINKINGj = true
					}
				}
				
				pairArcs {
					arc("s0", "s1") {
						guard {
							THIRSTYi  
						}
						action {
							fork_i_j=i;
							wantBottle_j_i = true;
						}	
					}
					arc("s0", "s1") {
						guard {
							THIRSTYi  
						}
						action {
							fork_i_j= i;
							wantBottle_j_i = false;
						}	
					}
					arc("s0", "s1") {
						guard {
							TRANQUILi || DRINKINGi  
						}
						action {
							wantBottle_j_i = true;
						}	
					}
					arc("s0", "s1") {
						guard {
							TRANQUILi || DRINKINGj  
						}
						action {
							wantBottle_j_i = false;
						}	
					}
					arc("s1", "s2") {
						guard {
							TRANQUILi || (THIRSTYi && fork_i_j==j) || (!(wantBottle_j_i && wantBottle_i_j) && DRINKINGi)  
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
	}
}