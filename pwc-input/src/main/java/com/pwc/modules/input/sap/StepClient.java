package com.pwc.modules.input.sap;

import com.sap.conn.jco.*;

import java.util.concurrent.CountDownLatch;

import static com.pwc.modules.input.sap.CustomDestinationDataProvider.getDestinationPropertiesFromUI;


/**
 * basic examples for Java to ABAP communication
 * qj
 */
public class StepClient
{
    static String ABAP_AS = "ABAP_AS";
    static String ABAP_AS_POOLED = "ABAP_AS";
    static String ABAP_MS = "ABAP_MS";

    /**
     * This example demonstrates the destination concept introduced with JCO 3.
     * The application does not deal with single connections anymore. Instead
     * it works with logical destinations like ABAP_AS and ABAP_MS which separates
     * the application logic from technical configuration.
     * @throws JCoException
     */
    public static void step1Connect() throws JCoException
    {
        JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS);
        System.out.println("Attributes:");
        System.out.println(destination.getAttributes());
        System.out.println();

        destination = JCoDestinationManager.getDestination(ABAP_MS);
        System.out.println("Attributes:");
        System.out.println(destination.getAttributes());
        System.out.println();
    }

    /**
     * This example uses a connection pool. However, the implementation of
     * the application logic is still the same. Creation of pools and pool management
     * are handled by the JCo runtime.
     *
     * @throws JCoException
     */
    public static void step2ConnectUsingPool() throws JCoException
    {
        JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS_POOLED);
        destination.ping();
        System.out.println("Attributes:");
        System.out.println(destination.getAttributes());
        System.out.println();
    }

    /**
     * The following example executes a simple RFC function STFC_CONNECTION.
     * In contrast to JCo 2 you do not need to take care of repository management.
     * JCo 3 manages the repository caches internally and shares the available
     * function metadata as much as possible.
     * @throws JCoException
     */
    public static void step3SimpleCall() throws JCoException
    {
        //JCoDestination is the logic address of an ABAP system and ...
        JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS_POOLED);
        // ... it always has a reference to a metadata repository
        //从对象仓库中获取 RFM 函数
        JCoFunction function = destination.getRepository().getFunction("ZFIT_082011");
        if(function == null) {
            throw new RuntimeException("ZFIT_082011 not found in SAP.");
        }

        //JCoFunction is container for function values. Each function contains separate
        //containers for import, export, changing and table parameters.
        //To set or get the parameters use the APIS setValue() and getXXX().
        // 设置import 参数
        JCoParameterList importParam = function.getImportParameterList();
        importParam.setValue("MBLNR", "123456789");

        try
        {
            //execute, i.e. send the function to the ABAP system addressed
            //by the specified destination, which then returns the function result.
            //All necessary conversions between Java and ABAP data types
            //are done automatically.
            function.execute(destination);
        }
        catch(AbapException e)
        {
            System.out.println(e.toString());
            return;
        }

        System.out.println("STFC_CONNECTION finished:");
        System.out.println(" Echo: " + function.getExportParameterList().getString("ECHOTEXT"));
        System.out.println(" Response: " + function.getExportParameterList().getString("RESPTEXT"));
        System.out.println();
    }

    /**
     * ABAP APIs often uses complex parameters. This example demonstrates  访问结构 (Structure)
     * how to read the values from a structure.
     * @throws JCoException
     */

    public static void step3WorkWithStructure() throws JCoException
    {
        JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS_POOLED);
        JCoFunction function = destination.getRepository().getFunction("ZFIT_082011");
        if(function == null) {
            throw new RuntimeException("ZFIT_082011 not found in SAP.");
        }

        try
        {
            function.execute(destination);
        }
        catch(AbapException e)
        {
            System.out.println(e.toString());
            return;
        }

        //从返回数据中解析
        JCoStructure exportStructure = function.getExportParameterList().getStructure("RFCSI_EXPORT");
        System.out.println("System info for " + destination.getAttributes().getSystemID() + ":\n");

        //*********也可直接通过结构中的字段名或字段所在的索引位置来读取某个字段的值
        System.out.println("RFCPROTO:\t"+exportStructure.getString(0));
        System.out.println("RFCPROTO:\t"+exportStructure.getString("RFCPROTO"));



        //The structure contains some fields. The loop just prints out each field with its name.
        for(int i = 0; i < exportStructure.getMetaData().getFieldCount(); i++)
        {
            System.out.println(exportStructure.getMetaData().getName(i) + ":\t" + exportStructure.getString(i));
        }
        System.out.println();

        //JCo still supports the JCoFields, but direct access via getXXX is more efficient as field iterator
        // efficient as field iterator  也可以使用下面的方式来遍历
        System.out.println("The same using field iterator: \nSystem info for " + destination.getAttributes().getSystemID() + ":\n");
        for(JCoField field : exportStructure)
        {
            System.out.println(field.getName() + ":\t" + field.getString());
        }
        System.out.println();
    }

    /**
     * A slightly more complex example than before. Query the companies list   访问表
     * returned in a table and then obtain more details for each company.
     * @throws JCoException
     */
    public static void step4WorkWithTable() throws JCoException
    {
        JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS_POOLED);
        JCoFunction function = destination.getRepository().getFunction("BAPI_COMPANYCODE_GETLIST");
        if(function == null) {
            throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");
        }

        try
        {
            function.execute(destination);
        }
        catch(AbapException e)
        {
            System.out.println(e.toString());
            return;
        }

        JCoStructure returnStructure = function.getExportParameterList().getStructure("RETURN");
        if (! (returnStructure.getString("TYPE").equals("")||returnStructure.getString("TYPE").equals("S"))  )
        {
           throw new RuntimeException(returnStructure.getString("MESSAGE"));
        }

        JCoTable codes = function.getTableParameterList().getTable("COMPANYCODE_LIST");
        for (int i = 0; i < codes.getNumRows(); i++)
        {
            codes.setRow(i);
            System.out.println(codes.getString("COMP_CODE") + '\t' + codes.getString("COMP_NAME"));
        }

        //move the table cursor to first row
        codes.firstRow();
        for (int i = 0; i < codes.getNumRows(); i++, codes.nextRow())
        {
            function = destination.getRepository().getFunction("BAPI_COMPANYCODE_GETDETAIL");
            if (function == null) {
                throw new RuntimeException("BAPI_COMPANYCODE_GETDETAIL not found in SAP.");
            }

            function.getImportParameterList().setValue("COMPANYCODEID", codes.getString("COMP_CODE"));

            //We do not need the addresses, so set the corresponding parameter to inactive.
            //Inactive parameters will be  either not generated or at least converted.
            function.getExportParameterList().setActive("COMPANYCODE_ADDRESS",false);

            try
            {
                function.execute(destination);
            }
            catch (AbapException e)
            {
                System.out.println(e.toString());
                return;
            }

            returnStructure = function.getExportParameterList().getStructure("RETURN");
            if (! (returnStructure.getString("TYPE").equals("") ||
                   returnStructure.getString("TYPE").equals("S") ||
                   returnStructure.getString("TYPE").equals("W")) )
            {
                throw new RuntimeException(returnStructure.getString("MESSAGE"));
            }

            JCoStructure detail = function.getExportParameterList().getStructure("COMPANYCODE_DETAIL");

            System.out.println(detail.getString("COMP_CODE") + '\t' +
                               detail.getString("COUNTRY") + '\t' +
                               detail.getString("CITY"));
        }//for
    }

    /**
     * this example shows the "simple" stateful call sequence. Since all calls belonging to one
     * session are executed within the same thread, the application does not need
     * to take into account the SessionReferenceProvider. MultithreadedExample.java
     * illustrates the more complex scenario, where the calls belonging to one session are
     * executed in different threads.
     *
     * Note: this example uses Z_GET_COUNTER and Z_INCREMENT_COUNTER. Most ABAP systems
     * contain function modules GET_COUNTER and INCREMENT_COUNTER that are not remote-enabled.
     * Copy these functions to Z_GET_COUNTER and Z_INCREMENT_COUNTER (or implement as wrapper)
     * and declare them to be remote enabled.
     * @throws JCoException
     */
    public static void step4SimpleStatefulCalls() throws JCoException
    {
        final JCoFunctionTemplate incrementCounterTemplate, getCounterTemplate;

        JCoDestination destination = JCoDestinationManager.getDestination(ABAP_MS);
        incrementCounterTemplate = destination.getRepository().getFunctionTemplate("Z_INCREMENT_COUNTER");
        getCounterTemplate = destination.getRepository().getFunctionTemplate("Z_GET_COUNTER");
        if(incrementCounterTemplate == null || getCounterTemplate == null) {
            throw new RuntimeException("This example cannot run without Z_INCREMENT_COUNTER and Z_GET_COUNTER functions");
        }

        final int threadCount = 5;
        final int loops = 5;
        final CountDownLatch startSignal = new CountDownLatch(threadCount);
        final CountDownLatch doneSignal = new CountDownLatch(threadCount);

        Runnable worker = new Runnable()
        {
            @Override
            public void run()
            {
                startSignal.countDown();
                try
                {
                    //wait for other threads
                    startSignal.await();

                    JCoDestination dest = JCoDestinationManager.getDestination(ABAP_MS);
                    JCoContext.begin(dest);
                    try
                    {
                        for(int i=0; i < loops; i++)
                        {
                            JCoFunction incrementCounter = incrementCounterTemplate.getFunction();
                            incrementCounter.execute(dest);
                        }
                        JCoFunction getCounter = getCounterTemplate.getFunction();
                        getCounter.execute(dest);

                        int remoteCounter = getCounter.getExportParameterList().getInt("GET_VALUE");
                        System.out.println("Thread-" + Thread.currentThread().getId() +
                                " finished. Remote counter has " + (loops==remoteCounter?"correct":"wrong") +
                                " value [" + remoteCounter + "]");
                    }
                    finally
                    {
                        JCoContext.end(dest);
                    }
                }
                catch(Exception e)
                {
                    System.out.println("Thread-" + Thread.currentThread().getId() + " ends with exception " + e.toString());
                }

                doneSignal.countDown();
            }
        };

        for(int i = 0; i < threadCount; i++)
        {
            new Thread(worker).start();
        }

        try
        {
            doneSignal.await();
        }
        catch(Exception e)
        {
        }

    }

    public static void main(String[] args) throws JCoException
    {
        CustomDestinationDataProvider.MyDestinationDataProvider myProvider = new CustomDestinationDataProvider.MyDestinationDataProvider();

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
        rfcCall();
//        step1Connect();
//        step2ConnectUsingPool();
//        step3SimpleCall();
//        step4WorkWithTable();
//        step4SimpleStatefulCalls();
    }

    public static void rfcCall() throws JCoException
    {
        //JCoDestination is the logic address of an ABAP system and ...
        JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS_POOLED);
        // ... it always has a reference to a metadata repository
        //从对象仓库中获取 RFM 函数
        JCoFunction function = destination.getRepository().getFunctionTemplate("ZFM_FI_TAXPLATFORM_PRICE").getFunction();

        if(function == null) {
            throw new RuntimeException("ZFM_FI_TAXPLATFORM_PRICE not found in SAP.");
        }

        try {
            //如果传如参数是内表的形式的话就以如下代码传入sap系统
            JCoTable T_ACCDOCUMENT = function.getTableParameterList().getTable("IT_MBLNR");
            T_ACCDOCUMENT.appendRow();//增加一行
            //给表参数中的字段赋值，此处测试，就随便传两个值进去
            T_ACCDOCUMENT.setValue("MBLNR", "5002463357");
           /* T_ACCDOCUMENT.appendRow();//增加一行
            T_ACCDOCUMENT.setValue("MBLNR", "5001916520");
            T_ACCDOCUMENT.appendRow();//增加一行
            T_ACCDOCUMENT.setValue("MBLNR", "5001925254");
            T_ACCDOCUMENT.appendRow();//增加一行
            T_ACCDOCUMENT.setValue("MBLNR", "5001919531");
            T_ACCDOCUMENT.appendRow();//增加一行
            T_ACCDOCUMENT.setValue("MBLNR", "5001909487");
            //执行调用函数*/
            function.execute(destination);
            //获取传入参数返回状态表
            JCoTable statusTable = function.getTableParameterList().getTable("IT_MBLNR");//得到sap返回的参数，你就把他当作c语言的结构体理解就可以了
            System.out.println(statusTable);
            for(int i = 0; i < statusTable.getNumRows(); i++) {
                statusTable.setRow(i);
                //这里获取sap函数传出内表结构的字段
                String rc = statusTable.getString("RCODE");
                String mblnr= statusTable.getString("MBLNR");//物料凭证编号 记住这里MBLNR一定是大写的，不然得不到值
                if(("02").equals(rc)){
                    System.out.println("物料凭证编号:"+mblnr+"  合同编号未维护");
                }else if(("03").equals(rc)){
                    System.out.println("物料凭证编号:"+mblnr+"  付款条件为空或不一致");
                }else if(("04").equals(rc)){
                    System.out.println("物料凭证编号:"+mblnr+"  质检未通过");
                }else if(("05").equals(rc)){
                    System.out.println("物料凭证编号:"+mblnr+"  物料凭证已被冲销");
                }else if(("06").equals(rc)){
                    System.out.println("物料凭证编号:"+mblnr+"  物料凭证移动类型有误");
                }else if(("07").equals(rc)){
                    System.out.println("物料凭证编号:"+mblnr+"  物料凭证移动不存在");
                }
                    JCoTable exportTable = function.getTableParameterList().getTable("ET_DATA");//得到sap返回的参数，你就把他当作c语言的结构体理解就可以了
                    System.out.println(exportTable);
                    //有时候sap那边只是返回一个输出参数，sap比方说你这边输入一个物料号，想得到sap那边的物料描述，这是sap方是不会返回一个内表给你的
                    //而是只是返回一个输出参数给你这时你就要用到下面的方法来得到输出参数
                    //paramList = function.getExportParameterList();
                    //paramList.getString("rfc返回字段字段名称");
                    for(int j = 0; j < exportTable.getNumRows(); j++) {
                        exportTable.setRow(i);
                        //这里获取sap函数传出内表结构的字段
                        String BUKRS = exportTable.getString("BUKRS");//记住这里BUKRS一定是大写的，不然得不到值
                        System.out.println("公司代码<<<<<<<<<<<<<<<"+BUKRS);

                        String LIFNR = exportTable.getString("LIFNR");
                        System.out.println("供应商编号<<<<<<<<<<<<<<<"+LIFNR);

                        String NAME1 = exportTable.getString("NAME1");
                        System.out.println("供应商全称<<<<<<<<<<<<<<<"+NAME1);

                        String SORTL = exportTable.getString("SORTL");
                        System.out.println("供应商简称<<<<<<<<<<<<<<<"+SORTL);

                        //得到了sap数据，然后下面就是你java擅长的部分了，想封装成什么类型的都由你
                    }
            }



        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } finally {
            destination = null;
        }
    }

}
