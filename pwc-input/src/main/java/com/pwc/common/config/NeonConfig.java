package com.pwc.common.config;

import com.fapiao.neon.client.in.*;
import com.fapiao.neon.client.in.impl.*;
import com.fapiao.neon.config.NeonConfiguration;
import com.fapiao.neon.config.Profile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NeonConfig {
    @Bean
    public NeonConfiguration neonConfiguration() {
        NeonConfiguration configuration = new NeonConfiguration();
        //根据实际运行环境配置Profile
//        configuration.setProfile(Profile.PRODUCT);
//        configuration.setClientId("9131000010177218151");
//        configuration.setClientSecret("6eaf8cae390142d491009393e7f53a70");

//        configuration.setProfile(Profile.PREPARE);
//        configuration.setClientId("test");
//        configuration.setClientSecret("test");

        // 安进
//        configuration.setProfile(Profile.PRODUCT);
//        configuration.setClientId("nL3UatBg");
//        configuration.setClientSecret("9d042fe1b2ae4fafb75531307fdb1e02");

        // 永和大王
        configuration.setProfile(Profile.PRODUCT);
        configuration.setClientId("G6GEq88i");
        configuration.setClientSecret("4b1b92b168c24a2f82e8f88912b81158");

        return configuration;
    }

    @Bean
    public BaseClient baseClient() {
        return new BaseClientImpl(neonConfiguration());
    }

//    @Resource
//    private BaseClient baseClient;

    @Bean
    public CollectClient collectClient() {
        return  new CollectClientImpl(neonConfiguration());
    }

    @Bean
    public CheckInvoiceClient checkInvoiceClient() {
        return  new CheckInvoiceClientImpl(neonConfiguration());
    }

    @Bean
    public DeductClient deductClient() {
        return  new DeductClientImpl(neonConfiguration());
    }

    @Bean
    public StatisticsClient statisticsClient() {
        return new StatisticsClientImpl(neonConfiguration());
    }

    @Bean
    public ConfirmClient confirmClient() {
        return new ConfirmClientImpl(neonConfiguration());
    }

    @Bean
    public CollectWholeClient collectWholeClient(){
        return new CollectWholeClientImpl(neonConfiguration());
    }
    @Bean
    public CollectCustomsClient collectCustomsClient(){return new CollectCustomsClientImpl(neonConfiguration());}
}
