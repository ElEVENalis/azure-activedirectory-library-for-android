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

import android.text.TextUtils;

/**
 * Contains identifier for a user.
 */
public class UserIdentifier {

    /**
     * Indicates the type of the user.
     */
    enum UserIdentifierType {
        /**
         * When a {@link UserIdentifier} of this type is passed in a token
         * acquisition operation, the operation is guaranteed to return a token
         * issued for the user with corresponding
         * {@link UserIdentifier#getUniqueId()} or fail. This can be GUID format.
         */
        UniqueId,

        /**
         * When a {@link UserIdentifier} of this type is passed in a token
         * acquisition operation, the operation restricts cache matches to the
         * value provided and injects it as a hint in the authentication
         * experience. However the end user could overwrite that value,
         * resulting in a token issued to a different account than the one
         * specified in the {@link UserIdentifier} in input.
         */
        OptionalDisplayableId,

        /**
         * When a {@link UserIdentifier} of this type is passed in a token
         * acquisition operation, the operation is guaranteed to return a token
         * issued for the user with corresponding
         * {@link UserIdentifier#getDisplayableId()} (UPN or email) or fail.
         */
        RequiredDisplayableId
    }

    private final static String ANY_USERID = "AnyUser";

    private final static UserIdentifier sAnyUser = new UserIdentifier(ANY_USERID,
            UserIdentifierType.UniqueId);

    private UserIdentifierType mUserIdentifierType;

    private String mId;

    /**
     * Creates userid with type options.
     * 
     * @param id Identifier of the user. Uniqueid or username based on given
     *            type.
     * @param type Given id type.
     */
    public UserIdentifier(String id, UserIdentifierType type) {
        if (TextUtils.isEmpty(id)) {
            throw new IllegalArgumentException("id");
        }

        this.mId = id;
        this.mUserIdentifierType = type;
    }

    public UserIdentifierType getUserIdentifierType() {
        return mUserIdentifierType;
    }

    void setUserIdentifierType(UserIdentifierType type) {
        this.mUserIdentifierType = type;
    }

    /**
     * Represent any user identifier.
     * @return
     */
    public static UserIdentifier getAnyUser() {
        return sAnyUser;
    }

    boolean isAnyUser() {
        return this.mUserIdentifierType == sAnyUser.getUserIdentifierType()
                && this.mId.equalsIgnoreCase(ANY_USERID);
    }

    public String getUniqueId() {
        return (!isAnyUser() && this.mUserIdentifierType == UserIdentifierType.UniqueId) ? this.mId
                : null;
    }

    public String getDisplayableId() {
        return (!isAnyUser() && (this.mUserIdentifierType == UserIdentifierType.OptionalDisplayableId || this.mUserIdentifierType == UserIdentifierType.RequiredDisplayableId)) ? this.mId
                : null;
    }
}
