/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.model.asdb;

import org.emitdo.oal.DOFObjectID.Authentication;

public class RemoteDomainInfo
{
    public final Authentication oid;
    public final int rdid;
    public final Authentication currentLocalNode;
    public final Authentication defaultLocalNode;
    
    public RemoteDomainInfo(Authentication oid, int rdid, Authentication currentLocalNode, Authentication defaultLocalNode)
    {
        this.oid = oid;
        this.rdid = rdid;
        this.currentLocalNode = currentLocalNode;
        this.defaultLocalNode = defaultLocalNode;
    }

    @Override
    public String toString()
    {
        return oid.toString() + " rdid: " + rdid + " cln: " + currentLocalNode + " dln:" + defaultLocalNode;
    }
}
