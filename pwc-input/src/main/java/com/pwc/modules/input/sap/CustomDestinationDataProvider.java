package com.pwc.modules.input.sap;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.DataProviderException;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class CustomDestinationDataProvider
{
    /**
     * qj
     */
    static class MyDestinationDataProvider implements DestinationDataProvider
    {
        private DestinationDataEventListener eL;
        private HashMap<String, Properties> secureDBStorage = new HashMap<String, Properties>();

        // 实现接口：获取连接配置属性
        @Override
        public Properties getDestinationProperties(String destinationName)
        {
            try
            {
                //read the destination from DB
                Properties p = secureDBStorage.get(destinationName);

                if(p!=null)
                {
                    //check if all is correct, for example
                    if(p.isEmpty())
                        throw new DataProviderException(DataProviderException.Reason.INVALID_CONFIGURATION, "destination configuration is incorrect", null);

                    return p;
                }

                return null;
            }
            catch(RuntimeException re)
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

        //implementation that saves the properties in a very secure way 添加连接配置属性
        void changeProperties(String destName, Properties properties)
        {
            synchronized(secureDBStorage)
            {
                if(properties==null)
                {
                    if(secureDBStorage.remove(destName)!=null)
                        eL.deleted(destName);
                }
                else
                {
                    secureDBStorage.put(destName, properties);
                    eL.updated(destName); // create or updated
                }
            }
        }
    } // end of MyDestinationDataProvider

    //business logic
    void executeCalls(String destName)
    {
        JCoDestination dest;
        try
        {
            dest = JCoDestinationManager.getDestination(destName);
            dest.ping();
            System.out.println("Destination " + destName + " works");
        }
        catch(JCoException e)
        {
            e.printStackTrace();
            System.out.println("Execution on destination " + destName+ " failed");
        }
    }

    static Properties getDestinationPropertiesFromUI()
    {
        //adapt parameters in order to configure a valid destination
        Properties connectProperties = new Properties();
        /*connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, "192.168.108.105");
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  "01");
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, "620");
        connectProperties.setProperty(DestinationDataProvider.JCO_USER,   "rpa3");
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, "123456");
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   "ZH");*/
        MyDestinationDataProvider myDestinationDataProvider=new MyDestinationDataProvider();
        try {
            connectProperties.load(myDestinationDataProvider.getClass().getResourceAsStream("/sap_conf.properties"));
            System.out.println("SAP链接配置文件<<<<<<<<<"+connectProperties);
        } catch (IOException e) {

        }
        return connectProperties;
    }

    public static void main(String[] args)
    {

        MyDestinationDataProvider myProvider = new MyDestinationDataProvider();

        //register the provider with the JCo environment;
        //catch IllegalStateException if an instance is already registered
        try
        {
            com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(myProvider);
        }
        catch(IllegalStateException providerAlreadyRegisteredException)
        {
            //somebody else registered its implementation,
            //stop the execution
            throw new Error(providerAlreadyRegisteredException);
        }

        String destName = "ABAP_AS";
        CustomDestinationDataProvider test = new CustomDestinationDataProvider();

        //set properties for the destination and ...
        myProvider.changeProperties(destName, getDestinationPropertiesFromUI());
        //... work with it
        test.executeCalls(destName);

        //now remove the properties and ...
        //myProvider.changeProperties(destName, null);
        //... and let the test fail
        //test.executeCalls(destName);
    }

}
