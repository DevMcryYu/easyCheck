package com.devmcryyu.easycheck;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.devmcryyu.easycheck.Graph.DirectedGraph;

public class MainActivity extends AppCompatActivity {
    private DirectedGraph<String> currencyGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGraph();
    }

    private void initGraph() {
        currencyGraph = new DirectedGraph<>();
        String[] labelGroup = "美元 人民币 欧元 英镑 土耳其新里拉".split(" ");
        for (String str : labelGroup)
            currencyGraph.addVertex(str);
        currencyGraph.addEdge(labelGroup[0], labelGroup[1], 6.408200);
        currencyGraph.addEdge(labelGroup[0], labelGroup[2], 0.854600);
        currencyGraph.addEdge(labelGroup[0], labelGroup[3], 0.750740);
        currencyGraph.addEdge(labelGroup[0], labelGroup[4], 4.598101);
        currencyGraph.addEdge(labelGroup[1], labelGroup[0], 0.156050);
        currencyGraph.addEdge(labelGroup[1], labelGroup[2], 0.133360);
        currencyGraph.addEdge(labelGroup[1], labelGroup[3], 0.117153);
        currencyGraph.addEdge(labelGroup[2], labelGroup[0], 1.170138);
        currencyGraph.addEdge(labelGroup[2], labelGroup[1], 7.498479);
        currencyGraph.addEdge(labelGroup[2], labelGroup[3], 0.878469);
        currencyGraph.addEdge(labelGroup[2], labelGroup[4], 5.380413);
        currencyGraph.addEdge(labelGroup[3], labelGroup[0], 1.332019);
        currencyGraph.addEdge(labelGroup[3], labelGroup[1], 8.535845);
        currencyGraph.addEdge(labelGroup[3], labelGroup[2], 1.138344);
        currencyGraph.addEdge(labelGroup[4], labelGroup[0], 0.217481);
        currencyGraph.addEdge(labelGroup[4], labelGroup[2], 0.185859);
    }

}
