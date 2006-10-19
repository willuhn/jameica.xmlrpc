/**********************************************************************
 * $Source: /cvsroot/jameica/jameica.xmlrpc/src/de/willuhn/jameica/xmlrpc/server/HandlerMappingImpl.java,v $
 * $Revision: 1.1 $
 * $Date: 2006/10/19 16:08:30 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.xmlrpc.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.server.XmlRpcNoSuchHandlerException;

import de.willuhn.jameica.xmlrpc.rmi.HandlerMapping;
import de.willuhn.logging.Logger;

/**
 * Implementiert das Mapping von XML-RPC-Namen auf Klassen.
 * @author willuhn
 */
public class HandlerMappingImpl extends UnicastRemoteObject implements
    HandlerMapping
{
  private boolean started = false;

  /**
   * ct,
   * @throws RemoteException
   */
  public HandlerMappingImpl() throws RemoteException
  {
    super();
  }

  /**
   * @see de.willuhn.datasource.Service#getName()
   */
  public String getName() throws RemoteException
  {
    return "XML-RPC Handlermapping";
  }

  /**
   * @see de.willuhn.datasource.Service#isStartable()
   */
  public boolean isStartable() throws RemoteException
  {
    return !started;
  }

  /**
   * @see de.willuhn.datasource.Service#isStarted()
   */
  public boolean isStarted() throws RemoteException
  {
    return started;
  }

  /**
   * @see de.willuhn.datasource.Service#start()
   */
  public void start() throws RemoteException
  {
    if (!isStartable() || isStarted())
    {
      Logger.warn("service not startable or allready started, skipping request");
      return;
    }
    this.started = true;
  }

  /**
   * @see de.willuhn.datasource.Service#stop(boolean)
   */
  public void stop(boolean arg0) throws RemoteException
  {
    if (!isStarted())
    {
      Logger.warn("service not started, skipping request");
      return;
    }
    started = false;
  }

  /**
   * @see org.apache.xmlrpc.server.XmlRpcHandlerMapping#getHandler(java.lang.String)
   */
  public XmlRpcHandler getHandler(String arg0)
      throws XmlRpcNoSuchHandlerException, XmlRpcException
  {
    if (!started)
      throw new XmlRpcException("xml-rpc handlermapper not started");
    return null;
  }

}


/*********************************************************************
 * $Log: HandlerMappingImpl.java,v $
 * Revision 1.1  2006/10/19 16:08:30  willuhn
 * *** empty log message ***
 *
 *********************************************************************/