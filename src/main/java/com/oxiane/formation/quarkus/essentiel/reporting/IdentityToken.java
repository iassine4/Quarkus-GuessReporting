package com.oxiane.formation.quarkus.essentiel.reporting;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequestScoped
public class IdentityToken {

    @Inject
    private JsonWebToken idToken;

    private List<String> computedRoles;

    private static final String ROLE_PATH = "realm_access/roles";

    public String getName() {
        return Stream.of("given_name", "family_name")
                .map(claim -> idToken.claim(claim))
                .filter(Optional::isPresent)
                .map(t -> t.get().toString())
                .collect(Collectors.joining(" "));
    }

    public String getId() {
        return idToken.getSubject();
    }

    public String getAvatar() {
        return idToken.claim("picture").orElse("").toString();
    }

    public String getEmail() {
        return Objects.toString(idToken.getClaim("email"));
    }

    public List<String> getRoles() {
        if (computedRoles == null) {
            computedRoles = parseRoles();
        }
        return computedRoles;
    }

    public boolean isParent() {
        return getRoles().contains("parent");
    }

    private List<String> parseRoles() {
        String[] rolePathSteps = ROLE_PATH.split("/");
        JsonObject jsonObject = null;
        JsonArray jsonArray = null;

        for (String step : rolePathSteps) {
            if (jsonObject == null) {
                jsonObject = idToken.getClaim(step);
            } else {
                jsonArray = jsonObject.getJsonArray(step);
            }
        }

        if (jsonArray != null) {
            return jsonArray.stream()
                    .map(JsonValue::toString)
                    .map(s -> s.replaceAll("\"", ""))
                    .toList();
        }

        return Collections.emptyList();
    }
}