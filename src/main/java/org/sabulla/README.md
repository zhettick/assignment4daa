# 🧠 DAA Assignment 4 — Smart City / Smart Campus Scheduling

## **1. Objective**

This project combines three key algorithms from graph theory:

- **Strongly Connected Components (SCC)** — Tarjan’s algorithm
- **Topological sorting** — Kahn’s algorithm
- **Shortest & longest paths in DAGs**

The scenario models city or campus task scheduling, where dependencies between tasks (e.g., repairs, maintenance,
cleaning) may contain cycles or acyclic hierarchies.  
The goal is to:

1. Detect and compress strongly connected components.
2. Build a condensation DAG.
3. Compute both topological order and shortest/longest paths.

---

## **2. Input Data Summary**

| Graph | Vertices (n) | Edges (m) | Directed | Source | Weight Model |
|:------|:-------------|:----------|:---------|:-------|:-------------|
| 1     | 6            | 5         | true     | 0      | edge         |
| 2     | 9            | 9         | true     | 1      | edge         |
| 3     | 9            | 8         | true     | 3      | edge         |
| 4     | 17           | 20        | true     | 0      | edge         |
| 5     | 19           | 52        | true     | 9      | edge         |
| 6     | 10           | 10        | true     | 0      | edge         |
| 7     | 43           | 113       | true     | 15     | edge         |
| 8     | 37           | 102       | true     | 15     | edge         |
| 9     | 43           | 45        | true     | 36     | edge         |

**Weight model:** All datasets use edge-based weights, where each edge weight `w` represents task duration, cost, or
dependency priority.

---

## **3. Algorithms Implemented**

| Algorithm                 | Purpose                                 | Complexity | Metrics Recorded         |
|---------------------------|-----------------------------------------|------------|--------------------------|
| **Tarjan SCC**            | Detect cycles, group vertices into SCCs | O(V + E)   | DFS calls, Edges checked |
| **Kahn Topological Sort** | Compute topological order in DAG        | O(V + E)   | Queue pushes, pops       |
| **DAG Shortest Path**     | Compute minimum total task time         | O(V + E)   | Relaxations, path length |
| **DAG Longest Path**      | Identify the critical path              | O(V + E)   | Relaxations, path length |

---

## **4. Results Summary**

| Graph | #SCCs | Largest SCC | Topo Len | Shortest Path | Longest Path                  | DFS Calls | Pushes / Pops | Relax (S/L) | Tarjan Time (ms) | Kahn Time (ms) | DAG Time (S/L, ms) |
|:------|:------|:------------|:---------|:--------------|:------------------------------|:----------|:--------------|:------------|:-----------------|:---------------|:-------------------|
| 1     | 6     | 1           | 6        | [5→4] (len=5) | [5,4,3,2,1,0] (len=5)         | 6         | 6 / 6         | 5 / 10      | 0.012            | 0.072          | 0.075/0.045        |
| 2     | 6     | 4           | 6        | [4→3] (len=4) | [4,3,2,1,0] (len=4)           | 9         | 6 / 6         | 4 / 8       | 0.034            | 0.026          | 0.026/0.029        |
| 3     | 9     | 1           | 9        | [5→4] (len=5) | [5,4,3,2,1,0] (len=5)         | 9         | 9 / 9         | 5 / 10      | 0.032            | 0.032          | 0.028/0.026        |
| 4     | 4     | 14          | 4        | [3→2] (len=3) | [3,2,1,0] (len=3)             | 17        | 4 / 4         | 3 / 6       | 0.058            | 0.026          | 0.031/0.02         |
| 5     | 2     | 18          | 2        | [1→0] (len=1) | [1,0] (len=1)                 | 19        | 2 / 2         | 2 / 4       | 0.075            | 0.011          | 0.015/0.015        |
| 6     | 10    | 1           | 10       | [9→8] (len=9) | [9,8,7,6,5,4,3,2,1,0] (len=9) | 10        | 10 / 10       | 10 / 20     | 0.04             | 0.029          | 0.022/0.02         |
| 7     | 1     | 43          | 1        | [0] (len=0)   | [0] (len=0)                   | 43        | 1 / 1         | 0           | 0.087            | 0.007          | 0.011/0.01         |
| 8     | 1     | 37          | 1        | [0] (len=0)   | [0] (len=0)                   | 37        | 1 / 1         | 0           | 0.069            | 0.005          | 0.011/0.01         |
| 9     | 33    | 11          | 33       | [6→5] (len=6) | [6,5,4,3,2,1,0] (len=6)       | 43        | 33 / 33       | 6 / 12      | 0.074            | 0.055          | 0.018/0.019        |

- Execution times are in the millisecond range, confirming all algorithms operate in linear O(V + E) time.

---

## **5. Analysis**

### **1. Tarjan SCC**

- Efficiently isolated cycles in < 0.1 ms for all graphs.
- Graphs 1–3 had mostly single-vertex SCCs → almost acyclic.
- Graphs 4–5 contained large SCCs (sizes 14–18) indicating deep dependency chains.
- Graphs 7–8 collapsed into one large SCC → no acyclic structure after compression.

**Meaning:**  
SCC compression removes redundant cyclic dependencies before planning, producing a smaller condensation DAG.

### **2. Kahn Topological Sort**

- Average time ≈ 0.03 ms, even for 43-vertex graphs.
- Queue operations (Push = Pop = V) confirm O(V + E) complexity.
- In single-SCC graphs, only one node remains → constant time.

**Observation:**  
Topo length equals SCC count, not original vertex count. After condensation, even dense graphs become simple linear
orders.

### **3. DAG Shortest & Longest Paths**

- Executed after topological order using dynamic programming.
- Average ≈ 0.02 – 0.03 ms per phase.
- **Shortest path** → minimum total duration.
- **Longest path** → critical path (bottleneck tasks).
- Longest length = 9 (Graph 6), showing deepest dependency chain.
- Graphs 7–8 → 0-length paths after SCC collapse.

**Performance:**  
Stable execution, constant time even for large DAGs.  
Relaxation count shows expected 1:2 ratio (shortest vs longest).

### **4. Structural Impact and Bottlenecks**

| Aspect                | Observation                                                      | Explanation                                        |
|:----------------------|:-----------------------------------------------------------------|:---------------------------------------------------|
| **Graph Density**     | Denser graphs → larger SCCs                                      | More edges cause cyclic clusters, reducing freedom |
| **SCC Compression**   | Reduces node count for Topo & DAG steps                          | Improves speed & clarity of dependency flow        |
| **Time Distribution** | Tarjan ≈ 0.04–0.09 ms · Kahn ≈ 0.01–0.07 ms · DAG ≈ 0.02–0.07 ms | All algorithms operate within linear time          |
| **Main Bottleneck**   | Tarjan slightly heavier in dense graphs                          | Recursive stack operations and edge iterations     |

---

## **6. Conclusions**

| Algorithm             | Purpose                    | When to Use                     | Output                        |
|:----------------------|:---------------------------|:--------------------------------|:------------------------------|
| **Tarjan SCC**        | Detect and collapse cycles | Always first on directed graphs | List of SCCs + condensed DAG  |
| **Kahn Topo Sort**    | Order acyclic components   | After SCC compression           | Valid task execution sequence |
| **DAG Shortest Path** | Minimum total duration     | For optimal efficiency          | Distance map & optimal route  |
| **DAG Longest Path**  | Identify critical path     | For project bottleneck analysis | Longest chain of tasks        |

---

### **Practical Findings**

- For small graphs (≤ 10 vertices) → total time < 1 ms → ideal for real-time systems.
- For dense graphs (> 30 vertices) → Tarjan dominates runtime but remains < 0.1 ms.
- For fully cyclic graphs → only SCC output is meaningful; Topo and DAG reduce to trivial cases.
- **Balanced graphs** (4–6) represent realistic task dependencies and produce the most insightful critical paths.

---

## **Final Conclusion**

The combined pipeline

> **Tarjan SCC → Kahn topological sort → DAG shortest / longest paths**

demonstrates a **robust, scalable, and interpretable approach** for analyzing complex task dependencies in Smart City
and Smart Campus contexts.

- All methods showed linear scalability and microsecond-level performance.
- The pipeline successfully separates cyclic from acyclic dependencies for optimal planning.
- The results highlight clear critical paths and minimal bottlenecks in task networks.