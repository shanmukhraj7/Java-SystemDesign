import java.util.*;

class SplitUser{
    private String userId; // Unique Identifier
    private String name;
    private Map<String, Double> balanceSheet; // Pending Transactions to be given

    public SplitUser(String userId, String name){
        this.userId = userId;
        this.name = name;
        balanceSheet = new HashMap<>();
    }

    public String getUserId(){
        return userId;
    }

    public String getName(){
        return name;
    }

    public Map<String, Double> getBalanceSheet(){
        return balanceSheet;
    }
}

enum SplitType{
    EQUAL,
    EXACT,
    PERCENT
}

abstract class Split{// forces subclass to inherit and define logic
    protected SplitUser user;
    protected double amount;

    public Split(SplitUser user){
        this.user = user;
    }

    public SplitUser getUser(){
        return user;
    }

    public double getAmount(){
        return amount;
    }

    public abstract void calculateAmount(double totalAmount, int totalUsers);
}

class EqualSplit extends Split{

    public EqualSplit(SplitUser user){
        super(user);
    }

    @Override
    public void calculateAmount(double totalAmount, int totalUsers){
        this.amount = totalAmount / totalUsers;
    }
}

class ExactSplit extends Split{

    public ExactSplit(SplitUser user, double amount){
        super(user);
        this.amount = amount;
    }

    @Override
    public void calculateAmount(double totalAmount, int totalUsers){

    }
}

class PercentSplit extends Split{
    private double percent;

    public PercentSplit(SplitUser user, double percent){
        super(user);
        this.percent = percent;
    }

    public double getPercent() {
        return percent;
    }

    @Override
    public void calculateAmount(double totalAmount, int totalUsers){
        this.amount = (totalAmount * percent ) / 100.0;
    }
}

class Expense{
    private String expenseId;
    private double amount;
    private List<Split> splits;
    private SplitUser paidBy;
    private SplitType splitType;

    public Expense(String expenseId, double amount, SplitUser paidBy, List<Split> splits, SplitType splitType){
        this.expenseId = expenseId;
        this.paidBy = paidBy;
        this.amount = amount;
        this.splits = splits;
        this.splitType = splitType;
    }

    public double getAmount(){
        return amount;
    }

    public SplitUser getPaidBy(){
        return paidBy;
    }

    public List<Split> getSplits(){
        return splits;
    }

    public SplitType getSplitType(){
        return splitType;
    }
}

class SplitwiseService{
    private Map<String, SplitUser> users;
    private List<Expense> expenses;

    public SplitwiseService(){
        users = new HashMap<>();
        expenses = new ArrayList<>();
    }

    private void validateExpense(Expense expense){
        if(expense.getSplitType() == SplitType.EXACT){
            double total = 0.0;
            for(Split split : expense.getSplits()){
                total += split.getAmount();
            }
            if(Math.abs(total - expense.getAmount()) > 0.001){
                throw new RuntimeException("Exact Split amount do not match total");
            }
        }
        if(expense.getSplitType() == SplitType.PERCENT){
            double totalPercent = 0.0;
            for(Split split : expense.getSplits()){
                totalPercent += ((PercentSplit) split).getPercent();
            }
            if (Math.abs(totalPercent - 100.0) > 0.001) {
                throw new RuntimeException("Percent split must sum to 100");
            }
        }
    }

    public void addUser(SplitUser user){
        users.put(user.getUserId(), user);
    }

    public void addExpense(Expense expense){
        int totalUsers = expense.getSplits().size();

        for (Split split : expense.getSplits()) {
            split.calculateAmount(expense.getAmount(), totalUsers);
        }

        validateExpense(expense);
        expenses.add(expense);

        for (Split split : expense.getSplits()) {

            SplitUser paidBy = expense.getPaidBy();
            SplitUser user = split.getUser();
            double amount = split.getAmount();

            if (paidBy.getUserId().equals(user.getUserId())) continue;

            paidBy.getBalanceSheet().put(
                    user.getUserId(),
                    paidBy.getBalanceSheet().getOrDefault(user.getUserId(), 0.0) + amount
            );

            user.getBalanceSheet().put(
                    paidBy.getUserId(),
                    user.getBalanceSheet().getOrDefault(paidBy.getUserId(), 0.0) - amount
            );
        }
    }

    public void showBalances() {
        for (SplitUser user : users.values()) {
            for (Map.Entry<String, Double> entry : user.getBalanceSheet().entrySet()) {
                if (entry.getValue() > 0) {
                    System.out.println(
                            user.getName() + " is owed " +
                                    entry.getValue() + " by " + entry.getKey()
                    );
                }
            }
        }
    }
}

public class Splitwise_LLD {
    public static void main(String[] args) {
        SplitwiseService service = new SplitwiseService();

        SplitUser u1 = new SplitUser("U1", "A");
        SplitUser u2 = new SplitUser("U2", "B");
        SplitUser u3 = new SplitUser("U3", "C");

        service.addUser(u1);
        service.addUser(u2);
        service.addUser(u3);

        List<Split> splits = new ArrayList<>();
        splits.add(new EqualSplit(u1));
        splits.add(new EqualSplit(u2));
        splits.add(new EqualSplit(u3));

        Expense expense = new Expense(
                "E1",
                3000,
                u1,
                splits,
                SplitType.EQUAL
        );

        service.addExpense(expense);

        service.showBalances();
    }
}