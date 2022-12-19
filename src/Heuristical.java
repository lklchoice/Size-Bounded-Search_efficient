import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Heuristical {
    VertexSet H;

    public Heuristical() {
        VertexSet H = new VertexSet();
    }

    public void GenerateInitAns(Graph G, Vertex q, int h, int l) {
        //生成初始解H
        int k = 0;
        H = null;
        if (G.relation.get(q.id).Hset.size() >= h - 1) {
            //构建ego-network
            Graph S = new Graph();
            S.vertices.Add2All(q);
            S.relation.put(q.id, G.relation.get(q));
            for (Vertex vertex : S.relation.get(q.id).Hset) {
                S.vertices.Add2All(vertex);
            }
            Graph copy = G;
            for (Vertex vertex : S.vertices.Hset) {
                if (vertex != q) {
                    copy.relation.get(vertex.id).Hset.retainAll(S.vertices.Hset);
                    S.relation.put(vertex.id, new VertexSet(copy.relation.get(vertex.id)));
                }
            }
            while (S.vertices.Size() >= l) {
                S.CalDegree();
                if (S.vertices.Size() <= h && S.MinDegree > k) {
                    k = S.MinDegree;
                    H = new VertexSet(S.vertices);
                }
                //删除最小度数的点
                S.DelFromG(S.vertices.Tset.first());
            }
        } else {
            Graph S = new Graph();
            S.vertices.Add2All(q);
            S.relation.put(q.id, null);
            q.degree = 1;//初始化设置q的degree为1
            while (S.vertices.Size() < h) {
                double max = -1;
                Vertex maxVertex = null;
                VertexSet neighbors = new VertexSet();
                for (Vertex vertex : S.vertices.Hset) {
                    for (Vertex ne : G.relation.get(vertex.id).Hset) {
                        if (!S.vertices.Hset.contains(ne)) {
                            //对于外面有连边的点，计算score
                            double score = 0;
                            if (S.vertices.Size() != 1) {
                                S.CalDegree();//这里可能会出问题
                            }//对于单个点来说，没有办法，只能度数为1
                            for (Vertex v : S.relation.get(ne.id).Hset) {
                                if (S.vertices.Hset.contains(v)) {
                                    //这些都是在S里面的邻居，需要存起来
                                    neighbors.Add2All(v);
                                    score += 1.0 / v.degree;
                                }
                            }
                            max = Math.max(max, score);
                            maxVertex = ne;
                        }
                    }
                    S.vertices.Add2All(maxVertex);
                    S.relation.put(maxVertex.id, neighbors);
                    S.CalDegree();
                    if (S.vertices.Size() >= l && S.MinDegree > k) {
                        k = S.MinDegree;
                        H = new VertexSet(S.vertices);
                    }
                }
            }
        }
    }
}