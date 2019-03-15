package org.nerdcoding.firstuploader.auth.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Customer REST endpoint for handling tokens. Only a extension for the default spring security token controller.
 */
@RestController
public class TokenController {

    private final TokenStore tokenStore;

    @Autowired
    public TokenController(final TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    /**
     * Deletes access_token and refresh_token from token store and thereby performs a logout.
     *
     * @param accessToken The access_token to remove from token store.
     * @param refreshToken The refresh_token to remove from token store.
     * @return HTTP 204 (NO CONTENT) when tokens were removed from token store. HTTP 404 (NOT FOUND) when tokens both
     *      don't exists in token store. And HTTP 400 (BAD REQUEST) when clint didn't provide valid tokens.
     */
    @DeleteMapping("/token")
    public ResponseEntity<Void> revokeAccessToken(
            @RequestParam("access_token") final String accessToken,
            @RequestParam("refresh_token") final String refreshToken
    ) {
        if (accessToken == null || refreshToken == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(accessToken);
        final OAuth2RefreshToken oAuth2RefreshToken = tokenStore.readRefreshToken(refreshToken);

        final ResponseEntity<Void> result;
        if (oAuth2AccessToken != null && oAuth2RefreshToken != null) {
            tokenStore.removeAccessToken(oAuth2AccessToken);
            tokenStore.removeRefreshToken(oAuth2RefreshToken);
            result = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else if (oAuth2RefreshToken != null) {
            tokenStore.removeRefreshToken(oAuth2RefreshToken);
            result = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else if (oAuth2AccessToken != null) {
            tokenStore.removeAccessToken(oAuth2AccessToken);
            result = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            result = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return result;
    }

}
