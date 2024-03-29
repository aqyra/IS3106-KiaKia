/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import enumeration.CityEnum;
import enumeration.CountryEnum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Natalienovaela
 */
@Entity
public class Trip implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripId;
    
    @Temporal(TemporalType.DATE)
    private Date startDate;
    
    @Temporal(TemporalType.DATE)
    private Date endDate;
    
    private String name;
    
    private String description;
    
    private Boolean isShared = Boolean.FALSE;
    
    private String inviteToken;
    
    @OneToMany
    private List<Note> notes = new ArrayList<>();
    
    @OneToMany
    private List<Document> documents = new ArrayList<>();
    
    @OneToMany
    private List<CheckList> checkLists = new ArrayList<>();
    
    @OneToMany
    private List<Poll> polls = new ArrayList<>();
    
    @OneToMany
    private List<PlaceLineItem> bucketList = new ArrayList<>();
    
    @OneToMany
    private List<DayItinerary> itinerary = new ArrayList<>();
    
    @OneToMany
    private List<Budget> budgets = new ArrayList<>();
    
    @OneToMany
    private List<Expense> expenses = new ArrayList<>();
    
    @OneToMany
    private List<Debt> debts = new ArrayList<>();

    @OneToMany
    private List<BudgetExpenseCategory> categories = new ArrayList<>();
    
    @OneToMany
    private List<UserExpense> userExpenses = new ArrayList<>();
    
    @ManyToOne(optional = false)
    @Enumerated(EnumType.STRING)
    private CountryEnum country;
    
    @ManyToOne(optional = false)
    @Enumerated(EnumType.STRING)
    private CityEnum city;
    
    public Trip() {
        
    }

    public Trip(String name, Date startDate, Date endDate){
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Trip(String name, Date startDate, Date endDate, CountryEnum country) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.country = country;
    }

    public Trip(Date startDate, Date endDate, String name, String description, CountryEnum country) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
        this.description = description;
        this.country = country;
    }
    
    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tripId != null ? tripId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the tripId fields are not set
        if (!(object instanceof Trip)) {
            return false;
        }
        Trip other = (Trip) object;
        if ((this.tripId == null && other.tripId != null) || (this.tripId != null && !this.tripId.equals(other.tripId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Trip[ id=" + tripId + " ]";
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsShared() {
        return isShared;
    }

    public void setIsShared(Boolean isShared) {
        this.isShared = isShared;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public List<CheckList> getCheckLists() {
        return checkLists;
    }

    public void setCheckLists(List<CheckList> checkLists) {
        this.checkLists = checkLists;
    }

    public List<Poll> getPolls() {
        return polls;
    }

    public void setPolls(List<Poll> polls) {
        this.polls = polls;
    }

    public List<PlaceLineItem> getBucketList() {
        return bucketList;
    }

    public void setBucketList(List<PlaceLineItem> bucketList) {
        this.bucketList = bucketList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getInviteToken() {
        return inviteToken;
    }

    public void setInviteToken(String inviteToken) {
        this.inviteToken = inviteToken;
    }

    public List<DayItinerary> getItinerary() {
        return itinerary;
    }

    public void setItinerary(List<DayItinerary> itinerary) {
        this.itinerary = itinerary;
    }
    
    public List<Budget> getBudgets() {
        return budgets;
    }

    public void setBudgets(List<Budget> budgets) {
        this.budgets = budgets;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public List<Debt> getDebts() {
        return debts;
    }

    public void setDebts(List<Debt> debts) {
        this.debts = debts;
    }

    public List<BudgetExpenseCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<BudgetExpenseCategory> categories) {
        this.categories = categories;
    }

    public List<UserExpense> getUserExpenses() {
        return userExpenses;
    }

    public void setUserExpenses(List<UserExpense> userExpenses) {
        this.userExpenses = userExpenses;
    }

    public CountryEnum getCountry() {
        return country;
    }

    public void setCountry(CountryEnum country) {
        this.country = country;
    }

    public CityEnum getCity() {
        return city;
    }

    public void setCity(CityEnum city) {
        this.city = city;
    }
    
}
