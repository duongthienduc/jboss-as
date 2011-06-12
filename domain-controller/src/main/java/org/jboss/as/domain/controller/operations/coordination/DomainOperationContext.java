/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.domain.controller.operations.coordination;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jboss.as.domain.controller.LocalHostControllerInfo;
import org.jboss.as.domain.controller.ServerIdentity;
import org.jboss.dmr.ModelNode;

/**
 * Stores overall contextual information for an operation executing on the domain.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class DomainOperationContext {

    private final LocalHostControllerInfo localHostInfo;
    private final ModelNode coordinatorResult = new ModelNode();
    private final ConcurrentMap<String, ModelNode> hostControllerResults = new ConcurrentHashMap<String, ModelNode>();
    private final ConcurrentMap<ServerIdentity, ModelNode> serverResults = new ConcurrentHashMap<ServerIdentity, ModelNode>();

    public DomainOperationContext(final LocalHostControllerInfo localHostInfo) {
        this.localHostInfo = localHostInfo;
    }

    public ModelNode getCoordinatorResult() {
        return coordinatorResult;
    }

    public void setCoordinatorResult(ModelNode coordinatorResult) {
        if (!this.coordinatorResult.isDefined()) {
            throw new IllegalStateException("coordinator result already set");
        } else {
            this.coordinatorResult.set(coordinatorResult);
        }
    }

    public Map<String, ModelNode> getHostControllerResults() {
        return new HashMap<String, ModelNode>(hostControllerResults);
    }

    public void addHostControllerResult(String hostId, ModelNode hostResult) {
        if (hostControllerResults.putIfAbsent(hostId, hostResult) != null) {
            throw new IllegalStateException(String.format("Result for host %s already set", hostId));
        }
    }

    public Map<ServerIdentity, ModelNode> getServerResults() {
        return new HashMap<ServerIdentity, ModelNode>(serverResults);
    }

    public void addServerResult(ServerIdentity serverId, ModelNode serverResult) {
        if (serverResults.putIfAbsent(serverId, serverResult) != null) {
            throw new IllegalStateException(String.format("Result for server %s already set", serverId));
        }
    }



}
