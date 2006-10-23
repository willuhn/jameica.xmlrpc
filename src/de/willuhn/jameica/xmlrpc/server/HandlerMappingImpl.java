/**********************************************************************
 * $Source: /cvsroot/jameica/jameica.xmlrpc/src/de/willuhn/jameica/xmlrpc/server/HandlerMappingImpl.java,v $
 * $Revision: 1.2 $
 * $Date: 2006/10/23 23:07:28 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.xmlrpc.server;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.server.XmlRpcHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcNoSuchHandlerException;

/**
 * Implementiert das Mapping von XML-RPC-Namen auf Klassen.
 * @author willuhn
 */
public class HandlerMappingImpl implements XmlRpcHandlerMapping
{

  /**
   * @see org.apache.xmlrpc.server.XmlRpcHandlerMapping#getHandler(java.lang.String)
   */
  public XmlRpcHandler getHandler(String arg0)
      throws XmlRpcNoSuchHandlerException, XmlRpcException
  {
    return null;
  }

}


/*********************************************************************
 * $Log: HandlerMappingImpl.java,v $
 * Revision 1.2  2006/10/23 23:07:28  willuhn
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/19 16:08:30  willuhn
 * *** empty log message ***
 *
 *********************************************************************/