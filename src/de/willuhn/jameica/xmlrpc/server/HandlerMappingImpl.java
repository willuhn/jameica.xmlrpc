/**********************************************************************
 * $Source: /cvsroot/jameica/jameica.xmlrpc/src/de/willuhn/jameica/xmlrpc/server/HandlerMappingImpl.java,v $
 * $Revision: 1.3 $
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

import java.util.Iterator;

import org.apache.xmlrpc.server.AbstractReflectiveHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcHandlerMapping;

import de.willuhn.datasource.Service;
import de.willuhn.jameica.plugin.AbstractPlugin;
import de.willuhn.jameica.plugin.Manifest;
import de.willuhn.jameica.plugin.ServiceDescriptor;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.Settings;
import de.willuhn.jameica.xmlrpc.Plugin;
import de.willuhn.jameica.xmlrpc.rmi.XmlRpcService;
import de.willuhn.logging.Logger;

/**
 * Implementiert das Mapping von XML-RPC-Namen auf Klassen.
 * @author willuhn
 */
public class HandlerMappingImpl extends AbstractReflectiveHandlerMapping implements XmlRpcHandlerMapping
{
  /**
   * ct.
   */
  public HandlerMappingImpl()
  {
    Logger.info("registering XML-RPC handlers");
    
    Settings settings = Application.getPluginLoader().getPlugin(Plugin.class).getResources().getSettings();
    
    Iterator manifests = Application.getPluginLoader().getInstalledManifests();
    while (manifests.hasNext())
    {
      Manifest mf = (Manifest) manifests.next();
      Logger.info("  checking plugin " + mf.getName());
      
      AbstractPlugin plugin = Application.getPluginLoader().getPlugin(mf.getPluginClass());

      ServiceDescriptor[] services = mf.getServices();
      for (int i=0;i<services.length;++i)
      {
        ServiceDescriptor service = services[i];
        Logger.info("    checking service " + service.getName() + "[" + service.getClassname()+ "]");
        try
        {
          if (!settings.getBoolean(service.getClassname() + ".shared",true))
            continue;

          Service s = Application.getServiceFactory().lookup(plugin.getClass(),service.getName());

          if (!(s instanceof XmlRpcService))
            continue;

          Logger.info("    register class " + service.getClassname());

          // Wir registrieren ihn ueber sein Interface
          registerPublicMethods(service.getClassname(), s.getClass());
          Logger.info("    service successfully registered. URL: http://<server>/" + service.getClassname());
        }
        catch (Exception e)
        {
          Logger.error("unable to register service",e);
        }
      }
    }
  }
}


/*********************************************************************
 * $Log: HandlerMappingImpl.java,v $
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