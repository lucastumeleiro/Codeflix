package com.codeflix.admin.catalogo.infrastructure.api;

import com.codeflix.admin.catalogo.ApiTest;
import com.codeflix.admin.catalogo.domain.Fixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.codeflix.admin.catalogo.ControllerTest;
import com.codeflix.admin.catalogo.application.video.create.CreateVideoCommand;
import com.codeflix.admin.catalogo.application.video.create.CreateVideoOutput;
import com.codeflix.admin.catalogo.application.video.create.CreateVideoUseCase;
import com.codeflix.admin.catalogo.application.video.delete.DeleteVideoUseCase;
import com.codeflix.admin.catalogo.application.video.media.get.GetMediaCommand;
import com.codeflix.admin.catalogo.application.video.media.get.GetMediaUseCase;
import com.codeflix.admin.catalogo.application.video.media.get.MediaOutput;
import com.codeflix.admin.catalogo.application.video.media.upload.UploadMediaCommand;
import com.codeflix.admin.catalogo.application.video.media.upload.UploadMediaOutput;
import com.codeflix.admin.catalogo.application.video.media.upload.UploadMediaUseCase;
import com.codeflix.admin.catalogo.application.video.retrieve.get.GetVideoUseCase;
import com.codeflix.admin.catalogo.application.video.retrieve.get.VideoOutput;
import com.codeflix.admin.catalogo.application.video.retrieve.list.ListVideosUseCase;
import com.codeflix.admin.catalogo.application.video.retrieve.list.VideoListOutput;
import com.codeflix.admin.catalogo.application.video.update.UpdateVideoCommand;
import com.codeflix.admin.catalogo.application.video.update.UpdateVideoOutput;
import com.codeflix.admin.catalogo.application.video.update.UpdateVideoUseCase;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberID;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalogo.domain.exceptions.NotificationException;
import com.codeflix.admin.catalogo.domain.genre.GenreID;
import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.domain.validation.Error;
import com.codeflix.admin.catalogo.domain.video.*;
import com.codeflix.admin.catalogo.infrastructure.video.models.CreateVideoRequest;
import com.codeflix.admin.catalogo.infrastructure.video.models.UpdateVideoRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.codeflix.admin.catalogo.domain.Utils.CollectionUtils.mapTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = VideoAPI.class)
public class VideoAPITest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private CreateVideoUseCase createVideoUseCase;
    @MockitoBean
    private GetVideoUseCase getVideoUseCase;
    @MockitoBean
    private UpdateVideoUseCase updateVideoUseCase;
    @MockitoBean
    private DeleteVideoUseCase deleteVideoUseCase;
    @MockitoBean
    private ListVideosUseCase listVideosUseCase;
    @MockitoBean
    private GetMediaUseCase getMediaUseCase;
    @MockitoBean
    private UploadMediaUseCase uploadMediaUseCase;

    @Test
    public void givenValidCommand_whenCallsCreateFull_shouldReturnAnId() throws Exception {
        final var lucas = Fixture.CastMembers.lucas();
        final var aulas = Fixture.Categories.aulas();
        final var tech = Fixture.Genres.tech();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas.getId().getValue());
        final var expectedGenres = Set.of(tech.getId().getValue());
        final var expectedMembers = Set.of(lucas.getId().getValue());

        final var expectedVideo =
                new MockMultipartFile("video_file", "video.mp4", "video/mp4", "VIDEO".getBytes());
        final var expectedTrailer =
                new MockMultipartFile("trailer_file", "trailer.mp4", "video/mp4", "TRAILER".getBytes());
        final var expectedBanner =
                new MockMultipartFile("banner_file", "banner.jpg", "image/jpg", "BANNER".getBytes());
        final var expectedThumb =
                new MockMultipartFile("thumb_file", "thumbnail.jpg", "image/jpg", "THUMB".getBytes());
        final var expectedThumbHalf =
                new MockMultipartFile("thumb_half_file", "thumbnailHalf.jpg", "image/jpg", "THUMBHALF".getBytes());

        when(createVideoUseCase.execute(any()))
                .thenReturn(new CreateVideoOutput(expectedId.getValue()));

        final var request = multipart("/videos")
                .file(expectedVideo)
                .file(expectedTrailer)
                .file(expectedBanner)
                .file(expectedThumb)
                .file(expectedThumbHalf)
                .with(ApiTest.VIDEOS_JWT)
                .param("title", expectedTitle)
                .param("description", expectedDescription)
                .param("year_launched", String.valueOf(expectedLaunchYear.getValue()))
                .param("duration", expectedDuration.toString())
                .param("opened", String.valueOf(expectedOpened))
                .param("published", String.valueOf(expectedPublished))
                .param("rating", expectedRating.getName())
                .param("cast_members_id", lucas.getId().getValue())
                .param("categories_id", aulas.getId().getValue())
                .param("genres_id", tech.getId().getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        this.mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/videos/" + expectedId.getValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

        final var captor = ArgumentCaptor.forClass(CreateVideoCommand.class);

        verify(createVideoUseCase).execute(captor.capture());

        final var actualCommand = captor.getValue();
        Assertions.assertEquals(expectedTitle, actualCommand.title());
        Assertions.assertEquals(expectedDescription, actualCommand.description());
        Assertions.assertEquals(expectedLaunchYear.getValue(), actualCommand.launchedAt());
        Assertions.assertEquals(expectedDuration, actualCommand.duration());
        Assertions.assertEquals(expectedOpened, actualCommand.opened());
        Assertions.assertEquals(expectedPublished, actualCommand.published());
        Assertions.assertEquals(expectedRating.getName(), actualCommand.rating());
        Assertions.assertEquals(expectedCategories, actualCommand.categories());
        Assertions.assertEquals(expectedGenres, actualCommand.genres());
        Assertions.assertEquals(expectedMembers, actualCommand.members());
        Assertions.assertEquals(expectedVideo.getOriginalFilename(), actualCommand.getVideo().get().name());
        Assertions.assertEquals(expectedTrailer.getOriginalFilename(), actualCommand.getTrailer().get().name());
        Assertions.assertEquals(expectedBanner.getOriginalFilename(), actualCommand.getBanner().get().name());
        Assertions.assertEquals(expectedThumb.getOriginalFilename(), actualCommand.getThumbnail().get().name());
        Assertions.assertEquals(expectedThumbHalf.getOriginalFilename(), actualCommand.getThumbnailHalf().get().name());
    }

    @Test
    public void givenInvalidCommand_whenCallsCreateFull_shouldReturnError() throws Exception {
        final var expectedErrorMessage = "title is required";

        when(createVideoUseCase.execute(any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        final var request = multipart("/videos")
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        final var response = this.mvc.perform(request);

        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenValidCommand_whenCallsCreatePartial_shouldReturnId() throws Exception {
        final var lucas = Fixture.CastMembers.lucas();
        final var aulas = Fixture.Categories.aulas();
        final var tech = Fixture.Genres.tech();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas.getId().getValue());
        final var expectedGenres = Set.of(tech.getId().getValue());
        final var expectedMembers = Set.of(lucas.getId().getValue());

        final var command = new CreateVideoRequest(
                expectedTitle,
                expectedDescription,
                expectedDuration,
                expectedLaunchYear.getValue(),
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedMembers,
                expectedCategories,
                expectedGenres
        );

        when(createVideoUseCase.execute(any()))
                .thenReturn(new CreateVideoOutput(expectedId.getValue()));


        final var request = post("/videos")
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(command));

        this.mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/videos/" + expectedId.getValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

        final var captor = ArgumentCaptor.forClass(CreateVideoCommand.class);

        verify(createVideoUseCase).execute(captor.capture());

        final var actualCommand = captor.getValue();
        Assertions.assertEquals(expectedTitle, actualCommand.title());
        Assertions.assertEquals(expectedDescription, actualCommand.description());
        Assertions.assertEquals(expectedLaunchYear.getValue(), actualCommand.launchedAt());
        Assertions.assertEquals(expectedDuration, actualCommand.duration());
        Assertions.assertEquals(expectedOpened, actualCommand.opened());
        Assertions.assertEquals(expectedPublished, actualCommand.published());
        Assertions.assertEquals(expectedRating.getName(), actualCommand.rating());
        Assertions.assertEquals(expectedCategories, actualCommand.categories());
        Assertions.assertEquals(expectedGenres, actualCommand.genres());
        Assertions.assertEquals(expectedMembers, actualCommand.members());
        Assertions.assertTrue(actualCommand.getVideo().isEmpty());
        Assertions.assertTrue(actualCommand.getTrailer().isEmpty());
        Assertions.assertTrue(actualCommand.getBanner().isEmpty());
        Assertions.assertTrue(actualCommand.getThumbnail().isEmpty());
        Assertions.assertTrue(actualCommand.getThumbnailHalf().isEmpty());
    }

    @Test
    public void givenEmptyBody_whenCallsCreatePartial_shouldReturnError() throws Exception {
        final var request = post("/videos")
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        response.andExpect(status().isBadRequest());
    }

    @Test
    public void givenInvalidCommand_whenCallsCreatePartial_shouldReturnError() throws Exception {
        final var expectedErrorMessage = "title is required";

        when(createVideoUseCase.execute(any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        final var request = post("/videos")
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "Olá Mundo!"
                        }
                        """);

        final var response = this.mvc.perform(request);

        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenValidId_whenCallsGetById_shouldReturnVideo() throws Exception {
        final var lucas = Fixture.CastMembers.lucas();
        final var aulas = Fixture.Categories.aulas();
        final var tech = Fixture.Genres.tech();

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas.getId().getValue());
        final var expectedGenres = Set.of(tech.getId().getValue());
        final var expectedMembers = Set.of(lucas.getId().getValue());

        final var expectedVideo = Fixture.Videos.audioVideo(VideoMediaType.VIDEO);
        final var expectedTrailer = Fixture.Videos.audioVideo(VideoMediaType.TRAILER);
        final var expectedBanner = Fixture.Videos.image(VideoMediaType.BANNER);
        final var expectedThumb = Fixture.Videos.image(VideoMediaType.THUMBNAIL);
        final var expectedThumbHalf = Fixture.Videos.image(VideoMediaType.THUMBNAIL_HALF);

        final var video = Video.newVideo(
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchYear,
                        expectedDuration,
                        expectedOpened,
                        expectedPublished,
                        expectedRating,
                        mapTo(expectedCategories, CategoryID::from),
                        mapTo(expectedGenres, GenreID::from),
                        mapTo(expectedMembers, CastMemberID::from)
                )
                .updateVideoMedia(expectedVideo)
                .updateTrailerMedia(expectedTrailer)
                .updateBannerMedia(expectedBanner)
                .updateThumbnailMedia(expectedThumb)
                .updateThumbnailHalfMedia(expectedThumbHalf);

        final var expectedId = video.getId().getValue();

        when(getVideoUseCase.execute(any()))
                .thenReturn(VideoOutput.from(video));

        final var request = get("/videos/{id}", expectedId)
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.title", equalTo(expectedTitle)))
                .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                .andExpect(jsonPath("$.year_launched", equalTo(expectedLaunchYear.getValue())))
                .andExpect(jsonPath("$.duration", equalTo(expectedDuration)))
                .andExpect(jsonPath("$.opened", equalTo(expectedOpened)))
                .andExpect(jsonPath("$.published", equalTo(expectedPublished)))
                .andExpect(jsonPath("$.rating", equalTo(expectedRating.getName())))
                .andExpect(jsonPath("$.created_at", equalTo(video.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(video.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.banner.id", equalTo(expectedBanner.id())))
                .andExpect(jsonPath("$.banner.name", equalTo(expectedBanner.name())))
                .andExpect(jsonPath("$.banner.location", equalTo(expectedBanner.location())))
                .andExpect(jsonPath("$.banner.checksum", equalTo(expectedBanner.checksum())))
                .andExpect(jsonPath("$.thumbnail.id", equalTo(expectedThumb.id())))
                .andExpect(jsonPath("$.thumbnail.name", equalTo(expectedThumb.name())))
                .andExpect(jsonPath("$.thumbnail.location", equalTo(expectedThumb.location())))
                .andExpect(jsonPath("$.thumbnail.checksum", equalTo(expectedThumb.checksum())))
                .andExpect(jsonPath("$.thumbnail_half.id", equalTo(expectedThumbHalf.id())))
                .andExpect(jsonPath("$.thumbnail_half.name", equalTo(expectedThumbHalf.name())))
                .andExpect(jsonPath("$.thumbnail_half.location", equalTo(expectedThumbHalf.location())))
                .andExpect(jsonPath("$.thumbnail_half.checksum", equalTo(expectedThumbHalf.checksum())))
                .andExpect(jsonPath("$.video.id", equalTo(expectedVideo.id())))
                .andExpect(jsonPath("$.video.name", equalTo(expectedVideo.name())))
                .andExpect(jsonPath("$.video.checksum", equalTo(expectedVideo.checksum())))
                .andExpect(jsonPath("$.video.location", equalTo(expectedVideo.rawLocation())))
                .andExpect(jsonPath("$.video.encoded_location", equalTo(expectedVideo.encodedLocation())))
                .andExpect(jsonPath("$.video.status", equalTo(expectedVideo.status().name())))
                .andExpect(jsonPath("$.trailer.id", equalTo(expectedTrailer.id())))
                .andExpect(jsonPath("$.trailer.name", equalTo(expectedTrailer.name())))
                .andExpect(jsonPath("$.trailer.checksum", equalTo(expectedTrailer.checksum())))
                .andExpect(jsonPath("$.trailer.location", equalTo(expectedTrailer.rawLocation())))
                .andExpect(jsonPath("$.trailer.encoded_location", equalTo(expectedTrailer.encodedLocation())))
                .andExpect(jsonPath("$.trailer.status", equalTo(expectedTrailer.status().name())))
                .andExpect(jsonPath("$.categories_id", equalTo(new ArrayList(expectedCategories))))
                .andExpect(jsonPath("$.genres_id", equalTo(new ArrayList(expectedGenres))))
                .andExpect(jsonPath("$.cast_members_id", equalTo(new ArrayList(expectedMembers))));
    }

    @Test
    public void givenInvalidId_whenCallsGetById_shouldReturnNotFound() throws Exception {
        final var expectedId = VideoID.unique();
        final var expectedErrorMessage = "Video with ID %s was not found".formatted(expectedId.getValue());

        when(getVideoUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Video.class, expectedId));

        final var request = get("/videos/{id}", expectedId)
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenValidCommand_whenCallsUpdateVideo_shouldReturnVideoId() throws Exception {
        final var lucas = Fixture.CastMembers.lucas();
        final var aulas = Fixture.Categories.aulas();
        final var tech = Fixture.Genres.tech();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas.getId().getValue());
        final var expectedGenres = Set.of(tech.getId().getValue());
        final var expectedMembers = Set.of(lucas.getId().getValue());

        final var command = new UpdateVideoRequest(
                expectedTitle,
                expectedDescription,
                expectedDuration,
                expectedLaunchYear.getValue(),
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedMembers,
                expectedCategories,
                expectedGenres
        );

        when(updateVideoUseCase.execute(any()))
                .thenReturn(new UpdateVideoOutput(expectedId.getValue()));


        final var request = put("/videos/{id}", expectedId.getValue())
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(command));

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(header().string("Location", "/videos/" + expectedId.getValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

        final var captor = ArgumentCaptor.forClass(UpdateVideoCommand.class);

        verify(updateVideoUseCase).execute(captor.capture());

        final var actualCommand = captor.getValue();

        Assertions.assertEquals(expectedTitle, actualCommand.title());
        Assertions.assertEquals(expectedDescription, actualCommand.description());
        Assertions.assertEquals(expectedLaunchYear.getValue(), actualCommand.launchedAt());
        Assertions.assertEquals(expectedDuration, actualCommand.duration());
        Assertions.assertEquals(expectedOpened, actualCommand.opened());
        Assertions.assertEquals(expectedPublished, actualCommand.published());
        Assertions.assertEquals(expectedRating.getName(), actualCommand.rating());
        Assertions.assertEquals(expectedCategories, actualCommand.categories());
        Assertions.assertEquals(expectedGenres, actualCommand.genres());
        Assertions.assertEquals(expectedMembers, actualCommand.members());
        Assertions.assertTrue(actualCommand.getVideo().isEmpty());
        Assertions.assertTrue(actualCommand.getTrailer().isEmpty());
        Assertions.assertTrue(actualCommand.getBanner().isEmpty());
        Assertions.assertTrue(actualCommand.getThumbnail().isEmpty());
        Assertions.assertTrue(actualCommand.getThumbnailHalf().isEmpty());
    }

    @Test
    public void givenInvalidCommand_whenCallsUpdateVideo_shouldReturnNotification() throws Exception {
        final var lucas = Fixture.CastMembers.lucas();
        final var aulas = Fixture.Categories.aulas();
        final var tech = Fixture.Genres.tech();

        final var expectedId = VideoID.unique();
        final var expectedErrorMessage = "'title' should not be empty";
        final var expectedErrorCount = 1;

        final var expectedTitle = "";
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas.getId().getValue());
        final var expectedGenres = Set.of(tech.getId().getValue());
        final var expectedMembers = Set.of(lucas.getId().getValue());

        final var command = new UpdateVideoRequest(
                expectedTitle,
                expectedDescription,
                expectedDuration,
                expectedLaunchYear.getValue(),
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedMembers,
                expectedCategories,
                expectedGenres
        );

        when(updateVideoUseCase.execute(any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));


        final var request = put("/videos/{id}", expectedId.getValue())
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request);

        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)))
                .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(updateVideoUseCase).execute(any());
    }

    @Test
    public void givenValidId_whenCallsDeleteById_shouldDeleteIt() throws Exception {
        final var expectedId = VideoID.unique();

        doNothing().when(deleteVideoUseCase).execute(any());

        final var request = delete("/videos/{id}", expectedId.getValue())
        .with(ApiTest.VIDEOS_JWT);

        final var response = this.mvc.perform(request);

        response.andExpect(status().isNoContent());

        verify(deleteVideoUseCase).execute(eq(expectedId.getValue()));
    }

    @Test
    public void givenValidParams_whenCallsListVideos_shouldReturnPagination() throws Exception {
        final var video = new VideoPreview(Fixture.video());

        final var expectedPage = 50;
        final var expectedPerPage = 50;
        final var expectedTerms = "Algo";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedCastMembers = "cast1";
        final var expectedGenres = "gen1";
        final var expectedCategories = "cat1";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(VideoListOutput.from(video));

        when(listVideosUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = get("/videos")
                .with(ApiTest.VIDEOS_JWT)
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .queryParam("cast_members_ids", expectedCastMembers)
                .queryParam("categories_ids", expectedCategories)
                .queryParam("genres_ids", expectedGenres)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(video.id())))
                .andExpect(jsonPath("$.items[0].title", equalTo(video.title())))
                .andExpect(jsonPath("$.items[0].description", equalTo(video.description())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(video.createdAt().toString())))
                .andExpect(jsonPath("$.items[0].updated_at", equalTo(video.updatedAt().toString())));

        final var captor = ArgumentCaptor.forClass(VideoSearchQuery.class);

        verify(listVideosUseCase).execute(captor.capture());

        final var actualQuery = captor.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedTerms, actualQuery.terms());
        Assertions.assertEquals(Set.of(CategoryID.from(expectedCategories)), actualQuery.categories());
        Assertions.assertEquals(Set.of(CastMemberID.from(expectedCastMembers)), actualQuery.castMembers());
        Assertions.assertEquals(Set.of(GenreID.from(expectedGenres)), actualQuery.genres());
    }

    @Test
    public void givenEmptyParams_whenCallsListVideosWithDefaultValues_shouldReturnPagination() throws Exception {
        final var video = new VideoPreview(Fixture.video());

        final var expectedPage = 0;
        final var expectedPerPage = 25;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(VideoListOutput.from(video));

        when(listVideosUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = get("/videos")
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(video.id())))
                .andExpect(jsonPath("$.items[0].title", equalTo(video.title())))
                .andExpect(jsonPath("$.items[0].description", equalTo(video.description())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(video.createdAt().toString())))
                .andExpect(jsonPath("$.items[0].updated_at", equalTo(video.updatedAt().toString())));

        final var captor = ArgumentCaptor.forClass(VideoSearchQuery.class);

        verify(listVideosUseCase).execute(captor.capture());

        final var actualQuery = captor.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedTerms, actualQuery.terms());
        Assertions.assertTrue(actualQuery.categories().isEmpty());
        Assertions.assertTrue(actualQuery.castMembers().isEmpty());
        Assertions.assertTrue(actualQuery.genres().isEmpty());
    }

    @Test
    public void givenValidVideoIdAndFileType_whenCallsGetMediaById_shouldReturnContent() throws Exception {
        final var expectedId = VideoID.unique();

        final var expectedMediaType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedMediaType);

        final var expectedMedia = new MediaOutput(expectedResource.content(), expectedResource.contentType(), expectedResource.name());

        when(getMediaUseCase.execute(any())).thenReturn(expectedMedia);

        final var request = get("/videos/{id}/medias/{type}", expectedId.getValue(), expectedMediaType.name())
        .with(ApiTest.VIDEOS_JWT);

        final var response = this.mvc.perform(request);

        response.andExpect(status().isOk())
                .andExpect(header().string(CONTENT_TYPE, expectedMedia.contentType()))
                .andExpect(header().string(CONTENT_LENGTH, String.valueOf(expectedMedia.content().length)))
                .andExpect(header().string(CONTENT_DISPOSITION, "attachment; filename=%s".formatted(expectedMedia.name())))
                .andExpect(content().bytes(expectedMedia.content()));

        final var captor = ArgumentCaptor.forClass(GetMediaCommand.class);

        verify(this.getMediaUseCase).execute(captor.capture());

        final var actualCommand = captor.getValue();
        Assertions.assertEquals(expectedId.getValue(), actualCommand.videoId());
        Assertions.assertEquals(expectedMediaType.name(), actualCommand.mediaType());
    }

    @Test
    public void givenValidVideoIdAndFile_whenCallsUploadMedia_shouldStoreIt() throws Exception {
        final var expectedId = VideoID.unique();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);

        final var expectedVideo =
                new MockMultipartFile("media_file", expectedResource.name(), expectedResource.contentType(), expectedResource.content());

        when(uploadMediaUseCase.execute(any()))
                .thenReturn(new UploadMediaOutput(expectedId.getValue(), expectedType));

        final var request = multipart("/videos/{id}/medias/{type}", expectedId.getValue(), expectedType.name())
                .file(expectedVideo)
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        final var response = this.mvc.perform(request);

        response.andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, "/videos/%s/medias/%s".formatted(expectedId.getValue(), expectedType.name())))
                .andExpect(header().string(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.video_id", equalTo(expectedId.getValue())))
                .andExpect(jsonPath("$.media_type", equalTo(expectedType.name())));

        final var captor = ArgumentCaptor.forClass(UploadMediaCommand.class);

        verify(this.uploadMediaUseCase).execute(captor.capture());

        final var actualCommand = captor.getValue();
        Assertions.assertEquals(expectedId.getValue(), actualCommand.videoId());
        Assertions.assertEquals(expectedResource.content(), actualCommand.videoResource().resource().content());
        Assertions.assertEquals(expectedResource.name(), actualCommand.videoResource().resource().name());
        Assertions.assertEquals(expectedResource.contentType(), actualCommand.videoResource().resource().contentType());
        Assertions.assertEquals(expectedType, actualCommand.videoResource().type());
    }

    @Test
    public void givenInvalidMediaType_whenCallsUploadMedia_shouldReturnError() throws Exception {
        final var expectedId = VideoID.unique();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);

        final var expectedVideo =
                new MockMultipartFile("media_file", expectedResource.name(), expectedResource.contentType(), expectedResource.content());

        final var request = multipart("/videos/{id}/medias/INVALID", expectedId.getValue())
                .file(expectedVideo)
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        final var response = this.mvc.perform(request);

        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo("Invalid INVALID for VideoMediaType")));
    }
}
