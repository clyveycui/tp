package seedu.anilist.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.anilist.commons.exceptions.IllegalValueException;
import seedu.anilist.model.anime.Anime;
import seedu.anilist.model.anime.Episode;
import seedu.anilist.model.anime.Name;
import seedu.anilist.model.anime.Status;
import seedu.anilist.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Anime}.
 */
class JsonAdaptedAnime {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Anime's %s field is missing!";

    private final String name;
    private final String episode;
    private final String status;
    private final List<JsonAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedAnime} with the given Anime details.
     */
    @JsonCreator
    public JsonAdaptedAnime(@JsonProperty("name") String name,
                            @JsonProperty("episode") String episode,
                            @JsonProperty("status") String status,
                            @JsonProperty("tagged") List<JsonAdaptedTag> tagged) {
        this.name = name;
        this.episode = episode;
        this.status = status;
        if (tagged != null) {
            this.tagged.addAll(tagged);
        }
    }

    /**
     * Converts a given {@code Anime} into this class for Jackson use.
     */
    public JsonAdaptedAnime(Anime source) {
        name = source.getName().fullName;
        this.episode = source.getEpisode().toString();
        this.status = source.getStatus().toString();
        tagged.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted Anime object into the model's {@code Anime} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted Anime.
     */
    public Anime toModelType() throws IllegalValueException {
        final List<Tag> animeTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tagged) {
            animeTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);
        if (episode == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Episode.class.getSimpleName()));
        }
        if (!Episode.isValidEpisode(episode)) {
            throw new IllegalValueException(Episode.MESSAGE_CONSTRAINTS);
        }
        final Episode modelEpisode = new Episode(episode);
        if (status == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Status.class.getSimpleName()));
        }
        if (!Status.isValidStatus(status)) {
            throw new IllegalValueException(Status.MESSAGE_CONSTRAINTS);
        }
        final Status modelStatus = new Status(status);

        final Set<Tag> modelTags = new HashSet<>(animeTags);
        return new Anime(modelName, modelEpisode, modelStatus, modelTags);
    }

}
