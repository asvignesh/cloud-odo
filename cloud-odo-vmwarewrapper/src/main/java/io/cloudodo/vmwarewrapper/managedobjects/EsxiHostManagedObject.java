package io.cloudodo.vmwarewrapper.managedobjects;

import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.util.MorUtil;
import io.cloudodo.vmwarewrapper.vmwareobject.VCenterConnectionConfig;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * User: asvignesh
 *
 * http://asvignesh.in/
 *
 * Date: 30/03/20
 */
public class EsxiHostManagedObject extends BaseManagedObject {

  private HostSystem hostSystem;
  private static String TYPE = "VirtualMachine";

  public EsxiHostManagedObject(VCenterConnectionConfig vCenterConnectionConfig)
      throws MalformedURLException, RemoteException {
    super(vCenterConnectionConfig);
  }

  public EsxiHostManagedObject(ServiceInstance serviceInstance,
      ManagedObjectReference managedObjectReference) {
    super(serviceInstance);
    hostSystem = (HostSystem) MorUtil
        .createExactManagedObject(serviceInstance.getServerConnection(), managedObjectReference);
  }

}
