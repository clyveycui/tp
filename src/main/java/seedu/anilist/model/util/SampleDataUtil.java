package seedu.anilist.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.anilist.model.AnimeList;
import seedu.anilist.model.ReadOnlyAnimeList;
import seedu.anilist.model.anime.Anime;
import seedu.anilist.model.anime.Name;
import seedu.anilist.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AnimeList} with sample data.
 */
public class SampleDataUtil {
    public static Anime[] getSampleAnime() {
        return new Anime[] {
            new Anime(new Name("Asobi Asobase"), getTagSet("comedy")),
            new Anime(new Name("BLEACH"), getTagSet("adventure", "supernatural")),
            new Anime(new Name("Charlotte"), getTagSet("drama", "supernatural")),
            new Anime(new Name("Darling in the Franxx"), getTagSet("action", "mecha", "science fiction")),
            new Anime(new Name("Haikyuu!!: To the Top"), getTagSet("sports")),
            new Anime(new Name("Higehiro: After Being Rejected, I Shaved and Took in a High School Runaway"),
                    getTagSet("tag1", "tag2", "tag3", "tag4", "tag5", "tag6", "tag7", "tag8", "tag9",
                            "tag10", "tag11", "tag12", "tag13", "tag14", "tag15")),
            new Anime(new Name("Steins;Gate 0"), getTagSet("science fiction"))
        };
    }

    public static ReadOnlyAnimeList getSampleAnimeList() {
        AnimeList sampleAb = new AnimeList();
        for (Anime sampleAnime : getSampleAnime()) {
            sampleAb.addAnime(sampleAnime);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
