package org.example.HolmdeLichtenbergThorupAlgorithm;

//HDT maintains a spanning forest of a dynamic graph
//under edge insertions and deletions, answering connectivity queries in O(log²n)
//amortized time. It uses a hierarchy of Euler Tour Trees across O(log n)
//levels to efficiently detect replacement edges after deletions.

import java.util.*;

public class HolmdeLichtenbergThorup {
    int n;
    List<Set<Integer>> adj;
    List<Set<Integer>> treeAdj;
    int[] parent, rank;

    public HolmdeLichtenbergThorup(int n) {
        this.n = n;
        adj = new ArrayList<>();
        treeAdj = new ArrayList<>();
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            adj.add(new HashSet<>());
            treeAdj.add(new HashSet<>());
            parent[i] = i;
            rank[i] = 0;
        }
    }

    int find(int x) {
        if (parent[x] != x)
            parent[x] = find(parent[x]);
        return parent[x];
    }

    void union(int x, int y) {
        int rx = find(x);
        int ry = find(y);

        if (rx == ry) return;
        if (rank[rx] < rank[ry]) {
            int t = rx;
            rx = ry;
            ry = t;
        }

        parent[ry] = rx;
        if (rank[rx] == rank[ry]) rank[rx]++;
    }

    void insert(int u, int v) {
        adj.get(u).add(v);
        adj.get(v).add(u);

        if (find(u) != find(v)) {
            union(u, v);
            treeAdj.get(u).add(v);
            treeAdj.get(v).add(u);
        }
    }

    void delete(int u, int v) {
        adj.get(u).remove(v);
        adj.get(v).remove(u);

        boolean wasTree = treeAdj.get(u).contains(v);
        if (!wasTree) return;

        treeAdj.get(u).remove(v);
        treeAdj.get(v).remove(u);
        rebuildUnionFind();

        Set<Integer> compU = getComponent(u);
        for (int node : compU) {
            for (int neighbor : adj.get(node)) {
                if (!compU.contains(neighbor)) {
                    union(node, neighbor);
                    treeAdj.get(node).add(neighbor);
                    treeAdj.get(neighbor).add(node);
                    return;
                }
            }
        }
    }

    void rebuildUnionFind() {
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }

        for (int u = 0; u < n; u++)
            for (int v : treeAdj.get(u))
                union(u, v);
    }

    Set<Integer> getComponent(int start) {
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            int curr = queue.poll();
            for (int nb : treeAdj.get(curr))
                if (visited.add(nb)) queue.add(nb);
        }

        return visited;
    }

    boolean connected(int u, int v) {
        return find(u) == find(v);
    }

    public static void main(String[] args) {
        HolmdeLichtenbergThorup g = new HolmdeLichtenbergThorup(4);

        g.insert(0, 1);
        g.insert(1, 2);
        g.insert(2, 3);
        g.insert(0, 2);

        System.out.println("Connected 0-3? " + g.connected(0, 3));

        g.delete(1, 2);
        System.out.println("Connected 0-3? " + g.connected(0, 3));

        g.delete(0, 2);
        System.out.println("Connected 0-3? " + g.connected(0, 3));
    }
}
