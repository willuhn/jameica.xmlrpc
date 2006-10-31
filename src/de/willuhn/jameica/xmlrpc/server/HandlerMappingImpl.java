/**********************************************************************
 * $Source: /cvsroot/jameica/jameica.xmlrpc/src/de/willuhn/jameica/xmlrpc/server/HandlerMappingImpl.java,v $
 * $Revision: 1.5 $
 * $Date: 2006/10/31 01:43:07 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.xmlrpc.server;

import java.util.Iterator;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.server.AbstractReflectiveHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcNoSuchHandlerException;

import de.willuhn.datasource.Service;
import de.willuhn.jameica.plugin.AbstractPlugin;
import de.willuhn.jameica.plugin.Manifest;
import de.willuhn.jameica.plugin.PluginLoader;
import de.willuhn.jameica.plugin.ServiceDescriptor;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.Settings;
import de.willuhn.jameica.xmlrpc.Plugin;
import de.willuhn.logging.Logger;

/**
 * Implementiert das Mapping von XML-RPC-Namen auf Klassen.
 * @author willuhn
 */
public class HandlerMappingImpl extends AbstractReflectiveHandlerMapping implements XmlRpcHandlerMapping
{
  private boolean initialized = false;

  /**
   * Initialisiert die Handler. Allerdings nicht sofort beim Systemstart sondern erst beim ersten Zugriff.
   */
  private synchronized void init()
  {
    if (initialized)
      return;
    
    try
    {
      Logger.info("registering XML-RPC handlers");
      
      PluginLoader loader = Application.getPluginLoader();
      AbstractPlugin self = loader.getPlugin(Plugin.class);
      Iterator manifests  = loader.getInstalledManifests();

      Settings settings   = self.getResources().getSettings();

      while (manifests.hasNext())
      {
        Manifest mf                  = (Manifest) manifests.next();
        AbstractPlugin plugin        = loader.getPlugin(mf.getPluginClass());
        ServiceDescriptor[] services = mf.getServices();

        Logger.info("  checking plugin " + mf.getName());

        for (int i=0;i<services.length;++i)
        {
          String name = mf.getName() + "." + services[i].getName();

          Logger.info("    checking service " + name);
          try
          {
            if (!settings.getBoolean(name + ".shared",false))
              continue;

            Service s = Application.getServiceFactory().lookup(plugin.getClass(),services[i].getName());

            Logger.info("    register service " + name + ", class: " + services[i].getClassname());
            registerPublicMethods(name, s.getClass());

            Logger.info("    service successfully registered. URL: http://" + Application.getCallback().getHostname() + ":" + de.willuhn.jameica.xmlrpc.Settings.getPort() + "/xmlrpc/" + name);
          }
          catch (Exception e)
          {
            Logger.error("unable to register service",e);
          }
        }
      }
    }
    finally
    {
      initialized = true;
    }
  }

  /**
   * @see org.apache.xmlrpc.server.AbstractReflectiveHandlerMapping#getHandler(java.lang.String)
   */
  public XmlRpcHandler getHandler(String pHandlerName) throws XmlRpcNoSuchHandlerException, XmlRpcException
  {
    init();
    return super.getHandler(pHandlerName);
  }
  
  
}


/*********************************************************************
 * $Log: HandlerMappingImpl.java,v $
 * Revision 1.5  2006/10/31 01:43:07  willuhn
 * *** empty log message ***
 *
 * Revision 1.4  2006/10/28 01:05:37  willuhn
 * @N add bindings on demand
 *
 * Revision 1.3  2006/10/26 23:54:15  willuhn
 * @N added needed jars
 * @N first working version
 *
 * Revision 1.2  2006/10/23 23:07:28  willuhn
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/19 16:08:30  willuhn
 * *** empty log message ***
 *
 *********************************************************************/