/**********************************************************************
 * $Source: /cvsroot/jameica/jameica.xmlrpc/src/de/willuhn/jameica/xmlrpc/server/Attic/LookupServiceImpl.java,v $
 * $Revision: 1.3 $
 * $Date: 2007/12/13 16:11:51 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn software & services
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.xmlrpc.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import de.willuhn.jameica.plugin.Manifest;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.xmlrpc.Plugin;
import de.willuhn.jameica.xmlrpc.rmi.LookupService;
import de.willuhn.jameica.xmlrpc.rmi.XmlRpcServiceDescriptor;
import de.willuhn.logging.Logger;
import de.willuhn.net.MulticastClient;

/**
 * Implementierung des Lookup-Services.
 */
public class LookupServiceImpl extends UnicastRemoteObject implements
    LookupService
{

  private Client client = null;

  /**
   * @throws RemoteException
   */
  public LookupServiceImpl() throws RemoteException
  {
    super();
  }

  /**
   * @see de.willuhn.datasource.Service#getName()
   */
  public String getName() throws RemoteException
  {
    return "Lookup-Service";
  }

  /**
   * @see de.willuhn.datasource.Service#isStartable()
   */
  public boolean isStartable() throws RemoteException
  {
    return !isStarted();
  }

  /**
   * @see de.willuhn.datasource.Service#isStarted()
   */
  public boolean isStarted() throws RemoteException
  {
    return this.client != null;
  }

  /**
   * @see de.willuhn.datasource.Service#start()
   */
  public void start() throws RemoteException
  {
    if (this.isStarted())
    {
      Logger.warn("service allready started, skipping request");
      return;
    }
    
    try
    {
      this.client = new Client();
    }
    catch (IOException e)
    {
      throw new RemoteException("unable to start lookup-service",e);
    }
  }

  /**
   * @see de.willuhn.datasource.Service#stop(boolean)
   */
  public void stop(boolean arg0) throws RemoteException
  {
    if (!this.isStarted())
    {
      Logger.warn("service not started, skipping request");
      return;
    }
    try
    {
      this.client.stop();
    }
    catch (IOException e)
    {
      throw new RemoteException("unable to stop lookup-service",e);
    }
    finally
    {
      this.client = null;
    }
      
  }
  
  /**
   * Client, der die Lookups beantwortet.
   */
  private class Client extends MulticastClient
  {
    /**
     * ct
     * @throws IOException
     */
    public Client() throws IOException
    {
      super();
    }

    /**
     * @see de.willuhn.net.MulticastClient#received(java.net.DatagramPacket)
     */
    public void received(DatagramPacket packet) throws IOException
    {
      InetAddress sender = packet.getAddress();
      InetAddress self   = InetAddress.getLocalHost();
      Logger.debug("got datagram packet from " + sender.getHostName());
      if (sender.equals(self))
        return; // ignore

      String s = new String(packet.getData());
      if (s == null)
      {
        Logger.debug("ignoring empty message");
        return; // nix erhalten
      }
      
      s = s.trim(); // Zeilenumbruch am Ende entfernen
      
      if (!s.equals("jameica.xmlrpc.queue"))
      {
        Logger.debug("ignoring message: " + s);
        return; // nix sinnvolles
      }
      
      Logger.info("got lookup request for queue url from " + sender.getCanonicalHostName());
      
      Manifest mf = Application.getPluginLoader().getManifest(Plugin.class);
      XmlRpcServiceDescriptor[] services = de.willuhn.jameica.xmlrpc.Settings.getServices();
      for (int i=0;i<services.length;++i)
      {
        String plugin = services[i].getPluginName();
        if (plugin == null || !plugin.equals(mf.getName()))
          continue;

        String name = services[i].getServiceName();
        if (name == null || !name.equals("queue"))
          continue;
        
        String url = services[i].getURL();
        if (url == null || url.length() == 0)
        {
          Logger.warn("no xml-rpc url defined for queue service");
          return;
        }
        Logger.info("sending url: " + url);
        send(url.getBytes());
        return;
      }
      Logger.warn("unable to find xml-rpc url for queue service");
    }
  }
}


/*********************************************************************
 * $Log: LookupServiceImpl.java,v $
 * Revision 1.3  2007/12/13 16:11:51  willuhn
 * @N Generischer Message-Queue-Service
 *
 **********************************************************************/