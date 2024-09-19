tree grammar SpecTreeParser;

options {
  language = Java;
  //output = AST;
  tokenVocab = SpecLexer;
  ASTLabelType = CommonTree;
}

@header {
  package eshmun.parser.antlr.tree;
  
  import eshmun.expression.*;
  import eshmun.expression.ctl.*;
  import eshmun.expression.mpctl.*;
  import eshmun.expression.atomic.arithmetic.*;
  import eshmun.expression.atomic.bool.*;
  import eshmun.expression.atomic.string.*;
  import eshmun.expression.propoperator.*;
  import eshmun.lts.syncskel.concurrentprogram.*;
  import eshmun.expression.atomic.*;
}

@members {
  private ComparisonOperator buildVarVarOperator(String op, String left, String right) {
    ComparisonOperator operator = null;
    ExpressionVariable leftValue = new ExpressionVariable(left);
    ExpressionVariable rightValue = new ExpressionVariable(right);
    if (op.equals("<")) {
      operator = new LessThanOperator(leftValue, rightValue);
    } else if (op.equals("<=")) {
      operator = new LessThanEqualOperator(leftValue, rightValue);
    } else if (op.equals(">")) {
      operator = new GreaterThanOperator(leftValue, rightValue);
    } else if (op.equals("<=")) {
      operator = new GreaterThanEqualOperator(leftValue, rightValue);
    } else if (op.equals("==")) {
      operator = new EqualOperator(leftValue, rightValue);
    } else if (op.equals("!=")) {
      operator = new NotEqualOperator(leftValue, rightValue);
    }
    return operator;
  }
  
  private ComparisonOperator buildVarConstOperator(String op, String left, float right) {
    ComparisonOperator operator = null;
    ArithmeticVariable leftValue = new ArithmeticVariable(left);
    ArithmeticConstant rightValue = new ArithmeticConstant(right);
    if (op.equals("<")) {
      operator = new LessThanOperator(leftValue, rightValue);
    } else if (op.equals("<=")) {
      operator = new LessThanEqualOperator(leftValue, rightValue);
    } else if (op.equals(">")) {
      operator = new GreaterThanOperator(leftValue, rightValue);
    } else if (op.equals("<=")) {
      operator = new GreaterThanEqualOperator(leftValue, rightValue);
    } else if (op.equals("==")) {
      operator = new EqualOperator(leftValue, rightValue);
    } else if (op.equals("!=")) {
      operator = new NotEqualOperator(leftValue, rightValue);
    }
    return operator;
  }
  
  private ComparisonOperator buildVarStringOperator(String op, String left, String right) {
    ComparisonOperator operator = null;
    StringVariable leftValue = new StringVariable(left);
    StringConstant rightValue = new StringConstant(right);
    if (op.equals("<")) {
      operator = new LessThanOperator(leftValue, rightValue);
    } else if (op.equals("<=")) {
      operator = new LessThanEqualOperator(leftValue, rightValue);
    } else if (op.equals(">")) {
      operator = new GreaterThanOperator(leftValue, rightValue);
    } else if (op.equals("<=")) {
      operator = new GreaterThanEqualOperator(leftValue, rightValue);
    } else if (op.equals("==")) {
      operator = new EqualOperator(leftValue, rightValue);
    } else if (op.equals("!=")) {
      operator = new NotEqualOperator(leftValue, rightValue);
    }
    return operator;
  }
  
  private BooleanPredicate buildVarBoolOperator(String var) {
    BooleanVariable value = new BooleanVariable(var);
    BooleanPredicate operator = new BooleanPredicate(value);
    return operator;
  }
  
  // This converts the text of a given AST node to a double.
  private float toFloat(String text) {
    float value = 0.0f;
    try {
      value = Float.parseFloat(text);
    } catch (NumberFormatException e) {
      throw new RuntimeException("Cannot convert \"" + text + "\" to a float.");
    }
    return value;
  }
}

ctl_formula returns [PredicateFormula ctlFormula]: 
  /*ctl_state_formula
  {
    $ctlFormula = $ctl_state_formula.ctlStateFormula;
  }
  |*/ mpctl_formula
  {
    $ctlFormula = $mpctl_formula.mpctlFormula;
  };

mpctl_formula returns [PredicateFormula mpctlFormula]:
  ^(conn=MPCTL_CONNECTIVE mpctl_state_formula)
  {
     if ($conn.text.startsWith("AND")) {
        $mpctlFormula = new MPCTLAndOperator($conn.text, $mpctl_state_formula.mpctlStateFormula);
     } else if ($conn.text.startsWith("OR")) {
        $mpctlFormula = new MPCTLOrOperator($conn.text, $mpctl_state_formula.mpctlStateFormula);
     } 
  };
  
mpctl_state_formula returns [PredicateFormula mpctlStateFormula]
scope { PredicateFormula oper; }
: 
  ^(MPCTL_STATE_FORMULA leftSubFormula = mpctl_state_sub_formula {$mpctl_state_formula::oper = $leftSubFormula.subFormula;}
      (
        operator = PROPOSITIONAL_CONNECTIVE 
        rightSubFormula = mpctl_state_sub_formula 
        {
          if ($operator.text.equals("&")) {
            $mpctl_state_formula::oper = new AndOperator($mpctl_state_formula::oper, $rightSubFormula.subFormula);
          } else if ($operator.text.equals("|")) {
            $mpctl_state_formula::oper = new OrOperator($mpctl_state_formula::oper, $rightSubFormula.subFormula);
          } else if ($operator.text.equals("=>")) {
            $mpctl_state_formula::oper = new ImpliesOperator($mpctl_state_formula::oper, $rightSubFormula.subFormula);
          } else if ($operator.text.equals("<=>")) {
            $mpctl_state_formula::oper = new EquivalentOperator($mpctl_state_formula::oper, $rightSubFormula.subFormula);
          }           
        }
      )*
  )
  {
      $mpctlStateFormula = $mpctl_state_formula::oper;
  };
  
mpctl_state_sub_formula returns [PredicateFormula subFormula]
  : mpctl_neg_sub_formula {$subFormula = $mpctl_neg_sub_formula.subFormula;}
  |^(MPCTL_STATE_SUB_FORMULA op=CTL_BRANCH_OP path_formula=mpctl_path_formula[$op.text])
  {$subFormula =  $path_formula.subFormula;}
  | atomic_formula {$subFormula =  $atomic_formula.subFormula;};
  
mpctl_neg_sub_formula returns [PredicateFormula subFormula]: 
  ^(MPCTL_STATE_SUB_FORMULA neg = (neg = NOT_CONNECTIVE)? mpctl_state_formula)
  {
    if ($neg == null) {
      $subFormula = $mpctl_state_formula.mpctlStateFormula;
    } else {
      $subFormula = new NotOperator($mpctl_state_formula.mpctlStateFormula);
    }
  };
  
  
mpctl_path_formula [String branchOperator] returns [PredicateFormula subFormula]
scope { List<String> indexNames; }
  : ^(MPCTL_PATH_FORMULA path_op = CTL_PATH_OP  
        (var = VAR 
          {
            if ($mpctl_path_formula::indexNames == null) $mpctl_path_formula::indexNames= new ArrayList<String>();
            $mpctl_path_formula::indexNames.add($var.text);
          }
        )*  mpctl_state_formula
    )
    {
      if ($branchOperator.equals("A")) {
        if($path_op.text.startsWith("X")) {
          $subFormula = new MPCTLAXOperator($branchOperator + $path_op.text, $mpctl_state_formula.mpctlStateFormula);
        } else if($path_op.text.startsWith("G")) {
          $subFormula = new MPCTLAGOperator($branchOperator + $path_op.text, $mpctl_state_formula.mpctlStateFormula);
        } else if($path_op.text.startsWith("F")) {
          $subFormula = new MPCTLAFOperator($branchOperator + $path_op.text, $mpctl_state_formula.mpctlStateFormula);
        }
      } else if ($branchOperator.equals("E")) {
        if($path_op.text.startsWith("X")) {
          $subFormula = new MPCTLEXOperator($branchOperator + $path_op.text, $mpctl_state_formula.mpctlStateFormula);
        } else if($path_op.text.startsWith("G")) {
          $subFormula = new MPCTLEGOperator($branchOperator + $path_op.text, $mpctl_state_formula.mpctlStateFormula);
        } else if($path_op.text.startsWith("F")) {
          $subFormula = new MPCTLEFOperator($branchOperator + $path_op.text, $mpctl_state_formula.mpctlStateFormula);
        } 
      }
      ((MPCTLParseTree)$subFormula).setIndexNames($mpctl_path_formula::indexNames);
    }
  | ^(MPCTL_PATH_FORMULA state_formula1 = mpctl_state_formula path_op = CTL_PATH_CONNECTIVE state_formula2 = mpctl_state_formula)
    {
      if ($branchOperator.equals("A")) {
        if ($path_op.text.equals("V")) {
          $subFormula = new AVOperator($state_formula1.mpctlStateFormula, $state_formula2.mpctlStateFormula);
        } else if ($path_op.text.equals("U")) {
          $subFormula = new AUOperator($state_formula1.mpctlStateFormula, $state_formula2.mpctlStateFormula);
        } else if ($path_op.text.equals("W")) {
          $subFormula = new AWOperator($state_formula1.mpctlStateFormula, $state_formula2.mpctlStateFormula);
        }  
      } else if ($branchOperator.equals("E")) {
        if ($path_op.text.equals("V")) {
          $subFormula = new EVOperator($state_formula1.mpctlStateFormula, $state_formula2.mpctlStateFormula);
        } else if ($path_op.text.equals("U")) {
          $subFormula = new EUOperator($state_formula1.mpctlStateFormula, $state_formula2.mpctlStateFormula);
        } else if ($path_op.text.equals("W")) {
          $subFormula = new EWOperator($state_formula1.mpctlStateFormula, $state_formula2.mpctlStateFormula);
        } 
      } 
    };


/*
ctl_state_formula returns [PredicateFormula ctlStateFormula]
scope { PredicateFormula oper; }
: 
  ^(
      CTL_STATE_FORMULA leftSubFormula = ctl_state_sub_formula {$ctl_state_formula::oper = $leftSubFormula.subFormula;}
      (
        operator = PROPOSITIONAL_CONNECTIVE 
        rightSubFormula = ctl_state_sub_formula 
        {
          if ($operator.text.equals("&")) {
            $ctl_state_formula::oper = new AndOperator($ctl_state_formula::oper, $rightSubFormula.subFormula);
          } else if ($operator.text.equals("|")) {
            $ctl_state_formula::oper = new OrOperator($ctl_state_formula::oper, $rightSubFormula.subFormula);
          } else if ($operator.text.equals("=>")) {
            $ctl_state_formula::oper = new ImpliesOperator($ctl_state_formula::oper, $rightSubFormula.subFormula);
          } else if ($operator.text.equals("<=>")) {
            $ctl_state_formula::oper = new EquivalentOperator($ctl_state_formula::oper, $rightSubFormula.subFormula);
          }           
        }
      )*
  )
  {
      $ctlStateFormula = $ctl_state_formula::oper;
  };
  


ctl_state_sub_formula returns [PredicateFormula subFormula]
  : ctl_neg_sub_formula {$subFormula = $ctl_neg_sub_formula.subFormula;}
  |^(CTL_STATE_SUB_FORMULA op=CTL_BRANCH_OP path_formula=ctl_path_formula[$op.text])
  {$subFormula =  $path_formula.subFormula;}
  | ^(CTL_STATE_SUB_FORMULA atomic_formula)
  { $subFormula =  $atomic_formula.subFormula;};

ctl_path_formula [String branchOperator] returns [PredicateFormula subFormula]
  : ^(CTL_PATH_FORMULA path_op = CTL_PATH_OP ctl_state_formula)
    {
      if ($branchOperator.equals("A")) {
        if ($path_op.text.equals("X")) {
          $subFormula = new AXOperator($ctl_state_formula.ctlStateFormula);
        } else if ($path_op.text.equals("G")) {
          $subFormula = new AGOperator($ctl_state_formula.ctlStateFormula);
        } else if ($path_op.text.equals("F")) {
          $subFormula = new AFOperator($ctl_state_formula.ctlStateFormula);
        } 
      } else if ($branchOperator.equals("E")) {
        if ($path_op.text.equals("X")) {
          $subFormula = new EXOperator($ctl_state_formula.ctlStateFormula);
        } else if ($path_op.text.equals("G")) {
          $subFormula = new EGOperator($ctl_state_formula.ctlStateFormula);
        } else if ($path_op.text.equals("F")) {
          $subFormula = new EFOperator($ctl_state_formula.ctlStateFormula);
        } 
      }
    }
  | ^(CTL_PATH_FORMULA state_formula1 = ctl_state_formula path_op = CTL_PATH_CONNECTIVE state_formula2 = ctl_state_formula)
    {
      if ($branchOperator.equals("A")) {
        if ($path_op.text.equals("V")) {
          $subFormula = new AVOperator($state_formula1.ctlStateFormula, $state_formula2.ctlStateFormula);
        } else if ($path_op.text.equals("U")) {
          $subFormula = new AUOperator($state_formula1.ctlStateFormula, $state_formula2.ctlStateFormula);
        } else if ($path_op.text.equals("W")) {
          $subFormula = new AWOperator($state_formula1.ctlStateFormula, $state_formula2.ctlStateFormula);
        }  
      } else if ($branchOperator.equals("E")) {
        if ($path_op.text.equals("V")) {
          $subFormula = new EVOperator($state_formula1.ctlStateFormula, $state_formula2.ctlStateFormula);
        } else if ($path_op.text.equals("U")) {
          $subFormula = new EUOperator($state_formula1.ctlStateFormula, $state_formula2.ctlStateFormula);
        } else if ($path_op.text.equals("W")) {
          $subFormula = new EWOperator($state_formula1.ctlStateFormula, $state_formula2.ctlStateFormula);
        } 
      } 
    };
  

ctl_neg_sub_formula returns [PredicateFormula subFormula]: 
  ^(CTL_STATE_SUB_FORMULA neg = (neg = NOT_CONNECTIVE)? ctl_state_formula)
  {
    if ($neg == null) {
      $subFormula = $ctl_state_formula.ctlStateFormula;
    } else {
      $subFormula = new NotOperator($ctl_state_formula.ctlStateFormula);
    }
  };
*/
atomic_formula returns [PredicateFormula subFormula] : 
  ^(ATOMIC_SUB_FORMULA var_expression)
  {
    $subFormula =  $var_expression.subFormula;
  }
  | ^(ATOMIC_SUB_FORMULA bool = BOOLEAN)
  {
    if ($bool.text != null && ($bool.text.equals("true") || $bool.text.equals("false"))) {
      boolean value = $bool.text.equals("true");
      $subFormula = new BooleanPredicate(new BooleanConstant(value));
    } 
  };
  
  
var_expression returns [PredicateFormula subFormula]
  : ^(op = COMPARISON_OPERATOR var1 = VAR var2 = VAR) 
  {
    $subFormula = buildVarVarOperator($op.text, $var1.text, $var2.text);
  }
  | ^(op = COMPARISON_OPERATOR var = VAR con = CONSTANT)
  {
    $subFormula = buildVarConstOperator($op.text, $var.text, toFloat($con.text));
  }
  | ^(op = COMPARISON_OPERATOR var=VAR str=STRING)
  {
    $subFormula = buildVarStringOperator($op.text, $var.text, $str.text);
  }
  | var = VAR
  {
    $subFormula = buildVarBoolOperator($var.text);
  };
  

  
  