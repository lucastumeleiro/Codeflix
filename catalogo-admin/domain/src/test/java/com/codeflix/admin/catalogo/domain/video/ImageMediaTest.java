package com.codeflix.admin.catalogo.domain.video;

import com.codeflix.admin.catalogo.domain.UnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ImageMediaTest extends UnitTest {

    @Test
    public void givenValidParams_whenCallsNewImage_ShouldReturnInstance() {
        final var expectedChecksum = "abc";
        final var expectedName = "Banner.png";
        final var expectedLocation = "/images/ac";

        final var actualImage =
                ImageMedia.with(expectedChecksum, expectedName, expectedLocation);

        Assertions.assertNotNull(actualImage);
        Assertions.assertEquals(expectedChecksum, actualImage.checksum());
        Assertions.assertEquals(expectedName, actualImage.name());
        Assertions.assertEquals(expectedLocation, actualImage.location());
    }

    @Test
    public void givenTwoImagesWithSameChecksumAndLocation_whenCallsEquals_ShouldReturnTrue() {
        final var expectedChecksum = "abc";
        final var expectedLocation = "/images/ac";

        final var img1 =
                ImageMedia.with(expectedChecksum, "Random", expectedLocation);

        final var img2 =
                ImageMedia.with(expectedChecksum, "Simple", expectedLocation);

        Assertions.assertEquals(img1, img2);
        Assertions.assertNotSame(img1, img2);
    }

    @Test
    public void givenInvalidParams_whenCallsWith_ShouldReturnError() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> ImageMedia.with(null, "Random", "/images")
        );

        Assertions.assertThrows(
                NullPointerException.class,
                () -> ImageMedia.with("abc", null, "/images")
        );

        Assertions.assertThrows(
                NullPointerException.class,
                () -> ImageMedia.with("abc", "Random", null)
        );
    }
}