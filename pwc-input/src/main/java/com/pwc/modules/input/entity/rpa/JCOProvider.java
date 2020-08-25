package com.pwc.modules.input.entity.rpa;

import com.sap.conn.jco.ext.DataProviderException;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class JCOProvider implements DestinationDataProvider
{
  private static Map<String, Properties> secureDBStorage = new ConcurrentHashMap<String, Properties>();
  private DestinationDataEventListener eL;

  @Override
  public Properties getDestinationProperties(String destinationName)
  {
    try
    {
      Properties p = (Properties)this.secureDBStorage.get(destinationName);

      if (p != null)
      {
        if (p.isEmpty()) {
          throw new DataProviderException(DataProviderException.Reason.INVALID_CONFIGURATION, "destination configuration is incorrect", null);
        }
        return p;
      }

      return null; 
    } catch (RuntimeException re) 
    {
    	throw new DataProviderException(DataProviderException.Reason.INTERNAL_ERROR, re);
    }
  }

  @Override
  public void setDestinationDataEventListener(DestinationDataEventListener eventListener)
  {
    this.eL = eventListener;
  }

  @Override
  public boolean supportsEvents()
  {
    return true;
  }

  public void changePropertiesForABAP_AS(String destName, Properties properties) {
    synchronized (this.secureDBStorage) {
      if (properties == null) {
        if (this.secureDBStorage.remove(destName) != null) {
          this.eL.deleted(destName);
        }
      } else {
        this.secureDBStorage.put(destName, properties);
//        if(this.eL == null) {
//          this.eL = BootApplication.bootApplication.provider.getEL();
//        }
        this.eL.updated(destName);
      }
    }
  }
}