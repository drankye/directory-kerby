/**
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package org.apache.kerby.kerberos.kerb.admin.server;

import org.apache.kerby.kerberos.kerb.identity.IdentityService;
import org.apache.kerby.kerberos.kerb.server.preauth.PreauthHandler;
import org.apache.kerby.kerberos.kerb.server.replay.ReplayCheckService;

public class AdminServerContext {
    private final AdminServerSetting adminServerSetting;

    private IdentityService identityService;
    private ReplayCheckService replayCache;
    private PreauthHandler preauthHandler;

    public AdminServerContext(AdminServerSetting adminServerSetting) {
        this.adminServerSetting = adminServerSetting;
    }

    public AdminServerSetting getAdminServerSetting() {
        return adminServerSetting;
    }

    public AdminServerConfig getConfig() {
        return adminServerSetting.getAdminServerConfig();
    }

    public void setPreauthHandler(PreauthHandler preauthHandler) {
        this.preauthHandler = preauthHandler;
    }

    public PreauthHandler getPreauthHandler() {
        return this.preauthHandler;
    }

    public void setReplayCache(ReplayCheckService replayCache) {
        this.replayCache = replayCache;
    }

    public ReplayCheckService getReplayCache() {
        return replayCache;
    }

    public void setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
    }

    public IdentityService getIdentityService() {
        return identityService;
    }

    public String getKdcRealm() {
        return adminServerSetting.getKdcRealm();
    }
}
