/**********************************************************************
 * $Source: /cvsroot/jameica/jameica.xmlrpc/src/de/willuhn/jameica/xmlrpc/ext/jameica/SettingsView.java,v $
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

package de.willuhn.jameica.xmlrpc.ext.jameica;

import de.willuhn.jameica.gui.extension.Extendable;
import de.willuhn.jameica.gui.extension.Extension;
import de.willuhn.jameica.gui.internal.views.Settings;
import de.willuhn.jameica.gui.util.TabGroup;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.xmlrpc.Plugin;
import de.willuhn.logging.Logger;
import de.willuhn.util.I18N;

/**
 * Erweitert die View mit dem System-Einstellungen um die XML-RPC-Optionen.
 * @author willuhn
 */
public class SettingsView implements Extension
{

  /**
   * @see de.willuhn.jameica.gui.extension.Extension#extend(de.willuhn.jameica.gui.extension.Extendable)
   */
  public void extend(Extendable extendable)
  {
    if (extendable == null || !(extendable instanceof Settings))
      return;

    I18N i18n = Application.getPluginLoader().getPlugin(Plugin.class).getResources().getI18N();
    Settings settings = (Settings) extendable;

    try
    {
      TabGroup tab = new TabGroup(settings.getTabFolder(),i18n.tr("XML-RPC"));
    }
    catch (Exception e)
    {
      Logger.error("unable to extend settings",e);
      Application.getMessagingFactory().sendMessage(new StatusBarMessage(i18n.tr("Fehler beim Anzeigen der XML-RPC Einstellungen"), StatusBarMessage.TYPE_ERROR));
    }
    
  }
  
  

}


/*********************************************************************
 * $Log: SettingsView.java,v $
 * Revision 1.2  2006/10/19 16:08:30  willuhn
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/19 15:27:01  willuhn
 * @N initial checkin
 *
 *********************************************************************/