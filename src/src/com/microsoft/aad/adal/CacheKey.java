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
import java.util.Locale;

/**
 * CacheKey will be the object for key.
 */
final class CacheKey implements Serializable {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 8067972995583126404L;

    private String mAuthority;

    private String[] mScope;

    private String mClientId;

    private String mUniqueId;

    private String mDisplayableId;

    private CacheKey() {
    }

    /**
     * @param authority URL of the authenticating authority
     * @param resource resource identifier
     * @param clientId client identifier
     * @param isMultiResourceRefreshToken true/false for refresh token type
     * @param userId userid provided from {@link UserInfo}
     * @return CacheKey to use in saving token
     */
    public static CacheKey createCacheKey(String authority, String[] scope, String clientId,
            String uniqueId, String displayableId) {

        if (authority == null) {
            throw new IllegalArgumentException("authority");
        }

        if (clientId == null) {
            throw new IllegalArgumentException("clientId");
        }

        CacheKey key = new CacheKey();
        key.mAuthority = authority.toLowerCase(Locale.US);
        if (key.mAuthority.endsWith("/")) {
            key.mAuthority = (String)key.mAuthority.subSequence(0, key.mAuthority.length() - 1);
        }

        key.mClientId = clientId.toLowerCase(Locale.US);
        key.mScope = scope.clone();
        key.mUniqueId = uniqueId != null ? uniqueId : "";
        key.mDisplayableId = displayableId != null ? displayableId : "";
        return key;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this.getClass() != obj.getClass()) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        final CacheKey rhs = (CacheKey)obj;
        return this.mAuthority.equalsIgnoreCase(rhs.mAuthority) 
                && this.mClientId.equalsIgnoreCase(rhs.mClientId)
                && StringExtensions.createStringFromArray(this.mScope, "").equalsIgnoreCase(StringExtensions.createStringFromArray(rhs.mScope, ""))
                && this.mUniqueId.equalsIgnoreCase(rhs.mUniqueId)
                && this.mDisplayableId.equalsIgnoreCase(rhs.mDisplayableId);
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = hash*17 + this.mAuthority.hashCode();
        hash = hash*17 + this.mClientId.hashCode();
        hash = hash*17 + StringExtensions.createStringFromArray(this.mScope, "").hashCode();
        hash = hash*17 + this.mUniqueId.hashCode();
        hash = hash*17 + this.mDisplayableId.hashCode();
        return hash; 
    }
    
    /**
     * Gets Authority.
     * 
     * @return Authority
     */
    public String getAuthority() {
        return mAuthority;
    }

    /**
     * Gets Resource.
     * 
     * @return Resource
     */
    public String[] getScope() {
        return mScope;
    }

    /**
     * Gets ClientId.
     * 
     * @return ClientId
     */
    public String getClientId() {
        return mClientId;
    }

    /**
     * Gets UniqueId.
     * 
     * @return UniqueId
     */
    public String getUniqueId() {
        return mUniqueId;
    }
    
    /**
     * Gets DisplayableId.
     * @return
     */
    public String getDisplayableId() {
        return mDisplayableId;
    }

    public static CacheKey createCacheKey(TokenCacheItem item) {
        // TODO Auto-generated method stub
        return null;
    }
}
