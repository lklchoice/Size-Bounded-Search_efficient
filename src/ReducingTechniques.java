import java.util.*;

public class ReducingTechniques {
    Graph C;


    Graph R;

    Graph Cr;

    Graph Rc;

    Graph originGraph;
    int h;

    int l;

    int k;
    //第一种剪枝，把点从R里面删除
    public void deleteR_1(){
        //对于传入的C R两幅图，对于R进行删点
        for(Vertex vertex:R.vertices.Hset){
            int degree_1=R.relation.get(vertex.id).Hset.size()+Rc.relation.get(vertex.id).Hset.size();
            int degree_2=Rc.relation.get(vertex.id).Hset.size()+h-C.vertices.Hset.size()-1;
            int curMin=Math.min(degree_1,degree_2);
            if(curMin<=k){
                R.DelFromG(vertex);
                Rc.DelFromG(vertex);
                for(Vertex vertex1:Cr.vertices.Hset){
                    if(Cr.relation.get(vertex1.id).Hset.contains(vertex)){
                        Cr.relation.get(vertex1.id).Hset.remove(vertex);
                    }
                }
            }
        }
    }
    //第二种剪枝，同样的把点从R里面删除
    public void deleteR_2(){
        Vertex u=null;
        for(Vertex vertex1:C.vertices.Hset){
            u=vertex1;
            break;
        }
        for(Vertex vertex:R.vertices.Hset){
            if(n(k+1,distance(u,vertex))>h){
                //discard v
                R.DelFromG(vertex);
                Rc.DelFromG(vertex);
                for(Vertex vertex1:Cr.vertices.Hset){
                    if(Cr.relation.get(vertex1.id).Hset.contains(vertex)){
                        Cr.relation.get(vertex1.id).Hset.remove(vertex);
                    }
                }
            }
        }
    }
    private int n(int k,int D){
        if(D>=1 &&D<=2||k==1){
            return k+D;
        }else{
            return (int) (k+D+1+Math.floor(1.0*D/3)*(k-2));
        }
    }

    private int distance(Vertex u,Vertex v){//求解v点到C里面所有点的最短路径，然后取一个最小值，就是如果n下限还大于h就抛弃
        //设置队列存储路径,只能在原图里面找
        Queue<List<Vertex>> queue=new LinkedList<>();
        Map<Vertex,Integer> visited=new HashMap<>();
        for(Vertex vertex:C.vertices.Hset){
            visited.put(vertex,0);
        }
        List<Vertex> list=new ArrayList<>();list.add(v);
        queue.offer(list);
        Set<List<Vertex>> ans=new HashSet<>();//存储所有可行路径
        while(!queue.isEmpty()){
            list=queue.poll();
            if(list.get(list.size()-1)==u){
                //找到一条路径
                ans.add(list);
            }
            for(Vertex vertex:originGraph.relation.get(list.get(list.size()-1)).Hset){
                if(visited.get(vertex)==0){
                    list.add(vertex);
                    queue.offer(list);
                    visited.put(vertex,1);
                }
            }
        }
        int min=Integer.MAX_VALUE;
        for(List list1:ans){
            if(list1.size()<min){
                min=list1.size();
            }
        }
        return min;
    }

    public  void  InclusionWay(){
        for(Vertex u:C.vertices.Hset){
            if(C.relation.get(u.id).Hset.size()+Cr.relation.get(u.id).Hset.size()==k+1){
                //greedily move to C
                //u在R里面的邻居，所以使用Cr
                HashSet<Vertex> keepSet=Cr.relation.get(u.id).Hset;
                //Cr对于新增的点直接用R里面的点，但是对于原先的点包含
                for(Vertex vertex1:Cr.vertices.Hset){
                    if(Cr.relation.get(vertex1.id).Hset.contains(keepSet)){
                        Cr.relation.get(vertex1.id).Hset.remove(keepSet);
                    }
                }
                for(Vertex vertex:keepSet){
                    //delete from R and Rc move to C and Cr
                    Cr.vertices.Hset.add(vertex);
                    Cr.relation.put(vertex.id,R.relation.get(vertex.id));
                    C.vertices.Hset.add(vertex);
                    C.relation.put(vertex.id,Rc.relation.get(vertex.id));
                    R.DelFromG(vertex);
                    Rc.DelFromG(vertex);
                }
            }
        }
    }
}
