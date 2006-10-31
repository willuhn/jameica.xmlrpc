import java.net.URL;
import java.rmi.RemoteException;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**********************************************************************
 * $Source: /cvsroot/jameica/jameica.xmlrpc/src/EchoTest.java,v $
 * $Revision: 1.4 $
 * $Date: 2006/10/31 17:06:26 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/

public class EchoTest
{

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception
  {
    XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
    config.setEnabledForExtensions(true);
    config.setServerURL(new URL("http://127.0.0.1:8888/xmlrpc"));
    XmlRpcClient client = new XmlRpcClient();
    client.setConfig(config);
    
    Object msg = (Object) client.execute("hibiscus.xmlrpc.konto.list",(Object[]) null);
    System.out.println(msg);
  }

}


/*********************************************************************
 * $Log: EchoTest.java,v $
 * Revision 1.4  2006/10/31 17:06:26  willuhn
 * @N GUI to configure xml-rpc
 *
 * Revision 1.3  2006/10/31 01:43:08  willuhn
 * *** empty log message ***
 *
 * Revision 1.2  2006/10/28 01:05:37  willuhn
 * @N add bindings on demand
 *
 * Revision 1.1  2006/10/26 23:54:15  willuhn
 * @N added needed jars
 * @N first working version
 *
 **********************************************************************/