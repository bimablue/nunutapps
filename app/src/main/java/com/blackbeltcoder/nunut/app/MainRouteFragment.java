package com.blackbeltcoder.nunut.app;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.blackbeltcoder.nunut.R;
import com.blackbeltcoder.nunut.adapter.RouteListAdapter;
import com.blackbeltcoder.nunut.component.CustomSortingDialog;
import com.blackbeltcoder.nunut.component.OnHidingScrollListener;
import com.blackbeltcoder.nunut.contract.RouteContract;
import com.blackbeltcoder.nunut.global.App;
import com.blackbeltcoder.nunut.global.ConstantVariable;
import com.blackbeltcoder.nunut.model.RouteModel;
import com.blackbeltcoder.nunut.pojo.SortSuggestion;
import com.blackbeltcoder.nunut.service.RestService;
import com.blackbeltcoder.nunut.util.ConverterUtil;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainRouteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainRouteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainRouteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RelativeLayout fragmentContainer;
    private LinearLayout layoutLoadingData;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingSearchView floatingSearchView;
    private RouteListAdapter adapter;
    private MaterialRefreshLayout refreshLayout;

    private long sortValueGlobal;
    private String mLastQuery = "";
    private Handler h;
    private Runnable r1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MainRouteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainRouteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainRouteFragment newInstance(String param1, String param2) {
        MainRouteFragment fragment = new MainRouteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        sortValueGlobal = 0l;
        h = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_route, container, false);

        fragmentContainer = (RelativeLayout) rootView.findViewById(R.id.fragmentContainer);
        layoutLoadingData = (LinearLayout) rootView.findViewById(R.id.layoutLoadingData);

        floatingSearchView = (FloatingSearchView) rootView.findViewById(R.id.floatingSearchView);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        refreshLayout = (MaterialRefreshLayout) rootView.findViewById(R.id.refreshLayout);

        initSearchView(rootView);
        initRefreshView(rootView);

        /*String routeStatus = App.secPref.getString("masterStatus", "C");
        if(routeStatus.equals("P")){
            layoutLoadingData.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
        } else {*/
            layoutLoadingData.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
        //}

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getActivity().getSupportLoaderManager().initLoader(ConstantVariable.LOADER_ROUTE, null, this);

        super.onActivityCreated(savedInstanceState);
    }

    private void initSearchView(View rootView){
        floatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    floatingSearchView.clearSuggestions();
                } else {
                    floatingSearchView.showProgress();

                    List<SortSuggestion> results = new ArrayList<SortSuggestion>();
                    List<RouteModel> rms = adapter.getOriginalItem();
                    int count = rms.size();
                    for (int i = 0; i < count; i++) {
                        if (rms.get(i).nameOrigin.toLowerCase().contains(newQuery.toLowerCase())) {
                            results.add(new SortSuggestion(rms.get(i).nameOrigin));
                        } else if (rms.get(i).nameDestination.toLowerCase().contains(newQuery.toLowerCase())) {
                            results.add(new SortSuggestion(rms.get(i).nameDestination));
                        }
                    }
                    floatingSearchView.swapSuggestions(results);
                    floatingSearchView.hideProgress();
                }
            }
        });

        floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                mLastQuery = searchSuggestion.getBody();
            }

            @Override
            public void onSearchAction(String query) {
                mLastQuery = query;
            }
        });

        floatingSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                floatingSearchView.setSearchText(mLastQuery);
            }

            @Override
            public void onFocusCleared() {
                mLastQuery = floatingSearchView.getQuery();
                adapter.getFilter().filter(floatingSearchView.getQuery() + "#" + sortValueGlobal);
            }
        });

        floatingSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                Hashtable<String, Long> param = new Hashtable<String, Long>();
                param.put("sortValue", sortValueGlobal);
                param.put("sortMode", ConstantVariable.MODE_SORT_ROUTE);
                new CustomSortingDialog(getActivity(), param
                        , new CustomSortingDialog.OnResultSortingDialogListener() {

                    @Override
                    public void OnResultSortingDialog(long sortValue) {
                        // TODO Auto-generated method stub
                        sortValueGlobal = sortValue;
                        refreshLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                refreshLayout.autoRefresh();
                            }
                        });
                    }
                }).show();
            }
        });
    }

    private void initRefreshView(View rootView){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setOnScrollListener(new OnHidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }
            @Override
            public void onShow() {
                showViews();
            }
        });

        List<RouteModel> rms = new ArrayList<RouteModel>();
        adapter = new RouteListAdapter(rms, getContext(), new RouteListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RouteModel item) {
                Snackbar.make(getView(), "Origin (" + item.nameOrigin + ")", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                /*if (mListener != null) {
                    mListener.onFragmentInteraction(item);
                }*/
            }
        });
        recyclerView.setAdapter(adapter);

        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                r1 =new Runnable() {
                    @Override
                    public void run() {
                        //App.secPref.put("masterStatus", "P");
                        refreshLayout.finishRefresh();

                        layoutLoadingData.setVisibility(View.VISIBLE);
                        refreshLayout.setVisibility(View.GONE);

                        Intent intent = new Intent(getActivity().getApplicationContext(), RestService.class);
                        intent.putExtra(ConstantVariable.BROADCAST_KEY, ConstantVariable.BROADCAST_KEY_ROUTES);
                        intent.putExtra(ConstantVariable.BROADCAST_OBJ, App.getNuter());
                        getActivity().startService(intent);
                    }
                };
                h.postDelayed(r1,1000);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                //load more refreshing...
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), RouteContract.CONTENT_URI, null, null, null, RouteContract.COLUMN_NAME_ORIGIN + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<RouteModel> rms = new ArrayList<RouteModel>();
        RouteModel rm;

        if(data != null) {
            while (data.moveToNext()) {
                rm = ConverterUtil.cursorToObject(RouteModel.class, data);
                rms.add(rm);
            }
        }

        if(rms.size() > 0){
            layoutLoadingData.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
            adapter.swap(rms);
        } else {
            layoutLoadingData.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swap(new ArrayList<RouteModel>());
    }

    private void hideViews() {
        floatingSearchView.animate().translationY(-floatingSearchView.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    private void showViews() {
        floatingSearchView.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

    public void refresh() {
        if (getArguments().getInt(ARG_PARAM2, 0) > 0 && recyclerView != null) {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    /**
     * Called when a fragment will be displayed
     */
    public void willBeDisplayed() {
        // Do what you want here, for example animate the content
        if (fragmentContainer != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            fragmentContainer.startAnimation(fadeIn);
        }
    }

    /**
     * Called when a fragment will be hidden
     */
    public void willBeHidden() {
        if (fragmentContainer != null) {
            Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
            fragmentContainer.startAnimation(fadeOut);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
