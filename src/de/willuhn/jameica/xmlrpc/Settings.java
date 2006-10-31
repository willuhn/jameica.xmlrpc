/**********************************************************************
 * $Source: /cvsroot/jameica/jameica.xmlrpc/src/de/willuhn/jameica/xmlrpc/Settings.java,v $
 * $Revision: 1.1 $
 * $Date: 2006/10/31 01:43:08 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.xmlrpc;

import de.willuhn.jameica.system.Application;

/**
 * Container fuer die Einstellungen.
 */
public class Settings
{
  private final static de.willuhn.jameica.system.Settings SETTINGS = Application.getPluginLoader().getPlugin(Plugin.class).getResources().getSettings();

  /**
   * Liefert den TCP-Port fuer den XML-RPC-Server.
   * @return der TCP-Port.
   */
  public static int getPort()
  {
    return SETTINGS.getInt("listener.http.port",8888);
  }
}


/*********************************************************************
 * $Log: Settings.java,v $
 * Revision 1.1  2006/10/31 01:43:08  willuhn
 * *** empty log message ***
 *
 **********************************************************************/