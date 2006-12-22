/**********************************************************************
 * $Source: /cvsroot/jameica/jameica.xmlrpc/src/de/willuhn/jameica/xmlrpc/ext/jameica/SettingsView.java,v $
 * $Revision: 1.6 $
 * $Date: 2006/12/22 09:31:38 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.xmlrpc.ext.jameica;

import java.rmi.RemoteException;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;

import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.Service;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.extension.Extendable;
import de.willuhn.jameica.gui.extension.Extension;
import de.willuhn.jameica.gui.formatter.TableFormatter;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.IntegerInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.internal.views.Settings;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.gui.util.TabGroup;
import de.willuhn.jameica.messaging.Message;
import de.willuhn.jameica.messaging.MessageConsumer;
import de.willuhn.jameica.messaging.SettingsChangedMessage;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.xmlrpc.Plugin;
import de.willuhn.jameica.xmlrpc.rmi.XmlRpcService;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.I18N;

/**
 * Erweitert die View mit dem System-Einstellungen um die XML-RPC-Optionen.
 * @author willuhn
 */
public class SettingsView implements Extension
{
  private IntegerInput port   = null;
  private CheckboxInput ssl   = null;
  private CheckboxInput auth  = null;
  private TablePart services  = null;
  private SelectInput address = null;

  private I18N i18n = null;
  private MessageConsumer consumer = null;
  
  /**
   * ct.
   */
  public SettingsView()
  {
    this.i18n = Application.getPluginLoader().getPlugin(Plugin.class).getResources().getI18N();
  }
  /**
   * @see de.willuhn.jameica.gui.extension.Extension#extend(de.willuhn.jameica.gui.extension.Extendable)
   */
  public void extend(Extendable extendable)
  {
    if (extendable == null || !(extendable instanceof Settings))
      return;

    Settings settings = (Settings) extendable;
    
    this.consumer = new MessageConsumer() {
    
      /**
       * @see de.willuhn.jameica.messaging.MessageConsumer#handleMessage(de.willuhn.jameica.messaging.Message)
       */
      public void handleMessage(Message message) throws Exception
      {
        handleStore();
      }
    
      /**
       * @see de.willuhn.jameica.messaging.MessageConsumer#getExpectedMessageTypes()
       */
      public Class[] getExpectedMessageTypes()
      {
        return new Class[]{SettingsChangedMessage.class};
      }
    
      /**
       * @see de.willuhn.jameica.messaging.MessageConsumer#autoRegister()
       */
      public boolean autoRegister()
      {
        return false;
      }
    };
    Application.getMessagingFactory().registerMessageConsumer(this.consumer);

    try
    {
      TabGroup tab = new TabGroup(settings.getTabFolder(),i18n.tr("XML-RPC"));
      
      // Da wir keine echte View sind, haben wir auch kein unbind zum Aufraeumen.
      // Damit wir unsere GUI-Elemente aber trotzdem disposen koennen, registrieren
      // wir einen Dispose-Listener an der Tabgroup
      tab.getComposite().addDisposeListener(new DisposeListener() {
      
        public void widgetDisposed(DisposeEvent e)
        {
          port     = null;
          ssl      = null;
          auth     = null;
          services = null;
          Application.getMessagingFactory().unRegisterMessageConsumer(consumer);
        }
      
      });
      tab.addLabelPair(i18n.tr("TCP-Port"), getPort());
      tab.addCheckbox(getUseSSL(), i18n.tr("HTTP-Kommunikation verschlüsseln (HTTPS)"));
      tab.addCheckbox(getUseAuth(), i18n.tr("Benutzerauthentifizierung mittels Jameica Master-Passwort"));
      
      tab.addHeadline(i18n.tr("Freigegebene Services"));
      tab.addPart(getServices());
    }
    catch (Exception e)
    {
      Logger.error("unable to extend settings",e);
      Application.getMessagingFactory().sendMessage(new StatusBarMessage(i18n.tr("Fehler beim Anzeigen der XML-RPC Einstellungen"), StatusBarMessage.TYPE_ERROR));
    }
    
  }
  
  /**
   * Wird beim Speichern aufgerufen.
   * @throws ApplicationException
   */
  private void handleStore() throws ApplicationException
  {
    Integer in = (Integer) getPort().getValue();
    if (in == null)
      throw new ApplicationException(i18n.tr("Bitte geben Sie eine TCP-Portnummer für XML-RPC ein"));
    de.willuhn.jameica.xmlrpc.Settings.setPort(in.intValue());
    de.willuhn.jameica.xmlrpc.Settings.setUseAuth(((Boolean) getUseAuth().getValue()).booleanValue());
    de.willuhn.jameica.xmlrpc.Settings.setUseSSL(((Boolean) getUseSSL().getValue()).booleanValue());
    
    // Jetzt noch die Freigaben speichern
    try
    {
      GenericIterator selected = getServices().getItems();
      XmlRpcService[] all = de.willuhn.jameica.xmlrpc.Settings.getServices();
      for (int i=0;i<all.length;++i)
      {
        all[i].setShared(selected.contains(all[i]) != null);
      }
    }
    catch (RemoteException re)
    {
      Logger.error("unable to apply service settings",re);
      throw new ApplicationException(i18n.tr("Fehler beim Übernehmen der freigegebenen Services"));
    }
    
    try
    {
      Logger.info("restart http listener");
      Service listener = Application.getServiceFactory().lookup(Plugin.class,"listener.http");
      listener.stop(true);
      listener.start();
    }
    catch (Exception e)
    {
      Logger.error("unable to restart listener",e);
      throw new ApplicationException(i18n.tr("Fehler beim Neustart des Dienstes, bitte starten Sie Jameica neu"));
    }
  }

  /**
   * Liefert die Checkbox zur Aktivierung von SSL.
   * @return die Checkbox.
   */
  private CheckboxInput getUseSSL()
  {
    if (this.ssl != null)
      return this.ssl;
    
    this.ssl = new CheckboxInput(de.willuhn.jameica.xmlrpc.Settings.getUseSSL());
    this.ssl.addListener(new AuthListener());
    return this.ssl;
  }
  
  /**
   * Liefert die Checkbox zur Aktivierung von Authentifizierung.
   * @return die Checkbox.
   */
  private CheckboxInput getUseAuth()
  {
    if (this.auth != null)
      return this.auth;
    
    this.auth = new CheckboxInput(de.willuhn.jameica.xmlrpc.Settings.getUseAuth());
    this.auth.addListener(new AuthListener());
    return this.auth;
  }
  
  /**
   * Liefert den zu verwendenden TCP-Port.
   * @return der TCP-Port.
   */
  private IntegerInput getPort()
  {
    if (this.port != null)
      return this.port;
    this.port = new IntegerInput(de.willuhn.jameica.xmlrpc.Settings.getPort());
    return this.port;
  }
  
  /**
   * Liefert die Liste der Services.
   * @return Liste der Services.
   * @throw RemoteException
   */
  private TablePart getServices() throws RemoteException
  {
    if (this.services != null)
      return this.services;
    
    XmlRpcService[] services = de.willuhn.jameica.xmlrpc.Settings.getServices();
    this.services = new TablePart(PseudoIterator.fromArray(services),null);
    this.services.setCheckable(true);
    this.services.setMulti(false);
    this.services.setRememberColWidths(true);
    this.services.setRememberOrder(true);
    this.services.setSummary(false);
    this.services.addColumn(i18n.tr("Plugin"),"pluginname");
    this.services.addColumn(i18n.tr("Service"),"servicename");
    this.services.setFormatter(new TableFormatter() {
    
      public void format(TableItem item)
      {
        if (item == null)
          return;
        XmlRpcService service = (XmlRpcService) item.getData();
        try
        {
          item.setChecked(service.isShared());
        }
        catch (RemoteException re)
        {
          Logger.error("unable to enable service",re);
          try
          {
            Application.getMessagingFactory().sendMessage(new StatusBarMessage(i18n.tr("Fehler beim Aktivieren des Services {0}", service.getServiceName()), StatusBarMessage.TYPE_ERROR));
          }
          catch (RemoteException re2)
          {
            // useless
            Logger.error("unable to enable service",re);
          }
        }
      }
    });
    
    return this.services;
  }

  /**
   * Listener zum Pruefen, dass Authentifizierung nur zusammen mit SSL aktiv ist.
   */
  private class AuthListener implements Listener
  {
    /**
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    public void handleEvent(Event event)
    {
      // Wir pruefen, ob SSL aktiv ist, damit das Passwort sicher uebertragen wird
      boolean a = ((Boolean) getUseAuth().getValue()).booleanValue();
      boolean s = ((Boolean) getUseSSL().getValue()).booleanValue();
      if (a && !s)
        GUI.getView().setErrorText(i18n.tr("Benutzerauthentifizierung sollte nur zusammen mit Verschlüsselung aktiviert werden"));
      else
        GUI.getView().setSuccessText("");
    }
  }
}


/*********************************************************************
 * $Log: SettingsView.java,v $
 * Revision 1.6  2006/12/22 09:31:38  willuhn
 * @N bind address
 *
 * Revision 1.5  2006/10/31 23:56:44  willuhn
 * *** empty log message ***
 *
 * Revision 1.4  2006/10/31 17:44:20  willuhn
 * *** empty log message ***
 *
 * Revision 1.3  2006/10/31 17:06:26  willuhn
 * @N GUI to configure xml-rpc
 *
 * Revision 1.2  2006/10/19 16:08:30  willuhn
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/19 15:27:01  willuhn
 * @N initial checkin
 *
 *********************************************************************/