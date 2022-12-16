package Day23;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import static java.lang.System.out;

public class Day23 {

    public static void main(String[] args) {

        var seed = "487912365";
        var nodesSmall = genCupsSmall(seed);

        var current = nodesSmall.get(Character.getNumericValue(seed.charAt(0)));
        for (var i = 0; i < 100; i++) {
            current = doRound(current, nodesSmall);
        }
        out.print("Task 01: ");
        printNode(nodesSmall.get(1));

        var nodesLarge = genCupsLarge(seed);
        current = nodesLarge.get(Character.getNumericValue(seed.charAt(0)));
        for (var i = 0; i < 10000000L; i++){
            current = doRound(current, nodesLarge);
        }
        var nodeOne = nodesLarge.get(1);
        var res = (long) nodeOne.next.value * nodeOne.next.next.value;
        out.println("Task 02: " + res);
    }

    static @NotNull HashMap<Integer, Node> genCupsLarge(String data){
        var nodeMap = genCupsSmall(data);
        var prev = nodeMap.get(Character.getNumericValue(data.charAt(data.length() - 1)));
        var head = nodeMap.get(Character.getNumericValue(data.charAt(0)));
        for (var i = 10; i <= 1000000; i++){
            var current = new Node(i);
            nodeMap.put(i, current);
            current.previous = prev;
            prev.next = current;
            prev = current;
        }
        head.previous = prev;
        prev.next = head;
        return nodeMap;
    }

    static @NotNull HashMap<Integer, Node> genCupsSmall(@NotNull String data){
        var head = new Node(Character.getNumericValue(data.charAt(0)));
        var prev = head;
        var nodeMap = new HashMap<Integer, Node>();
        nodeMap.put(head.value, head);
        for (var i = 1; i < data.length(); i++) {
            var current = new Node(Character.getNumericValue(data.charAt(i)));
            nodeMap.put(current.value, current);
            current.previous = prev;
            prev.next = current;
            prev = current;
        }
        head.previous = prev;
        prev.next = head;
        return nodeMap;
    }

    static Node doRound(@NotNull Node current, HashMap<Integer, Node> nodeMap) {
        var pickedUp = new HashSet<Integer>();
        var next = current.next;
        var pickUpStart = next;
        for (var i = 0; i < 3; i++) {
            pickedUp.add(next.value);
            next = next.next;
        }
        var pickUpAdj = next;
        var pickUpEnd = next.previous;
        var destination = current.value - 1;
        if (destination == 0) destination = nodeMap.size();
        while (pickedUp.contains(destination)) {
            destination--;
            if (destination < 1) {
                destination = nodeMap.size();
            }
        }
        current.next = pickUpAdj;
        pickUpAdj.previous = current;
        var nodeDest = nodeMap.get(destination);
        var nodeDestNext = nodeDest.next;
        nodeDest.next = pickUpStart;
        pickUpStart.previous = nodeDest;
        pickUpEnd.next = nodeDestNext;
        nodeDestNext.previous = pickUpEnd;
        return current.next;
    }

    static void printNode(@NotNull Node head) {
        var value = head.value;
        StringBuilder str = new StringBuilder();
        var current = head.next;
        while (!Objects.equals(current.value, value)) {
            str.append(current.value);
            current = current.next;
        }
        out.println(str);
    }

}

class Node {
    Node next;
    Node previous;

    Integer value;

    public Node(Integer value) {
        this.value = value;
        this.next = null;
        this.previous = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return new EqualsBuilder().append(value, node.value).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(value).toHashCode();
    }

    @Override
    public String toString() {
        return "Node: " + this.value;
    }

}
