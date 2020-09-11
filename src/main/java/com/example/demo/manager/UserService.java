package com.example.demo.manager;

import com.example.demo.dao.AmountTellerRepository;
import com.example.demo.dao.TransactionRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
@Service
public class UserService implements UserManager{
    @Autowired
    public UserRepository userRepo;
    @Autowired
    public AmountTellerRepository amountRepo;
    @Autowired
    public TransactionRepository transactionRepo;
    public Object userAddition(User user)  {
            Object validation= userValidation(user,true);
            if(validation!=null)
                return validation;
            userRepo.save(user);
            AmountTeller a=new AmountTeller();
            a.setFinalAmount(0);
            a.setSerialNo((int) userRepo.count());
            a.setCreated(dateTimeGenerator());
            a.setUpdated(a.getCreated());
            user.setCreated(dateTimeGenerator());
            user.setUpdated(user.getCreated());
            amountRepo.save(a);
            return user;
    }

    public Object updateUser(User user)  {
        Object validation=userValidation(user,false);
        if(validation!=null)
            return validation;
            userRepo.updateUser(user.getEmail(),user.getUserPassword(),user.getFirstName(),user.getLastName(),user.getMobileNumber());
            user.setUserNo(userNoFetcher(user.getMobileNumber()));
            user.setUpdated(dateTimeGenerator());
            user.setCreated(createdFetcher(user.getMobileNumber()));
            return user;
    }

    public Object topUp(Transaction transaction) {
        Object finalAmount = transactionValidation(transaction,true);
        if(finalAmount.getClass()==ErrorMessage.class)
            return finalAmount;
        transaction.setFinalAmount((Integer) finalAmount);
        transaction.setDateAndTime(dateTimeGenerator());
        transaction.setTransactionId(idGenerator());
        transaction.setTransactionType("Credit");
        transaction.setUserName(nameTeller(transaction.getUserId()));
        transaction.setRefundable(false);
        amountRepo.updateAmount((Integer) finalAmount,dateTimeGenerator(),transaction.getUserId());
        transaction.setTransactionNo((int)transactionRepo.count()+1);
        transactionRepo.save(transaction);
        Message message = new Message();
        message.setMessage(String.format("Your top-up of Rs. %d is done successfully, your balance is now Rs. %d.",transaction.getTransactionAmount(),transaction.getFinalAmount()));
        return message;
    }

    public Object pay(Transaction transaction) {
        Object finalAmount = transactionValidation(transaction,false);
        if(finalAmount.getClass()==ErrorMessage.class)
            return finalAmount;
        transaction.setFinalAmount((Integer) finalAmount);
        transaction.setDateAndTime(dateTimeGenerator());
        transaction.setTransactionId(idGenerator());
        transaction.setTransactionType("Debit");
        transaction.setUserName(nameTeller(transaction.getUserId()));
        transaction.setRefundable(true);
        amountRepo.updateAmount((Integer) finalAmount,dateTimeGenerator(),transaction.getUserId());
        transaction.setTransactionNo((int)transactionRepo.count()+1);
        transactionRepo.save(transaction);
        Message message = new Message();
        message.setMessage(String.format("Your payment of Rs. %d is done successfully, your balance is now Rs. %d.",transaction.getTransactionAmount(),transaction.getFinalAmount()));
        return message;
    }

    public List<Object> showPassbook(Integer userId) {
        String fName = isUserPresent(userId);
        if(fName==null) {
        List<ErrorMessage> passbook = new ArrayList<ErrorMessage>();
        ErrorMessage err = new ErrorMessage();
        err.setErrorMessage("The user doesn't exist");
        passbook.add(err);
        return Collections.singletonList(passbook);
        }
        List<Transaction> passbook =transactionRepo.findAllByUserId(userId);
        return Collections.singletonList(passbook);
    }



    public Object refund(Refund refund) {
        String transactionId = refund.getTransactionId();
        Object validation =refundValidation(transactionId);
        if(validation!=null)
            return validation;
        Transaction previousTransaction = findByTransactionId(transactionId);
        Integer transactionAmount = previousTransaction.getTransactionAmount();
        transactionRepo.setNonRefundable(transactionId);
        Transaction newTransaction = new Transaction();
        newTransaction.setRefundable(false);
        newTransaction.setUserName(previousTransaction.getUserName());
        newTransaction.setTransactionType("Credit");
        newTransaction.setTransactionId(idGenerator());
        newTransaction.setDateAndTime(dateTimeGenerator());
        newTransaction.setFinalAmount(previousTransaction.getFinalAmount()+transactionAmount);
        newTransaction.setSecondParty(previousTransaction.getSecondParty());
        newTransaction.setUserId(previousTransaction.getUserId());
        newTransaction.setTransactionAmount(transactionAmount);
        amountRepo.updateAmount(previousTransaction.getFinalAmount()+transactionAmount,dateTimeGenerator(),previousTransaction.getUserId());
        newTransaction.setTransactionNo((int) (transactionRepo.count() +1));
        transactionRepo.save(newTransaction);
        Message message = new Message();
        message.setMessage(String.format("Your refund of Rs. %d is done successfully, your balance is now Rs. %d.",transactionAmount,newTransaction.getFinalAmount()));
        return message;
    }



    public Object balanceChecking(Integer userId) {
        String fName = isUserPresent(userId);
        ErrorMessage err = new ErrorMessage();
        if(fName==null)
        {
            err.setErrorMessage("Invalid user id!");
            return err;
        }
        Integer finalAmount = balanceTeller(userId);
        {
            Message message = new Message();
            message.setMessage(String.format("Your balance is Rs. %d.",finalAmount));
            return message;
        }
    }


    public Object p2p(P2P p2p) {
        Integer creditorNo = p2p.getCreditorNo();
        Integer debitorNo = p2p.getDebitorNo();
        Integer amount = p2p.getAmount();
        String fName = isUserPresent(debitorNo);
        ErrorMessage err = new ErrorMessage();
        if(fName==null) {
            err.setErrorMessage("The debitor's account does not exist!");
            return err;
        }
        fName = isUserPresent(creditorNo);
        if(fName==null) {
            err.setErrorMessage("The creditor's account does not exist!");
            return err;
        }
        Integer debitorBalance = balanceTeller(debitorNo);
        if(debitorBalance<amount) {
            err.setErrorMessage("Insufficient balance!");
            return err;
        }
        Transaction debitorTransaction = new Transaction();
        Transaction creditorTransaction = new Transaction();
        debitorTransaction.setRefundable(false);
        debitorTransaction.setUserName(nameTeller(debitorNo));
        debitorTransaction.setTransactionType("Debit");
        debitorTransaction.setTransactionId(idGenerator());
        debitorTransaction.setDateAndTime(dateTimeGenerator());
        debitorTransaction.setFinalAmount(debitorBalance-amount);
        debitorTransaction.setSecondParty(nameTeller(creditorNo));
        debitorTransaction.setUserId(debitorNo);
        debitorTransaction.setTransactionAmount(amount);
        Integer creditorBalance = balanceTeller(creditorNo);
        creditorTransaction.setRefundable(false);
        creditorTransaction.setUserName(nameTeller(creditorNo));
        creditorTransaction.setTransactionType("Credit");
        creditorTransaction.setTransactionId(debitorTransaction.getTransactionId());
        creditorTransaction.setDateAndTime(dateTimeGenerator());
        creditorTransaction.setFinalAmount(creditorBalance+amount);
        creditorTransaction.setSecondParty(nameTeller(debitorNo));
        creditorTransaction.setUserId(creditorNo);
        creditorTransaction.setTransactionAmount(amount);
        debitorTransaction.setTransactionNo((int)transactionRepo.count()+1);
        transactionRepo.save(debitorTransaction);
        creditorTransaction.setTransactionNo((int)transactionRepo.count()+1);
        transactionRepo.save(creditorTransaction);
        amountRepo.updateAmount(debitorBalance-amount,dateTimeGenerator(),debitorNo);
        amountRepo.updateAmount(creditorBalance+amount,dateTimeGenerator(),creditorNo);
        Message message = new Message();
        message.setMessage(String.format("Rs. %d paid to %s, your balance is now Rs. %d",amount,debitorTransaction.getSecondParty(),debitorTransaction.getFinalAmount()));
        return message;
    }

    public Object userValidation(User user, boolean flag) {
        String mobileNumber= user.getMobileNumber();
        ErrorMessage err = new ErrorMessage();
        if(mobileNumber ==null )
        {
            err.setErrorMessage("Mobile number can't be null!");
            return err;
        }
        if(mobileNumber.length()!=10)
        {
            err.setErrorMessage("Invalid mobile number!");
            return err;
        }
        try {
            Long l = Long.parseLong(mobileNumber);

        } catch (NumberFormatException nfe) {
            err.setErrorMessage("Invalid mobile number!");
            return err;
        }
        Long l = Long.parseLong(mobileNumber);
        if(l<=0)
        {
            err.setErrorMessage("Invalid mobile number!");
            return err;
        }
        if(user.getEmail()==null) {
            err.setErrorMessage("Email cannot cannot be null!");
            return err;
        }
        if(user.getUserPassword()==null) {
            err.setErrorMessage("Password cannot be null!");
            return err;
        }
        if(user.getFirstName()==null) {
            err.setErrorMessage("First name cannot be null!");
            return err;
        }

        String fName = isMobileNumberPresent(user.getMobileNumber());

        if(fName!=null&&flag==true)
        {
            err.setErrorMessage("Your account already exists!");
            return err;
        }
        if(fName==null&&flag==false)
        {
            err.setErrorMessage("Your account doesn't exist!");
            return err;
        }
        return null;
    }


    public Object transactionValidation(Transaction transaction, boolean flag)
    {
        String fName = isUserPresent(transaction.getUserId());
        ErrorMessage err = new ErrorMessage();
        if(fName==null) {
            err.setErrorMessage("The account does not exist!");
            return err;
        }
        if(transaction.getTransactionAmount()<=0) {
            err.setErrorMessage("Your transaction amount is invalid!");
            return err;
        }
        Integer finalAmount = balanceTeller(transaction.getUserId());
        if(flag==true)
            return finalAmount+transaction.getTransactionAmount();
        else{
                if(finalAmount<transaction.getTransactionAmount()) {
                    {
                        err.setErrorMessage("Insufficient balance!");
                        return err;
                    }
                }
                    return finalAmount-transaction.getTransactionAmount();
        }
    }
    public String idGenerator() {
        String dateTime =dateTimeGenerator();
        String ENC_ALGO = "SHA";
        byte[] unencodedString = dateTime.getBytes();
        MessageDigest md = null;
        try {
            // first create an instance, given the provider
            md = MessageDigest.getInstance(ENC_ALGO);
        } catch (Exception e) {
            //log.error("Exception: " + e);
            return dateTime;
        }
        md.reset();
        // call the update method one or more times
        // (useful when you don't know the size of your data, eg. stream)
        md.update(unencodedString);
        // now calculate the hash
        byte[] encodedString = md.digest();
        StringBuilder buf = new StringBuilder();
        for (Integer i = 0; i < encodedString.length; i++) {
            if ((encodedString[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString(encodedString[i] & 0xff, 16));
        }
        return buf.toString();
    }
    public String dateTimeGenerator() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateTime = dtf.format(now);
        return dateTime;
    }

    public Object refundValidation(String transactionId) {
        ErrorMessage err = new ErrorMessage();
        try {
            boolean isRefundable = checkRefund(transactionId);
        }
        catch(Throwable e)
        {
            err.setErrorMessage("The transaction is not valid!");
            return err;
        }
        boolean isRefundable = checkRefund(transactionId);
        if(isRefundable==false) {
            err.setErrorMessage("The transaction is non-refundable!");
            return err;
        }
        return null;
    }

    private Boolean checkRefund(String transactionId) {
        Collection <Boolean> checkRefund=transactionRepo.checkRefund(transactionId);
        return checkRefund.iterator().next();
    }

    private String nameTeller(Integer userId) {
        Collection <String> name=userRepo.nameTeller(userId);
        return name.iterator().next();
    }

    private String isUserPresent(Integer userId) {
        Collection<String> isUserPresent=userRepo.isUserPresent(userId);
        if(isUserPresent.size()==0)
            return null;
        else
            return isUserPresent.iterator().next();
    }

    private String isMobileNumberPresent(String mobileNumber) {
        Collection<String> isMobileNumberPresent=userRepo.isMobileNumberPresent(mobileNumber);
        if(isMobileNumberPresent.size()==0)
            return null;
        else
        return isMobileNumberPresent.iterator().next();
    }

    private Transaction findByTransactionId(String transactionId) {
        Collection<Transaction> findByTransactionId=transactionRepo.findByTransactionId(transactionId);
        return findByTransactionId.iterator().next();
    }

    private Integer balanceTeller(Integer userId) {
        Collection<Integer> balanceTeller= amountRepo.balanceTeller(userId);
        return balanceTeller.iterator().next();
    }

    private Integer userNoFetcher(String mobileNumber) {
        Collection<Integer> userNoFetcher=userRepo.userNoFetcher(mobileNumber);
        return userNoFetcher.iterator().next();
    }

    private String createdFetcher(String mobileNumber) {
        Collection<String> createdFetcher=userRepo.createdFetcher(mobileNumber);
        return createdFetcher.iterator().next();
    }

}