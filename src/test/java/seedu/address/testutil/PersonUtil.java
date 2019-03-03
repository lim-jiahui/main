package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.logic.commands.expensecommands.AddCommand;
import seedu.address.logic.commands.expensecommands.EditCommand;
import seedu.address.model.expense.Expense;
import seedu.address.model.tag.Tag;

/**
 * A utility class for Expense.
 */
public class PersonUtil {

    /**
     * Returns an add command string for adding the {@code expense}.
     */
    public static String getAddCommand(Expense expense) {
        return AddCommand.COMMAND_WORD + " " + getPersonDetails(expense);
    }

    /**
     * Returns the part of command string for the given {@code expense}'s details.
     */
    public static String getPersonDetails(Expense expense) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + expense.getName().name + " ");
        sb.append(PREFIX_PHONE + expense.getAmount().value + " ");
        sb.append(PREFIX_EMAIL + expense.getEmail().value + " ");
        sb.append(PREFIX_ADDRESS + expense.getAddress().value + " ");
        expense.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditRecurringDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(EditCommand.EditRecurringDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.name).append(" "));
        descriptor.getAmount().ifPresent(phone -> sb.append(PREFIX_PHONE).append(phone.value).append(" "));
        descriptor.getEmail().ifPresent(email -> sb.append(PREFIX_EMAIL).append(email.value).append(" "));
        descriptor.getAddress().ifPresent(address -> sb.append(PREFIX_ADDRESS).append(address.value).append(" "));
        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_TAG);
            } else {
                tags.forEach(s -> sb.append(PREFIX_TAG).append(s.tagName).append(" "));
            }
        }
        return sb.toString();
    }
}
