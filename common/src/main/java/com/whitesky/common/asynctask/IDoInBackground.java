package com.whitesky.common.asynctask;

public interface IDoInBackground<Params, Progress, Result>
{
    Result doInBackground(IPublishProgress<Progress> publishProgress, Params... params);
}
