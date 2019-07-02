package com.navercorp.pinpoint.web.dao.mysql;

import com.navercorp.pinpoint.web.dao.ApplicationRealtimeStatDao;
import com.navercorp.pinpoint.web.dao.UserDao;
import com.navercorp.pinpoint.web.vo.ApplicationRealtimeStat;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * description :
 * <p></p>
 *
 * @author Camus
 * @createTime 2018/3/8 20:25
 * @lastUpdater Camus
 * @lastUpdateTime 2018/3/8 20:25
 **/
@Repository
public class MysqlApplicationRealtimeStatDao implements ApplicationRealtimeStatDao{

    private static final String NAMESPACE = ApplicationRealtimeStatDao.class.getPackage().getName() + "." + ApplicationRealtimeStatDao.class.getSimpleName() + ".";

    @Autowired
    @Qualifier("sqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public void insertList(List<ApplicationRealtimeStat> list) {
        sqlSessionTemplate.insert(NAMESPACE + "insertList", list);
    }

    @Override
    public void updateIsShow() {
        sqlSessionTemplate.update(NAMESPACE + "updateIsShow");
    }
}
