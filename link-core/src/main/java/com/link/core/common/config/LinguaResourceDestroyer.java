package com.link.core.common.config;


import com.link.core.utils.LinguaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 *@Description 应用结束时释放加载的 Lingua翻译包的资源
 *@author Link
 *@since 2026/1/31 10:01
 **/
@Component
@Slf4j
public class LinguaResourceDestroyer implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        // 卸载语言模型
        LinguaUtils.destroyDetector();
        log.info("Lingua翻译包资源已释放");
    }
}
