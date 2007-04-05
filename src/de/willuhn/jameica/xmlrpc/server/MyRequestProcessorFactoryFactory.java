/**********************************************************************
 * $Source: /cvsroot/jameica/jameica.xmlrpc/src/de/willuhn/jameica/xmlrpc/server/MyRequestProcessorFactoryFactory.java,v $
 * $Revision: 1.2 $
 * $Date: 2007/04/05 12:14:40 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn software & services
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.xmlrpc.server;

import java.rmi.RemoteException;
import java.util.Hashtable;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.server.RequestProcessorFactoryFactory.RequestSpecificProcessorFactoryFactory;

import de.willuhn.jameica.xmlrpc.Settings;
import de.willuhn.jameica.xmlrpc.rmi.XmlRpcServiceDescriptor;
import de.willuhn.logging.Logger;

/**
 * Ueberschrieben, damit der Service nicht bei jedem Request neu instanziiert wird.
 */
public class MyRequestProcessorFactoryFactory extends RequestSpecificProcessorFactoryFactory
{
  private final static Hashtable serviceCache = new Hashtable();

  /**
   * Liefert die Instanz des Services.
   * @see org.apache.xmlrpc.server.RequestProcessorFactoryFactory$RequestSpecificProcessorFactoryFactory#getRequestProcessor(java.lang.Class, org.apache.xmlrpc.XmlRpcRequest)
   */
  protected Object getRequestProcessor(Class pClass, XmlRpcRequest pRequest) throws XmlRpcException
  {
    String method = pRequest.getMethodName();
    
    // "method ist der vollqualifizierte Name bestehend aus
    // pluginname.servicename.methodname
    // Um den Service zu finden, muessen wir den methodname
    // abschneiden
    
    if (method == null || method.indexOf(".") == -1)
      throw new XmlRpcException("no xmlrpc method given or method name invalid: " + method);
    
    method = method.substring(0,method.lastIndexOf("."));
    
    Object service = serviceCache.get(method);
    if (service != null)
      return service;
    
    XmlRpcServiceDescriptor[] services = Settings.getServices();
    for (int i=0;i<services.length;++i)
    {
      XmlRpcServiceDescriptor s = services[i];
      try
      {
        if (!s.isShared())
          continue;
        
        if (s.getID().equals(method))
        {
          // gefunden
          service = s.getService();
          serviceCache.put(method,service);
          return service;
        }
      }
      catch (RemoteException re)
      {
        Logger.error("error while checking xmlrpc service [method: " + method + "]",re);
      }
    }
    
    //return super.getRequestProcessor(pClass, pRequest);
    throw new XmlRpcException("no xmlrpc service found for method: " + method);
  }

}


/*********************************************************************
 * $Log: MyRequestProcessorFactoryFactory.java,v $
 * Revision 1.2  2007/04/05 12:14:40  willuhn
 * @N Liste der Services im Handler statisch
 * @C XmlRpcService in XmlRpcServiceDescriptor umbenannt
 *
 * Revision 1.1  2007/02/15 11:03:58  willuhn
 * @B Services wurden bei jedem Request neu instanziiert
 *
 **********************************************************************/