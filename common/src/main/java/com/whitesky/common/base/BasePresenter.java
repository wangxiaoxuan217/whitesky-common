package com.whitesky.common.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public abstract class BasePresenter<T extends BaseView>
{
    protected Reference<T> mViewRef;

    public void attachView(T view)
    {
        // 同View建立关联
        mViewRef = new WeakReference<T>(view);
    }

    protected T getView()
    {
        // 获取View引用
        return mViewRef.get();
    }

    public boolean isViewAttached()
    {
        // 判断是否与View建立关联
        return mViewRef != null && mViewRef.get() != null;
    }

    public void detachView()
    {
        // 解除与View的关联
        if (mViewRef != null)
        {
            mViewRef.clear();
            mViewRef = null;
        }
    }
}
