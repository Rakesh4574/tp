package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RemarkCommand;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.testutil.PersonBuilder;

public class RemarkCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addRemarkUnfilteredList_success() {
        Person personToRemark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        RemarkCommand remarkCommand = new RemarkCommand(INDEX_FIRST_PERSON, new Remark("Likes coffee"));
        Person remarkedPerson = new PersonBuilder(personToRemark).withRemark("Likes coffee").build();

        String expectedMessage = String.format(RemarkCommand.MESSAGE_ADD_REMARK_SUCCESS, Messages.format(remarkedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToRemark, remarkedPerson);
        expectedModel.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_clearRemarkUnfilteredList_success() {
        Person personToRemark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person personWithRemark = new PersonBuilder(personToRemark).withRemark("temp").build();
        model.setPerson(personToRemark, personWithRemark);

        RemarkCommand remarkCommand = new RemarkCommand(INDEX_FIRST_PERSON, Remark.EMPTY);
        Person personCleared = new PersonBuilder(personWithRemark).withRemark("").build();

        String expectedMessage = String.format(RemarkCommand.MESSAGE_DELETE_REMARK_SUCCESS,
                Messages.format(personCleared));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToRemark, personWithRemark);
        expectedModel.setPerson(personWithRemark, personCleared);
        expectedModel.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        RemarkCommand remarkCommand = new RemarkCommand(outOfBoundIndex, new Remark("test"));

        assertCommandFailure(remarkCommand, model, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        RemarkCommand remarkCommand = new RemarkCommand(outOfBoundIndex, new Remark("test"));

        assertCommandFailure(remarkCommand, model, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        RemarkCommand addRemarkCommand = new RemarkCommand(INDEX_FIRST_PERSON, new Remark("Hi"));
        RemarkCommand addRemarkCommandCopy = new RemarkCommand(INDEX_FIRST_PERSON, new Remark("Hi"));
        RemarkCommand differentIndexCommand = new RemarkCommand(INDEX_SECOND_PERSON, new Remark("Hi"));
        RemarkCommand differentRemarkCommand = new RemarkCommand(INDEX_FIRST_PERSON, new Remark("Bye"));

        assertTrue(addRemarkCommand.equals(addRemarkCommandCopy));
        assertTrue(addRemarkCommand.equals(addRemarkCommand));
        assertFalse(addRemarkCommand.equals(null));
        assertFalse(addRemarkCommand.equals(new ClearCommand()));
        assertFalse(addRemarkCommand.equals(differentIndexCommand));
        assertFalse(addRemarkCommand.equals(differentRemarkCommand));
    }
}
