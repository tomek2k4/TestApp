package com.example.tmaslon.testapp;

import java.util.List;

/**
 * Created by tomasz on 06.01.2016.
 */
public class JobsListResponse {
    private List<Job> jobsList;
    private JobsStatusEnum jobsStatusEnum;

    public JobsListResponse() {
        this(null, JobsStatusEnum.LIST_EMPTY);
    }

    public JobsListResponse(List<Job> jobsList, JobsStatusEnum jobsStatusEnum) {
        this.jobsList = jobsList;
        this.jobsStatusEnum = jobsStatusEnum;
    }

    public List<Job> getJobsList() {
        return jobsList;
    }

    public void setJobsList(List<Job> jobsList) {
        this.jobsList = jobsList;
    }

    public JobsStatusEnum getJobsStatusEnum() {
        return jobsStatusEnum;
    }

    public void setJobsStatusEnum(JobsStatusEnum jobsStatusEnum) {
        this.jobsStatusEnum = jobsStatusEnum;
    }
}
