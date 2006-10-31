/**********************************************************************
 * $Source: /cvsroot/jameica/jameica.xmlrpc/src/de/willuhn/jameica/xmlrpc/rmi/Attic/XmlRpcService.java,v $
 * $Revision: 1.3 $
 * $Date: 2006/10/31 17:06:26 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.xmlrpc.rmi;

import java.rmi.RemoteException;

import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.Service;

/**
 * Container fuer die Eigenschaften eines XML-RPC-Services.
 */
public interface XmlRpcService extends GenericObject
{
  /**
   * Prueft, ob der Service via XML-RPC verfuegbar sein soll.
   * @return true, wenn er verfuegbar sein soll.
   * @throws RemoteException
   */
  public boolean isShared() throws RemoteException;
  
  /**
   * Legt fest, ob der Service via XML-RPC verfuegbar sein soll.
   * @param shared true, wenn er verfuegbar sein soll.
   * @throws RemoteException
   */
  public void setShared(boolean shared) throws RemoteException;
  
  /**
   * Liefert den Namen des Services.
   * @return Name des Services.
   * @throws RemoteException
   */
  public String getServiceName() throws RemoteException;

  /**
   * Liefert den Namen des Plugins.
   * @return Name des Plugins.
   * @throws RemoteException
   */
  public String getPluginName() throws RemoteException;
  
  /**
   * Liefert den zugehoerigen Service.
   * @return der zugehoerige Service.
   * @throws RemoteException
   */
  public Service getService() throws RemoteException;
}


/*********************************************************************
 * $Log: XmlRpcService.java,v $
 * Revision 1.3  2006/10/31 17:06:26  willuhn
 * @N GUI to configure xml-rpc
 *
 **********************************************************************/