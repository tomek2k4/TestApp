package com.example.tmaslon.testapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomasz on 10.12.2015.
 */

public class JobsListProvider{

    private List<Job> jobs = new ArrayList<Job>();

    /**
     *
     * @return
     * The jobs
     */
    public List<Job> getJobs() {
        return jobs;
    }

    /**
     *
     * @param jobs
     * The jobs
     */
    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

}




