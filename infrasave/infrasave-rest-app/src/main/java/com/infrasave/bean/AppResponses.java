package com.infrasave.bean;

/**
 * @author huseyinaydin
 */
public final class AppResponses {

  private static final AppResponse DEFAULT_SUCCESS = successful(null);

  public static AppResponse successful() {
    return DEFAULT_SUCCESS;
  }

  public static <T> AppResponse<T> successful(T data) {
    AppResponse<T> appResponse = new AppResponse<>();
    appResponse.setSuccessful(true);
    appResponse.setData(data);
    return appResponse;
  }

  public static <T> AppResponse<T> failure(String errorCode) {
    return failure(errorCode, null, null);
  }

  public static <T> AppResponse<T> failure(String errorCode, String errorTitle, String errorDetail) {
    AppResponse<T> appResponse = new AppResponse<>();
    appResponse.setSuccessful(false);
    appResponse.setErrorCode(errorCode);
    appResponse.setErrorTitle(errorTitle);
    appResponse.setErrorDetail(errorDetail);
    return appResponse;
  }
}

