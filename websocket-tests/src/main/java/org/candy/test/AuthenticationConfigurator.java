package org.candy.test;

import jakarta.websocket.ClientEndpointConfig;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * AuthenticationConfigurator
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/7/4
 */
public class AuthenticationConfigurator extends ClientEndpointConfig.Configurator {

    private final static String bizSystemCode = "abc";
    private final static String username = "123";
    private final static String password = "12345";

    @Override
    public void beforeRequest(Map<String, List<String>> headers) {
        headers.put("Biz-System", List.of(bizSystemCode));
        headers.put("Authorization", List.of("WSSE profile=\"UsernameToken\""));
        headers.put("X-WSSE", List.of(buildWSSEHeader()));
    }

    private String buildWSSEHeader() {
        WSSEHeader header = WSSEHeader.newBuilder()
                .nonce(getNonce())
                .created(getCreated())
                .username(username)
                .password(password)
                .build();
        return header.header();
    }

    private String getNonce() {
        return UUID.randomUUID().toString();
    }

    private String getCreated() {
        return Instant.now().toString();
    }

    private record WSSEHeader(String header) {

        static Builder newBuilder() {
                return new Builder();
            }

            static class Builder {

                private String username;
                private String password;
                private String nonce;
                private String created;

                Builder username(String username) {
                    this.username = username;
                    return this;
                }

                Builder password(String password) {
                    this.password = password;
                    return this;
                }

                Builder nonce(String nonce) {
                    this.nonce = nonce;
                    return this;
                }

                Builder created(String created) {
                    this.created = created;
                    return this;
                }

                WSSEHeader build() {
                    try {
                        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                        String passwordDigestSource = nonce + created + password;
                        messageDigest.update(passwordDigestSource.getBytes(StandardCharsets.UTF_8));
                        String passwordDigest = Base64.getEncoder().encodeToString(messageDigest.digest());
                        String header = "UsernameToken Username=\"" + username
                                + "\", PasswordDigest=\"" + passwordDigest
                                + "\", Nonce=\"" + nonce
                                + "\", Created=\"" + created
                                + "\"";
                        return new WSSEHeader(header);
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        }
}
