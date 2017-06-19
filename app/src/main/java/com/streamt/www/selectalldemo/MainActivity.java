package com.streamt.www.selectalldemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private TextView mSelectAll;
    private TextView mDelete;
    private LinearLayout mLlEdite;
    private TextView mEditable;

    public static List<String> content;

//    private boolean[] flag = new boolean[100];//此处添加一个boolean类型的数组

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
        initData();

        myAdapter = new MyAdapter(content);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

        myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {


            }

            @Override
            public void checkBoxClick(int position, boolean isChecked) {
                if (!isChecked) {
                    mSelectAll.setText("全选");
                    isSelectAll = false;
                }
            }
        });
    }

    private void initData() {
        content = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            content.add("数据：" + i);
        }
    }

    private void initListener() {
        mEditable.setOnClickListener(this);
        mSelectAll.setOnClickListener(this);
        mDelete.setOnClickListener(this);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.list_view);
        mSelectAll = (TextView) findViewById(R.id.id_select_all);
        mDelete = (TextView) findViewById(R.id.id_delete);
        mLlEdite = (LinearLayout) findViewById(R.id.ll_edit);
        mEditable = (TextView) findViewById(R.id.editable);
    }

    //标识 全选/取消全选 的状态
    boolean isSelectAll = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editable://编辑
                if (!myAdapter.isShow()) {//非编辑状态
                    mEditable.setText("取消");
                    mLlEdite.setVisibility(View.VISIBLE);
                    myAdapter.setShow(true);
//                    myAdapter.notifyItemRangeChanged(0, myAdapter.getItemCount() - 1);//重绘recylerView
                } else {//编辑状态
                    mEditable.setText("编辑");
                    mLlEdite.setVisibility(View.GONE);
                    myAdapter.setShow(false);
                    //将所有checkbox状态还原
                    for (int i = 0; i < MyApp.flag.length; i++) {
                        MyApp.flag[i] = false;
                    }
                    if (isSelectAll) {
                        mSelectAll.setText("全选");
                        isSelectAll = false;
                    }
                }
                myAdapter.notifyItemRangeChanged(0, myAdapter.getItemCount() - 1);//重绘recylerView
                break;
            case R.id.id_select_all:
//                Toast.makeText(this,"全选",Toast.LENGTH_SHORT).show();
                isSelectAll = !isSelectAll;
                if (isSelectAll) {//全选
                    mSelectAll.setText("取消全选");
                    for (int i = 0; i < MyApp.flag.length; i++) {
                        MyApp.flag[i] = true;
                    }
                } else {//取消全选
                    mSelectAll.setText("全选");
                    for (int i = 0; i < MyApp.flag.length; i++) {
                        MyApp.flag[i] = false;
                    }
                }
                myAdapter.notifyItemRangeChanged(0, myAdapter.getItemCount() - 1);//重绘recylerView
                break;
            case R.id.id_delete://删除
                //将选中的删除
                if (isSelectAll) {
                    content.clear();
                } else {
                    Log.e(TAG, "onClick: delete");
                    for (int i = 0; i < content.size(); i++) {
                        if (MyApp.flag[i]) {
                            content.remove(i);
                            MyApp.flag[i] = Boolean.parseBoolean(null);
                        }
                    }
                }
                myAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (int i = 0; i < MyApp.flag.length; i++) {
            MyApp.flag[i] = false;
        }
    }
}
