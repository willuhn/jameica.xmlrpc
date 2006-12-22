/**********************************************************************
 * $Source: /cvsroot/jameica/jameica.xmlrpc/src/de/willuhn/jameica/xmlrpc/Settings.java,v $
 * $Revision: 1.5 $
 * $Date: 2006/12/22 13:49:58 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.xmlrpc;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

import de.willuhn.jameica.plugin.Manifest;
import de.willuhn.jameica.plugin.PluginLoader;
import de.willuhn.jameica.plugin.ServiceDescriptor;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.xmlrpc.rmi.XmlRpcService;
import de.willuhn.jameica.xmlrpc.server.XmlRpcServiceImpl;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Container fuer die Einstellungen.
 */
public class Settings
{
  /**
   * Die Einstellungen des Plugins.
   */
  public final static de.willuhn.jameica.system.Settings SETTINGS = Application.getPluginLoader().getPlugin(Plugin.class).getResources().getSettings();
  
  private static XmlRpcService[] services = null;
  
  /**
   * Liefert den TCP-Port fuer den XML-RPC-Server.
   * @return der TCP-Port.
   */
  public static int getPort()
  {
    return SETTINGS.getInt("listener.http.port",8888);
  }
  
  /**
   * Speichert den zu verwendenden TCP-Port.
   * @param port der Port.
   * @throws ApplicationException
   */
  public static void setPort(int port) throws ApplicationException
  {
    if (port == getPort())
    {
      // hat sich nicht geaendert
      return;
    }
    
    if (port < 1 || port > 65535)
      throw new ApplicationException(Application.getI18n().tr("TCP-Portnummer für XML-RPC ausserhalb des gültigen Bereichs von {0} bis {1}", new String[]{""+1,""+65535}));

    ServerSocket s = null;
    try
    {
      // Wir machen einen Test auf dem Port wenn es nicht der aktuelle ist
      Logger.info("testing TCP port " + port);
      s = new ServerSocket(port);
    }
    catch (BindException e)
    {
      throw new ApplicationException(Application.getI18n().tr("Die angegebene TCP-Portnummer für XML-RPC {0} ist bereits belegt",""+port));
    }
    catch (IOException ioe)
    {
      Logger.error("error while opening socket on port " + port);
      throw new ApplicationException(Application.getI18n().tr("Fehler beim Testen der TCP-Portnummer für XML-RPC {0}. Ist der Port bereits belegt?",""+port));
    }
    finally
    {
      if (s != null)
      {
        try
        {
          s.close();
        }
        catch (Exception e)
        {
          // ignore
        }
      }
    }
    SETTINGS.setAttribute("listener.http.port",port);
  }
  
  /**
   * Liefert die Adresse, an die der Server gebunden werden soll.
   * @return die Adresse, an die der Server gebunden werden soll oder <code>null</code> fuer alle.
   */
  public static InetAddress getAddress()
  {
    String s = SETTINGS.getString("listener.http.address",null);
    if (s == null)
      return null;
    try
    {
      return InetAddress.getByName(s);
    }
    catch (UnknownHostException e)
    {
      Logger.error("unable to resolve address " + s,e);
    }
    return null;
  }
  
  /**
   * Speichert die Adresse, an die der Server gebunden werden soll.
   * @param address die Adresse, an die der Server gebunden werden soll oder <code>null</code> fuer alle.
   */
  public static void setAddress(InetAddress address)
  {
    SETTINGS.setAttribute("listener.http.address",address == null ? null : address.getHostAddress());
  }
  
  /**
   * Liefert true, wenn die Kommunikation SSL-verschluesselt werden soll.
   * @return true, wenn SSL verwendet wird.
   */
  public static boolean getUseSSL()
  {
    return SETTINGS.getBoolean("listener.http.ssl",true);
  }
  
  /**
   * Legt fest, ob SSL verwendet werden soll.
   * @param ssl true, wenn SSL verwendet werden soll.
   */
  public static void setUseSSL(boolean ssl)
  {
    SETTINGS.setAttribute("listener.http.ssl",ssl);
  }
  
  /**
   * Liefert true, wenn das Jameica-Masterpasswort als HTTP-Authorisierung abgefragt werden soll.
   * @return true, wenn das Passwort abgefragt werden soll.
   */
  public static boolean getUseAuth()
  {
    return SETTINGS.getBoolean("listener.http.auth",true);
  }
  
  /**
   * Legt fest, ob das Jameica-Masterpasswort als HTTP-Authorisierung abgefragt werden soll.
   * @param auth true, wenn das Passwort abgefragt werden soll.
   */
  public static void setUseAuth(boolean auth)
  {
    SETTINGS.setAttribute("listener.http.auth",auth);
  }

  /**
   * Liefert die Liste aller potentiellen Services. Auch jene, welche nicht aktiv sind.
   * @return Liste aller XML-RPC-tauglichen Services.
   */
  public static XmlRpcService[] getServices()
  {
    if (services != null)
      return services;

    Logger.info("checking XML-RPC handlers");

    PluginLoader loader = Application.getPluginLoader();
    Iterator manifests  = loader.getInstalledManifests();

    Manifest self = Application.getPluginLoader().getManifest(Plugin.class);
    
    ArrayList l = new ArrayList();
    while (manifests.hasNext())
    {
      Manifest mf = (Manifest) manifests.next();
      
      ServiceDescriptor[] services = mf.getServices();
      
      Logger.info("  checking plugin " + mf.getName());
      for (int i=0;i<services.length;++i)
      {
        if ("listener.http".equals(services[i].getName()) && self.getPluginClass().equals(mf.getPluginClass()))
          continue; // Das sind wir selbst

        Logger.info("    checking service " + mf.getName() + "." + services[i].getName());
        try
        {
          l.add(new XmlRpcServiceImpl(mf,services[i]));
        }
        catch (RemoteException re)
        {
          Logger.error("unable to load service",re);
        }
      }
    }
    services = (XmlRpcService[]) l.toArray(new XmlRpcService[l.size()]);
    return services;
  }
}


/*********************************************************************
 * $Log: Settings.java,v $
 * Revision 1.5  2006/12/22 13:49:58  willuhn
 * @N server kann an interface gebunden werden
 *
 * Revision 1.4  2006/12/22 09:31:38  willuhn
 * @N bind address
 *
 * Revision 1.3  2006/10/31 17:44:20  willuhn
 * *** empty log message ***
 *
 * Revision 1.2  2006/10/31 17:06:26  willuhn
 * @N GUI to configure xml-rpc
 *
 * Revision 1.1  2006/10/31 01:43:08  willuhn
 * *** empty log message ***
 *
 **********************************************************************/