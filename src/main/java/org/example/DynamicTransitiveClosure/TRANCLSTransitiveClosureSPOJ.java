package org.example.DynamicTransitiveClosure;

import java.io.*;
import java.util.*;

public class TRANCLSTransitiveClosureSPOJ {
    static boolean[][] reach;
    static int N;

    static void addEdge(int u, int v) {
        if (reach[u][v]) return;

        List<Integer> sources = new ArrayList<>();
        List<Integer> targets = new ArrayList<>();

        for (int i = 0; i < N; i++) if (reach[i][u]) sources.add(i);
        for (int k = 0; k < N; k++) if (reach[v][k]) targets.add(k);

        sources.add(u);
        targets.add(v);

        for (int i : sources)
            for (int k : targets)
                reach[i][k] = true;
    }

    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);

        int T = Integer.parseInt(br.readLine());

        for (int t = 1; t <= T; t++) {

            N = Integer.parseInt(br.readLine());

            reach = new boolean[N][N];

            boolean[][] original = new boolean[N][N];

            int[][] edges = new int[N][2];

            for (int i = 0; i < N; i++) {
                String[] s = br.readLine().split(" ");
                int u = Integer.parseInt(s[0]);
                int v = Integer.parseInt(s[1]);

                edges[i][0] = u;
                edges[i][1] = v;

                original[u][v] = true;
            }

            for (int i = 0; i < N; i++)
                addEdge(edges[i][0], edges[i][1]);

            int added = 0;

            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)

                    if (reach[i][j] && !original[i][j])
                        added++;

            out.println("Case #" + t + ": " + added);
        }

        out.flush();
    }
}