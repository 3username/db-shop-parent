package com.db.service.impl;

import com.db.base.BaseApiService;
import com.db.base.BaseResponse;
import com.db.entity.AppEntity;
import com.db.service.AppService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppServiceImpl extends BaseApiService<AppEntity> implements AppService {
    @Override
    public BaseResponse<AppEntity> getApp() {
        return setResultSuccess(new AppEntity("11","22"));
    }
}
