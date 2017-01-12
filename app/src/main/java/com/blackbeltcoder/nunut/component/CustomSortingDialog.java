package com.blackbeltcoder.nunut.component;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.blackbeltcoder.nunut.R;
import com.blackbeltcoder.nunut.adapter.SortListAdapter;
import com.blackbeltcoder.nunut.global.ConstantVariable;
import com.blackbeltcoder.nunut.pojo.SortObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by ainozenbook on 10/9/2016.
 */

public class CustomSortingDialog extends Dialog {

    private Activity a;
    private OnResultSortingDialogListener onResultSortingDialogListener;
    private Button btnClose;
    private RecyclerView rvDataList;
    private List<SortObject> dataSet;
    private SortObject dataObj;
    private SortListAdapter adapter;
    private long sortValue, sortMode;

    public interface OnResultSortingDialogListener {
        public void OnResultSortingDialog(long sortValue);
    }

    public CustomSortingDialog(Activity a, Hashtable<String, Long> param, OnResultSortingDialogListener onResultSortingDialogListener) {
        super(a);
        this.a = a;
        this.sortValue = param.get("sortValue");
        this.sortMode = param.get("sortMode");
        this.onResultSortingDialogListener = onResultSortingDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_sort_dialog);

        /*
        * INIT COMPONENT
        * */
        btnClose = (Button) findViewById(R.id.btnClose);
        rvDataList = (RecyclerView) findViewById(R.id.rvDataList);


        /*
        * BIND COMPONENT
        * */
        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        String[] sortCriteria = null;
        dataSet = new ArrayList<SortObject>();
        if(sortMode == ConstantVariable.MODE_SORT_ROUTE){
            sortCriteria = getContext().getResources().getStringArray(R.array.route_sort_criteria);
        }

        for(long i = 0;i < sortCriteria.length;i++){
            dataObj = new SortObject();
            dataObj.setId(i);
            dataObj.setLabel(sortCriteria[(int) i]);

            if(i == sortValue)
                dataObj.setStatus("Y");
            else
                dataObj.setStatus("N");

            dataSet.add(dataObj);
        }
        adapter = new SortListAdapter(dataSet, getContext(), new SortListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SortObject item) {
                onResultSortingDialogListener.OnResultSortingDialog(item.getId());
                dismiss();
            }
        });
        rvDataList.setAdapter(adapter);
        rvDataList.setLayoutManager(new LinearLayoutManager(getContext()));


        /*
        * PREPARE COMPONENT
        * */
        Window window = getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.dialog_animation;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0.7f;
        lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(lp);
    }
}
