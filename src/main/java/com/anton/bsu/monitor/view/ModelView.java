package com.anton.bsu.monitor.view;

import com.anton.bsu.monitor.model.RequestsModel;

/**
 * @author Q-APE
 */
public interface ModelView {

    String render(RequestsModel model);

}
