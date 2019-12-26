package com.yyj;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

import com.mj.BinarySearchTree.Node;
import com.mj.printer.BinaryTreeInfo;

@SuppressWarnings("unchecked")

public class BinarySearchTree<E> implements BinaryTreeInfo{ //实现这个可以打印
	
	private int size;
	private Node<E> root;
	private Comparator<E> comparator; //比较器，java提供，可以提供比较规则
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public BinarySearchTree() {
		this(null);
	}
	
	public BinarySearchTree(Comparator<E> comparator) {
		this.comparator = comparator;
	}
	
	
	//添加元素
	public void add(E element) {
		elementNotNulCheck(element);
		
		if (root == null) { //root 节点
			root = new Node<>(element, null);
			size++;
			return;
		}
		
		//找到目标节点，并保存新节点比目标节点的大小
		Node<E> tmp = root;
		Node<E> parent = root; //保存节点
		int cmp = 0;  //比较结果
		while (tmp != null) {
			parent = tmp;
			cmp = compare(element, tmp.element);
			if (cmp>0) {//肯定是往右边加
				tmp = tmp.right;
			}else if (cmp<0) {
				tmp = tmp.left;
			}else {
				//相等，就覆盖
				tmp.element = element;
				return;
			}
		}
		
		//目标位置添加
		Node<E> newNode = new Node<>(element, parent);
		if (cmp > 0) {
			parent.right = newNode;
		}else {
			parent.left = newNode;
		}
		size++;
	}
	
	/**
	 * 递归 前序遍历
	 * 树状结构展示（打印)
	 * */
	public void preOrderTraversal(Visitor<E> visitor) {
		preOrderTraversal(root, visitor);
	}
	
	private void preOrderTraversal(Node<E> node, Visitor<E> visitor) {
		if (node == null || visitor == null) return;
//		System.out.println(node.element);
		visitor.visit(node.element);
		preOrderTraversal(node.left, visitor);
		preOrderTraversal(node.right, visitor);
	}

	/**
	 * 递归 中序遍历
	 * 二叉搜索树升序降序处理节点
	 * */
	public void inOrderTraversal(Visitor<E> visitor) {
		inOrderTraversal(root, visitor);
	}
	private void inOrderTraversal(Node<E> node, Visitor<E> visitor) {
		if (node == null || visitor == null) return;
		inOrderTraversal(node.left, visitor);
//		System.out.println(node.element);
		visitor.visit(node.element);
		inOrderTraversal(node.right, visitor);
	}
	
	/**
	 * 递归 后序遍历
	 * 适用于一些先子后父的操作
	 * */
	public void postOrderTraversal(Visitor<E> visitor) {
		postOrderTraversal(root, visitor);
	}
	private void postOrderTraversal(Node<E> node, Visitor<E> visitor) {
		if (node == null) return;
		postOrderTraversal(node.left, visitor);
		postOrderTraversal(node.right, visitor);
		visitor.visit(node.element);
//		System.out.println(node.element);
	}
	
	/**
	 * 迭代 层序遍历
	 * 计算二叉树的高度
	 * 判断一颗二叉树是否是完全二叉树
	 * */
	public void levelOrderTraversal(Visitor<E> visitor) {
		//存放元素
		Queue<Node<E>> queue = new LinkedList<>();
		queue.offer(root);

		while (!queue.isEmpty()) {
			//取出并清空队列
			Node<E> node = queue.poll(); 
//			System.out.println(node.element);
			visitor.visit(node.element);
			//下一层的放入队列
			if (node.left != null) {
				queue.offer(node.left);
			}
			if (node.right != null) {
				queue.offer(node.right);
			}
		}
	}
	
	/**
	 * 利用层序遍历，判断是否为完全二叉树
	 * */
	public boolean isCompleteTree() {
		if (root == null) return false;
		
		Queue<Node<E>> queue = new LinkedList<>();
		queue.offer(root);
		Boolean isMustBeLeaf = false;
		
		while (!queue.isEmpty()) {
			Node<E> node = queue.poll();
			
			if (isMustBeLeaf && !node.isLeaf()) return false;
				
			if (node.haveTwoChildren()) {
				//最有都有就入队迭代
				queue.offer(node.left);
				queue.offer(node.right);
			}else if (node.left == null && node.right != null) {
				//左边为空， 右边不为空不是完全二叉树
				return false;
			}else {
				//(node.left!= null && node.right== null) || (node.left == null && node.right == null)
				//如左子树不为空，并且右子树为空， 或者左子树右子树都为空, 
				//那么他们的后续节点都为叶子节点才是完全二叉树
				isMustBeLeaf = true;
			}
						
		}
		return true;
	}
	
	public void clear() {
		
	}
	
	public void remove(E element) {
		
	}
	
	public boolean contains(E element) {
		return false;
	}
	
	/**
	 * 二叉搜索树高度  递归
	 * */
	public int height() {
		return height(root, 0);
	}
	
	private int height(Node<E> node, int height) {
		if (node == null) return height;
		height += 1;
		int maxheight = Math.max(height(node.left, height), height(node.right, height));
		return maxheight;
	}
	
	/**
	 * 老师写的递归
	 * */
	public int height2() {
		return height(root);
	}
	
	private int height(Node<E> node) {
		if (node == null) return 0;
		return 1 + Math.max(height(node.left), height(node.right));
	}
	
	/**
	 * 层序遍历计算高度
	 * */
	public int treeHeight() {
		if (root == null) return 0;

		Queue<Node<E>> queue = new LinkedList<>();
		queue.offer(root);
		int leverSize = 1; //第一层只有一个root
		int height = 0; 
		while (!queue.isEmpty()) {
			Node<E> node = queue.poll();
			leverSize --;
						
			if (node.left != null) {
				queue.offer(node.left);
			}
			if (node.right != null) {
				queue.offer(node.right);
			}
			//这个方法很关键
			if (leverSize == 0) { //一层没有了，开始下一层了
				leverSize = queue.size(); //队列里有几个元素，这一层就有多少个
				height++;
			}

		}
		return height;
	}
	
	//如果 e1=e2返回0 e1>e2返回>0 e1<e2返回小于0
	private int compare(E e1, E e2) {
		//如果有比较器调用比较器的比较方法
		if (comparator != null) {
			return comparator.compare(e1, e2);
		}		
		//java.lang 系统的类型都有默认实现
		return ((Comparable<E>)e1).compareTo(e2);
	}
	
	private void elementNotNulCheck(E element) {
		if (element == null) {
			throw new IllegalArgumentException("element should not be null");
		}
	}
	
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		toString(root, sb, "");
		return sb.toString();
	}
	
	/**
	 * 利用前序遍历打印二叉树
	 * */
	private void toString(Node<E> node, StringBuffer sb, String prefix) {
		if (node == null) return;
		sb.append(prefix).append(node.element).append("\n");
		toString(node.left, sb, prefix+"L--");
		toString(node.right, sb, prefix+"R--");
	}
	
	private static class Node<E> {
		E element;
		Node<E> left;
		Node<E> right;
		Node<E> parent;
		public Node(E element, Node<E> parent) {
			this.element = element;
			this.parent = parent;
		}
		
		public boolean haveTwoChildren() {
			return left != null && right != null;
		}
		
		public boolean isLeaf() {
			return left == null && right == null;
		}
	}
	
	//访问器，供外部访问遍历结果
	public static interface Visitor<E> {
		void visit(E element);
	}

	
	/**
	 * 打印相关
	 * */
	@Override
	public Object root() {
		// TODO Auto-generated method stub
		return root;
	}

	@Override
	public Object left(Object node) {
		// TODO Auto-generated method stub
		return ((Node<E>)node).left;
	}

	@Override
	public Object right(Object node) {
		// TODO Auto-generated method stub
		return ((Node<E>)node).right;
	}

	@Override
	public Object string(Object node) {
		Node<E> myNode = (Node<E>)node;
		String parentString = "null";
		if (myNode.parent != null) {
			parentString = myNode.parent.element.toString();
		}
		return myNode.element + "_p(" + parentString + ")";
	}
	
	
}
