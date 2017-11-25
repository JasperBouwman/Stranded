package com.besaba.revonline.pastebinapi.response;




/**
 *
 */
public class FailedResponse<T> implements Response<T> {
  private final String reason;

  public FailedResponse(final String reason) {
    this.reason = reason;
  }


  @Override
  public T get() {
    // TODO replace with specific exception
    throw new RuntimeException(getError());
  }

  @Override
  public boolean hasError() {
    return true;
  }


  @Override
  public String getError() {
    return reason;
  }
}
