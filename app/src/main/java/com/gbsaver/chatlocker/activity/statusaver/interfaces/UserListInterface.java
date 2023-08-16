package com.gbsaver.chatlocker.activity.statusaver.interfaces;

import com.gbsaver.chatlocker.activity.statusaver.model.NodeModel;
import com.gbsaver.chatlocker.activity.statusaver.model.TrayModel;

public interface UserListInterface {
    void userListClick(int position, TrayModel trayModel);
    void fbUserListClick(int position, NodeModel trayModel);
}
