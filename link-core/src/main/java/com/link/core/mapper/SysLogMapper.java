package com.nssoftware.wakagaoagent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nssoftware.wakagaoagent.common.aop.annotation.CustomDataPermission;
import com.nssoftware.wakagaoagent.entity.SysLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志 Mapper
 *
 * @author wenbin
 * @version V1.0
 * @date 2020年3月18日
 */
@Mapper
@CustomDataPermission
public interface SysLogMapper extends BaseMapper<SysLog> {
}