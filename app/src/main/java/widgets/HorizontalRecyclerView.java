package widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Sushant Hatcheryhub on 18/12/16.
 */

public class HorizontalRecyclerView extends RecyclerView {
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private int startingPageIndex = 0;
    private int currentPage = 0;
    private LinearLayoutManager layoutManager;
    /**
     * @param context
     */
    public HorizontalRecyclerView(Context context) {
        super(context);
        initView();
    }

    /**
     * @param context
     * @param attrs
     */
    public HorizontalRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public HorizontalRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    /**
     *
     */
    private void initView() {
         layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        setLayoutManager(layoutManager);
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView mRecyclerView, int dx, int dy) {
                super.onScrolled(mRecyclerView, dx, dy);
                LinearLayoutManager mLayoutManager = (LinearLayoutManager) mRecyclerView
                        .getLayoutManager();

                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                onScroll(firstVisibleItem, visibleItemCount, totalItemCount);
            }
        });
    }


    /**
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    public void onScroll(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
            currentPage++;
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem +
                visibleThreshold)) {
            if (onLoadMoreListener != null)
                onLoadMoreListener.
                        onLoadMore(currentPage + 1, totalItemCount);
            loading = true;
        }
    }

    // Defines the process for actually loading more data based on page
    public interface IOnLoadMoreListener {
        /**
         * @param page
         * @param totalItemsCount
         */
        public void onLoadMore(int page, int totalItemsCount);
    }

    private IOnLoadMoreListener onLoadMoreListener;

    /**
     * @param onLoadMoreListener
     */
    public void setOnLoadMoreListener(IOnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void scrollIn(){
       getLayoutManager().scrollToPosition(layoutManager.findLastVisibleItemPosition() - 1);
    }
}

