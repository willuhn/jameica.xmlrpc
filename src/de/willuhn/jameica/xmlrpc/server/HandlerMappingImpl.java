/**********************************************************************
 * $Source: /cvsroot/jameica/jameica.xmlrpc/src/de/willuhn/jameica/xmlrpc/server/HandlerMappingImpl.java,v $
 * $Revision: 1.19 $
 * $Date: 2008/04/04 00:17:13 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.xmlrpc.server;

import java.util.ArrayList;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.server.AbstractReflectiveHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcNoSuchHandlerException;

import de.willuhn.jameica.messaging.LookupService;
import de.willuhn.jameica.xmlrpc.Settings;
import de.willuhn.jameica.xmlrpc.rmi.XmlRpcServiceDescriptor;
import de.willuhn.logging.Logger;

/**
 * Implementiert das Mapping von XML-RPC-Namen auf Klassen.
 * @author willuhn
 */
public class HandlerMappingImpl extends AbstractReflectiveHandlerMapping implements XmlRpcHandlerMapping
{
  // Statisch, weil wir die Liste nur einmal ermitteln wollen.
  private static ArrayList services  = null;

  /**
   * Initialisiert die Handler.
   * @throws XmlRpcException
   */
  private synchronized void init() throws XmlRpcException
  {
    if (services != null)
      return;
    
    try
    {
      services = new ArrayList();
      this.setRequestProcessorFactoryFactory(new MyRequestProcessorFactoryFactory());

      XmlRpcServiceDescriptor[] all = Settings.getServices();
      for (int i=0;i<all.length;++i)
      {
        try
        {
          XmlRpcServiceDescriptor service = all[i];
          if (!service.isShared())
            continue;

          registerPublicMethods(service.getID(),service.getService().getClass());
          services.add(service);
          LookupService.register("xmlrpc:" + service.getID(),service.getURL());
        }
        catch (Exception e)
        {
          Logger.error("unable to register service, skipping",e);
        }
      }
    }
    catch (Exception e)
    {
      throw new XmlRpcException("unable to register services",e);
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
 * Revision 1.19  2008/04/04 00:17:13  willuhn
 * @N Apache XML-RPC von 3.0 auf 3.1 aktualisiert
 * @N jameica.xmlrpc ist jetzt von jameica.webadmin abhaengig
 * @N jameica.xmlrpc nutzt jetzt keinen eigenen embedded Webserver mehr sondern den Jetty von jameica.webadmin mittels Servlet. Damit kann nun XML-RPC ueber den gleichen TCP-Port (8080) gemacht werden, wo auch die restlichen Webfrontends laufen -> spart einen TCP-Port und skaliert besser wegen Multi-Threading-Support in Jetty
 *
 * Revision 1.18  2007/12/14 13:41:01  willuhn
 * *** empty log message ***
 *
 * Revision 1.17  2007/12/14 13:28:40  willuhn
 * @C Lookup-Service nach Jameica verschoben
 *
 * Revision 1.16  2007/11/16 18:34:06  willuhn
 * @D javadoc fixed
 * @R removed unused methods/deprecated methods
 *
 * Revision 1.15  2007/10/18 22:13:14  willuhn
 * @N XML-RPC URL via Service-Descriptor abfragbar
 *
 * Revision 1.14  2007/04/05 12:14:40  willuhn
 * @N Liste der Services im Handler statisch
 * @C XmlRpcService in XmlRpcServiceDescriptor umbenannt
 *
 * Revision 1.13  2007/04/05 11:12:56  willuhn
 * *** empty log message ***
 *
 * Revision 1.12  2007/04/05 10:42:32  willuhn
 * @N Registrieren der XML/RPC-Handler erst nachdem alle Services geladen wurden (mittels SystemMessage). Somit koennen bereits beim Initialisieren die XMLRPC-URLs im Log ausgegeben werden und nicht erst beim ersten Request.
 *
 * Revision 1.11  2007/03/07 17:05:09  willuhn
 * @B Bei Fehler eines Services nicht gleich die komplette Registrierung abbrechen
 *
 * Revision 1.10  2007/02/15 11:04:25  willuhn
 * @D
 *
 * Revision 1.9  2007/02/15 11:03:58  willuhn
 * @B Services wurden bei jedem Request neu instanziiert
 *
 * Revision 1.8  2007/02/13 16:00:15  willuhn
 * *** empty log message ***
 *
 * Revision 1.7  2006/12/22 16:14:07  willuhn
 * @N Ausgabe der IP, an die der Service gebunden wurde
 *
 * Revision 1.6  2006/10/31 17:06:26  willuhn
 * @N GUI to configure xml-rpc
 *
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