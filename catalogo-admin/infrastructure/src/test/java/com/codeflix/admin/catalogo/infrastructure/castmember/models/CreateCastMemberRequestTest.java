package com.codeflix.admin.catalogo.infrastructure.castmember.models;

import com.codeflix.admin.catalogo.JacksonTest;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
public class CreateCastMemberRequestTest {

    @Autowired
    private JacksonTester<CreateCastMemberRequest> json;

    @Test
    public void testUnmarshall() throws Exception {
        final var expectedName = "Lucas";
        final var expectedType = CastMemberType.ACTOR.toString();

        final var json = """
                {
                  "name": "%s",
                  "type": "%s"
                }
                """.formatted(expectedName, expectedType);

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("type", expectedType);
    }
}
