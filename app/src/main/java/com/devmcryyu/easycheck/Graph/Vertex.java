package com.devmcryyu.easycheck.Graph;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by 92075 on 2018/6/4.
 */

public class Vertex<T> implements VertexInterface<T>, Serializable {
    private T label;                                                                                //标识标点,可以用不同类型来标识顶点
    private List<Edge> edgeList;                                                                    //到该顶点邻接点的边,LinkedList实现
    private boolean Visited;                                                                        //标记顶点是否已访问
    private VertexInterface<T> previousVertex;                                                      //该顶点的前驱顶点
    private double cost;                                                                            //顶点的权值,区别于边的权值

    public Vertex(T vertexLabel) {
        label = vertexLabel;
        edgeList = new LinkedList<>();                                                              //是Vertex的属性,说明每个顶点都有一个edgeList用来存储所有与该顶点关系的边
        Visited = false;
        previousVertex = null;
        cost = 0;
    }

    /**
     * 这里用了一个单独的类来表示边,主要是考虑到带权值的边
     * 可以看出,Edge类封装了一个顶点和一个double类型变量
     * 若不需要考虑权值,可以不需要单独创建一个Edge类来表示边,只需要一个保存顶点的列表即可
     */

    protected class Edge implements Serializable {
        private VertexInterface<T> vertex;                                                          //终点
        private double weight;                                                                      //权值

        //Vertex 类本身就代表顶点对象,因此在这里只需提供 endVertex，就可以表示一条边了
        protected Edge(VertexInterface<T> endVertex, double edgeWeight) {
            vertex = endVertex;
            weight = edgeWeight;
        }

        protected VertexInterface<T> getEndVertex() {
            return vertex;
        }

        protected double getWeight() {
            return weight;
        }

        protected void setWeight(double weight) {
            this.weight = weight;
        }
    }

    /**
     * 遍历该顶点邻接点的迭代器--为 getNeighborIterator()方法 提供迭代器
     * 由于顶点的邻接点以边的形式存储在java.util.List中,因此借助List的迭代器来实现
     */

    private class NeighborIterator implements Iterator<VertexInterface<T>> {
        Iterator<Edge> edgesIterator;

        private NeighborIterator() {
            edgesIterator = edgeList.iterator();                                                    //获得LinkedList的迭代器
        }

        @Override
        public boolean hasNext() {
            return edgesIterator.hasNext();
        }

        @Override
        public VertexInterface<T> next() {
            VertexInterface<T> nextNeighbor = null;
            if (edgesIterator.hasNext()) {
                Edge edgeToNextNeighbor = edgesIterator.next();
                nextNeighbor = edgeToNextNeighbor.getEndVertex();
            } else
                throw new NoSuchElementException();
            return nextNeighbor;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * 生成一个遍历该顶点所有邻接边的权值的迭代器
     * 权值是Edge类的属性,因此先获得一个遍历Edge对象的迭代器,取得Edge对象,再获得权值
     */

    private class WeightIterator implements Iterator {
        private Iterator<Edge> edgesIterator;

        private WeightIterator() {
            edgesIterator = edgeList.iterator();
        }

        @Override
        public boolean hasNext() {
            return edgesIterator.hasNext();
        }

        @Override
        public Edge next() {
            Edge edge;
            if (edgesIterator.hasNext())
                edge = edgesIterator.next();
            else throw new NoSuchElementException();
            return edge;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public double getWeight(VertexInterface<T> endVertex) {
        Edge nextNeighbor = null;
        WeightIterator iterator = new WeightIterator();
        while (iterator.hasNext()) {
            nextNeighbor = iterator.next();
            if (endVertex.equals(nextNeighbor.getEndVertex()))
                break;
        }
        return nextNeighbor.weight;
    }

    @Override
    public void setWeight(VertexInterface<T> endVertex, double weight) {
        Edge nextNeighbor = null;
        WeightIterator iterator = new WeightIterator();
        if (weight > 0) {
            while (iterator.hasNext()) {
                nextNeighbor = iterator.next();
                if (endVertex.equals(nextNeighbor.getEndVertex())) {
                    nextNeighbor.setWeight(weight);
                    break;
                }
            }
        }
    }

    @Override
    public T getLabel() {
        return label;
    }

    @Override
    public void visit() {
        this.Visited = true;
    }

    @Override
    public void unVisit() {
        this.Visited = false;
    }

    @Override
    public boolean isVisited() {
        return Visited;
    }

    @Override
    public boolean connect(VertexInterface<T> endVertex, double edgeWeight) {
        //将"边"(边的实质是顶点)插入顶点的邻接表
        boolean result = false;
        if (!this.equals(endVertex)) {                                                              //顶点互不相同
            Iterator<VertexInterface<T>> neighbors = this.getNeighborIterator();
            boolean duplicateEdge = false;
            while (!duplicateEdge && neighbors.hasNext()) {                                         //保证不添加重复的边
                VertexInterface<T> nextNeighbor = neighbors.next();
                if (endVertex.equals(nextNeighbor)) {
                    duplicateEdge = true;
                    break;
                }
            }
            if (!duplicateEdge) {
                edgeList.add(new Edge(endVertex, edgeWeight));                                      //添加一条新边
                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean connect(VertexInterface endVertex) {
        return connect(endVertex, 0);
    }

    @Override
    public Iterator<VertexInterface<T>> getNeighborIterator() {
        return new NeighborIterator();
    }

    @Override
    public Iterator<Double> getWeightIterator() {
        return new WeightIterator();
    }

    @Override
    public boolean hasNeighbor() {
        return !(edgeList.isEmpty());
    }

    @Override
    public VertexInterface<T> getUnvisitedNeighbor() {                                              //最坏情况下复杂度为O(E)
        VertexInterface<T> result = null;
        Iterator<VertexInterface<T>> neighbors = getNeighborIterator();
        while (neighbors.hasNext() && result == null) {                                             //获得该顶点的第一个未被访问的邻接点
            VertexInterface<T> nextNeighbor = neighbors.next();
            if (!nextNeighbor.isVisited())
                result = nextNeighbor;
        }
        return result;
    }

    @Override
    public void setPredecessor(VertexInterface<T> predecessor) {
        this.previousVertex = predecessor;
    }

    @Override
    public VertexInterface<T> getPredecessor() {
        return this.previousVertex;
    }

    @Override
    public boolean hasPredecessor() {
        return this.previousVertex != null;
    }

    @Override
    public void setCost(double newCost) {
        cost = newCost;
    }

    @Override
    public double getCost() {
        return cost;
    }

    /**
     * 判断两个顶点是否相同
     */

    public boolean equals(Object other) {
        boolean result;
        if ((other == null) || (getClass() != other.getClass()))
            result = false;
        else {
            Vertex<T> otherVertex = (Vertex<T>) other;
            result = label.equals(otherVertex.label);                                               //节点是否相同最终还是由标识节点类型的类的equals() 决定
        }
        return result;
    }
}
