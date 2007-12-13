/**********************************************************************
 * $Source: /cvsroot/jameica/jameica.xmlrpc/src/de/willuhn/jameica/xmlrpc/rmi/Attic/QueueService.java,v $
 * $Revision: 1.1 $
 * $Date: 2007/12/13 16:11:51 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn software & services
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.xmlrpc.rmi;

import java.rmi.RemoteException;

import de.willuhn.datasource.Service;

/**
 * Ueber diesen Service koennen Nachrichten asynchron ausgetauscht werden.
 * Jameica-Instanz A kann Nachrichten hier abliefern, Instanz B kann sie
 * irgendwann abrufen.
 */
public interface QueueService extends Service
{
  /**
   * Uebergibt eine Nachricht an die Queue.
   * @param channel Name des Channels.
   * @param recipient Name des Empfaengers.
   * @param data die Nutzdaten.
   * @return true, wenn die Nachricht gespeichert werden konnte.
   * Ist ein Zugestaendnis an XML-RPC, da dort ein Rueckgabe-Wert Pflicht ist.
   * @throws RemoteException
   */
  public boolean put(String channel, String recipient, byte[] data) throws RemoteException;
  
  /**
   * Ruft die naechste vorliegende Nachricht ab.
   * @param channel Channel.
   * @param recipient Empfaenger.
   * @return die naechste Nachricht oder <code>null</code> wenn keine weiteren Nachrichten vorliegen.
   * @throws RemoteException
   */
  public byte[] get(String channel, String recipient) throws RemoteException;
}


/*********************************************************************
 * $Log: QueueService.java,v $
 * Revision 1.1  2007/12/13 16:11:51  willuhn
 * @N Generischer Message-Queue-Service
 *
 **********************************************************************/