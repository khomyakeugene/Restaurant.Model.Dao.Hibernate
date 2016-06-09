package com.company.restaurant.dao.hibernate;

import com.company.restaurant.dao.JobPositionDao;
import com.company.restaurant.model.JobPosition;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Yevhen on 09.06.2016.
 */
public class HJobPositionDao extends HDaoTable implements JobPositionDao {
    @Override
    protected void initMetadata() {

    }

    @Transactional
    @Override
    public JobPosition addJobPosition(String name) {
        JobPosition jobPosition = new JobPosition();
        jobPosition.setName(name);

        getCurrentSeccion().save(jobPosition);

        return jobPosition;
    }

    @Override
    public String delJobPosition(String name) {
        return null;
    }

    @Override
    public JobPosition findJobPositionByName(String name) {
        return null;
    }

    @Override
    public JobPosition findJobPositionById(int jobPositionId) {
        return null;
    }

    @Override
    public List<JobPosition> findAllJobPositions() {
        return null;
    }
}
