package org.jeecg.common.es;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;

/**
 *
 */
@Configuration
@Slf4j
public class ElasticSearchConfig {

    /**
     * 注入elasticsearch高阶客户端
     * @return
     */
    @Bean
    public RestHighLevelClient restHighLevelClient(@Value("${jeecg.elasticsearch.cluster-nodes}") String clusterNodes,
                                                   @Value("${jeecg.elasticsearch.check-enabled}") boolean checkEnabled) {
        RestHighLevelClient client= null;
        if(checkEnabled){
            try{
                // 验证配置的ES地址是否有效
                client = RestClients.create(ClientConfiguration.create(clusterNodes)).rest();
            } catch (Exception e) {
                log.warn("ElasticSearch 服务连接失败，原因：配置未通过。可能是cluster-nodes、port未配置或配置有误，也可能是Elasticsearch服务未启动。接下来将会拒绝执行任何方法！");
            }
        }

        return client;
    }

}