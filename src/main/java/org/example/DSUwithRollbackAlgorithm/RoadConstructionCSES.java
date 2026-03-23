package org.example.DSUwithRollbackAlgorithm;

import java.io.*;

public class RoadConstructionCSES {
    static int[] par, rnk, sz;
    static int[] histU, histV;
    static int[] histRnkU, histSzU, histSzV;
    static int histTop;
    static int components, maxSize;

    static int find(int x) {
        while (par[x] != x) x = par[x];
        return x;
    }

    static void unite(int a, int b) {
        a = find(a);
        b = find(b);

        if (a == b) {
            histU[histTop++] = -1;
            return;
        }

        if (rnk[a] < rnk[b]) {
            int t = a;
            a = b;
            b = t;
        }

        histU[histTop] = a;
        histV[histTop] = b;
        histRnkU[histTop] = rnk[a];
        histSzU[histTop] = sz[a];
        histSzV[histTop] = sz[b];
        histTop++;

        par[b] = a;
        sz[a] += sz[b];
        if (rnk[a] == rnk[b]) rnk[a]++;
        components--;
        if (sz[a] > maxSize) maxSize = sz[a];
    }

    static void rollback(int target) {
        while (histTop > target) {
            histTop--;
            int a = histU[histTop];
            if (a == -1) continue;
            int b = histV[histTop];
            par[b] = b;
            rnk[a] = histRnkU[histTop];
            sz[a] = histSzU[histTop];
            sz[b] = histSzV[histTop];
            components++;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);

        in.nextToken();
        int n = (int) in.nval;
        in.nextToken();
        int m = (int) in.nval;

        par = new int[n + 1];
        rnk = new int[n + 1];
        sz = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            par[i] = i;
            sz[i] = 1;
        }

        int histMax = m + 10;
        histU = new int[histMax];
        histV = new int[histMax];
        histRnkU = new int[histMax];
        histSzU = new int[histMax];
        histSzV = new int[histMax];
        histTop = 0;

        components = n;
        maxSize = 1;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m; i++) {
            in.nextToken();
            int a = (int) in.nval;
            in.nextToken();
            int b = (int) in.nval;
            unite(a, b);
            sb.append(components).append(' ').append(maxSize).append('\n');
        }
        System.out.print(sb);
    }
}