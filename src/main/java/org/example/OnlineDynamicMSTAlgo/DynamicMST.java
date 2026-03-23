package org.example.OnlineDynamicMSTAlgo;

//Online Dynamic MST (Incremental) maintains a Minimum Spanning Tree (MST)
//while edges are added one by one.
//After each insertion, the MST is updated without recomputing from scratch.
//It replaces heavier edges in cycles to keep total weight minimal.

import java.io.*;
import java.util.*;

public class DynamicMST {
    static class E {
        int u, v, w;

        E(int a, int b, int c) {
            u = a;
            v = b;
            w = c;
        }
    }

    static int n = 4;
    static List<E> mst = new ArrayList<>();

    static boolean dfs(int u, int target, boolean[] vis, List<E> path, List<E> res) {
        if (u == target) {
            res.addAll(path);
            return true;
        }

        vis[u] = true;

        for (E e : mst) {
            int nxt = -1;

            if (e.u == u) nxt = e.v;
            else if (e.v == u) nxt = e.u;

            if (nxt != -1 && !vis[nxt]) {
                path.add(e);
                if (dfs(nxt, target, vis, path, res)) return true;
                path.remove(path.size() - 1);
            }
        }

        return false;
    }

    static void addEdge(int u, int v, int w) {
        boolean[] vis = new boolean[n];
        List<E> path = new ArrayList<>();
        List<E> res = new ArrayList<>();

        if (!dfs(u, v, vis, path, res)) {
            mst.add(new E(u, v, w));
            System.out.println("Added edge: " + u + "-" + v + " weight " + w);
        } else {
            E maxEdge = res.get(0);

            for (E e : res) {
                if (e.w > maxEdge.w) maxEdge = e;
            }

            if (w < maxEdge.w) {
                mst.remove(maxEdge);
                mst.add(new E(u, v, w));

                System.out.println("Replaced edge (" + maxEdge.u + "-" + maxEdge.v + ") with (" + u + "-" + v + ")");
            } else {
                System.out.println("Ignored edge: " + u + "-" + v);
            }
        }
    }

    public static void main(String[] args) {
        addEdge(0, 1, 4);
        addEdge(1, 2, 3);
        addEdge(0, 2, 2);

        System.out.println("\n Final MST:");

        for (E e : mst) {
            System.out.println(e.u + " - " + e.v + " : " + e.w);
        }
    }


}
