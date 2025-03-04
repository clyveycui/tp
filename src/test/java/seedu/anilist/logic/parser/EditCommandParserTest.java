package seedu.anilist.logic.parser;

import static seedu.anilist.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.anilist.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.anilist.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.anilist.logic.commands.CommandTestUtil.NAME_DESC_AKIRA;
import static seedu.anilist.logic.commands.CommandTestUtil.TAG_DESC_SHOUNEN;
import static seedu.anilist.logic.commands.CommandTestUtil.TAG_DESC_SUPERHERO;
import static seedu.anilist.logic.commands.CommandTestUtil.VALID_NAME_AKIRA;
import static seedu.anilist.logic.commands.CommandTestUtil.VALID_TAG_SHOUNEN;
import static seedu.anilist.logic.commands.CommandTestUtil.VALID_TAG_SUPERHERO;
import static seedu.anilist.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.anilist.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.anilist.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.anilist.testutil.TypicalIndexes.INDEX_FIRST_ANIME;
import static seedu.anilist.testutil.TypicalIndexes.INDEX_SECOND_ANIME;
import static seedu.anilist.testutil.TypicalIndexes.INDEX_THIRD_ANIME;

import org.junit.jupiter.api.Test;

import seedu.anilist.commons.core.index.Index;
import seedu.anilist.logic.commands.EditCommand;
import seedu.anilist.model.anime.Name;
import seedu.anilist.model.tag.Tag;
import seedu.anilist.testutil.EditAnimeDescriptorBuilder;

public class EditCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_AKIRA, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_AKIRA, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_AKIRA, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS); // invalid tag

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code Anime} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, "1" + TAG_DESC_SUPERHERO + TAG_DESC_SHOUNEN + TAG_EMPTY, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_DESC_SUPERHERO + TAG_EMPTY + TAG_DESC_SHOUNEN, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_EMPTY + TAG_DESC_SUPERHERO + TAG_DESC_SHOUNEN, Tag.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_NAME_DESC,
                Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_ANIME;
        String userInput = targetIndex.getOneBased() + TAG_DESC_SHOUNEN
                + NAME_DESC_AKIRA + TAG_DESC_SUPERHERO;

        EditCommand.EditAnimeDescriptor descriptor = new EditAnimeDescriptorBuilder().withName(VALID_NAME_AKIRA)
                .withTags(VALID_TAG_SHOUNEN, VALID_TAG_SUPERHERO).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_ANIME;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AKIRA;
        EditCommand.EditAnimeDescriptor descriptor =
                new EditAnimeDescriptorBuilder().withName(VALID_NAME_AKIRA).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = targetIndex.getOneBased() + TAG_DESC_SUPERHERO;
        descriptor = new EditAnimeDescriptorBuilder().withTags(VALID_TAG_SUPERHERO).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        Index targetIndex = INDEX_FIRST_ANIME;
        String userInput = targetIndex.getOneBased() + TAG_DESC_SUPERHERO + TAG_DESC_SUPERHERO + TAG_DESC_SHOUNEN;

        EditCommand.EditAnimeDescriptor descriptor = new EditAnimeDescriptorBuilder()
                .withTags(VALID_TAG_SUPERHERO, VALID_TAG_SHOUNEN)
                .build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }


    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_ANIME;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        EditCommand.EditAnimeDescriptor descriptor = new EditAnimeDescriptorBuilder().withTags().build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}

