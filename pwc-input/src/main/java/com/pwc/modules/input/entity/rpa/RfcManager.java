package com.pwc.modules.input.entity.rpa;

import com.sap.conn.jco.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public final class RfcManager
{
  private static Logger logger = LoggerFactory.getLogger(RfcManager.class);

  private static String ABAP_AS_POOLED = "ABAP_AS";

  private static JCOProvider provider = null;

  private static JCoDestination destination = null;

//  static {
//    Properties properties = loadProperties();
//
//    provider = new JCOProvider();
//    try
//    {
//      Environment.registerDestinationDataProvider(provider);
//    } catch (IllegalStateException e) {
//      //logger.debug(e);
//    }
//
//    provider.changePropertiesForABAP_AS(ABAP_AS_POOLED, properties);
//  }

  public static Properties loadProperties() {
    RfcManager manager = new RfcManager();
    Properties prop = new Properties();
    try {
      prop.load(manager.getClass().getResourceAsStream("/sap_conf.properties"));
    } catch (IOException e) {
      //logger.debug(e);
    }
    return prop;
  }

  public static JCoDestination getDestination() throws JCoException {
    if (destination == null) {
      destination = JCoDestinationManager.getDestination(ABAP_AS_POOLED);
    }
    return destination;
  }

  public static JCoFunction getFunction(String functionName) {
    JCoFunction function = null;
    try {
//      JCoDestination jCoDestination = JCoDestinationManager.getDestination(ABAP_AS_POOLED);
      JCoDestination jCoDestination = getDestination();
      jCoDestination.ping();
      JCoRepository jCoRepository = jCoDestination.getRepository();
      JCoFunctionTemplate jCoFunctionTemplate = jCoRepository.getFunctionTemplate(functionName);
      function = jCoFunctionTemplate.getFunction();
    } catch (JCoException e) {
      logger.error(e.getMessage());
    } catch (NullPointerException e) {
      logger.error(e.getMessage());
    }
    return function;
  }

  public static void execute(JCoFunction function) {
    //logger.debug("SAP Function Name : " + function.getName());
    JCoParameterList paramList = function.getImportParameterList();

    if (paramList != null) {
      //logger.debug("Function Import Structure : " + paramList.toString());
    }
    try
    {
      function.execute(getDestination());
    } catch (JCoException e) {
      //logger.error(e);
    }
    paramList = function.getExportParameterList();

    //if (paramList != null)
      //logger.debug("Function Export Structure : " + paramList.toString());
  }

  public static String ping()
  {
    String msg = null;
    try {
      getDestination().ping();
      msg = "Destination " + ABAP_AS_POOLED + " is ok";
    } catch (JCoException ex) {
      msg = ex.toString();
    }
    //logger.debug(msg);
    System.out.println(msg);
    return msg;
  }

  public static void main(String[] args) {
    ping();
  }
}