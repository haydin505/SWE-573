package com.infrasave.bean;

import java.util.List;

/**
 * @author onurozcan
 */
public final class AppResponse<T> {

  private Boolean successful;

  private String errorCode;

  private String errorTitle;

  private String errorDetail;

  private List<Violation> violations;

  private T data;

  public static AppResponse successful() {
    AppResponse appResponse = new AppResponse();
    appResponse.setSuccessful(true);
    return appResponse;
  }

  public Boolean getSuccessful() {
    return successful;
  }

  public void setSuccessful(Boolean successful) {
    this.successful = successful;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorTitle() {
    return errorTitle;
  }

  public void setErrorTitle(String errorTitle) {
    this.errorTitle = errorTitle;
  }

  public String getErrorDetail() {
    return errorDetail;
  }

  public void setErrorDetail(String errorDetail) {
    this.errorDetail = errorDetail;
  }

  public List<Violation> getViolations() {
    return violations;
  }

  public void setViolations(List<Violation> violations) {
    this.violations = violations;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "AppResponse{" +
           "successful=" + successful +
           ", errorCode='" + errorCode + '\'' +
           ", errorTitle='" + errorTitle + '\'' +
           ", errorDetail='" + errorDetail + '\'' +
           ", violations=" + violations +
           ", data=" + data +
           '}';
  }

  public static class Violation {

    private final String field;

    private final String cause;

    private final String errorTitle;

    private final String errorDetail;

    public Violation(String field, String cause, String errorTitle, String errorDetail) {
      this.field = field;
      this.cause = cause;
      this.errorTitle = errorTitle;
      this.errorDetail = errorDetail;
    }

    public String getField() {
      return field;
    }

    public String getCause() {
      return cause;
    }

    public String getErrorTitle() {
      return errorTitle;
    }

    public String getErrorDetail() {
      return errorDetail;
    }

    @Override
    public String toString() {
      return "Violation{" +
             "field='" + field + '\'' +
             ", cause='" + cause + '\'' +
             ", errorTitle='" + errorTitle + '\'' +
             ", errorDetail='" + errorDetail + '\'' +
             '}';
    }
  }
}
