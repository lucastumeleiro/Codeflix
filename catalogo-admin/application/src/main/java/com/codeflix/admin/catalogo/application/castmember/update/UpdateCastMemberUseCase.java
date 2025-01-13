package com.codeflix.admin.catalogo.application.castmember.update;

import com.codeflix.admin.catalogo.application.UseCase;

public sealed abstract class UpdateCastMemberUseCase
        extends UseCase<UpdateCastMemberCommand, UpdateCastMemberOutput>
        permits DefaultUpdateCastMemberUseCase {
}
