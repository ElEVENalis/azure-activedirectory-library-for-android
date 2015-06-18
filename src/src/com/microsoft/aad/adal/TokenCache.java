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

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.crypto.NoSuchPaddingException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Store/Retrieve TokenCacheItem from private SharedPreferences.
 * SharedPreferences saves items when it is committed in an atomic operation.
 * One more retry is attempted in case there is a lock in commit.
 */
public class TokenCache {

    private static final String SHARED_PREFERENCE_NAME = "com.microsoft.aad.adal.cache";

    private static final String TAG = "DefaultTokenCacheStore";

    SharedPreferences mPrefs;

    private Context mContext;

    private Gson mGson = new GsonBuilder()
    .registerTypeAdapter(Date.class, new DateTimeAdapter())
    .create();

    private static StorageHelper sHelper;

    private static Object sLock = new Object();
    
    /**
     * @param context {@link Context}
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    public TokenCache(Context context) throws NoSuchAlgorithmException,
            NoSuchPaddingException {
        mContext = context;
        if (context != null) {

            if (!StringExtensions.IsNullOrBlank(AuthenticationSettings.INSTANCE
                    .getSharedPrefPackageName())) {
                try {
                    // Context is created from specified packagename in order to
                    // use same file. Reading private data is only allowed if apps specify same
                    // sharedUserId. Android OS will assign same UID, if they
                    // are signed with same certificates.
                    mContext = context.createPackageContext(
                            AuthenticationSettings.INSTANCE.getSharedPrefPackageName(),
                            Context.MODE_PRIVATE);
                } catch (NameNotFoundException e) {
                    throw new IllegalArgumentException("Package name:"
                            + AuthenticationSettings.INSTANCE.getSharedPrefPackageName()
                            + " is not found");
                }
            }
            mPrefs = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
        } else {
            throw new IllegalArgumentException("Context is null");
        }

        synchronized (sLock) {
            if (sHelper == null) {
                Logger.v(TAG, "Started to initialize storage helper");
                sHelper = new StorageHelper(mContext);
                Logger.v(TAG, "Finished to initialize storage helper");
            }
        }
    }

    private String encrypt(String value) {
        try {
            return sHelper.encrypt(value);
        } catch (Exception e) {
            Logger.e(TAG, "Encryption failure", "", ADALError.ENCRYPTION_FAILED, e);
        }

        return null;
    }

    private String decrypt(CacheKey key, String value) {
        try {
            return sHelper.decrypt(value);
        } catch (Exception e) {
            Logger.e(TAG, "Decryption failure", "", ADALError.ENCRYPTION_FAILED, e);
            if (!StringExtensions.IsNullOrBlank(value)) {
                Logger.v(TAG, String.format("Decryption error for key: '%s'. Item will be removed",
                        value));
                removeItem(key);
                Logger.v(TAG, String.format("Item removed for key: '%s'", value));
            }
        }

        return null;
    }

    public void removeItem(TokenCacheItem item) {

        argumentCheck();

        if (item == null) {
            throw new IllegalArgumentException("item");
        }

        CacheKey key = CacheKey.createCacheKey(item);
        removeItem(key);
    }
    
    public List<TokenCacheItem> getAll(){
        List<TokenCacheItem> cacheItems = new ArrayList<TokenCacheItem>();
        
        // TODO update
        return cacheItems;
    }
    
    private void removeItem(CacheKey key){
        String jsonKey = mGson.toJson(key);
        
        Map<String,String> allItems = (Map<String, String>)mPrefs.getAll();
        
        // TODO iterate over and delete
    }

    void setItem(CacheKey key, TokenCacheItem item) {

        argumentCheck();

        if (key == null) {
            throw new IllegalArgumentException("key");
        }

        if (item == null) {
            throw new IllegalArgumentException("item");
        }

        String json = mGson.toJson(item);
        String encrypted = encrypt(json);
        if (encrypted != null) {
            Editor prefsEditor = mPrefs.edit();
            prefsEditor.putString(mGson.toJson(key), encrypted);

            // apply will do Async disk write operation.
            prefsEditor.apply();
        } else {
            Logger.e(TAG, "Encrypted output is null", "", ADALError.ENCRYPTION_FAILED);
        }
    }

    public void clear() {

        argumentCheck();
        Editor prefsEditor = mPrefs.edit();
        prefsEditor.clear();
        // apply will do Async disk write operation.
        prefsEditor.apply();
    }

    private void argumentCheck() {
        if (mContext == null) {
            throw new AuthenticationException(ADALError.DEVELOPER_CONTEXT_IS_NOT_PROVIDED);
        }

        if (mPrefs == null) {
            throw new AuthenticationException(ADALError.DEVICE_SHARED_PREF_IS_NOT_AVAILABLE);
        }
    }

    private boolean isAboutToExpire(Date expires) {
        Date validity = getTokenValidityTime().getTime();

        if (expires != null && expires.before(validity)) {
            return true;
        }

        return false;
    }

    private static final int TOKEN_VALIDITY_WINDOW = 10;

    private static Calendar getTokenValidityTime() {
        Calendar timeAhead = Calendar.getInstance();
        timeAhead.add(Calendar.SECOND, TOKEN_VALIDITY_WINDOW);
        return timeAhead;
    }

    boolean contains(String key) {
        argumentCheck();

        if (key == null) {
            throw new IllegalArgumentException("key");
        }

        return mPrefs.contains(key);
    }
}
