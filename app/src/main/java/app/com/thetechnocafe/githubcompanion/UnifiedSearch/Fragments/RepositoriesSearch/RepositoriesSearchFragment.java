package app.com.thetechnocafe.githubcompanion.UnifiedSearch.Fragments.RepositoriesSearch;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import app.com.thetechnocafe.githubcompanion.Models.RepositoriesModel;
import app.com.thetechnocafe.githubcompanion.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gurleensethi on 06/01/17.
 */

public class RepositoriesSearchFragment extends Fragment implements RepositoriesSearchContract.View {

    @BindView(R.id.recycler_view)
    RecyclerView mRepositoriesRecyclerView;

    private static final String TAG = RepositoriesSearchFragment.class.getSimpleName();
    private static final String ARG_SEARCH_KEYWORD = "search_keyword";
    private RepositoriesSearchContract.Presenter mPresenter;
    private RepositoriesRecyclerAdapter mRepositoriesRecyclerAdapter;

    public static Fragment getInstance(String searchKeyword) {
        //Create arguments bundle
        Bundle args = new Bundle();
        args.putString(ARG_SEARCH_KEYWORD, searchKeyword);

        //Create fragment and apply arguments
        Fragment fragment = new RepositoriesSearchFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_repositories_search, container, false);

        ButterKnife.bind(this, root);

        mPresenter = new RepositoriesSearchPresenter();
        mPresenter.attachView(this);
        mPresenter.loadRepositories(getArguments().getString(ARG_SEARCH_KEYWORD));

        return root;
    }

    @Override
    public Context getAppContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void initViews() {
        mRepositoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void showRepositories(List<RepositoriesModel> list) {
        setUpOrRefreshRecyclerView(list);
    }

    //Set up recycler view (create new adapter if already not created, else refresh it)
    private void setUpOrRefreshRecyclerView(List<RepositoriesModel> list) {
        if (mRepositoriesRecyclerAdapter == null) {
            mRepositoriesRecyclerAdapter = new RepositoriesRecyclerAdapter(getContext(), list);
            mRepositoriesRecyclerView.setAdapter(mRepositoriesRecyclerAdapter);
        } else {
            mRepositoriesRecyclerAdapter.notifyDataSetChanged();
        }
    }
}
