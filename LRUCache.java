import java.util.*;

public class LRUCache {
    class Node{
        int key, value;
        Node next, prev;
        Node(int key, int value){
            this.key = key;
            this.value = value;
            next = prev = null;
        }
    }

    class Cache{
        private int capacity;
        private Map<Integer, Node> map;
        private Node head, tail;

        Cache(int capacity){
            this.capacity = capacity;
            map = new HashMap<>();
            head = new Node(-1, -1);
            tail = new Node(-1, -1);
            head.next = tail;
            tail.prev = head;
        }

        public void remove(Node node){
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        public void insert(Node node){
            node.next = head.next;
            head.next.prev = node;
            head.next = node;
            node.prev = head;
        }

        public int get(int key){
            if(!map.containsKey(key)) return -1;
            // Get the element from the map, delete it and then insert it at the front
            Node node = map.get(key);
            remove(node);
            insert(node);
            return node.value;
        }

        public void put(int key, int value){
            Node node = new Node(key, value);
            // Case 1: if element already exist in map
            if(map.containsKey(key)){
                Node temp = map.get(key);
                remove(temp);
                insert(temp);
            }
            // Case 2: if capacity is full
            if(map.size() == capacity){
                Node temp = tail.prev;
                remove(temp);
                map.remove(temp.key);
            }
            // Case 3: capacity is not full
            insert(node);
            map.put(key, node);
        }
    }
}
