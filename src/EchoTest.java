import java.net.URL;
import java.rmi.RemoteException;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**********************************************************************
 * $Source: /cvsroot/jameica/jameica.xmlrpc/src/EchoTest.java,v $
 * $Revision: 1.3 $
 * $Date: 2006/10/31 01:43:08 $
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
    config.setServerURL(new URL("http://127.0.0.1:8888/xmlrpc"));
    XmlRpcClient client = new XmlRpcClient();
    client.setConfig(config);
    
    Object msg = (Object) client.execute("hibiscus.xmlrpc.konto.create",new String[]{"123456789","12345678","RPC-Konto","neuer Inhaber"});
    System.out.println(msg);
    Object[] konten = (Object[]) client.execute("hibiscus.xmlrpc.konto.getList",new Object[0]);
    for (int i=0;i<konten.length;++i)
    {
      System.out.println(konten[i]);
    }
  }

}


/*********************************************************************
 * $Log: EchoTest.java,v $
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