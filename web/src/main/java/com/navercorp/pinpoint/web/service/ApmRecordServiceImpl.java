package com.navercorp.pinpoint.web.service;

import com.navercorp.pinpoint.web.dao.ApmRecordDao;
import com.navercorp.pinpoint.web.vo.ApmRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Jacob on 2018/5/24.
 */
@Service
public class ApmRecordServiceImpl implements  ApmRecordService {
    @Autowired
    ApmRecordDao apmRecordDao;
    @Override
    public void insertApmRecord(ApmRecord apmRecord) {
            apmRecordDao.insertApmRecord(apmRecord);
    }
}
