package eshmun.DecisionProcedure;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import eshmun.expression.PredicateFormula;
import eshmun.expression.atomic.bool.BooleanPredicate;

public class DPGraph {
	
	private List<AndNode> _andNodes;
	private List<OrNode> _orNodes;
	private List<DPEdge> _edges;
	
	
	

	public List<AndNode> getAndNodes() {
		return _andNodes;
	}


	public List<OrNode> getOrNodes() {
		return _orNodes;
	}


	public List<DPEdge> getEdges() {
		return _edges;
	}


	public DPGraph() {
		_andNodes = new ArrayList<AndNode>();
		_orNodes = new ArrayList<OrNode>();
		_edges = new ArrayList<DPEdge>();
		
	}
	
	public AndNode AddAndNode(AndNode vertex)
	{
		if(!_andNodes.contains(vertex))
		{
			_andNodes.add(vertex);
			return vertex;
		}
		return _andNodes.get(_andNodes.indexOf(vertex));
	}
	
	private void RemoveAndNode(AndNode vertex)
	{
		_andNodes.remove(vertex);
	}
	private void RemoveOrNode(OrNode vertex)
	{
		_orNodes.remove(vertex);
	}
	
	public void DeleteNode(DPVertex vertex)
	{
		if(vertex instanceof AndNode)
		{
			RemoveAndNode((AndNode)vertex);	
		}
		if(vertex instanceof OrNode)
		{
			RemoveOrNode((OrNode)vertex);
			
		}
		List<DPEdge> edges = vertex.getIncomingEdges(); 
		for (DPEdge edge : edges) {
			DPVertex parent = edge.getvFrom();
			if(parent instanceof AndNode)
				((AndNode) parent).setToBeDeleted(true);
			parent.RemoveOutcomingEdge(edge);
			_edges.remove(edge);
		}
		edges = vertex.getEdges(); 
		for (DPEdge edge : edges) {
			DPVertex parent = edge.getvTo();
			parent.RemoveIncomingEdge(edge);
		}
	}
	
	public OrNode AddOrNode(OrNode vertex)
	{		
		if(!_orNodes.contains(vertex))
		{
			_orNodes.add(vertex);
			return vertex;
		}
		return _orNodes.get(_orNodes.indexOf(vertex));
	}
	
	public boolean ContainsAndNode(AndNode and)
	{
		return _andNodes.contains(and);
	}
	
	public boolean ContainsOrNode(OrNode or)
	{
		return _orNodes.contains(or);
	}
	public boolean isAndNodeDeleted(AndNode and)
	{
		return !(_andNodes.contains(and));
	}
	
	public boolean isOrNodeDeleted(OrNode or)
	{
		return !(_orNodes.contains(or));
	}
	
	public void addEdge(DPVertex vertexFrom, DPVertex vertexTo)
	{
		DPEdge edge = new DPEdge(vertexFrom, vertexTo);
		_edges.add(edge);
		vertexFrom.AddEdge(edge);
		vertexTo.AddIncomingEdge(edge);
		
	}
	
	public boolean CheckEF(DPVertex bNode, PredicateFormula f)
	{
		List<DPVertex> VisitedNodes = new ArrayList<DPVertex>();//means already in the queue or was in the queue
		Queue<DPVertex> queueA = new LinkedList<DPVertex>();
		queueA.add(bNode);	
		VisitedNodes.add(bNode);
		while(!queueA.isEmpty())
		{
			DPVertex vertex = queueA.poll();
			
				if(vertex instanceof AndNode && vertex.ContainsFormula(f))
				{
					return true;
				}
				else
				{	//so we just need to add the childre that are not visited yet disregarding their type 
					List<DPEdge> edges = vertex.getEdges();
					for (DPEdge edge : edges) {
						DPVertex child = edge.getvTo();
						if(!VisitedNodes.contains(child))
						{
							VisitedNodes.add(child);
							queueA.add(child);
						}
					}
				}
			
		}
		return false;
	}
	public boolean CheckEU(DPVertex bNode, PredicateFormula f, PredicateFormula g)
	{
		List<DPVertex> VisitedNodes = new ArrayList<DPVertex>();
		Queue<DPVertex> queueA = new LinkedList<DPVertex>();
		queueA.add(bNode);
		VisitedNodes.add(bNode);
		while(!queueA.isEmpty())
		{
			DPVertex vertex = queueA.poll();
			if(vertex instanceof AndNode)
			{
				if(vertex.ContainsFormula(g))
				{
					return true;
				}//only if the node contains f we need to visit it
				else if(vertex.ContainsFormula(f))
				{
					List<DPEdge> edges = vertex.getEdges();
					for (DPEdge edge : edges) {
						DPVertex child = edge.getvTo();
						if(!VisitedNodes.contains(child))
						{
							VisitedNodes.add(child);
							queueA.add(child);
						}
					}
				}
			}//we just need to add the children of ornodes that are not visited yet
			if(vertex instanceof OrNode)
			{
				List<DPEdge> edges = vertex.getEdges();
				for (DPEdge edge : edges) {
					DPVertex child = edge.getvTo();
					if(!VisitedNodes.contains(child))
					{
						VisitedNodes.add(child);
						queueA.add(child);
					}
				}
			}
			
		}
		return false;
	}
	
	public boolean CheckAF(DPVertex bNode, PredicateFormula f)
	{
		List<DPVertex> VisitedNodes = new ArrayList<DPVertex>();
		Stack<DPVertex> stack = new Stack<DPVertex>(); 
		stack.push(bNode);
		boolean atLeastOneContainsFormula = false;
		while(!stack.isEmpty())
		{
			DPVertex vertex = stack.pop();
			VisitedNodes.add(vertex);
			if(vertex instanceof AndNode)
			{
				if(!vertex.ContainsFormula(f))
				{
					List<DPEdge> edges = vertex.getEdges();
					boolean hasNewChildren = false;
					for (DPEdge edge : edges) {
						DPVertex child = edge.getvTo();
						if(!VisitedNodes.contains(child))
						{
							stack.push(child);
							hasNewChildren = true;
						}
					}
					//if the node has no f and has no children or all its children were visited and also have no f return false
					//if(edges.size() == 0 || !hasNewChildren )//msakr:fix:31-03-2015: we just need to check leaves
					if(edges.size() == 0)//means is leaf and doesnot contains formula
						return false;
					
				}
				else
				{
					atLeastOneContainsFormula = true;
				}
			}			
			if(vertex instanceof OrNode)
			{
				List<DPEdge> edges = vertex.getEdges();
				boolean hasNewChildren = false;
				for (DPEdge edge : edges) {
					DPVertex child = edge.getvTo();
					if(!VisitedNodes.contains(child))
					{
						stack.push(child);
						hasNewChildren = true;
					}
				}
				//if the node has no children or all its children were visited and also have no f return false
				//if(edges.size() == 0 || !hasNewChildren)//msakr:fix:31-03-2015: we just need to check leaves
				if(edges.size() == 0)
					return false;
			}
		}
		
		return atLeastOneContainsFormula;
	}
	
	public boolean CheckAU(DPVertex bNode, PredicateFormula f, PredicateFormula g)
	{
		List<DPVertex> VisitedNodes = new ArrayList<DPVertex>();
		Stack<DPVertex> stack = new Stack<DPVertex>(); 
		stack.push(bNode);
		while(!stack.isEmpty())
		{
			DPVertex vertex = stack.pop();
			VisitedNodes.add(vertex);
			if(vertex instanceof AndNode)
			{
				if(!vertex.ContainsFormula(g))
				{
					if(vertex.ContainsFormula(f))
					{
						List<DPEdge> edges = vertex.getEdges();
						boolean hasNewChildren = false;
						for (DPEdge edge : edges) {
							DPVertex child = edge.getvTo();
							if(!VisitedNodes.contains(child))
							{
								stack.push(child);
								hasNewChildren = true;
							}
						}
						//if the node has no f and has no children or all its children were visited and also have no f return false
						if(edges.size() == 0 || !hasNewChildren )
							return false;
						
					}
					else
						return false;
				}
			}			
			if(vertex instanceof OrNode)
			{
				List<DPEdge> edges = vertex.getEdges();
				boolean hasNewChildren = false;
				for (DPEdge edge : edges) {
					DPVertex child = edge.getvTo();
					if(!VisitedNodes.contains(child))
					{
						stack.push(child);
						hasNewChildren = true;
					}
				}
				//if the node has no children or all its children were visited and also have no f return false
				if(edges.size() == 0 || !hasNewChildren)
					return false;
			}
		}
		
		return true;
	}
	
	public boolean hasStartState()
	{
		for (OrNode orNode : _orNodes) {
			if(orNode.isStartState())
				return true;
		}
		return false;
	}
	
	public boolean OldWronCheckAU(DPVertex bNode, PredicateFormula f, PredicateFormula g)
	{
		List<DPVertex> VisitedNodes = new ArrayList<DPVertex>();
		Queue<DPVertex> queueA = new LinkedList<DPVertex>();
		queueA.add(bNode);
		VisitedNodes.add(bNode);
		while(!queueA.isEmpty())
		{
			DPVertex vertex = queueA.poll();
			if(vertex instanceof AndNode)
			{
				if(vertex.ContainsFormula(g))
				{
					return true;
				}
				else if(vertex.ContainsFormula(f))
				{
					List<DPEdge> edges = vertex.getEdges();
					for (DPEdge edge : edges) {
						DPVertex child = edge.getvTo();
						if(!VisitedNodes.contains(child))
						{
							VisitedNodes.add(child);
							queueA.add(child);
						}
					}
				}
				else
					return false;
			}
			if(vertex instanceof OrNode)
			{
				List<DPEdge> edges = vertex.getEdges();
				for (DPEdge edge : edges) {
					DPVertex child = edge.getvTo();
					if(!VisitedNodes.contains(child))
					{
						VisitedNodes.add(child);
						queueA.add(child);
					}
				}
			}
			
		}
		return false;
	}	

}
