/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package error;

/**
 *
 * @author MK
 */
public class UnableToSetBudgetException extends Exception {

    /**
     * Creates a new instance of <code>BudgetNotFoundException</code> without
     * detail message.
     */
    public UnableToSetBudgetException() {
    }

    /**
     * Constructs an instance of <code>BudgetNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UnableToSetBudgetException(String msg) {
        super(msg);
    }
}
