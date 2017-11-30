/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.model.asdb;

import org.emitdo.oal.DOFObjectID.Authentication;
import org.emitdo.oal.security.DOFPermissionSet;

public class ScopePermission
{
    public final Authentication oid;
    public final Integer scope;
    public final DOFPermissionSet pset;
    
    public ScopePermission(Authentication oid, Integer scope, DOFPermissionSet pset)
    {
        this.scope = scope;
        this.oid = oid;
        this.pset = pset;
    }

    @Override
    public String toString()
    {
        return oid.toString() + " " + scope + " " + pset.toString();
    }
}
