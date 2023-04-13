/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Budget;
import entity.BudgetExpenseCategory;
import entity.Trip;
import error.TripNotFoundException;
import error.UnableToSetBudgetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author MK
 */
@Stateless
public class BudgetSessionBean implements BudgetSessionBeanLocal 
{

    @PersistenceContext(unitName = "KiaKia-ejbPU")
    private EntityManager em;

    @Override
    public void setBudget(Long tripId, Budget newB, BudgetExpenseCategory category) throws TripNotFoundException, UnableToSetBudgetException
    {   
        if (tripId == null) 
        {
            throw new IllegalArgumentException("Trip ID cannot be null.");
        }
        
        Trip trip = em.find(Trip.class, tripId);

        if (trip == null)
        {
            throw new TripNotFoundException("Trip is not found.");
        }
        
        if (category.getBudget() == null)
        {
            category.setBudget(newB);
            trip.getBudgets().add(newB);
            em.persist(newB);
        }
        else
        {
            throw new UnableToSetBudgetException("Budget for this category already exists.");
        }
        
    }

    @Override
    public void updateBudget(Budget budget) throws UnableToSetBudgetException
    {
        Budget oldB = em.find(Budget.class, budget.getBudgetId());
        
        if (oldB == null)
        {
            throw new UnableToSetBudgetException("Budget not found.");
        }
        
        oldB.setBudgetAmt(budget.getBudgetAmt());
        
        em.merge(oldB);
    }

    @Override
    public void deleteBudget(Long budgetId, Long tripId) throws UnableToSetBudgetException, TripNotFoundException
    {
        if (tripId == null || budgetId == null) 
        {
            throw new IllegalArgumentException("Trip ID or Budget ID cannot be null.");
        }
        
        Trip trip = em.find(Trip.class, tripId);
        if (trip == null)
        {
            throw new TripNotFoundException("Trip not found.");
        }
        
        Budget budget = em.find(Budget.class, budgetId);
        if (budget == null)
        {
            throw new UnableToSetBudgetException("Budget not found.");
        }
        
        trip.getBudgets().remove(budget);
        budget.getCategory().setBudget(null);
        em.remove(budget);
    }

    @Override
    public BigDecimal getBudgetByCategory(Long tripId, BudgetExpenseCategory category) throws UnableToSetBudgetException, TripNotFoundException
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

        Budget budget = trip.getBudgets()
                          .stream()
                          .filter(b -> b.getCategory() == category)
                          .findFirst()
                          .orElse(null);

        if (budget != null) 
        {
            return budget.getBudgetAmt();
        } 
        else 
        {
            throw new UnableToSetBudgetException("Budget not found.");
        }
    }

    // for setting budget
    @Override
    public List<BudgetExpenseCategory> getAvailableBudgetCategory(Long tripId) throws TripNotFoundException
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
        
        List<BudgetExpenseCategory> categories = trip.getCategories();
        List<BudgetExpenseCategory> availableCategories = new ArrayList<>();
        categories.stream().filter(c -> (c.getBudget() == null)).forEachOrdered(c -> {
            availableCategories.add(c);
        });
        
        return availableCategories;
    }

    @Override
    public BigDecimal getTotalBudget(Long tripId) throws TripNotFoundException
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
        
        List<Budget> budgets = new ArrayList<>();
        BigDecimal totalBudget = BigDecimal.ZERO;
        
        trip.getBudgets().forEach(b -> {
            budgets.add(b);
        });
        
        for (Budget b: budgets)
        {
            totalBudget = totalBudget.add(b.getBudgetAmt());
        }
        
        return totalBudget;        
    }

}