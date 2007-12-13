/**********************************************************************
 * $Source: /cvsroot/jameica/jameica.xmlrpc/src/de/willuhn/jameica/xmlrpc/server/XmlRpcServiceDescriptorImpl.java,v $
 * $Revision: 1.4 $
 * $Date: 2007/12/13 16:11:51 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.xmlrpc.server;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.Service;
import de.willuhn.jameica.plugin.AbstractPlugin;
import de.willuhn.jameica.plugin.Manifest;
import de.willuhn.jameica.plugin.PluginLoader;
import de.willuhn.jameica.plugin.ServiceDescriptor;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.xmlrpc.Settings;
import de.willuhn.jameica.xmlrpc.rmi.XmlRpcServiceDescriptor;

/**
 * Container fuer die Eigenschaften eines XML-RPC-Services.
 */
public class XmlRpcServiceDescriptorImpl extends UnicastRemoteObject implements XmlRpcServiceDescriptor
{
  private Manifest manifest         = null;
  private ServiceDescriptor service = null;
  
  /**
   * ct.
   * @param manifest
   * @param service
   * @throws RemoteException
   */
  public XmlRpcServiceDescriptorImpl(Manifest manifest, ServiceDescriptor service) throws RemoteException
  {
    super();
    this.manifest = manifest;
    this.service  = service;
  }
  
  /**
   * @see de.willuhn.jameica.xmlrpc.rmi.XmlRpcServiceDescriptor#isShared()
   */
  public boolean isShared() throws RemoteException
  {
    return Settings.SETTINGS.getBoolean(getID() + ".shared",false);
  }
  
  /**
   * @see de.willuhn.jameica.xmlrpc.rmi.XmlRpcServiceDescriptor#setShared(boolean)
   */
  public void setShared(boolean shared) throws RemoteException
  {
    Settings.SETTINGS.setAttribute(getID() + ".shared",shared);
  }
  
  /**
   * @see de.willuhn.jameica.xmlrpc.rmi.XmlRpcServiceDescriptor#getServiceName()
   */
  public String getServiceName() throws RemoteException
  {
    return this.service.getName();
  }
  
  /**
   * @see de.willuhn.jameica.xmlrpc.rmi.XmlRpcServiceDescriptor#getPluginName()
   */
  public String getPluginName() throws RemoteException
  {
    return this.manifest.getName();
  }
  
  /**
   * @see de.willuhn.jameica.xmlrpc.rmi.XmlRpcServiceDescriptor#getService()
   */
  public Service getService() throws RemoteException
  {
    PluginLoader loader = Application.getPluginLoader();
    AbstractPlugin plugin = loader.getPlugin(this.manifest.getPluginClass());
    try
    {
      return Application.getServiceFactory().lookup(plugin.getClass(),this.service.getName());
    }
    catch (RemoteException re)
    {
      throw re;
    }
    catch (Exception e)
    {
      throw new RemoteException("unable to load service",e);
    }
  }

  /**
   * @see de.willuhn.datasource.GenericObject#equals(de.willuhn.datasource.GenericObject)
   */
  public boolean equals(GenericObject arg0) throws RemoteException
  {
    if (arg0 == null || !(arg0 instanceof XmlRpcServiceDescriptor))
      return false;
    return this.getID().equals(arg0.getID());
  }

  /**
   * @see de.willuhn.datasource.GenericObject#getAttribute(java.lang.String)
   */
  public Object getAttribute(String arg0) throws RemoteException
  {
    if ("servicename".equals(arg0))
      return getServiceName();
    if ("pluginname".equals(arg0))
      return getPluginName();
    if ("shared".equals(arg0))
      return new Boolean(isShared());
    return null;
  }

  /**
   * @see de.willuhn.datasource.GenericObject#getAttributeNames()
   */
  public String[] getAttributeNames() throws RemoteException
  {
    return new String[]{"servicename","pluginname","shared"};
  }

  /**
   * @see de.willuhn.datasource.GenericObject#getID()
   */
  public String getID() throws RemoteException
  {
    if (Settings.getUseInterfaceNames())
      return service.getClassname();
    return manifest.getName() + "." + service.getName();
  }

  /**
   * @see de.willuhn.datasource.GenericObject#getPrimaryAttribute()
   */
  public String getPrimaryAttribute() throws RemoteException
  {
    return "servicename";
  }

  /**
   * @see de.willuhn.jameica.xmlrpc.rmi.XmlRpcServiceDescriptor#getURL()
   */
  public String getURL() throws RemoteException
  {
    try
    {
      InetAddress host = Settings.getAddress();
      String address = host == null ? Application.getCallback().getHostname() : host.getHostAddress();
      return "http" + (Settings.getUseSSL() ? "s" : "") + "://" + address + ":" + Settings.getPort() + "/xmlrpc/" + this.getID();
    }
    catch (Exception e)
    {
      throw new RemoteException(e.getMessage(),e);
    }
  }

}


/*********************************************************************
 * $Log: XmlRpcServiceDescriptorImpl.java,v $
 * Revision 1.4  2007/12/13 16:11:51  willuhn
 * @N Generischer Message-Queue-Service
 *
 * Revision 1.3  2007/10/18 22:13:14  willuhn
 * @N XML-RPC URL via Service-Descriptor abfragbar
 *
 * Revision 1.2  2007/06/13 14:50:10  willuhn
 * @N Als XML-RPC-Servicenamen koennen nun auch direkt die Interface-Namen verwendet werden. Das ermoeglicht die Verwendung von dynamischen Proxies auf Clientseite.
 *
 * Revision 1.1  2007/04/05 12:14:40  willuhn
 * @N Liste der Services im Handler statisch
 * @C XmlRpcService in XmlRpcServiceDescriptor umbenannt
 *
 * Revision 1.1  2006/10/31 17:06:26  willuhn
 * @N GUI to configure xml-rpc
 *
 **********************************************************************/