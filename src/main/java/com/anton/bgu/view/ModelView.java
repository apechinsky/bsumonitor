package com.anton.bgu.view;

import com.anton.bgu.model.RequestsModel;

/**
 * @author Q-APE
 */
public interface ModelView {

    String render(RequestsModel model);


    static void log(Object pattern, Object... arguments) {
        System.out.printf(pattern + "%n", arguments);
    }

}
