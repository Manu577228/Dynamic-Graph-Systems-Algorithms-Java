package org.example.DynamicTransitiveClosure;

//Dynamic Transitive Closure maintains reachability
//information in a graph while edges are added or removed dynamically.
//It allows us to quickly answer: “Is there a path from node u to v?” after updates.
//Commonly implemented using Floyd–Warshall updates or matrix propagation.

import java.io.*;
import java.util.*;

public class DynamicTransitiveClosure {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);

        int n = 4;

        boolean[][] reach = new boolean[n][n];

        reach[0][1] = true;
        reach[1][2] = true;

        for (int i = 0; i < n; i++) reach[i][i] = true;

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                if (reach[i][k]) {
                    for (int j = 0; j < n; j++) {
                        if (reach[k][j]) {
                            reach[i][j] = true;
                        }
                    }
                }
            }
        }

        reach[2][3] = true;

        for (int i = 0; i < n; i++) {
            if (reach[i][2]) {
                for (int j = 0; j < n; j++) {
                    if (reach[3][j]) {
                        reach[i][j] = true;
                    }
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                out.print((reach[i][j] ? 1 : 0) + " ");
            }

            out.println();
        }

        out.flush();
    }
}