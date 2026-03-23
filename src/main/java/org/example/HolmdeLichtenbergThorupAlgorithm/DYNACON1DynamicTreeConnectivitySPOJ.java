package org.example.HolmdeLichtenbergThorupAlgorithm;

import java.io.*;
import java.util.*;

public class DYNACON1DynamicTreeConnectivitySPOJ {
    static final int MAXN = 100_005;
    static final int MAXLOG = 17;

    static int[] tL, tR, tPar;
    static int[] tSz, tPri;
    static boolean[] tNTE, tOwn, tVtx;

    static int nc = 0;

    static final Random RNG = new Random(0xdeadbeef);

    static int newNode(boolean isVertex) {
        int x = ++nc;
        tL[x] = tR[x] = tPar[x] = 0;
        tSz[x] = 1;
        tPri[x] = RNG.nextInt();
        tNTE[x] = tOwn[x] = false;
        tVtx[x] = isVertex;
        return x;
    }

    static void pull(int x) {
        if (x == 0) return;
        tSz[x] = tSz[tL[x]] + 1 + tSz[tR[x]];
        tNTE[x] = tOwn[x] | tNTE[tL[x]] | tNTE[tR[x]];
    }

    static int splitL, splitR;

    static void splitK(int x, int k) {
        if (x == 0) {
            splitL = splitR = 0;
            return;
        }

        int ls = tSz[tL[x]];

        if (ls >= k) {
            splitK(tL[x], k);
            tL[x] = splitR;
            if (splitR != 0) tPar[splitR] = x;
            tPar[x] = 0;
            pull(x);
            splitR = x;
        } else {
            splitK(tR[x], k - ls - 1);
            tR[x] = splitL;
            if (splitL != 0) tPar[splitL] = x;
            tPar[x] = 0;
            pull(x);
            splitL = x;
        }
    }

    static void splitBefore(int v) {
        int pos = tSz[tL[v]];
        int cur = v;
        while (tPar[cur] != 0) {
            int p = tPar[cur];
            if (tR[p] == cur) pos += tSz[tL[p]] + 1;
            cur = p;
        }

        splitK(cur, pos);
    }

    static int merge(int a, int b) {
        if (a == 0) {
            if (b != 0) tPar[b] = 0;
            return b;
        }

        if (b == 0) {
            tPar[a] = 0;
            return a;
        }

        int r;

        if (tPri[a] > tPri[b]) {
            tR[a] = merge(tR[a], b);
            if (tR[a] != 0) tPar[tR[a]] = a;
            tPar[a] = 0;
            pull(a);
            r = a;
        } else {
            tL[b] = merge(a, tL[b]);
            if (tL[b] != 0) tPar[tL[b]] = b;
            tPar[b] = 0;
            pull(b);
            r = b;
        }

        return r;
    }

    static int getRoot(int x) {
        while (tPar[x] != 0) x = tPar[x];
        return x;
    }

    static void makeFirst(int vnode) {
        splitBefore(vnode);
        merge(splitR, splitL);
    }

    static int[][] vn;
    static int[] nodeToVtx;

    static HashMap<Long, int[]> eNodes = new HashMap<>();
    static HashMap<Long, Integer> eLevel = new HashMap<>();

    static ArrayList<Long>[][] nteList;
    static HashMap<Long, Integer> nteLevel = new HashMap<>();

    static ArrayList<Long>[][] treeList;

    static long ek(int u, int v) {
        if (u > v) {
            int t = u;
            u = v;
            v = t;
        }

        return (long) u * 100001 + v;
    }

    static int eu(long k) {
        return (int) (k / 100001);
    }

    static int ev(long k) {
        return (int) (k % 100001);
    }

    static void ettLink(int u, int v, int l) {
        makeFirst(vn[u][l]);
        makeFirst(vn[v][l]);
        long k = ek(u, v);
        int[] oc = eNodes.computeIfAbsent(k, x -> new int[MAXLOG * 2]);
        int fwd = newNode(false), bck = newNode(false);
        oc[l * 2] = fwd;
        oc[l * 2 + 1] = bck;

        int ru = getRoot(vn[u][l]);
        int rv = getRoot(vn[v][l]);
        merge(merge(merge(ru, fwd), rv), bck);
    }

    static void ettCut(int u, int v, int l) {
        long k = ek(u, v);
        int[] oc = eNodes.get(k);
        int fwd = oc[l * 2], bck = oc[l * 2 + 1];

        int posFwd = posOf(fwd);
        int posBck = posOf(bck);
        if (posFwd > posBck) {
            int t = fwd;
            fwd = bck;
            bck = t;
        }

        int root = getRoot(fwd);
        int pbck = posOf(bck);

        splitK(root, pbck);
        int left = splitL, rightPart = splitR;

        splitK(rightPart, 1);
        int bckNode = splitL, C = splitR;

        int pfwd = posOf(fwd);
        splitK(left, pfwd);
        int A = splitL, fwdAndB = splitR;

        splitK(fwdAndB, 1);
        int fwdNode = splitL, B = splitR;

        merge(A, C);
        oc[l * 2] = oc[l * 2 + 1] = -1;
    }

    static int posOf(int x) {
        int pos = tSz[tL[x]];
        while (tPar[x] != 0) {
            int p = tPar[x];
            if (tR[p] == x) pos += tSz[tL[p]] + 1;
            x = p;
        }

        return pos;
    }

    static boolean connL(int u, int v, int l) {
        if (u == v) return true;
        return getRoot(vn[u][l]) == getRoot(vn[v][l]);
    }

    static boolean connected(int u, int v) {
        return connL(u, v, 0);
    }

    static int compSz(int v, int l) {
        return tSz[getRoot(vn[v][l])];
    }

    static void setOwn(int v, int l, boolean f) {
        int nd = vn[v][l];
        if (tOwn[nd] == f) return;
        tOwn[nd] = f;
        int x = nd;
        pull(x);
        while (tPar[x] != 0) {
            x = tPar[x];
            pull(x);
        }
    }


    static void link(int u, int v) {
        long k = ek(u, v);
        if (!connected(u, v)) {
            eLevel.put(k, 0);
            treeList[u][0].add(k);
            treeList[v][0].add(k);
            ettLink(u, v, 0);
        } else {
            nteLevel.put(k, 0);
            nteList[u][0].add(k);
            nteList[v][0].add(k);
            setOwn(u, 0, true);
            setOwn(v, 0, true);
        }
    }

    static void cut(int u, int v) {
        long k = ek(u, v);
        if (!eLevel.containsKey(k)) {
            int l = nteLevel.remove(k);
            nteList[u][l].remove(k);
            nteList[v][l].remove(k);
            if (nteList[u][l].isEmpty()) setOwn(u, l, false);
            if (nteList[v][l].isEmpty()) setOwn(v, l, false);
            return;
        }

        int lvl = eLevel.remove(k);
        treeList[u][lvl].remove(k);
        treeList[v][lvl].remove(k);
        for (int l = 0; l <= lvl; l++) ettCut(u, v, l);

        for (int l = lvl; l >= 0; l--) {
            int su = compSz(u, l), sv = compSz(v, l);
            int small = su <= sv ? u : v;
            int large = small == u ? v : u;

            if (l + 1 < MAXLOG) promoteTree(small, l);
            if (findReplacement(small, large, l)) return;
        }
    }

    static void promoteTree(int v, int l) {
        int root = getRoot(vn[v][l]);
        ArrayList<Integer> verts = new ArrayList<>();
        collectVerts(root, verts);

        for (int sv : verts) {
            for (long k : new ArrayList<>(treeList[sv][l])) {
                if (!eLevel.containsKey(k) || eLevel.get(k) != l) continue;
                int eu = eu(k), ev = ev(k);
                if (!connL(eu, v, l)) continue;

                eLevel.put(k, l + 1);
                treeList[eu][l].remove(k);
                treeList[ev][l].remove(k);
                treeList[eu][l + 1].add(k);
                treeList[ev][l + 1].add(k);
                ettLink(eu, ev, l + 1);
            }
        }
    }

    static boolean findReplacement(int small, int large, int l) {
        int root = getRoot(vn[small][l]);
        ArrayList<Integer> verts = new ArrayList<>();
        collectVerts(root, verts);

        for (int sv : verts) {
            for (long k : new ArrayList<>(nteList[sv][l])) {
                int eu = eu(k), ev = ev(k);
                int other = eu == sv ? ev : eu;
                if (connL(other, large, l)) {
                    nteLevel.remove(k);
                    nteList[eu][l].remove(k);
                    nteList[ev][l].remove(k);
                    if (nteList[eu][l].isEmpty()) setOwn(eu, l, false);
                    if (nteList[ev][l].isEmpty()) setOwn(ev, l, false);

                    eLevel.put(k, l);
                    treeList[eu][l].add(k);
                    treeList[ev][l].add(k);
                    for (int ll = 0; ll <= l; ll++) ettLink(eu, ev, ll);
                    return true;
                } else {
                    if (l + 1 < MAXLOG) {
                        nteLevel.remove(k);
                        nteList[eu][l].remove(k);
                        nteList[ev][l].remove(k);
                        if (nteList[eu][l].isEmpty()) setOwn(eu, l, false);
                        if (nteList[ev][l].isEmpty()) setOwn(ev, l, false);
                        nteLevel.put(k, l + 1);
                        nteList[eu][l + 1].add(k);
                        nteList[ev][l + 1].add(k);
                        setOwn(eu, l + 1, true);
                        setOwn(ev, l + 1, true);
                    }
                }
            }
        }

        return false;
    }

    static void collectVerts(int x, ArrayList<Integer> out) {
        if (x == 0) return;
        collectVerts(tL[x], out);
        if (tVtx[x]) out.add(nodeToVtx[x]);
        collectVerts(tR[x], out);
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());

        int POOL = (N + M * 2) * MAXLOG + 16;
        tL = new int[POOL];
        tR = new int[POOL];
        tPar = new int[POOL];
        tSz = new int[POOL];
        tPri = new int[POOL];
        tNTE = new boolean[POOL];
        tOwn = new boolean[POOL];
        tVtx = new boolean[POOL];
        vn = new int[N + 1][MAXLOG];
        nodeToVtx = new int[POOL];
        nteList = new ArrayList[N + 1][MAXLOG];
        treeList = new ArrayList[N + 1][MAXLOG];

        for (int i = 1; i <= N; i++)
            for (int l = 0; l < MAXLOG; l++) {
                nteList[i][l] = new ArrayList<>();
                treeList[i][l] = new ArrayList<>();
                vn[i][l] = newNode(true);
                nodeToVtx[vn[i][l]] = i;
            }

        StringBuilder sb = new StringBuilder();
        for (int q = 0; q < M; q++) {
            st = new StringTokenizer(br.readLine());
            String op = st.nextToken();
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            if (op.equals("add")) link(a, b);
            else if (op.equals("rem")) cut(a, b);
            else sb.append(connected(a, b) ? "YES" : "NO").append('\n');
        }
        System.out.print(sb);
    }
}
