package com.codeflix.admin.catalogo.infrastructure.castmember.models;

import com.codeflix.admin.catalogo.domain.castmember.CastMemberType;

public record UpdateCastMemberRequest(String name, CastMemberType type) {
}
