/**********************************************************************
 * $Source: /cvsroot/jameica/jameica.xmlrpc/src/de/willuhn/jameica/xmlrpc/server/Attic/LookupServiceImpl.java,v $
 * $Revision: 1.1 $
 * $Date: 2006/10/19 15:27:01 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by  bbv AG
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.xmlrpc.server;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

import de.willuhn.jameica.xmlrpc.rmi.LookupService;

/**
 * @author willuhn
 */
public class LookupServiceImpl extends UnicastRemoteObject implements
    LookupService
{

  /**
   * @throws RemoteException
   */
  public LookupServiceImpl() throws RemoteException {
    // TODO Auto-generated constructor stub
  }

  /**
   * @param port
   * @throws RemoteException
   */
  public LookupServiceImpl(int port) throws RemoteException {
    super(port);
    // TODO Auto-generated constructor stub
  }

  /**
   * @param port
   * @param csf
   * @param ssf
   * @throws RemoteException
   */
  public LookupServiceImpl(int port, RMIClientSocketFactory csf,
      RMIServerSocketFactory ssf) throws RemoteException {
    super(port, csf, ssf);
    // TODO Auto-generated constructor stub
  }

  /**
   * @see de.willuhn.datasource.Service#getName()
   */
  public String getName() throws RemoteException
  {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see de.willuhn.datasource.Service#isStartable()
   */
  public boolean isStartable() throws RemoteException
  {
    // TODO Auto-generated method stub
    return false;
  }

  /**
   * @see de.willuhn.datasource.Service#isStarted()
   */
  public boolean isStarted() throws RemoteException
  {
    // TODO Auto-generated method stub
    return false;
  }

  /**
   * @see de.willuhn.datasource.Service#start()
   */
  public void start() throws RemoteException
  {
    // TODO Auto-generated method stub

  }

  /**
   * @see de.willuhn.datasource.Service#stop(boolean)
   */
  public void stop(boolean arg0) throws RemoteException
  {
    // TODO Auto-generated method stub

  }

}


/*********************************************************************
 * $Log: LookupServiceImpl.java,v $
 * Revision 1.1  2006/10/19 15:27:01  willuhn
 * @N initial checkin
 *
 *********************************************************************/