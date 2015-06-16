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
import java.util.Calendar;
import java.util.Date;

/**
 * Extended result to store more info Queries will be performed over this item
 * not the key.
 */
public class TokenCacheItem implements Serializable {

    /**
     * Serial version.
     */
    private static final long serialVersionUID = 1L;

    private static final String TAG = "TokenCacheItem";

    private String mAuthority;

    private String[] mScope;
    
    private String mClientId;
    
    
    private String mUniqueId;
    
    private String mDisplayableId;
    
    private String mTenantId;
    
    /**
     * This time is GMT.
     */
    private Date mExpiresOn;

    private String mToken;

    private String mProfileInfo;
    
    private String mFamilyName;
    
    private String mGivenName;
    
    private String mIdentityProvider;
    
    private String mRawIdToken;
    /**
     * Construct default cache item.
     */
    public TokenCacheItem() {

    }

    TokenCacheItem(final TokenCacheKey request, final AuthenticationResult result) {
        if (request != null) {
            mAuthority = request.getAuthority();
            mClientId = request.getClientId();
            mScope = request.getScope();
            mUniqueId = request.getUniqueId();
            mDisplayableId = request.getDisplayableId();
            mTenantId = request.getTenantId();
            mExpiresOn = request.getExpiresOn();
            mToken = request.getToken();
            mProfileInfo = request.getProfileInfo();
            
            if( result.getUserInfo() != null){
                mGivenName = result.getUserInfo().getGivenName();
                mFamilyName = result.getUserInfo().getFamilyName();
                mIdentityProvider = result.getUserInfo().getIdentityProvider();
            }
            
            mRawIdToken = result.getIdToken();
        }
    }

    /**
     * Checks expiration time.
     * 
     * @return true if expired
     */
    public static boolean isTokenExpired(Date expiresOn) {
        Calendar calendarWithBuffer = Calendar.getInstance();
        calendarWithBuffer.add(Calendar.SECOND,
                AuthenticationSettings.INSTANCE.getExpirationBuffer());
        Date validity = calendarWithBuffer.getTime();
        Logger.v(TAG, "expiresOn:" + expiresOn + " timeWithBuffer:" + calendarWithBuffer.getTime()
                + " Buffer:" + AuthenticationSettings.INSTANCE.getExpirationBuffer());

        if (expiresOn != null && expiresOn.before(validity)) {
            return true;
        }

        return false;
    }

    boolean match(TokenCacheKey key)
    {
//        return (key.Authority == this.mAuthority && key.ScopeEquals(this.Scope) && key.ClientIdEquals(this.ClientId)
//                && key.TokenSubjectType == this.TokenSubjectType && key.UniqueId == this.UniqueId &&
//                key.DisplayableIdEquals(this.DisplayableId));
        return false;
    }
    
    public String getAuthority() {
        return mAuthority;
    }

    public void setAuthority(String mAuthority) {
        this.mAuthority = mAuthority;
    }

    public String[] getScope() {
        return mScope;
    }

    public void setScope(String[] mScope) {
        this.mScope = mScope;
    }

    public String getClientId() {
        return mClientId;
    }

    public void setClientId(String mClientId) {
        this.mClientId = mClientId;
    }

    public String getUniqueId() {
        return mUniqueId;
    }

    public void setUniqueId(String mUniqueId) {
        this.mUniqueId = mUniqueId;
    }

    public String getDisplayableId() {
        return mDisplayableId;
    }

    public void setDisplayableId(String mDisplayableId) {
        this.mDisplayableId = mDisplayableId;
    }

    public String getTenantId() {
        return mTenantId;
    }

    public void setTenantId(String mTenantId) {
        this.mTenantId = mTenantId;
    }

    public Date getExpiresOn() {
        return mExpiresOn;
    }

    public void setExpiresOn(Date mExpiresOn) {
        this.mExpiresOn = mExpiresOn;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String mToken) {
        this.mToken = mToken;
    }

    public String getProfileInfo() {
        return mProfileInfo;
    }

    public void setProfileInfo(String mProfileInfo) {
        this.mProfileInfo = mProfileInfo;
    }

    public String getFamilyName() {
        return mFamilyName;
    }

    public void setFamilyName(String mFamilyName) {
        this.mFamilyName = mFamilyName;
    }

    public String getGivenName() {
        return mGivenName;
    }

    public void setGivenName(String mGivenName) {
        this.mGivenName = mGivenName;
    }

    public String getIdentityProvider() {
        return mIdentityProvider;
    }

    public void setIdentityProvider(String mIdentityProvider) {
        this.mIdentityProvider = mIdentityProvider;
    }

    public String getRawIdToken() {
        return mRawIdToken;
    }

    public void setRawIdToken(String mRawIdToken) {
        this.mRawIdToken = mRawIdToken;
    }
}
