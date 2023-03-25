package progulski.invoicemanagementsystem.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private LocalDate date;
    private String city;
    private String companyName;
    private int NIP;
    @OneToMany(mappedBy = "invoice")
    private Set<Item> items;
    private float totalPrice;

    public Invoice(LocalDate date, String city, String companyName, int NIP, Set<Item> items) {
        this.date = date;
        this.city = city;
        this.companyName = companyName;
        this.NIP = NIP;
        this.items = items;
        this.totalPrice= items.stream()
                .map(item -> item.getTotalPrice())
                .reduce(0F,Float::sum);
    }

    public void setTotalPrice() {
        this.totalPrice= items.stream()
                .map(item -> item.getTotalPrice())
                .reduce(0F,Float::sum);
    }
}
