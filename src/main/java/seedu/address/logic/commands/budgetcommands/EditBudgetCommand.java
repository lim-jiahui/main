package seedu.address.logic.commands.budgetcommands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ENDDATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARKS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STARTDATE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_BUDGETS;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.Messages;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.attributes.Amount;
import seedu.address.model.attributes.Category;
import seedu.address.model.attributes.Date;
import seedu.address.model.budget.Budget;



/**
 * Edits the details of an existing budget in the finance tracker
 */
public class EditBudgetCommand extends Command {

    public static final String COMMAND_WORD = "editbudget";

    public static final String COMMAND_WORD_SHORTCUT = "eb";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the budget identified "
            + "by its category in the budget list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: " + PREFIX_CATEGORY + "CATEGORY "
            + "[" + PREFIX_AMOUNT + "AMOUNT] "
            + "[" + PREFIX_STARTDATE + "START_DATE] "
            + "[" + PREFIX_ENDDATE + "END_DATE] "
            + "[" + PREFIX_REMARKS + "REMARKS]\n"
            + "Example: " + COMMAND_WORD
            + PREFIX_CATEGORY + "food "
            + PREFIX_AMOUNT + "200 ";

    public static final String MESSAGE_EDIT_BUDGET_SUCCESS = "Edited budget:\n%1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";

    private final Category category;
    private final EditBudgetDescriptor editBudgetDescriptor;

    /**
     * @param category of budget to be edited
     * @param editBudgetDescriptor details to edit the budget with
     */
    public EditBudgetCommand(Category category, EditBudgetCommand.EditBudgetDescriptor editBudgetDescriptor) {
        requireNonNull(category);
        requireNonNull(editBudgetDescriptor);
        this.category = category;
        this.editBudgetDescriptor = new EditBudgetDescriptor(editBudgetDescriptor);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws ParseException {
        requireNonNull(model);
        List<Budget> lastShownList = model.getFilteredBudgetList();

        int index = -1;
        for (Budget budget : lastShownList) {
            if (budget.getCategory() == category) {
                index = lastShownList.indexOf(budget);
                break;
            }
        }

        if (index == -1) {
            throw new IllegalArgumentException(Messages.MESSAGE_BUDGET_DOES_NOT_EXIST_FOR_CATEGORY);
        }

        Budget budgetToEdit = lastShownList.get(index);
        Budget editedBudget = createEditedBudget(budgetToEdit, editBudgetDescriptor);

        if (!(editedBudget.getEndDate().getLocalDate().isAfter(budgetToEdit.getStartDate().getLocalDate()))) {
            throw new IllegalArgumentException(Budget.MESSAGE_CONSTRAINTS_END_DATE);
        }
        model.setBudget(budgetToEdit, editedBudget);
        model.updateFilteredBudgetList(PREDICATE_SHOW_ALL_BUDGETS);
        model.commitFinanceTracker();
        return new CommandResult(String.format(MESSAGE_EDIT_BUDGET_SUCCESS, editedBudget));
    }

    public Category getCategory() {
        return category;
    }

    /**
     * Creates and returns a {@code Budget} with the details of {@code budgetToEdit}
     * edited with {@code editBudgetDescriptor}.
     */
    private static Budget createEditedBudget(Budget budgetToEdit, EditBudgetDescriptor editBudgetDescriptor) throws ParseException {
        assert budgetToEdit != null;

        Amount updatedAmount = editBudgetDescriptor.getAmount().orElse(budgetToEdit.getAmount());
        Date updatedStartDate = editBudgetDescriptor.getStartDate().orElse(budgetToEdit.getStartDate());
        if (!(updatedStartDate.isEqualOrAfterToday())) {
            throw new IllegalArgumentException(Budget.MESSAGE_CONSTRAINTS_START_DATE);
        }
        Date updatedEndDate = editBudgetDescriptor.getEndDate().orElse(budgetToEdit.getEndDate());
        if (!(updatedEndDate.getLocalDate().isAfter(updatedStartDate.getLocalDate()))) {
            throw new ParseException(Budget.MESSAGE_CONSTRAINTS_END_DATE);
        }
        String updatedRemarks = editBudgetDescriptor.getRemarks().orElse(budgetToEdit.getRemarks());

        return new Budget(budgetToEdit.getCategory(), updatedAmount, updatedStartDate, updatedEndDate, updatedRemarks);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EditBudgetCommand // instanceof handles nulls
                && category.equals(((EditBudgetCommand) other).category))
                && editBudgetDescriptor.equals(((EditBudgetCommand) other).editBudgetDescriptor);
    }

    /**
     * Stores the details to edit the budget with. Each non-empty field value will replace the
     * corresponding field value of the budget.
     */
    public static class EditBudgetDescriptor {
        private Amount amount;
        private Date startDate;
        private Date endDate;
        private String remarks;

        public EditBudgetDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditBudgetDescriptor(EditBudgetDescriptor toCopy) {
            this();
            setAmount(toCopy.amount);
            setStartDate(toCopy.startDate);
            setEndDate(toCopy.endDate);
            setRemarks(toCopy.remarks);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(amount, startDate, endDate, remarks);
        }

        public void setAmount(Amount amount) {
            this.amount = amount;
        }

        public Optional<Amount> getAmount() {
            return Optional.ofNullable(amount);
        }

        public void setStartDate(Date startDate) {
                this.startDate = startDate;
        }

        public Optional<Date> getStartDate() {
            return Optional.ofNullable(startDate);
        }

        public void setEndDate(Date endDate) {
                this.endDate = endDate;
        }

        public Optional<Date> getEndDate() {
            return Optional.ofNullable(endDate);
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public Optional<String> getRemarks() {

            return Optional.ofNullable(remarks);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditBudgetDescriptor)) {
                return false;
            }

            // state check
            EditBudgetDescriptor e = (EditBudgetDescriptor) other;

            return getAmount().equals(e.getAmount())
                    && getStartDate().equals(e.getStartDate())
                    && getEndDate().equals(e.getEndDate())
                    && getRemarks().equals(e.getRemarks());
        }
    }

}
