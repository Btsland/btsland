package info.btsland.app.ui.view;

/**
 * Created by Administrator on 2017/10/23.
 */

public interface OnRefreshListener {

    /**
     * 下拉刷新
     */
    void onDownPullRefresh();

    /**
     * 上拉加载更多
     */
    void onLoadingMore();
}
