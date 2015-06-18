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
    
    private UserInfo mUserInfo = new UserInfo();
    
    private String mTenantId;
    
    /**
     * This time is GMT.
     */
    private Date mExpiresOn;

    private String mToken;

    private String mProfileInfo;
    
    private String mIdentityProvider;
    
    private String mRawIdToken;
    
    private String mRefreshToken;
    
    private boolean mIsMultiResourceRefresh;
    
    /**
     * Construct default cache item.
     */
    public TokenCacheItem() {
        mUserInfo = new UserInfo();
    }

    TokenCacheItem(final CacheKey request, final AuthenticationResult result) {
        if (request != null) {
            mAuthority = request.getAuthority();
            mClientId = request.getClientId();
            mScope = result.getScopesInResponse();
            mTenantId = result.getTenantId();
            mExpiresOn = result.getExpiresOn();
            mToken = result.getAccessToken();
            mProfileInfo = result.getIdToken();
            mRefreshToken = result.getRefreshToken();
            if( result.getUserInfo() != null){
                mUserInfo = result.getUserInfo();
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

    boolean match(CacheKey key)
    {
        // Use all the key fields to check match.
         return (key.getAuthority().equalsIgnoreCase(this.mAuthority) && StringExtensions.createStringFromArray(key.getScope(), "").equalsIgnoreCase(StringExtensions.createStringFromArray(this.mScope, ""))
                 && key.getClientId().equalsIgnoreCase(this.mClientId)
                 && key.getUniqueId().equalsIgnoreCase(this.getUserInfo().getUniqueId())
                 && key.getDisplayableId().equalsIgnoreCase(this.getUserInfo().getDisplayableId()));
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

    String getRefreshToken() {
        return mRefreshToken;
    }

    void setRefreshToken(String refreshToken) {
        this.mRefreshToken = refreshToken;
    }

    public UserInfo getUserInfo() {
        if(mUserInfo == null){
            mUserInfo = new UserInfo();
        }
        
        return mUserInfo;
    }

    public void setUserInfo(UserInfo mUserInfo) {
        this.mUserInfo = mUserInfo;
    }

    public boolean isMultiResourceRefresh() {
        return mIsMultiResourceRefresh;
    }

    public void setIsMultiResourceRefresh(boolean mIsMultiResourceRefresh) {
        this.mIsMultiResourceRefresh = mIsMultiResourceRefresh;
    }
}
