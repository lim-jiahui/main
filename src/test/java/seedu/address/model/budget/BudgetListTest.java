package seedu.address.model.budget;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalBudgets.BUDGET;
import static seedu.address.testutil.TypicalBudgets.ENTERTAINMENT_BUDGET;
import static seedu.address.testutil.TypicalBudgets.FOOD_BUDGET;
import static seedu.address.testutil.TypicalBudgets.HEALTHCARE_BUDGET;
import static seedu.address.testutil.TypicalBudgets.SHOPPING_BUDGET;
import static seedu.address.testutil.TypicalBudgets.TRAVEL_BUDGET;
import static seedu.address.testutil.TypicalBudgets.UTILITIES_BUDGET;
import static seedu.address.testutil.TypicalBudgets.WORK_BUDGET;

import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BudgetListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final BudgetList budgetList = new BudgetList();

    @Test
    public void contains_nullBudget_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        budgetList.contains(null);
    }

    @Test
    public void contains_budgetNotInList_returnsFalse() {
        assertFalse(budgetList.contains(BUDGET));
    }

    @Test
    public void contains_budgetInList_returnsTrue() {
        budgetList.addBudget(FOOD_BUDGET);
        assertTrue(budgetList.contains(FOOD_BUDGET));
    }

    @Test
    public void add_nullBudget_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        budgetList.addBudget(null);
    }

    @Test
    public void setBudget_nullTargetBudget_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        budgetList.setBudget(null, TRAVEL_BUDGET);
    }

    @Test
    public void setBudget_nullEditedBudget_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        budgetList.setBudget(FOOD_BUDGET, null);
    }

    @Test
    public void setExpense_targetBudgetNotInList_throwsBudgetNotFoundException() {
        thrown.expect(BudgetNotFoundException.class);
        budgetList.setBudget(BUDGET, BUDGET);
    }

    @Test
    public void setBudget_editedBudgetIsSameBudget_success() {
        budgetList.addBudget(TRAVEL_BUDGET);
        budgetList.setBudget(TRAVEL_BUDGET, TRAVEL_BUDGET);
        BudgetList expectedBudgetList = new BudgetList();
        expectedBudgetList.addBudget(TRAVEL_BUDGET);
        assertEquals(expectedBudgetList, budgetList);
    }

    @Test
    public void setBudget_editedBudgetIsDifferentBudget_success() {
        budgetList.addBudget(ENTERTAINMENT_BUDGET);
        budgetList.setBudget(ENTERTAINMENT_BUDGET, BUDGET);
        BudgetList expectedBudgetList = new BudgetList();
        expectedBudgetList.addBudget(BUDGET);
        for (Budget budget : budgetList) {
            System.out.println(budget.getCategory());
        }
        for (Budget budget : expectedBudgetList) {
            System.out.println(budget.getCategory());
        }
        assertEquals(expectedBudgetList, budgetList);
    }

    @Test
    public void setBudgets_nullBudgetList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        budgetList.setBudgets((BudgetList) null);
    }

    @Test
    public void setBudgets_budgetList_replacesOwnListWithProvidedBudgetList() {
        budgetList.addBudget(UTILITIES_BUDGET);
        BudgetList expectedBudgetList = new BudgetList();
        expectedBudgetList.addBudget(SHOPPING_BUDGET);
        budgetList.setBudgets(expectedBudgetList);
        assertEquals(expectedBudgetList, budgetList);
    }

    @Test
    public void setBudgets_nullList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        budgetList.setBudgets((List<Budget>) null);
    }

    @Test
    public void setBudgets_list_replacesOwnListWithProvidedList() {
        budgetList.addBudget(WORK_BUDGET);
        List<Budget> budgetList = Collections.singletonList(ENTERTAINMENT_BUDGET);
        this.budgetList.setBudgets(budgetList);
        BudgetList expectedBudgetList = new BudgetList();
        expectedBudgetList.addBudget(ENTERTAINMENT_BUDGET);
        assertEquals(expectedBudgetList, this.budgetList);
    }

    @Test
    public void remove_nullBudget_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        budgetList.removeBudget(null);
    }

    @Test
    public void remove_budgetDoesNotExist_throwsBudgetNotFoundException() {
        thrown.expect(BudgetNotFoundException.class);
        budgetList.removeBudget(HEALTHCARE_BUDGET);
    }

    @Test
    public void remove_existingBudget_removesBudget() {
        budgetList.addBudget(FOOD_BUDGET);
        budgetList.removeBudget(FOOD_BUDGET);
        BudgetList expectedBudgetList = new BudgetList();
        assertEquals(expectedBudgetList, budgetList);
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        budgetList.asUnmodifiableObservableList().remove(0);
    }
}
