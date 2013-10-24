package com.ezimgur.view.event;

/**
 * EggmanProjects
 * Author: Matthew Harris
 * Date: 10/3/12
 * Time: 9:57 AM
 */
public class OnTaskLoadEvent {
    private TaskLoading mType;

    public OnTaskLoadEvent(TaskLoading type) {
        mType = type;
    }

    public TaskLoading getType() {
        return mType;
    }

    public enum TaskLoading {
        LOAD_STARTED,
        LOAD_FINISHED,
        LOAD_CANCELED
    }
}


