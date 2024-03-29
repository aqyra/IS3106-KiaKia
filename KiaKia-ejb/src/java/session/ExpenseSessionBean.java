/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.BudgetExpenseCategory;
import entity.Expense;
import entity.Trip;
import entity.User;
import entity.UserExpense;
import error.CategoryNotFoundException;
import error.DebtNotFoundException;
import error.ExpenseNotFoundException;
import error.TripNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author MK
 */
@Stateless
public class ExpenseSessionBean implements ExpenseSessionBeanLocal 
{

    @EJB
    private DebtSessionBeanLocal debtSessionBeanLocal;

    @PersistenceContext(unitName = "KiaKia-ejbPU")
    private EntityManager em;
    
    @Override
    public void addExpense(Long tripId, Expense expense) throws TripNotFoundException, CategoryNotFoundException
    {
        expense.setExpenseDate(new Date());
        
         if (tripId == null || expense.getCategory().getCategoryId() == null) 
        {
            throw new IllegalArgumentException("Trip ID or Category ID cannot be null.");
        }
        
        Trip trip = em.find(Trip.class, tripId);
        if (trip == null) 
        {
            throw new TripNotFoundException("Trip not found.");
        }

        BudgetExpenseCategory category = em.find(BudgetExpenseCategory.class, expense.getCategory().getCategoryId());
        if (category == null) 
        {
            throw new CategoryNotFoundException("Category not found.");
        }

        trip.getExpenses().add(expense);
        
        updateUserExpenses(expense, trip);
        
        em.persist(expense);
        
        try 
        {
            debtSessionBeanLocal.handleDebt(trip, expense, true);
        } 
        catch (DebtNotFoundException ex) 
        {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void updateExpense(Expense expense) throws ExpenseNotFoundException
    {
        try
        {
            Expense oldE = em.find(Expense.class, expense.getExpenseId());
            oldE.setDescription(expense.getDescription());
            oldE.setExpenseAmt(expense.getExpenseAmt());
            // if payer and payee is changed, debts need to be changed
            // relink category if changed
            // change splitType too?
            // need to pass both old and new?
            // use some data structure to store the changes in the amt of debt, changes can be (-) and (+)
            em.merge(oldE);
        }
        catch (NoResultException e)
        {
            throw new ExpenseNotFoundException("Expense not found");
        }
    }

    @Override
    public void deleteExpense(Long expenseId, Long tripId) throws ExpenseNotFoundException, TripNotFoundException
    {
        if (tripId == null || expenseId == null) 
        {
            throw new IllegalArgumentException("Trip ID or Expense ID cannot be null.");
        }
        
        Trip trip = em.find(Trip.class, tripId);
        if (trip == null)
        {
            throw new TripNotFoundException("Trip not found");
        }
        
        Expense e = em.find(Expense.class, expenseId);
        if (e == null)
        {
            throw new ExpenseNotFoundException("Expense not found");
        }
        
        trip.getExpenses().remove(e);        
        e.setExpenseAmt(e.getExpenseAmt().negate());
        
        updateUserExpenses(e, trip);
        try 
        {
            debtSessionBeanLocal.handleDebt(trip, e, false);
        } 
        catch (DebtNotFoundException ex) 
        {
            System.out.println(ex.getMessage());
        }
        
        em.remove(e);
    }
    
    @Override
    public List<Expense> getAllExpenses(Long tripId) throws TripNotFoundException
    {
        if (tripId == null) 
        {
            throw new IllegalArgumentException("Trip ID cannot be null.");
        }
        
        Trip trip = em.find(Trip.class, tripId);
        if (trip == null)
        {
            throw new TripNotFoundException("Trip not found");
        }
        
        return trip.getExpenses();
    }
    
    @Override
    public Map<String, BigDecimal> getTotalExpenseByCategories(Long tripId) throws TripNotFoundException
    {
        if (tripId == null)
        {
            throw new IllegalArgumentException("Trip ID cannot be null.");
        }
        
        Trip trip = em.find(Trip.class, tripId);
        if (trip == null)
        {
            throw new TripNotFoundException("Trip not found.");
        }
        
        List<Expense> expenses = trip.getExpenses();
        
        Map<String, BigDecimal> expenseByCategory = new HashMap<>();
        
        for (Expense expense : expenses) 
        {
            BudgetExpenseCategory category = expense.getCategory();
            String categoryName = category.getName();
            
            expenseByCategory.putIfAbsent(categoryName, BigDecimal.ZERO);
            
            BigDecimal currentExpense = expenseByCategory.get(categoryName);
            BigDecimal newExpense = currentExpense.add(expense.getExpenseAmt());

            expenseByCategory.put(categoryName, newExpense);
        }
        
        return expenseByCategory;
    }
    
    @Override
    public BigDecimal getTotalExpenseByCategory(Long tripId, Long categoryId) throws TripNotFoundException, CategoryNotFoundException
    {
        if (tripId == null || categoryId == null)
        {
            throw new IllegalArgumentException("Trip ID and Category ID cannot be null.");
        }
        
        Trip trip = em.find(Trip.class, tripId);
        if (trip == null)
        {
            throw new TripNotFoundException("Trip not found.");
        }
        
        BudgetExpenseCategory category = em.find(BudgetExpenseCategory.class, categoryId);
        if (category == null)
        {
            throw new CategoryNotFoundException("Category not found.");
        }
        
        List<Expense> expenses = trip.getExpenses();
        
        BigDecimal totalExpenseByCategory = BigDecimal.ZERO;
        for (Expense e: expenses)
        {
            if (e.getCategory().equals(category))
            {
                totalExpenseByCategory = totalExpenseByCategory.add(e.getExpenseAmt());
            }
        }
        
        return totalExpenseByCategory;
    }

    @Override
    public BigDecimal getTotalExpense(Long tripId) throws TripNotFoundException
    {
        if (tripId == null)
        {
            throw new IllegalArgumentException("Trip ID or Category ID cannot be null.");
        }
        
        Trip trip = em.find(Trip.class, tripId);
        if (trip == null)
        {
            throw new TripNotFoundException("Trip not found.");
        }
        
        return calculateTotalExpense(trip.getExpenses());
    }

    @Override
    public BigDecimal getTotalExpenseByUser(Long userId, Long tripId) throws TripNotFoundException
    {
        if (tripId == null || userId == null)
        {
            throw new IllegalArgumentException("Trip ID or Category ID cannot be null.");
        }
        
        Trip trip = em.find(Trip.class, tripId);
        if (trip == null)
        {
            throw new TripNotFoundException("Trip not found.");
        }
        
        List<UserExpense> allUserE = trip.getUserExpenses();
        List<UserExpense> userE = getUserExpenseByUser(userId, allUserE);
        
        BigDecimal totalE = BigDecimal.ZERO;
        for (UserExpense ue: userE)
        {
            totalE = totalE.add(ue.getExpenseAmt());
        }
        
        return totalE;
    }
    
    public BigDecimal calculateTotalExpense(List<Expense> expenses)
    {
        BigDecimal totalE = BigDecimal.ZERO;
        for (Expense e: expenses)
        {
            totalE = totalE.add(e.getExpenseAmt());
        }
        
        return totalE;
    }
    
    public List<UserExpense> getUserExpenseByUser(Long userId, List<UserExpense> allUserE)
    {
        List<UserExpense> userE = new ArrayList<>();
        allUserE.stream().filter(ue -> (ue.getUser().getUserId() == userId)).forEachOrdered(ue -> {
            userE.add(ue);
        });
        
        return userE;
    }
    
    public void updateUserExpenses(Expense expense, Trip trip)
    {
        List<User> payees = expense.getPayees();
        BigDecimal splitAmt = expense.getExpenseAmt().setScale(4, RoundingMode.HALF_UP).divide(new BigDecimal(payees.size()), 2, RoundingMode.HALF_UP);
        
        for (User u: payees)
        {
            boolean found = false;
            for (UserExpense payeeE : trip.getUserExpenses()) 
            {
                if (payeeE.getUser().equals(u)) 
                {
                    payeeE.setExpenseAmt(payeeE.getExpenseAmt().add(splitAmt));
                    found = true;
                    break;
                }
            }
            if (!found) 
            {
                UserExpense payeeE = new UserExpense();
                payeeE.setUser(u);
                payeeE.setExpenseAmt(splitAmt);
                trip.getUserExpenses().add(payeeE);
                em.persist(payeeE);
            }
        }
    }

}