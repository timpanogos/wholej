/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.model;

import java.sql.SQLException;

import org.emitdo.app.res.management.EnterpriseResPlatformManager;
import org.emitdo.oal.DOFObjectID;
import org.emitdo.oal.DOFObjectID.Authentication;

public interface ResManager extends EnterpriseResPlatformManager
{
    public void setVersion(DOFObjectID deviceOid, DOFObjectID versionOid, Authentication managerOid) throws SQLException;
}
