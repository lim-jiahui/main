package seedu.address.model.budget;

/**
 * Signals that the operation is unable to find the specified budget.
 */
public class BudgetNotFoundException extends RuntimeException {
    public BudgetNotFoundException() {
        super("Budget does not exist for that category.");
    }
}
