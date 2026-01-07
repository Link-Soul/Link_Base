package com.nssoftware.wakagaoagent.common.satoken;

import cn.dev33.satoken.stp.StpInterface;
import com.nssoftware.wakagaoagent.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * SaToken自定义权限加载接口实现类
 *
 * @author wenbin
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private PermissionService permissionService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return permissionService.getPermissionsByUserId(String.valueOf(loginId));
    }

    /**
     * 返回一个账号所拥有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return null;
    }

}
