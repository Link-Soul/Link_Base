package com.link.arknights.cardpool.controller;

import com.alibaba.fastjson2.JSON;
import com.link.arknights.cardpool.entity.PoolInformation;
import com.link.arknights.cardpool.entity.cardMegForWeb.PoolCount;
import com.link.arknights.cardpool.entity.entityForMessage.CardMsgByPool;
import com.link.arknights.cardpool.entity.entityForMessage.RespectiveNum;
import com.link.arknights.cardpool.entity.entityForMessage.Role;
import com.link.arknights.cardpool.entity.entityForMessage.getNumByPoolEntity;
import com.link.arknights.cardpool.mapper.CardMsgByPoolMapper;
import com.link.arknights.cardpool.mapper.CardStateMapper;
import com.link.arknights.cardpool.mapper.PoolInformationMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@ResponseBody
public class DataMessageController {

    @Resource
    private CardStateMapper cardStateMapper;
    @Resource
    private PoolInformationMapper poolInformationMapper;
    @Resource
    private CardMsgByPoolMapper cardMsgByPoolMapper;

    /**
     * 通过uid获取抽卡信息
     *
     * @param uid 传入的uid
     * @return JSON.toJSONString(list);
     */
    @RequestMapping(value = "/getCardMessageById", method = RequestMethod.GET)
    public String getCardMessageById(Long uid) {
        long startTime = new Date().getTime();

        List<CardMsgByPool> cardMsgByPoolList = cardMsgByPoolMapper.selectByUid(uid);
        for (CardMsgByPool cardMsgByPool : cardMsgByPoolList) {
            String totalSix = cardMsgByPool.getTotalSix();
            if (!Objects.isNull(totalSix)) {
                String[] split = totalSix.split(";");
                List<Role> list = new ArrayList<>();
                for (String s : split) {
                    String[] split1 = s.split("&");
                    Role role = new Role();
                    role.setName(split1[0]);
                    role.setUrl(split1[1]);
                    role.setNum(Integer.parseInt(split1[2]));
                    role.setTime(Long.valueOf(split1[3]));
                    list.add(role);
                }
                cardMsgByPool.setSix(list);
            }
        }
        long stopTime = new Date().getTime();
        System.out.println(uid + " 通过uid获取抽卡信息 共消耗时间 " + (stopTime - startTime) + " ms");
        return JSON.toJSONString(cardMsgByPoolList);
    }

    /**
     * 柱状图用数据
     *
     * @return 柱状图用数据
     */
    @GetMapping("/queryRespectiveNum")
    public List<RespectiveNum> queryRespectiveNum() {
        long time = new Date().getTime();
        List<RespectiveNum> respectiveNums = cardStateMapper.queryRespectiveNum();
        if (!Objects.isNull(respectiveNums)) {
            RespectiveNum respectiveNum = new RespectiveNum();
            int sum = 0, six = 0, five = 0, four = 0, three = 0;
            for (RespectiveNum respective : respectiveNums) {
                sum += respective.getSum();
                six += respective.getSix();
                five += respective.getFive();
                four += respective.getFour();
                three += respective.getThree();
            }
            respectiveNum.setPool("total");
            respectiveNum.setSum(sum);
            respectiveNum.setSix(six);
            respectiveNum.setFive(five);
            respectiveNum.setFour(four);
            respectiveNum.setThree(three);
            respectiveNums.add(respectiveNum);
            long time2 = new Date().getTime();
            System.out.println("查询柱状图用数据消耗时间" + (time2 - time) + "ms");
            return respectiveNums;
        } else {
            return null;
        }
    }

    @PostMapping("/insertPoolMsg")
    public int insertPoolMsg(PoolInformation pool) {
        pool.setStartTime(pool.getStartTime().substring(0, 10));
        pool.setStopTime(pool.getStopTime().substring(0, 10));
        return poolInformationMapper.savePool(pool);
    }

    @GetMapping("/getNumByPool")
    public List<getNumByPoolEntity> getNumByPool() {
        return cardStateMapper.getNumByPool();
    }


}
