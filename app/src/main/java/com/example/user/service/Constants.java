package com.example.user.service;

/**
 * Created by User on 6/2/2017.
 */

public class Constants {
    public interface IAction {
        public static final String MAIN_ACTION = "com.example.user.service.action.main";
        public static final String ACTION_PLAY = "com.example.user.service.action_play";
        public static final String ACTION_PAUSE = "com.example.user.service.action_pause";
        public static final String ACTION_RESUME = "com.example.user.service.action_resume";
        public static final String ACTION_STOP = "com.example.user.service.action_stop";
        public static final String ACTION_PREVIOUS = "com.example.user.service.action_previous";
        public static final String ACTION_NEXT = "com.example.user.service.action_next";
        public static final String ACTION_STOPFOREGROUND = "com.example.user.service.action_stopforeground";
    }
    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }
}
