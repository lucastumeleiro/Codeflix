package com.codeflix.admin.catalogo.infrastructure.castmember.models;

import com.codeflix.admin.catalogo.domain.castmember.CastMemberType;

public record CreateCastMemberRequest(String name, CastMemberType type) {
}
