package com.company.restaurant.dao.hibernate;

import com.company.restaurant.dao.JobPositionDao;
import com.company.restaurant.dao.hibernate.proto.HDaoTableSimpleDic;
import com.company.restaurant.model.JobPosition;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Yevhen on 09.06.2016.
 */
public class HJobPositionDao extends HDaoTableSimpleDic<JobPosition> implements JobPositionDao {
    private static final String JOB_POSITION_DIC_TABLE_NAME = "job_position_dic";
    private static final String JOB_POSITION_ID_FIELD_NAME = "job_position_id";
    private static final String DEFAULT_ORDER_BY_CONDITION = "ORDER BY job_position_id";

    @Override
    protected void initMetadata() {
        super.initMetadata();

        this.tableName = JOB_POSITION_DIC_TABLE_NAME;
        this.idFieldName = JOB_POSITION_ID_FIELD_NAME;
        this.orderByCondition = DEFAULT_ORDER_BY_CONDITION;
    }

    @Transactional
    @Override
    public JobPosition addJobPosition(String name) {
        JobPosition jobPosition = new JobPosition();
        jobPosition.setName(name);

        save(jobPosition);

        return jobPosition;
    }

    @Override
    public String delJobPosition(String name) {
        return null;
    }

    @Transactional
    @Override
    public JobPosition findJobPositionByName(String name) {
        return findObjectByName(name);
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
