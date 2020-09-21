package com.tencent.fskin.demo.base.adapter;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 支持ViewTypeData的Adapter for RecycleView
 *
 * 该类允许子类设置特定的ViewType，并为每个ViewType配置一个特定的ViewTypeViewHolder类，并把不同ViewType
 * 绑定数据的代码分发到特定的ViewTypeHolder,以防止Adapter代码的臃肿
 *
 * ViewTypeViewHolder允许配置一个特定的布局，以及数据绑定到UI上的方法
 *
 * DEMO:
 * <code>
      class Data implements BaseViewTypeAdapter.ViewTypeData {
            int viewType;
            public int getViewType() {
                return viewType;
            }
        }

       class VC1 extends ViewTypeViewHolder<Data> {
            protected void onViewHolderCreate() {
                super.onViewHolderCreate();
                setContentView(R.layout.list_item1);
            }
            protected void bindData(int position, Data data) {
                // binding data
            }
        }

       class VC2 extends ViewTypeViewHolder<Data> {
            protected void onViewHolderCreate() {
                super.onViewHolderCreate();
                setContentView(R.layout.list_item2);
            }
            protected void bindData(int position, Data data) {
                // binding data
            }
       }

        class MyAdapter extends BaseViewTypeAdapter<Data> {
            public MyAdapter(Context context, List<Data> datas) {
                super(context, datas);
                addViewType(0, VC1.class);
                addViewType(1, VC2.class);
            }
        }

            Userage:

        RecyclerView rv = (RecyclerView) findViewById(R.id.my_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        Random random = new Random();
        List<Data> datas = new ArrayList<>();
        for (int i=0; i<30; i++) {
            Data d= new Data();
            d.viewType = random.nextInt(3);
            datas.add(d);
        }
        MyAdapter adapter = new MyAdapter(this, datas);
        rv.setAdapter(adapter);
            </code>
 * Created by fortunexiao on 2016/12/15.
 */
public class BaseViewTypeAdapter<T> extends RecyclerView.Adapter {

//    private static final String TAG = BaseViewTypeAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_DEFAULT = 0;

    private final Context mContext;
    private final SparseArray<Class<? extends ViewTypeViewHolder>> mItems = new SparseArray<>();

    protected List<T> mData = new ArrayList<>();

    private ViewTypeViewHolderHook<T> viewTypeViewHolderHook;

    private FragmentManager fragmentManager;



    public BaseViewTypeAdapter(Context context) {
        this(context, null, null);
    }

    /**
     * 构造器
     *
     * @param context 上下文
     * @param data   数据列表
     */
    public BaseViewTypeAdapter(Context context, List<T> data) {
        this(context, null, data);
    }

    public BaseViewTypeAdapter(Context context, FragmentManager fragmentManager, List<T> data) {
        this.mContext = context;
        if (data != null) {
            this.mData = data;
        }
        this.fragmentManager = fragmentManager;
    }


    public Context getContext() {
        return mContext;
    }

    /**
     * 当List只有一种ViewType的时候使用默认的ViewType
     * @param viewHolderClass ViewTypeViewHolder
     */
    public void addViewType(Class<? extends ViewTypeViewHolder> viewHolderClass) {
        addViewType(VIEW_TYPE_DEFAULT, viewHolderClass);
    }

    /**
     * 增加一个ViewType
     * @param viewType        viewType
     * @param viewHolderClass ViewType对应的ViewHolder类
     */
    public void addViewType(int viewType, Class<? extends ViewTypeViewHolder> viewHolderClass) {
        mItems.put(viewType, viewHolderClass);
    }

    public void setViewTypeViewHolderHook(ViewTypeViewHolderHook<T> viewTypeViewHolderHook) {
        this.viewTypeViewHolderHook = viewTypeViewHolderHook;
    }

    @Override
    public int getItemViewType(int position) {
        T t = getItem(position);
        if (t != null && t instanceof ViewTypeData) {
            return ((ViewTypeData)t).getViewType();
        }
        return VIEW_TYPE_DEFAULT;
    }

    public void addDatas(List<T> datas) {
        if (datas != null) {
            if (this.mData == null) {
                this.mData = new ArrayList<>();
            }
            this.mData.addAll(datas);
            notifyDataSetChanged();
        }
    }

    public void addData(int index, T data) {
        if (data != null) {
            if (this.mData == null) {
                this.mData = new ArrayList<>();
            }
            this.mData.add(index, data);

            notifyDataSetChanged();
        }
    }

    public void addData(T data) {
        if (data != null) {
            if (this.mData == null) {
                this.mData = new ArrayList<>();
            }
            this.mData.add(data);

            notifyDataSetChanged();
        }
    }

    public boolean isEmpty() {
        return mData.isEmpty();
    }

    /**
     * 删除一个item
     * <br/>1.默认调用notifyItemRemoved更新界面
     * <br/>2.当数据全部被删除完时，调用notifyDataSetChanged更新界面
     * @param data
     */
    public void removeData(T data) {
        removeData(data, false);
    }

    /**
     *  删除一个item
     * @param data
     * @param isNotifyDataSetChanged true 调用notifyDataSetChanged更新界面；false 调用notifyItemRemoved更新界面
     */
    public void removeData(T data, boolean isNotifyDataSetChanged) {
        if (data == null || mData == null) {
            return;
        }
        int removePosition = mData.indexOf(data);
        if (removePosition == -1) {
            return;
        }

        mData.remove(data);

        if(isNotifyDataSetChanged || mData.size() == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRemoved(removePosition);
        }

    }

    /**
     * 监听ViewHolder的Recylcer
     * @param holder
     */
    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);

        if (holder instanceof StubViewHolder) {
            ((StubViewHolder)holder).viewTypeViewHolder.onRecycled();
        }
    }

    public List<T> getDatas() {
        return mData;
    }

    public void setDatas(List<T> datas) {
        if (datas != null) {
            this.mData = new ArrayList<>(datas);
        } else {
            this.mData = null;
        }
        notifyDataSetChanged();
    }


    @Override
    public final StubViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Class<? extends ViewTypeViewHolder> viewHolderClazz = mItems.get(viewType);

        if (viewHolderClazz == null) {
            viewHolderClazz = EmptyViewHolder.class;
        }

        ViewTypeViewHolder holder;
        try {
            holder = viewHolderClazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        onViewHolderInstance(holder);

        holder.context = mContext;
        holder.fragmentManager = fragmentManager;
        holder.mAdapter = this;
        holder.mParent = parent;

        holder.setViewTypeViewHolderHook(viewTypeViewHolderHook);

        holder.onPreCreate();
        holder.onCreate();
        onViewHolderCreated(holder);
        holder.onPostCreate();

        // 在onCreate方法
        if (holder.viewHolder == null) {
            throw new RuntimeException("You should call setContentView in onViewHolderCreate method");
        }

        return holder.viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StubViewHolder) {
            StubViewHolder<T> svh = (StubViewHolder<T>) holder;
            if (svh.viewTypeViewHolder != null) {
                svh.viewTypeViewHolder.bindDataInner(position, getItem(position));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }


    /**
     * 这个方法当Holder刚创建当时候调用
     * @param holder
     */
    protected void onViewHolderInstance(ViewTypeViewHolder holder) {

    }

    /**
     * ViewHolder被创建的回调
     * 允许用户在ViewHolder创建的时候个性初始化ViewHolder
     *
     * 该方法在ViewHolder的onCreate之后在onPostCreate之前调用
     *
     * @param holder 创建的ViewHolder
     */
    protected void onViewHolderCreated(ViewTypeViewHolder holder) {

        // 首先判断Adapter是否设置了OnItemClickListener
        if (onItemClickListener != null) {

            // 在看下Adapter的onItemClick是否自己处理了，如果处理了，则忽略ViewHolder的onItemClick
            final OnItemClickListener orgHolderItemClickListener = holder.onItemClickListener;

            holder.onItemClickListener = new OnItemClickListener<T>() {
                @Override
                public boolean onItemClick(int position, T data) {
                    if (!onItemClickListener.onItemClick(position, data)) {
                        if (orgHolderItemClickListener != null) {
                            orgHolderItemClickListener.onItemClick(position, data);
                        }
                    }
                    return false;
                }
            };
        }

        if (onItemLongClickListener != null) {

            // 在看下Adapter的onItemClick是否自己处理了，如果处理了，则忽略ViewHolder的onItemClick
            final OnItemLongClickListener orgHolderItemLongClickListener = holder.onItemLongClickListener;

            holder.onItemLongClickListener = new OnItemLongClickListener<T>() {
                @Override
                public boolean onItemLongClick(int position, T data) {
                    if (!onItemLongClickListener.onItemLongClick(position, data)) {
                        if (orgHolderItemLongClickListener != null) {
                            orgHolderItemLongClickListener.onItemLongClick(position, data);
                        }
                    }
                    return false;
                }
            };

        }
    }

    protected T getItem(int position) {
        return (mData != null && position < mData.size()) ? mData.get(position) : null;
    }


    /*Item点击监听器*/

    private OnItemClickListener<T> onItemClickListener;

    /**
     * 设置点击Item监听
     * @param listener 监听器
     */
    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.onItemClickListener = listener;
    }

    /**
     * Adapter也有onItemClickListener
     * @param <U>
     */
    public interface OnItemClickListener<U> {
        boolean onItemClick(int position, U data);
    }


    /*Item点击监听器*/

    private OnItemLongClickListener<T> onItemLongClickListener;

    /**
     * 设置点击Item监听
     * @param listener 监听器
     */
    public void setOnItemLongClickListener(OnItemLongClickListener<T> listener) {
        this.onItemLongClickListener = listener;
    }

    /**
     * Adapter也有onItemClickListener
     * @param <U>
     */
    public interface OnItemLongClickListener<U> {
        boolean onItemLongClick(int position, U data);
    }


    /**
     * 当用户配置ViewType的时候同时需要配置一个ViewTypeViewHolder类
     * 该类负责为特定的ViewType绑定数据
     *
     * @param <U>
     */
    public static abstract class ViewTypeViewHolder<U> {

        private static final int KEY_VIEW_HOLDER = 0x7f0a0034;

        /**
         * 根据ItemView获取其ViewTypeViewHolder
         * @param view
         * @return
         */
        public static ViewTypeViewHolder getViewHolder(View view) {
            if (view != null) {
                Object vh = view.getTag(KEY_VIEW_HOLDER);
                if (vh instanceof ViewTypeViewHolder) {
                    return (ViewTypeViewHolder) vh;
                }
            }
            return null;
        }


        /**
         * RecyclerView本身的ViewHolder
         */
        private StubViewHolder<U> viewHolder;

        /**
         * 当前ViewHolder所依附的Adapter
         */
        private BaseViewTypeAdapter mAdapter;

        /**
         * 一个上下文
         */
        private Context context;

        /**
         * 当前ViewHolder在Adapter中的位置
         */
        private int currentBindPosition;

        /**
         * 当前ViewHolder绑定的数据对象
         */
        private U currentBindData;

        private Bundle mArgs;

        private ViewTypeViewHolderHook<U> viewTypeViewHolderHook;

        /**
         * fragmentManager，需要用户手动在Adapter中指定
         *
         */
        private FragmentManager fragmentManager;

        /**
         * 一些附加数据
         */
        public final Map<String, Object> extra = new HashMap<>();


        /**
         * 当前RecyclerView
         */
        private ViewGroup mParent;

        // === methods ===


        public Bundle getArgs() {
            return mArgs;
        }

        public void setArgs(Bundle mArgs) {
            this.mArgs = mArgs;
        }

        public final Context getContext() {
            return context;
        }

        public FragmentManager getFragmentManager() {
            return fragmentManager;
        }


        /**
         * 标识当前的ViewHolder是否被添加到窗口上
         */
        protected boolean isAttachToWindow = false;

        protected void onPreCreate() {}

        protected void onCreate() {
            if (viewTypeViewHolderHook != null) {
                viewTypeViewHolderHook.onViewHolderCreate(this);
            }

            if (mParent instanceof RecyclerView) {
                RecyclerView rv = (RecyclerView) mParent;
                rv.addOnScrollListener(new ScrollListener(this));
            }
//            myRecyclerView?.addOnScrollListener(ScrollListener(this))

        }

        protected void onPostCreate() {
            if (viewTypeViewHolderHook != null) {
                viewTypeViewHolderHook.onViewHoldPostCreate(this);
            }
        }

        public void setContentView(int resId) {
            setContentViewInner(LayoutInflater.from(getContext()).inflate(resId, mParent, false));
        }

        public void setContentView(View view) {
            setContentViewInner(view);
        }


        /**
         * ViewHolder被添加到窗口上
         */
        protected void onAttachToWindow() {

        }

        public void onRecycled() {

        }

        /**
         * 通知Adapter数据变化了
         */
        public void notifyDataSetChanged() {
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }

        /**
         * 当前的ViewHolder从窗口中移除
         */
        protected void onDetachFromWindow() {

//            RecyclerView rv = null;
//            final LinearLayoutManager llm = (LinearLayoutManager)rv.getLayoutManager();
//            int first = llm.findFirstCompletelyVisibleItemPosition();
//            int last = llm.findLastCompletelyVisibleItemPosition();
//            if (currentBindPosition >= first && currentBindPosition <= last) {
//                return true;
//            } else {
//                return false;
//            }
        }

        /**
         * 当前的ViewHolder在RecyclerView中是否完全可见
         * @return
         */
        protected boolean isCompletelyVisible() {
            if (mParent instanceof RecyclerView) {
                final RecyclerView rv = (RecyclerView) mParent;
                final RecyclerView.LayoutManager lm = rv.getLayoutManager();
                if (lm instanceof LinearLayoutManager) {
                    LinearLayoutManager llm = (LinearLayoutManager) lm;

                    int first = llm.findFirstCompletelyVisibleItemPosition();
                    int last = llm.findLastCompletelyVisibleItemPosition();

                    if (currentBindPosition >= first && currentBindPosition <= last) {
                        return true;
                    }
                }
            }
            return false;
        }

        private void setContentViewInner(View view) {

            // 把ViewHolder添加到View到tag里
            view.setTag(KEY_VIEW_HOLDER, this);

            viewHolder = new StubViewHolder<>(view, this);

            viewHolder.itemView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    isAttachToWindow = true;
                    onAttachToWindow();
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    isAttachToWindow = false;
                    onDetachFromWindow();
                }
            });


            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(currentBindPosition, currentBindData);
                    }
                    onItemClick(currentBindPosition, currentBindData);
                    if (viewTypeViewHolderHook != null) {
                        viewTypeViewHolderHook.onViewHolderItemClick(ViewTypeViewHolder.this, currentBindData);
                    }
                }
            });

            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(currentBindPosition, currentBindData);
                    }
                    boolean isHandle = onItemLongClick(currentBindPosition, currentBindData);
                    if (viewTypeViewHolderHook != null) {
                        viewTypeViewHolderHook.onViewHolderItemLongClick(ViewTypeViewHolder.this, currentBindData);
                    }
                    return isHandle;
                }
            });

            if (viewTypeViewHolderHook != null) {
                viewTypeViewHolderHook.onViewHolderSetContentViewView(this, viewHolder.itemView);
            }
        }

        public void setViewTypeViewHolderHook(ViewTypeViewHolderHook<U> viewTypeViewHolderHook) {
            this.viewTypeViewHolderHook = viewTypeViewHolderHook;
        }

        private void setAdapter(BaseViewTypeAdapter adapter) {
            this.mAdapter = adapter;
        }


        /**
         * 当前ViewHolder的容器RecyclerView
         * @return
         */
        public ViewGroup getParentView() {
            return mParent;
        }

        /**
         * 当前ViewHolder依附的Adapter
         * @return
         */
        public BaseViewTypeAdapter getAdapter() {
            return mAdapter;
        }

        public <T extends View> T findViewById(int id) {
            if (viewHolder != null) {
                return viewHolder.itemView.findViewById(id);
            }
            return null;
        }

        private void bindDataInner(int position, U data) {
            currentBindPosition = position;
            currentBindData = data;
            bindData(position, data);
            if (viewTypeViewHolderHook != null) {
                viewTypeViewHolderHook.onViewHolderBindData(this, data);
            }
        }

        /**
         * 绑定item的数据
         *
         * @param position 位置
         * @param data     数据
         */
        protected abstract void bindData(int position, U data);

        /**
         * 返回当前ViewHolder bind的Data对象
         * @return 当前绑定的数据对象
         */
        protected U getCurrentBindData() {
            return currentBindData;
        }

        /**
         * 返回当前ViewHolder绑定的position
         * @return ViewHolder绑定的position
         */
        public int getCurrentBindPosition() {
            return currentBindPosition;
        }

        public View getItemView() {
            return viewHolder != null ? viewHolder.itemView : null;
        }

        /*Item点击监听器*/

        private OnItemClickListener<U> onItemClickListener;
        private OnItemLongClickListener<U> onItemLongClickListener;

        protected void onItemClick(int position, U data) {}

        protected boolean onItemLongClick(int position, U data) { return false;}

//        /**
//         * 设置点击Item监听
//         * @param listener 监听器
//         */
//        private void setOnItemClickListener(OnItemClickListener<U> listener) {
//            this.onItemClickListener = listener;
//        }
//
//        private OnItemClickListener<U> getOnItemClickListener() {
//            return onItemClickListener;
//        }
//
//        /**
//         * ViewHolder 有自己的onItemClickListener监听器
//         * @param <K>
//         */
//        private interface OnItemClickListener<K> {
//            void onItemClick(int position, K data);
//        }


        private static class ScrollListener extends RecyclerView.OnScrollListener {


            private SoftReference<ViewTypeViewHolder> ref;

            ScrollListener(ViewTypeViewHolder viewHolder) {
                ref = new SoftReference(viewHolder);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                ViewTypeViewHolder vth = ref.get();
                if (vth != null) {
                    vth.handleRecyclerScroll(dy, recyclerView);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                ViewTypeViewHolder vth = ref.get();
                if (vth != null) {
                    vth.onRecyclerScrollStateChanged(recyclerView, newState);
                }
            }
        }
//
//        /**
//         * 监听RecyclerView的Scroll
//         */
//        private static class ScrollListener implements RecyclerView.OnScrollListener() {
//            var baseRef: SoftReference<VHBaseScoll<*>> = SoftReference(base)
//
//            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                Log.d(TAG, "Recycler onScrolled: $dy, contentY:${(recyclerView as? MyRecyclerView)?.contentScrollY}")
//                baseRef.get()?.run {
//                    handleRecyclerScroll(dy, recyclerView)
//                }
//            }
//
//            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                baseRef.get()?.onRecyclerScrollStateChanged(recyclerView, newState)
//            }
//        }

        /**
         * 监听RecyclerView的滑动状态
         * @param recyclerView
         * @param newState
         */
        protected void  onRecyclerScrollStateChanged(RecyclerView recyclerView, int newState) {

        }


        /**
         * 当前的RecyclerView在滑动
         * @param dy
         * @param recyclerView
         */
        protected void handleRecyclerScroll(int dy, RecyclerView recyclerView) {}

    }


    /**
     * ViewHolder的一些方法的Hook，允许外部通过后门来做一些羞羞的事
     * @param <T>
     */
    public interface ViewTypeViewHolderHook<T> {
        void onViewHolderCreate(ViewTypeViewHolder<T> holder);
        void onViewHolderSetContentViewView(ViewTypeViewHolder<T> holder, View contentView);
        void onViewHoldPostCreate(ViewTypeViewHolder<T> holder);
        void onViewHolderBindData(ViewTypeViewHolder<T> holder, T data);
        void onViewHolderItemClick(ViewTypeViewHolder<T> holder, T data);
        void onViewHolderItemLongClick(ViewTypeViewHolder<T> holder, T data);
    }

    public static class AbsViewTypeViewHolderHook<T> implements ViewTypeViewHolderHook<T> {

        @Override
        public void onViewHolderCreate(ViewTypeViewHolder<T> holder) {}

        @Override
        public void onViewHolderSetContentViewView(ViewTypeViewHolder<T> holder, View contentView) {}

        @Override
        public void onViewHoldPostCreate(ViewTypeViewHolder<T> holder) {

        }

        @Override
        public void onViewHolderBindData(ViewTypeViewHolder<T> holder, T data) {}

        @Override
        public void onViewHolderItemClick(ViewTypeViewHolder<T> holder, T data) {}

        @Override
        public void onViewHolderItemLongClick(ViewTypeViewHolder<T> holder, T data) {}
    }


    public final static class StubViewHolder<U> extends RecyclerView.ViewHolder {
        public final ViewTypeViewHolder<U> viewTypeViewHolder;

        private StubViewHolder(View itemView, ViewTypeViewHolder<U> viewTypeViewHolder) {
            super(itemView);
            this.viewTypeViewHolder = viewTypeViewHolder;
        }
    }

    /**
     * 要支持ViewTypeAdapter数据源必须实现该接口
     */
    @Deprecated
    public interface ViewTypeData {

        /**
         * 返回数据源对应的viewType
         *
         * @return viewType
         */
        int getViewType();
    }

    /**
     * 当数据源返回一个没有配置的ViewType的时候，该Item隐藏
     *
     * PS:这个类还不能设置成private，因为通过Class.newInstance来创建对象
     */
    public final static class EmptyViewHolder<T> extends ViewTypeViewHolder<T> {

        @Override
        protected void onCreate() {
            super.onCreate();
            View emptyView = new View(getContext());
            emptyView.setLayoutParams(new RecyclerView.LayoutParams(1, 0));
            setContentView(emptyView);
        }

        @Override
        public void bindData(int position, T data) {
            // do nothing
        }
    }
}
