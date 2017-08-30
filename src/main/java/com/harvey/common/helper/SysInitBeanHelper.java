package main.java.com.harvey.common.helper;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 系统初始化
 */
@Component
public class SysInitBeanHelper implements InitializingBean {
    private Logger log = Logger.getLogger(this.getClass());

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("系统开始启动...");
        initSearchServerManager();

    }

    private void initSearchServerManager(){
        log.info("索引服务启动中...");

        log.info("索引服务启动完毕...");
    }

}
