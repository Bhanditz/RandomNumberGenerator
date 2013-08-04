package com.luver.random;

/**
 * @author Vladislav Lubenskiy
 */
public interface CallbackClient<Result> {
    void registerCallback(Callback<Result> callback);
}
