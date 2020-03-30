package io.cloudodo.vmwareobject;

import lombok.Data;

/**
 * User: asvignesh
 *
 * http://asvignesh.in/
 *
 * Date: 30/03/20
 */
@Data
public class VCenterConnectionConfig {

  private String hostname;
  private String port;
  private String username;
  private String password;

}
