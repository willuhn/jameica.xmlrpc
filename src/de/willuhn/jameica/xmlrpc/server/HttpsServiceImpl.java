/**********************************************************************
 * $Source: /cvsroot/jameica/jameica.xmlrpc/src/de/willuhn/jameica/xmlrpc/server/Attic/HttpsServiceImpl.java,v $
 * $Revision: 1.2 $
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

/**
 * @author willuhn
 */
public class HttpsServiceImpl extends HttpServiceImpl implements
    de.willuhn.jameica.xmlrpc.rmi.HttpsService
{

  /**
   * @throws RemoteException
   */
  public HttpsServiceImpl() throws RemoteException
  {
    super();
  }

  /**
   * @see de.willuhn.datasource.Service#getName()
   */
  public String getName() throws RemoteException
  {
    return "HTTPS Listener";
  }
}


/*********************************************************************
 * $Log: HttpsServiceImpl.java,v $
 * Revision 1.2  2006/10/19 16:08:30  willuhn
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/19 15:27:01  willuhn
 * @N initial checkin
 *
 *********************************************************************/