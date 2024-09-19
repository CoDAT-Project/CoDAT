automaton("P", "i", "j", "k", "l", "m") {
	//j -> P I want to talk to
	//k -> P I want to recieve from
	//l -> C I want to talk to
	//m -> C I want to recieve from
	states {
		prev_state = null
		state = 'START'
		phase = 1
	}
	actions {
		
		//output to nxt process in line
		
		output("p_submit_i_j") {
			pre {
				prev_state == "SUBMIT" && state =='START' && phase == 1
			}
			effect { state = "SUBMIT"  }
		}
		
		output("p_abort_i_j") {
			pre {
				state == 'START' && phase == 1
			}
			effect {  state = "ABORT"  }
		}
		
		output("p_commit_i_j") {
			pre {
				 prev_state=="COMMIT" && phase == 2//state  !="COMMIT" &&
			}
			effect { state = "COMMIT" }
		}
		
		
		output("p_abort_commit_i_j") {
			pre {
				 prev_state=="ABORTCOMMIT" && phase == 2//state !="ABORTCOMMIT" &&
			}
			effect { state = "ABORTCOMMIT" }
		}
		
		//recieve from P_k
		input("p_submit_k_i") {
			effect {
				prev_state = "SUBMIT"
				phase = 1
			}
		}
		
		input("p_abort_k_i") {
			effect {
				prev_state = "ABORT"
				phase = 1
			}
		}
		
		input("p_commit_k_i") {
			effect {
				prev_state = "COMMIT"
				phase = 2
			}
		}
		
		input("p_abort_commit_k_i") {
			effect {
				prev_state = "ABORTCOMMIT"
				phase = 2
			}
		}
		
		//outout to C_l
		output("c_ack_submit_i_l") {
			pre {
				prev_state == "SUBMIT" && state =='START' && phase == 1
			}
			effect { state = "SUBMIT" }
		}
		
		output("c_ack_abort_i_l") {
			pre {
				state == 'START' && phase == 1
			}
			effect { state = "ABORT" }
		}
		
		output("c_ack_commit_i_l") {
			pre {
				 prev_state=="COMMIT" && phase == 2//state  !="COMMIT" &&
			}
			effect { state = "COMMIT" }
		}
		
		output("c_ack_abort_commit_i_l") {
			pre {
				state !="ABORTCOMMIT" && prev_state=="ABORTCOMMIT" && phase == 2//state !="ABORTCOMMIT" &&
			}
			effect { state = "ABORTCOMMIT" }
		}
		
		//recieve from C _M
		
		//inputs from the connecttor
		input("c_submit_m_i") {
			effect {
				prev_state = "SUBMIT"
				phase = 1
			}
		}

		input("c_commit_m_i") {
			effect {
				prev_state = "COMMIT"
				phase = 2
			}
		}
		
		
		input("c_abort_commit_m_i") {
			effect {
				prev_state = "ABORTCOMMIT"
				phase = 2
			}
		}
		
//		internal("reset_i") {
//			pre {
//				(state == "ABORTCOMMIT" || state =="COMMIT" ) && phase ==2
//			}
//			effect {
//				state = 'START'
//				phase = 1
//				prev_state = null
//			}
//		}
	}
	tasks {
//		task("P_internal_i_", "S") {
//			action("reset_i")
//		}
		task("P_channel_i_j_", "S") {
			action("p_submit_i_j");
			action("p_abort_i_j");
			action("p_commit_i_j");
			action("p_abort_commit_i_j");
		}
		task("P_channel_i_l_", "S") {
			action("c_ack_submit_i_l");
			action("c_ack_submit_i_l");
			action("c_ack_abort_i_l");
			action("c_ack_commit_i_l");
			action("c_ack_abort_commit_i_l");
		}
	}
}


automaton("C", "i" , "j", "k") {
	states {
		phase = 1
		state = 'START'
		prev_state = null
	}
	actions {
		
		//output to first process
		
		output("c_submit_i_j") {
			pre { phase == 1 && state == 'START' }
			effect {  state = "SUBMIT" }
		}
		
		//		output("c_abort_i_j") {
		//			pre { phase == 1 && state == 'START' }
		//			effect {  state = "ABORT"  }
		//		}
		
		output("c_commit_i_j") {
			pre { prev_state == "SUBMIT" && phase == 2  }// state != "COMMIT" &&
			effect { state = "COMMIT" }
		}
		
		output("c_abort_commit_i_j") {
			pre {  prev_state == "ABORT" && phase == 2 }//state != "ABORTCOMMIT" &&
			effect { state = "ABORTCOMMIT" }
		}
		
		
		input("c_ack_submit_k_i") {
			effect {
				prev_state = "SUBMIT"
				phase = 2
			}
		}
		
		input("c_ack_abort_k_i") {
			effect {
				prev_state = "ABORT"
				phase = 2
			}
		}
		
		input("c_ack_commit_k_i") {
			effect {
//				prev_state = null
//				phase= 1
//				state = 'START'
			}
		}
		
		input("c_ack_abort_commit_k_i") {
			effect {
//				prev_state = null
//				phase= 1
//				state = 'START'
			}
		}
	}
	tasks {
		task("C_channel_i_j_", "S") {
			action("c_submit_i_j");
			//			action("c_abort_i_j");
			action("c_commit_i_j");
			action("c_abort_commit_i_j");
		}
	}
}