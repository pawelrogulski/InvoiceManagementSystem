package progulski.invoicemanagmentsystem.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "items")
public class Items {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JoinColumn(name="invoice_id")
    private Invoice invoice;
    private String name;
    private float price;
    private int quantity;
    private int tax;
    private float totalPrice = price*quantity/(100-tax);
}
