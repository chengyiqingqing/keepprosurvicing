package com.amber.applivelib.live.core.lollipop;

import com.amber.applivelib.live.core.AbsLiveService;
import com.amber.applivelib.live.service.abs.AbsInnerService;

/**
 * Created by zhanghan on 2017/9/5.
 * <p>
 * 保护服务
 * 核心实现类
 */

public class LiveServiceL extends AbsLiveService {

    @Override
    public Class<? extends AbsInnerService> getInnerService() {
        return InnerService.class;
    }

    public static class InnerService extends AbsInnerService {

    }
}
