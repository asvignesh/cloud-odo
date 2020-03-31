package io.cloudodo.vmwarewrapper.vmwareobject;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * User: asvignesh
 *
 * http://asvignesh.in/
 *
 * Date: 31/03/20
 */

@Getter
@AllArgsConstructor
public enum ErrorCode {

  SNAP_CREATE_FAILED(1000, "Failed to create snapshot"),
  VM_TOOL_ERROR(1001, "Guest VM Tools not running, operation failed"),
  SNAP_DELETE_FAILED(1002, "Failed to delete the snapshot"),
  SNAP_NOT_FOUND(1003,"The snapshot not exist"),
  SNAP_REVERT_FAILED(1004,"Failed to revert snapshot"),
  ;


  private Integer code;
  private String msg;
}
