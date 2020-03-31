package io.cloudodo.vmwarewrapper.managedobjects;

import static io.cloudodo.vmwarewrapper.vmwareobject.ErrorCode.SNAP_CREATE_FAILED;
import static io.cloudodo.vmwarewrapper.vmwareobject.ErrorCode.SNAP_DELETE_FAILED;
import static io.cloudodo.vmwarewrapper.vmwareobject.ErrorCode.SNAP_NOT_FOUND;
import static io.cloudodo.vmwarewrapper.vmwareobject.ErrorCode.SNAP_REVERT_FAILED;
import static io.cloudodo.vmwarewrapper.vmwareobject.ErrorCode.VM_TOOL_ERROR;

import com.vmware.vim25.GuestInfo;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.VirtualMachineSnapshotInfo;
import com.vmware.vim25.VirtualMachineSnapshotTree;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;
import com.vmware.vim25.mo.VirtualMachineSnapshot;
import io.cloudodo.vmwarewrapper.exception.VMwareMOExcpetion;
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

  private GuestInfo getVmGuestInfo() throws Exception {
    return virtualMachine.getGuest();
  }

  public void shutdown() throws Exception {
    virtualMachine.shutdownGuest();
  }

  public void rebootGuest() throws Exception {
    virtualMachine.rebootGuest();
  }

  public void markAsTemplate() throws Exception {
    virtualMachine.markAsTemplate();
  }

  public boolean isTemplate() throws Exception {
    VirtualMachineConfigInfo configInfo = getConfigInfo();
    return configInfo.isTemplate();
  }

  private VirtualMachineConfigInfo getConfigInfo() {
    return virtualMachine.getConfig();
  }

  public boolean isVMwareToolsRunning() throws Exception {
    GuestInfo guestInfo = getVmGuestInfo();
    if (guestInfo != null) {
      if ("guestToolsRunning".equalsIgnoreCase(guestInfo.getToolsRunningStatus())) {
        return true;
      }
    }
    return false;
  }

  public void createSnapshot(String snapshotName, String snapshotDescription, boolean dumpMemory,
      boolean quiesce) throws Exception {
    if ((quiesce || dumpMemory) && !isVMwareToolsRunning()) {
      throw new VMwareMOExcpetion(VM_TOOL_ERROR);
    }

    Task snapshotTask = virtualMachine
        .createSnapshot_Task(snapshotName, snapshotDescription, dumpMemory, quiesce);
    String response = snapshotTask.waitForTask();
    if (!Task.SUCCESS.equals(response)) {
      throw new VMwareMOExcpetion(SNAP_CREATE_FAILED);
    }
  }

  private ManagedObjectReference getSnapshotMor(String snapshotName) {
    VirtualMachineSnapshotInfo info = getSnapshotInfo();
    if (info != null) {
      VirtualMachineSnapshotTree[] rootSnapshotList = info.getRootSnapshotList();
      return findSnapshotInTree(rootSnapshotList, snapshotName);
    }
    return null;
  }

  public boolean hasSnapshot() {
    VirtualMachineSnapshotInfo info = getSnapshotInfo();
    if (info != null) {
      return info.getCurrentSnapshot() != null;
    }
    return false;
  }

  private VirtualMachineSnapshotInfo getSnapshotInfo() {
    return virtualMachine.getSnapshot();
  }

  private ManagedObjectReference findSnapshotInTree(VirtualMachineSnapshotTree[] snapTree,
      String snapshotName) {
    for (VirtualMachineSnapshotTree machineSnapshotTree : snapTree) {
      VirtualMachineSnapshotTree vmSnapTree = machineSnapshotTree;
      String name = vmSnapTree.getName();
      if (snapshotName.equalsIgnoreCase(name)) {
        return vmSnapTree.getSnapshot();
      } else {
        VirtualMachineSnapshotTree[] snapChildTree = vmSnapTree
            .getChildSnapshotList();
        if (snapChildTree != null) {
          ManagedObjectReference mor = findSnapshotInTree(
              snapChildTree, snapshotName);
          if (mor != null) {
            return mor;
          }
        }
      }
    }
    return null;
  }

  private VirtualMachineSnapshot getSnapshotInTree(String snapshotName) {

    VirtualMachineSnapshotTree[] snapTree = null;
    snapTree = virtualMachine.getSnapshot().getRootSnapshotList();

    if (snapTree != null) {
      ManagedObjectReference mor = findSnapshotInTree(snapTree, snapshotName);
      if (mor != null) {
        VirtualMachineSnapshot virtualMachineSnapshot = new VirtualMachineSnapshot(
            virtualMachine.getServerConnection(), mor);
        return virtualMachineSnapshot;
      }
    }
    return null;
  }

  public void removeSnapshot(String snapshotName, boolean removeChildren, boolean consolidate)
      throws RemoteException, InterruptedException, VMwareMOExcpetion {
    VirtualMachineSnapshot virtualMachineSnapshot = getSnapshotInTree(snapshotName);
    Task removeSnapshotTask = virtualMachineSnapshot
        .removeSnapshot_Task(removeChildren, consolidate);
    waitfortaskHandleError(removeSnapshotTask, SNAP_DELETE_FAILED);
  }

  public void revertToSnapshot(String snapshotName) throws Exception {
    VirtualMachineSnapshot morSnapshot = getSnapshotInTree(snapshotName);
    if (morSnapshot == null) {
      throw new VMwareMOExcpetion(SNAP_NOT_FOUND);
    }
    Task revertToSnapshotTask = morSnapshot.revertToSnapshot_Task(null);
    waitfortaskHandleError(revertToSnapshotTask, SNAP_REVERT_FAILED);
  }

}
