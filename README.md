<div align="center">
  <img src="https://avatars.githubusercontent.com/u/102406248?v=4" width="120" height="120" style="border-radius: 50%;" alt="code-with-Bharadwaj"/>
  
  <h1>⚡ Dynamic Graph Systems — Java</h1>

  <p><strong>Algorithms that adapt, evolve, and respond — Graph structures that never stop changing</strong><br/>
  As explored on <a href="https://www.youtube.com/@code-with-Bharadwaj">@code-with-Bharadwaj</a></p>

  <p>
    <img src="https://img.shields.io/badge/Language-Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
    <img src="https://img.shields.io/badge/Topic-Dynamic%20Graph%20Systems-0077B6?style=for-the-badge"/>
    <img src="https://img.shields.io/badge/Level-Advanced-FF4C4C?style=for-the-badge"/>
    <img src="https://img.shields.io/badge/YouTube-@code--with--Bharadwaj-FF0000?style=for-the-badge&logo=youtube&logoColor=white"/>
  </p>
</div>

---

## 🎯 What Is This Repository?

This repository is the **official code companion** to the YouTube series by [@code-with-Bharadwaj](https://www.youtube.com/@code-with-Bharadwaj), covering **Dynamic Graph Systems** — a class of algorithms that handle graphs **as they change over time**.

Static graphs are the easy part. The real world demands something harder: graphs where edges are **inserted, deleted, or modified** at any moment. These algorithms answer connectivity, reachability, and structure queries **efficiently on graphs that are never at rest.** This is where data structures meet graph theory at the highest level.

---

## 📁 Repository Structure

```
📦 Dynamic-Graph-Systems-Algorithms-Java
 ┗ 📂 src
    ┗ 📂 main
       ┗ 📂 java
          ┗ 📂 org
             ┗ 📂 example
                ┣ 📂 DSUwithRollbackAlgorithm
                ┣ 📂 DynamicTransitiveClosure
                ┣ 📂 HolmdeLichtenbergThorupAlgorithm
                ┣ 📂 OfflineDynamicConnectivity
                ┣ 📂 OnlineDynamicMSTAlgo
                ┗ 📄 Main.java
```

---

## 🧠 Algorithms Covered

### 1. ↩️ DSU with Rollback (Union-Find with Undo)
A **Disjoint Set Union (DSU)** structure augmented with a **rollback stack** — enabling the undo of union operations. Essential when you need to process queries in a divide-and-conquer or offline fashion where edges are temporarily added and then removed.

- **Use case:** Offline dynamic connectivity, time-travel queries, segment tree on time
- **Complexity:** `O(log n)` per union/rollback (no path compression — uses union by rank only)
- **Key insight:** Path compression breaks rollback; union by rank alone gives `O(log n)` while still being fully reversible
- **Why it matters:** The backbone of virtually every offline dynamic graph algorithm

---

### 2. 🔄 Dynamic Transitive Closure
Maintains **reachability information** in a directed graph as edges are inserted or deleted, answering queries of the form *"Can node u reach node v?"* without recomputing from scratch.

- **Use case:** Incremental compiler dependency resolution, dynamic reachability in DAGs, live program analysis
- **Complexity:** `O(n²)` per update (dense), `O(n)` per query — or better with sparse variants
- **Key insight:** Maintains a boolean reachability matrix; edge insertions/deletions trigger propagated updates through predecessor/successor sets
- **Variants:** Incremental-only (easier) vs. fully dynamic (insertions + deletions)

---

### 3. 🏗️ Holm-de Lichtenberg-Thorup Algorithm
The **landmark algorithm** for fully dynamic graph connectivity. Supports both edge insertions and deletions while answering connectivity queries — all in **polylogarithmic amortized time**.

- **Use case:** Network monitoring, dynamic road/rail connectivity, fault-tolerant systems
- **Complexity:** `O(log² n)` amortized per update, `O(1)` per query
- **Key insight:** Maintains a spanning forest using a clever **multi-level edge labeling** scheme (levels 0 to log n); when a tree edge is deleted, non-tree edges at progressively lower levels are searched as replacements
- **Historical significance:** Remained the best deterministic result for over two decades after its 2001 publication

---

### 4. 📴 Offline Dynamic Connectivity
Processes a **sequence of edge insertions and deletions** with connectivity queries offline — i.e., all operations are known in advance. Uses a **segment tree over time** + DSU with rollback for a clean and optimal solution.

- **Use case:** Competitive programming, batch network change analysis, historical graph queries
- **Complexity:** `O(m log m · α(n))` or `O(m log² n)` depending on implementation
- **Key insight:** Each edge has a "lifetime" interval `[insert_time, delete_time]`. These intervals are mapped onto a segment tree; DFS over the tree applies/rolls back DSU unions — each query is answered at the leaves
- **Elegant property:** Converts a hard online problem into a clean divide-and-conquer structure

---

### 5. 🌿 Online Dynamic MST Algorithm
Maintains a **Minimum Spanning Tree** under live edge insertions and deletions, answering "what is the current MST weight?" or "is this edge in the MST?" after every update.

- **Use case:** Live network cost optimization, dynamic infrastructure planning, online Kruskal scenarios
- **Complexity:** `O(log² n)` to `O(log³ n)` per update depending on the approach
- **Key insight:** On edge insertion — check if the new edge creates a cycle in the MST and replace the max-weight edge in that cycle if beneficial. On deletion — find a replacement edge from non-tree edges efficiently using **top trees** or **ET trees**
- **Data structures involved:** Euler Tour Trees, Link-Cut Trees, or HDT-style level structures

---

## 🚀 Getting Started

### Prerequisites
- Java 11 or higher
- Maven (optional, for build management)
- IntelliJ IDEA (recommended) or any Java IDE

### Clone & Run

```bash
# Clone the repository
git clone https://github.com/Manu577228/Dynamic-Graph-Systems-Algorithms-Java.git

# Navigate to the project
cd Dynamic-Graph-Systems-Algorithms-Java

# Compile from root
javac src/main/java/org/example/Main.java

# Run
java -cp src/main/java org.example.Main
```

---

## 📊 Algorithm Quick Reference

| Algorithm | Query Type | Supports Deletions? | Offline / Online | Complexity (Update) |
|---|---|---|---|---|
| DSU with Rollback | Connectivity | Via rollback only | Offline | O(log n) |
| Dynamic Transitive Closure | Reachability | ✅ Yes | Online | O(n²) update |
| Holm-de Lichtenberg-Thorup | Connectivity | ✅ Yes | Online | O(log² n) amortized |
| Offline Dynamic Connectivity | Connectivity | ✅ Yes | Offline | O(m log² n) total |
| Online Dynamic MST | MST weight/structure | ✅ Yes | Online | O(log² n – log³ n) |

---

## 🧩 How These Algorithms Connect

```
Dynamic Graph Problems
        │
        ├──► Offline? (all queries known in advance)
        │         └──► Offline Dynamic Connectivity  ← uses DSU with Rollback
        │
        └──► Online? (queries arrive in real time)
                  ├──► Connectivity queries
                  │         └──► Holm-de Lichtenberg-Thorup (HDT)
                  ├──► Reachability in directed graphs
                  │         └──► Dynamic Transitive Closure
                  └──► Minimum Spanning Tree
                            └──► Online Dynamic MST (ET-Trees / HDT)
```

---

## 🎓 Who Is This For?

- 🏆 **Competitive programmers** pushing toward ICPC / Codeforces top-tier divisions
- 🔬 **Researchers** working in dynamic data structures and graph theory
- 💼 **System engineers** building fault-tolerant, real-time network monitoring tools
- 📺 **Viewers** of [@code-with-Bharadwaj](https://www.youtube.com/@code-with-Bharadwaj) following along with the series

---

## 🔗 Related Repositories

Also check out the companion repos in this series:

[![Advanced Graph Algorithms](https://img.shields.io/badge/Repo-Advanced%20Graph%20Algorithms-00B4D8?style=for-the-badge&logo=github&logoColor=white)](https://github.com/Manu577228/Advanced-Graph-Algorithms-Java)

[![DP Optimization Algorithms](https://img.shields.io/badge/Repo-DP%20Optimization%20Algorithms-6C63FF?style=for-the-badge&logo=github&logoColor=white)](https://github.com/Manu577228)

---

## 📺 YouTube Channel

All these algorithms are explained visually with step-by-step walkthroughs on the channel:

<div align="center">

[![YouTube](https://img.shields.io/badge/Watch%20on-YouTube-FF0000?style=for-the-badge&logo=youtube&logoColor=white)](https://www.youtube.com/@code-with-Bharadwaj)

**@code-with-Bharadwaj**  
*Making hard algorithms easy to understand* 🔥

</div>

---

## 🤝 Contributing

Contributions are welcome! If you have a better implementation, edge case tests, or visualizations:

1. Fork this repository
2. Create your feature branch: `git checkout -b feature/improve-hdt`
3. Commit your changes: `git commit -m 'Improve HDT with level compression'`
4. Push to the branch: `git push origin feature/improve-hdt`
5. Open a Pull Request

---

## ⭐ Support

If this repo helped you understand dynamic graphs or solve a problem you thought was impossible — **star it** and **share it!**

And subscribe to [@code-with-Bharadwaj](https://www.youtube.com/@code-with-Bharadwaj) for more content on algorithms that actually matter. 🚀

---

<div align="center">
  <sub>Built with ❤️ by <a href="https://www.youtube.com/@code-with-Bharadwaj">@code-with-Bharadwaj</a> · Happy Coding! 💻</sub>
</div>
