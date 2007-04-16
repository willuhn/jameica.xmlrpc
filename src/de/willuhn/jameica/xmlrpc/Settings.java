/**********************************************************************
 * $Source: /cvsroot/jameica/jameica.xmlrpc/src/de/willuhn/jameica/xmlrpc/Settings.java,v $
 * $Revision: 1.9 $
 * $Date: 2007/04/16 12:36:41 $
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
import java.util.ArrayList;
import java.util.List;

import de.willuhn.jameica.plugin.Manifest;
import de.willuhn.jameica.plugin.PluginLoader;
import de.willuhn.jameica.plugin.ServiceDescriptor;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.xmlrpc.rmi.XmlRpcServiceDescriptor;
import de.willuhn.jameica.xmlrpc.server.XmlRpcServiceDescriptorImpl;
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
  
  private static XmlRpcServiceDescriptor[] services = null;
  
  /**
   * Liefert den TCP-Port fuer den XML-RPC-Server.
   * @return der TCP-Port.
   */
  public static int getPort()
  {
    return SETTINGS.getInt("listener.http.port",8888);
  }
  
  /**
   * Liefert eine Liste von IPs oder IP-Bereichen, von denen aus der Zugriff erlaubt sein soll.
   * Standardmaessig koennen Clients von ueberall auf den XMLRPC-Dienst zugreifen, insofern
   * die in <code>listener.http.address</code> angegebene Adresse erreichbar ist.
   * Werden hier jedoch Adressen definiert, dann sind Verbindungen nur noch von
   * diesen Adressen aus moeglich. Verwenden Sie "*" als Wildcard.
   * Beispiele:
   * <ul>
   *   <li>192.168.1.100</li>
   *   <li>192.168.1.*</li>
   *   <li>172.16.*.*</li>
   * </ul>
   * @return Die Liste der erlaubten Adressen oder <code>null</code> wenn keine Einschraenkung
   * existiert.
   */
  public static String[] getAllowedClients()
  {
    String[] list = SETTINGS.getList("listener.http.allowedclients",null);
    if (list != null && list.length > 0)
      return list;
    return null;
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
  public static XmlRpcServiceDescriptor[] getServices()
  {
    if (services != null)
      return services;

    Logger.info("checking XML-RPC services");

    PluginLoader loader = Application.getPluginLoader();
    List manifests  = loader.getInstalledManifests();

    Manifest self = Application.getPluginLoader().getManifest(Plugin.class);
    
    ArrayList l = new ArrayList();
    for (int i=0;i<manifests.size();++i)
    {
      Manifest mf = (Manifest) manifests.get(i);
      
      ServiceDescriptor[] services = mf.getServices();
      
      Logger.info("  checking plugin " + mf.getName());
      if (services == null || services.length == 0)
        continue;
      for (int k=0;k<services.length;++k)
      {
        try
        {
          if ("listener.http".equals(services[k].getName()) && self.getPluginClass().equals(mf.getPluginClass()))
            continue; // Das sind wir selbst

          Logger.info("    checking service " + mf.getName() + "." + services[k].getName());

          l.add(new XmlRpcServiceDescriptorImpl(mf,services[k]));
        }
        catch (Exception e)
        {
          Logger.error("unable to load service",e);
        }
      }
    }
    services = (XmlRpcServiceDescriptor[]) l.toArray(new XmlRpcServiceDescriptor[l.size()]);
    return services;
  }
}


/*********************************************************************
 * $Log: Settings.java,v $
 * Revision 1.9  2007/04/16 12:36:41  willuhn
 * @C getInstalledPlugins und getInstalledManifests liefern nun eine Liste vom Typ "List" statt "Iterator"
 *
 * Revision 1.8  2007/04/05 12:14:40  willuhn
 * @N Liste der Services im Handler statisch
 * @C XmlRpcService in XmlRpcServiceDescriptor umbenannt
 *
 * Revision 1.7  2007/04/05 10:42:33  willuhn
 * @N Registrieren der XML/RPC-Handler erst nachdem alle Services geladen wurden (mittels SystemMessage). Somit koennen bereits beim Initialisieren die XMLRPC-URLs im Log ausgegeben werden und nicht erst beim ersten Request.
 *
 * Revision 1.6  2007/01/24 15:52:24  willuhn
 * @N Client access restrictions
 *
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