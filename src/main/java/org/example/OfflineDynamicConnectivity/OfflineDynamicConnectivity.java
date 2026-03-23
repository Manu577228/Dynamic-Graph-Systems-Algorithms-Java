package org.example.OfflineDynamicConnectivity;

//Offline Dynamic Connectivity is a technique to answer connectivity
//queries in a graph where edges are added and removed,
// by processing all operations offline (known beforehand).
//It uses Divide & Conquer + DSU (Union-Find) with rollback to
//efficiently simulate dynamic changes.

import java.util.*;
import java.io.*;

public class OfflineDynamicConnectivity {
    static class DSU {
        int[] parent, size;
        Stack<int[]> history;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            history = new Stack<>();

            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            while (x != parent[x]) x = parent[x];
            return x;
        }

        void union(int a, int b) {
            a = find(a);
            b = find(b);

            if (a == b) {
                history.push(new int[]{
                        -1, -1, -1
                });

                return;
            }

            if (size[a] < size[b]) {
                int temp = a;
                a = b;
                b = temp;
            }

            history.push(new int[]{
                    b, parent[b], size[a]
            });

            parent[b] = a;
            size[a] += size[b];
        }

        void rollback() {
            int[] last = history.pop();

            if (last[0] == -1) return;

            int b = last[0];
            int oldParent = last[1];
            int oldSize = last[2];

            int a = parent[b];

            parent[b] = oldParent;
            size[a] = oldSize;
        }
    }

    static List<int[]>[] seg;
    static List<String> queries;

    static void addEdge(int node, int l, int r, int ql, int qr, int u, int v) {
        if (qr < l || r < ql) return;

        if (ql <= l && r <= qr) {
            seg[node].add(new int[]{
                    u, v
            });
            return;
        }

        int mid = (l + r) / 2;
        addEdge(node * 2, l, mid, ql, qr, u, v);
        addEdge(node * 2 + 1, mid + 1, r, ql, qr, u, v);
    }

    static void dfs(int node, int l, int r, DSU dsu) {
        int changes = 0;

        for (int[] e : seg[node]) {
            dsu.union(e[0], e[1]);
            changes++;
        }

        if (l == r) {
            String[] parts = queries.get(l).split(" ");

            if (parts[0].equals("query")) {
                int u = Integer.parseInt(parts[1]);
                int v = Integer.parseInt(parts[2]);

                if (dsu.find(u) == dsu.find(v)) {
                    System.out.println("YES");
                } else {
                    System.out.println("NO");
                }
            }
        } else {
            int mid = (l + r) / 2;

            dfs(node * 2, l, mid, dsu);
            dfs(node * 2 + 1, mid + 1, r, dsu);
        }

        while (changes-- > 0) {
            dsu.rollback();
        }
    }

    public static void main(String[] args) throws Exception {
        int n = 5; // nodes
        int q = 5; // operations

        queries = new ArrayList<>();
        queries.add("add 1 2");
        queries.add("add 2 3");
        queries.add("query 1 3");
        queries.add("remove 2 3");
        queries.add("query 1 3");

        seg = new ArrayList[4 * q];
        for (int i = 0; i < 4 * q; i++) seg[i] = new ArrayList<>();

        Map<String, Integer> map = new HashMap<>();

        for (int i = 0; i < q; i++) {
            String[] parts = queries.get(i).split(" ");
            if (parts[0].equals("add")) {
                String key = parts[1] + " " + parts[2];
                map.put(key, i);
            } else if (parts[0].equals("remove")) {
                String key = parts[1] + " " + parts[2];
                int start = map.get(key);
                map.remove(key);

                addEdge(1, 0, q - 1, start, i - 1,
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            }
        }

        for (String key : map.keySet()) {
            int start = map.get(key);
            String[] p = key.split(" ");
            addEdge(1, 0, q - 1, start, q - 1,
                    Integer.parseInt(p[0]),
                    Integer.parseInt(p[1]));
        }

        DSU dsu = new DSU(n);
        dfs(1, 0, q - 1, dsu);
    }
}
