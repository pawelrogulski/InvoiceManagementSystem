package progulski.invoicemanagmentsystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="invoice_id")
    private Invoice invoice;
    private String name;
    private float price;
    private int quantity;
    private int tax;
    private float totalPrice;

    public Item(String name, float price, int quantity, int tax) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.tax = tax;
        this.totalPrice = this.price*this.quantity*(100+this.tax)/100;
    }


    public void setTotalPrice() {
        this.totalPrice = this.price*this.quantity*(100+this.tax)/100;
    }
}
