package com.company.restaurant.dao.hibernate;

import com.company.restaurant.dao.JobPositionDao;
import com.company.restaurant.dao.hibernate.proto.HDaoEntitySimpleDic;
import com.company.restaurant.model.JobPosition;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Yevhen on 09.06.2016.
 */
public class HJobPositionDao extends HDaoEntitySimpleDic<JobPosition> implements JobPositionDao {
    @Transactional
    @Override
    public JobPosition addJobPosition(String name) {
        return save(name);
    }

    @Transactional
    @Override
    public String delJobPosition(String name) {
        return delete(name);
    }

    @Transactional
    @Override
    public JobPosition findJobPositionByName(String name) {
        return findObjectByName(name);
    }

    @Transactional
    @Override
    public JobPosition findJobPositionById(int jobPositionId) {
        return findObjectById(jobPositionId);
    }

    @Transactional
    @Override
    public List<JobPosition> findAllJobPositions() {
        return findAllObjects();
    }
}
