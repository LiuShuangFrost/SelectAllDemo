package com.streamt.www.selectalldemo;

import android.graphics.Color;
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

    /**
     * 数据集
     */
    public static List<String> content;

    /**
     * 标识 全选/取消全选 的状态
     */
    boolean isSelectAll = false;

    /**
     * 记录被删除的条目个数，用于控制底部 全选、删除 布局的隐藏
     */
    int deleteNum = 0;



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
            public void checkBoxClick(int position, boolean isChecked) {
                /**
                 * 用于记录被选中的CheckBox的个数
                 */
                int selectedNum = 0;
                for (int i = 0; i < MyApp.flag.length; i++) {
                    if (MyApp.flag[i]) {
                        selectedNum++;
                    }
                }
                if (selectedNum > 0) {
                    String text = "删除" + "(" + selectedNum + ")";
                    setDeleteStatus(text, Color.RED);
                }
                if (!isChecked) {
                    mSelectAll.setText("全选");
                    isSelectAll = false;
                }
            }
        });
    }

    /**
     * 设置底部 删除 的状态
     *
     * @param text  文字
     * @param color 字体颜色
     */
    private void setDeleteStatus(String text, int color) {
        mDelete.setText(text);
        mDelete.setTextColor(color);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editable://编辑
                if (!myAdapter.isShow()) {//非编辑状态
                    setEditableStatus("取消", View.VISIBLE, true);
                } else {//编辑状态
                    setEditableStatus("编辑", View.GONE, false);
                    //将所有checkbox状态还原
                    setCheckBoxStatus(false);
                    if (isSelectAll) {
                        mSelectAll.setText("全选");
                        isSelectAll = false;
                    }
                }
                myAdapter.notifyItemRangeChanged(0, myAdapter.getItemCount() - 1);//重绘recylerView
                break;
            case R.id.id_select_all:
                isSelectAll = !isSelectAll;
                if (isSelectAll) {//全选
                    setSelectAllStatus(true, "取消全选");
                    setDeleteStatus("删除" + "(" + content.size() + ")", Color.RED);
                } else {//取消全选
                    setSelectAllStatus(false, "全选");
                }
                myAdapter.notifyItemRangeChanged(0, myAdapter.getItemCount() - 1);//重绘recylerView
                break;
            case R.id.id_delete://删除
                setDeleteStatus("删除", Color.GRAY);
                if (isSelectAll) {//全选
                    //清空数据集
                    content.clear();
                    //数据集中没有数据时
                    deleteNum = MyApp.flag.length;
                } else {
                    //将选中的删除
                    Log.e(TAG, "onClick: delete");
                    int count = myAdapter.getItemCount();
                    for (int i = 0; i < count; i++) {
                        // 因为List的特性,删除了2个item,则3变成2,所以这里要进行这样的换算,才能拿到删除后真正的position
                        int position = i - (count - myAdapter.getItemCount());
                        if (MyApp.flag[i]) {
                            content.remove(position);
                            MyApp.flag[i] = false;
                            deleteNum++;
                        }
                    }
                    myAdapter.notifyDataSetChanged();
                }
                Log.e(TAG, "onClick: deldeteNum:" + deleteNum);
                if (deleteNum == MyApp.flag.length) {
                    mEditable.setText("编辑");
                    mLlEdite.setVisibility(View.GONE);
                }
                break;
        }
    }

    /**
     * 设置CheckBox的状态
     *
     * @param status true:选中 false：未选中
     */
    private void setCheckBoxStatus(boolean status) {
        for (int i = 0; i < MyApp.flag.length; i++) {
            MyApp.flag[i] = status;
        }
    }

    /**
     * 设置Editable（编辑/取消）的状态
     *
     * @param text    编辑/取消
     * @param visible 底部全选/取消全选 隐藏或显示
     * @param show    是否显示checkBox
     */
    private void setEditableStatus(String text, int visible, boolean show) {
        mEditable.setText(text);
        mLlEdite.setVisibility(visible);
        myAdapter.setShow(show);
    }

    /**
     * 设置全选的状态：文字、checkBox的选中状态
     *
     * @param isSelectAll 是否全选
     * @param text        全选/取消全选
     */
    private void setSelectAllStatus(boolean isSelectAll, String text) {
        mSelectAll.setText(text);
        setCheckBoxStatus(isSelectAll);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //当界面退到后台，再次进入的时候CheckBox之前的状态还在，在这里进行清除
        setCheckBoxStatus(false);
    }
}
