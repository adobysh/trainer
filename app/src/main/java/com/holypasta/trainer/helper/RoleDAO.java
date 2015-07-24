package com.holypasta.trainer.helper;

import com.google.android.gms.wallet.LineItem;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

public class RoleDAO extends BaseDaoImpl<LineItem.Role, Integer> {

    protected RoleDAO(ConnectionSource connectionSource, Class<LineItem.Role> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<LineItem.Role> getAllRoles() throws SQLException{
        return this.queryForAll();
    }
}
