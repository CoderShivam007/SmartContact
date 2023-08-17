package smartmanager.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class PaymentOrder 
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long oid;
    
    private String orderid;
    private String amount;
    private String receipt;
    private String status;
    
    @ManyToOne
    private User user;
    
    private String paymentid;

    public PaymentOrder(Long oid, String orderid, String amount, String receipt, String status, User user, String paymentid) {
        this.oid = oid;
        this.orderid = orderid;
        this.amount = amount;
        this.receipt = receipt;
        this.status = status;
        this.user = user;
        this.paymentid = paymentid;
    }
    
    public PaymentOrder() {
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }


    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPaymentid() {
        return paymentid;
    }

    public void setPaymentid(String paymentid) {
        this.paymentid = paymentid;
    }

    @Override
    public String toString() {
        return "PaymentOrder{" + "oid=" + oid + ", orderid=" + orderid + ", amount=" + amount + ", receipt=" + receipt + ", status=" + status + ", user=" + user + ", paymentid=" + paymentid + '}';
    }

    
    
    
    
    
}
