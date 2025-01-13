package com.codeflix.admin.catalogo.application.castmember.create;

import com.codeflix.admin.catalogo.application.UseCase;

public sealed abstract class CreateCastMemberUseCase extends UseCase<CreateCastMemberCommand, CreateCastMemberOutput>
        permits DefaultCreateCastMemberUseCase {
}