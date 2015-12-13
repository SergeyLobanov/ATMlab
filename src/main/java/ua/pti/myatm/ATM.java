package ua.pti.myatm;


public class ATM {
    private double ATMBalance;
    private Card card;

    //Можно задавать количество денег в банкомате 
    ATM(double moneyInATM) {
        if (moneyInATM < 0)
            throw new IllegalArgumentException("ATM can't have negative balance");
        else
            this.ATMBalance = moneyInATM;
    }

    // Возвращает количестов денег в банкомате
    public double getMoneyInATM() {
        return ATMBalance;
    }

    //С вызова данного метода начинается работа с картой
    //Метод принимает карту и пин-код, проверяет пин-код карты и не заблокирована ли она
    //Если неправильный пин-код или карточка заблокирована, возвращаем false. При этом, вызов всех последующих методов
    // у ATM с данной картой должен генерировать исключение NoCardInserted
    public boolean validateCard(Card card, int pinCode) {
        if (!card.checkPin(pinCode)){
            System.out.println("Illegal PIN Code");
            return false;
        }
        else if (card.isBlocked()){
            System.out.println("This card is blocked!");
            return false;
        }
        else{
            this.card = card;
            return true;
        }
    }

    //Возвращает сколько денег есть на счету
    public double checkBalance() throws NoCardInsertedException {
        if( card == null && card.isBlocked()){
            throw new NoCardInsertedException();
        }
        else {
            Account acc = card.getAccount();
            return acc.getBalance();
        }
    }

    //Метод для снятия указанной суммы
    //Метод возвращает сумму, которая у клиента осталась на счету после снятия
    //Кроме проверки счета, метод так же должен проверять достаточно ли денег в самом банкомате
    //Если недостаточно денег на счете, то должно генерироваться исключение NotEnoughMoneyInAccount
    //Если недостаточно денег в банкомате, то должно генерироваться исключение NotEnoughMoneyInATM
    //При успешном снятии денег, указанная сумма должна списываться со счета, и в банкомате должно уменьшаться количество денег
    public double getCash(double amount) throws NoCardInsertedException, NotEnoughMoneyInATMException, NotEnoughMoneyInAccountException {
        Account acc = card.getAccount();
        if (amount > this.getMoneyInATM()) {
            throw new NotEnoughMoneyInATMException();
        } else if (amount > this.checkBalance()) {
            throw new NotEnoughMoneyInAccountException();
        } else {
            ATMBalance -= acc.withdrow(amount);
            return this.checkBalance();
        }
    }
}

class NoCardInsertedException extends Exception{
    public NoCardInsertedException(){
        super("Insert your Card, please.");
    }
}

class NotEnoughMoneyInATMException extends Exception{
    public NotEnoughMoneyInATMException(){
        super("ATM have not enought money. Take less amount or try again later.");
    }
}

class NotEnoughMoneyInAccountException extends Exception{
    public NotEnoughMoneyInAccountException(){
        super("Your Account have not enought money. Take less amount.");
    }
}

