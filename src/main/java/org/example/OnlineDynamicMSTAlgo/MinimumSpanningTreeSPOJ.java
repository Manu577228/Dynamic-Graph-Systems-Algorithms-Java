package org.example.OnlineDynamicMSTAlgo;

import java.util.*;
import java.io.*;

public class MinimumSpanningTreeSPOJ {
    static class Node {
        Node l, r, p;
        boolean rev;
        int val, max;
        Node maxNode;

        Node u, v;
    }

    static boolean isRoot(Node x) {
        return x.p == null || (x.p.l != x && x.p.r != x);
    }

    static void push(Node x) {
        if (x != null && x.rev) {
            Node t = x.l;
            x.l = x.r;
            x.r = t;
            if (x.l != null) x.l.rev ^= true;
            if (x.r != null) x.r.rev ^= true;
            x.rev = false;
        }
    }

    static void pull(Node x) {
        x.max = x.val;
        x.maxNode = x;

        if (x.l != null && x.l.max > x.max) {
            x.max = x.l.max;
            x.maxNode = x.l.maxNode;
        }

        if (x.r != null && x.r.max > x.max) {
            x.max = x.r.max;
            x.maxNode = x.r.maxNode;
        }
    }

    static void rotate(Node x) {
        Node p = x.p, g = p.p;
        push(p);
        push(x);

        if (p.l == x) {
            p.l = x.r;
            if (x.r != null) x.r.p = p;
            x.r = p;
        } else {
            p.r = x.l;
            if (x.l != null) x.l.p = p;
            x.l = p;
        }

        p.p = x;
        x.p = g;

        if (g != null) {
            if (g.l == p) g.l = x;
            else if (g.r == p) g.r = x;
        }
        pull(p);
        pull(x);
    }

    static void splay(Node x) {
        push(x);
        while (!isRoot(x)) {
            Node p = x.p, g = p.p;
            if (!isRoot(p)) {
                if ((p.l == x) == (g.l == p)) rotate(p);
                else rotate(x);
            }

            rotate(x);
        }

        pull(x);
    }

    static void access(Node x) {
        Node last = null;
        for (Node y = x; y != null; y = y.p) {
            splay(y);
            y.r = last;
            pull(y);
            last = y;
        }

        splay(x);
    }

    static void makeRoot(Node x) {
        access(x);
        x.rev ^= true;
    }

    static Node findRoot(Node x) {
        access(x);
        while (true) {
            push(x);
            if (x.l == null) break;
            x = x.l;
        }

        splay(x);
        return x;
    }

    static void link(Node u, Node v) {
        makeRoot(u);
        u.p = v;
    }

    static void cut(Node u, Node v) {
        makeRoot(u);
        access(v);
        if (v.l == u) {
            v.l.p = null;
            v.l = null;
            pull(v);
        }
    }

    static Node queryMax(Node u, Node v) {
        makeRoot(u);
        access(v);
        return v.maxNode;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);

        String[] nm = br.readLine().split(" ");
        int n = Integer.parseInt(nm[0]);
        int m = Integer.parseInt(nm[1]);

        Node[] nodes = new Node[n + m + 5];

        for (int i = 1; i <= n + m; i++) {
            nodes[i] = new Node();
            nodes[i].val = -1;
            nodes[i].max = -1;
            nodes[i].maxNode = nodes[i];
        }

        int edgeIdx = n + 1;
        long mst = 0;

        for (int i = 0; i < m; i++) {
            String[] s = br.readLine().split(" ");
            int u = Integer.parseInt(s[0]);
            int v = Integer.parseInt(s[1]);
            int w = Integer.parseInt(s[2]);

            Node nu = nodes[u];
            Node nv = nodes[v];

            if (findRoot(nu) != findRoot(nv)) {
                Node e = nodes[edgeIdx++];
                e.val = w;
                e.u = nu;
                e.v = nv;
                pull(e);

                link(nu, e);
                link(e, nv);

                mst += w;
            } else {
                Node maxEdge = queryMax(nu, nv);

                if (maxEdge.val > w) {
                    cut(maxEdge, maxEdge.u);
                    cut(maxEdge, maxEdge.v);

                    Node e = nodes[edgeIdx++];
                    e.val = w;
                    e.u = nu;
                    e.v = nv;
                    pull(e);

                    link(nu, e);
                    link(e, nv);

                    mst += w - maxEdge.val;
                }
            }
        }

        out.println(mst);
        out.flush();
    }
}
