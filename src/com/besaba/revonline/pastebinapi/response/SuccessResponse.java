package com.besaba.revonline.pastebinapi.response;


/**
 *
 */
public class SuccessResponse<T> implements Response<T> {
    private final T result;

    public SuccessResponse(final T result) {
        this.result = result;
    }


    @Override
    public T get() {
        return result;
    }

    @Override
    public boolean hasError() {
        return false;
    }


    @Override
    public String getError() {
        return null;
    }
}
