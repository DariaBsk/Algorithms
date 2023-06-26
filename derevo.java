// Необходимо превратить собранное на семинаре дерево поиска в полноценное левостороннее красно-черное дерево.
// И реализовать в нем метод добавления новых элементов с балансировкой.

// Красно-черное дерево имеет следующие критерии:
// • Каждая нода имеет цвет (красный или черный)
// • Корень дерева всегда черный
// • Новая нода всегда красная
// • Красные ноды могут быть только левым ребенком
// • У краной ноды все дети черного цвета

// Соответственно, чтобы данные условия выполнялись, после добавления элемента в дерево необходимо произвести балансировку,
// благодаря которой все критерии выше станут валидными. Для балансировки существует 3 операции – левый малый поворот,
// правый малый поворот и смена цвета.

// Реализация двух структур данных: HashMap и Tree.
// HashMap - это хэш-таблица, которая используется для хранения пар ключ-значение. Она представлена классом HashMap,
// который содержит в себе массив корзин (baskets) и методы для добавления, поиска и удаления элементов (find, remove, push).
// Каждая корзина представлена классом Basket, который содержит связный список элементов (Node). Каждый элемент списка
// представлен классом Node, который содержит объект Entity (ключ-значение) и ссылку на следующий элемент списка. Методы push,
// find и remove в классе Basket работают за O(1), так как они выполняются в пределах одной корзины и обходят только один связаный список.
// Tree - это бинарное дерево поиска, которое используется для хранения упорядоченных значений. Оно представлено классом Tree, 
// который содержит в себе ссылку на корень дерева (root) и методы для поиска и вставки элементов. Каждый узел дерева представлен 
// классом Node, который содержит значение элемента, ссылки на левого и правого потомков и поле цвета (color). В данной реализации 
// используется красно-черное дерево, где черный цвет узла означает, что его поддерево удовлетворяет условиям балансировки, 
// а красный цвет - нарушает эти условия. Методы find и insert в классе Tree работают за O(log n), так как они выполняются в пределах 
// дерева и обходят только часть его элементов.

class HashMap {
	    class Entity {
	        int key;
	        int value;
	    }
	
	    class Basket {
	        Node head;
	
	        class Node {
	            Entity entity;
	            Node next;
	        }
	
	        public Integer find(int key) { 
	            Node node = head;
	            while (node != null) {
	                if (node.entity.key == key) {
	                    return node.entity.value;
	                }
	                node = node.next;
	            }
	            return null;
	        }
	
	        public boolean push(Entity entity) { 
	            Node node = new Node();
	            node.entity = entity;
	
	            if (head == null) {
	                head = node;
	            } else {
	                if (head.entity.key == entity.key) {
	                    return false;
	                } else {
	                    Node cur = head;
	                    while (cur.next != null) {
	                        if (cur.next.entity.key == entity.key) {
	                            return false;
	                        }
	                        cur = cur.next;
	                    }
	                    cur.next = node;
	                }
	            }
	            return true;
	        }
	
	        public boolean remove(int key) { 
	            if (head != null) {
	                if (head.entity.key == key) {
	                    head = head.next;
	                    return true;
	                } else {
	                    Node cur = head;
	                    while (cur.next != null) {
	                        if (cur.next.entity.key == key) {
	                            cur.next = cur.next.next;
	                            return true;
	                        }
	                        cur = cur.next;
	                    }
	                }
	            }
	            return false;
	        }
	    }
	
	    static final int INIT_SIZE = 16;
	
	    Basket[] baskets;
	
	    public HashMap() {
	        this(INIT_SIZE);
	    }
	
	    public HashMap(int size) {
	        baskets = new Basket[size];
	    }
	
	    private int getIndex(int key) {
	        return key % baskets.length;
	    }
	
	    public Integer find(int key) { 
	        int index = getIndex(key);
	        Basket basket = baskets[index];
	        if (basket != null) {
	            return basket.find(key);
	        }
	        return null;
	    }
	
	    public boolean push(int key, int value) {
	        int index = getIndex(key);
	        Basket basket = baskets[index];
	        if (basket == null) {
	            basket = new Basket();
	            baskets[index] = basket;
	        }
	        Entity entity = new Entity();
	        entity.key = key;
	        entity.value = value;
	        return basket.push(entity);
	    }
	
	    public boolean remove(int key) {
	        int index = getIndex(key);
	        Basket basket = baskets[index];
	        if (basket != null) {
	            return basket.remove(key);
	        }
	        return false;
	    }
	}
	
	class Tree {
	    Node root;
	
	    class Node {
	        int value;
	        Node left;
	        Node right;
	        boolean color; // добавлено поле цвета
	
	        static final boolean RED = true;
	        static final boolean BLACK = false;
	    }
	
	    public Node find(int value) {
	        return find(root, value);
	    }
	
	    private Node find(Node node, int value) {
	        if (node == null) {
	            return null;
	        }
	        if (node.value == value) {
	            return node;
	        }
	        if (node.value < value) {
	            return find(node.right, value);
	        } else {
	            return find(node.left, value);
	        }
	    }
	
	    public void insert(int value) {
	        if (root == null) {
	            root = new Node();
	            root.value = value;
	            root.color = Node.BLACK; // корень всегда черный
	        } else {
	            insert(root, value);
	            root.color = Node.BLACK; // после балансировки корень становится черным
	        }
	    }
	
	    private Node insert(Node node, int value) {
	        if (node == null) {
	            node = new Node();
	            node.value = value;
	            node.color = Node.RED; // новая нода всегда красная
	        } else if (value < node.value) {
	            node.left = insert(node.left, value);
	        } else if (value > node.value) {
	            node.right = insert(node.right, value);
	        }
	
	        // балансировка дерева
	        if (isRed(node.right) && !isRed(node.left)) {
	            node = rotateLeft(node);
	        }
	        if (isRed(node.left) && isRed(node.left.left)) {
	            node = rotateRight(node);
	        }
	        if (isRed(node.left) && isRed(node.right)) {
	            flipColors(node);
	        }
	
	        return node;
	    }
	
	    private boolean isRed(Node node) {
	        if (node == null) {
	            return false;
	        }
	        return node.color == Node.RED;
	    }
	
	    private Node rotateLeft(Node node) {
	        Node x = node.right;
	        node.right = x.left;
	        x.left = node;
	        x.color = node.color;
	        node.color = Node.RED;
	        return x;
	    }
	
	    private Node rotateRight(Node node) {
	        Node x = node.left;
	        node.left = x.right;
	        x.right = node;
	        x.color = node.color;
	        node.color = Node.RED;
	        return x;
	    }
	
	    private void flipColors(Node node) {
	        node.color = Node.RED;
	        node.left.color = Node.BLACK;
	        node.right.color = Node.BLACK;
	    }
	}
	
	public class Main {
	    public static void main(String[] args) {
	        HashMap hashMap = new HashMap();
	        hashMap.push(1, 10);
	        hashMap.push(2, 20);
	        System.out.println(hashMap.find(1));
	        System.out.println(hashMap.find(2));
	
	        Tree tree = new Tree();
	        tree.insert(5);
	        tree.insert(3);
	        tree.insert(7);
	        System.out.println(tree.find(5));
	        System.out.println(tree.find(4));
	    }
	}

