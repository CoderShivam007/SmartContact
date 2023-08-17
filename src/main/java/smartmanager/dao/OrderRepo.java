/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package smartmanager.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import smartmanager.entities.PaymentOrder;

/**
 *
 * @author Shivam Gupta
 */
public interface OrderRepo extends JpaRepository<PaymentOrder, Long>
{
    public PaymentOrder findByOrderid(String orderid);
}
