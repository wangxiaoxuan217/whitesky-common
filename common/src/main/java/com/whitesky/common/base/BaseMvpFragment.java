package com.whitesky.common.base;

import androidx.lifecycle.Lifecycle;

import autodispose2.AutoDispose;
import autodispose2.AutoDisposeConverter;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public abstract class BaseMvpFragment<T extends BasePresenter> extends BaseFragment implements BaseView
{
    protected T mPresenter;
    
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        if (mPresenter != null)
        {
            mPresenter.detachView();
        }
        super.onDestroyView();
    }
    
    /**
     * 绑定生命周期 防止MVP内存泄漏
     *
     * @param <T>
     * @return
     */
    @Override
    public <T> AutoDisposeConverter<T> bindAutoDispose()
    {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY));
    }
}
