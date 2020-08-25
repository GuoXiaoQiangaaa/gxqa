package com.pwc.modules.input.entity.rpa;

import com.pwc.modules.input.entity.InputSapConfEntity;
import com.pwc.modules.input.service.InputSapConfEntityService;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.DataProviderException;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Properties;

public class CustomDestinationDataProvider
{
    private static InputSapConfEntityService sapConfEntityService;

    @Autowired
    public void setSapConfEntityService(InputSapConfEntityService sapConfEntityService) {
        CustomDestinationDataProvider.sapConfEntityService = sapConfEntityService;
    }

    /**
     * qj
     */
    public static class MyDestinationDataProvider implements DestinationDataProvider
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
                    if(p.isEmpty()) {
                        throw new DataProviderException(DataProviderException.Reason.INVALID_CONFIGURATION, "destination configuration is incorrect", null);
                    }

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

        public DestinationDataEventListener getEL() {
            return this.eL;
        }

        //implementation that saves the properties in a very secure way 添加连接配置属性
        public void changeProperties(String destName, Properties properties)
        {
            synchronized(secureDBStorage)
            {
                if(properties==null)
                {
                    if(secureDBStorage.remove(destName)!=null) {
                        eL.deleted(destName);
                    }
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
    public JCoDestination executeCalls(String destName)
    {
        JCoDestination dest = null;
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
        return dest;
    }

    /**
     * sap
     * @return
     */
    public static Properties getDestinationPropertiesFromUI()
    {
        InputSapConfEntity sapConfEntity = sapConfEntityService.getById(1);
        //adapt parameters in order to configure a valid destination
        Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, sapConfEntity.getAshost());
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  sapConfEntity.getSysnr());
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, sapConfEntity.getClient());
        connectProperties.setProperty(DestinationDataProvider.JCO_USER,   sapConfEntity.getUser());
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, sapConfEntity.getPassWord());
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   sapConfEntity.getLang());
//        MyDestinationDataProvider myDestinationDataProvider=new MyDestinationDataProvider();
//        try {
//            connectProperties.load(myDestinationDataProvider.getClass().getResourceAsStream("/sap_conf.properties"));
            System.out.println("SAP链接配置文件<<<<<<<<<"+connectProperties);
//        } catch (IOException e) {
//
//        }
        return connectProperties;
    }

    /**
     * rfc
     * @return
     */
    public static Properties getDestinationPropertiesFromUI2()
    {
        InputSapConfEntity sapConfEntity = sapConfEntityService.getById(4);
        //adapt parameters in order to configure a valid destination
        Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, sapConfEntity.getAshost());
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  sapConfEntity.getSysnr());
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, sapConfEntity.getClient());
        connectProperties.setProperty(DestinationDataProvider.JCO_USER,   sapConfEntity.getUser());
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, sapConfEntity.getPassWord());
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   sapConfEntity.getLang());
//        MyDestinationDataProvider myDestinationDataProvider=new MyDestinationDataProvider();
//        try {
//            connectProperties.load(myDestinationDataProvider.getClass().getResourceAsStream("/sap_conf.properties"));
        System.out.println("SAP链接配置文件<<<<<<<<<"+connectProperties);
//        } catch (IOException e) {
//
//        }
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
