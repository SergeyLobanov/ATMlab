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
        ATM atm = new ATM(1000);
        double expResult = 1000;
        double result = atm.getMoneyInATM();
        assertEquals(expResult, result, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeATMBalance(){
        System.out.println("getNegativeATMBalance");
        ATM atm = new ATM(-230);
    }

    @Test
    public void testValidateCard() {
        System.out.println("validateCard");
        ATM atm = new ATM(1000);
        Card card = mock(Card.class);
        int pinCode = 1111;
        when(card.checkPin(pinCode)).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        assertTrue(atm.validateCard(card, pinCode));
    }

    @Test
    public void testValidateBlockedCard() {
        System.out.println("validateBlockedCard");
        ATM atm = new ATM(1000);
        Card card = mock(Card.class);
        int pinCode = 1111;
        when(card.checkPin(pinCode)).thenReturn(true);
        when(card.isBlocked()).thenReturn(true);
        assertFalse(atm.validateCard(card, pinCode));
        verify(card).isBlocked();
    }
    @Test
    public void testValidateCardWithIllegalPIN() {
        System.out.println("validateCardWithIllegalPINCode");
        ATM atm = new ATM(1000);
        Card card = mock(Card.class);
        int pinCode = 1111;
        when(card.checkPin(pinCode)).thenReturn(false);
        when(card.isBlocked()).thenReturn(false);
        assertFalse(atm.validateCard(card, pinCode));
        verify(card).checkPin(pinCode);//do we need it?
    }

    @Test
    public void testCheckBalance() throws NoCardInsertedException {
        System.out.println("checkBalance");
        ATM atm = new ATM(1000);
        Card card = mock(Card.class);
        int pinCode = 1111;
        when(card.checkPin(pinCode)).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        atm.validateCard(card, pinCode);
        Account acc = mock(Account.class);
        when(card.getAccount()).thenReturn(acc);
        double accBalance = 830;
        when(acc.getBalance()).thenReturn(accBalance);
        assertEquals(accBalance, atm.checkBalance(), 0.0);
        InOrder inOrder = inOrder(card,acc);
        inOrder.verify(card).getAccount();
        inOrder.verify(acc).getBalance();
    }

    @Test
    public void testGetCash() throws NotEnoughMoneyInAccountException, NotEnoughMoneyInATMException, NoCardInsertedException {
        System.out.println("getCash");
        double atmBalance = 1000;
        ATM atm = new ATM(atmBalance);
        Card card = mock(Card.class);
        int pinCode = 1111;
        when(card.checkPin(pinCode)).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        Account acc = mock(Account.class);
        when(card.getAccount()).thenReturn(acc);
        double accBalance = 830;
        when(acc.getBalance()).thenReturn(accBalance);
        atm.validateCard(card, pinCode);
        double amount = 230;
        when(card.getAccount().withdrow(amount)).thenReturn(accBalance - amount);
        atm.getCash(amount);
        when(acc.getBalance()).thenReturn(accBalance - amount);
        assertEquals(atm.getMoneyInATM(), atmBalance - amount, 0.0);
        assertEquals(atm.checkBalance(), accBalance - amount, 0.0);
        assertEquals(600, acc.getBalance(), 0.0);//we need obvious chek?
//        InOrder inOrder = inOrder(card,acc);
//        inOrder.verify(card).checkPin(pinCode);
//        inOrder.verify(card).isBlocked();
//        inOrder.verify(card).getAccount();
//        inOrder.verify(acc, atLeastOnce()).getBalance();
//        inOrder.verify(acc, times(1)).withdrow(amount);
    }

    @Test(expected = NotEnoughMoneyInATMException.class)
    public void testGetCashForNotEnoughMoneyInATMException() throws NoCardInsertedException, NotEnoughMoneyInATMException, NotEnoughMoneyInAccountException {
        System.out.println("getCashForNotEnoughMoneyInATM");
        ATM atm = new ATM(1000);
        Card card = mock(Card.class);
        Account acc = mock(Account.class);
        int pinCode = 1111;
        when(card.checkPin(pinCode)).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        atm.validateCard(card, pinCode);
        when(card.getAccount()).thenReturn(acc);
        atm.checkBalance();
        double amount = 2000;
        atm.getCash(amount);
    }

    @Test(expected = NotEnoughMoneyInAccountException.class)
    public void testGetCashForNotEnoughMoneyInAccount() throws NoCardInsertedException, NotEnoughMoneyInATMException, NotEnoughMoneyInAccountException{
        System.out.println("getCashNotEnoughMoneyInAccount");
        ATM atm = new ATM(1000);
        Card card = mock(Card.class);
        int pinCode = 1111;
        when(card.checkPin(pinCode)).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        atm.validateCard(card, pinCode);
        Account acc = mock(Account.class);
        when(card.getAccount()).thenReturn(acc);
        double accBalance = 830;
        when(acc.getBalance()).thenReturn(accBalance);
        double amount = 1430;
        when(card.getAccount().withdrow(amount)).thenReturn(amount);
        atm.getCash(amount);
        InOrder inOrder=inOrder(card,acc);
        inOrder.verify(card).getAccount();
        inOrder.verify(acc).getBalance();
    }
}
