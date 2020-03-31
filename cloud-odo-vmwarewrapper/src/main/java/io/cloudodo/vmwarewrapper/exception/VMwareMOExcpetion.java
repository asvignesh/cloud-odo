package io.cloudodo.vmwarewrapper.exception;

import io.cloudodo.vmwarewrapper.vmwareobject.ErrorCode;
import lombok.Getter;

/**
 * User: asvignesh
 *
 * http://asvignesh.in/
 *
 * Date: 31/03/20
 */
@Getter
public class VMwareMOExcpetion extends Exception {

  private static final long serialVersionUID = 3030374277105375809L;

  private Integer code;
  private String message;
  private String traceMessage;

  public VMwareMOExcpetion() {
    super();
  }

  public VMwareMOExcpetion(ErrorCode errorCode) {
    super(errorCode.getMsg());
    this.code = errorCode.getCode();
    this.message = errorCode.getMsg();
    this.traceMessage = errorCode.getMsg();
  }

  public VMwareMOExcpetion(ErrorCode errorCode, String message) {
    super(errorCode.getMsg());
    this.code = errorCode.getCode();
    this.message = errorCode.getMsg();
    this.traceMessage = message;
  }


  public VMwareMOExcpetion(String message, Throwable cause) {
    super(message, cause);
  }

  public VMwareMOExcpetion(String message) {
    super(message);
  }

  public VMwareMOExcpetion(Throwable cause) {
    super(cause);
  }

}
