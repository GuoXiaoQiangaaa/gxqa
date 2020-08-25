package com.pwc.common.third;

import com.pwc.common.third.common.TtkConstants;
import com.ttk.jchl.openapi.sdk.TtkOpenAPI;

/**
 * @author zk
 */
class TtkOpenApiClientFactory {

    private static String apiHost = TtkConstants.API_HOST;
    private static String webHost = TtkConstants.WEB_HOST;
    private static String appKey = TtkConstants.APP_KEY;
    private static String appSecret = TtkConstants.APP_SECRET;
    private static TtkOpenAPI ttkOpenAPI = new TtkOpenAPI(apiHost, appKey, appSecret, webHost);

    public static TtkOpenAPI getTtkOpenAPI(){
        return ttkOpenAPI;
    }

}
