// Copyright © Microsoft Open Technologies, Inc.
//
// All Rights Reserved
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// THIS CODE IS PROVIDED *AS IS* BASIS, WITHOUT WARRANTIES OR CONDITIONS
// OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION
// ANY IMPLIED WARRANTIES OR CONDITIONS OF TITLE, FITNESS FOR A
// PARTICULAR PURPOSE, MERCHANTABILITY OR NON-INFRINGEMENT.
//
// See the Apache License, Version 2.0 for the specific language
// governing permissions and limitations under the License.

package com.microsoft.aad.adal;

import java.io.Serializable;
import java.util.UUID;

/**
 * Represent request and keeps authorization code and similar info.
 */
class AuthenticationRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private int mRequestId = 0;

    private String mAuthority = null;

    private String mRedirectUri = null;

    private String[] mScope = null;

    private String mClientId = null;

    private UserIdentifier mUserIdentifier = null;

    private String mBrokerAccountName = null;

    private UUID mCorrelationId;

    private String mExtraQueryParamsAuthentication;

    private PromptBehavior mPrompt;

    private boolean mSilent = false;

    private String mVersion = null;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String authority, String[] scope, String client, String redirect,
            UserIdentifier userId, PromptBehavior prompt, String extraQueryParams,
            UUID correlationId) {
        mAuthority = authority;
        mScope = scope;
        mClientId = client;
        mRedirectUri = redirect;
        mUserIdentifier = userId;
        mBrokerAccountName = userId.getDisplayableId();
        mPrompt = prompt;
        mExtraQueryParamsAuthentication = extraQueryParams;
        mCorrelationId = correlationId;
    }

    public AuthenticationRequest(String authority, String[] scope, String client, String redirect,
            UserIdentifier userId, UUID requestCorrelationId) {
        mAuthority = authority;
        mScope = scope;
        mClientId = client;
        mRedirectUri = redirect;
        mUserIdentifier = userId;
        mBrokerAccountName = userId.getDisplayableId();
        mCorrelationId = requestCorrelationId;
    }

    public AuthenticationRequest(String authority, String[] scope, String client, String redirect,
            UserIdentifier userId) {
        mAuthority = authority;
        mScope = scope;
        mClientId = client;
        mRedirectUri = redirect;
        mUserIdentifier = userId;
        mBrokerAccountName = userId.getDisplayableId();
    }

    public AuthenticationRequest(String authority, String[] scope, String clientid) {
        mAuthority = authority;
        mScope = scope;
        mClientId = clientid;
    }

    /**
     * Cache usage and refresh token requests.
     * 
     * @param authority
     * @param resource
     * @param clientid
     * @param userid
     * @param correlationId
     */
    public AuthenticationRequest(String authority, String[] scope, String clientid, UserIdentifier userId,
            UUID correlationId) {
        mAuthority = authority;
        mScope = scope;
        mClientId = clientid;
        mUserIdentifier = userId;
        mBrokerAccountName = userId.getDisplayableId();
        mCorrelationId = correlationId;
    }

    public AuthenticationRequest(String authority, String[] scope, String clientId,
            UUID correlationId) {
        mAuthority = authority;
        mClientId = clientId;
        mScope = scope;
        mCorrelationId = correlationId;
    }

    public String getAuthority() {
        return mAuthority;
    }

    public void setAuthority(String authority) {
        mAuthority = authority;
    }

    public String getRedirectUri() {
        return mRedirectUri;
    }

    public String[] getScope() {
        return mScope;
    }
    
    public String getScopeString(){
        return StringExtensions.createStringFromArray(mScope, AuthenticationConstants.AAD.SCOPE_DELIMETER);
    }

    public String getClientId() {
        return mClientId;
    }

    public String getLoginHint() {
        return mUserIdentifier != null ? mUserIdentifier.getDisplayableId() : "";
    }

    public UUID getCorrelationId() {
        return this.mCorrelationId;
    }

    public String getExtraQueryParamsAuthentication() {
        return mExtraQueryParamsAuthentication;
    }

    public String getLogInfo() {
        return String.format("Request authority:%s scope:%s clientid:%s", mAuthority,
                StringExtensions.createStringFromArray(mScope, " "), mClientId);
    }

    public PromptBehavior getPrompt() {
        return mPrompt;
    }

    public void setPrompt(PromptBehavior prompt) {
        this.mPrompt = prompt;
    }

    /**
     * @return the mRequestId related to the delegate
     */
    public int getRequestId() {
        return mRequestId;
    }

    /**
     * @param requestId the requestId to set
     */
    public void setRequestId(int requestId) {
        this.mRequestId = requestId;
    }

    public String getBrokerAccountName() {
        return mBrokerAccountName;
    }

    public void setBrokerAccountName(String brokerAccountName) {
        this.mBrokerAccountName = brokerAccountName;
    }

    public String getUniqueId() {
        return mUserIdentifier != null ? mUserIdentifier.getUniqueId() : "";
    }

    public boolean isSilent() {
        return mSilent;
    }

    public void setSilent(boolean silent) {
        this.mSilent = silent;
    }

    public String getVersion() {
        return mVersion;
    }

    public void setVersion(String version) {
        this.mVersion = version;
    }

    public UserIdentifier getUserIdentifier(){
        return mUserIdentifier;
    }
    
    public void setUserIdentifier(UserIdentifier user){
        this.mUserIdentifier = user;
    }

    public String getDisplayableId() {
        return mUserIdentifier != null ? mUserIdentifier.getDisplayableId() : "";
    }
}
