package com.nssoftware.wakagaoagent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nssoftware.wakagaoagent.entity.SysLog;
import com.nssoftware.wakagaoagent.mapper.SysLogMapper;
import com.nssoftware.wakagaoagent.service.LogService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统日志
 *
 * @author wenbin
 * @version V1.0
 * @date 2020年3月18日
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class LogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements LogService {
    /**
     * 保存日志，为了加异步注解
     *
     * @author FangCheng
     * @since 2024/12/26 09:02
     * @param sysLog sysLog
     */
    @Async
    public void saveLog(SysLog sysLog) {
        this.save(sysLog);
    }
}
