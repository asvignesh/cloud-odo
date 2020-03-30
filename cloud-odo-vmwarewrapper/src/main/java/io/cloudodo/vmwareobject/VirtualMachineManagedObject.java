package io.cloudodo.vmwareobject;

import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.VirtualMachine;
import io.cloudodo.vmwarewrapper.managedobjects.BaseManagedObject;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * User: asvignesh
 *
 * http://asvignesh.in/
 *
 * Date: 30/03/20
 */
public class VirtualMachineManagedObject extends BaseManagedObject {

  private VirtualMachine virtualMachine;
  private static String TYPE = "VirtualMachine";

  public VirtualMachineManagedObject(VCenterConnectionConfig vCenterConnectionConfig,
      String vmname) throws RemoteException, MalformedURLException {
    super(vCenterConnectionConfig);

    virtualMachine = (VirtualMachine) new InventoryNavigator(getFolder())
        .searchManagedEntity(TYPE, vmname);
  }

  public EsxiHostManagedObject getRunningHost() throws Exception {
    VirtualMachineRuntimeInfo runtimeInfo = getRuntimeInfo();
    return new EsxiHostManagedObject(serviceInstance, runtimeInfo.getHost());
  }

  private VirtualMachineRuntimeInfo getRuntimeInfo() {
    return null;
  }

  public String getVmName() throws Exception {
    return (String) virtualMachine.getName();
  }


}
