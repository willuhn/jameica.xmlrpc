/**********************************************************************
 * $Source: /cvsroot/jameica/jameica.xmlrpc/src/de/willuhn/jameica/xmlrpc/server/Attic/HttpServiceImpl.java,v $
 * $Revision: 1.7 $
 * $Date: 2007/05/15 15:40:43 $
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

import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;

import de.willuhn.logging.Logger;

/**
 * @author willuhn
 */
public class HttpServiceImpl extends UnicastRemoteObject implements
    de.willuhn.jameica.xmlrpc.rmi.HttpService
{

  private MyWebServer server = null;
  
  /**
   * @throws RemoteException
   */
  public HttpServiceImpl() throws RemoteException
  {
    super();
  }


  /**
   * @see de.willuhn.datasource.Service#getName()
   */
  public String getName() throws RemoteException
  {
    return "XML/RPC HTTP-Connector";
  }

  /**
   * @see de.willuhn.datasource.Service#isStartable()
   */
  public boolean isStartable() throws RemoteException
  {
    return server == null;
  }

  /**
   * @see de.willuhn.datasource.Service#isStarted()
   */
  public boolean isStarted() throws RemoteException
  {
    return server != null;
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

    try
    {
      Logger.info("starting XML/RPC HTTP-Connector");
      this.server = new MyWebServer();
      
      XmlRpcServer xmlRpcServer = this.server.getXmlRpcServer();
      xmlRpcServer.setHandlerMapping(new HandlerMappingImpl());
    
      XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
      serverConfig.setEnabledForExtensions(true);
      serverConfig.setContentLengthOptional(false);

      this.server.start();
    }
    catch (Exception e)
    {
      this.server = null;
      Logger.error("unable to start xml/rpc connector",e);
      throw new RemoteException("unable to start xml/rpc connector",e);
    }
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
    try
    {
      Logger.info("stopping XML/RPC HTTP-Connector");
      this.server.shutdown();
    }
    finally
    {
      this.server = null;
    }
    
  }

}


/*********************************************************************
 * $Log: HttpServiceImpl.java,v $
 * Revision 1.7  2007/05/15 15:40:43  willuhn
 * *** empty log message ***
 *
 * Revision 1.6  2006/10/31 17:44:20  willuhn
 * *** empty log message ***
 *
 * Revision 1.5  2006/10/31 01:43:07  willuhn
 * *** empty log message ***
 *
 * Revision 1.4  2006/10/26 23:54:15  willuhn
 * @N added needed jars
 * @N first working version
 *
 * Revision 1.3  2006/10/23 23:07:28  willuhn
 * *** empty log message ***
 *
 * Revision 1.2  2006/10/19 16:08:30  willuhn
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/19 15:27:01  willuhn
 * @N initial checkin
 *
 *********************************************************************/