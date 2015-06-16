package com.microsoft.aad.adal;

class TokenCacheKey {
   
    private String mAuthority;
    private String[] mScope;
    private String mClientId;
    private String mUniqueId;
    private String mDisplayableId;
    
    
    public TokenCacheKey(String authority, String[] scope, String clientId, UserInfo userInfo)
{
       init(authority, scope, clientId, (userInfo != null) ? userInfo.getUniqueId() : null, (userInfo != null) ? userInfo.getDisplayableId() : null);
}

  TokenCacheKey(String authority, String[] scope, String clientId, String uniqueId, String displayableId)
{
      init(authority, scope, clientId, uniqueId, displayableId);
}
  
  private void init(String authority, String[] scope, String clientId, String uniqueId, String displayableId){
      this.mAuthority = authority;
      this.mScope = scope;
      this.mClientId = clientId;
      this.mUniqueId = uniqueId;
      this.mDisplayableId = displayableId;
  }

  // TODO add other methods
}
