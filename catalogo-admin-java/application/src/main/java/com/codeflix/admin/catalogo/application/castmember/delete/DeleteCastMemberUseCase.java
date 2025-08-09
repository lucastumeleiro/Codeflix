package com.codeflix.admin.catalogo.application.castmember.delete;

import com.codeflix.admin.catalogo.application.UnitUseCase;

public sealed abstract class DeleteCastMemberUseCase
        extends UnitUseCase<String>
        permits DefaultDeleteCastMemberUseCase {
}
