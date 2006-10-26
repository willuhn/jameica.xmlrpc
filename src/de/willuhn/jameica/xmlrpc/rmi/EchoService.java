/**********************************************************************
 * $Source: /cvsroot/jameica/jameica.xmlrpc/src/de/willuhn/jameica/xmlrpc/rmi/EchoService.java,v $
 * $Revision: 1.1 $
 * $Date: 2006/10/26 23:54:15 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.xmlrpc.rmi;

import java.rmi.RemoteException;

/**
 * Beispiel-Service, der ein Echo macht.
 */
public interface EchoService extends XmlRpcService
{
  /**
   * Liefert den uebergebenen Text als Echo zurueck.
   * @param text der Text.
   * @return der Text zurueck.
   * @throws RemoteException
   */
  public String echo(String text) throws RemoteException;

}


/*********************************************************************
 * $Log: EchoService.java,v $
 * Revision 1.1  2006/10/26 23:54:15  willuhn
 * @N added needed jars
 * @N first working version
 *
 **********************************************************************/