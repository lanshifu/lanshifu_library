package library.lanshifu.com.lsf_library.commwidget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import library.lanshifu.com.lsf_library.R;

/**
 * Created by lWX385269 on 2017/1/4.
 */

public class IRecyclerView extends FrameLayout {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private View loadMoreView;

    private boolean isRefresh = false;
    private boolean isLoadMore = false;

    public IRecyclerView(Context context) {
        this(context,null);
    }

    public IRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public IRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View view  = LayoutInflater.from(context).inflate(R.layout.irecycler_view, null);

        mSwipeRefreshLayout = (SwipeRefreshLayout)view. findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mrecyclerView);
        loadMoreView = view.findViewById(R.id.loadMoreView);

        addListener();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        addView(view,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
    }



    private void addListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(mSwipeRefreshListener !=null){
                    mSwipeRefreshListener.onRefresh();
                }
            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);

                int lastVisibleItemPosition = -1;

                if(newState == RecyclerView.SCROLL_STATE_IDLE && !isLoadMore && mSwipeRefreshListener != null){

                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if(layoutManager instanceof LinearLayoutManager){
                        lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    }

                    //最后一条
                    if(layoutManager.getChildCount() >0 && lastVisibleItemPosition>= layoutManager.getItemCount()-1 && !isLoadMore){
                        setIsLoadingMore(true);
                        mSwipeRefreshListener.onLoadMore();
                    }

                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    private void  setIsLoadingMore(boolean isLoadMore){
        this.isLoadMore = isLoadMore;
        loadMoreView.setVisibility(isLoadMore ? VISIBLE : GONE);

    }


    public void setAdapter(RecyclerView.Adapter adapter){
        mRecyclerView.setAdapter(adapter);
    }


    /**
     * 加载更多完成
     */
    public void loadMoreComplete(){
        setIsLoadingMore(false);

    }


    /***
     * 刷新完成
     */
    public void refreshComplete(){
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private OnSwipeRefreshListener mSwipeRefreshListener;

    public void setOnRefreshListener(OnSwipeRefreshListener mSwipeRefreshListener){
        this.mSwipeRefreshListener = mSwipeRefreshListener;

    }

    public interface OnSwipeRefreshListener {
        void onRefresh();
        void onLoadMore();
    }





}
