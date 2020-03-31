package io.cloudodo.vmwarewrapper.managedobjects;

import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import io.cloudodo.vmwarewrapper.exception.VMwareMOExcpetion;
import io.cloudodo.vmwarewrapper.vmwareobject.ErrorCode;
import io.cloudodo.vmwarewrapper.vmwareobject.VCenterConnectionConfig;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

/**
 * User: asvignesh
 *
 * http://asvignesh.in/
 *
 * Date: 30/03/20
 */
public class BaseManagedObject {

  protected ServiceInstance serviceInstance;

  public BaseManagedObject(VCenterConnectionConfig vCenterConnectionConfig)
      throws MalformedURLException, RemoteException {
    serviceInstance = new ServiceInstance(new URL(vCenterConnectionConfig.getHostname()),
        vCenterConnectionConfig.getUsername(), vCenterConnectionConfig.getPassword(),
        true);
  }

  protected BaseManagedObject(ServiceInstance serviceInstance) {
    this.serviceInstance = serviceInstance;
  }

  protected Folder getFolder() {
    return serviceInstance.getRootFolder();
  }

  protected void waitfortaskHandleError(Task task, ErrorCode errorCode)
      throws RemoteException, InterruptedException, VMwareMOExcpetion {
    String status = task.waitForTask();
    if (!status.equals("success")) {
      String message = task.getTaskInfo().getError()
          .getLocalizedMessage();
      throw new VMwareMOExcpetion(errorCode, message);
    }
  }
}
