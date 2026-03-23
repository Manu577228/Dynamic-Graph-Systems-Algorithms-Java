package org.example.OfflineDynamicConnectivity;

import java.io.*;
import java.util.*;

public class DynamicGraphVertexAddComponentSumCodeforces891C {
    static class Edge {
        int u, v;

        Edge(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }

    static class Add {
        int node;
        long delta;

        Add(int node, long delta) {
            this.node = node;
            this.delta = delta;
        }
    }

    static class Record {
        int type;

        int childRoot, parentRoot;
        int prevSize;
        long prevSum;

        Record(int type, int childRoot, int parentRoot, int prevSize, long prevSum) {
            this.type = type;
            this.childRoot = childRoot;
            this.parentRoot = parentRoot;
            this.prevSize = prevSize;
            this.prevSum = prevSum;
        }
    }

    static int N, Q;

    static int[] type;
    static int[] A, B;
    static long[] X;

    static ArrayList<Edge>[] segEdges;
    static ArrayList<Add>[] segAdds;

    static int[] parent, size;
    static long[] compSum;

    static ArrayList<Record> history = new ArrayList<>();
    static long[] answer;

    static void initDSU() {
        parent = new int[N];
        size = new int[N];
        compSum = new long[N];

        for (int i = 0; i < N; i++) {
            parent[i] = i;
            size[i] = 1;
            compSum[i] = 0;
        }
    }

    static int find(int x) {
        while (parent[x] != x) x = parent[x];
        return x;
    }

    static void union(int u, int v) {
        int ru = find(u);
        int rv = find(v);

        if (ru == rv) return;

        if (size[ru] < size[rv]) {
            int tmp = ru;
            ru = rv;
            rv = tmp;
        }

        history.add(new Record(0, rv, ru, size[ru], compSum[ru]));

        parent[rv] = ru;
        size[ru] += size[rv];
        compSum[ru] += compSum[rv];
    }

    static void addValue(int node, long delta) {
        int root = find(node);

        history.add(new Record(1, root, -1, -1, compSum[root]));

        compSum[root] += delta;
    }

    static void rollback(int checkpoint) {
        while (history.size() > checkpoint) {
            Record r = history.remove(history.size() - 1);

            if (r.type == 0) {
                parent[r.childRoot] = r.childRoot;
                size[r.parentRoot] = r.prevSize;
                compSum[r.parentRoot] = r.prevSum;
            } else {
                compSum[r.childRoot] = r.prevSum;
            }
        }
    }

    static void addEdge(int idx, int l, int r, int ql, int qr, Edge e) {
        if (ql >= r || qr <= l) return;

        if (ql <= l && r <= qr) {
            segEdges[idx].add(e);
            return;
        }

        int mid = (l + r) >> 1;

        addEdge(idx << 1, l, mid, ql, qr, e);
        addEdge(idx << 1 | 1, mid, r, ql, qr, e);
    }

    static void addUpdate(int idx, int l, int r, int ql, int qr, Add a) {
        if (ql >= r || qr <= l) return;

        if (ql <= l && r <= qr) {
            segAdds[idx].add(a);
            return;
        }

        int mid = (l + r) >> 1;

        addUpdate(idx << 1, l, mid, ql, qr, a);
        addUpdate(idx << 1 | 1, mid, r, ql, qr, a);
    }

    static void dfs(int idx, int l, int r) {
        int checkpoint = history.size();

        for (Edge e : segEdges[idx]) {
            union(e.u, e.v);
        }

        for (Add a : segAdds[idx]) {
            addValue(a.node, a.delta);
        }

        if (l + 1 == r) {
            if (type[l] == 3) {
                int root = find(A[l]);
                answer[l] = compSum[root];
            }
        } else {
            int mid = (l + r) >> 1;
            dfs(idx << 1, l, mid);
            dfs(idx << 1 | 1, mid, r);
        }

        rollback(checkpoint);
    }

    public static void main(String[] args) throws Exception {

        FastScanner fs = new FastScanner(System.in);

        N = fs.nextInt();
        Q = fs.nextInt();

        long[] initial = new long[N];
        for (int i = 0; i < N; i++) initial[i] = fs.nextLong();

        type = new int[Q];
        A = new int[Q];
        B = new int[Q];
        X = new long[Q];

        for (int i = 0; i < Q; i++) {
            int t = fs.nextInt();
            type[i] = t;

            if (t == 0 || t == 1) {
                A[i] = fs.nextInt();
                B[i] = fs.nextInt();
            } else if (t == 2) {
                A[i] = fs.nextInt();
                X[i] = fs.nextLong();
            } else {
                A[i] = fs.nextInt();
            }
        }

        segEdges = new ArrayList[4 * Q];
        segAdds = new ArrayList[4 * Q];

        for (int i = 0; i < 4 * Q; i++) {
            segEdges[i] = new ArrayList<>();
            segAdds[i] = new ArrayList<>();
        }

        /* ---------- Handle Edge Lifetimes ---------- */

        HashMap<Long, Integer> active = new HashMap<>();

        for (int t = 0; t < Q; t++) {
            if (type[t] == 0) {
                int u = A[t], v = B[t];
                if (u > v) { int tmp = u; u = v; v = tmp; }

                long key = (((long) u) << 32) | (v & 0xffffffffL);
                active.put(key, t);
            }

            else if (type[t] == 1) {
                int u = A[t], v = B[t];
                if (u > v) { int tmp = u; u = v; v = tmp; }

                long key = (((long) u) << 32) | (v & 0xffffffffL);

                int start = active.remove(key);
                addEdge(1, 0, Q, start, t, new Edge(u, v));
            }
        }

        for (Map.Entry<Long, Integer> e : active.entrySet()) {
            long key = e.getKey();
            int start = e.getValue();

            int u = (int) (key >> 32);
            int v = (int) key;

            addEdge(1, 0, Q, start, Q, new Edge(u, v));
        }

        /* ---------- Handle Add Queries ---------- */

        for (int i = 0; i < N; i++) {
            addUpdate(1, 0, Q, 0, Q, new Add(i, initial[i]));
        }

        for (int t = 0; t < Q; t++) {
            if (type[t] == 2) {
                addUpdate(1, 0, Q, t, Q, new Add(A[t], X[t]));
            }
        }

        /* ---------- Solve ---------- */

        initDSU();

        answer = new long[Q];

        dfs(1, 0, Q);

        /* ---------- Output ---------- */

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < Q; i++) {
            if (type[i] == 3) {
                sb.append(answer[i]).append('\n');
            }
        }

        System.out.print(sb);
    }

    /* -------------------- Fast Input -------------------- */

    static class FastScanner {
        private final InputStream in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;

        FastScanner(InputStream is) { in = is; }

        private int read() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0) return -1;
            }
            return buffer[ptr++];
        }

        long nextLong() throws IOException {
            int c;
            while ((c = read()) <= 32) if (c == -1) return Long.MIN_VALUE;

            int sign = 1;
            if (c == '-') {
                sign = -1;
                c = read();
            }

            long val = 0;
            while (c > 32) {
                val = val * 10 + (c - '0');
                c = read();
            }

            return val * sign;
        }

        int nextInt() throws IOException {
            return (int) nextLong();
        }
    }
}
