package org.example.DSUwithRollbackAlgorithm;

//DSU with Rollback is an advanced version of
//Disjoint Set Union (Union-Find) that allows undoing previous union operations.
//It maintains a history of changes so we can efficiently revert to earlier states.
//Useful in offline queries, dynamic connectivity, and divide & conquer problems.

import java.io.*;
import java.util.*;

public class DSUWithRollback {
    static int[] parent;
    static int[] size;
    static Stack<int[]> history = new Stack<>();

    static int find(int x) {
        while (x != parent[x]) x = parent[x];
        return x;
    }

    static void union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);

        if (rootA == rootB) {
            history.push(new int[]{
                    -1, -1
            });

            return;
        }

        if (size[rootA] > size[rootB]) {
            int temp = rootA;
            rootA = rootB;
            rootB = temp;
        }

        history.push(new int[]{
                rootB, size[rootA]
        });

        parent[rootB] = rootA;
        size[rootA] += size[rootB];
    }

    static void rollback() {

        int[] last = history.pop();
        if (last[0] == -1) return;

        int rootB = last[0];
        int prevSize = last[1];

        int rootA = parent[rootB];

        parent[rootB] = rootB;
        size[rootA] = prevSize;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);

        int n = 5;

        parent = new int[n + 1];
        size = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        union(1, 2);
        union(2, 3);

        rollback();

        union(4, 5);


        for (int i = 1; i <= n; i++) {
            out.print(find(i) + " ");
        }

        out.flush();
    }
}

