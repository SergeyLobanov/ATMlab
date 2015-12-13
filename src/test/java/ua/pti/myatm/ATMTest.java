/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pti.myatm;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author andrii
 */
public class ATMTest {

    @Test
    public void testGetMoneyInATM() {
        System.out.println("getMoneyInATM");
        ATM atm = new ATM(0.0);
        double expResult = 0.0;
        double result = atm.getMoneyInATM();
        assertEquals(expResult, result, 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor(){
        ATM atm = new ATM(-700);
    }

    @Test
    public void testValidateCard() {
        System.out.println("validateCard");
        Card card = Mockito.mock(Card.class);
        int pinCode = 0;
        ATM atm = new ATM(1234.5);
        when(card.isBlocked()).thenReturn(true);
        when(card.checkPin(pinCode)).thenReturn(true);
        assertFalse(atm.validateCard(card, pinCode));
    }

    @Test
    public void testCheckBalance() throws NoCardInserted{
        System.out.println("checkBalance");
        ATM atm = new ATM(0.0);
        int pinCode = 1111;
        Card card = Mockito.mock(Card.class);
        Account acc = Mockito.mock(Account.class);
        double accBalance = 500;
        when(card.checkPin(pinCode)).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        when(card.getAccount()).thenReturn(acc);
        when(acc.getBalance()).thenReturn(accBalance);
        atm.validateCard(card, pinCode);
        atm.checkBalance();
        InOrder inOrder = inOrder(card,acc);
        inOrder.verify(card).checkPin(pinCode);
        inOrder.verify(card).isBlocked();
        inOrder.verify(card).getAccount();
        inOrder.verify(acc).getBalance();
        double result = atm.checkBalance();
        assertEquals(accBalance, result, 0.0);
    }

    @Test
    public void testGetCash() throws NotEnoughMoneyInATM, NoCardInserted, NotEnoughMoneyInAccount {
        System.out.println("getCash");
        ATM atm = new ATM(1000);
        Card card = mock(Card.class);
        Account acc = Mockito.mock(Account.class);
        int pinCode = 1111;
        double accBalance = 830;
        double amount = 230;
        when(card.checkPin(pinCode)).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        when(card.getAccount()).thenReturn(acc);
        when(acc.getBalance()).thenReturn(accBalance);
       // when(card.getAccount().withdrow(amount)).thenReturn(accBalance - amount);
        atm.validateCard(card, pinCode);
        atm.getCash(amount);
//        InOrder inOrder = inOrder(card,acc);
//        inOrder.verify(card,times(1)).checkPin(pinCode);
//        inOrder.verify(card,times(1)).isBlocked();
//        inOrder.verify(card,times(3)).getAccount();
//        inOrder.verify(acc).getBalance();
//        inOrder.verify(acc,times(1)).withdrow(amount);
        when(acc.getBalance()).thenReturn(accBalance - amount);
        assertEquals(600,acc.getBalance(),0.0);
    }

    @Test(expected = NotEnoughMoneyInATM.class)
    public void testGetCashForNotEnoughMoneyInATMException() throws NoCardInserted, NotEnoughMoneyInATM, NotEnoughMoneyInAccount{
        System.out.println("getCashForNotEnoughMoneyInATM");
        ATM atm = new ATM(1000);
        Card card = Mockito.mock(Card.class);
        Account acc = Mockito.mock(Account.class);
        int pinCode = 1111;
        when(card.checkPin(pinCode)).thenReturn(true);
        when(card.getAccount()).thenReturn(acc);
        atm.validateCard(card, pinCode);
        atm.checkBalance();
        double amount = 2000;
        atm.getCash(amount);
    }

    @Test(expected = NotEnoughMoneyInAccount.class)
    public void testGetCashForNotEnoughMoneyInAccount() throws NoCardInserted, NotEnoughMoneyInATM, NotEnoughMoneyInAccount{
        System.out.println("getCashNotEnoughMoneyInAccount");
        ATM atm = new ATM(1000);
        Card card = Mockito.mock(Card.class);
        Account account = Mockito.mock(Account.class);
        int pinCode = 1111;
        double accBalance = 300;
        double amount = 330;
        when(card.checkPin(pinCode)).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(accBalance);
        when(card.getAccount().withdrow(amount)).thenReturn(accBalance-amount);
        InOrder inOrder=inOrder(card,account);
        atm.validateCard(card,pinCode);
        atm.getCash(amount);
        inOrder.verify(card).getAccount();
        inOrder.verify(account).getBalance();
    }
}
