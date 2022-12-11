import java.util.*;

public class UpperBoundingTechniques {

    Graph C;
    Graph R;
    Graph Cr;
    Graph Rc;

    public int Ud(int h){
        int min=Integer.MAX_VALUE;
        for(Vertex vertex:C.vertices.Hset){
            int degree_1=C.relation.get(vertex.id).Size()+Cr.relation.get(vertex.id).Size();
            int degree_2=C.relation.get(vertex.id).Size()+h-C.vertices.Size();
            int minDegree=Math.min(degree_1,degree_2);
            if(minDegree<min) min=minDegree;
        }
        return min;
    }
}
