package com.codeflix.admin.catalogo.application;

import com.codeflix.admin.catalogo.domain.Identifier;
import com.codeflix.admin.catalogo.domain.castmember.CastMember;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberType;
import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.genre.Genre;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
public abstract class UseCaseTest implements BeforeEachCallback {

    @Override
    public void beforeEach(final ExtensionContext context) {
        Mockito.reset(getMocks().toArray());
    }

    protected abstract List<Object> getMocks();

    protected Set<String> asString(final Set<? extends Identifier> ids) {
        return ids.stream()
                .map(Identifier::getValue)
                .collect(Collectors.toSet());
    }

    protected List<String> asString(final List<? extends Identifier> ids) {
        return ids.stream()
                .map(Identifier::getValue)
                .toList();
    }

    public static final class Fixture {

        private static final Faker FAKER = new Faker();

        public static String name() {
            return FAKER.name().fullName();
        }

        public static Integer year() {
            return FAKER.random().nextInt(2020, 2030);
        }

        public static Double duration() {
            return FAKER.options().option(120.0, 15.5, 35.5, 10.0, 2.0);
        }

        public static boolean bool() {
            return FAKER.bool().bool();
        }

        public static String title() {
            return FAKER.options().option(
                    "System Design no Mercado Livre na prática",
                    "Não cometa esses erros ao trabalhar com Microsserviços",
                    "Testes de Mutação. Você não testa seu software corretamente"
            );
        }

        public static String checksum() {
            return "03fe62de";
        }

        public static final class Categories {

            private static final Category AULAS =
                    Category.newCategory("Aulas", "Some description", true);

            private static final Category LIVES =
                    Category.newCategory("Lives", "Some description", true);

            public static Category aulas() {
                return AULAS.clone();
            }

            public static Category lives() {
                return LIVES.clone();
            }
        }

        public static final class CastMembers {

            private static final CastMember LUCAS =
                    CastMember.newMember("Lucas", CastMemberType.ACTOR);

            private static final CastMember GEOVANA =
                    CastMember.newMember("Geovana", CastMemberType.ACTOR);

            public static CastMemberType type() {
                return FAKER.options().option(CastMemberType.values());
            }

            public static CastMember wesley() {
                return CastMember.with(LUCAS);
            }

            public static CastMember gabriel() {
                return CastMember.with(GEOVANA);
            }
        }

        public static final class Genres {

            private static final Genre TECH =
                    Genre.newGenre("Technology", true);

            private static final Genre BUSINESS =
                    Genre.newGenre("Business", true);

            public static Genre tech() {
                return Genre.with(TECH);
            }

            public static Genre business() {
                return Genre.with(BUSINESS);
            }
        }
    }
}
