package com.devmcryyu.easycheck;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.devmcryyu.easycheck.Graph.DirectedGraph;
import com.devmcryyu.easycheck.Graph.VertexInterface;

import java.util.Queue;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "easyCheck";
    public static final String GRAPH = "美元 人民币 欧元 英镑 土耳其新里拉 卢布";
    private Context mContext;
    private DirectedGraph<String> currencyGraph;
    private ImageButton btn_update, btn_exchange;
    private Button btn_request;
    private EditText edit_exchangeRate, edit_begin, edit_end;
    private Spinner spinner_begin, spinner_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initGraph();
        initView();

        //TEST
        Queue<String> queue = currencyGraph.getBreadthFirstTraversal("人民币");
        Log.i(TAG, "广度优先遍历(人民币)");
        while (!queue.isEmpty()) {
            Log.i(TAG, queue.poll());
        }
        queue = currencyGraph.getDepthFirstTraversal("人民币");
        Log.i(TAG, "深度优先遍历(人民币)");
        while (!queue.isEmpty()) {
            Log.i(TAG, queue.poll());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showResult();
    }

    private void initGraph() {
        currencyGraph = new DirectedGraph<>();
        String[] labelGroup = GRAPH.split(" ");
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
        currencyGraph.addEdge(labelGroup[4], labelGroup[5], 2.015484);
        currencyGraph.addEdge(labelGroup[5], labelGroup[4], 1.015484);
    }

    private void initView() {
        //TODO: init view
        final String[] items = GRAPH.split(" ");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_begin = findViewById(R.id.beginSpinner);
        spinner_begin.setAdapter(adapter);
        spinner_begin.setSelection(1, true);
        spinner_begin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this, items[position], Toast.LENGTH_SHORT).show();
                double rate = getExchangeRate(items[spinner_begin.getSelectedItemPosition()], items[spinner_end.getSelectedItemPosition()]);
                edit_exchangeRate.setText(String.valueOf(rate));
                showResult();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_end = findViewById(R.id.endSpinner);
        spinner_end.setAdapter(adapter);
        spinner_end.setSelection(0, true);
        spinner_end.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this, items[position], Toast.LENGTH_SHORT).show();
                double rate = getExchangeRate(items[spinner_begin.getSelectedItemPosition()], items[spinner_end.getSelectedItemPosition()]);
                edit_exchangeRate.setText(String.valueOf(rate));
                showResult();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edit_begin = findViewById(R.id.begin);
        edit_end = findViewById(R.id.end);
        edit_exchangeRate = findViewById(R.id.edit_exchangeRate);
        edit_exchangeRate.setText(String.format("%s", getExchangeRate(items[spinner_begin.getSelectedItemPosition()], items[spinner_end.getSelectedItemPosition()])));
        btn_exchange = findViewById(R.id.btn_exchange);
        btn_exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int beginPosition = spinner_begin.getSelectedItemPosition();
                int endPosition = spinner_end.getSelectedItemPosition();
                spinner_begin.setSelection(endPosition);
                spinner_end.setSelection(beginPosition);
                double rate = getExchangeRate(items[spinner_begin.getSelectedItemPosition()], items[spinner_end.getSelectedItemPosition()]);
                edit_exchangeRate.setText(String.valueOf(rate));
                showResult();
            }
        });
        btn_update = findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = edit_exchangeRate.getText().toString();
                if (!str.equals("")) {
                    double rate = Double.parseDouble(str);
                    setExchangeRate(items[spinner_begin.getSelectedItemPosition()], items[spinner_end.getSelectedItemPosition()], rate);
                }
            }
        });
        btn_request = findViewById(R.id.btn_request);
        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResult();
            }
        });
    }

    private double getExchangeRate(String begin, String end) {
        Stack<String> stack = new Stack<>();
        double totalWeight = 1;
        if (begin.equals(end))
            return 1;
        Log.i(TAG, "" + currencyGraph.getShortestPath(begin, end, stack));
        while (!stack.isEmpty()) {
            VertexInterface<String> vertex = currencyGraph.getVertex(stack.pop());
            if (stack.isEmpty())
                break;
            Log.i(TAG, vertex.getLabel() + "与" + stack.peek() + "汇率为: " + vertex.getWeight(currencyGraph.getVertex(stack.peek())));
            totalWeight *= vertex.getWeight(currencyGraph.getVertex(stack.peek()));
        }
        Log.i(TAG, "TotalWeight is :" + totalWeight);
        return totalWeight;
    }

    private void setExchangeRate(String begin, String end, double rate) {
        VertexInterface<String> vertex = currencyGraph.getVertex(begin);
        vertex.setWeight(currencyGraph.getVertex(end), rate);
    }

    private void showResult() {
        if (Double.valueOf(edit_begin.getText().toString()) > 100000)
            Toast.makeText(mContext, "你自己有没有这么多钱心里还没点数嘛?", Toast.LENGTH_SHORT).show();
        else {
            double result = Double.valueOf(edit_begin.getText().toString()) * Double.valueOf(edit_exchangeRate.getText().toString());
            Log.i(TAG, "结果为: " + String.format("%.2f", result));
            edit_end.setText(String.format("%.2f", result));
        }
    }

}