/**********************************************************************
 * $Source: /cvsroot/jameica/jameica.xmlrpc/src/de/willuhn/jameica/xmlrpc/server/Attic/HttpServiceImpl.java,v $
 * $Revision: 1.4 $
 * $Date: 2006/10/26 23:54:15 $
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
import org.apache.xmlrpc.webserver.WebServer;

import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.Settings;
import de.willuhn.jameica.xmlrpc.Plugin;
import de.willuhn.logging.Logger;

/**
 * @author willuhn
 */
public class HttpServiceImpl extends UnicastRemoteObject implements
    de.willuhn.jameica.xmlrpc.rmi.HttpService
{

  private WebServer server = null;
  
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
    return "HTTP Listener";
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
      Settings settings = Application.getPluginLoader().getPlugin(Plugin.class).getResources().getSettings();
      
      this.server = new WebServer(settings.getInt("listener.http.port",8888));
      
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
      Logger.error("unable to start http listener",e);
      throw new RemoteException("unable to start http listener",e);
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