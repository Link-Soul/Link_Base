package com.link.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.link.core.entity.SysLog;


/**
 * 系统日志
 *
 * @author wenbin
 * @version V1.0
 * @date 2020年3月18日
 */
public interface LogService extends IService<SysLog> {
    /**
     * 保存日志，为了加异步注解
     *
     * @author Link
     * @since 2024/12/26 09:02
     * @param sysLog sysLog
     */
    void saveLog(SysLog sysLog);
}
