package ru.fintech.db.hw1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DB on 12.11.2017.
 */

public class Node {
    int value;
    List<Node> children;

    Node(int val) {value = val;
    children = new ArrayList<>();}
}
